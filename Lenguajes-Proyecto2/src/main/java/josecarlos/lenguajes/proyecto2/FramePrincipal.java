package josecarlos.lenguajes.proyecto2;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import josecarlos.lenguajes.proyecto2.frontend.ExportImportFile;
import josecarlos.lenguajes.proyecto2.frontend.LineNumber;
import josecarlos.lenguajes.proyecto2.sintactico.AnalizadorSintactico;
import josecarlos.lenguajes.proyecto2.tokens.Token;

/**
 *
 * @author emahch
 */
public class FramePrincipal extends javax.swing.JFrame {

    private File selectedFile;

    /**
     * Creates new form FramePrincipal
     */
    public FramePrincipal() {
        initComponents();
        LineNumber lineNumber = new LineNumber(this.txtPane, 3);
        lineNumber.setUpdateFont(false);
        this.scrollPane.setRowHeaderView(lineNumber);
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        txtPane = new javax.swing.JTextPane();
        btnAnalizar = new javax.swing.JButton();
        lblPosicion = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        cargarArchivo = new javax.swing.JMenuItem();
        guardarArchivo = new javax.swing.JMenuItem();
        guardarArchivoComo = new javax.swing.JMenuItem();
        menuGrafico = new javax.swing.JMenu();
        menuReportes = new javax.swing.JMenu();
        menuErrores = new javax.swing.JMenu();
        erroresLexicos = new javax.swing.JMenuItem();
        erroresSintácticos = new javax.swing.JMenuItem();
        reporteTablasHalladas = new javax.swing.JMenuItem();
        reporteTablasModificadas = new javax.swing.JMenuItem();
        reporteOperaciones = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SQL Analizer");

        scrollPane.setBorder(null);

        txtPane.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtPaneCaretUpdate(evt);
            }
        });
        scrollPane.setViewportView(txtPane);

        btnAnalizar.setText("Analizar");
        btnAnalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarActionPerformed(evt);
            }
        });

        lblPosicion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosicion.setText("Fila 1, Columna 1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnAnalizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 383, Short.MAX_VALUE)
                        .addComponent(lblPosicion, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnalizar)
                    .addComponent(lblPosicion, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        menuArchivo.setText("Archivo");

        cargarArchivo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        cargarArchivo.setText("Cargar");
        cargarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarArchivoActionPerformed(evt);
            }
        });
        menuArchivo.add(cargarArchivo);

        guardarArchivo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        guardarArchivo.setText("Guardar");
        guardarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarArchivoActionPerformed(evt);
            }
        });
        menuArchivo.add(guardarArchivo);

        guardarArchivoComo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        guardarArchivoComo.setText("Guardar como");
        guardarArchivoComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarArchivoComoActionPerformed(evt);
            }
        });
        menuArchivo.add(guardarArchivoComo);

        menuBar.add(menuArchivo);

        menuGrafico.setText("Generar Gráfico");
        menuBar.add(menuGrafico);

        menuReportes.setText("Reportes");

        menuErrores.setText("Errores");

        erroresLexicos.setText("Léxicos");
        menuErrores.add(erroresLexicos);

        erroresSintácticos.setText("Sintácticos");
        menuErrores.add(erroresSintácticos);

        menuReportes.add(menuErrores);

        reporteTablasHalladas.setText("Tablas Halladas");
        menuReportes.add(reporteTablasHalladas);

        reporteTablasModificadas.setText("Tablas Modificadas");
        menuReportes.add(reporteTablasModificadas);

        reporteOperaciones.setText("Operaciones");
        menuReportes.add(reporteOperaciones);

        menuBar.add(menuReportes);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarActionPerformed
        String entrada = txtPane.getText();
        Lexico analizadorLexico = new Lexico(new StringReader(entrada));

        List<Token> lista = new ArrayList<>();
        List<Token> listaVista = new ArrayList<>();
        List<Token> listaErrores = new ArrayList<>();
        try {
            while (analizadorLexico.yylex() != Lexico.YYEOF) {
            }
            lista = analizadorLexico.getLista();
            listaVista = analizadorLexico.getListaVista();
            listaErrores = analizadorLexico.getListaErrores();
        } catch (IOException e) {
        }
        if (listaVista.isEmpty()) {
            return;
        }
        AnalizadorSintactico analizadorSintaxis = new AnalizadorSintactico(lista);
        analizadorSintaxis.analizar();
        updateText(listaVista);
    }//GEN-LAST:event_btnAnalizarActionPerformed

    private void txtPaneCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtPaneCaretUpdate
        try {
            int caretPosition = txtPane.getCaretPosition();
            int row = (txtPane.getDocument().getDefaultRootElement().getElementIndex(caretPosition)) + 1;
            int startOfLineOffset = txtPane.getDocument().getDefaultRootElement().getElement(row - 1).getStartOffset();
            int column = caretPosition - startOfLineOffset + 1;

            lblPosicion.setText("Fila " + row + ", Columna " + column);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_txtPaneCaretUpdate

    private void guardarArchivoComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarArchivoComoActionPerformed
        ExportImportFile exportFile = new ExportImportFile();
        boolean isSelected = exportFile.selectPath("querySQL.txt");
        if (isSelected) {
            this.selectedFile = exportFile.getArchivoSeleccionado();
            exportFile.writeFile(txtPane.getText());
        }
    }//GEN-LAST:event_guardarArchivoComoActionPerformed

    private void guardarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarArchivoActionPerformed
        ExportImportFile exportFile = new ExportImportFile();
        if (selectedFile == null) {
            guardarArchivoComoActionPerformed(evt);
            return;
        } else {
            exportFile.setArchivoSeleccionado(selectedFile);
        }
        exportFile.writeFile(txtPane.getText());
    }//GEN-LAST:event_guardarArchivoActionPerformed

    private void cargarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargarArchivoActionPerformed
        ExportImportFile importFile = new ExportImportFile();
        boolean isSelected = importFile.openFile();
        if (isSelected) {
            String datos = importFile.recibirArchivoEntrada();
            txtPane.setText(datos);
        }
    }//GEN-LAST:event_cargarArchivoActionPerformed

    private void updateText(List<Token> listaVista) {
        StyledDocument doc = txtPane.getStyledDocument();
        txtPane.setText("");

        Style style = txtPane.addStyle("style", null);
        for (Token token : listaVista) {
            StyleConstants.setForeground(style, token.getTipo().getColor());
            try {
                doc.insertString(doc.getLength(), token.getValor(), style);
            } catch (BadLocationException ex) {
            }
        }
        txtPane.removeStyle("style");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FramePrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnalizar;
    private javax.swing.JMenuItem cargarArchivo;
    private javax.swing.JMenuItem erroresLexicos;
    private javax.swing.JMenuItem erroresSintácticos;
    private javax.swing.JMenuItem guardarArchivo;
    private javax.swing.JMenuItem guardarArchivoComo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblPosicion;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuErrores;
    private javax.swing.JMenu menuGrafico;
    private javax.swing.JMenu menuReportes;
    private javax.swing.JMenuItem reporteOperaciones;
    private javax.swing.JMenuItem reporteTablasHalladas;
    private javax.swing.JMenuItem reporteTablasModificadas;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextPane txtPane;
    // End of variables declaration//GEN-END:variables
}
