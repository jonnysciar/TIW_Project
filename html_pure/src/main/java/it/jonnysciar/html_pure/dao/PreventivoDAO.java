package it.jonnysciar.html_pure.dao;

import it.jonnysciar.html_pure.beans.Opzione;
import it.jonnysciar.html_pure.beans.Preventivo;
import it.jonnysciar.html_pure.enums.TipoOpzione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreventivoDAO extends DAO{

    public PreventivoDAO(Connection connection) {
        super(connection);
    }

    public void addPreventivo(Preventivo preventivo, List<Integer> codiciOpzioniRichiesti) throws SQLException {
        preventivo.setId(getNextId() + 1);
        connection.setAutoCommit(false);
        String query = "INSERT INTO preventivi(id, codice_prodotto, id_utente) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, preventivo.getId());
            statement.setInt(2, preventivo.getCodice_prodotto());
            statement.setInt(3, preventivo.getId_utente());
            statement.executeUpdate();
        }
        List<Opzione> opzioni = new ProdottoDAO(connection).getAllOpzioniById(preventivo.getCodice_prodotto());
        List<Integer> codiciOpzioniProdotto = opzioni.stream().map(Opzione::getCodice).toList();
        for (Integer i : codiciOpzioniRichiesti) {
            if (codiciOpzioniProdotto.contains(i)) {
                query = "INSERT INTO preventivi_opzioni(id_preventivo, codice_opzione) VALUES (?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, preventivo.getId());
                    statement.setInt(2, i);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    connection.rollback();
                    connection.setAutoCommit(false);
                    throw e;
                }
            } else {
                connection.rollback();
                connection.setAutoCommit(false);
                throw new SQLException();
            }
        }
        connection.commit();
        connection.setAutoCommit(false);
    }

    private int getNextId() throws SQLException {
        String query = "SELECT max(id) FROM preventivi";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet result = statement.executeQuery()) {
                if (result.isBeforeFirst()) {
                    result.next();
                    return result.getInt(1);
                } else return 0;
            }
        }
    }

    public List<Preventivo> getAllByUserId(int id_utente) throws SQLException {
        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        String query = "SELECT id, codice_prodotto, id_utente, id_impiegato, prezzo FROM preventivi WHERE id_utente = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id_utente);
            try (ResultSet result = statement.executeQuery()) {
                List<Preventivo> preventivi = new ArrayList<>();
                while (result.next()) {
                    Preventivo p = new Preventivo(result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4), result.getInt(5));
                    p.setNomeProdotto(prodottoDAO.getByCodice(p.getCodice_prodotto()).getNome());
                    preventivi.add(p);
                }
                return new ArrayList<>(preventivi);
            }
        }
    }

    public List<Preventivo> getAllByImpiegatoId(int id_impiegato) throws SQLException {
        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        String query = "SELECT id, codice_prodotto, id_utente, id_impiegato, prezzo FROM preventivi WHERE id_impiegato = ? AND prezzo > 0";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id_impiegato);
            try (ResultSet result = statement.executeQuery()) {
                List<Preventivo> preventivi = new ArrayList<>();
                while (result.next()) {
                    Preventivo p = new Preventivo(result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4), result.getInt(5));
                    p.setNomeProdotto(prodottoDAO.getByCodice(p.getCodice_prodotto()).getNome());
                    preventivi.add(p);
                }
                return new ArrayList<>(preventivi);
            }
        }
    }

    public List<Preventivo> getAllPreventiviNotManaged() throws SQLException {
        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        String query = "SELECT id, codice_prodotto, id_utente, id_impiegato, prezzo FROM preventivi WHERE isnull(prezzo) AND isnull(id_impiegato)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet result = statement.executeQuery()) {
                List<Preventivo> preventivi = new ArrayList<>();
                while (result.next()) {
                    Preventivo p = new Preventivo(result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4), result.getInt(5));
                    p.setNomeProdotto(prodottoDAO.getByCodice(p.getCodice_prodotto()).getNome());
                    preventivi.add(p);
                }
                return new ArrayList<>(preventivi);
            }
        }
    }

    public Preventivo getById(int id) throws SQLException {
        String query = "SELECT id, codice_prodotto, id_utente, id_impiegato, prezzo FROM preventivi WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.isBeforeFirst()) {
                    result.next();
                    return new Preventivo(result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4), result.getInt(5));
                } else return null;
            }
        }
    }

    public List<Opzione> getAllOpzioniById(int id) throws SQLException {
        String query = "SELECT o.codice, o.nome, o.tipo FROM preventivi AS p, preventivi_opzioni AS po, opzioni AS o " +
                "WHERE p.id = ? AND po.id_preventivo = p.id AND po.codice_opzione = o.codice";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                List<Opzione> options = new ArrayList<>();
                while (result.next()) {
                    options.add(new Opzione(result.getInt(1), result.getString(2), TipoOpzione.getTipoOpzione(result.getString(3))));
                }
                return new ArrayList<>(options);
            }
        }
    }

    public void updatePreventivoById(int id, int id_impiegato, int prezzo) throws SQLException {
        String query = "UPDATE preventivi SET id_impiegato = ?, prezzo = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id_impiegato);
            statement.setInt(2, prezzo);
            statement.setInt(3, id);
            statement.executeUpdate();
        }
    }
}
