package josecarlos.lenguajes.proyecto2;

//Seccion de imports
import java.util.ArrayList;
import java.util.List;

%%
%{

// Codigo Java

    private List<String> lista = new ArrayList<>();
    private List<String> listaErrores = new ArrayList<>();

    public void addList(String token) {
        lista.add(token);
    }

    public void addListaErrores(String token) {
        lista.add(token);
    }

    public List<String> getLista(){
        return lista;
    }
    
    public List<String> getListaErrores(){
        return listaErrores;
    }

%}

%public
%class Test
%unicode
%line
%column
%standalone

ENTERO = [0-9]+
LETRA = [a-zA-z]+
ESPACIOS = [\b\t\r\n ]  //Todos los espacios en blanco

%%

{LETRA}                 {addList(yytext());}
{ENTERO}                {addList(yytext());}
{ENTERO}.{ENTERO}       {addList(yytext());}
{ESPACIOS}              {/**/}
 .                      {addListaErrores(yytext());}
