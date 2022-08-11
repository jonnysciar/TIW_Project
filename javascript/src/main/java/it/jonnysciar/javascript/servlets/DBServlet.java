package it.jonnysciar.javascript.servlets;

import it.jonnysciar.javascript.database.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;

public abstract class DBServlet extends HttpServlet {

    protected Connection connection = null;

    @Override
    public void init() throws ServletException {
        connection = DBConnection.getConnection(getServletContext());
    }

}
