package backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserDatabaseAccess {
    private static final Gson gson = new GsonBuilder().create();

    public static void getUserById(HttpServletRequest request, HttpServletResponse response) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String pathInfo = request.getPathInfo(); // e.g., /1
            if (pathInfo == null || pathInfo.split("/").length != 2) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Invalid URL format. Expected /{id}");
                return;
            }

            int userId = Integer.parseInt(pathInfo.split("/")[1]);
            String query = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    RegisteredUser user = new RegisteredUser(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("hashed_password"), // Updated
                        resultSet.getInt("weight_pounds"),     // Updated
                        resultSet.getInt("height_inches"),     // Updated
                        resultSet.getInt("age"),
                        resultSet.getString("gender").charAt(0),
                        resultSet.getString("goal")
                    );

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().println(gson.toJson(user));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().println("User not found.");
                }
            }
        } catch (SQLException | IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().println("Error retrieving user: " + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public static void getAllUsers(HttpServletResponse response) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM users";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();

                StringBuilder jsonResponse = new StringBuilder("[");
                while (resultSet.next()) {
                    RegisteredUser user = new RegisteredUser(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("hashed_password"), // Updated
                        resultSet.getInt("weight_pounds"),     // Updated
                        resultSet.getInt("height_inches"),     // Updated
                        resultSet.getInt("age"),
                        resultSet.getString("gender").charAt(0),
                        resultSet.getString("goal")
                    );
                    jsonResponse.append(gson.toJson(user)).append(",");
                }
                if (jsonResponse.length() > 1) {
                    jsonResponse.setLength(jsonResponse.length() - 1); // Remove trailing comma
                }
                jsonResponse.append("]");

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println(jsonResponse.toString());
            }
        } catch (SQLException | IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().println("Error retrieving users: " + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public static void createUser(HttpServletRequest request, HttpServletResponse response) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            StringBuilder requestBody = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            }

            RegisteredUser newUser = gson.fromJson(requestBody.toString(), RegisteredUser.class);

            // Validate gender
            if (!isValidGender(newUser.getGender())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Invalid gender value. Allowed values: M, F, O.");
                return;
            }

            String query = "INSERT INTO users (email, hashed_password, weight_pounds, height_inches, age, gender, goal) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, newUser.getEmail());
                statement.setString(2, newUser.getHashed_password());
                statement.setInt(3, newUser.getWeightPounds());
                statement.setInt(4, newUser.getHeightInches());
                statement.setInt(5, newUser.getAge());
                statement.setString(6, String.valueOf(newUser.getGender())); // Convert char to String for SQL
                statement.setString(7, newUser.getGoal());
                statement.executeUpdate();

                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().println("User created successfully.");
            }
        } catch (SQLException | IOException e) {
            handleException(response, "Error creating user", e);
        }
    }

    public static void updateUser(HttpServletRequest request, HttpServletResponse response) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.split("/").length != 2) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Invalid URL format. Expected /{id}");
                return;
            }

            int userId = Integer.parseInt(pathInfo.split("/")[1]);
            StringBuilder requestBody = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            }

            RegisteredUser updatedUser = gson.fromJson(requestBody.toString(), RegisteredUser.class);
            
            if (!isValidGender(updatedUser.getGender())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Invalid gender value. Allowed values: M, F, O.");
                return;
            }
            
            String query = "UPDATE users SET email = ?, hashed_password = ?, weight_pounds = ?, height_inches = ?, age = ?, gender = ?, goal = ? WHERE id = ?"; // Updated
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, updatedUser.getEmail());
                statement.setString(2, updatedUser.getHashed_password()); // Updated
                statement.setInt(3, updatedUser.getWeightPounds());      // Updated
                statement.setInt(4, updatedUser.getHeightInches());      // Updated
                statement.setInt(5, updatedUser.getAge());
                statement.setString(6, String.valueOf(updatedUser.getGender()));
                statement.setString(7, updatedUser.getGoal());
                statement.setInt(8, userId);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println("User updated successfully.");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().println("User not found.");
                }
            }
        } catch (SQLException | IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().println("Error updating user: " + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    public static void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.split("/").length != 2) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Invalid URL format. Expected /{id}");
                return;
            }

            int userId = Integer.parseInt(pathInfo.split("/")[1]);
            String query = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().println("User not found.");
                }
            }
        } catch (SQLException | IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().println("Error deleting user: " + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
    
    
    private static boolean isValidGender(char gender) {
        return gender == 'M' || gender == 'F' || gender == 'O';
    }
    private static void handleException(HttpServletResponse response, String message, Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        try {
            response.getWriter().println(message + ": " + e.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        e.printStackTrace();
    }
}

