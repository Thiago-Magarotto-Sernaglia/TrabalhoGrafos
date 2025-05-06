package model;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

public enum TipoDeRepresentacao {
    MATRIZ_DE_ADJACENCIA(1), 
    MATRIZ_DE_INCIDENCIA(2), 
    LISTA_DE_ADJACENCIA(3);
    
    private int tipo;

    TipoDeRepresentacao(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo(){
        return tipo;
    }

}
