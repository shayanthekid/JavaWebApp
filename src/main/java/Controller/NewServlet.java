package Controller;

import Models.Inventory;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewServlet extends HttpServlet {

//    private String url = "jdbc:postgresql://localhost:5432/Inventory";
//    private String username = "postgres";
//    private String password = "1234";
    
 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            // Create an instance of InventoryController
            InventoryController inventoryController = new InventoryController();

            // Call the displayAllItems method and get the list of inventory items
            List<Inventory> items = inventoryController.displayAllItems();

            // Convert the list of inventory items to JSON format
            Gson gson = new Gson();
            String jsonData = gson.toJson(items);

            // Write the JSON data to the response output stream
            response.getWriter().write(jsonData);
        }
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
