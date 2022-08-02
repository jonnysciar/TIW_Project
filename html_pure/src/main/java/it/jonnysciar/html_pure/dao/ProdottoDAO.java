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
}
