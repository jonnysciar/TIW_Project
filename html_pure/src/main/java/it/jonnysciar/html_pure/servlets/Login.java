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
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;

@WebServlet("/login")
public class Login extends ThymeLeafServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("variabile","/DBTest");
        String path = "/WEB-INF/templates/login.html";
        templateEngine.process(path, ctx, response.getWriter());
    }
}
