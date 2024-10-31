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

    public String print() {
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

    private String formatLlave() {
        if (this.llave == null) {
            return "";
        }
        String texto = "FK: " + "\n" + llave.getForeignKey() + ": " + llave.getIdentificador() + " from " + llave.getReference() + "(" + llave.getIdReference() + ")";
        return texto;
    }

    public String generarDot() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("    node [shape=plaintext];\n");

        dot.append("    " + nombre + " [label=<\n");
        dot.append("    <table border='0' cellborder='0' cellspacing='0'>\n");
        dot.append("        <tr><td colspan='2' bgcolor='#ffffff'><b>").append(nombre).append("</b></td></tr>\n");

        for (Declaracion declaracion : declaraciones) {
            String identificador = declaracion.getIdentificador();
            String tipoDato = declaracion.getTipoDato();
            String condicion = declaracion.getCondicion();
            boolean esPrimaryKey = "PRIMARY KEY".equalsIgnoreCase(condicion);

            dot.append("        <tr><td align='left'>");

            // Agregar en negrita y subrayado si es PRIMARY KEY
            if (esPrimaryKey) {
                dot.append("<b><u>").append(identificador).append("</u></b>");
            } else {
                dot.append(identificador);
            }

            dot.append(" : ").append(tipoDato);

            if (condicion != null && !condicion.isEmpty()) {
                dot.append(" ").append(condicion);
            }

            // Verificar si es una Foreign Key
            if (llave != null && identificador.equals(llave.getForeignKey())) {
                dot.append(" FK");
            }

            dot.append("</td></tr>\n");
        }

        dot.append("    </table>\n");
        dot.append("    >, shape=box, style=\"rounded,filled\", color=black, fillcolor=white];\n");
        dot.append("}\n");

        return dot.toString();
    }
}
