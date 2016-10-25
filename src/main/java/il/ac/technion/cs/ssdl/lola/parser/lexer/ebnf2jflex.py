import re

with open('java.jflex', 'w') as f:
    for line in open('java.ebnf', 'r'):
        # line.translate("=::", " ")
        if line == '\n':
            continue
        words = line.replace('\n', ' ').replace('::=', ' ::= ').replace('>', '> ').replace('?', '? ').split(' ')
        tokenMode = 0
        rightSide = False
        for word in words:
            if word == '':
                continue
            if word == '::=':
                word = ' = '
                rightSide = True
            elif '=' in word or word in {'(', ')', '[', ']', '{', '}', '+', '*', '++', '--', '.', '^', '&&', '||', '~'}:
                word = '"' + word + '" '
            elif word == '"':
                word = '"\\"" '
            else:
                if word[0] == "<":
                    if rightSide:
                        word = '{' + word[1:]
                    else:
                        word = word[1:]
                    tokenMode = 1
                elif tokenMode == 1:
                    word = word[0].upper() + word[1:]
                if word[-1] == ">":
                    if rightSide:
                        word = word[:-1] + '}'
                    else:
                        word = word[:-1]
                    tokenMode = 0
                if tokenMode == 0:
                    word += ' '
            f.write(word)
        f.write('\n')






