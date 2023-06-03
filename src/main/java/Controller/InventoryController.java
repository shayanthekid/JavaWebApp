/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Models.Inventory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sajid
 */
public class InventoryController {
    
    
    
   public List<Inventory> displayAllItems() {
    List<Inventory> items = new ArrayList<>();
    try {
        DBconnection dbConnection = DBconnection.getInstance();
        Connection connection = dbConnection.getConnection();

        String displaySQL = "SELECT * FROM inventory";
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery(displaySQL);

        while (result.next()) {
            Inventory item = new Inventory.InventoryBuilder().id(result.getInt("id"))
                    .name(result.getString("name"))
                    .price(result.getFloat("price"))
                    .quantity(result.getInt("quantity"))
                    .build();
            items.add(item);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return items;
}
    
    
    
}
