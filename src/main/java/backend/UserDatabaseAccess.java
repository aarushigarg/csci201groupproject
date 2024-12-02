package backend;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import com.google.gson.Gson;

public class UserDatabaseAccess {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Gson gson = new Gson();

    public static void getUserById(String uri, PrintWriter out) {
        // Endpoint: /users/{id}
        lock.lock();
        try {
            // Extract user ID from URI
            int userId = Integer.parseInt(uri.split("/")[2]);

            try (Connection connection = DatabaseHandler.getConnection()) {
                String query = "SELECT * FROM users WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, userId);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        RegisteredUser user = new RegisteredUser(
                            resultSet.getInt("id"), // add 
                            resultSet.getString("email"),
                            resultSet.getString("hashedPassword"),
                            resultSet.getInt("weightPounds"),
                            resultSet.getInt("heightInches"),
                            resultSet.getInt("age"),
                            resultSet.getString("gender"),
                            resultSet.getString("goal")
                        );

                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: application/json");
                        out.println();
                        out.println(gson.toJson(user));
                    } else {
                        sendErrorResponse(out, 404, "User not found");
                    }
                }
            } catch (SQLException e) {
                sendErrorResponse(out, 500, "Internal Server Error");
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

    public static void getAllUsers(PrintWriter out) {
        // Endpoint: /users
        lock.lock();
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM users";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();

                StringBuilder jsonResponse = new StringBuilder("[");
                while (resultSet.next()) {
                	RegisteredUser user = new RegisteredUser(
                        resultSet.getInt("id"), // need to fix the ID/USER Class
                        resultSet.getString("email"),
                        resultSet.getString("hashedPassword"),
                        resultSet.getInt("weightPounds"),
                        resultSet.getInt("heightInches"),
                        resultSet.getInt("age"),
                        resultSet.getString("gender"),
                        resultSet.getString("goal")
                    );
                    jsonResponse.append(gson.toJson(user)).append(",");
                }
                if (jsonResponse.length() > 1) {
                    jsonResponse.setLength(jsonResponse.length() - 1); // Remove trailing comma
                }
                jsonResponse.append("]");

                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: application/json");
                out.println();
                out.println(jsonResponse.toString());
            }
        } catch (SQLException e) {
            sendErrorResponse(out, 500, "Internal Server Error");
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void createUser(BufferedReader in, PrintWriter out) {
        // Endpoint: /users
        lock.lock();
        try (Connection connection = DatabaseHandler.getConnection()) {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                requestBody.append(line);
            }
            RegisteredUser newUser = gson.fromJson(requestBody.toString(), RegisteredUser.class);

            String query = "INSERT INTO users (email, hashedPassword, weightPounds, heightInches, age, gender, goal) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, newUser.getEmail());
                statement.setString(2, newUser.getHashedPassword());
                statement.setInt(3, newUser.getWeightPounds());
                statement.setInt(4, newUser.getHeightInches());
                statement.setInt(5, newUser.getAge());
                statement.setString(6, String.valueOf(newUser.getGender()));
                statement.setString(7, newUser.getGoal());
                statement.executeUpdate();

                out.println("HTTP/1.1 201 Created");
                out.println("Content-Type: application/json");
                out.println();
                out.println(gson.toJson(newUser));
            }
        } catch (SQLException | IOException e) {
            sendErrorResponse(out, 500, "Internal Server Error");
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void updateUser(String uri, BufferedReader in, PrintWriter out) {
        // Endpoint: /users/{id}
        lock.lock();
        try (Connection connection = DatabaseHandler.getConnection()) {
            int userId = Integer.parseInt(uri.split("/")[2]);

            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                requestBody.append(line);
            }
            RegisteredUser updatedUser = gson.fromJson(requestBody.toString(), RegisteredUser.class);

            String query = "UPDATE users SET email = ?, hashedPassword = ?, weightPounds = ?, heightInches = ?, age = ?, gender = ?, goal = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
            	statement.setString(1, updatedUser.getEmail());
                statement.setString(2, updatedUser.getHashedPassword());
                statement.setInt(3, updatedUser.getWeightPounds());
                statement.setInt(4, updatedUser.getHeightInches());
                statement.setInt(5, updatedUser.getAge());
                statement.setString(6, String.valueOf(updatedUser.getGender()));
                statement.setString(7, updatedUser.getGoal());
                statement.setInt(8, userId);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    out.println("HTTP/1.1 200 OK");
                    out.println();
                } else {
                    sendErrorResponse(out, 404, "User not found");
                }
            }
        } catch (SQLException | IOException e) {
            sendErrorResponse(out, 500, "Internal Server Error");
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void deleteUser(String uri, PrintWriter out) {
        // Endpoint: /users/{id}
        lock.lock();
        try (Connection connection = DatabaseHandler.getConnection()) {
            int userId = Integer.parseInt(uri.split("/")[2]);

            String query = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    out.println("HTTP/1.1 204 No Content");
                    out.println();
                } else {
                    sendErrorResponse(out, 404, "User not found");
                }
            }
        } catch (SQLException e) {
            sendErrorResponse(out, 500, "Internal Server Error");
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private static void sendErrorResponse(PrintWriter out, int statusCode, String message) {
        out.println("HTTP/1.1 " + statusCode + " " + message);
        out.println("Content-Type: text/plain");
        out.println();
        out.println(message);
    }
}