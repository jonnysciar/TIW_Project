package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.beans.Utente;
import it.jonnysciar.html_pure.dao.UtenteDAO;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;

@WebServlet("/CheckSignUp")
public class ControlloRegistrazione extends ThymeLeafServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        String path = "/WEB-INF/templates/registrazione.html";

        String nome = StringEscapeUtils.escapeJava(request.getParameter("nome"));
        String cognome = StringEscapeUtils.escapeJava(request.getParameter("cognome"));
        String email = StringEscapeUtils.escapeJava(request.getParameter("email"));
        String username = StringEscapeUtils.escapeJava(request.getParameter("username"));
        String password = StringEscapeUtils.escapeJava(request.getParameter("password"));
        String password2 = StringEscapeUtils.escapeJava(request.getParameter("password2"));
        String checkbox = StringEscapeUtils.escapeJava(request.getParameter("checkbox"));

        if (nome == null || cognome == null || email == null || username == null || password == null || password2 == null ||
            nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || password2.isEmpty()) {

            ctx.setVariable("errorMsg", "Alcuni campi non risultano compilati correttamente");
            templateEngine.process(path, ctx, response.getWriter());
        } else if (!password.equals(password2)) {
            ctx.setVariable("errorMsg", "Le password inserite non coincidono");
            templateEngine.process(path, ctx, response.getWriter());
        } else if (!validateEmail(email)) {
            ctx.setVariable("errorMsg", "email non valida");
            templateEngine.process(path, ctx, response.getWriter());
        } else {
            Utente utente = new Utente(username, nome, cognome, email, checkbox != null);
            try {
                if (new UtenteDAO(connection).addUtente(utente, password)) {
                    path = "/WEB-INF/templates/reg_successo.html";
                    templateEngine.process(path, ctx, response.getWriter());
                } else throw new SQLException();
            } catch (SQLException e) {
                String errorColumn = "username";
                if (e.getMessage().contains("email")) errorColumn = "email";
                ctx.setVariable("errorMsg",  errorColumn + " già in uso");
                templateEngine.process(path, ctx, response.getWriter());
            }
        }
    }

    private boolean validateEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(regexPattern);
    }
}
