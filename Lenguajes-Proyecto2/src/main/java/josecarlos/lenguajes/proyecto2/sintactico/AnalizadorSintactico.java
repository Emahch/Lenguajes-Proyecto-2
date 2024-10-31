package josecarlos.lenguajes.proyecto2.sintactico;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import josecarlos.lenguajes.proyecto2.Operaciones;
import josecarlos.lenguajes.proyecto2.ddl.Alter;
import josecarlos.lenguajes.proyecto2.ddl.Drop;
import josecarlos.lenguajes.proyecto2.ddl.Tabla;
import josecarlos.lenguajes.proyecto2.tokens.ListSyntaxError;
import josecarlos.lenguajes.proyecto2.enums.PalabraReservada;
import josecarlos.lenguajes.proyecto2.tokens.Token;
import josecarlos.lenguajes.proyecto2.tokens.TokenErrorSintaxis;

/**
 *
 * @author emahch
 */
public class AnalizadorSintactico {

    public static final String FIN_BLOQUE = ";";
    public static final String[] INICIO = {PalabraReservada.CREATE.name(), PalabraReservada.ALTER.name(), PalabraReservada.DROP.name(),
        PalabraReservada.INSERT.name(), PalabraReservada.UPDATE.name(), PalabraReservada.SELECT.name(), PalabraReservada.DELETE.name()};

    private final Token[] tokens;
    private int currentIndex;
    private ListSyntaxError tokensError;
    private AnalizadorDDL analizadorDDL;
    private AnalizadorDML analizadorDML;
    private Operaciones operaciones;

    public AnalizadorSintactico(List<Token> tokens) {
        this.currentIndex = 0;
        if (tokens.isEmpty()) {
            this.tokens = null;
            return;
        }
        this.operaciones = new Operaciones();
        this.tokensError = new ListSyntaxError();
        this.analizadorDDL = new AnalizadorDDL(tokensError, operaciones);
        this.analizadorDML = new AnalizadorDML(tokensError, operaciones);
        this.tokens = new Token[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            this.tokens[i] = tokens.get(i);
        }
    }

    private void next() {
        this.currentIndex++;
    }

    private boolean isEnd() {
        return this.currentIndex >= this.tokens.length;
    }

    public void analizar() {
        AnalizadorHelper helper = new AnalizadorHelper();
        while (!isEnd()) {
            if (!helper.analizarValor(this.tokens[currentIndex], INICIO)) {
                this.tokensError.add(new TokenErrorSintaxis(tokens[currentIndex], "Se esperaba un token de inicio DDL o DML"));
                next();
            } else {
                if (this.tokens[currentIndex].getValor().equals(PalabraReservada.CREATE.name())) {
                    analizadorDDL.analizarCreate(getTokensBloque());
                } else if (this.tokens[currentIndex].getValor().equals(PalabraReservada.ALTER.name())) {
                    analizadorDDL.analizarAlter(getTokensBloque());
                } else if (this.tokens[currentIndex].getValor().equals(PalabraReservada.DROP.name())) {
                    analizadorDDL.analizarDrop(getTokensBloque());
                } else if (this.tokens[currentIndex].getValor().equals(PalabraReservada.INSERT.name())) {
                    analizadorDML.analizarInsert(getTokensBloque());
                } else if (this.tokens[currentIndex].getValor().equals(PalabraReservada.SELECT.name())) {
                    analizadorDML.analizarSelect(getTokensBloque());
                } else if (this.tokens[currentIndex].getValor().equals(PalabraReservada.UPDATE.name())) {
                    analizadorDML.analizarUpdate(getTokensBloque());
                } else if (this.tokens[currentIndex].getValor().equals(PalabraReservada.DELETE.name())) {
                    analizadorDML.analizarDelete(getTokensBloque());
                }
            }
        }
        for (TokenErrorSintaxis tokenErrorSintaxis : tokensError) {
            System.out.println(tokenErrorSintaxis.print());
        }
        analizadorDDL.printDB();
    }

    private Pila getTokensBloque() {
        Pila tokensBloque = new Pila();
        Token token = tokens[currentIndex];

        while (!isEnd() && !token.getValor().equals(FIN_BLOQUE)) {
            tokensBloque.add(token);
            next();
            if (!isEnd()) {
                token = tokens[currentIndex];
            }
        }

        next();
        if (tokensBloque.isEmpty()) {
            return null;
        }
        return tokensBloque;
    }

    public void printTables() {
        JFrame frame = new JFrame("Tablas creadas");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 1, 1));
        panel.setBackground(Color.white);

        for (Tabla tablaCreada : analizadorDDL.getTablasCreadas()) {
            JLabel label = mostrarDiagrama(tablaCreada.generarDot());
            if (label != null) {
                panel.add(label);
            }
        }
        for (Alter alter : analizadorDDL.getTablasModificadas()) {
            JLabel label = mostrarDiagrama(alter.generarDot());
            if (label != null) {
                panel.add(label);
            }
        }
        for (Drop drop : analizadorDDL.getTablasBorradas()) {
            JLabel label = mostrarDiagrama(drop.generarDot());
            if (label != null) {
                panel.add(label);
            }
        }
        // Agregar el panel dentro de un JScrollPane para permitir desplazamiento
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Ajustar el tamaño de la ventana y hacerla visible
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private JLabel mostrarDiagrama(String dot) {
        try {
            // Parsear y renderizar cada gráfico DOT a un BufferedImage
            MutableGraph graph = new Parser().read(new ByteArrayInputStream(dot.getBytes()));
            BufferedImage image = Graphviz.fromGraph(graph).width(400).render(Format.PNG).toImage();

            JLabel label = new JLabel(new ImageIcon(image));
            return label;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ListSyntaxError getTokensError() {
        return tokensError;
    }

    public Operaciones getOperaciones() {
        return operaciones;
    }
    
}
