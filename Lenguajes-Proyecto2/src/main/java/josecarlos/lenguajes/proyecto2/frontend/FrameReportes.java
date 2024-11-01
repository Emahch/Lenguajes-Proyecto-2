package josecarlos.lenguajes.proyecto2.frontend;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import josecarlos.lenguajes.proyecto2.tokens.ListSyntaxError;
import josecarlos.lenguajes.proyecto2.tokens.Token;
import josecarlos.lenguajes.proyecto2.tokens.TokenErrorSintaxis;

/**
 *
 * @author emahch
 */
public class FrameReportes extends javax.swing.JFrame {

    private static final String[] TITULOS_LEXICO = new String[]{"Token", "Línea", "Columna", "Descripción"};
    private static final String[] TITULOS_SYNTAX = new String[]{"Token", "Tipo Token", "Línea", "Columna", "Descripción"};

    public FrameReportes(String titulo) {
        initComponents();
        setTitle(titulo);
    }
    
    public void generarTabla(List<Token> tokens){
        DefaultTableModel modeloTabla = new DefaultTableModel(TITULOS_LEXICO, 0);
        tabla.setModel(modeloTabla);
        for (Token token : tokens) {
            String[] row = new String[TITULOS_LEXICO.length];
            row[0] = token.getValor();
            int linea = token.getLinea();
            row[1] = linea == 0 ? "" : String.valueOf(linea);
            int columna = token.getColumna();
            row[2] = columna == 0 ? "" : String.valueOf(columna);
            row[3] = "Token no reconocido";
            modeloTabla.addRow(row);
        }
    }
    
    public void generarTabla(ListSyntaxError tokens){
        DefaultTableModel modeloTabla = new DefaultTableModel(TITULOS_SYNTAX, 0);
        tabla.setModel(modeloTabla);
        for (TokenErrorSintaxis tokenSyntax : tokens) {
            String[] row = new String[TITULOS_SYNTAX.length];
            row[0] = tokenSyntax.getToken().getValor();
            row[1] = tokenSyntax.getToken().getTipo().name();
            int linea = tokenSyntax.getToken().getLinea();
            row[2] = linea == 0 ? "" : String.valueOf(linea);
            int columna = tokenSyntax.getToken().getColumna();
            row[3] = columna == 0 ? "" : String.valueOf(columna);
            row[4] = tokenSyntax.getReason();
            modeloTabla.addRow(row);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1105, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
