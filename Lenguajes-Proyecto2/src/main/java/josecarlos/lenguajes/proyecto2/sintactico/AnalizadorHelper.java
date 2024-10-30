package josecarlos.lenguajes.proyecto2.sintactico;

import java.util.Optional;
import josecarlos.lenguajes.proyecto2.enums.DataType;
import josecarlos.lenguajes.proyecto2.tokens.Token;
import josecarlos.lenguajes.proyecto2.tokens.TokenType;

/**
 *
 * @author emahch
 */
public class AnalizadorHelper {

    public boolean analizarTipo(Token token, TokenType tipoEsperado) {
        return token.getTipo().equals(tipoEsperado);
    }

    public boolean analizarValor(Token token, String valorEsperado) {
        return token.getValor().equals(valorEsperado);
    }
    
    public boolean analizarValor(Token token, String[] valorEsperado) {
        for (String valor : valorEsperado) {
            if (token.getValor().equals(valor)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean analizarFinBloque(Token token) {
        return token.getValor().equals(AnalizadorSintactico.FIN_BLOQUE) || token.getTipo().equals(TokenType.FIN);
    }
    
    public Optional<DataType> analizarDato(Token token) {
        try {
            DataType tipoDato = DataType.valueOf(token.getValor());
            return Optional.of(tipoDato);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    public boolean isDatoEspecial(DataType dataType){
        return dataType.equals(DataType.DECIMAL) || dataType.equals(DataType.VARCHAR) || dataType.equals(DataType.NUMERIC);
    }
}
