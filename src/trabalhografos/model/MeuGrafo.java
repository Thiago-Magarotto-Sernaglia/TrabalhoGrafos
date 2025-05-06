package model;
import java.util.ArrayList;

public class MeuGrafo implements Grafo{
    
    private int tipo;
    public int num_arestas;

    public  ArrayList<Integer>[][] matrizAdjacencia;
    public  int[][] matrizIncidencia;
    public  ArrayList<ArrayList<ListaAdjacencia>> listaAdjacencia = new ArrayList<ArrayList<ListaAdjacencia>>();

    public ArrayList<Vertice> vertices = new ArrayList<>();
    public ArrayList<Aresta> arestas = new ArrayList<>();

    public MeuGrafo(MeuGrafo original) {
        this.tipo = original.tipo;
        this.num_arestas = original.num_arestas;
        this.matrizAdjacencia = original.matrizAdjacencia;
        this.matrizIncidencia = original.matrizIncidencia;
        this.listaAdjacencia = original.listaAdjacencia;
        this.vertices = original.vertices;
        this.arestas = original.arestas;
    }

    public MeuGrafo(int tipo) {
        this.tipo = tipo;
    }

    int getTipo(){
        return tipo;
    }

    @Override
    public void adicionarAresta(Vertice origem, Vertice destino) throws Exception {

        if(existeVertice(origem) && existeVertice(destino)){

            Aresta novAresta = new Aresta(origem, destino);
            arestas.add(novAresta);

        }else{
            throw new UnsupportedOperationException("Aresta não pode ser adicionada!");
        }

        if (getTipo() == 1) {

            if(matrizAdjacencia == null){
                matrizAdjacencia = new ArrayList[numeroDeVertices()][numeroDeVertices()];
                for (int i = 0; i < numeroDeVertices(); i++) {
                    for (int j = 0; j < numeroDeVertices(); j++) {
                        matrizAdjacencia[i][j]=new ArrayList<Integer>();
                        matrizAdjacencia[i][j].add(Integer.MAX_VALUE);
                    }
                }
            }

            if(origem.id() == destino.id()){
                matrizAdjacencia[destino.id()][origem.id()].add(1);
                matrizAdjacencia[destino.id()][origem.id()].remove(Integer.MAX_VALUE);
            }else{
                matrizAdjacencia[origem.id()][destino.id()].add(-1);
                matrizAdjacencia[origem.id()][destino.id()].remove(Integer.MAX_VALUE);
                matrizAdjacencia[destino.id()][origem.id()].add(1);
                matrizAdjacencia[destino.id()][origem.id()].remove(Integer.MAX_VALUE);
            }


        }else if(getTipo() == 2){

            if(matrizIncidencia == null){
                matrizIncidencia = new int[numeroDeVertices()][numeroDeArestas()];
                for (int i = 0; i < numeroDeVertices(); i++) {
                    for (int j = 0; j < numeroDeArestas(); j++) {
                        matrizIncidencia[i][j] = Integer.MAX_VALUE;
                    }
                }
            }
            
            matrizIncidencia[origem.id()][arestas.size()-1] = -1;
            matrizIncidencia[destino.id()][arestas.size()-1] = 1;
            

        }else if(getTipo() == 3){

            if(listaAdjacencia.isEmpty()){
                for (int i = 0; i < numeroDeVertices(); i++) {
                    listaAdjacencia.add(new ArrayList<ListaAdjacencia>());
                }
            }
       
            listaAdjacencia.get(origem.id()).add(new ListaAdjacencia(destino.id(), 1));

        }
    }

    @Override
    public void adicionarAresta(Vertice origem, Vertice destino, double peso) throws Exception {

        if(existeVertice(origem) && existeVertice(destino)){

            Aresta novAresta = new Aresta(origem, destino, peso);
            arestas.add(novAresta);

        }else{
            throw new UnsupportedOperationException("Aresta não pode ser adicionada!");
        }

        if (getTipo() == 1) { 

            if(matrizAdjacencia == null){
                matrizAdjacencia = new ArrayList[numeroDeVertices()][numeroDeVertices()];
                for (int i = 0; i < numeroDeVertices(); i++) {
                    for (int j = 0; j < numeroDeVertices(); j++) {
                        matrizAdjacencia[i][j]=new ArrayList<Integer>();
                        matrizAdjacencia[i][j].add(Integer.MAX_VALUE);
                    }
                }
            }

            if(origem.id() == destino.id()){
                matrizAdjacencia[destino.id()][origem.id()].add((int)peso*-1);
                if(matrizAdjacencia[destino.id()][origem.id()].contains(Integer.MAX_VALUE)){
                    matrizAdjacencia[destino.id()][origem.id()].remove(matrizAdjacencia[destino.id()][origem.id()].indexOf(Integer.MAX_VALUE));
                }
            }else{
                matrizAdjacencia[origem.id()][destino.id()].add((int)peso*-1);
                if(matrizAdjacencia[origem.id()][destino.id()].contains(Integer.MAX_VALUE)){
                    matrizAdjacencia[origem.id()][destino.id()].remove(matrizAdjacencia[origem.id()][destino.id()].indexOf(Integer.MAX_VALUE));
                }
                matrizAdjacencia[destino.id()][origem.id()].add((int)peso);
                if(matrizAdjacencia[destino.id()][origem.id()].contains(Integer.MAX_VALUE)){
                    matrizAdjacencia[destino.id()][origem.id()].remove(matrizAdjacencia[destino.id()][origem.id()].indexOf(Integer.MAX_VALUE));
                }
            }

        }else if(getTipo() == 2){

            if(matrizIncidencia == null){
                matrizIncidencia = new int[numeroDeVertices()][numeroDeArestas()];
                for (int i = 0; i < numeroDeVertices(); i++) {
                    for (int j = 0; j < numeroDeArestas(); j++) {
                        matrizIncidencia[i][j] = Integer.MAX_VALUE;
                    }
                }
            }
            
            matrizIncidencia[origem.id()][arestas.size()-1] = (int)peso*-1;
            matrizIncidencia[destino.id()][arestas.size()-1] = (int)peso;

        }else if(getTipo() == 3){

            if(listaAdjacencia.isEmpty()){
                for (int i = 0; i < numeroDeVertices(); i++) {
                    listaAdjacencia.add(new ArrayList<ListaAdjacencia>());
                }
            }
       
            listaAdjacencia.get(origem.id()).add(new ListaAdjacencia(destino.id(), (int)peso));

        }
    }

    public boolean existeVertice(Vertice vertice){

        for (Vertice v : vertices) {
            if(v == vertice){
                return true;
            }
        }
        return false;
    }

    public Vertice buscaVertice(int id){
        for (Vertice v : vertices) {
            if(v.id() == id){
                return v;
            }
        }
        return null;
    }

    public Aresta buscaAresta(Vertice origem, Vertice destino){

        for (Aresta aresta : arestas) {
            if(aresta.origem() == origem && aresta.destino() == destino){
                return aresta;
            }
        }
        return null;
    }

    public Aresta buscaAresta(Vertice origem, Vertice destino, double peso){
        for (Aresta aresta : arestas) {
            if(aresta.origem() == origem && aresta.destino() == destino && aresta.peso() == peso){
                return aresta;
            }
        }
        return null;
    }

    @Override
    public boolean existeAresta(Vertice origem, Vertice destino) {

        if(existeVertice(origem) && existeVertice(destino)){

            for (Aresta aresta : arestas) {
                if(aresta.origem().equals(origem) && aresta.destino().equals(destino)){
                    return true;
                }
            }

            return false;
        }else{
            
        throw new UnsupportedOperationException("Impossível realizar operação, origem ou destino inexistentes!");
        }
    }

    @Override
    public int grauDoVertice(Vertice vertice) throws Exception {
        int grau = 0;
        if(existeVertice(vertice)){
            if(getTipo() == 1){
                for (int i = 0; i < numeroDeVertices(); i++) {
                    if(!matrizAdjacencia[vertice.id()][i].contains(Integer.MAX_VALUE)){
                        grau += matrizAdjacencia[vertice.id()][i].size();
                    }
                }
            }else if(getTipo() == 2){
                for (int i = 0; i < numeroDeArestas(); i++) {
                    if(matrizIncidencia[vertice.id()][i] != Integer.MAX_VALUE){
                        grau++;
                    }
                }
            }else if(getTipo() == 3){
                int i = 0;
                for (ArrayList<ListaAdjacencia> lista : listaAdjacencia) {
                    if(vertice.id() != i){
                        for (ListaAdjacencia listaAdjacencia : lista) {
                            if(listaAdjacencia.destino == vertice.id()){
                                grau++;
                            }
                        }
                    }
                    i++;
                }
                grau += listaAdjacencia.get(vertice.id()).size();
            }
        }else{
            throw new UnsupportedOperationException("Unimplemented method 'grauDoVertice'");
        }
        return grau;
    }

    @Override
    public int numeroDeVertices() {
        return vertices.size();
    }

    @Override
    public int numeroDeArestas(){
        return num_arestas;
    }

    @Override
    public ArrayList<Vertice> adjacentesDe(Vertice vertice) throws Exception {
        ArrayList<Vertice> verticeAdjacentes = new ArrayList<Vertice>();
        if(existeVertice(vertice)){
            if(getTipo() == 1){
                for (int i = 0; i < numeroDeVertices(); i++) {
                    if(!matrizAdjacencia[vertice.id()][i].contains(Integer.MAX_VALUE)){
                        for (Integer v : matrizAdjacencia[vertice.id()][i]) {
                            if(v < 0){
                                verticeAdjacentes.add(vertices.get(i));
                                break;
                            }   
                        }
                    }
                }
            }else if(getTipo() == 2){
                for (int i = 0; i < numeroDeArestas(); i++) {
                    if(matrizIncidencia[vertice.id()][i] != Integer.MAX_VALUE){
                        int contador=0;
                        for (int j = 0; j < numeroDeVertices(); j++) {
                            if(vertice.id() != j && matrizIncidencia[j][i] != Integer.MAX_VALUE){
                                if(matrizIncidencia[j][i] > 0){
                                    if(!verticeAdjacentes.contains(vertices.get(j))){
                                        verticeAdjacentes.add(vertices.get(j));
                                    }
                                }
                                contador++;
                            }
                        }
                        if (contador == 0) {
                            verticeAdjacentes.add(vertice);
                        }
                    }
                }
            }else if(getTipo() == 3){
                for (ListaAdjacencia lista : listaAdjacencia.get(vertice.id())) {
                    verticeAdjacentes.add(vertices.get(lista.destino));
                }
            }
        }else{
            throw new UnsupportedOperationException("Unimplemented method 'adjacentesDe'");
        }
        return verticeAdjacentes;
    }

    @Override
    public void setarPeso(Vertice origem, Vertice destino, double peso) throws Exception {
        if((existeVertice(origem) && existeVertice(destino)) && existeAresta(origem, destino)){
        
            for (Aresta aresta : arestas) {
                if(aresta.origem().equals(origem) && aresta.destino().equals(destino)){
                    aresta.setarPeso(peso);
                    return;
                }
            }

        }else{
            
            throw new UnsupportedOperationException("Impossível realizar operação, origem ou destino inexistentes!");
        }
    }

    @Override
    public ArrayList<Aresta> arestasEntre(Vertice origem, Vertice destino) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'arestasEntre'");
    }

    @Override
    public ArrayList<Vertice> vertices() {
        return vertices;
    }

    public ArrayList<Aresta> arestas() {
        return arestas;
    } 
    
}
