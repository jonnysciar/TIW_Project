package it.jonnysciar.html_pure.beans;

public class Utente {

    private int id;
    private String username;
    private String nome;
    private String cognome;
    private boolean impiegato;


    public Utente(int id, String username, String nome, String cognome, boolean impiegato) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.impiegato = impiegato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public boolean isImpiegato() {
        return impiegato;
    }

    public void setImpiegato(boolean impiegato) {
        this.impiegato = impiegato;
    }
}
