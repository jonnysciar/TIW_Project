package it.jonnysciar.javascript.servlets;

import it.jonnysciar.javascript.database.DBConnection;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.UnicodeUnescaper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DBServlet extends HttpServlet {

    protected Connection connection = null;
    private final UnicodeUnescaper unicodeUnescaper = new UnicodeUnescaper();

    @Override
    public void init() throws ServletException {
        connection = DBConnection.getConnection(getServletContext());
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
