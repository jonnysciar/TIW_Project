package it.jonnysciar.html_pure.database;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection = null;

    public static Connection getConnection(ServletContext context) throws ServletException {
        if (connection == null) {
            try {
                String driver = context.getInitParameter("dbDriver");
                String url = context.getInitParameter("dbUrl");
                String user = context.getInitParameter("dbUser");
                String password = context.getInitParameter("dbPassword");
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException e) {
                throw new UnavailableException("Can't load database driver");
            } catch (SQLException e) {
                throw new UnavailableException("Couldn't get db connection");
            }
        }
        return connection;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

}
