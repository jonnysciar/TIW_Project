package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Preventivo;
import it.jonnysciar.html_pure.beans.Prodotto;
import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.PreventivoDAO;
import it.jonnysciar.html_pure.dao.ProdottoDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

@WebServlet("/CheckPreventivo")
public class ControlloPreventivo extends ThymeLeafServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = "/WEB-INF/templates/homepageUtente.html";
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());

        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
        Utente utente = (Utente) request.getSession().getAttribute("user");
        String[] optionsArray = request.getParameterValues("checkbox");
        Prodotto prodotto;
        try {
            prodotto = prodottoDAO.getByNome(sanitizeString(request.getParameter("productName")));
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
                                            utente.getId());
                new PreventivoDAO(connection).addPreventivo(preventivo, Stream.of(optionsArray).map(Integer::parseInt).toList());
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
