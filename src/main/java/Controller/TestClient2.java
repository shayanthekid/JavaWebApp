/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TestClient2 {

    private final BlockingQueue<URL> requestQueue;

    public TestClient2() {
        requestQueue = new LinkedBlockingQueue<>();
    }

    public void sendRequest(URL request) throws InterruptedException {
        requestQueue.put(request);
    }

    public void startProcessing() {
        while (true) {
            try {
                URL request = requestQueue.poll(100, TimeUnit.MILLISECONDS);
                if (request != null) {
                    sendHttpRequest(request);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void sendHttpRequest(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Response: Item deleted successfully.");
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            System.out.println("Error sending HTTP request: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<TestClient2> clients = new ArrayList<>();

        // Create 2 clients
        for (int i = 0; i < 2; i++) {
            clients.add(new TestClient2());
        }

        List<Thread> threads = new ArrayList<>();

        // Start the processing thread for each client
        for (final TestClient2 client : clients) {
            Thread clientThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    client.startProcessing();
                }
            });
            threads.add(clientThread);
            clientThread.start();
        }

        // Simulate sending delete requests
        
        
        try {
    for (int i = 0; i < 10; i++) {
        for (TestClient2 client : clients) {
            URL request = new URL("http://localhost:8080/mavenproject1/DeleteItemServlet?itemId=33&employeeId=1");
            client.sendRequest(request);

            // Introduce a delay of 100 milliseconds between requests
            Thread.sleep(100);
            /*
            
            By adding the Thread.sleep(100) statement after each request, 
            introduced a 100-millisecond delay between requests. 
            This delay can create a race condition where multiple clients may attempt to delete the same item simultaneously.
            */
        }
    }
} catch (IOException e) {
    System.out.println("Error creating URL: " + e.getMessage());
} catch (InterruptedException e) {
    System.out.println("Error occurred during delay: " + e.getMessage());
}
      

        // Wait for the processing to complete
        for (Thread thread : threads) {
            thread.join();
        }
    }
}