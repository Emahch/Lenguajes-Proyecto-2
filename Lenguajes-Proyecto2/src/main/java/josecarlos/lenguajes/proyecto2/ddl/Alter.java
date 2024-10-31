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
    
    
}
