package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.UtenteDAO;
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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = sanitizeString(request.getParameter("username"));
        String password = sanitizeString(request.getParameter("password"));

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
        } else {

            UtenteDAO userDao = new UtenteDAO(connection);
            Utente utente;
            try {
                utente = userDao.checkCredentials(username, password);
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to check credentials");
                return;
            }

            String path;
            if (utente == null) {
                ServletContext servletContext = getServletContext();
                final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
                ctx.setVariable("errorMsg", "username o password non corretti");
                path = "/WEB-INF/templates/login.html";
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

}
