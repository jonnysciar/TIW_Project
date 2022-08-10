package it.jonnysciar.javascript.beans;

import it.jonnysciar.javascript.enums.TipoOpzione;

public class Opzione {

    private final int codice;
    private final String nome;
    private final TipoOpzione tipo;

    public Opzione(int codice, String nome, TipoOpzione tipo) {
        this.codice = codice;
        this.nome = nome;
        this.tipo = tipo;
    }

    public int getCodice() {
        return codice;
    }

    public String getNome() {
        return nome;
    }

    public TipoOpzione getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "Opzione{" +
                "codice=" + codice +
                ", nome='" + nome + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}
