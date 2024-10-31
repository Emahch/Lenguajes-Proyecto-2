package josecarlos.lenguajes.proyecto2.ddl;

/**
 *
 * @author emahch
 */
public class Alter {
    private String idTabla;
    private AlterType tipo;
    private String identificador;
    private String objetivo;
    private String tipoDato;
    private String identificadorObjetivo;
    private Llave llave;

    public Alter() {
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }
    
    public AlterType getTipo() {
        return tipo;
    }

    public void setTipo(AlterType tipo) {
        this.tipo = tipo;
    }

    public String getIdTabla() {
        return idTabla;
    }

    public void setIdTabla(String idTabla) {
        this.idTabla = idTabla;
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

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getIdentificadorObjetivo() {
        return identificadorObjetivo;
    }

    public void setIdentificadorObjetivo(String identificadorObjetivo) {
        this.identificadorObjetivo = identificadorObjetivo;
    }

    public Llave getLlave() {
        return llave;
    }

    public void setLlave(Llave llave) {
        this.llave = llave;
    }
    
    public String generarDot(){
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("    node [shape=plaintext];\n");

        dot.append("AlterTable [label=<\n");
        dot.append("<table border='0' cellborder='0' cellspacing='0'>\n");

        // Título principal: ALTER TABLE <identificador>
        dot.append("<tr><td colspan='2' bgcolor='#ffffff'>").append("ALTER TABLE ").append(getIdTabla()).append("</td></tr>\n");

        // Segundo título en negrita y subrayado: <tipo> <objetivo>
        dot.append("<tr><td><b><u>").append(getTipo()).append(" ").append(getObjetivo()).append("</u></b></td></tr>\n");

        // Contenido de Llave o identificadorObjetivo con tipoDato
        if (getLlave() != null) {
            dot.append("<tr><td align='left'>")
               .append(getLlave().getReference()).append(" : ")
               .append(getLlave().getForeignKey()).append(" FK")
               .append("</td></tr>\n");
        } else {
            dot.append("<tr><td align='left'>")
               .append(getIdentificador()).append(": ")
               .append(getTipoDato())
               .append("</td></tr>\n");
        }

        dot.append("</table>\n");
        dot.append("    >, shape=box, style=\"rounded,filled\", color=black, fillcolor=white];\n");
        dot.append("}\n");
        return dot.toString();
    }
    
}
