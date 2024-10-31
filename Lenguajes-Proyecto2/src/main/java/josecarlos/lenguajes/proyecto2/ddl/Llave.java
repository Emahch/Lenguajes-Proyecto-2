package josecarlos.lenguajes.proyecto2.ddl;

/**
 *
 * @author emahch
 */
public class Llave{
    private String identificador;
    private String foreignKey;
    private String reference;
    private String idReference;

    public Llave() {
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getIdReference() {
        return idReference;
    }

    public void setIdReference(String idReference) {
        this.idReference = idReference;
    }
    
    
}
