package backend;

public class RegisteredUser {
	private String name;
	private int age;
	private char gender;
	private int heightInches;
	private int weightPounds;
    private String email; 
    private String hashedPassword;
    private String goal;

    public RegisteredUser(int age, char gender, int heightInches, int weightPounds, String email, String hashedPassword, String goal) {
        this.age = age;
        this.gender = gender;
        this.heightInches = heightInches;
        this.weightPounds = weightPounds;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.goal = goal;
    }

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
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

	public boolean login(String email, String password) {
        return true;
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

}