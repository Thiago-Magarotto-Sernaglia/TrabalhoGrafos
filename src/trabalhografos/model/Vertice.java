package model;
public class Vertice {
    private int vertice;
    
    public Vertice( int v ){
        this.vertice = v;
    }

    public int id() {
        return vertice;
    }

    public void setarVertice(int vertice) {
        this.vertice = vertice;
    }
    
    // Construtor de cópia para o Vertice
    public Vertice(Vertice v) {
        this.vertice = v.vertice;
    }

}
