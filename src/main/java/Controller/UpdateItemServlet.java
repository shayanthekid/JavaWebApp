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
public class UpdateItemServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
  

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          // Retrieve the form data from the request
        int itemId = Integer.parseInt(request.getParameter("itemId"));
    float price = Float.parseFloat(request.getParameter("price"));
    int quantity = Integer.parseInt(request.getParameter("quantity"));
    int employeeId = Integer.parseInt(request.getParameter("employeeId"));

    // Create instances of the required objects
    Inventory inventory = new Inventory.InventoryBuilder()
            .id(itemId)
            .price(price)
            .quantity(quantity)
            .build();

    Employee employee = new Employee.EmployeeBuilder(employeeId).build();

    // Call the addItem method of InventoryController
    InventoryController inventoryController = new InventoryController();
    java.sql.Date date = new java.sql.Date(new Date().getTime());

    try {
        inventoryController.updateItem(inventory, employee, date);

        // Item added successfully, redirect to the additem.html page
       response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('Item updated successfully');");
        out.println("window.location.href = 'updateitem.html';");
        out.println("</script>");
        out.close();
    } catch (IllegalArgumentException e) {
        // An error occurred, display an alert and redirect back to the additem.html page
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('" + e.getMessage() + "');");
        out.println("window.location.href = 'updateitem.html';");
        out.println("</script>");
        out.close();
    }
    }

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
