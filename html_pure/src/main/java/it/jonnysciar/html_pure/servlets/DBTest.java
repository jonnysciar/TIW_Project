package it.jonnysciar.html_pure.servlets;

import it.jonnysciar.html_pure.database.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/DBTest")
public class DBTest extends HttpServlet {

    private Connection connection;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String result = "Connection worked";
        try {
            connection = DBConnection.getConnection(getServletContext());
        } catch (Exception e) {
            result = "Connection failed";
            e.printStackTrace();
        }
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println(result);
        out.close();
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
