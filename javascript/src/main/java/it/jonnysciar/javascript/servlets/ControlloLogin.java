package it.jonnysciar.javascript.servlets;

import com.google.gson.Gson;
import it.jonnysciar.javascript.beans.Utente;
import it.jonnysciar.javascript.dao.UtenteDAO;

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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = sanitizeString(request.getParameter("username"));
        String password = sanitizeString(request.getParameter("password"));

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Credenziali nulle!");
        } else {

            UtenteDAO userDao = new UtenteDAO(connection);
            Utente utente;
            try {
                utente = userDao.checkCredentials(username, password);
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Errore interno al server!");
                return;
            }

            if (utente == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Username o password errati!");
            } else {
                request.getSession().setAttribute("user", utente);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println(new Gson().toJson(utente));
            }
        }
    }

}
