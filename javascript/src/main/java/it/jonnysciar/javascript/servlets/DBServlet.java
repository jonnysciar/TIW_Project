package it.jonnysciar.javascript.servlets;

import it.jonnysciar.javascript.database.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DBServlet extends HttpServlet {

    protected Connection connection = null;

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

}
