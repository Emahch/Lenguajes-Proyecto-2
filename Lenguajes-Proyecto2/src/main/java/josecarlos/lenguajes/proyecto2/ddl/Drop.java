package josecarlos.lenguajes.proyecto2.ddl;

/**
 *
 * @author emahch
 */
public class Drop {

    private String idTabla;

    public String getIdTabla() {
        return idTabla;
    }

    public void setIdTabla(String idTabla) {
        this.idTabla = idTabla;
    }

    public String generarDot() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph G {\n");
        dot.append("    node [shape=plaintext];\n");

        dot.append("AlterTable [label=<\n");
        dot.append("<table border='0' cellborder='0' cellspacing='0'>\n");

        dot.append("<tr><td colspan='2' bgcolor='#ffffff'>").append("DROP TABLE ").append(getIdTabla()).append("</td></tr>\n");

        dot.append("<tr><td><b><u>Drop ").append(getIdTabla()).append("</u></b></td></tr>\n");

        // Contenido de Llave o identificadorObjetivo con tipoDato
        dot.append("<tr><td align='left'>").append("cascade").append("</td></tr>\n");

        dot.append("</table>\n");
        dot.append("    >, shape=box, style=\"rounded,filled\", color=black, fillcolor=white];\n");
        dot.append("}\n");
        return dot.toString();
    }
}
