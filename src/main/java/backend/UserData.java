package backend;

import java.util.List;
import java.sql.Date;
import java.time.LocalDate;

/**
 * This class encapsulates all the data related to a user's
 * exercise plan and physical attributes.
 */
public class UserData {

    // List to hold Exercise objects
    private List<Exercise> exercises;

    // User's age in years
    int age;

    // User's gender represented as a String
    private String gender;

    // User's height in inches
     private int height;

    // User's weight in pounds
  private int weight;

    // User's fitness goal as a String
    private String goal;

    // Current date as a Date object
    private Date currDate;

    // Additional fields for email and hashedPassword
    private String email;
    String hashedPassword;

    // User ID from the database
    private int id;

    // Default constructor
    public UserData() {
        this.exercises = null;
        this.age = 0;
        this.gender = null;
        this.height = 0;
        this.weight = 0;
        this.goal = null;
        this.currDate = Date.valueOf(LocalDate.now());
        this.email = null;
        this.hashedPassword = null;
        this.id = 0;
    }

    // Constructor for database integration
    public UserData(
        int id,
        String email,
        String hashedPassword,
        int weight,
        int height,
        int age,
        String gender,
        String goal
    ) {
        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
        this.goal = goal;
        this.currDate = Date.valueOf(LocalDate.now());
    }

    // Getters and setters
    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Date getDate() {
        return currDate;
    }

    public void setDate(Date currDate) {
        this.currDate = currDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

