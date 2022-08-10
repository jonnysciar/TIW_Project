package it.jonnysciar.javascript.servlets;

import it.jonnysciar.javascript.beans.Opzione;
import it.jonnysciar.javascript.beans.Preventivo;
import it.jonnysciar.javascript.beans.Prodotto;
import it.jonnysciar.javascript.beans.Utente;
import it.jonnysciar.javascript.dao.PreventivoDAO;
import it.jonnysciar.javascript.dao.ProdottoDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/homepageUtente")
public class HomepageUtente extends ThymeLeafServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        setupPage(request, response, ctx);
        ctx.setVariable("method", "GET");

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
                ctx.setVariable("options", options);
            }
            ctx.setVariable("selected", true);
            ctx.setVariable("prodotto", prodotto);
            ctx.setVariable("buttonAction", "/CheckPreventivo");
            ctx.setVariable("method", "POST");
        } else if (request.getParameter("productId") != null) {
            ctx.setVariable("selected", false);
            ctx.setVariable("errorMsg", "Il prodotto selezionato non esiste");
            ctx.setVariable("buttonAction", "/homepageUtente");
        } else {
            ctx.setVariable("selected", false);
            ctx.setVariable("buttonAction", "/homepageUtente");
        }

        String path = "/WEB-INF/templates/homepageUtente.html";
        templateEngine.process(path, ctx, response.getWriter());
    }

    private void setupPage(HttpServletRequest request, HttpServletResponse response, WebContext context) throws IOException {
        response.setCharacterEncoding("UTF-8");

        Utente utente = (Utente) request.getSession().getAttribute("user");
        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        PreventivoDAO preventivoDAO = new PreventivoDAO(connection);
        List<Prodotto> products;
        List<Preventivo> preventivi;
        try {
            products = prodottoDAO.getAll();
            preventivi = preventivoDAO.getAllByUserId(utente.getId());
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DB Error!");
            return;
        }
        context.setVariable("preventivi", preventivi);
        context.setVariable("name", utente.getNome());
        context.setVariable("products", products);
    }
}
