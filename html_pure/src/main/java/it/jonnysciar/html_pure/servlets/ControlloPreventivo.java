package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Opzione;
import it.jonnysciar.html_pure.beans.Prodotto;
import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.ProdottoDAO;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            System.out.println(StringEscapeUtils.escapeJava(request.getParameter("productName")));
            prodotto = prodottoDAO.getByNome(StringEscapeUtils.escapeJava(request.getParameter("productName")));
            System.out.println(prodotto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (optionsArray != null) {
            List<String> options = Arrays.asList(optionsArray);
            System.out.println(options);

        } else {
            setupPageError(ctx, utente, prodottoDAO, prodotto, "Nessuna opzione è stata scelta");
            String path = "/WEB-INF/templates/homepageUtente.html";
            templateEngine.process(path, ctx, response.getWriter());
        }
    }

    private void setupPageError(WebContext context,
                                Utente utente, ProdottoDAO prodottoDAO, Prodotto prodotto, String errorMsg) throws IOException {

        List<Opzione> options;
        try {
            options = prodottoDAO.getAllOpzioniById(prodotto.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (options != null) {
            context.setVariable("options", options);
        }
        context.setVariable("selected", true);
        context.setVariable("productName", prodotto.getNome());
        context.setVariable("buttonAction", "/CheckPreventivo");
    }
}
