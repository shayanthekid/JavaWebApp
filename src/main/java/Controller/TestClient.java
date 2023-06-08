/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TestClient {

    private final BlockingQueue<URL> requestQueue;

    public TestClient() {
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
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                System.out.println("Response: " + response.toString());
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            System.out.println("Error sending HTTP request: " + e.getMessage());
        }
    }
public static void main(String[] args) throws InterruptedException {
    List<TestClient> clients = new ArrayList<>();

    // Create 15 clients
    for (int i = 0; i < 15; i++) {
        clients.add(new TestClient());
    }

    List<Thread> threads = new ArrayList<>();

    // Start the processing thread for each client
    for (final TestClient client : clients) {
        Thread clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                client.startProcessing();
            }
        });
        threads.add(clientThread);
        clientThread.start();
    }

    // Simulate sending requests
    try {
        for (int i = 0; i < 10; i++) {
            for (TestClient client : clients) {
                URL request = new URL("http://localhost:8080/mavenproject1/NewServlet");
                client.sendRequest(request);
            }
        }
    } catch (IOException e) {
        System.out.println("Error creating URL: " + e.getMessage());
    }

    // Wait for the processing to complete
    for (Thread thread : threads) {
        thread.join();
    }
}
//    public static void main(String[] args) throws InterruptedException {
//        final TestClient  client1 = new TestClient();
//        final  TestClient client2 = new TestClient();
//
//        // Start the processing thread for each client
//        Thread client1Thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                client1.startProcessing();
//            }
//        });
//
//        Thread client2Thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                client2.startProcessing();
//            }
//        });
//
//        client1Thread.start();
//        client2Thread.start();
//
//        // Simulate sending requests
//        try {
//            for (int i = 0; i < 10; i++) {
//                URL request = new URL("http://localhost:8080/mavenproject1/NewServlet");
//                client1.sendRequest(request);
//
//                URL request2 = new URL("http://localhost:8080/mavenproject1/DisplayLogsServlet");
//                client2.sendRequest(request2);
//            }
//        } catch (IOException e) {
//            System.out.println("Error creating URL: " + e.getMessage());
//        }
//
//        // Wait for the processing to complete
//        client1Thread.join();
//        client2Thread.join();
//    }
}