package it.jonnysciar.javascript.servlets;

import it.jonnysciar.javascript.beans.Opzione;
import it.jonnysciar.javascript.beans.Prodotto;
import it.jonnysciar.javascript.dao.ProdottoDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/homepageUtente")
public class HomepageUtente extends DBServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        Prodotto prodotto;
        try {
            prodotto = prodottoDAO.getByCodice(Integer.parseInt(request.getParameter("productId")));
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DB Error!");
            return;
        } catch (NumberFormatException e) {
            prodotto = null;
        }

        if (prodotto != null) {

            List<Opzione> options;
            try {
                options = prodottoDAO.getAllOpzioniById(prodotto.getId());
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DB Error!");
                return;
            }
            if (options != null) {

            }
        } else if (request.getParameter("productId") != null) {

        } else {

        }

        String path = "/templates/homepageUtente.html";
    }

}
