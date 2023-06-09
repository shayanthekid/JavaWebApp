import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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
            connection.setDoOutput(true);

            String params = "itemId=25&price=22&quantity=1&employeeId=1";
            byte[] postData = params.getBytes("UTF-8");
            connection.setRequestProperty("Content-Length", Integer.toString(postData.length));

            OutputStream outputStream = null;
            try {
                outputStream = connection.getOutputStream();
                outputStream.write(postData);
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Error sending request data: " + e.getMessage());
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        System.out.println("Error closing output stream: " + e.getMessage());
                    }
                }
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Item removed successfully");
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            System.out.println("Error sending HTTP request: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String servletUrl = "http://localhost:8080/mavenproject1/UpdateItemServlet";
        List<TestClient3> clients = new ArrayList<>();

        // Create 30 clients
        for (int i = 0; i < 30; i++) {
            clients.add(new TestClient3());
        }

        List<Thread> threads = new ArrayList<>();

        // Start the processing thread for each client
        for (final TestClient3 client : clients) {
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
            for (int i = 0; i < 30; i++) {
                URL request = new URL(servletUrl);
                clients.get(i % clients.size()).sendRequest(request);
            }
        } catch (IOException e) {
            System.out.println("Error creating URL: " + e.getMessage());
        }

        // Wait for the processing to complete
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
