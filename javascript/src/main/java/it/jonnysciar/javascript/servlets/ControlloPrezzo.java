package it.jonnysciar.javascript.servlets;

import it.jonnysciar.javascript.beans.Opzione;
import it.jonnysciar.javascript.beans.Preventivo;
import it.jonnysciar.javascript.beans.Prodotto;
import it.jonnysciar.javascript.beans.Utente;
import it.jonnysciar.javascript.dao.PreventivoDAO;
import it.jonnysciar.javascript.dao.ProdottoDAO;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/CheckPrice")
@MultipartConfig
public class ControlloPrezzo extends DBServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        Utente utente = (Utente) request.getSession().getAttribute("user");

        PreventivoDAO preventivoDAO = new PreventivoDAO(connection);
        Preventivo preventivo;
        int prezzo;
        try {
            preventivo = preventivoDAO.getById(Integer.parseInt(request.getParameter("preventivoId")));
            prezzo = Integer.parseInt(request.getParameter("price"));
            if (preventivo == null || preventivo.getPrezzo() != 0 || preventivo.getId_impiegato() != 0) {
                throw new SQLException();
            }
        } catch (SQLException | NumberFormatException e) {

            return;
        }

        if (prezzo <= 0) {

            Prodotto prodotto;
            List<Opzione> opzioni;
            try {
                opzioni = preventivoDAO.getAllOpzioniById(preventivo.getId());
                prodotto = new ProdottoDAO(connection).getByCodice(preventivo.getCodice_prodotto());
            } catch (SQLException e) {

                return;
            }

        } else {
            try {
                preventivoDAO.updatePreventivoById(preventivo.getId(), utente.getId(), prezzo);
            } catch (SQLException e) {

                return;
            }
            response.sendRedirect(getServletContext().getContextPath() + "/homepageImpiegato");
        }
    }
}
