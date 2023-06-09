package Controller;

import Models.Employee;
import Models.Inventory;
import java.io.IOException;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteItemServlet extends HttpServlet {

    private Queue<HttpServletRequest> requestQueue;
    private ExecutorService executorService;

    @Override
  public void init() throws ServletException {
    super.init();
    // Create a concurrent linked queue to store the requests
    requestQueue = new ConcurrentLinkedQueue<>();
    // Specify the number of threads to be used for processing the requests
    int numThreads = 5;
    // Create a fixed thread pool with the specified number of threads
    executorService = Executors.newFixedThreadPool(numThreads);
}

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Add the request to the queue for processing
        requestQueue.add(request);

        // Process the requests from the queue and the current request
        while (!requestQueue.isEmpty()) {
            HttpServletRequest queuedRequest = requestQueue.poll();
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

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // Set error response code and message
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error deleting item: " + e.getMessage());
            }
        }

    }

    @Override
    public String getServletInfo() {
        return "Servlet for deleting items";
    }

}
