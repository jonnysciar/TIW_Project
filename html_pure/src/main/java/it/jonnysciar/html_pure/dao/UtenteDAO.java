package it.jonnysciar.html_pure.dao;

import it.jonnysciar.html_pure.beans.Utente;

import java.sql.*;

public class UtenteDAO {

    private final Connection connection;

    public UtenteDAO(Connection connection) {
        this.connection = connection;
    }

    public Utente checkCredentials(String username, String password) throws SQLException {
        String query = "SELECT  id, username, nome, cognome, impiegato FROM utenti WHERE username = ? AND password = ?";
        try (PreparedStatement pstatement = connection.prepareStatement(query)) {
            pstatement.setString(1, username);
            pstatement.setString(2, password);
            try (ResultSet result = pstatement.executeQuery()) {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return null;
                else {
                    result.next();
                    return new Utente(result.getInt("id"), result.getString("username"),
                            result.getString("nome"), result.getString("cognome"), result.getBoolean("impiegato"));
                }
            }
        }
    }

    public boolean addUtente(Utente utente, String password) throws SQLException {
        String usernameQuery = "SELECT username from utenti";
        try (Statement statement = connection.createStatement()) {
            try (ResultSet result = statement.executeQuery(usernameQuery)) {
                while (result.next()) {
                    if (result.getString("username").equals(utente.getUsername())) return false;
                }
            }
        }

        String insertQuery = "INSERT INTO utenti (username, password, nome, cognome, impiegato) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstatement = connection.prepareStatement(insertQuery)) {
            pstatement.setString(1, utente.getUsername());
            pstatement.setString(2, password);
            pstatement.setString(3, utente.getNome());
            pstatement.setString(4, utente.getCognome());
            pstatement.setBoolean(5, utente.isImpiegato());
            return pstatement.executeUpdate() != 0;
        }
    }

}
