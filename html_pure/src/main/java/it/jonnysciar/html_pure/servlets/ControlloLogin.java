package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.UtenteDAO;
import it.jonnysciar.html_pure.database.DBConnection;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;

@WebServlet("/CheckLogin")
public class ControlloLogin extends ThymeLeafServlet {

    @Serial
    private static final long serialVersionUID = 1L;

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
                path = "/templates/login.html";
                templateEngine.process(path, ctx, response.getWriter());
            } else {
                request.getSession().setAttribute("user", utente);

                path = getServletContext().getContextPath();
                if (utente.isImpiegato()) path = path + "/homepageImpiegato";
                else path = path + "/homepageUtente";

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
