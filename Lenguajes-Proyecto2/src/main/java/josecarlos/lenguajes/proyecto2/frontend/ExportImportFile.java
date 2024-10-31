package josecarlos.lenguajes.proyecto2.frontend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author emahch
 */
public class ExportImportFile {

    private File archivoSeleccionado;

    public ExportImportFile() {
    }

    /**
     * Metodo que permite seleccionar la ubicación y el nombre de donde se desea guardar el archivo
     * 
     * @param pathName
     * @return 
     */
    public boolean selectPath(String pathName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setSelectedFile(new File(pathName));

        int opcion = fileChooser.showSaveDialog(new JFrame());

        if (opcion == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = fileChooser.getSelectedFile();
        }
        return opcion == JFileChooser.APPROVE_OPTION;
    }
    
    public boolean openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);

        int opcion = fileChooser.showOpenDialog(new JFrame());

        if (opcion == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = fileChooser.getSelectedFile();
        }
        return opcion == JFileChooser.APPROVE_OPTION;
    }
    
    /**
     * Lee el archivo de entrada y lo convierte a una cadena de texto
     * @return string texto del archivo de entrada
     */
    public String recibirArchivoEntrada() {
        try (FileReader fileReader = new FileReader(archivoSeleccionado); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String textoRecibido = "";
            String linea = bufferedReader.readLine();
            while (linea != null) {
                textoRecibido = textoRecibido + linea + "\n";
                linea = bufferedReader.readLine();
            }
            return textoRecibido;
        } catch (IOException e) {
            System.out.println("Error al recibir el archivo de entrada");
            return null;
        }
    }
    
    public void writeFile(String texto){
        try (FileWriter fw = new FileWriter(archivoSeleccionado);
                BufferedWriter bf = new BufferedWriter(fw)) {
            bf.write(texto);
            bf.flush();
        } catch (Exception e) {
        }
    }

    public File getArchivoSeleccionado() {
        return archivoSeleccionado;
    }

    public void setArchivoSeleccionado(File archivoSeleccionado) {
        this.archivoSeleccionado = archivoSeleccionado;
    }
}
