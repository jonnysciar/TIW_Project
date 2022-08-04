package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Opzione;
import it.jonnysciar.html_pure.beans.Prodotto;
import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.ProdottoDAO;
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

        ctx.setVariable("selected", false);
        ctx.setVariable("buttonAction", "/homepageUtente");

        String path = "/WEB-INF/templates/homepageUtente.html";
        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        setupPage(request, response, ctx);

        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);

        Prodotto prodotto;
        try {
            prodotto = prodottoDAO.getByCodice(Integer.parseInt(request.getParameter("dropProduct")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            prodotto = null;
        }

        if (prodotto != null) {

            List<Opzione> options;
            try {
                options = prodottoDAO.getAllOpzioniById(prodotto.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (options != null) {
                ctx.setVariable("options", options);
            }
            ctx.setVariable("selected", true);
            ctx.setVariable("productName", prodotto.getNome());
            ctx.setVariable("buttonAction", "/CheckPreventivo");
        } else {
            ctx.setVariable("selected", false);
            ctx.setVariable("errorMsg", "Il prodotto selezionato non esiste");
            ctx.setVariable("buttonAction", "/homepageUtente");
        }

        String path = "/WEB-INF/templates/homepageUtente.html";
        templateEngine.process(path, ctx, response.getWriter());
    }

    private void setupPage(HttpServletRequest request, HttpServletResponse response, WebContext context) {
        response.setCharacterEncoding("UTF-8");

        Utente utente = (Utente) request.getSession().getAttribute("user");
        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        List<Prodotto> products;
        try {
            products = prodottoDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        context.setVariable("name", utente.getNome());
        context.setVariable("products", products);
    }
}
