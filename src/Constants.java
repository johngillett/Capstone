
/**
 * A class that holds all constant values that
 * are referenced throughout the program
 * 
 * @author Anna Dovzhik & John Gillett
 *
 */
public class Constants 
{

	static final int NUM_STUDENTS = 652;
	
	static final boolean PARSE_ALL_COURSES = true;
	
	static final int TOT_COURSES = 40;
	
	static final int STUD_COURSE_LIMIT = 4;
	
	static final int STUD_SEM_LIMIT = 1;
	
	static final int NUM_PREFS = 6;
	
	static final String NULL_PREF = "NULL";
	
	static final int COURSE_SEATS = 10;
	
	enum SAT_SCALE{Geometric, Linear};
	
	static final SAT_SCALE SAT = SAT_SCALE.Linear;
	
	//has advising, 1st pref seminar + 1st pref regular + 2nd pref regular
	static final int MIN_SAT_LINEAR = 4;
	static final int MAX_SAT_LINEAR = NUM_PREFS * STUD_COURSE_LIMIT;
	
	static final int MIN_SAT_GEOMETRIC = 30;
	static final int MAX_SAT_GEOMETRIC = (int) Math.pow(2,9);
	
	static final float POP_SCALE_FACTOR = 0f;
	
	//Algorithm Tracker
	static final int TRACK_FIDELITY = 100;

	//Simulated Annealing
	//static final int LINEAR_OBJ_THRESHOLD = (int) (((NUM_PREFS * 4)-4) * ((double)NUM_STUDENTS/2.5f)); // multiplied by # students
	static final int LINEAR_OBJ_THRESHOLD = 2608;
	
	static final int GEOMETRIC_OBJ_THRESHOLD = (int)((Math.pow(2,NUM_PREFS) + Math.pow(2, NUM_PREFS-1) + Math.pow(2, NUM_PREFS-2) + Math.pow(2, NUM_PREFS-3) + Math.pow(2, NUM_PREFS-4))* ((double)NUM_STUDENTS/8.5f)); 
	
	static final float INIT_TEMP_VAL = 1f;
	static final float TEMP_SCALE_FACTOR = .9f;

	static final float MIN_TEMP = 0.075f;

	static final int ITERS_BEFORE_TEMP_SCALE = 1000;
	
}
