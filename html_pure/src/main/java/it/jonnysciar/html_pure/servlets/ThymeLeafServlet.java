package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.database.DBConnection;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.UnicodeUnescaper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class ThymeLeafServlet extends HttpServlet {

    protected Connection connection = null;
    protected TemplateEngine templateEngine;
    private final UnicodeUnescaper unicodeUnescaper = new UnicodeUnescaper();

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
    public void destroy() {
        try {
            DBConnection.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected String sanitizeString(String s) {
        return unicodeUnescaper.translate(StringEscapeUtils.escapeJava(s));
    }
}
