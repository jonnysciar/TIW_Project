package it.jonnysciar.html_pure.dao;

import java.sql.Connection;

public abstract class DAO {

    protected final Connection connection;

    public DAO(Connection connection) {
        this.connection = connection;
    }
}
