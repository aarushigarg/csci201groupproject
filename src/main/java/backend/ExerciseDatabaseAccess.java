package backend;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import java.sql.Date;

public class ExerciseDatabaseAccess {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Gson gson = new Gson();

    public static void getExercisesByUserIdAndDate(int userId, String date, HttpServletResponse response) {
	    lock.lock();
	    try (Connection connection = DatabaseHandler.getConnection()) {
	        String query = "SELECT * FROM Exercise WHERE user_id = ? AND date = ?";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setInt(1, userId);
	            statement.setDate(2, Date.valueOf(date)); // Convert string to SQL Date
	            ResultSet resultSet = statement.executeQuery();
	
	            // Build JSON response
	            StringBuilder jsonResponse = new StringBuilder("[");
	            while (resultSet.next()) {
	                Exercise exercise = new Exercise(
	                    resultSet.getInt("id"),
	                    resultSet.getInt("user_id"),
	                    resultSet.getDate("date"),
	                    resultSet.getString("name"),
	                    resultSet.getInt("repetitions"),
	                    resultSet.getInt("sets"),
	                    resultSet.getInt("duration_mins"),
	                    resultSet.getInt("is_ai_suggestion")
	                );
	                jsonResponse.append(gson.toJson(exercise)).append(",");
	            }
	            if (jsonResponse.length() > 1) {
	                jsonResponse.setLength(jsonResponse.length() - 1); // Remove trailing comma
	            }
	            jsonResponse.append("]");
	
	            response.setStatus(HttpServletResponse.SC_OK);
	            response.setContentType("application/json");
	            response.getWriter().println(jsonResponse.toString());
	        } catch (IOException e) {
				e.printStackTrace();
			}
	    } catch (SQLException e) {
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        try {
	            response.getWriter().println("Error retrieving exercises: " + e.getMessage());
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
	    } finally {
	        lock.unlock();
	    }
	}

    public static void getExercisesByUserId(int userId, HttpServletResponse response) {
	    lock.lock();
	    try (Connection connection = DatabaseHandler.getConnection()) {
	        String query = "SELECT * FROM Exercise WHERE user_id = ?";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setInt(1, userId);
	            ResultSet resultSet = statement.executeQuery();
	
	            // Build JSON response
	            StringBuilder jsonResponse = new StringBuilder("[");
	            while (resultSet.next()) {
	                Exercise exercise = new Exercise(
	                    resultSet.getInt("id"),
	                    resultSet.getInt("user_id"),
	                    resultSet.getDate("date"),
	                    resultSet.getString("name"),
	                    resultSet.getInt("repetitions"),
	                    resultSet.getInt("sets"),
	                    resultSet.getInt("duration_mins"),
	                    resultSet.getInt("is_ai_suggestion")
	                );
	                jsonResponse.append(gson.toJson(exercise)).append(",");
	            }
	            if (jsonResponse.length() > 1) {
	                jsonResponse.setLength(jsonResponse.length() - 1); // Remove trailing comma
	            }
	            jsonResponse.append("]");
	
	            response.setStatus(HttpServletResponse.SC_OK);
	            response.setContentType("application/json");
	            response.getWriter().println(jsonResponse.toString());
	        } catch (IOException e) {
				e.printStackTrace();
			}
	    } catch (SQLException e) {
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        try {
	            response.getWriter().println("Error retrieving exercises: " + e.getMessage());
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
	    } finally {
	        lock.unlock();
	    }
	}

    public static void createExercise(HttpServletRequest request, HttpServletResponse response) {
        lock.lock();
        try (Connection connection = DatabaseHandler.getConnection()) {
            // Read the JSON request body
            StringBuilder requestBody = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
            }

            // Parse JSON into Exercise object
            Exercise newExercise = gson.fromJson(requestBody.toString(), Exercise.class);

            // SQL query to insert the exercise
            String query = "INSERT INTO Exercise (user_id, date, name, repetitions, sets, duration_mins, is_ai_suggestion) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, newExercise.getUserId());
                statement.setDate(2, newExercise.getDate());
                statement.setString(3, newExercise.getType());
                statement.setInt(4, newExercise.getRepetitions());
                statement.setInt(5, newExercise.getSets());
                statement.setInt(6, (int) newExercise.getDuration());
                statement.setBoolean(7, newExercise.getIsAISuggestion());

                // Execute the statement
                statement.executeUpdate();

                // Respond with HTTP 201 Created
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().println("Exercise created successfully.");
            }
        } catch (SQLException | IOException e) {
            try {
                // Handle errors
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Error creating exercise: " + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void updateExercise(HttpServletRequest request, HttpServletResponse response) {
	    lock.lock();
	    try (Connection connection = DatabaseHandler.getConnection()) {
	        // Extract exercise ID from the URI
	        String pathInfo = request.getPathInfo(); // e.g., /1
	        if (pathInfo == null || pathInfo.split("/").length != 2) {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            response.getWriter().println("Invalid URL format. Expected /{id}");
	            return;
	        }
	
	        int exerciseId = Integer.parseInt(pathInfo.split("/")[1]);
	
	        // Parse JSON request body
	        StringBuilder requestBody = new StringBuilder();
	        try (BufferedReader reader = request.getReader()) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                requestBody.append(line);
	            }
	        }
	        Exercise updatedExercise = gson.fromJson(requestBody.toString(), Exercise.class);
	
	        // SQL query to update exercise
	        String query = "UPDATE Exercise SET name = ?, repetitions = ?, sets = ?, duration_mins = ?, is_ai_suggestion = ? WHERE id = ?";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setString(1, updatedExercise.getType());
	            statement.setInt(2, updatedExercise.getRepetitions());
	            statement.setInt(3, updatedExercise.getSets());
	            statement.setInt(4, (int) updatedExercise.getDuration());
	            statement.setBoolean(5, updatedExercise.getIsAISuggestion());
	            statement.setInt(6, exerciseId);
	
	            int rowsUpdated = statement.executeUpdate();
	
	            if (rowsUpdated > 0) {
	                response.setStatus(HttpServletResponse.SC_OK);
	                response.getWriter().println("Exercise updated successfully.");
	            } else {
	                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	                response.getWriter().println("Exercise not found.");
	            }
	        }
	    } catch (SQLException | IOException e) {
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        try {
				response.getWriter().println("Error updating exercise: " + e.getMessage());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
	        e.printStackTrace();
	    } finally {
	        lock.unlock();
	    }
    }

    public static void deleteExercise(HttpServletRequest request, HttpServletResponse response) {
	    lock.lock();
	    try (Connection connection = DatabaseHandler.getConnection()) {
	        // Extract exercise ID from the URI
	        String pathInfo = request.getPathInfo(); // e.g., /1
	        if (pathInfo == null || pathInfo.split("/").length != 2) {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            response.getWriter().println("Invalid URL format. Expected /{id}");
	            return;
	        }
	
	        int exerciseId = Integer.parseInt(pathInfo.split("/")[1]);
	
	        // SQL query to delete exercise
	        String query = "DELETE FROM Exercise WHERE id = ?";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setInt(1, exerciseId);
	            int rowsDeleted = statement.executeUpdate();
	
	            if (rowsDeleted > 0) {
	                response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
	            } else {
	                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	                response.getWriter().println("Exercise not found.");
	            }
	        }
	    } catch (SQLException | IOException e) {
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        try {
				response.getWriter().println("Error deleting exercise: " + e.getMessage());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
	        e.printStackTrace();
	    } finally {
	        lock.unlock();
	    }
    }
}
