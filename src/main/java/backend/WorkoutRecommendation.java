package backend;

import java.util.ArrayList;

public class WorkoutRecommendation {
	private ArrayList<BaseExercise> workout_rec;
	public WorkoutRecommendation(ArrayList<BaseExercise> workout_rec) {
		this.workout_rec = workout_rec;
	}
	public ArrayList<BaseExercise> getWorkoutRec() {
		return this.workout_rec;
	}
}
