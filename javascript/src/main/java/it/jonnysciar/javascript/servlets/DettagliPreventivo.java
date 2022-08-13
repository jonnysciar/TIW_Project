package it.jonnysciar.javascript.servlets;

import com.google.gson.Gson;
import it.jonnysciar.javascript.beans.Preventivo;
import it.jonnysciar.javascript.beans.Prodotto;
import it.jonnysciar.javascript.beans.Utente;
import it.jonnysciar.javascript.dao.PreventivoDAO;
import it.jonnysciar.javascript.dao.ProdottoDAO;
import it.jonnysciar.javascript.dao.UtenteDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/dettagliPreventivo")
public class DettagliPreventivo extends DBServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");

        Utente utente = (Utente) request.getSession().getAttribute("user");
        PreventivoDAO preventivoDAO = new PreventivoDAO(connection);
        Preventivo preventivo;
        Prodotto prodotto;
        String impiegato;
        List<Object> list;
        try {
            preventivo = preventivoDAO.getById(Integer.parseInt(request.getParameter("id")));
            if (preventivo == null || (utente.getId() != preventivo.getId_utente() && utente.getId() != preventivo.getId_impiegato())) {
                throw new SQLException();
            }
            preventivo.setOpzioni(preventivoDAO.getAllOpzioniById(preventivo.getId()));
            prodotto = new ProdottoDAO(connection).getByCodice(preventivo.getCodice_prodotto());
            list = new ArrayList<>(List.of(prodotto, preventivo));
            if (preventivo.getId_impiegato() != 0) {
                impiegato = new UtenteDAO(connection).getById(preventivo.getId_impiegato()).getUsername();
                list.add(impiegato);
            }

        } catch (SQLException | NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Errore nella richiesta!");
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().println(new Gson().toJson(list));
    }
}
