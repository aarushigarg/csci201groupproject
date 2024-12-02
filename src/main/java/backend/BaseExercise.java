package backend;
/**
 * A class to represent an exercise with essential attributes: date, name, repetitions, sets, and duration.
 */
public class BaseExercise {
    // Attributes
    private String date;
    private String name;
    private int repetitions;
    private int sets;
    private int durationMins;

    /**
     * default constructor, initializes the BaseExercise object with default values.
     */
    public BaseExercise() {
        this.date = "";
        this.name = "";
        this.repetitions = 0;
        this.sets = 0;
        this.durationMins = 0;
    }

    /**
     * parameterized constructor, initializes the BaseExercise object with specified values.
     * 
     * @param date         the date of the exercise in YYYY-MM-DD format.
     * @param name         the name of the exercise (e.g., "Push-up", "Rest").
     * @param repetitions  the number of repetitions performed (0 for rest days).
     * @param sets         the number of sets performed (0 for rest days).
     * @param durationMins the duration of the exercise in minutes (0 for rest days).
     */
    public BaseExercise(String date, String name, int repetitions, int sets, int durationMins) {
        this.date = date;
        this.name = name;
        this.repetitions = repetitions;
        this.sets = sets;
        this.durationMins = durationMins;
    }

    // getters and setters

    /**
     * gets the date of the exercise.
     * 
     * @return the date of the exercise in YYYY-MM-DD format.
     */
    public String getDate() {
        return date;
    }

    /**
     * sets the date of the exercise.
     * 
     * @param date the date in YYYY-MM-DD format.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * gets the name of the exercise.
     * 
     * @return the name of the exercise.
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the exercise.
     * 
     * @param name the name of the exercise (e.g., "Push-up", "Rest").
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the number of repetitions.
     * 
     * @return the number of repetitions (0 for rest days).
     */
    public int getRepetitions() {
        return repetitions;
    }

    /**
     * sets the number of repetitions.
     * 
     * @param repetitions the number of repetitions (0 for rest days).
     */
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    /**
     * gets the number of sets.
     * 
     * @return the number of sets (0 for rest days).
     */
    public int getSets() {
        return sets;
    }

    /**
     * sets the number of sets.
     * 
     * @param sets the number of sets (0 for rest days).
     */
    public void setSets(int sets) {
        this.sets = sets;
    }

    /**
     * gets the duration of the exercise in minutes.
     * 
     * @return the duration of the exercise in minutes (0 for rest days).
     */
    public int getDurationMins() {
        return durationMins;
    }

    /**
     * sets the duration of the exercise in minutes.
     * 
     * @param durationMins the duration of the exercise in minutes (0 for rest days).
     */
    public void setDurationMins(int durationMins) {
        this.durationMins = durationMins;
    }

}
