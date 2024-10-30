package josecarlos.lenguajes.proyecto2.tokens;

import java.awt.Color;

/**
 *
 * @author emahch
 */
public enum TokenType {
    PALABRA_RESERVADA(new Color(255, 128, 0)), //Naranja
    TIPO_DATO(new Color(102, 0, 204)), //Morado
    ENTERO(new Color(0, 0, 183)), //Azul
    DECIMAL(new Color(0, 0, 183)), //Azul
    FECHA(new Color(204, 204, 0)), //Amarillo
    CADENA(new Color(0, 153, 0)), //Verde
    IDENTIFICADOR(new Color(204, 0, 102)), //Fucsia
    BOOLEANO(new Color(0, 0, 183)), //Azul
    AGREGACION(new Color(0, 0, 183)), //Azul
    SIGNO(new Color(0, 0, 0)), //Negro
    ARITMETICO(new Color(0, 0, 0)), //Negro
    RELACIONAL(new Color(0, 0, 0)), //Negro
    LOGICO(new Color(255, 128, 0)), //Naranja
    COMENTARIO(new Color(128, 128, 128)), //Gris
    ESPACIO(new Color(0, 0, 0)), //Negro
    DESCONOCIDO(new Color(204, 0, 0)), //Negro
    FIN(new Color(204, 0, 0)); //Negro
    
    private final Color color;
    
    private TokenType(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }    
}
