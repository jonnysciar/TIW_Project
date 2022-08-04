package it.jonnysciar.html_pure.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PreventivoDAO extends DAO{

    public PreventivoDAO(Connection connection) {
        super(connection);
    }

    public void getAll() throws SQLException {
        String query = "SELECT id from preventivi";
        try (Statement statement = connection.createStatement()) {
            try (ResultSet result = statement.executeQuery(query)) {
                while (result.next()) {
                    System.out.println(result.getString("nome"));
                }
            }
        }
    }
}
