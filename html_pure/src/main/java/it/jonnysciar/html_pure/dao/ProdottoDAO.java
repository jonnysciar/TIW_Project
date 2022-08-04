package it.jonnysciar.html_pure.dao;

import it.jonnysciar.html_pure.beans.Prodotto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdottoDAO extends DAO {

    public ProdottoDAO(Connection connection) {
        super(connection);
    }

    public List<Prodotto> getAll() throws SQLException {
        String query = "SELECT codice, nome, immagine FROM prodotti";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet result = statement.executeQuery()) {
                List<Prodotto> products = new ArrayList<>();
                while (result.next()) {
                    products.add(new Prodotto(result.getInt(1), result.getString(2), result.getString(3)));
                }
                return new ArrayList<>(products);
            }
        }
    }

    public String findByCodice(int codice) throws SQLException {
        String query = "SELECT nome FROM prodotti WHERE codice = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, codice);
            try (ResultSet result = statement.executeQuery()) {
                if (result.isBeforeFirst()) {
                    result.next();
                    return result.getString("nome");
                } else return null;
            }
        }
    }
}
