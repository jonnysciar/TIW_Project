package it.jonnysciar.html_pure.dao;

import it.jonnysciar.html_pure.beans.Opzione;
import it.jonnysciar.html_pure.beans.Prodotto;
import it.jonnysciar.html_pure.enums.TipoOpzione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public Prodotto getByCodice(int codice) throws SQLException {
        String query = "SELECT codice, nome, immagine FROM prodotti WHERE codice = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, codice);
            try (ResultSet result = statement.executeQuery()) {
                if (result.isBeforeFirst()) {
                    result.next();
                    return new Prodotto(result.getInt(1), result.getString(2), result.getString(3));
                } else return null;
            }
        }
    }

    public Prodotto getByNome(String nome) throws SQLException {
        String query = "SELECT codice, nome, immagine FROM prodotti WHERE nome = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome);
            try (ResultSet result = statement.executeQuery()) {
                if (result.isBeforeFirst()) {
                    result.next();
                    return new Prodotto(result.getInt(1), result.getString(2), result.getString(3));
                } else return null;
            }
        }
    }

    public List<Opzione> getAllOpzioniById(int codice) throws SQLException {
        String query = "SELECT o.codice, o.nome, o.tipo FROM prodotti AS p, prodotti_opzioni AS po, opzioni AS o " +
                "WHERE p.codice = ? AND po.codice_prodotto = p.codice AND po.codice_opzione = o.codice";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, codice);
            try (ResultSet result = statement.executeQuery()) {
                List<Opzione> options = new ArrayList<>();
                while (result.next()) {
                    options.add(new Opzione(result.getInt(1), result.getString(2), TipoOpzione.getTipoOpzione(result.getString(3))));
                }
                return new ArrayList<>(options);
            }
        }
    }
}
