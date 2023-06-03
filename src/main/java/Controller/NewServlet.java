package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewServlet extends HttpServlet {

    private String url = "jdbc:postgresql://localhost:5432/Inventory";
    private String username = "postgres";
    private String password = "1234";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // Database connection status message
            String dbStatusMessage;

            // Database connection variables
            Connection connection = null;

            try {
                // Register the PostgreSQL JDBC driver
                Class.forName("org.postgresql.Driver");

                // Establish a connection to the database
                connection = DriverManager.getConnection(url, username, password);

                // Set the database connection status message
                dbStatusMessage = "Database connection successful!";
            } catch (ClassNotFoundException e) {
                // Handle driver not found exception
                dbStatusMessage = "Error: PostgreSQL JDBC driver not found!";
            } catch (SQLException e) {
                // Handle database connection errors
                dbStatusMessage = "Error: Failed to connect to the database!";
            } finally {
                // Close the connection
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        // Handle connection closing error
                        dbStatusMessage = "Error: Failed to close the database connection!";
                    }
                }
            }

            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet NewServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
            out.println("<p>Database Status: " + dbStatusMessage + "</p>");
            out.println("</body>");
            out.println("</html>");
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
