package josecarlos.lenguajes.proyecto2;

/**
 *
 * @author emahch
 */
public class Operaciones {
    private int create;
    private int delete;
    private int update;
    private int select;
    private int alter;

    public int getCreate() {
        return create;
    }

    public void sumCreate() {
        this.create++;
    }

    public int getDelete() {
        return delete;
    }

    public void sumDelete() {
        this.delete++;
    }

    public int getUpdate() {
        return update;
    }

    public void sumUpdate() {
        this.update++;
    }

    public int getSelect() {
        return select;
    }

    public void sumSelect() {
        this.select++;
    }

    public int getAlter() {
        return alter;
    }

    public void sumAlter() {
        this.alter++;
    }
    
    
}
