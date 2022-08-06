package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Opzione;
import it.jonnysciar.html_pure.beans.Preventivo;
import it.jonnysciar.html_pure.beans.Prodotto;
import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.PreventivoDAO;
import it.jonnysciar.html_pure.dao.ProdottoDAO;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Stream;

@WebServlet("/CheckPreventivo")
public class ControlloPreventivo extends ThymeLeafServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        Utente utente = (Utente) request.getSession().getAttribute("user");
        String[] optionsArray = request.getParameterValues("checkbox");
        Prodotto prodotto;
        try {
            prodotto = prodottoDAO.getByNome(StringEscapeUtils.escapeJava(request.getParameter("productName")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (optionsArray == null) {
            setupPageError(ctx, utente, prodottoDAO, "Nessuna opzione è stata scelta");
            String path = "/WEB-INF/templates/homepageUtente.html";
            templateEngine.process(path, ctx, response.getWriter());
        } else if (prodotto == null) {
            setupPageError(ctx, utente, prodottoDAO, "Il prodotto selezionato non esiste");
            String path = "/WEB-INF/templates/homepageUtente.html";
            templateEngine.process(path, ctx, response.getWriter());
        } else {

            try {
                Preventivo preventivo = new Preventivo(prodotto.getId(),
                                            utente.getId(), Stream.of(optionsArray).map(Integer::parseInt).toList());
                new PreventivoDAO(connection).addPreventivo(preventivo);
                response.sendRedirect(getServletContext().getContextPath() + "/homepageUtente");
            } catch (SQLException | NumberFormatException e) {
                setupPageError(ctx, utente, prodottoDAO, "Errore nella richista di preventivo!");
                String path = "/WEB-INF/templates/homepageUtente.html";
                templateEngine.process(path, ctx, response.getWriter());
            }
        }
    }

    private void setupPageError(WebContext context, Utente utente, ProdottoDAO prodottoDAO, String errorMsg) {

        PreventivoDAO preventivoDAO = new PreventivoDAO(connection);
        List<Preventivo> preventivi;
        List<Prodotto> products;
        try {
            products = prodottoDAO.getAll();
            preventivi = preventivoDAO.getAllByUserId(utente.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        context.setVariable("preventivi", preventivi);
        context.setVariable("products", products);
        context.setVariable("selected", false);
        context.setVariable("name", utente.getNome());
        context.setVariable("errorMsg", errorMsg);
        context.setVariable("buttonAction", "/homepageUtente");
    }
}
