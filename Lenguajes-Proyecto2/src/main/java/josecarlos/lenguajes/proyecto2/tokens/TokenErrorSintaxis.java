package josecarlos.lenguajes.proyecto2.tokens;

/**
 *
 * @author emahch
 */
public class TokenErrorSintaxis {
    private Token token;
    private String reason;

    public TokenErrorSintaxis(Token token, String reason) {
        this.token = token;
        this.reason = reason;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String print(){
        return "<-----ERROR-----> token: " + token.getValor() + ", tipo: " + token.getTipo().name() + ", linea: " + token.getLinea() + ", columna: " + token.getColumna() + " RAZON: " + reason;
    }
}
