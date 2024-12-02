package backend;

import java.sql.Date;

public class Exercise {
    private int id;
    private int userId;
    private Date date;
    private String name; 
    private int repetitions;
    private int sets;
    private int durationMins;
    private Boolean isAISuggestion;
    public Exercise() {}
    public Exercise(int id, int userId, Date date, String name, int repetitions, int sets, int durationMins, int isAISuggestion) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.name = name;
        this.repetitions = repetitions;
        this.sets = sets;
        this.durationMins = durationMins;
        if (isAISuggestion == 1)
        	this.setIsAISuggestion(true);
        else
        	this.setIsAISuggestion(false);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return name;
    }

    public void setType(String name) {
        this.name = name;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public double getDuration() {
        return durationMins;
    }

    public void setDuration(int durationMins) {
        this.durationMins = durationMins;
    }

	public Boolean getIsAISuggestion() {
		return isAISuggestion;
	}

	public void setIsAISuggestion(Boolean isAISuggestion) {
		this.isAISuggestion = isAISuggestion;
	}
}
