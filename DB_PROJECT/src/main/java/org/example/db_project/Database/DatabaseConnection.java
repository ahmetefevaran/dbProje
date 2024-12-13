package org.example.db_project.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/hastane_uyg";
    private static final String USER = "proje_user";
    private static final String PASSWORD = "proje_123";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC sürücüsü bulunamadı.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
