package it.jonnysciar.html_pure.dao;

import it.jonnysciar.html_pure.beans.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAO extends DAO{

    public UtenteDAO(Connection connection) {
        super(connection);
    }

    public Utente checkCredentials(String username, String password) throws SQLException {
        String query = "SELECT  id, username, nome, cognome, email, impiegato FROM utenti WHERE username = ? AND password = ?";
        try (PreparedStatement pstatement = connection.prepareStatement(query)) {
            pstatement.setString(1, username);
            pstatement.setString(2, password);
            try (ResultSet result = pstatement.executeQuery()) {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return null;
                else {
                    result.next();
                    return new Utente(result.getInt("id"), result.getString("username"),
                            result.getString("nome"), result.getString("cognome"), result.getString("email"), result.getBoolean("impiegato"));
                }
            }
        }
    }

    public boolean addUtente(Utente utente, String password) throws SQLException {

        String insertQuery = "INSERT INTO utenti (username, password, nome, cognome, email, impiegato) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstatement = connection.prepareStatement(insertQuery)) {
            pstatement.setString(1, utente.getUsername());
            pstatement.setString(2, password);
            pstatement.setString(3, utente.getNome());
            pstatement.setString(4, utente.getCognome());
            pstatement.setString(5, utente.getEmail());
            pstatement.setBoolean(6, utente.isImpiegato());
            return pstatement.executeUpdate() != 0;
        }
    }

}
