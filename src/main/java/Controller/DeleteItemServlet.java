/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Models.Employee;
import Models.Inventory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sajid
 */
public class DeleteItemServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */


    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
        // Retrieve the input parameters from the request
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        java.sql.Date date = new java.sql.Date(new Date().getTime());
        
        InventoryController inventoryController = new InventoryController();
        Inventory inventory = new Inventory.InventoryBuilder()
            .id(itemId)
            .build();

        Employee employee = new Employee.EmployeeBuilder(employeeId).build();
        
        // Call the removeItem method with the provided parameters
        inventoryController.removeItem(inventory, employee, date);
        
        // Set success response code and message
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Item deleted successfully");
    } catch (Exception e) {
        // Set error response code and message
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("Error deleting item: " + e.getMessage());
    }
}
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
