import java.util.ArrayList;
import java.util.HashMap;
	/**
	 * This class loads in Fall 2015 course data,
	 * generates students and their preferences,
	 * runs our scheduling algorithms, and displays
	 * the results.
	 * 
	 * @author Anna Dovzhik & John Gillett
	 * @version 4.8.2016
	 * 
	 */
	public class Driver
	{
		
	static ArrayList<Course> courseList;
	static HashMap<String,ArrayList<Course>> courses;
	static ArrayList<Course> advisingCourses;
	static ArrayList<String> seminarCourses;
	static ArrayList<String> regularCourses;
	static HashMap<Integer, Student> students;
	static HashMap<String, Integer> freshmenCourseCounts;
	static boolean doingSeminar;
	static ArrayList<Integer> studsWithAdAsSem;
	
	public static void main(String[] args) {
		
		//Setup
		SetupCourses();
		GenerateStudents();
		
		//Schedule
		RunSchedulingProcess();

		//Results
		PrintOutDataInfo();
	}
	

	/**
	 * Schedules students into their seminar preferences
	 * and then schedules students into their remaining courses
	 */
	private static void RunSchedulingProcess()
	{
		AlgTracker.init();
		
		long startTime = System.currentTimeMillis();
		
		//Set to Seminar mode
		setStudentPrefsToSeminar();
		
		printSeminarInfo();
		
		//Preliminary Seminar Greedy Schedule
		GreedyScheduler.greedyScheduleByPref(students, courses,doingSeminar);
		printSeminarInfo();
		
		//Simulated Annealing:
		Solution sol = SimAnnealingScheduler.ScheduleUnbiased(students, courses);
		
		printSeminarInfo();
		
		System.out.println("Switching to Regular Courses.");
		//Lock Current Placed courses
		lockAllStudentSeminarCourses();
		setStudentPrefsToRegular();
		
		GreedyScheduler.greedyScheduleByPref(students, courses,doingSeminar);
			
		sol = SimAnnealingScheduler.ScheduleUnbiased(students, courses);
		
		long endTime = System.currentTimeMillis();
		
		float resTime = (float)(endTime - startTime)/1000;
		
		System.out.println("Scheduler ran in "+resTime+" seconds.");
		
		
	}
	
	/**
	 * Prints out results of the scheduling process
	 */
	private static void PrintOutDataInfo()
	{

		printSeminarInfo();
		
		printCourseData();
		
		getGraphs();
		getAlgTrackerGraph();
		
		printStudents();
		//printCourses();
		//printFreshmenCourseCountTotal();
		
		printSatisfactionScoreInfo();

	}
	
	/**
	 * Reads course file and extracts relevant info about courses
	 */
	private static void SetupCourses()
	{
		courseList = new ArrayList<Course>();
		
		//Generate Courses
		courses =  CourseParser.parseCourses(courseList,Constants.TOT_COURSES);
				
		seperateCourses();
				
		freshmenCourseCounts = CourseParser.updateEnrollmentTotals(courses);
		//printCourseCounts();
				
		setAdvisingCourses();	
		
	}
	
	/**
	 * Creates students (either randomly or through actual data)
	 * Generates students' preferences, or loads standard preferences
	 */
	private static void GenerateStudents()
	{
		//Generate students
		students = FreshmanParser.parseFreshmen(courses);
		//students = StudentGenerator.generateStudents(advisingCourses);
		
		
		//Generate Seminar Preferences
		setStudentPrefsToSeminar();
		//PreferenceGenerator.getRealSemPrefs(students, seminarCourses);
		//PreferenceGenerator.generatePopPrefs(freshmenCourseCounts, students,doingSeminar);
		//PreferenceGenerator.generateRanPrefs(students, seminarCourses, doingSeminar);
		PreferenceGenerator.getStandardPrefs(students);
		
		//Generate Regular Preferences
		setStudentPrefsToRegular();
		//PreferenceGenerator.generatePopPrefs(freshmenCourseCounts, students,doingSeminar);	
		//PreferenceGenerator.generateRanPrefs(students, regularCourses, doingSeminar);
		PreferenceGenerator.getStandardPrefs(students);

		//Find out how many students initially have advising as seminar
		studsWithAdAsSem = getStudentsWithAdAsSem();
		System.out.println("The number of students with advising as seminar: " + studsWithAdAsSem.size());

	}
	
	/**
	 * Determines all courses that are advising courses
	 */
	static void setAdvisingCourses()
	{
		advisingCourses = new ArrayList<Course>(); 
		for(HashMap.Entry<String, ArrayList<Course>> entry : courses.entrySet()){
			ArrayList<Course> sections = entry.getValue();
			for(Course section : sections){
				if(section.isAdvising){
					advisingCourses.add(section);
					//System.out.println(section);
				}
			}
		}
			
	}

	/**
	 * Produces graphs displaying range of satisfaction scores
	 * and preference placements
	 */
	static void getGraphs()
	{
		if(Constants.SAT == Constants.SAT_SCALE.Linear)
		{
			double[] satCount = getLinearSatCount(students);
			BarChartMaker.makeBarChartScores(satCount, doingSeminar);
		}
		
		setStudentPrefsToSeminar();
		double[] prefCount = getPrefCount();
		BarChartMaker.makeBarChartPrefs(prefCount,doingSeminar);
		
		setStudentPrefsToRegular();
		prefCount = getPrefCount();
		BarChartMaker.makeBarChartPrefs(prefCount,doingSeminar); 
		
		
	}
	
	/**
	 * Produces graph showing change of satisfaction score over time
	 * due to the simulated annealing/greedy approach
	 */
	static void getAlgTrackerGraph()
	{
		int[] algTrackerResults = AlgTracker.getArray();
		//BarChartMaker.makeAlgTrackerChart(algTrackerResults);
		XYChartMaker.makeAlgTrackerChart(algTrackerResults);
	}
	
	/**
	 * Prints out the number of freshmen counted in the 
	 * HashMap where (k -> v) is (course -> num of freshmen)
	 */
	static void printFreshmenCourseCountTotal()
	{
		int totalCount = 0;
		for(HashMap.Entry<String, Integer> c : freshmenCourseCounts.entrySet()){
			int count = c.getValue();
			totalCount += count;
		}	
		//System.out.println("The total number is " + totalCount);
	}
	
	/**
	 * Prints average satisfaction score and number of students without a full course load
	 * Can also print each student's info
	 */
	static void printStudents()
	{	
		double avgScore = 0;
		
		int numStudsWithoutFullCourseLoad = 0;

		//System.out.println("Students without full course load: ");
		for(HashMap.Entry<Integer, Student> c : students.entrySet()){
			Student stud = c.getValue();
			
			if(stud.getClassCount() < Constants.STUD_COURSE_LIMIT)
			{	
				numStudsWithoutFullCourseLoad++;
			}
			
			//System.out.println(stud.toString());
			avgScore += stud.satisfactionScore;
		}
		
		avgScore = avgScore / students.size();
		
		System.out.println("Number of students without full course load: "+numStudsWithoutFullCourseLoad);
		System.out.println("Average Score: "+avgScore);
			
	}
	
	/**
	 * Displays the number of students that have a seminar course
	 */
	static void printSeminarInfo()
	{
		
		int count = 0;
		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			Student stu = entry.getValue();
			if(stu.hasSeminarCourse())
				count++;
		}
		
		System.out.println("Number of students in a seminar course: "+count);
	}
	
	
	/**
	 * Prints every course
	 */
	static void printCourses()
	{
		for(HashMap.Entry<String, ArrayList<Course>> entry : courses.entrySet()){
			ArrayList<Course> sections = entry.getValue();
			
			for(Course section : sections)
				System.out.println(section);
		}	
	}
	
	/**
	 * Prints only courses that are seminars
	 */
	static void printSeminarCourses()
	{
		for(HashMap.Entry<String, ArrayList<Course>> entry : courses.entrySet()){
			ArrayList<Course> sections = entry.getValue();
			
			if(!sections.get(0).isSeminar)
				continue;
			
			for(Course section : sections)
				System.out.println(section);
		}	
	}
	
	/**
	 * Prints only courses that are advising courses
	 */
	static void printAdvisingCourses()
	{
		for(Course c: advisingCourses){
			System.out.println(c);
		}
	}
	
	/**
	 * Prints out how many students have all locked courses
	 */
	static void printAdvisingInfo()
	{
		int count = 0;
		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			Student stu = entry.getValue();
			if(stu.getClassCount() == stu.lockedCourses.size())
				count++;
		}
		
		System.out.println("Number of students with all locked courses: "+count);
		
		
	}
	
	/**
	 * Prints out the total number of freshmen enrolled in
	 * the courses. Should be around 652*4, not counting labs
	 */
	static void printCourseData()
	{
		int totStudents = 0;
		for(HashMap.Entry<String, ArrayList<Course>> entry : courses.entrySet()){
			ArrayList<Course> sections = entry.getValue();
			
			for(Course section : sections)
				totStudents += section.curSize;
			
		}
		
		System.out.println("Found "+totStudents+" in courses");
		
	}
	
	/**
	 * Prints how many freshmen are in each course
	 */
	static void printCourseCounts()
	{
		for(HashMap.Entry<String, Integer> c : freshmenCourseCounts.entrySet()){
			String id = c.getKey();
			int count = c.getValue();
			System.out.println("Course "+ id + " has "+ count + " freshmen");
		}		
		
	}
	
	/**
	 * Prints out how many students are in each
	 * preference, for either regular courses or seminars
	 * @param students the students that have been placed into courses 
	 * @param doingSeminar whether we are considering seminar prefs
	 * @return
	 */
	static double[] getPrefCount()
	{
		int[] prefCount; 
		if(doingSeminar)
		{
			prefCount = new int[Constants.NUM_PREFS+1];
			prefCount[0] = studsWithAdAsSem.size();
			
			HashMap<Integer,Student> studsToCheck = getStudentsWithoutAdAsSem();

			for(int i = 1; i < prefCount.length; i++)
			{
				for(HashMap.Entry<Integer, Student> entry : studsToCheck.entrySet()){
					Integer id = entry.getKey();
					Student stu = entry.getValue();
					String prefID;

					prefID= stu.semPrefs[i-1];

					if(stu.hasCourse(prefID))
					{
						prefCount[i] ++;
					}
				}

			}
			
			//debugging
//			int sum = 0;
//			for(int i = 0; i < prefCount.length; i++){
//				sum += prefCount[i];
//			}
//			System.out.println("Total number of students is " + students.size() +
//					", Total number of those in seminars is " + sum);
		}

		else{
			prefCount = new int[Constants.NUM_PREFS];

			for(int i = 0; i < prefCount.length; i++)
			{
				for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
					Student stu = entry.getValue();

					String prefID;

					prefID = stu.regPrefs[i];

					if(stu.hasCourse(prefID))
					{
						prefCount[i]  += 1;
					}
				}

			}
		}

		//Optional printing of info
		for(int i = 0; i <prefCount.length; i++)
		{
			System.out.println("There are " + prefCount[i] + " students in their preference " + (i+1) + " class.");
		}
		
		//normalize out of 1
		double[] normalizedPrefCount = new double[prefCount.length];
		for(int i = 0; i < prefCount.length; i++){
			normalizedPrefCount[i] = (double) prefCount[i]/students.size();
		}
		
		return normalizedPrefCount;
		
	}
	
	/**
	 * Counts how many students have each possible linear satisfaction score
	 * 
	 * @param students the students that have been placed into courses
	 * @return an array where each index holds the corresponding number of students with that score
	 */
	static double[] getLinearSatCount(HashMap<Integer,Student> students)
	{
		double[] satCount = new double[Constants.MAX_SAT_LINEAR-Constants.MIN_SAT_LINEAR];
		

		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			Student stu = entry.getValue();
				satCount[(int) (stu.satisfactionScore - Constants.MIN_SAT_LINEAR)] += 1;
			}
		
		return satCount;
		
	}
	
	/**
	 * Goes through the courses and places them 
	 * either into the seminarCourses ArrayList or regularCourses ArrayList
	 */
	static void seperateCourses()
	{
		seminarCourses = new ArrayList<String>();
		regularCourses = new ArrayList<String>();
		
		for(HashMap.Entry<String, ArrayList<Course>> entry : courses.entrySet()){
			ArrayList<Course>cs = entry.getValue();
			
			if(cs.get(0).isSeminar()){
				seminarCourses.add(cs.get(0).getID());
				//System.out.println(cs.get(0).getID());
			}
				
			else
				regularCourses.add(cs.get(0).getID());
		}
	}
	
	/**
	 * Sets the current prefs we're considering to seminars
	 */
	static void setStudentPrefsToSeminar()
	{
		doingSeminar = true;
		
		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			Student stu = entry.getValue();
			stu.prefs = stu.semPrefs;
		}
	}
	
	/**
	 * Sets the current prefs we're considering to regular courses
	 */
	static void setStudentPrefsToRegular()
	{
		doingSeminar = false;
		
		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			Student stu = entry.getValue();
			stu.prefs = stu.regPrefs;
		}
	}

	
	/**
	 * Iterates through all students and locks their seminar course
	 * @param inStudents the students that have seminar courses
	 */
	private static void lockAllStudentSeminarCourses()
	{
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
		{
				Student stud = entry.getValue();
				
				for(Course c : stud.courses)
				{
					if(c.isSeminar())
					{
						stud.lockCourse(c.getID());
						break;
					}
				}
				
		}	
	}
	
	/**
	 * Helper method to check that we know the actual
	 * total satisfaction score
	 */
	private static void printSatisfactionScoreInfo()
	{
		int score = 0;
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
		{
				Student stud = entry.getValue();
				score += stud.satisfactionScore;	
		}		
		
		System.out.println("Current Actual Satisfaction Score: "+score);
	}
	
	
	private static ArrayList<Integer> getStudentsWithAdAsSem(){
		int count = 0;
		ArrayList<Integer> studs = new ArrayList<Integer>();
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
		{
				Integer id = entry.getKey();
				Student stud = entry.getValue();
				if (stud.advisingIsSeminar()) studs.add(id);
		}	
		return studs;
	}
	
	private static HashMap<Integer,Student> getStudentsWithoutAdAsSem(){
		HashMap<Integer,Student> studs = new HashMap<Integer,Student>();
		//go through current students
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
		{
				Integer id = entry.getKey();
				Student stud = entry.getValue();
				//only consider those who didn't initally have advising as sem
				if(!studsWithAdAsSem.contains(id)){
					studs.put(id, stud);
				}
		}	
		return studs;
	}

	
}
