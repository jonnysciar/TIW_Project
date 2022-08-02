package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Prodotto;
import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.ProdottoDAO;
import it.jonnysciar.html_pure.database.DBConnection;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/homepage")
public class Homepage extends ThymeLeafServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        Utente utente = (Utente) session.getAttribute("user");

        String path;
        if (utente.isImpiegato()) {
            path = "/WEB-INF/templates/homepageImpiegato.html";
        } else {
            path = "/WEB-INF/templates/homepageUtente.html";
            ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
            try {
                List<Prodotto> products = prodottoDAO.getAll();
                //System.out.println(products);
                ctx.setVariable("products", products);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ctx.setVariable("buttonAction", "/homepage");
        }
        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        Utente utente = (Utente) session.getAttribute("user");

        String path;
        if (utente.isImpiegato()) {
            path = "/WEB-INF/templates/homepageImpiegato.html";
        } else {
            path = "/WEB-INF/templates/homepageUtente.html";
            ProdottoDAO prodottoDAO = new ProdottoDAO(connection);
            try {
                List<Prodotto> products = prodottoDAO.getAll();
                ctx.setVariable("products", products);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ctx.setVariable("buttonAction", "/homepage");

        }
        templateEngine.process(path, ctx, response.getWriter());
    }

}
