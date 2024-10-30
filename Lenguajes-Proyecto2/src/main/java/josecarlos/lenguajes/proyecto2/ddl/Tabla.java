package josecarlos.lenguajes.proyecto2.ddl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emahch
 */
public class Tabla {
    private String nombre;
    private List<Declaracion> declaraciones;
    private Llave llave;

    public Tabla() {
        this.declaraciones = new ArrayList<>();
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Declaracion> getDeclaraciones() {
        return declaraciones;
    }

    public void addDeclaracion(Declaracion declaracion) {
        this.declaraciones.add(declaracion);
    }

    public Llave getLlave() {
        return llave;
    }

    public void setLlave(Llave llave) {
        this.llave = llave;
    }
    
    public String print(){
        String contenido = "Tabla: " + nombre + "\n" + formatDeclaraciones() + formatLlave();
        return contenido;
    }
    
    private String formatDeclaraciones() {
        StringBuilder sb = new StringBuilder();
        for (Declaracion declaracion : declaraciones) {
            String texto = declaracion.getIdentificador() + ": " + declaracion.getTipoDato() + " " + declaracion.getCondicion();
            sb.append(texto);
            sb.append("\n");
        }
        return sb.toString();
    }
    
    private String formatLlave(){
        if (this.llave == null) {
            return "";
        }
        String texto = "FK: " + "\n" + llave.getForeignKey() + ": " + llave.getIdentificador() + " from " + llave.getReference() + "(" + llave.getIdReference() + ")";
        return texto;
    }
}
