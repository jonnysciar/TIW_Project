package it.jonnysciar.javascript.beans;

public class Utente {

    private int id;
    private String username;
    private String nome;
    private String cognome;
    private String email;
    private boolean impiegato;

    public Utente(int id, String username, String nome, String cognome, String email, boolean impiegato) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.impiegato = impiegato;
    }

    public Utente(String username, String nome, String cognome, String email, boolean impiegato) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
