package it.jonnysciar.javascript.servlets;

import it.jonnysciar.javascript.beans.Preventivo;
import it.jonnysciar.javascript.beans.Prodotto;
import it.jonnysciar.javascript.beans.Utente;
import it.jonnysciar.javascript.dao.PreventivoDAO;
import it.jonnysciar.javascript.dao.ProdottoDAO;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Stream;

@WebServlet("/CheckPreventivo")
public class ControlloPreventivo extends DBServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        String path = "/templates/homepageUtente.html";

        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        Utente utente = (Utente) request.getSession().getAttribute("user");
        String[] optionsArray = request.getParameterValues("checkbox");
        Prodotto prodotto;
        try {
            prodotto = prodottoDAO.getByNome(StringEscapeUtils.escapeJava(request.getParameter("productName")));
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DB Error!");
            return;
        }

        if (optionsArray == null) {

        } else if (prodotto == null) {

        } else {

            try {
                Preventivo preventivo = new Preventivo(prodotto.getId(),
                                            utente.getId());
                new PreventivoDAO(connection).addPreventivo(preventivo,  Stream.of(optionsArray).map(Integer::parseInt).toList());
                response.sendRedirect(getServletContext().getContextPath() + "/homepageUtente");
            } catch (SQLException | NumberFormatException e) {
                if (false) {

                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DB Error!");
                }
            }
        }
    }

}
