package it.jonnysciar.html_pure.dao;

import it.jonnysciar.html_pure.beans.Opzione;
import it.jonnysciar.html_pure.beans.Preventivo;
import it.jonnysciar.html_pure.beans.Prodotto;
import it.jonnysciar.html_pure.enums.TipoOpzione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PreventivoDAO extends DAO{

    public PreventivoDAO(Connection connection) {
        super(connection);
    }

    public void addPreventivo(Preventivo preventivo) throws SQLException {
        preventivo.setId(getNextId() + 1);
        connection.setAutoCommit(false);
        String query = "INSERT INTO preventivi(id, codice_prodotto, id_utente) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, preventivo.getId());
            statement.setInt(2, preventivo.getCodice_prodotto());
            statement.setInt(3, preventivo.getId_utente());
            statement.executeUpdate();
        }
        List<Integer> opzioni = new ProdottoDAO(connection).getAllOpzioniById(preventivo.getCodice_prodotto())
                                .stream()
                                .map(Opzione::getCodice)
                                .toList();
        for (Integer o : preventivo.getOpzioni()) {
            if (opzioni.contains(o)) {
                query = "INSERT INTO preventivi_opzioni(id_preventivo, codice_opzione) VALUES (?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1,preventivo.getId());
                    statement.setInt(2, o);
                    statement.executeUpdate();
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
}
