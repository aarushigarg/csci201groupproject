package backend;

public class RegisteredUser {
    private int id; // Unique identifier
    private int age;
    private String gender; // Changed to String for compatibility
    private int heightInches;
    private int weightPounds;
    private String email;
    private String hashedPassword;
    private String goal;

    // Constructor with ID (used when reading from the database)
    public RegisteredUser(int id, String email, String hashedPassword, int weightPounds, int heightInches, int age, String gender, String goal) {
        this.id = id;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.weightPounds = weightPounds;
        this.heightInches = heightInches;
        this.age = age;
        this.gender = gender;
        this.goal = goal;
    }

    // Constructor without ID (used when creating a new user)
    public RegisteredUser(String email, String hashedPassword, int weightPounds, int heightInches, int age, String gender, String goal) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.weightPounds = weightPounds;
        this.heightInches = heightInches;
        this.age = age;
        this.gender = gender;
        this.goal = goal;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getHeightInches() {
        return heightInches;
    }

    public void setHeightInches(int heightInches) {
        this.heightInches = heightInches;
    }

    public int getWeightPounds() {
        return weightPounds;
    }

    public void setWeightPounds(int weightPounds) {
        this.weightPounds = weightPounds;
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

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    @Override
    public String toString() {
        return "RegisteredUser{" +
                "id=" + id +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", heightInches=" + heightInches +
                ", weightPounds=" + weightPounds +
                ", email='" + email + '\'' +
                ", goal='" + goal + '\'' +
                '}';
    }
}
