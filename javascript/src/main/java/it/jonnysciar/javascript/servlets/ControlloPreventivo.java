package it.jonnysciar.javascript.servlets;

import it.jonnysciar.javascript.beans.Preventivo;
import it.jonnysciar.javascript.beans.Prodotto;
import it.jonnysciar.javascript.beans.Utente;
import it.jonnysciar.javascript.dao.PreventivoDAO;
import it.jonnysciar.javascript.dao.ProdottoDAO;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

@WebServlet("/CheckPreventivo")
public class ControlloPreventivo extends ThymeLeafServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        String path = "/WEB-INF/templates/homepageUtente.html";
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

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
            setupPageError(ctx, utente, prodottoDAO, "Nessuna opzione è stata scelta");
            templateEngine.process(path, ctx, response.getWriter());
        } else if (prodotto == null) {
            setupPageError(ctx, utente, prodottoDAO, "Il prodotto selezionato non esiste");
            templateEngine.process(path, ctx, response.getWriter());
        } else {

            try {
                Preventivo preventivo = new Preventivo(prodotto.getId(),
                                            utente.getId(), Stream.of(optionsArray).map(Integer::parseInt).toList());
                new PreventivoDAO(connection).addPreventivo(preventivo);
                response.sendRedirect(getServletContext().getContextPath() + "/homepageUtente");
            } catch (SQLException | NumberFormatException e) {
                if (setupPageError(ctx, utente, prodottoDAO, "Errore nella richista di preventivo!")) {
                    templateEngine.process(path, ctx, response.getWriter());
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DB Error!");
                }
            }
        }
    }

    private boolean setupPageError(WebContext context, Utente utente, ProdottoDAO prodottoDAO, String errorMsg) {

        PreventivoDAO preventivoDAO = new PreventivoDAO(connection);
        List<Preventivo> preventivi;
        List<Prodotto> products;
        try {
            products = prodottoDAO.getAll();
            preventivi = preventivoDAO.getAllByUserId(utente.getId());
        } catch (SQLException e) {
            return false;
        }
        context.setVariable("preventivi", preventivi);
        context.setVariable("products", products);
        context.setVariable("selected", false);
        context.setVariable("name", utente.getNome());
        context.setVariable("errorMsg", errorMsg);
        context.setVariable("buttonAction", "/homepageUtente");
        return true;
    }
}
