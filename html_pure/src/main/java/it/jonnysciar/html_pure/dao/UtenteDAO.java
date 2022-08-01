package it.jonnysciar.html_pure.dao;

import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.database.DBConnection;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                    Utente utente = new Utente(result.getInt("id"), result.getString("username"),
                            result.getString("nome"), result.getString("cognome"), result.getBoolean("impiegato"));
                    return utente;
                }
            }
        }
    }

}
