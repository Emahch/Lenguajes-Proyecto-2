package josecarlos.lenguajes.proyecto2;

//Seccion de imports
import java.util.ArrayList;
import java.util.List;
import josecarlos.lenguajes.proyecto2.tokens.*;

%%
%{

// Codigo Java

    private List<Token> lista = new ArrayList<Token>();
    private List<Token> listaVista = new ArrayList<Token>();
    private List<Token> listaErrores = new ArrayList<Token>();

    public void addList(Token token) {
        lista.add(token);
        addListaVista(token);
    }

    public void addListaErrores(Token token) {
        lista.add(token);
        listaErrores.add(token);
        addListaVista(token);
    }

    public void addListaVista(Token token) {
        listaVista.add(token);
    }

    public List<Token> getLista(){
        return lista;
    }

    public List<Token> getListaVista(){
        return listaVista;
    }
    
    public List<Token> getListaErrores(){
        return listaErrores;
    }

%}

%public
%class Lexico
%unicode
%line
%column
%standalone

N = [0-9]
LMINS = [a-z]
IDENTIFICADOR = {LMINS}({LMINS}|_|{N})*
FECHA = '{N}{4}-{N}{2}-{N}{2}'
RESERVED = (CREATE | DATABASE | TABLE | KEY | NULL | PRIMARY | UNIQUE | FOREIGN | REFERENCES | ALTER | ADD | COLUMN | TYPE | DROP | CONSTRAINT | IF | EXISTS | CASCADE | ON | DELETE | SET | UPDATE | INSERT | INTO | VALUES | SELECT | FROM | WHERE | AS | GROUP | ORDER | BY | ASC | DESC | LIMIT | JOIN)
DATO = (INTEGER | BIGINT | VARCHAR | DECIMAL | NUMERIC | DATE | TEXT | BOOLEAN | SERIAL)
BOOLEANO = (TRUE | FALSE)
AGREGACION = (SUM | AVG | COUNT | MAX | MIN)
LOGICOS = (AND | OR | NOT)
SIGNOS = ("(" | ")" | , | ; | "." | =)
ARITMETICOS = ("+" | - | "*" | "/")
RELACIONALES = (<= | >= | < | >)
COMENTARIOS = (-" "-).*(\n)
ESPACIOS = [\b\t\r\n ]  //Todos los espacios en blanco

%%
{COMENTARIOS}           {addList(new Token(TokenType.COMENTARIO, yytext(), yyline, yycolumn));}
{RESERVED}              {addList(new Token(TokenType.PALABRA_RESERVADA, yytext(), yyline, yycolumn));}
{DATO}                  {addList(new Token(TokenType.TIPO_DATO, yytext(), yyline, yycolumn));}
{BOOLEANO}              {addList(new Token(TokenType.BOOLEANO, yytext(), yyline, yycolumn));}
{AGREGACION}            {addList(new Token(TokenType.AGREGACION, yytext(), yyline, yycolumn));}
{LOGICOS}               {addList(new Token(TokenType.LOGICO, yytext(), yyline, yycolumn));}
{IDENTIFICADOR}         {addList(new Token(TokenType.IDENTIFICADOR, yytext(), yyline, yycolumn));}
{N}+                    {addList(new Token(TokenType.ENTERO, yytext(), yyline, yycolumn));}
{N}+"."{N}+             {addList(new Token(TokenType.DECIMAL, yytext(), yyline, yycolumn));}
{FECHA}                 {addList(new Token(TokenType.FECHA, yytext(), yyline, yycolumn));}
'[^']*'                 {addList(new Token(TokenType.CADENA, yytext(), yyline, yycolumn));}
{ARITMETICOS}           {addList(new Token(TokenType.ARITMETICO, yytext(), yyline, yycolumn));}
{SIGNOS}                {addList(new Token(TokenType.SIGNO, yytext(), yyline, yycolumn));}
{RELACIONALES}          {addList(new Token(TokenType.RELACIONAL, yytext(), yyline, yycolumn));}
{ESPACIOS}              {addListaVista(new Token(TokenType.ESPACIO, yytext(), yyline, yycolumn));}
 .                      {addListaErrores(new Token(TokenType.DESCONOCIDO, yytext(), yyline, yycolumn));}
