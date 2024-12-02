package backend;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class testAPI {
    public static void main(String[] args) {
        try {
            // define the target URL
            URL url = new URL("http://localhost:7273/listen");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set up the request
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // prepare the user ID in JSON format
            String userIdJson = "15"; // test userID

            // write the JSON data to the request body
            try (OutputStream os = conn.getOutputStream()) {
                os.write(userIdJson.getBytes());
                os.flush();
            }

            // read and print the response
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) {
                try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("Response: " + response.toString());
                }
            } else {
                System.out.println("Error: " + conn.getResponseMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
