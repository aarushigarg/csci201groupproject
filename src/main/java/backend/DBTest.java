package backend;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest {
	public static void main(String[] args) {
		saveUser();
	}
	
	public static void saveUser() {
		try (Connection connection = DatabaseHandler.getConnection()) {
			Statement st = connection.createStatement();
			String query = "INSERT INTO Users (age, gender, height_inches, weight_pounds, email, hashed_password, goal) " +
						   "VALUES (18, 'F', 60, 120, 'a@gmail.com', 123, 'Lose weight')";
			st.executeUpdate(query);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Saved user");
	}
}
