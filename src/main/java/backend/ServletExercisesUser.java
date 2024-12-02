package backend;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/exercises/user/*")
public class ServletExercisesUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Example paths:
        // - /exercises/user/1
        // - /exercises/user/1/date/2024-11-15
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid URL format. Expected /user/{userId} or /user/{userId}/date/{date}");
            return;
        }

        String[] parts = pathInfo.split("/");
        try {
            if (parts.length == 2) {
                // Case: /exercises/user/{userId}
                int userId = Integer.parseInt(parts[1]);
                ExerciseDatabaseAccess.getExercisesByUserId(userId, response);
            } else if (parts.length == 4 && "date".equals(parts[2])) {
                // Case: /exercises/user/{userId}/date/{date}
                int userId = Integer.parseInt(parts[1]);
                String date = parts[3];
                ExerciseDatabaseAccess.getExercisesByUserIdAndDate(userId, date, response);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Invalid URL format. Expected /user/{userId} or /user/{userId}/date/{date}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid ID format. Expected numeric userId.");
        }
	}

}
