package it.jonnysciar.html_pure.beans;

public class Preventivo {

    private final int id;
    private final int codice_prodotto;
    private final int id_utente;
    private int id_impiegato;
    private int prezzo;

    public Preventivo(int id, int codice_prodotto, int id_utente) {
        this.id = id;
        this.codice_prodotto = codice_prodotto;
        this.id_utente = id_utente;
    }

    public void setId_impiegato(int id_impiegato) {
        this.id_impiegato = id_impiegato;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
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

}
