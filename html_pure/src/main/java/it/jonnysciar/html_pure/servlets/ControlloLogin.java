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

@WebServlet("/CheckLogin")
public class ControlloLogin extends HttpServlet {

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

        String username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        String password = StringEscapeUtils.escapeJava(request.getParameter("password"));

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
        } else {
            // query db to authenticate for user
            UtenteDAO userDao = new UtenteDAO(connection);
            Utente utente;
            try {
                utente = userDao.checkCredentials(username, password);
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to check credentials");
                return;
            }
            // If the user exists, add info to the session and go to home page, otherwise
            // show login page with error message
            String path;
            if (utente == null) {
                ServletContext servletContext = getServletContext();
                final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
                ctx.setVariable("errorMsg", "username o password non corretti");
                path = "/WEB-INF/templates/login.html";
                templateEngine.process(path, ctx, response.getWriter());
            } else {
                request.getSession().setAttribute("user", utente);
                path = getServletContext().getContextPath() + "/homepage";
                response.sendRedirect(path);
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
