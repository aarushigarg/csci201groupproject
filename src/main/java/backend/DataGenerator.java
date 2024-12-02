package backend;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    public static RegisteredUser generateRandomUser() {
        Random rand = new Random();
        String name = "User" + rand.nextInt(1000); // Random user name
        int age = rand.nextInt(50) + 18;          // Age between 18 and 68
        char gender = rand.nextBoolean() ? 'M' : 'F'; // Random gender
        int height = rand.nextInt(30) + 150;      // Height in cm
        int weight = rand.nextInt(50) + 50;       // Weight in kg
        String email = name.toLowerCase() + "@example.com"; // Valid email format
        String password = generateValidPassword(); // Valid password
        String goal = generateRandomGoal();       // Random fitness goal

        return new RegisteredUser(name, age, gender, height, weight, email, password, goal);
    }

    private static String generateValidPassword() {
        Random rand = new Random();
        char capitalLetter = (char) ('A' + rand.nextInt(26)); 
        char number = (char) ('0' + rand.nextInt(10));      
        String basePassword = "password";                 
        return capitalLetter + basePassword + number; 
    }

    private static String generateRandomGoal() {
        String[] goals = {"Lose weight", "Gain muscle", "Maintain fitness", "Improve cardio"};
        return goals[new Random().nextInt(goals.length)];
    }

    public static List<Exercise> generateExerciseHistory(int userId, int days) {
        List<Exercise> exercises = new ArrayList<>();
        Random rand = new Random();
        String[] exerciseNames = {"Squats", "Deadlift", "Bench Press", "Pull-Ups", "Plank"};

        for (int i = 0; i < days; i++) { // Generate exercises for specified days
            Date date = new Date(System.currentTimeMillis() - i * 1000L * 60 * 60 * 24); // Recent days
            String name = exerciseNames[rand.nextInt(exerciseNames.length)];
            int repetitions = rand.nextInt(20) + 5; // Between 5 and 25 reps
            int sets = rand.nextInt(5) + 1;         // Between 1 and 5 sets
            int durationMins = rand.nextInt(60) + 10; // Between 10 and 70 minutes
            int isAISuggestion = rand.nextInt(20) + 5; 

            exercises.add(new Exercise(0, userId, date, name, repetitions, sets, durationMins,isAISuggestion));
        }
        return exercises;
    }
    }
