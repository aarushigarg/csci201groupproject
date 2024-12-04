package backend;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.text.SimpleDateFormat;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class openapi {
	private static DatabaseHandler db;

    public openapi() throws Exception {
    	db = new DatabaseHandler();
        // create an HTTP server listening on port 7273
        HttpServer server = HttpServer.create(new java.net.InetSocketAddress(7273), 0);
        server.createContext("/listen", new RequestHandler());
        server.setExecutor(null); // create a default executor
        System.out.println("Server started on port 7273");
        server.start();
    }

    static class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                InputStream inputStream = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
                reader.close();

                try {
                    // parse the user ID from the request body
                    Gson gson = new Gson();
                    int userId = gson.fromJson(requestBody.toString(), int.class);
                    System.out.println("Received user ID: " + userId);

                    // create an example request to the backend
                    URL url = new URL("http://localhost:5000/process");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    List<Exercise> exlist = db.getExercisesByUserIdPreviousWeek(userId, Date.valueOf(LocalDate.now()));
                    RegisteredUser u = db.getUserById(userId);
                    int age = u.getAge();
                    char gender = u.getGender().toCharArray()[0];
                    int height = u.getHeightInches();
                    int weight = u.getWeightPounds();
                    String goal = u.getGoal();
                    Date currDate = Date.valueOf(LocalDate.now());

                    // create UserData object
                    UserData userData = new UserData(
                    		exlist,
                    		age,
                    		gender,
                    		height,
                    		weight,
                    		goal,
                    		currDate
                    	);
                    // convert to JSON and send to Python
                    String jsonInput = gson.toJson(userData);
                    System.out.println(jsonInput);
                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(jsonInput.getBytes());
                        os.flush();
                    }

                    // read the response from backend
                    String responseStr = "";
                    try (BufferedReader backendReader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                        String backendLine;
                        while ((backendLine = backendReader.readLine()) != null) {
                            responseStr += backendLine;
                        }
                    }

                    // parse and respond with the backend response
                    WorkoutRecommendation response = gson.fromJson(responseStr, WorkoutRecommendation.class);
                    String responseBody = gson.toJson(response.getWorkoutRec());
                    List<BaseExercise> recexercises = response.getWorkoutRec();
                    for(int i = 0; i < recexercises.size(); ++i) {
                    	BaseExercise x = recexercises.get(i);
                    	db.addAIExercise(userId, Date.valueOf(x.getDate()), x.getName(), x.getRepetitions(), x.getSets(), x.getDurationMins());
                    }
                    System.out.println(responseBody);

                } catch (Exception e) {
                    // error handling
                    String errorResponse = "Error processing the request: " + e.getMessage();
                    exchange.sendResponseHeaders(500, errorResponse.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(errorResponse.getBytes());
                    os.close();
                }
            } else {
                // if method is not POST, respond with 405
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new openapi(); // initialize the server
    }
}
