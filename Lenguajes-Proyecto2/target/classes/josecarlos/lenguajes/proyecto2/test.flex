%%
%{

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

"+"     {System.out.println("Suma");}

