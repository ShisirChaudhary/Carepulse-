package com.carepulse.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Database connection settings for the MySQL instance shipped with XAMPP.
public class DBConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/carepulse_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    // Opens and returns a new connection to the carepulse_db database.
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
