package josecarlos.lenguajes.proyecto2.sintactico;

import java.util.ArrayList;
import java.util.List;
import josecarlos.lenguajes.proyecto2.tokens.Token;
import josecarlos.lenguajes.proyecto2.tokens.TokenType;

/**
 *
 * @author emahch
 */
public class Pila {
    private List<Token> pila;

    public Pila() {
        this.pila = new ArrayList<>();
    }
    
    public Token popFirst(){
        if (this.pila.isEmpty()) {
            // Si esta vacía se duevuelve un token de Fin
            return new Token(TokenType.FIN, "", -1, -1);
        }
        return this.pila.remove(0);
    }
    
    public Token getFirst(){
        if (this.pila.isEmpty()) {
            // Si esta vacía se duevuelve un token de Fin
            return new Token(TokenType.FIN, "", -1, -1);
        }
        return this.pila.get(0);
    }
    
    public void add(Token data){
        this.pila.add(data);
    }
    
    public int getSize(){
        return this.pila.size();
    }
    
    public boolean isEmpty(){
        return this.pila.isEmpty();
    }
}
