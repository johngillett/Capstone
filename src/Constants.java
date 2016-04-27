
/**
 * A class that holds all constant values that
 * are referenced throughout the program
 * 
 * @author Anna Dovzhik & John Gillett
 *
 */
public class Constants 
{

	//Basic info
	static final int NUM_STUDENTS = 646;
	
	static final int NUM_STUDENTS_AD_AS_SEM = 172;
		
	static final int NUM_STUDENTS_NO_ADVISING = 4;
	
	static final int STUD_COURSE_LIMIT = 4;
	
	static final int STUD_SEM_LIMIT = 1;
	
	static final int NUM_PREFS = 6;

	static final String NULL_PREF = "NULL";
	
	
	//Scaling of Scores
	enum SAT_SCALE{Geometric, Linear};
	static final SAT_SCALE SAT = SAT_SCALE.Linear;
	
	//REGULAR COURSES
	//Minimum possible scores a student can have
	static final int MIN_SAT_LINEAR = 1 + 1 + 2;     //has advising, 1st pref seminar + 1st pref regular + 2nd pref regular
	static final int MIN_SAT_LINEAR_ADVISING = 1 + 2 + 3; //has advising as seminar
	
	//Maximum possible scores a student can have
	static final int MAX_SAT_LINEAR = (NUM_PREFS+1) * STUD_COURSE_LIMIT-1;    //has advising course, but that's it 
	static final int MAX_SAT_LINEAR_ADVISING = (NUM_PREFS+1) * STUD_COURSE_LIMIT-1; //has advising as seminar, but that's it
	static final int MAX_SAT_LINEAR_NOADVISING = (NUM_PREFS+1) * STUD_COURSE_LIMIT; //doesn't have advising course either
	
	//SEMINARS
	//Minimum possible scores a student can have
	static final int MIN_SAT_LINEAR_SEM = 1;   //get their top pick seminar
	static final int MIN_SAT_LINEAR_SEM_ADVISING = 0; //advising is seminar
	
	//Maximum possible scores a student can have
	static final int MAX_SAT_LINEAR_SEM = NUM_PREFS+1; //don't get a seminar
	static final int MAX_SAT_LINEAR_SEM_ADVISING = 0;  //advising is seminar
	
	static final int TOT_SAT_TO_SEM_MOD = (NUM_STUDENTS_AD_AS_SEM *(3*(NUM_PREFS+1))) + ((NUM_STUDENTS -NUM_STUDENTS_AD_AS_SEM)* (2*(NUM_PREFS+1)));
	
	//Geometric scaling
	static final int MIN_SAT_GEOMETRIC = 2 + 2 + 4;
	static final int MIN_SAT_GEOMETRIC_ADVISING = 2 + 4 + 8;
	static final int MAX_SAT_GEOMETRIC = (int) Math.pow(2,(NUM_PREFS+1))*STUD_COURSE_LIMIT;
	
	
	static final float POP_SCALE_FACTOR = 0f;
	
	//Algorithm Tracker
	static final int TRACK_FIDELITY = 1;

	//Min & max total possible scores
	//static final int LINEAR_OBJ_THRESHOLD = (int) (((NUM_PREFS * 4)-4) * ((double)NUM_STUDENTS/2.5f)); // multiplied by # students
	static final int LINEAR_OBJ_THRESHOLD = NUM_STUDENTS_AD_AS_SEM*MIN_SAT_LINEAR_ADVISING+ (NUM_STUDENTS -NUM_STUDENTS_AD_AS_SEM)*MIN_SAT_LINEAR;
	
	static final int LINEAR_OBJ_SEM_THRESHOLD = NUM_STUDENTS_AD_AS_SEM*MIN_SAT_LINEAR_SEM_ADVISING + (NUM_STUDENTS - NUM_STUDENTS_AD_AS_SEM) * MIN_SAT_LINEAR_SEM;
	
	static final int GEOMETRIC_OBJ_THRESHOLD = NUM_STUDENTS_AD_AS_SEM*MIN_SAT_GEOMETRIC_ADVISING+ (NUM_STUDENTS -NUM_STUDENTS_AD_AS_SEM)*MIN_SAT_GEOMETRIC;
	
	static final int LINEAR_OBJ_MAX_THRESHOLD = NUM_STUDENTS_AD_AS_SEM*MAX_SAT_LINEAR_ADVISING+ (NUM_STUDENTS -NUM_STUDENTS_AD_AS_SEM-NUM_STUDENTS_NO_ADVISING)*MAX_SAT_LINEAR + (NUM_STUDENTS_NO_ADVISING * MAX_SAT_LINEAR_NOADVISING);
	
	//TO CALCULATE
	//static final int LINEAR_OBJ_SEM_MAX_THRESHOLD = NUM_STUDENTS_AD_AS_SEM*MAX_SAT_LINEAR_SEM_ADVISING + (NUM_STUDENTS - NUM_STUDENTS_AD_AS_SEM) * MIN_SAT_LINEAR_SEM;

	//Simulated Annealing
	static final float INIT_TEMP_VAL = 1f;
	static final float TEMP_SCALE_FACTOR = .9f;

	static final float MIN_TEMP = 0.075f;

	static final int ITERS_BEFORE_TEMP_SCALE = 1000;
	
}
