package it.jonnysciar.html_pure.dao;

import it.jonnysciar.html_pure.beans.Preventivo;

import java.sql.*;

public class PreventivoDAO extends DAO{

    public PreventivoDAO(Connection connection) {
        super(connection);
    }

    public void addPreventivo(Preventivo preventivo) throws SQLException {
        connection.setAutoCommit(false);
        String query = "INSERT INTO preventivi(codice_prodotto, id_utente) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, preventivo.getCodice_prodotto());
            statement.setInt(2, preventivo.getId_utente());
            try (ResultSet result = statement.executeQuery(query)) {
                while (result.next()) {
                    System.out.println(result.getString("nome"));
                }
            }
        }
    }
}
