package josecarlos.lenguajes.proyecto2.tokens;

import java.util.ArrayList;
import java.util.Arrays;
import josecarlos.lenguajes.proyecto2.tokens.Token;
import josecarlos.lenguajes.proyecto2.tokens.TokenErrorSintaxis;
import josecarlos.lenguajes.proyecto2.tokens.TokenType;

/**
 *
 * @author emahch
 */
public class ListSyntaxError extends ArrayList<TokenErrorSintaxis> {

    public void addError(Token token, TokenType tipo) {
        add(new TokenErrorSintaxis(token, "Se esperaba un <" + tipo.name().toLowerCase() + ">"));
    }

    public void addError(Token token, String valorEsperado) {
        add(new TokenErrorSintaxis(token, "Se esperaba un " + valorEsperado));
    }

    public void addError(Token token, String[] valoresEsperados) {
        add(new TokenErrorSintaxis(token, "Se esperaba " + formatArray(valoresEsperados)));
    }

    private String formatArray(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i != array.length) {
                sb.append(" o ");
            }
        }
        return sb.toString();
    }
}
