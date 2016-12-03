package il.ac.technion.cs.ssdl.lola.parser.lexer;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import il.ac.technion.cs.ssdl.lola.parser.CategoriesHierarchy;
import il.ac.technion.cs.ssdl.lola.parser.Balancing;
import java_cup.runtime.Symbol;
import java.io.IOException;

%%

%class JflexLexer
%unicode
%cup
%line
%public
%column
%init{
  	try {
		initialize();
	} catch (CycleFoundException e) {
		e.printStackTrace();
	}
%init}

%{
	private Token token(String categoryName) {
		return new Token(yyline, yycolumn, yytext(), CategoriesHierarchy.getCategory(categoryName));
	}
	
	private Token tokenWithSnippet(String categoryName) {
		return new Token(yyline, yycolumn, yytext().split(" ", 2)[0], CategoriesHierarchy.getCategory(categoryName), yytext().split(" ", 2)[1]);
	}
	
	public Token next_token_safe() throws IOException {
		final Symbol $ = next_token();
		return $.sym == YYEOF ? null : (Token) $;
	}
	
	public class sym{
		public static final int EOF = -1;
	}
	
	private static boolean initialized = false;
	public final static String lolaEscapingCharacter = "##";
	public static void initialize() throws CycleFoundException {
		if(initialized)
			return;
		initialized = true;
		CategoriesHierarchy.addTriviaCategory("ignore");
		CategoriesHierarchy.addCategory("tridot");
		CategoriesHierarchy.addCategory("eqrsh");
		CategoriesHierarchy.addCategory("eqlsh");
		CategoriesHierarchy.addCategory("eqadd");
		CategoriesHierarchy.addCategory("eqmul");
		CategoriesHierarchy.addCategory("eqdiv");
		CategoriesHierarchy.addCategory("eqmod");
		CategoriesHierarchy.addCategory("eqand");
		CategoriesHierarchy.addCategory("eqor");
		CategoriesHierarchy.addCategory("eqxor");
		CategoriesHierarchy.addCategory("birsh");
		CategoriesHierarchy.addCategory("bilsh");
		CategoriesHierarchy.addCategory("plus");
		CategoriesHierarchy.addCategory("minus");
		CategoriesHierarchy.addCategory("uninc");
		CategoriesHierarchy.addCategory("undec");
		CategoriesHierarchy.addCategory("biandlogic");
		CategoriesHierarchy.addCategory("biorlogic");
		CategoriesHierarchy.addCategory("bilesseq");
		CategoriesHierarchy.addCategory("bigreatereq");
		CategoriesHierarchy.addCategory("bieq");
		CategoriesHierarchy.addCategory("bineq");
		CategoriesHierarchy.addCategory("or");
		CategoriesHierarchy.addCategory("bixor");
		CategoriesHierarchy.addCategory("bimod");
		CategoriesHierarchy.addCategory("bigreater");
		CategoriesHierarchy.addCategory("biless");
		CategoriesHierarchy.addCategory("bidiv");
		CategoriesHierarchy.addCategory("star");
		CategoriesHierarchy.addCategory("amp");
		CategoriesHierarchy.addCategory("unnot");
		CategoriesHierarchy.addCategory("unneg");
		CategoriesHierarchy.addCategory("semicolon");
		CategoriesHierarchy.addCategory("comma");
		CategoriesHierarchy.addCategory("apostrophe");
		CategoriesHierarchy.addCategory("colon");
		CategoriesHierarchy.addCategory("dot");
		CategoriesHierarchy.addCategory("arrow");
		CategoriesHierarchy.addCategory("eq");
		CategoriesHierarchy.addCategory("ques");
		CategoriesHierarchy.addCategory("diopencurly");
		CategoriesHierarchy.addCategory("opencurly");
		CategoriesHierarchy.addCategory("diclosecurly");
		CategoriesHierarchy.addCategory("closecurly");
		CategoriesHierarchy.addCategory("openparen");
		CategoriesHierarchy.addCategory("closeparen");
		CategoriesHierarchy.addCategory("openbracket");
		CategoriesHierarchy.addCategory("closebracket");
		CategoriesHierarchy.addCategory("void");
		CategoriesHierarchy.addCategory("float");
		CategoriesHierarchy.addCategory("short");
		CategoriesHierarchy.addCategory("char");
		CategoriesHierarchy.addCategory("double");
		CategoriesHierarchy.addCategory("int");
		CategoriesHierarchy.addCategory("long");
		CategoriesHierarchy.addCategory("auto");
		CategoriesHierarchy.addCategory("const");
		CategoriesHierarchy.addCategory("extern");
		CategoriesHierarchy.addCategory("register");
		CategoriesHierarchy.addCategory("signed");
		CategoriesHierarchy.addCategory("static");
		CategoriesHierarchy.addCategory("unsigned");
		CategoriesHierarchy.addCategory("volatile");
		CategoriesHierarchy.addCategory("break");
		CategoriesHierarchy.addCategory("case");
		CategoriesHierarchy.addCategory("continue");
		CategoriesHierarchy.addCategory("default");
		CategoriesHierarchy.addCategory("do");
		CategoriesHierarchy.addCategory("else");
		CategoriesHierarchy.addCategory("for");
		CategoriesHierarchy.addCategory("goto");
		CategoriesHierarchy.addCategory("if");
		CategoriesHierarchy.addCategory("return");
		CategoriesHierarchy.addCategory("switch");
		CategoriesHierarchy.addCategory("while");
		CategoriesHierarchy.addCategory("enum");
		CategoriesHierarchy.addCategory("class");
		CategoriesHierarchy.addCategory("interface");
		CategoriesHierarchy.addCategory("typedef");
		CategoriesHierarchy.addCategory("sizeof");
		CategoriesHierarchy.addCategory("lhexadecimal");
		CategoriesHierarchy.addCategory("loctal");
		CategoriesHierarchy.addCategory("ldecimal");
		CategoriesHierarchy.addCategory("lfloat_form_a");
		CategoriesHierarchy.addCategory("lfloat_form_b");
		CategoriesHierarchy.addCategory("lfloat_form_c");
		CategoriesHierarchy.addCategory("lstring");
		CategoriesHierarchy.addCategory("identifier");
		CategoriesHierarchy.addCategory("character");
		CategoriesHierarchy.addCategory("sharp");
		CategoriesHierarchy.addKeywordCategory("##and");
		CategoriesHierarchy.addKeywordCategory("##anchor");
		CategoriesHierarchy.addKeywordCategory("##NewLine");
		CategoriesHierarchy.addKeywordCategory("##replace");
		CategoriesHierarchy.addKeywordCategory("##see");
		CategoriesHierarchy.addKeywordCategory("##SomeIdentifier");
		CategoriesHierarchy.addKeywordCategory("##run");
		CategoriesHierarchy.addKeywordCategory("##Neither");
		CategoriesHierarchy.addKeywordCategory("##Include");
		CategoriesHierarchy.addKeywordCategory("##find");
		CategoriesHierarchy.addKeywordCategory("##Empty");
		CategoriesHierarchy.addKeywordCategory("##Unbalanced");
		CategoriesHierarchy.addKeywordCategory("##closer");
		CategoriesHierarchy.addKeywordCategory("##exceptFor");
		CategoriesHierarchy.addKeywordCategory("##delete");
		CategoriesHierarchy.addKeywordCategory("##Unless");
		CategoriesHierarchy.addKeywordCategory("##andAlso");
		CategoriesHierarchy.addKeywordCategory("##Import");
		CategoriesHierarchy.addKeywordCategory("##log");
		CategoriesHierarchy.addKeywordCategory("##Find");
		CategoriesHierarchy.addKeywordCategory("##prepend");
		CategoriesHierarchy.addKeywordCategory("##note");
		CategoriesHierarchy.addKeywordCategory("##opener");
		CategoriesHierarchy.addKeywordCategory("##Match");
		CategoriesHierarchy.addKeywordCategory("##Splice");
		CategoriesHierarchy.addKeywordCategory("##example");
		CategoriesHierarchy.addKeywordCategory("##Nothing");
		CategoriesHierarchy.addKeywordCategory("##SameLine");
		CategoriesHierarchy.addKeywordCategory("##PermutationOf");
		CategoriesHierarchy.addKeywordCategory("##Optional");
		CategoriesHierarchy.addKeywordCategory("##Case");
		CategoriesHierarchy.addKeywordCategory("##followedBy");
		CategoriesHierarchy.addKeywordCategory("##xor");
		CategoriesHierarchy.addKeywordCategory("##ProperSubsetOf");
		CategoriesHierarchy.addKeywordCategory("##description");
		CategoriesHierarchy.addKeywordCategory("##resultsIn");
		CategoriesHierarchy.addKeywordCategory("##append");
		CategoriesHierarchy.addKeywordCategory("##else");
		CategoriesHierarchy.addKeywordCategory("##assert");
		CategoriesHierarchy.addKeywordCategory("##Not");
		CategoriesHierarchy.addKeywordCategory("##Xither");
		CategoriesHierarchy.addKeywordCategory("##EndOfFile");
		CategoriesHierarchy.addKeywordCategory("##NoneOrMore");
		CategoriesHierarchy.addKeywordCategory("##Either");
		CategoriesHierarchy.addKeywordCategory("##If");
		CategoriesHierarchy.addKeywordCategory("##elseIf");
		CategoriesHierarchy.addKeywordCategory("##SubsetOf");
		CategoriesHierarchy.addKeywordCategory("##Literal");
		CategoriesHierarchy.addKeywordCategory("##Identifier");
		CategoriesHierarchy.addKeywordCategory("##with");
		CategoriesHierarchy.addKeywordCategory("##nor");
		CategoriesHierarchy.addKeywordCategory("##BeginningOfLine");
		CategoriesHierarchy.addKeywordCategory("##NonEmptySubsetOf");
		CategoriesHierarchy.addKeywordCategory("##ifNone");
		CategoriesHierarchy.addKeywordCategory("##Sequence");
		CategoriesHierarchy.addKeywordCategory("##of");
		CategoriesHierarchy.addKeywordCategory("##OneOrMore");
		CategoriesHierarchy.addKeywordCategory("##or");
		CategoriesHierarchy.addKeywordCategory("##filter");
		CategoriesHierarchy.addKeywordCategory("##without");
		CategoriesHierarchy.addKeywordCategory("##separator");
		CategoriesHierarchy.addKeywordCategory("##ForEach");
		CategoriesHierarchy.addKeywordCategory("##otherwise");
		CategoriesHierarchy.addKeywordCategory("##Any");
		CategoriesHierarchy.addKeywordCategory("##EndOfLine");
		CategoriesHierarchy.addKeywordCategory("##");
		CategoriesHierarchy.addCategory("eqop");
		CategoriesHierarchy.addClassification("eqor", "eqop");
		CategoriesHierarchy.addClassification("eqdiv", "eqop");
		CategoriesHierarchy.addClassification("eqrsh", "eqop");
		CategoriesHierarchy.addClassification("eqxor", "eqop");
		CategoriesHierarchy.addClassification("eqlsh", "eqop");
		CategoriesHierarchy.addClassification("eqmul", "eqop");
		CategoriesHierarchy.addClassification("eqand", "eqop");
		CategoriesHierarchy.addClassification("eqadd", "eqop");
		CategoriesHierarchy.addClassification("eqmod", "eqop");
		CategoriesHierarchy.addCategory("biop");
		CategoriesHierarchy.addClassification("bigreater", "biop");
		CategoriesHierarchy.addClassification("or", "biop");
		CategoriesHierarchy.addClassification("star", "biop");
		CategoriesHierarchy.addClassification("biless", "biop");
		CategoriesHierarchy.addClassification("bilesseq", "biop");
		CategoriesHierarchy.addClassification("birsh", "biop");
		CategoriesHierarchy.addClassification("bixor", "biop");
		CategoriesHierarchy.addClassification("bimod", "biop");
		CategoriesHierarchy.addClassification("bieq", "biop");
		CategoriesHierarchy.addClassification("bilsh", "biop");
		CategoriesHierarchy.addClassification("bineq", "biop");
		CategoriesHierarchy.addClassification("bidiv", "biop");
		CategoriesHierarchy.addClassification("plus", "biop");
		CategoriesHierarchy.addClassification("bigreatereq", "biop");
		CategoriesHierarchy.addClassification("amp", "biop");
		CategoriesHierarchy.addClassification("minus", "biop");
		CategoriesHierarchy.addClassification("biandlogic", "biop");
		CategoriesHierarchy.addClassification("biorlogic", "biop");
		CategoriesHierarchy.addCategory("unop");
		CategoriesHierarchy.addClassification("star", "unop");
		CategoriesHierarchy.addClassification("uninc", "unop");
		CategoriesHierarchy.addClassification("undec", "unop");
		CategoriesHierarchy.addClassification("plus", "unop");
		CategoriesHierarchy.addClassification("unnot", "unop");
		CategoriesHierarchy.addClassification("unneg", "unop");
		CategoriesHierarchy.addClassification("minus", "unop");
		CategoriesHierarchy.addCategory("punctuation");
		CategoriesHierarchy.addClassification("semicolon", "punctuation");
		CategoriesHierarchy.addClassification("tridot", "punctuation");
		CategoriesHierarchy.addClassification("ques", "punctuation");
		CategoriesHierarchy.addClassification("comma", "punctuation");
		CategoriesHierarchy.addClassification("arrow", "punctuation");
		CategoriesHierarchy.addClassification("amp", "punctuation");
		CategoriesHierarchy.addClassification("apostrophe", "punctuation");
		CategoriesHierarchy.addClassification("colon", "punctuation");
		CategoriesHierarchy.addClassification("eq", "punctuation");
		CategoriesHierarchy.addClassification("dot", "punctuation");
		CategoriesHierarchy.addCategory("JavaType");
		CategoriesHierarchy.addClassification("short", "JavaType");
		CategoriesHierarchy.addClassification("int", "JavaType");
		CategoriesHierarchy.addClassification("double", "JavaType");
		CategoriesHierarchy.addClassification("void", "JavaType");
		CategoriesHierarchy.addClassification("float", "JavaType");
		CategoriesHierarchy.addClassification("long", "JavaType");
		CategoriesHierarchy.addClassification("char", "JavaType");
		CategoriesHierarchy.addCategory("qualifier");
		CategoriesHierarchy.addClassification("const", "qualifier");
		CategoriesHierarchy.addClassification("auto", "qualifier");
		CategoriesHierarchy.addClassification("register", "qualifier");
		CategoriesHierarchy.addClassification("unsigned", "qualifier");
		CategoriesHierarchy.addClassification("signed", "qualifier");
		CategoriesHierarchy.addClassification("static", "qualifier");
		CategoriesHierarchy.addClassification("extern", "qualifier");
		CategoriesHierarchy.addClassification("volatile", "qualifier");
		CategoriesHierarchy.addCategory("ctrlflow");
		CategoriesHierarchy.addClassification("case", "ctrlflow");
		CategoriesHierarchy.addClassification("do", "ctrlflow");
		CategoriesHierarchy.addClassification("return", "ctrlflow");
		CategoriesHierarchy.addClassification("goto", "ctrlflow");
		CategoriesHierarchy.addClassification("for", "ctrlflow");
		CategoriesHierarchy.addClassification("default", "ctrlflow");
		CategoriesHierarchy.addClassification("while", "ctrlflow");
		CategoriesHierarchy.addClassification("else", "ctrlflow");
		CategoriesHierarchy.addClassification("break", "ctrlflow");
		CategoriesHierarchy.addClassification("switch", "ctrlflow");
		CategoriesHierarchy.addClassification("continue", "ctrlflow");
		CategoriesHierarchy.addClassification("if", "ctrlflow");
		CategoriesHierarchy.addCategory("TypeKeyword");
		CategoriesHierarchy.addClassification("interface", "TypeKeyword");
		CategoriesHierarchy.addClassification("enum", "TypeKeyword");
		CategoriesHierarchy.addClassification("class", "TypeKeyword");
		CategoriesHierarchy.addCategory("keyword");
		CategoriesHierarchy.addClassification("int", "keyword");
		CategoriesHierarchy.addClassification("float", "keyword");
		CategoriesHierarchy.addClassification("while", "keyword");
		CategoriesHierarchy.addClassification("char", "keyword");
		CategoriesHierarchy.addClassification("interface", "keyword");
		CategoriesHierarchy.addClassification("static", "keyword");
		CategoriesHierarchy.addClassification("sizeof", "keyword");
		CategoriesHierarchy.addClassification("if", "keyword");
		CategoriesHierarchy.addClassification("const", "keyword");
		CategoriesHierarchy.addClassification("for", "keyword");
		CategoriesHierarchy.addClassification("unsigned", "keyword");
		CategoriesHierarchy.addClassification("long", "keyword");
		CategoriesHierarchy.addClassification("volatile", "keyword");
		CategoriesHierarchy.addClassification("do", "keyword");
		CategoriesHierarchy.addClassification("return", "keyword");
		CategoriesHierarchy.addClassification("goto", "keyword");
		CategoriesHierarchy.addClassification("auto", "keyword");
		CategoriesHierarchy.addClassification("void", "keyword");
		CategoriesHierarchy.addClassification("enum", "keyword");
		CategoriesHierarchy.addClassification("else", "keyword");
		CategoriesHierarchy.addClassification("break", "keyword");
		CategoriesHierarchy.addClassification("extern", "keyword");
		CategoriesHierarchy.addClassification("class", "keyword");
		CategoriesHierarchy.addClassification("case", "keyword");
		CategoriesHierarchy.addClassification("short", "keyword");
		CategoriesHierarchy.addClassification("default", "keyword");
		CategoriesHierarchy.addClassification("double", "keyword");
		CategoriesHierarchy.addClassification("register", "keyword");
		CategoriesHierarchy.addClassification("signed", "keyword");
		CategoriesHierarchy.addClassification("switch", "keyword");
		CategoriesHierarchy.addClassification("continue", "keyword");
		CategoriesHierarchy.addCategory("linteger");
		CategoriesHierarchy.addClassification("lhexadecimal", "linteger");
		CategoriesHierarchy.addClassification("ldecimal", "linteger");
		CategoriesHierarchy.addClassification("loctal", "linteger");
		CategoriesHierarchy.addCategory("lfloat");
		CategoriesHierarchy.addClassification("lfloat_form_c", "lfloat");
		CategoriesHierarchy.addClassification("lfloat_form_b", "lfloat");
		CategoriesHierarchy.addClassification("lfloat_form_a", "lfloat");
		CategoriesHierarchy.addCategory("literal");
		CategoriesHierarchy.addClassification("character", "literal");
		CategoriesHierarchy.addClassification("loctal", "literal");
		CategoriesHierarchy.addClassification("lstring", "literal");
		CategoriesHierarchy.addClassification("lhexadecimal", "literal");
		CategoriesHierarchy.addClassification("ldecimal", "literal");
		CategoriesHierarchy.addClassification("lfloat_form_c", "literal");
		CategoriesHierarchy.addClassification("lfloat_form_b", "literal");
		CategoriesHierarchy.addClassification("lfloat_form_a", "literal");
		CategoriesHierarchy.addKeywordCategory("lola_keyword");
		CategoriesHierarchy.addCategory("package_snippet");
		CategoriesHierarchy.addCategory("snippet");
		CategoriesHierarchy.addTriviaCategory("enter");
		Balancing.addBalancing((("{")), (("}")));
		Balancing.addBalancing((("(")), ((")")));
		Balancing.addBalancing((("[")), (("]")));
	}
%}
D = (([0-9]))
L = (((([A-Z]))|(([a-z]))|(("_"))))
H = (((([A-F]))|(([a-f]))|(([0-9]))))
E = (((([eE]))(((("+"))|(("-")))?)(({D})+)))
FS = (((([fF]))|(([lL]))))
LS = (([lL]))
LLS = (((({LS}))(({LS}))))
NS = ((""))
UIS = (([uU]))
ULS = (((((({UIS}))(({LS}))))|(((({LS}))(({UIS}))))))
ULLS = (((((({UIS}))(({LLS}))))|(((({LLS}))(({UIS}))))))
US = (((({UIS}))|(({ULS}))|(({ULLS}))))
IS = (((({US}))|(({LS}))|(({LLS}))|(({NS}))))
WS = ((((\t))|((\v))|((" "))))
%%
<YYINITIAL> ((\t))    { Token $ = token("ignore"); yycolumn += (3 - yycolumn % 4); return $;}
<YYINITIAL>((((\t))|((\v))|((" "))))	{ return token("ignore"); }
<YYINITIAL>(("..."))	{ return token("tridot"); }
<YYINITIAL>((">>="))	{ return token("eqrsh"); }
<YYINITIAL>(("<<="))	{ return token("eqlsh"); }
<YYINITIAL>(("+="))	{ return token("eqadd"); }
<YYINITIAL>(("*="))	{ return token("eqmul"); }
<YYINITIAL>(("/="))	{ return token("eqdiv"); }
<YYINITIAL>(("%="))	{ return token("eqmod"); }
<YYINITIAL>(("&="))	{ return token("eqand"); }
<YYINITIAL>(("|="))	{ return token("eqor"); }
<YYINITIAL>(("^="))	{ return token("eqxor"); }
<YYINITIAL>((">>"))	{ return token("birsh"); }
<YYINITIAL>(("<<"))	{ return token("bilsh"); }
<YYINITIAL>(("+"))	{ return token("plus"); }
<YYINITIAL>(("-"))	{ return token("minus"); }
<YYINITIAL>(("++"))	{ return token("uninc"); }
<YYINITIAL>(("--"))	{ return token("undec"); }
<YYINITIAL>(("&&"))	{ return token("biandlogic"); }
<YYINITIAL>(("||"))	{ return token("biorlogic"); }
<YYINITIAL>(("<="))	{ return token("bilesseq"); }
<YYINITIAL>((">="))	{ return token("bigreatereq"); }
<YYINITIAL>(("=="))	{ return token("bieq"); }
<YYINITIAL>(("!="))	{ return token("bineq"); }
<YYINITIAL>(("|"))	{ return token("or"); }
<YYINITIAL>(("^"))	{ return token("bixor"); }
<YYINITIAL>(("%"))	{ return token("bimod"); }
<YYINITIAL>((">"))	{ return token("bigreater"); }
<YYINITIAL>(("<"))	{ return token("biless"); }
<YYINITIAL>(("/"))	{ return token("bidiv"); }
<YYINITIAL>(("*"))	{ return token("star"); }
<YYINITIAL>(("&"))	{ return token("amp"); }
<YYINITIAL>(("!"))	{ return token("unnot"); }
<YYINITIAL>(("~"))	{ return token("unneg"); }
<YYINITIAL>((";"))	{ return token("semicolon"); }
<YYINITIAL>((","))	{ return token("comma"); }
<YYINITIAL>(("'"))	{ return token("apostrophe"); }
<YYINITIAL>((":"))	{ return token("colon"); }
<YYINITIAL>(("."))	{ return token("dot"); }
<YYINITIAL>(("->"))	{ return token("arrow"); }
<YYINITIAL>(("="))	{ return token("eq"); }
<YYINITIAL>(("?"))	{ return token("ques"); }
<YYINITIAL>(("<%"))	{ return token("diopencurly"); }
<YYINITIAL>(("{"))	{ return token("opencurly"); }
<YYINITIAL>(("%>"))	{ return token("diclosecurly"); }
<YYINITIAL>(("}"))	{ return token("closecurly"); }
<YYINITIAL>(("("))	{ return token("openparen"); }
<YYINITIAL>((")"))	{ return token("closeparen"); }
<YYINITIAL>(("["))	{ return token("openbracket"); }
<YYINITIAL>(("]"))	{ return token("closebracket"); }
<YYINITIAL>(("void"))	{ return token("void"); }
<YYINITIAL>(("float"))	{ return token("float"); }
<YYINITIAL>(("short"))	{ return token("short"); }
<YYINITIAL>(("char"))	{ return token("char"); }
<YYINITIAL>(("double"))	{ return token("double"); }
<YYINITIAL>(("int"))	{ return token("int"); }
<YYINITIAL>(("long"))	{ return token("long"); }
<YYINITIAL>(("auto"))	{ return token("auto"); }
<YYINITIAL>(("const"))	{ return token("const"); }
<YYINITIAL>(("extern"))	{ return token("extern"); }
<YYINITIAL>(("register"))	{ return token("register"); }
<YYINITIAL>(("signed"))	{ return token("signed"); }
<YYINITIAL>(("static"))	{ return token("static"); }
<YYINITIAL>(("unsigned"))	{ return token("unsigned"); }
<YYINITIAL>(("volatile"))	{ return token("volatile"); }
<YYINITIAL>(("break"))	{ return token("break"); }
<YYINITIAL>(("case"))	{ return token("case"); }
<YYINITIAL>(("continue"))	{ return token("continue"); }
<YYINITIAL>(("default"))	{ return token("default"); }
<YYINITIAL>(("do"))	{ return token("do"); }
<YYINITIAL>(("else"))	{ return token("else"); }
<YYINITIAL>(("for"))	{ return token("for"); }
<YYINITIAL>(("goto"))	{ return token("goto"); }
<YYINITIAL>(("if"))	{ return token("if"); }
<YYINITIAL>(("return"))	{ return token("return"); }
<YYINITIAL>(("switch"))	{ return token("switch"); }
<YYINITIAL>(("while"))	{ return token("while"); }
<YYINITIAL>(("enum"))	{ return token("enum"); }
<YYINITIAL>(("class"))	{ return token("class"); }
<YYINITIAL>(("interface"))	{ return token("interface"); }
<YYINITIAL>(("typedef"))	{ return token("typedef"); }
<YYINITIAL>(("sizeof"))	{ return token("sizeof"); }
<YYINITIAL>(((("0"))(([xX]))(({H})+)(({IS})?)))	{ return token("lhexadecimal"); }
<YYINITIAL>(((("0"))(({D})+)(({IS})?)))	{ return token("loctal"); }
<YYINITIAL>(((({D})+)(({IS})?)))	{ return token("ldecimal"); }
<YYINITIAL>(((({D})+)(({E}))(({FS})?)))	{ return token("lfloat_form_a"); }
<YYINITIAL>(((({D})*)(("."))(({D})+)(({E})?)(({FS})?)))	{ return token("lfloat_form_b"); }
<YYINITIAL>(((({D})+)(("."))(({D})*)(({E})?)(({FS})?)))	{ return token("lfloat_form_c"); }
<YYINITIAL>(((("\""))((([^\"\\])|(((("\\"))(((((("'"))|(("\""))|(("?"))|(("\\"))|(("a"))|(("b"))|(("f"))|(("n"))|(("r"))|(("t"))|(("v"))))|(((([0-7]))(([0-7])?)(([0-7])?)))|(((("x"))(({H})+))))))))*)(("\"")))+)	{ return token("lstring"); }
<YYINITIAL>(((({L}))(((({L}))|(({D})))*)))	{ return token("identifier"); }
<YYINITIAL>(((("'"))(((({D}))|(({L}))))(("'"))))	{ return token("character"); }
<YYINITIAL>(("#"))	{ return token("sharp"); }
<YYINITIAL>##Import[ ]((({L}+)[.])*({L}+))	{ return tokenWithSnippet("lola_keyword"); }
<YYINITIAL>##Include[ ]((({L}+)[.])*({L}+))	{ return tokenWithSnippet("lola_keyword"); }
<YYINITIAL>##and	{ return token("##and"); }
<YYINITIAL>##anchor	{ return token("##anchor"); }
<YYINITIAL>##NewLine	{ return token("##NewLine"); }
<YYINITIAL>##replace	{ return token("##replace"); }
<YYINITIAL>##see	{ return token("##see"); }
<YYINITIAL>##SomeIdentifier	{ return token("##SomeIdentifier"); }
<YYINITIAL>##run	{ return token("##run"); }
<YYINITIAL>##Neither	{ return token("##Neither"); }
<YYINITIAL>##Include	{ return token("##Include"); }
<YYINITIAL>##find	{ return token("##find"); }
<YYINITIAL>##Empty	{ return token("##Empty"); }
<YYINITIAL>##Unbalanced	{ return token("##Unbalanced"); }
<YYINITIAL>##closer	{ return token("##closer"); }
<YYINITIAL>##exceptFor	{ return token("##exceptFor"); }
<YYINITIAL>##delete	{ return token("##delete"); }
<YYINITIAL>##Unless	{ return token("##Unless"); }
<YYINITIAL>##andAlso	{ return token("##andAlso"); }
<YYINITIAL>##Import	{ return token("##Import"); }
<YYINITIAL>##log	{ return token("##log"); }
<YYINITIAL>##Find	{ return token("##Find"); }
<YYINITIAL>##prepend	{ return token("##prepend"); }
<YYINITIAL>##note	{ return token("##note"); }
<YYINITIAL>##opener	{ return token("##opener"); }
<YYINITIAL>##Match	{ return token("##Match"); }
<YYINITIAL>##Splice	{ return token("##Splice"); }
<YYINITIAL>##example	{ return token("##example"); }
<YYINITIAL>##Nothing	{ return token("##Nothing"); }
<YYINITIAL>##SameLine	{ return token("##SameLine"); }
<YYINITIAL>##PermutationOf	{ return token("##PermutationOf"); }
<YYINITIAL>##Optional	{ return token("##Optional"); }
<YYINITIAL>##Case	{ return token("##Case"); }
<YYINITIAL>##followedBy	{ return token("##followedBy"); }
<YYINITIAL>##xor	{ return token("##xor"); }
<YYINITIAL>##ProperSubsetOf	{ return token("##ProperSubsetOf"); }
<YYINITIAL>##description	{ return token("##description"); }
<YYINITIAL>##resultsIn	{ return token("##resultsIn"); }
<YYINITIAL>##append	{ return token("##append"); }
<YYINITIAL>##else	{ return token("##else"); }
<YYINITIAL>##assert	{ return token("##assert"); }
<YYINITIAL>##Not	{ return token("##Not"); }
<YYINITIAL>##Xither	{ return token("##Xither"); }
<YYINITIAL>##EndOfFile	{ return token("##EndOfFile"); }
<YYINITIAL>##NoneOrMore	{ return token("##NoneOrMore"); }
<YYINITIAL>##Either	{ return token("##Either"); }
<YYINITIAL>##If	{ return token("##If"); }
<YYINITIAL>##elseIf	{ return token("##elseIf"); }
<YYINITIAL>##SubsetOf	{ return token("##SubsetOf"); }
<YYINITIAL>##Literal	{ return token("##Literal"); }
<YYINITIAL>##Identifier	{ return token("##Identifier"); }
<YYINITIAL>##with	{ return token("##with"); }
<YYINITIAL>##nor	{ return token("##nor"); }
<YYINITIAL>##BeginningOfLine	{ return token("##BeginningOfLine"); }
<YYINITIAL>##NonEmptySubsetOf	{ return token("##NonEmptySubsetOf"); }
<YYINITIAL>##ifNone	{ return token("##ifNone"); }
<YYINITIAL>##Sequence	{ return token("##Sequence"); }
<YYINITIAL>##of	{ return token("##of"); }
<YYINITIAL>##OneOrMore	{ return token("##OneOrMore"); }
<YYINITIAL>##or	{ return token("##or"); }
<YYINITIAL>##filter	{ return token("##filter"); }
<YYINITIAL>##without	{ return token("##without"); }
<YYINITIAL>##separator	{ return token("##separator"); }
<YYINITIAL>##ForEach	{ return token("##ForEach"); }
<YYINITIAL>##otherwise	{ return token("##otherwise"); }
<YYINITIAL>##Any	{ return token("##Any"); }
<YYINITIAL>##EndOfLine	{ return token("##EndOfLine"); }
<YYINITIAL>##({L}+)	{ return token("lola_keyword"); }
<YYINITIAL>##	{ return token("##"); }
<YYINITIAL>(\r\n)	{ return token("enter"); }
<YYINITIAL>((\n)|(\r))	{ return token("enter"); }
/* error fallback */
[^] { throw new Error("Illegal character <"+
yytext()+">"); }
