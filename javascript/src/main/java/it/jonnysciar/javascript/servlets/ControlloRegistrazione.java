package it.jonnysciar.javascript.servlets;

import it.jonnysciar.javascript.beans.Utente;
import it.jonnysciar.javascript.dao.UtenteDAO;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;

@WebServlet("/CheckSignUp")
@MultipartConfig
public class ControlloRegistrazione extends DBServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");

        String nome = StringEscapeUtils.escapeJava(request.getParameter("nome"));
        String cognome = StringEscapeUtils.escapeJava(request.getParameter("cognome"));
        String email = StringEscapeUtils.escapeJava(request.getParameter("email"));
        String username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        String password = StringEscapeUtils.escapeJava(request.getParameter("password"));
        String password2 = StringEscapeUtils.escapeJava(request.getParameter("password2"));
        String checkbox = StringEscapeUtils.escapeJava(request.getParameter("checkbox"));

        if (nome == null || cognome == null || email == null || username == null || password == null || password2 == null ||
            nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Alcuni campi non risultano compilati correttamente");
        } else if (!password.equals(password2)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Le password inserite non coincidono");
        } else if (!validateEmail(email)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Email non valida");
        } else {
            Utente utente = new Utente(username, nome, cognome, email, checkbox != null);
            try {
                if (new UtenteDAO(connection).addUtente(utente, password)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else throw new SQLException();
            } catch (SQLException e) {
                String errorField = "username";
                if (e.getMessage().contains("email")) errorField = "email";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(errorField + " già in uso");
            }
        }
    }

    private boolean validateEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(regexPattern);
    }

}
