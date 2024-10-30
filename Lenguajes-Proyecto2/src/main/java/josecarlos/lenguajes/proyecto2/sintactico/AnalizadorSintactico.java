package josecarlos.lenguajes.proyecto2.sintactico;

import java.util.ArrayList;
import java.util.List;
import josecarlos.lenguajes.proyecto2.ListSyntaxError;
import josecarlos.lenguajes.proyecto2.enums.PalabraReservada;
import josecarlos.lenguajes.proyecto2.tokens.Token;
import josecarlos.lenguajes.proyecto2.tokens.TokenErrorSintaxis;
import josecarlos.lenguajes.proyecto2.tokens.TokenType;

/**
 *
 * @author emahch
 */
public class AnalizadorSintactico {

    public static final String FIN_BLOQUE = ";";

    private final Token[] tokens;
    private int currentIndex;
    private ListSyntaxError tokensError;
    private AnalizadorDDL analizadorDDL;

    public AnalizadorSintactico(List<Token> tokens) {
        this.currentIndex = 0;
        if (tokens.isEmpty()) {
            this.tokens = null;
            return;
        }
        this.tokensError = new ListSyntaxError();
        this.analizadorDDL = new AnalizadorDDL(tokensError);
        this.tokens = new Token[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            this.tokens[i] = tokens.get(i);
        }
    }

    private void next() {
        this.currentIndex++;
    }

    private boolean isEnd() {
        return this.currentIndex >= this.tokens.length;
    }

    public void analizar() {
        while (!isEnd()) {
            if (this.tokens[currentIndex].getTipo() != TokenType.PALABRA_RESERVADA) {
                this.tokensError.add(new TokenErrorSintaxis(tokens[currentIndex], "Se esperaba un token de inicio DDL o DML"));
                next();
            } else {
                if (this.tokens[currentIndex].getValor().equals(PalabraReservada.CREATE.name())) {
                    analizadorDDL.analizarCreate(getTokensBloque());
                } else if (this.tokens[currentIndex].getValor().equals(PalabraReservada.ALTER.name())) {
                    analizadorDDL.analizarAlter(getTokensBloque());
                } else if (this.tokens[currentIndex].getValor().equals(PalabraReservada.DROP.name())) {
                    analizadorDDL.analizarDrop(getTokensBloque());
                }
            }
        }
        for (TokenErrorSintaxis tokenErrorSintaxis : tokensError) {
            System.out.println(tokenErrorSintaxis.print());
        }
        analizadorDDL.printDB();
        analizadorDDL.printTables();
    }

    private Pila getTokensBloque() {
        Pila tokensBloque = new Pila();
        Token token = tokens[currentIndex];

        while (!isEnd() && !token.getValor().equals(FIN_BLOQUE)) {
            tokensBloque.add(token);
            next();
            if (!isEnd()) {
                token = tokens[currentIndex];
            }
        }
        
        next();
        if (tokensBloque.isEmpty()) {
            return null;
        }
        return tokensBloque;
    }
}
