/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Models.Employee;
import Models.Inventory;
import Models.Observer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author sajid
 */
public class Logger_controller  implements Observer {
    public void displayLogs() {
        try {
            DBconnection dbConnection = DBconnection.getInstance();
            Connection connection = dbConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM log");
            System.out.println("+------------------------------------------------+");
            System.out.println("| ID  | Employee ID | Inventory ID | Type  | Date Logged  |");
            System.out.println("+------------------------------------------------+");

            //iterate through result set and print data
            while (resultSet.next()) {
                System.out.printf("| %-4d | %-11d | %-12d | %-5s | %-14s |%n",
                        resultSet.getInt("ID"),
                        resultSet.getInt("employeeid"),
                        resultSet.getInt("inventoryid"),
                        resultSet.getString("type"),
                        resultSet.getDate("datelogged"));
                System.out.println("+------------------------------------------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void CheckUsage(Employee employee) {
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

                    String format = "%1$-5s %2$-15s %3$-20s %4$-15s %5$-20s %6$-10s %7$-20s%n";
                    System.out.printf(format, "ID", "Employee ID", "Employee Name", "Inventory ID", "Inventory Name", "Type", "Date Logged");

                    while (resultSet.next()) {
                        System.out.printf(format,
                                resultSet.getInt("id"),
                                resultSet.getInt("employeeid"),
                                resultSet.getString("employee_name"),
                                resultSet.getInt("inventoryid"),
                                resultSet.getString("inventory_name"),
                                resultSet.getString("type"),
                                resultSet.getDate("datelogged")
                        );
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
