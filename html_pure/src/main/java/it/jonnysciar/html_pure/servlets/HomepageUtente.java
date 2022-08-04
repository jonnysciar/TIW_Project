package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Prodotto;
import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.ProdottoDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        response.setCharacterEncoding("UTF-8");
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        ProdottoDAO prodottoDAO = new ProdottoDAO(connection);


        String nome;
        try {
            nome = prodottoDAO.findByCodice(Integer.parseInt(request.getParameter("dropProduct")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            nome = null;
        }

        setupPage(request, response, ctx);
        if (nome != null) {
            ctx.setVariable("selected", true);
            ctx.setVariable("productName", nome);
            ctx.setVariable("buttonAction", "/CheckPreventivo");
        } else {
            ctx.setVariable("selected", false);
            ctx.setVariable("errorMsg", "Il prodotto selezionato non esiste");
            ctx.setVariable("buttonAction", "/homepageUtente");
        }




        String path = "/WEB-INF/templates/homepageUtente.html";
        templateEngine.process(path, ctx, response.getWriter());
    }

    public void setupPage(HttpServletRequest request, HttpServletResponse response, WebContext context) {
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        Utente utente = (Utente) session.getAttribute("user");
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
