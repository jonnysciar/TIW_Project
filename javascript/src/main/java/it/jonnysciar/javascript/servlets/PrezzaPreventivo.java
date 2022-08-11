package it.jonnysciar.javascript.servlets;

import it.jonnysciar.javascript.beans.Opzione;
import it.jonnysciar.javascript.beans.Preventivo;
import it.jonnysciar.javascript.beans.Prodotto;
import it.jonnysciar.javascript.dao.PreventivoDAO;
import it.jonnysciar.javascript.dao.ProdottoDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/prezzaPreventivo")
public class PrezzaPreventivo extends DBServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PreventivoDAO preventivoDAO = new PreventivoDAO(connection);
        Preventivo preventivo;
        Prodotto prodotto;
        List<Opzione> opzioni;
        try {
            preventivo = preventivoDAO.getById(Integer.parseInt(request.getParameter("id")));
            if (preventivo == null || preventivo.getPrezzo() != 0 || preventivo.getId_impiegato() != 0) {
                throw new SQLException();
            }
            opzioni = preventivoDAO.getAllOpzioniById(preventivo.getId());
            prodotto = new ProdottoDAO(connection).getByCodice(preventivo.getCodice_prodotto());
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return;
        }

    }
}
