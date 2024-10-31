package josecarlos.lenguajes.proyecto2.dml;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emahch
 */
public class Insert {
    private String idTabla;
    private List<String> columnas;
    private List<List<String>> conjuntoDatos;

    public Insert() {
        this.columnas = new ArrayList<>();
        this.conjuntoDatos = new ArrayList<>();
    }

    public String getIdTabla() {
        return idTabla;
    }

    public List<String> getColumnas() {
        return columnas;
    }

    public List<List<String>> getConjuntoDatos() {
        return conjuntoDatos;
    }

    public void setIdTabla(String idTabla) {
        this.idTabla = idTabla;
    }
    
    public void addColumna(String columna){
        this.columnas.add(columna);
    }

    public void setColumnas(List<String> columnas) {
        this.columnas = columnas;
    }
    
    public void addConjuntoDatos(List<String> datos){
        this.conjuntoDatos.add(datos);
    }
    
    @Override
    public String toString(){
        return "INSERT " + idTabla + "\n" + "Columnas: " + formatColumns() + "\n" + "Datos: " + formatDatos();
    }
    
    private String formatColumns(){
        if (this.columnas.isEmpty()){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String columna : columnas) {
            String columnaFormat = columna + ", ";
            sb.append(columnaFormat);
        }
        return sb.toString();
    }
    
    private String formatDatos(){
        if (this.conjuntoDatos.isEmpty()){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (List<String> datos : conjuntoDatos) {
            sb.append("( ");
            for (String dato : datos) {
                String datoFormat = dato + ", ";
                sb.append(datoFormat);
            }
            sb.append("), ");
        }
        return sb.toString();
    }
}
