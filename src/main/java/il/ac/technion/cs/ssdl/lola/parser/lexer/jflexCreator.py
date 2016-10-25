import re
import sys
import os
import xml.etree.ElementTree as et
from collections import OrderedDict

# the key is the name of the defined regular expression, and the value is a 
# string  in the flex pattern format. this is used for the regular expression
# definitions part of the .lex file.
regex_defs = OrderedDict()
escape_character = '#'


# the key is the name of the defined token, and the value is an instance of the
# token class.
class token:
    """
    a class describing a defined token.
    
    self.flex: a string  in the flex pattern format. this is used for the token 
               definitions part of the .lex file.
    self.method: if exists it is a string corresponding to the content of the 
               <method> attribute of <token>.
    self.ignored: true if the token should be ignored, false otherwise. ignored
                  token are comments, white-spaces, etc. false is a default if 
                  ignored was not defined. 
    """
    pass


token_defs = OrderedDict()

# the key is the name of the defined class (defined using <classification>), 
# and the value is a set of the tokens (flattened) that are used to compose
# this class (should make sure there are no circle references).
class_defs = OrderedDict()

# the key is the name of the begin token, and the value is the name of the end.
# no classes (classifications) are allowed.
blnce_defs = OrderedDict()

# the key is the name of the from token, and the value is the name of the to 
# token. no classes (classifications) are allowed.
trsnl_defs = OrderedDict()


def apply_to_inner_element(choices, element):
    """checks the access to the inner element"""
    inner = next(item for item in (element.find(choice) for choice in choices.keys()) if item is not None)
    return choices[inner.tag](inner)


def add_member_function(cls, name=None):
    """
    this decorator adds the decorated function as a member function to the 
    specified class. the name, if not specified (and name is None), is
    set to be the __name__ of the decorated function.  
    """

    def real_add_member_function(fn):
        lname = name
        if lname is None:
            lname = fn.__name__
        setattr(cls, lname, fn)
        return fn

    return real_add_member_function


@add_member_function(et.Element)
def findalltext(self, match, default=None):
    return [(e.text if e is not None else default) for e in self.findall(match)]


###############################################################################
# 2flex                                                                       #
###############################################################################


def or2flex(o):
    return '(' + '|'.join(pattern2flex(p) for p in o) + ')'


def sequence2flex(o):
    return '(' + ''.join(pattern2flex(p) for p in o) + ')'


def char2flex(s):
    return '(' + {
        "horizontal tab": '\\t',
        "new line": '\\n',
        "vertical tab": '\\v',
        "form feed": '\\f',
        "carriage return": '\\r',
        "space": '" "',
        "carriage return new line": '\\r\\n'
    }[s.text] + ')'


def range2flex(r):
    start = r.findtext('range-from')
    end = r.findtext('range-to')
    # TODO somehow make sure that rs and re are valid (e.g., alphabet), and escape it
    return '([' + start + '-' + end + '])'


def assert_valid_flex_definition_name(n):
    if not re.match('[a-zA-Z0-9_][a-zA-Z0-9_\-]*', n):
        raise Exception("a reference must begin with a letter or an underscore ('_')"
                        " followed by zero or more letters, digits, '_', or '-' (dash)")


def ref2flex(r):
    n = r.text
    assert_valid_flex_definition_name(n)
    return '({' + n + '})'


# def escape2flex(v):
#     #TODO is it also for upper case?
#     #return ''.join((c if c in ['a', 'b', 'f', 'n', 'r', 't', 'v'] else '\\' + c) for c in v)
#     return '"' + v.replace('\\', '\\\\').replace('"', '\\"') + '"'
#

def escape_backslash(v):
    return v


def escape(v):
    tmp = '\\'
    return v.replace('"\\"', '"\\\\"').replace('"""', '"' + tmp + '""')


def string2flex(s):
    v = s.findtext('value', '')
    case = s.findtext('case', 'sensitive')
    if case == 'insensitive':
        ci = '(' + '[' + v + v.capitalize() + ']' + ')'
    else:
        ci = '(' + escape('"' + v + '"') + ')'
    return ci


def negation2flex(n):
    v = n.findtext('value')
    # TODO should escape v better
    return '[^' + v.replace('\\', '\\\\').replace(']', '\\]').replace('"', '\\"') + ']'


def pattern2flex(p):
    occurrences = p.findtext('occurrences', '')
    return '(' + \
           apply_to_inner_element({
               'or': or2flex,
               'sequence': sequence2flex,
               'range': range2flex,
               'string': string2flex,
               'special-character': char2flex,
               'regular-expression-reference': ref2flex,
               'character-negation': negation2flex,
           }, p) + ({
                        # left is the text of the XML attribute, right is the flex symbol
                        '': '',  # once
                        '+': '+',  # onceOrMore
                        '*': '*',  # noneOrMore
                        '?': '?',  # noneOrOnce
                    }[occurrences]) + ')'


###############################################################################
# parse                                                                       #
###############################################################################

def is_name_exists(name):
    return (name in regex_defs) or (name in class_defs) or (name in token_defs)


def assert_name_exists(name):
    if is_name_exists(name):
        return

    print ('error: reference to undefined name ' + name + '. ' +
           'execution aborted.')
    exit(1)


def assert_name_not_exists(name):
    if not is_name_exists(name):
        return

    print ('error: multiple definitions of name ' + name + '. ' +
           'execution aborted.')
    exit(1)


def assert_names_exists(names):
    for name in names:
        assert_name_exists(name)


def parse_regular_expression_definition(d):
    name = d.find('regular-expression-name').text
    assert_valid_flex_definition_name(name)

    # add an entry for the regular expression to the regular expression 
    # definitions dictionary.
    assert_name_not_exists(name)
    regex_defs[name] = pattern2flex(d.find('regular-expression'))


def parse_token(t):
    name = t.findtext('token-name')
    assert_valid_flex_definition_name(name)

    # add an entry for the regular expression to the regular expression 
    # definitions dictionary.
    assert_name_not_exists(name)

    tkn = token()
    tkn.name = name
    tkn.method = t.findtext('method')
    tkn.ignored = bool(t.findtext('ignored'))
    tkn.flex = pattern2flex(t.find('regular-expression'))

    token_defs[name] = tkn


def flatten_refs(refs):
    """
    flatten the refs to include only tokens. if a class in the list, take 
    the tokens of the class.
    """
    l = []
    for name in refs:
        if name in token_defs:
            l.append(name)
        else:
            l.extend(flatten_refs(class_defs[name]))
    return set(l)


def parse_classification(c):
    name = c.findtext('token-name')
    refs = c.findalltext('token-name-reference')
    assert_valid_flex_definition_name(name)

    # add an entry for the regular expression to the regular expression 
    # definitions dictionary.
    assert_name_not_exists(name)
    assert_names_exists(refs)
    class_defs[name] = flatten_refs(refs)


def parse_balance(b):
    balance_begin = b.find('begin').findtext('token-name-reference')
    balance_end = b.find('end').findtext('token-name-reference')

    if balance_begin not in token_defs:
        print ('error: ' + balance_begin + ' is not a token. ' +
               'execution aborted.')
        exit(1)
    if balance_end not in token_defs:
        print ('error: ' + balance_end + ' is not a token. ' +
               'execution aborted.')
        exit(1)

    if balance_begin in blnce_defs:
        print ('error: multiple definitions of balance ' + balance_begin + '. ' +
               'execution aborted.')
        exit(1)
    elif balance_end in blnce_defs.values():
        print ('error: multiple definitions of balance ' + balance_end + '. ' +
               'execution aborted.')
        exit(1)

    blnce_defs[balance_begin] = balance_end


def parse_translate(t):
    translate_from = t.find('from').findalltext('token-name-reference')
    translate_to = t.find('to').findtext('token-name-reference')

    if translate_to not in token_defs:
        print ('error: ' + translate_to + ' is not a token.' +
               'execution aborted.')
        exit(1)

    ignored = token_defs[translate_to].ignored
    method = token_defs[translate_to].method

    for t in translate_from:
        if t not in token_defs:
            print ('error: ' + t + ' is not a token.' +
                   'execution aborted.')
            exit(1)

        if token_defs[t].ignored is not ignored:
            print ('error: ' + t + ' must have the same ignored value as ' +
                   translate_to + ' to be a translate.' +
                   'execution aborted.')
            exit(1)

        if token_defs[t].method is not method:
            print ('error: ' + t + ' must have the same method value as ' +
                   translate_to + ' to be a translate.' +
                   'execution aborted.')
            exit(1)

        trsnl_defs[t] = translate_to


def parse_lola_escape_character(e):
    global escape_character
    escape_character = e.findtext('value')


def parse_config_element(e):
    apply_to_inner_element({
        'regular-expression-definition': parse_regular_expression_definition,
        'token': parse_token,
        'classification': parse_classification,
        'balance': parse_balance,
        'translate': parse_translate,
        'lola-escape-character': parse_lola_escape_character
    }, e)


###############################################################################
# from now on is the actual execution of the program.                         #
###############################################################################
if __name__ != "__main__":
    print 'this module is not designed to work as a non main module.'
    exit(1)

if len(sys.argv) is not 2:
    print 'usage is "jflexCreator.py config-file.xml"'
    exit(1)

# we just assume that the XML file follows the schema.
# TODO: verify it.

# get the root element of the specified XML file.
root = et.parse(sys.argv[1]).getroot()

# for each <configuration-element> in the XML file.
for config_element in root.getchildren():
    parse_config_element(config_element)


###############################################################################
# We create 4 and a half files:                                               #
###############################################################################
def str2enum(text):
    """returns a string that can be used as enum element name."""
    # TODO: check for legal identifier, check not a keyword
    return '___' + text + '___'


def str2string(text):
    """returns a string that can be used as a string literal."""
    # TODO: escape string
    return '"' + text + '"'


lola_atomic = ['Splice', 'Nothing']
lola_with_non_surrounded_snippet = ['Import', 'Include']
lola_constructors = ['If', 'Unless', 'Case', 'ForEach']
lola_constructors_elaborators = ['else', 'elseIf', 'of', 'otherwise', 'ifNone', 'separator']
lola_re_atomic = ['SomeIdentifier', 'Empty', 'Any', 'EndOfFile', 'NewLine', 'BeginningOfLine', 'EndOfLine']
lola_re_atomic_elaborators = ['without']
lola_Re_constructors_traditional = ['Sequence', 'Optional', 'OneOrMore', 'NoneOrMore', 'Either']
lola_Re_constructors_traditional_elaborators = ['followedBy', 'separator', 'opener', 'closer', 'ifNone', 'or']
lola_Re_constructors_power = ['Xither', 'Neither', 'Match', 'SubsetOf', 'NonEmptySubsetOf', 'ProperSubsetOf',
                              'PermutationOf', 'Not']
lola_Re_constructors_power_elaborators = ['xor', 'nor', 'andAlso', 'exceptFor', 'and', 'with']
lola_Re_constructors_modifying = ['SameLine', 'Unbalanced']
lola_declarative_headers = ['Find', 'find', 'description', 'note']
lola_Actions = ['run', 'replace', 'delete', 'append', 'prepend', 'filter', 'anchor', 'assert']
lola_declarative_footers = ['example', 'log', 'see']
lola_declarative_footers_elaborators = ['resultsIn']
lola_more = ['Identifier', 'Literal']
lola_keywords = list(set(lola_atomic + lola_constructors + lola_constructors_elaborators + lola_re_atomic + \
                         lola_re_atomic_elaborators + lola_Re_constructors_traditional + \
                         lola_Re_constructors_traditional_elaborators + lola_Re_constructors_power + \
                         lola_Re_constructors_power_elaborators + lola_Re_constructors_modifying + \
                         lola_declarative_headers + lola_declarative_headers + lola_Actions + lola_declarative_footers + \
                         lola_declarative_footers_elaborators + lola_more + lola_with_non_surrounded_snippet))

# jlex.part2.autogenerated
# TODO: must check that user definitions and my 
#       definition do not clash
FILENAME = 'jflex.part2.autogenerated'
with open(FILENAME, 'w') as f:
    f.write('public final static String lolaEscapingCharacter = "' + escape_character + '";\n')
    f.write('\tpublic static void initialize() throws CycleFoundException {\n')
    f.write('\t\tif(initialized)\n\t\t\treturn;\n')
    f.write('\t\tinitialized = true;\n')
    for key in token_defs.keys():
        if token_defs[key].ignored:
            f.write('\t\tCategoriesHierarchy.addTriviaCategory("' + key + '");\n')
        else:
            f.write('\t\tCategoriesHierarchy.addCategory("' + key + '");\n')
    #
    for kw in lola_keywords:  # add keywords
        f.write('\t\tCategoriesHierarchy.addKeywordCategory("' + escape_character + kw + '");\n')
    f.write('\t\tCategoriesHierarchy.addKeywordCategory("' + escape_character + '");\n')  # add lexi keyword
    for classification, sons in class_defs.iteritems():
        f.write('\t\tCategoriesHierarchy.addCategory("' + classification + '");\n')
        for son in sons:
            f.write('\t\tCategoriesHierarchy.addClassification("' + son + '", "' + classification + '");\n')
    f.write('\t\tCategoriesHierarchy.addKeywordCategory("lola_keyword");\n')
    f.write('\t\tCategoriesHierarchy.addCategory("package_snippet");\n')
    f.write('\t\tCategoriesHierarchy.addCategory("snippet");\n')
    f.write('\t\tCategoriesHierarchy.addTriviaCategory("enter");\n')
    for begin, end in blnce_defs.iteritems():
        f.write('\t\tBalancing.addBalancing(' + token_defs[begin].flex + ', ' + token_defs[end].flex + ');\n')
    f.write('\t}\n')
    f.write('%}\n')
    for key, val in regex_defs.items():
        f.write(key + ' = ' + val + '\n')

# parser.lex.part4.autogenerated
# TODO: must check that user rules and my 
#       rules do not clash
FILENAME = 'jflex.part4.autogenerated'
with open(FILENAME, 'w') as f:
    f.write('<YYINITIAL> ((\\t))    { Token $ = token("ignore"); yycolumn += (3 - yycolumn % 4); return $;}\n')
    for key, val in token_defs.items():
        f.write('<YYINITIAL>' + escape_backslash(val.flex) + '\t' + '{ return ' +
                'token("' + key + '"); }\n')
    # special cases:
    for kw in lola_with_non_surrounded_snippet:
        f.write(
            '<YYINITIAL>' + escape_character + kw + '[ ]((({L}+)[.])*({L}+))	{ return tokenWithSnippet("lola_keyword"); }\n')
    for kw in lola_keywords:
        f.write('<YYINITIAL>' + escape_character + kw + '\t' + '{ return ' +
                'token("' + escape_character + kw + '"); }\n')
    f.write('<YYINITIAL>' + escape_character + '({L}+)	{ return token("lola_keyword"); }\n')
    f.write('<YYINITIAL>' + escape_character + '\t' + '{ return ' +
            'token("' + escape_character + '"); }\n')
    f.write('<YYINITIAL>(\\r\\n)	{ return token("enter"); }\n')
    f.write('<YYINITIAL>((\\n)|(\\r))	{ return token("enter"); }\n')


# parser.lex
def concatenate_files(filenames, outpath):
    with open(outpath, 'w') as outfile:
        for fname in filenames:
            with open(fname) as infile:
                outfile.write(infile.read())


concatenate_files(
    filenames=[os.path.join(os.path.dirname(__file__), 'jflex.part1'),
               'jflex.part2.autogenerated',
               os.path.join(os.path.dirname(__file__), 'jflex.part3'),
               'jflex.part4.autogenerated',
               os.path.join(os.path.dirname(__file__), 'jflex.part5')
               ],
    outpath=os.path.join(os.path.dirname(__file__), '.', 'autogenerated.jflex'))
