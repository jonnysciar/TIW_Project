package it.jonnysciar.javascript.servlets;

import com.google.gson.Gson;
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
import java.util.stream.Stream;

@WebServlet("/CheckPreventivo")
@MultipartConfig
public class ControlloPreventivo extends DBServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");

        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        Utente utente = (Utente) request.getSession().getAttribute("user");
        String[] optionsArray = request.getParameterValues("checkbox");
        Prodotto prodotto;
        try {
            prodotto = prodottoDAO.getByCodice(Integer.parseInt(request.getParameter("selectProduct")));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Errore interno al server!");
            return;
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Errore nella richiesta!");
            return;
        }

        if (optionsArray == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Nessuna opzione è stata scelta");
        } else if (prodotto == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Il prodotto selezionato non esiste");
        } else {

            try {
                Preventivo preventivo = new Preventivo(prodotto.getId(),
                                            utente.getId());
                PreventivoDAO preventivoDAO = new PreventivoDAO(connection);
                preventivoDAO.addPreventivo(preventivo,  Stream.of(optionsArray).map(Integer::parseInt).toList());
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println(new Gson().toJson(preventivoDAO.getAllByUserId(utente.getId())));
            } catch (SQLException | NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Errore nella richiesta!");
            }
        }
    }

}
