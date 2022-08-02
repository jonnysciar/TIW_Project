package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.UtenteDAO;
import it.jonnysciar.html_pure.database.DBConnection;
import org.apache.commons.text.StringEscapeUtils;
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
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/CheckSignUp")
public class ControlloRegistrazione extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        connection = DBConnection.getConnection(servletContext);

        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        String nome = StringEscapeUtils.escapeJava(request.getParameter("nome"));
        String cognome = StringEscapeUtils.escapeJava(request.getParameter("cognome"));
        String username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        String password = StringEscapeUtils.escapeJava(request.getParameter("password"));
        String password2 = StringEscapeUtils.escapeJava(request.getParameter("password2"));
        String checkbox = request.getParameter("checkbox");

        if (nome == null || cognome == null || username == null || password == null || password2 == null) {
            ctx.setVariable("errorMsg", "Alcuni campi non risultano compilati correttamente");
            String path = "/WEB-INF/templates/registrazione.html";
            templateEngine.process(path, ctx, response.getWriter());
        } else if (!password.equals(password2)) {
            ctx.setVariable("errorMsg", "Le password inserite non coincidono");
            String path = "/WEB-INF/templates/registrazione.html";
            templateEngine.process(path, ctx, response.getWriter());
        } else {
            Utente utente = new Utente(username, nome, cognome, checkbox != null);
            try {
                if (new UtenteDAO(connection).addUtente(utente, password)) {
                    String path = "/WEB-INF/templates/reg_successo.html";
                    templateEngine.process(path, ctx, response.getWriter());
                } else throw new SQLException();
            } catch (SQLException e) {
                ctx.setVariable("errorMsg", "Username già in uso");
                String path = "/WEB-INF/templates/registrazione.html";
                templateEngine.process(path, ctx, response.getWriter());
            }
        }
    }

    @Override
    public void destroy() {
        try {
            DBConnection.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
