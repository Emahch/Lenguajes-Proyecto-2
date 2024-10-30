package josecarlos.lenguajes.proyecto2.ddl;

import java.util.Optional;
import josecarlos.lenguajes.proyecto2.enums.Logico;
import josecarlos.lenguajes.proyecto2.enums.PalabraReservada;

/**
 *
 * @author emahch
 */
public class Declaracion implements Modificador{
    
    public static final String[] ACEPTED = {PalabraReservada.PRIMARY.name(), Logico.NOT.name(), PalabraReservada.UNIQUE.name()};
    
    private String identificador;
    private String tipoDato;
    private String condicion;
    private boolean llavePrimaria;

    public Declaracion() {
        this.llavePrimaria = false;
    }
    
    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public String getCondicion() {
        if (condicion == null) {
            return "";
        }
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }
    
    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public boolean isLlavePrimaria() {
        return llavePrimaria;
    }

    public void setLlavePrimaria(boolean llavePrimaria) {
        this.llavePrimaria = llavePrimaria;
    }
    
}
