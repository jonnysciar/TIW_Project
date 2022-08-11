package it.jonnysciar.javascript.servlets;

import it.jonnysciar.javascript.beans.Utente;
import it.jonnysciar.javascript.dao.UtenteDAO;
import it.jonnysciar.javascript.database.DBConnection;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;

@WebServlet("/CheckLogin")
@MultipartConfig
public class ControlloLogin extends DBServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        String password = StringEscapeUtils.escapeJava(request.getParameter("password"));

        System.out.println(request);
        System.out.println(password);

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Missing credential value");
        } else {

            UtenteDAO userDao = new UtenteDAO(connection);
            Utente utente;
            try {
                utente = userDao.checkCredentials(username, password);
            } catch (SQLException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to check credentials");
                return;
            }

            if (utente == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Utente nullo!");
            } else {
                request.getSession().setAttribute("user", utente);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(username);
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
