package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Utente;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/")
public class Homepage extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        if (session.isNew() || session.getAttribute("user") == null) {
            String path = "/WEB-INF/templates/login.html";
            templateEngine.process(path, ctx, response.getWriter());
        } else {
            Utente utente = (Utente) session.getAttribute("user");
            ctx.setVariable("value", utente.getNome() + " " + utente.getCognome() + " è impiegato: " + utente.isImpiegato());
            String path = "/WEB-INF/templates/homepage.html";
            templateEngine.process(path, ctx, response.getWriter());
        }
    }
}
