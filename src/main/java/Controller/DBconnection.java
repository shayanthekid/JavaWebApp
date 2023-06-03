/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author sajid
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    
    private static DBconnection instance;
    private Connection connection;

    // private String url = "jdbc:postgresql://hostname:port/dbname";
    private String url = "jdbc:postgresql://localhost:5432/Inventory";
    private String username = "postgres";
    private String password = "1234";

    private DBconnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DBconnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBconnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DBconnection();
        }

        return instance;
    }

}