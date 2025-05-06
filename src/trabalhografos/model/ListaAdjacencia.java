package model;
public class ListaAdjacencia {
    
    int destino;  
    int peso;     
    
    public ListaAdjacencia(int destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }

    // Método para exibir a aresta (usado para debug)
    @Override
    public String toString() {
        return "" + destino + "-" + peso + "";
    }

}
