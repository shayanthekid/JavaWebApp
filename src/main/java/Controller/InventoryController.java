/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Models.Employee;
import Models.Inventory;
import Models.Observer;
import Models.Subject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author sajid
 */
public class InventoryController implements Subject {
     private List<Observer> observers;

    public InventoryController() {

        this.observers = new ArrayList<Observer>();

    }
    
     public void addItem(Inventory inventory, Employee employee, Date date) {
    // Validate input
    if (!(inventory.getName() instanceof String) || inventory.getName().isEmpty()) {
        throw new IllegalArgumentException("Name must be a non-empty string");
    }
    if (inventory.getPrice() <= 0) {
        throw new IllegalArgumentException("Price must be greater than zero");
    }
    if (inventory.getQuantity() <= 0) {
        throw new IllegalArgumentException("Quantity must be greater than zero");
    }
    if (!checkEmployee(employee.getId())) {
        throw new IllegalArgumentException("Employee with ID " + employee.getId() + " does not exist.");
    }

    // Add items to inventory
    try {
        // Synchronize on the connection or a shared lock object
        synchronized (DBconnection.class) { // only one thread can execute the synchronized block at a time.
            DBconnection dbConnection = DBconnection.getInstance();
            Connection connection = dbConnection.getConnection();
            String insertSQL = "INSERT INTO inventory (name, price, quantity) VALUES ( ?, ?, ?) RETURNING id";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, inventory.getName());
            preparedStatement.setDouble(2, inventory.getPrice());
            preparedStatement.setInt(3, inventory.getQuantity());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int itemId = rs.getInt("id");
                inventory.setId(itemId);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    synchronized (observers) {
           /* 
    synchronizing on the observers object, 
     ensure that only one thread can modify the list at a time. 
    Other threads attempting to modify the list will be blocked until the lock is released.
    */
        Observer observer = new Logger_controller();
        addObserver(observer);
    }
    
 
    notifyObservers("add", employee, inventory, date);
}
    
    
public List<Inventory> displayAllItems() {
    final List<Inventory> items = new ArrayList<>();

    try {
        DBconnection dbConnection = DBconnection.getInstance();
        Connection connection = dbConnection.getConnection();

        String displaySQL = "SELECT * FROM inventory";
        Statement stmt = connection.createStatement();
        final ResultSet result = stmt.executeQuery(displaySQL);

        // Create a thread pool
        ExecutorService executor = Executors.newFixedThreadPool(10); // Adjust the thread pool size as needed
//We create an ExecutorService object using the Executors.newFixedThreadPool() method. 
//  This creates a thread pool with a fixed number of threads (in this case, 10). 
// The thread pool will manage the execution of our tasks concurrently. Database access so 10 threads can run concurrently

        while (result.next()) {
            final int id = result.getInt("id");
            final String name = result.getString("name");
            final float price = result.getFloat("price");
            final int quantity = result.getInt("quantity");

            Runnable task = new Runnable() {
                //We create a Runnable task that encapsulates the logic to build an Inventory object using the values from the current row.

                @Override
                public void run() {
                    Inventory item = new Inventory.InventoryBuilder()
                            .id(id)
                            .name(name)
                            .price(price)
                            .quantity(quantity)
                            .build();
                    synchronized (items) {
                        //By synchronizing the code block containing items.add(item), 
                        //you ensure that only one thread can execute that code block at a time, preventing concurrent modifications to the items list.
                        items.add(item);
                    }
                }
            };
            executor.execute(task);
            //The ExecutorService is responsible for scheduling the task for execution by one of the threads in the thread pool.
        }
        // Shutdown the executor and wait for all tasks to complete
        executor.shutdown();
        //Once all tasks have been submitted to the ExecutorService, we call shutdown() to initiate a graceful shutdown of the thread pool. 
       // This means that no new tasks will be accepted, but the already submitted tasks will be allowed to complete.
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//       We then call awaitTermination() to wait for all tasks to complete. 
//        This method blocks until all tasks have finished executing or until the specified timeout has elapsed.
        
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }

    return items;
}

/** 
 * 
 * 
 * Threading allows multiple tasks or operations to be executed concurrently, which can help improve performance and responsiveness in certain scenarios.
 * In this code, threading is used to execute the database queries and object creation for each row in parallel, leveraging the available threads in the thread pool. 
 * This can potentially speed up the retrieval of data from the database.
 * 
 * By using threading, each user's request can be processed concurrently without blocking other users. 
 * This can improve the overall performance and responsiveness of the application, as multiple users can be served simultaneously.


 * 
 * 
 */
    
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

    
public void addObserver(Observer observer) {

        if (observers == null) {
            observers = new ArrayList<Observer>();
        }
        observers.add(observer);

    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String action, Employee employee, Inventory inventory, Date date) {

        for (Observer observer : observers) {
            observer.update(action, employee, inventory, date);
        }
    }

}
