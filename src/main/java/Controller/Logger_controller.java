/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Models.Employee;
import Models.Inventory;
import Models.Log;
import Models.LogJson;
import Models.Observer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author sajid
 */
public class Logger_controller  implements Observer {
 public List<Log> displayLogs() {
    List<Log> logs = new ArrayList<>();
    try {
        DBconnection dbConnection = DBconnection.getInstance();
        Connection connection = dbConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM log");
        
        // Iterate through the result set and add logs to the list
        while (resultSet.next()) {
            Log log = new Log();
            log.setID(resultSet.getInt("ID"));
             
            
            Employee employee = new Employee.EmployeeBuilder(resultSet.getInt("employeeid")).build();
            log.setEmployeeID(employee);

            Inventory inventory = new Inventory.InventoryBuilder()
                            .id(resultSet.getInt("inventoryid"))
                            .build();
            log.setInventoryID(inventory);
            
            log.setType(resultSet.getString("type"));
            log.setDateLogged(resultSet.getDate("datelogged"));
            
            logs.add(log);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return logs;
}

    public void update(String action, Employee employee, Inventory inventory, Date date) {
        //log the action, employee, and inventory to the database    

        try {
            DBconnection dbConnection = DBconnection.getInstance();
            Connection connection = dbConnection.getConnection();
            String insertSQL = "INSERT INTO log (employeeid, inventoryid, type, datelogged) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, employee.getId());
            preparedStatement.setInt(2, inventory.getID());
            preparedStatement.setString(3, action);
            preparedStatement.setDate(4, (java.sql.Date) date);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

public List<LogJson> CheckUsage(Employee employee) {
    List<LogJson> logsJson = new ArrayList<>();

    try {
        if (employee == null || employee.getId() <= 0) {
            throw new IllegalArgumentException("Invalid employee details");
        }
        if (!checkEmployee(employee.getId())) {
            throw new IllegalArgumentException("Employee with ID " + employee.getId() + " does not exist.");
        }

        try {
            DBconnection dbConnection = DBconnection.getInstance();
            Connection connection = dbConnection.getConnection();
            Statement statement = connection.createStatement();

            if (checkEmployee(employee.getId())) {
                String query = "SELECT log.id, log.employeeid, log.inventoryid, log.type, log.datelogged, employee.name AS employee_name, inventory.name AS inventory_name FROM log "
                        + "JOIN employee ON log.employeeid = employee.id "
                        + "JOIN inventory ON log.inventoryid = inventory.id "
                        + "WHERE log.employeeid = " + employee.getId();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    LogJson logJson = new LogJson();
                    logJson.id = resultSet.getInt("id");
                    logJson.employeeId = resultSet.getInt("employeeid");
                    logJson.employeeName = resultSet.getString("employee_name");
                    logJson.inventoryId = resultSet.getInt("inventoryid");
                    logJson.inventoryName = resultSet.getString("inventory_name");
                    logJson.type = resultSet.getString("type");
                    logJson.dateLogged = resultSet.getDate("datelogged").toString();
                    logsJson.add(logJson);
                }
            } else {
                throw new IllegalArgumentException("Employee with ID " + employee.getId() + " does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    } catch (IllegalArgumentException e) {
        System.out.println("Error: " + e.getMessage());
    }

    return logsJson;
}

    // check if employee exists
    public boolean checkEmployee(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid Employee id");
        }
        try {
            DBconnection dbConnection = DBconnection.getInstance();
            Connection connection = dbConnection.getConnection();

            String checkSQL = "SELECT COUNT(*) FROM log WHERE employeeid = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSQL);
            checkStatement.setInt(1, id);
            ResultSet checkResult = checkStatement.executeQuery();

            if (checkResult.next()) {
                int count = checkResult.getInt(1);
                if (count == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
