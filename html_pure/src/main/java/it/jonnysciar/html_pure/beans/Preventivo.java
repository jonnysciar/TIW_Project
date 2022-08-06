package it.jonnysciar.html_pure.beans;

import java.util.ArrayList;
import java.util.List;

public class Preventivo {

    private int id;
    private final int codice_prodotto;
    private String nomeProdotto;
    private final int id_utente;
    private int id_impiegato;
    private int prezzo;
    private List<Integer> opzioni;

    public Preventivo(int codice_prodotto, int id_utente, List<Integer> opzioni) {
        this.codice_prodotto = codice_prodotto;
        this.id_utente = id_utente;
        this.opzioni = new ArrayList<>(opzioni);
    }

    public Preventivo(int id, int codice_prodotto, int id_utente, int id_impiegato, int prezzo) {
        this.id = id;
        this.codice_prodotto = codice_prodotto;
        this.id_utente = id_utente;
        this.id_impiegato = id_impiegato;
        this.prezzo = prezzo;
    }

    public void setId_impiegato(int id_impiegato) {
        this.id_impiegato = id_impiegato;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCodice_prodotto() {
        return codice_prodotto;
    }

    public int getId_utente() {
        return id_utente;
    }

    public int getId_impiegato() {
        return id_impiegato;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public List<Integer> getOpzioni() {
        return new ArrayList<>(opzioni);
    }
}
