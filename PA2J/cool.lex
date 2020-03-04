/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    int get_curr_lineno() {
	    return curr_lineno;
    }

    private int commentANestLevel = 0;

    private AbstractSymbol filename;

    void set_filename(String fname) {
	    filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	    return filename;
    }

    // string lexing stuff
    char[] mappableCharacters = { 'n', 'b', 't', 'f' };
    char[] constantCharacters = { '\n', '\b', '\t', '\f' };
    boolean isPreviousBackslashEscaped = false;
    boolean previousCharacterIsBackslash = false;   
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
        case COMMENT_A:
            yybegin(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in comment");
        case COMMENT_B:
            yybegin(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in comment");
        case STRING:
            yybegin(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup

%state COMMENT_A COMMENT_B STRING


%%

<YYINITIAL>\n {
    curr_lineno++;
}

<YYINITIAL>[ \f\r\t\v\013] {
    // ignore whitespace
    // not sure if \n is needed.
}

<YYINITIAL>"(*" {
    commentANestLevel++;
    yybegin(COMMENT_A);
}

<YYINITIAL>"--" {
    yybegin(COMMENT_B);
}

<COMMENT_B>[^\n]* {
}

<COMMENT_B>\n {
    curr_lineno++;
    yybegin(YYINITIAL);
}

<COMMENT_A>"*)" {
    commentANestLevel--;
    if (commentANestLevel == 0) {
        yybegin(YYINITIAL);
    } else if (commentANestLevel < 0) {
        return new Symbol(TokenConstants.ERROR, "Unmatched *)");
    }
}

<COMMENT_A>"(*" {
    commentANestLevel++;
}

<COMMENT_A>\n {
    curr_lineno++;
}

<COMMENT_A>. {
}

<YYINITIAL>"*)" {
   return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}

<YYINITIAL>"=>"			{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }

<YYINITIAL>[cC][lL][aA][sS][sS] {
    return new Symbol(TokenConstants.CLASS);
}

<YYINITIAL>[eE][lL][sS][eE] {
    return new Symbol(TokenConstants.ELSE);
}

<YYINITIAL>f[aA][lL][sS][eE] {
    return new Symbol(TokenConstants.BOOL_CONST, false);
}

<YYINITIAL>t[rR][uU][eE] {
    return new Symbol(TokenConstants.BOOL_CONST, true);
}

<YYINITIAL>[iI][fF] {
    return new Symbol(TokenConstants.IF);
}

<YYINITIAL>[fF][iI] {
    return new Symbol(TokenConstants.FI);
}
<YYINITIAL>[iI][nN] {
    return new Symbol(TokenConstants.IN);
}
<YYINITIAL>[iI][sS][vV][oO][iI][dD] {
    return new Symbol(TokenConstants.ISVOID);
}
<YYINITIAL>[lL][eE][tT] {
    return new Symbol(TokenConstants.LET);
}
<YYINITIAL>[lL][oO][oO][pP] {
    return new Symbol(TokenConstants.LOOP);
}
<YYINITIAL>[pP][oO][oO][lL] {
    return new Symbol(TokenConstants.POOL);
}
<YYINITIAL>[tT][hH][eE][nN] {
    return new Symbol(TokenConstants.THEN);
}
<YYINITIAL>[wW][hH][iI][lL][eE] {
    return new Symbol(TokenConstants.WHILE);
}

<YYINITIAL>[nN][eE][wW] {
    return new Symbol(TokenConstants.NEW);
}
<YYINITIAL>[nN][oO][tT] {
    return new Symbol(TokenConstants.NOT);
}

<YYINITIAL>[cC][aA][sS][eE] {
    return new Symbol(TokenConstants.CASE);
}
<YYINITIAL>[eE][sS][aA][cC] {
    return new Symbol(TokenConstants.ESAC);
}

<YYINITIAL>[oO][fF] {
    return new Symbol(TokenConstants.OF);
}

<YYINITIAL>[iI][nN][hH][eE][rR][iI][tT][sS] {
    return new Symbol(TokenConstants.INHERITS);
}

<YYINITIAL>"{" {
    return new Symbol(TokenConstants.LBRACE);
}

<YYINITIAL>"}" {
    return new Symbol(TokenConstants.RBRACE);
}

<YYINITIAL>"(" {
    return new Symbol(TokenConstants.LPAREN);
}

<YYINITIAL>")" {
    return new Symbol(TokenConstants.RPAREN);
}

<YYINITIAL>";" {
    return new Symbol(TokenConstants.SEMI);
}

<YYINITIAL>"+" {
    return new Symbol(TokenConstants.PLUS);
}

<YYINITIAL>"-" {
    return new Symbol(TokenConstants.MINUS);
}

<YYINITIAL>"/" {
    return new Symbol(TokenConstants.DIV);
}

<YYINITIAL>"*" {
    return new Symbol(TokenConstants.MULT);
}

<YYINITIAL>":" {
    return new Symbol(TokenConstants.COLON);
}

<YYINITIAL>"<-" {
    return new Symbol(TokenConstants.ASSIGN);
}

<YYINITIAL>"=" {
    return new Symbol(TokenConstants.EQ);
}

<YYINITIAL>"<" {
    return new Symbol(TokenConstants.LT);
}

<YYINITIAL>"<=" {
    return new Symbol(TokenConstants.LE);
}

<YYINITIAL>"@" {
    return new Symbol(TokenConstants.AT);
}

<YYINITIAL>"." {
    return new Symbol(TokenConstants.DOT);
}

<YYINITIAL>"," {
    return new Symbol(TokenConstants.COMMA);
}

<YYINITIAL>"~" {
    return new Symbol(TokenConstants.NEG);
}

<YYINITIAL>\" {
    yybegin(STRING);
}

<STRING>\n {
    curr_lineno++;

    char c = yytext().charAt(0);

    Character previousCharacter = null;
    if (string_buf.length() > 0) {
        previousCharacter = string_buf.charAt(string_buf.length() - 1);
    }
    boolean previousCharacterIsBackslash = previousCharacter != null && previousCharacter == '\\';

    // do not allow a newline if it is unescaped
    if (!previousCharacterIsBackslash || isPreviousBackslashEscaped) {
        yybegin(YYINITIAL);
        string_buf.setLength(0);
        isPreviousBackslashEscaped = false;
        return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
    }

    // pop the escaping backslash
    string_buf.setLength(string_buf.length() - 1);
    string_buf.append(c);
}

<STRING>\" {
    char c = yytext().charAt(0);

    Character previousCharacter = null;
    if (string_buf.length() > 0) {
        previousCharacter = string_buf.charAt(string_buf.length() - 1);
    }
    boolean previousCharacterIsBackslash = previousCharacter != null && previousCharacter == '\\';

    if (previousCharacterIsBackslash && !isPreviousBackslashEscaped) {
        string_buf.setLength(string_buf.length() - 1);
        string_buf.append(c);
    } else {
        yybegin(YYINITIAL);
        String newStr = string_buf.toString();
        string_buf.setLength(0);
        isPreviousBackslashEscaped = false;

        if (newStr.length() >= MAX_STR_CONST) {
           return new Symbol(TokenConstants.ERROR, "String constant too long"); 
        }
        
        AbstractSymbol symbol = AbstractTable.stringtable.addString(newStr, newStr.length());
        return new Symbol(TokenConstants.STR_CONST, symbol);
    }
}

<STRING>\\ {
    char c = yytext().charAt(0);
    Character previousCharacter = null;
    if (string_buf.length() > 0) {
        previousCharacter = string_buf.charAt(string_buf.length() - 1);
    }
    boolean previousCharacterIsBackslash = previousCharacter != null && previousCharacter == '\\';

    if (!isPreviousBackslashEscaped && previousCharacterIsBackslash) {
        isPreviousBackslashEscaped = true;
    } else {
        string_buf.append('\\');
        isPreviousBackslashEscaped = false;
    }
}

<STRING>. {
    char c = yytext().charAt(0);
    Character previousCharacter = null;
    if (string_buf.length() > 0) {
        previousCharacter = string_buf.charAt(string_buf.length() - 1);
    }
    boolean previousCharacterIsBackslash = previousCharacter != null && previousCharacter == '\\';

    boolean replaced = false;
    for (int i = 0; i < mappableCharacters.length; i++) {
        char mappableCharacter = mappableCharacters[i];
        if (!isPreviousBackslashEscaped && previousCharacterIsBackslash && mappableCharacter == c) { 
            string_buf.setLength(string_buf.length() - 1);
            string_buf.append(constantCharacters[i]);
            replaced = true;
            break;
        }
    }

    if (!replaced) {
        if (c == '\0') {
            // catch unescaped null characters
            if (!previousCharacterIsBackslash || isPreviousBackslashEscaped) {
                return new Symbol(TokenConstants.ERROR, "String contains null character");
            }
            // catch escaped null characters
            return new Symbol(TokenConstants.ERROR, "String contains escaped null character.");
        }

        // remove previous backslash such that \c -> c.
        if (!isPreviousBackslashEscaped && previousCharacterIsBackslash) {
            string_buf.setLength(string_buf.length() - 1);	
        }

        string_buf.append(c);
    }
}

<YYINITIAL>[0-9]+ {
    AbstractSymbol symbol = AbstractTable.inttable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.INT_CONST, symbol);
}

<YYINITIAL>[A-Z][A-Za-z0-9_]* {
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}

<YYINITIAL>[a-z][A-Za-z0-9_]* {
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext(), yylength());
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}


. {
    return new Symbol(TokenConstants.ERROR, yytext());
}
