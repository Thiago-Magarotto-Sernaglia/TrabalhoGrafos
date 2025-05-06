package service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import model.Aresta;
import model.Grafo;
import model.MeuGrafo;
import model.TipoDeRepresentacao;
import model.Vertice;

public class MeusAlgoritmosEmGrafos{

    public static int[] cor;
    public static int[] d;
    public static int[] f;
    public static Vertice[] pai;
    public static int tempo;
    public ArrayList<Aresta> arestasArvore;
    public ArrayList<Aresta> arestasRetorno;
    public ArrayList<Aresta> arestasAvanco;
    public ArrayList<Aresta> arestasCruzamento;

    public double fluxoMaximo;

    public MeuGrafo carregarGrafo(String path, TipoDeRepresentacao t){
        ArrayList<String> dadosExemplo = FileManager.stringReader(path);
        int ti = t.getTipo();
        MeuGrafo grafo = new MeuGrafo(ti);
        int num_vertices = Integer.parseInt(dadosExemplo.get(0));

        for (int i = 1; i <= num_vertices; i++) {   // trecho adiciona os vertices ao grafo
            
            String linha = dadosExemplo.get(i).replace(";", "");
            //linha = linha.replace("-", "");
            String partes_linha[] = linha.split(" ");

            Vertice vertice = new Vertice(Integer.parseInt(partes_linha[0]));
            
            grafo.vertices.add(vertice);

        }
        int num_arestas = 0;
        for (int i = 1; i <= num_vertices; i++) {   // trecho adiciona as arestas ao grafo
            
            String linha = dadosExemplo.get(i).replace(";", "");
            //linha = linha.replace("-", "");
            String partes_linha[] = linha.split(" ");
            num_arestas += partes_linha.length -1;
        }

        grafo.num_arestas = num_arestas;

        for (int i = 1; i <= num_vertices; i++) {   // trecho adiciona as arestas ao grafo
            
            String linha = dadosExemplo.get(i).replace(";", "");
            //linha = linha.replace("-", "");
            String partes_linha[] = linha.split(" ");

            for (int j = 1; j < partes_linha.length; j++) {
                Vertice origem = grafo.buscaVertice(Integer.parseInt(partes_linha[0]));
                String aresta[] = partes_linha[j].split("-");
                Vertice destino = grafo.buscaVertice(Integer.parseInt(aresta[0]));
                double peso = Integer.parseInt(aresta[1]);

                try {
                    grafo.adicionarAresta(origem, destino, peso);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return grafo;
    }

    public void buscaEmLargura(MeuGrafo g, Vertice vertice) throws Exception {
        cor = new int[g.numeroDeVertices()];
        d = new int[g.numeroDeVertices()];
        pai = new Vertice[g.numeroDeVertices()];
        
        for (Vertice v : g.vertices) {
            cor[v.id()] = 0;
            d[v.id()] = Integer.MAX_VALUE;
            pai[v.id()] = null;
        }
        cor[vertice.id()] = 1;
        d[vertice.id()] = 0;
        pai[vertice.id()] = null;

        Queue<Vertice> Q = new LinkedList<Vertice>();
        Q.offer(vertice);

        while (!Q.isEmpty()) {
            Vertice v = Q.poll();
            ArrayList<Vertice> vAdjc = g.adjacentesDe(v);
            for (Vertice vtc : vAdjc) {
                if(cor[vtc.id()] == 0){
                    cor[vtc.id()] = 1;
                    d[vtc.id()] = d[v.id()] + 1;
                    pai[vtc.id()] = v;
                    Q.offer(vtc);
                }
            }
            cor[v.id()] = 2;
        }
    }

    public void buscaEmLarguraFM(MeuGrafo g, Vertice vertice) throws Exception {
        cor = new int[g.numeroDeVertices()];
        d = new int[g.numeroDeVertices()];
        pai = new Vertice[g.numeroDeVertices()];
        
        for (Vertice v : g.vertices) {
            cor[v.id()] = 0;
            d[v.id()] = Integer.MAX_VALUE;
            pai[v.id()] = null;
        }
        cor[vertice.id()] = 1;
        d[vertice.id()] = 0;
        pai[vertice.id()] = null;

        Queue<Vertice> Q = new LinkedList<Vertice>();
        Q.offer(vertice);

        while (!Q.isEmpty()) {
            Vertice v = Q.poll();
            ArrayList<Vertice> vAdjc = g.adjacentesDe(v);
            vAdjc.sort((Vertice v1, Vertice v2) -> Double.compare(g.buscaAresta(v, v2).peso(), g.buscaAresta(v, v1).peso()));
            for (Vertice vtc : vAdjc) {
            Aresta aresta = g.buscaAresta(v, vtc);
                if(cor[vtc.id()] == 0 && aresta.peso() != 0){
                    cor[vtc.id()] = 1;
                    d[vtc.id()] = d[v.id()] + 1;
                    pai[vtc.id()] = v;
                    Q.offer(vtc);
                }
            }
            cor[v.id()] = 2;
        }
    }

    public Collection<Aresta> buscaEmProfundidade(MeuGrafo g, Vertice vertice){
        cor = new int[g.numeroDeVertices()];
        d = new int[g.numeroDeVertices()];
        f = new int[g.numeroDeVertices()];
        pai = new Vertice[g.numeroDeVertices()];
        Collection<Aresta> arestasFloresta = new ArrayList<Aresta>();
        arestasArvore = new ArrayList<Aresta>();
        arestasRetorno = new ArrayList<Aresta>();
        arestasAvanco = new ArrayList<Aresta>();
        arestasCruzamento = new ArrayList<Aresta>();
        for (Vertice v : g.vertices()) {
            cor[v.id()] = 0;
            pai[v.id()] = null;
        }    
        tempo=0;

        try {
            arestasFloresta = buscaEmProfundidadeVisit(g, vertice, arestasFloresta);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Vertice v : g.vertices()) {
           if( cor[v.id()] == 0){
                try {
                    arestasFloresta = buscaEmProfundidadeVisit(g, v, arestasFloresta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
           }
        }

        return arestasFloresta;
        
    }

    public Collection<Aresta> buscaEmProfundidadeVisit(MeuGrafo g, Vertice v, Collection<Aresta> arestasFloresta) throws Exception{
        cor[v.id()] = 1;
        tempo++;
        d[v.id()] = tempo;
        ArrayList<Vertice> vAdjc = g.adjacentesDe(v);
        for (Vertice vertice : vAdjc) {
            Aresta aresta = g.buscaAresta(v, vertice);
            if(cor[vertice.id()] == 0 && aresta.peso() != 0){
                pai[vertice.id()] = v; 
                aresta.setTipo("Árvore");
                arestasFloresta.add(aresta);
                arestasArvore.add(aresta);
                buscaEmProfundidadeVisit(g, vertice, arestasFloresta);
            }else{
                if(ehAncestral(v, vertice)){
                    aresta.setTipo("Retorno");
                    arestasFloresta.add(aresta);
                    arestasRetorno.add(aresta);
                }else if(ehAncestral(vertice, v)){
                    aresta.setTipo("Avanço");
                    arestasFloresta.add(aresta);
                    arestasAvanco.add(aresta);
                }else{
                    aresta.setTipo("Cruzamento");
                    arestasFloresta.add(aresta);
                    arestasCruzamento.add(aresta);
                }
            }
        }
        cor[v.id()] = 2;
        tempo++;
        f[v.id()] = tempo;
        return arestasFloresta;
    }

    public Collection<Aresta> dfsFM(MeuGrafo g, Vertice vertice){
        cor = new int[g.numeroDeVertices()];
        d = new int[g.numeroDeVertices()];
        f = new int[g.numeroDeVertices()];
        pai = new Vertice[g.numeroDeVertices()];
        Collection<Aresta> arestasFloresta = new ArrayList<Aresta>();
        arestasArvore = new ArrayList<Aresta>();
        arestasRetorno = new ArrayList<Aresta>();
        arestasAvanco = new ArrayList<Aresta>();
        arestasCruzamento = new ArrayList<Aresta>();
        for (Vertice v : g.vertices()) {
            cor[v.id()] = 0;
            pai[v.id()] = null;
        }    
        tempo=0;

        try {
            arestasFloresta = dfsFMVisit(g, vertice, arestasFloresta);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Vertice v : g.vertices()) {
           if( cor[v.id()] == 0){
                try {
                    arestasFloresta = dfsFMVisit(g, v, arestasFloresta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
           }
        }

        return arestasFloresta;
        
    }

    public Collection<Aresta> dfsFMVisit(MeuGrafo g, Vertice v, Collection<Aresta> arestasFloresta) throws Exception{
        cor[v.id()] = 1;
        tempo++;
        d[v.id()] = tempo;
        ArrayList<Vertice> vAdjc = g.adjacentesDe(v);
        vAdjc.sort((Vertice v1, Vertice v2) -> Double.compare(g.buscaAresta(v, v2).peso(), g.buscaAresta(v, v1).peso()));
        for (Vertice vertice : vAdjc) {
            Aresta aresta = g.buscaAresta(v, vertice);
            if(cor[vertice.id()] == 0 && aresta.peso() != 0){
                pai[vertice.id()] = v; 
                aresta.setTipo("Aresta de Árvore");
                arestasFloresta.add(aresta);
                arestasArvore.add(aresta);
                dfsFMVisit(g, vertice, arestasFloresta);
            }else{
                if(ehAncestral(v, vertice)){
                    aresta.setTipo("Aresta de Retorno");
                    arestasFloresta.add(aresta);
                    arestasRetorno.add(aresta);
                }else if(ehAncestral(vertice, v)){
                    aresta.setTipo("Aresta de Avanço");
                    arestasFloresta.add(aresta);
                    arestasAvanco.add(aresta);
                }else{
                    aresta.setTipo("Aresta de Cruzamento");
                    arestasFloresta.add(aresta);
                    arestasCruzamento.add(aresta);
                }
            }
        }
        cor[v.id()] = 2;
        tempo++;
        f[v.id()] = tempo;
        return arestasFloresta;
    }

    public boolean ehAncestral(Vertice v1, Vertice v2){
        boolean retorno;
        if(v1 == v2){
            return true;
        }
        if(pai[v1.id()] == null){
            return false;
        }
        if(pai[v1.id()] == v2){
            return true;
        }else{
            Vertice vAtual = pai[v1.id()];
            retorno = ehAncestral(vAtual, v2);
        }

        return retorno;

    }


    public ArrayList<Aresta> menorCaminho(MeuGrafo g, Vertice origem, Vertice destino) throws Exception {
        
        d = new int[g.numeroDeVertices()];
        pai = new Vertice[g.numeroDeVertices()];

        for (Vertice v : g.vertices()) {
            d[v.id()] = Integer.MAX_VALUE;
            pai[v.id()] = null;
        }
        d[origem.id()] = 0;

        ArrayList<Vertice> S = new ArrayList<Vertice>();
        ArrayList<Vertice> Q = new ArrayList<Vertice>();
        for (Vertice v : g.vertices()) {
            Q.add(v);
        }
        ArrayList<Aresta> arestasCaminhoMinimo = new ArrayList<Aresta>();

        Vertice u;

        // for (Vertice v : g.adjacentesDe(origem)) {
        //     relaxa(origem, v, g.buscaAresta(origem, v));
        //     Q.remove(origem);
        //     S.add(origem);
        //     if(S.contains(destino)){
        //         arestasCaminhoMinimo.add(g.buscaAresta(origem, v));
        //         return arestasCaminhoMinimo;
        //     }
        // }

        while(!Q.isEmpty()){
            Q = extrairMinimo(Q);
            u = Q.removeFirst();
            S.add(u);
            if(S.contains(destino)){
                break;
            }
            for (Vertice v : g.adjacentesDe(u)) {
                relaxa(u, v, g.buscaAresta(u, v));
            }
        }

        Vertice o = new Vertice(Integer.MIN_VALUE);
        Vertice d = new Vertice(Integer.MIN_VALUE);

        for (int i = 0; i < g.numeroDeVertices(); i++) {
            if(i==0){
                o = pai[destino.id()];
                arestasCaminhoMinimo.add(g.buscaAresta(o, destino));
            }else{
                d = o;
                o = pai[o.id()];
                arestasCaminhoMinimo.add(g.buscaAresta(o, d));
            }

            if(o == null){
                throw new UnsupportedOperationException("Caminho Impossivel");
            }

            if(o == origem){
                return arestasCaminhoMinimo;
            }
            
        }
        throw new UnsupportedOperationException("Caminho Impossivel");
    }

    public ArrayList<Vertice> extrairMinimo(ArrayList<Vertice> Q){
        Q.sort((Vertice v1, Vertice v2) -> Integer.compare(d[v1.id()], d[v2.id()]));
        return Q;
    } 

    public void relaxa(Vertice origem, Vertice destino, Aresta aresta){
        if(d[destino.id()] > (d[origem.id()] + (int)aresta.peso())){
            d[destino.id()] = d[origem.id()] + (int)aresta.peso();
            pai[destino.id()] = origem;
        }
    }


    public boolean existeCiclo(Grafo g) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existeCiclo'");
    }


    public Collection<Aresta> agmUsandoKruskall(MeuGrafo g) {
        Collection<Aresta> arestasAGM = new ArrayList<Aresta>();
        ArrayList<Vertice>[] conjuntos = new ArrayList[g.numeroDeVertices()];

        for (int i = 0; i < g.numeroDeVertices(); i++) {
            conjuntos[i] = new ArrayList<Vertice>();
            conjuntos[i].add(g.vertices.get(i));
        }

        ArrayList<Aresta> arestasOrdenadas = g.arestas();
        arestasOrdenadas.sort((Aresta a1, Aresta a2) -> Double.compare(a1.peso(), a2.peso()));

        for (Aresta aresta : arestasOrdenadas) {
            if(!conjuntos[aresta.origem().id()].contains(aresta.destino())){
                arestasAGM.add(aresta);
                for (Vertice v : conjuntos[aresta.destino().id()]) {
                    if(!conjuntos[aresta.origem().id()].contains(v)){
                        conjuntos[aresta.origem().id()].add(v);
                    }
                    if(v.id() != aresta.destino().id()){
                        conjuntos[v.id()] = conjuntos[aresta.origem().id()];
                    }
                }
                conjuntos[aresta.destino().id()] = conjuntos[aresta.origem().id()];
            }
        }
        return arestasAGM;
    }

    public double fluxoMaximo(MeuGrafo g, Vertice origem, Vertice destino){
        double fm = 0;
        MeuGrafo grafoResidual = new MeuGrafo(g);
        ArrayList<Aresta> arestasCaminhoMinimo = new ArrayList<Aresta>();

        //dfsFM(grafoResidual, origem);
        try {
            buscaEmLarguraFM(grafoResidual, origem);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(ehAncestral(destino, origem)){
        
            Vertice o = new Vertice(Integer.MIN_VALUE);
            Vertice d = new Vertice(Integer.MIN_VALUE);
            arestasCaminhoMinimo.clear();

            for (int i = 0; i < grafoResidual.numeroDeVertices(); i++) {
                if(i==0){
                    o = pai[destino.id()];
                    arestasCaminhoMinimo.add(grafoResidual.buscaAresta(o, destino));
                }else{
                    d = o;
                    o = pai[o.id()];
                    arestasCaminhoMinimo.add(grafoResidual.buscaAresta(o, d));
                }

                if(o == origem){

                    double pesoMin = Double.MAX_VALUE;
                    for (Aresta a : arestasCaminhoMinimo) {
                        pesoMin = Double.min(pesoMin, a.peso());
                    }
                    for (Aresta a : arestasCaminhoMinimo) {
                        a.setarPeso(a.peso() - pesoMin);
                    }
                    for (Aresta a : arestasCaminhoMinimo) {
                        if(a.peso() == 0){
                            arestasCaminhoMinimo.remove(a);
                            break;
                        }
                    }
                    fm += pesoMin;
                    break;
                }
                
            }
            //dfsFM(grafoResidual, origem);
            try {
                buscaEmLarguraFM(grafoResidual, origem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fm;
    }

    // public double fluxoMaximo(MeuGrafo g, Vertice origem, Vertice destino){
    //     fluxoMaximo = 0;
    //     cor = new int[g.numeroDeVertices()];
    //     ArrayList<Aresta> arestasRedeResidual = new ArrayList<Aresta>();
    //     boolean existeCaminhoAumentante = false;
    //     do{
    //         for (Vertice v : g.vertices()) {
    //             cor[v.id()] = 0;
    //         }
    //         try {
    //             arestasRedeResidual = fluxoMaximoVisit(g, origem, origem, destino, arestasRedeResidual);
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }
    //         for (Aresta aresta : arestasRedeResidual) {
    //             if(aresta.destino() == destino){
    //                 existeCaminhoAumentante = true;
    //             }
    //         }
    //     }while(existeCaminhoAumentante);
        

    //     return fluxoMaximo;
    // }

    // public ArrayList<Aresta> fluxoMaximoVisit(MeuGrafo g, Vertice origem, Vertice v, Vertice destino, ArrayList<Aresta> arestasRedeResidual) throws Exception{
    //     cor[v.id()] = 1;
    //     ArrayList<Vertice> vAdjc = g.adjacentesDe(v);
    //     for (Vertice vertice : vAdjc) {
    //         if(v == origem){
    //             arestasRedeResidual.clear();
    //         }
    //         Aresta aresta = new Aresta(g.buscaAresta(v, vertice));
    //         if(cor[vertice.id()] == 0  && aresta.peso() != 0 && vertice.id() != destino.id()){
    //             arestasRedeResidual.add(aresta);
    //             fluxoMaximoVisit(g, origem, vertice, destino, arestasRedeResidual);
    //         }
    //         if(vertice.id() == destino.id() && aresta.peso() != 0){
    //             arestasRedeResidual.add(aresta);
    //             double pesoMin = Double.MAX_VALUE;
    //             for (Aresta a : arestasRedeResidual) {
    //                 pesoMin = Double.min(pesoMin, a.peso());
    //             }
    //             for (Aresta a : arestasRedeResidual) {
    //                 a.setarPeso(a.peso() - pesoMin);
    //             }
    //             fluxoMaximo += pesoMin;
    //             return arestasRedeResidual;
    //         }
    //         arestasRedeResidual.remove(aresta);
    //     }
        
    //     cor[v.id()] = 2;
    //     return arestasRedeResidual;
    // }


    public double custoDaArvoreGeradora(Grafo g, Collection<Aresta> arestas) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'custoDaArvoreGeradora'");
    }


    public boolean ehArvoreGeradora(Grafo g, Collection<Aresta> arestas) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ehArvoreGeradora'");
    }


    public ArrayList<Aresta> caminhoMaisCurto(Grafo g, Vertice origem, Vertice destino) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'caminhoMaisCurto'");
    }


    public double custoDoCaminho(Grafo g, ArrayList<Aresta> arestas, Vertice origem, Vertice destino) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'custoDoCaminho'");
    }


    public boolean ehCaminho(ArrayList<Aresta> arestas, Vertice origem, Vertice destino) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ehCaminho'");
    }
    
}
