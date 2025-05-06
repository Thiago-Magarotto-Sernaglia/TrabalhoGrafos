package model;
public class Aresta {

    private Vertice origem;
    private Vertice destino;
    private double peso;
    private String tipo = "Ñ dfnd";
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Aresta( Vertice origem, Vertice destino ){
        this.origem = origem;
        this.destino = destino;
        this.peso = 1;
    }
    
    public Aresta( Vertice origem, Vertice destino, double peso ){
        this.origem = origem;
        this.destino = destino;
        this.peso = peso;
    }

    public Vertice origem() {
        return origem;
    }

    public void setarOrigem(Vertice origem) {
        this.origem = origem;
    }

    public Vertice destino() {
        return destino;
    }

    public void setarDestino(Vertice destino) {
        this.destino = destino;
    }

    public double peso() {
        return peso;
    }

    public void setarPeso(double peso) {
        this.peso = peso;
    }
    
    // Método para exibir a aresta (usado para debug)
    @Override
    public String toString() {
        return "(Origem: " + origem.id() + ", Destino: " + destino.id() + ", Peso: " + peso + ", Tipo: " + tipo + ")";
    }

    // Método de cópia profunda
    public Aresta(Aresta original) {
        this.peso = original.peso;
        this.tipo = original.tipo;
        // Aqui fazemos uma cópia profunda do objeto referenciado
        this.origem = new Vertice(original.origem);
        this.destino = new Vertice(original.destino);
    }
}
