package backend;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/exercises/*")
public class ServletExercisesId extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ExerciseDatabaseAccess.updateExercise(request, response);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ExerciseDatabaseAccess.deleteExercise(request, response);
	}

}
