
public class Constants {

	
	static final int NUM_STUDENTS = 600;
	
	static final boolean PARSE_ALL_COURSES = true;
	
	static final int TOT_COURSES = 40;
	
	static final int STUD_COURSE_LIMIT = 4;
	
	static final int NUM_PREFS = 8;
	
	static final int COURSE_SEATS = 10;
	
	enum SAT_SCALE{Geometric, Linear};
	
	static final SAT_SCALE SAT = SAT_SCALE.Linear;
	
	static final int MIN_SAT_LINEAR = 10;
	static final int MAX_SAT_LINEAR = 36;
	
	static final int MIN_SAT_GEOMETRIC = 30;
	static final int MAX_SAT_GEOMETRIC = (int) Math.pow(2,9);
	
	static final float POP_SCALE_FACTOR = 0f;

	//Simulated Annealing
	static final int LINEAR_OBJ_THRESHOLD = (NUM_PREFS * 4)-4;
	static final int GEOMETRIC_OBJ_THRESHOLD = (int)(Math.pow(2,NUM_PREFS) + Math.pow(2, NUM_PREFS-1) + Math.pow(2, NUM_PREFS-2) + Math.pow(2, NUM_PREFS-3) + Math.pow(2, NUM_PREFS-4));
}
