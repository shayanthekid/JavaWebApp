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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author sajid
 */
public class InventoryController {
    
    
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
    
    
}
