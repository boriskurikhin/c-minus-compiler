import java_cup.runtime.*;
      
%%
%class Lexer

%eofval{
  return null;
%eofval};

%line
%column

%cup
   
%{   

    private Symbol symbol (int type) {
        return new symbol (type, yyline, yycolumn);
    }
    
    private Symbol symbol (int type, Object value) {
        return new symbol (type, yyline, yycolumn, value);
    }
%}

id = [_a-zA-Z][_a-zA-Z0-9]*
number = [0-9]+
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
   
%%
   
"if"               { return symbol (sym.IF); }
"else"             { return symbol (sym.ELSE); }
"return"           { return symbol (sym.RETURN); }
"int"              { return symbol (sym.INT); }
"void"             { return symbol (sym.VOID); }
"while"            { return symbol (sym.WHILE); }
"<="               { return symbol (sym.LESSTHANEQ); }
">="               { return symbol (sym.GREATERTHANEQ); }
"=="               { return symbol (sym.CHECKEQUALS); }
"!="               { return symbol (sym.CHECKNOTEQUALS); }         
"="                { return symbol (sym.SETEQUALS); }
"<"                { return symbol (sym.LESSTHAN); }
">"                { return symbol (sym.GRETERTHAN); }
"+"                { return symbol (sym.PLUS); }
"-"                { return symbol (sym.MINUS); }
"*"                { return symbol (sym.TIMES); }
"/"                { return symbol (sym.DIVIDE); }
"("                { return symbol (sym.LBRACKET); }
")"                { return symbol (sym.RBRACKET); }
";"                { return symbol (sym.SEMICOLON); }
"["                { return symbol (sym.LSQUAREBRACKET); }
"]"                { return symbol (sym.RSQUAREBRACKET); }
"{"                { return symbol (sym.LCURLYBRACKET); }
"}"                { return symbol (sym.RCURLYBRACKET); }
","                { return symbol (sym.COMMA); }
{number}           { return symbol (sym.NUMBER, yytext()); }
{id}+              { return symbol (sym.ID, yytext()); }
{WhiteSpace}+      {}
{comment}          {}
.                  { return symbol (sym.ERROR); }