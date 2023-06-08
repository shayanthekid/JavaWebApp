package Controller;

import com.google.gson.Gson;
import Models.Employee;
import Models.Log;
import Models.LogJson;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetPersonalUsageServlet extends HttpServlet {

    private Gson gson; // Gson instance for JSON conversion
    private Logger_controller logController; // Logger controller instance

    @Override
    public void init() {
        // Initialize the Gson instance
        gson = new Gson();
        // Initialize the Logger_controller instance
        logController = new Logger_controller();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Retrieve the employee ID from the request parameters
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));

            // Create an instance of your Employee class with the retrieved ID
            Employee employee = new Employee.EmployeeBuilder(employeeId).build();

            // Call the CheckUsage method to retrieve the logs for the employee
            List<LogJson> logsJson = logController.CheckUsage(employee);

            // Convert the list of logs to JSON using Gson
            String logsJsonString = gson.toJson(logsJson);

            // Set the response content type to JSON
            response.setContentType("application/json");

            // Write the JSON response to the servlet response
            response.getWriter().write(logsJsonString);
        } catch (NumberFormatException e) {
            // Handle NumberFormatException and set appropriate response status and message
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid employee ID");
        } catch (Exception e) {
            // Handle other exceptions and set appropriate response status and message
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error retrieving personal usage: " + e.getMessage());
        }
    }

    
}