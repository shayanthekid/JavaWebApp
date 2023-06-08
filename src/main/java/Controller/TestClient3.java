/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author sajid
 */

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.OutputStream;
public class TestClient3 {

    private final BlockingQueue<URL> requestQueue;

    public TestClient3() {
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
            connection.setRequestMethod("POST");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Response: Item updated successfully.");
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

        // Simulate sending update requests
        try {
            for (int i = 0; i < 10; i++) {
             for (TestClient2 client : clients) {
    URL request = new URL("http://localhost:8080/mavenproject1/UpdateItemServlet");
    HttpURLConnection connection = (HttpURLConnection) request.openConnection();
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);

    // Set the request parameters
    String parameters = "itemId=25&price=999&quantity=10&employeeId=1";
    byte[] postData = parameters.getBytes("UTF-8");

    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    connection.setRequestProperty("Content-Length", String.valueOf(postData.length));

    // Write the parameters to the output stream
   try (OutputStream outputStream = connection.getOutputStream()) {
    outputStream.write(postData);
}
    client.sendRequest(request);

    // Introduce a delay of 100 milliseconds between requests
    Thread.sleep(100);
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