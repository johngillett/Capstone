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
	static HashMap<Integer,Student> studsWithAdAsSem;
	//greedy results
	static double[] prefCountReg2Greedy;
	static double[] prefCountReg3Greedy;
	static double[] prefCountSemGreedy;
	static double[] satPerGreedy;
	static int greedyTotalSat;
	
	public static void main(String[] args) {
		
		//Setup
		SetupCourses();
		GenerateStudents();
		
		//Schedule using just greedy 
		runGreedy();
		//PrintOutDataInfo();
		
		//Reschedule using simulated annealing (with greedy)
		SetupCourses();
		GenerateStudents();
		RunSchedulingProcess();

		//Results
		PrintOutDataInfo();
	}
	
	/**
	 * Runs just the greedy algorithm in order to compare results
	 * with simulated annealing
	 */
	private static void runGreedy()
	{
		AlgTracker.init();
		
		long startTime = System.currentTimeMillis();
		
		//Set to Seminar mode
		setStudentPrefsToSeminar();
		
		printSeminarInfo();
		
		//Preliminary Seminar Greedy Schedule
		GreedyScheduler.greedyScheduleByPref(students, courses,doingSeminar);
		printSeminarInfo();
		
		System.out.println("Switching to Regular Courses.");
		//Lock Current Placed courses
		lockAllStudentSeminarCourses();
		setStudentPrefsToRegular();
		
		GreedyScheduler.greedyScheduleByPref(students, courses,doingSeminar);
		
		long endTime = System.currentTimeMillis();
		
		float resTime = (float)(endTime - startTime)/1000;
		
		System.out.println("Greedy Scheduler ran in "+resTime+" seconds.");
		
		
		//store results of greedy to graph later
		double[] satCount = getLinearSatCount(students);
		satPerGreedy = getLinearSatPer(satCount);
		
		setStudentPrefsToSeminar();
		HashMap<Integer,Student> studsToCheck = getStudentsWithoutAdAsSem();
		prefCountSemGreedy = getPrefCount(studsToCheck);
		
		setStudentPrefsToRegular();
		//get students with 3 courses to schedule
		HashMap<Integer,Student> studs3 = getStudentsWith3CoursesToPlace();
		prefCountReg3Greedy = getPrefCount(studs3);
		
		//get students with 2 courses to schedule
		HashMap<Integer,Student> studs2 = getStudentsWith2CoursesToPlace();
		prefCountReg2Greedy = getPrefCount(studs2);
		
		greedyTotalSat = getTotalSatScore();
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
		courses =  CourseParser.parseCourses(courseList);
				
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
		setStudentsWithAdAsSem();
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
			double[] satPer = getLinearSatPer(satCount);
			//BarChartMaker.makeBarChartScores(satCount, doingSeminar,false);
			boolean normalize = true;
			BarChartMaker.makeBarChartScores(satPer,satPerGreedy,normalize);
		}
		
		//get seminar placements
		setStudentPrefsToSeminar();
		HashMap<Integer,Student> studsToCheck = getStudentsWithoutAdAsSem();
		double[] prefCountSem = getPrefCount(studsToCheck);
		double[] actualSemCount = getActualSemCount(studsToCheck);
		String title = "Seminar Preference Placements";
		BarChartMaker.makeBarChartSemPrefs(prefCountSem,prefCountSemGreedy,actualSemCount,title);
		
		setStudentPrefsToRegular();
		//get students with 3 courses to schedule
		HashMap<Integer,Student> studs3 = getStudentsWith3CoursesToPlace();
		double[] prefCountReg3 = getPrefCount(studs3);
		String title3 = "Regular Preference Placements for Students With 3 Courses to Schedule";
		BarChartMaker.makeBarChartPrefs(prefCountReg3,prefCountReg3Greedy,title3); 

		//get students with 2 courses to schedule
		HashMap<Integer,Student> studs2 = getStudentsWith2CoursesToPlace();
		double[] prefCountReg2 = getPrefCount(studs2);
		String title2 = "Regular Preference Placements for Students With 2 Courses to Schedule";
		BarChartMaker.makeBarChartPrefs(prefCountReg2,prefCountReg2Greedy,title2); 
		
	}
	
	/**
	 * Produces graph showing change of satisfaction score over time
	 * due to the simulated annealing/greedy approach
	 */
	static void getAlgTrackerGraph()
	{
		int[] algTrackerSimResults = AlgTracker.getSimAnnealArray();
		//BarChartMaker.makeAlgTrackerChart(algTrackerResults);
		XYChartMaker.makeAlgTrackerChart(algTrackerSimResults);
		
		//int[] algTrackerGreedResults = AlgTracker.getGreedyArray();
		//XYChartMaker.makeAlgTrackerChart(algTrackerGreedResults);
		
		
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
	
	public static int getTotalSatScore()
	{
		int score = 0;
		
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
			{
					Student stud = entry.getValue();
					score += stud.satisfactionScore;
			}
		
		//System.out.print("Initial Total score is: "+score);
		return score;
	}
	
	/**
	 * Prints out how many students are in each
	 * preference, for either regular courses or seminars
	 * @param students the students that have been placed into courses 
	 * @param doingSeminar whether we are considering seminar prefs
	 * @return
	 */
	static double[] getPrefCount(HashMap<Integer,Student> studsToCheck)
	{
		int[] prefCount; 
		if(doingSeminar)
		{
			prefCount = new int[Constants.NUM_PREFS+1];
			prefCount[0] = studsWithAdAsSem.size();

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
				for(HashMap.Entry<Integer, Student> entry : studsToCheck.entrySet()){
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
		for(int i = 0; i < prefCount.length; i++)
		{
			if(doingSeminar) normalizedPrefCount[i] = (double) prefCount[i]/students.size();
			else normalizedPrefCount[i] = (double) prefCount[i]/studsToCheck.size();
		}
		
		return normalizedPrefCount;
		
	}
	
	static double[] getActualSemCount(HashMap<Integer,Student> studsToCheck)
	{
		int[] prefCount; 
		prefCount = new int[Constants.NUM_PREFS+1];
		prefCount[0] = studsWithAdAsSem.size();

		for(int i = 1; i < prefCount.length; i++)
		{
			for(HashMap.Entry<Integer, Student> entry : studsToCheck.entrySet()){
				Integer id = entry.getKey();
				Student stu = entry.getValue();
				String prefID;

				prefID= stu.semPrefs[i-1];

				if(stu.getActualSeminar().equals(prefID))
				{
					prefCount[i] ++;
				}
			}

		}

		//normalize out of 1
		double[] normalizedPrefCount = new double[prefCount.length];
		for(int i = 0; i < prefCount.length; i++)
		{
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
	
	static double[] getLinearSatPer(double[] satCount)
	{
		double[] satPer = new double[satCount.length];
		for(int i = 0; i < satCount.length; i++)
		{
			satPer[i] = (double) satCount[i]/students.size();
		}
		return satPer;
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
	
	
	private static void setStudentsWithAdAsSem(){
		int count = 0;
		studsWithAdAsSem = new HashMap<Integer,Student>();
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
		{
				Integer id = entry.getKey();
				Student stud = entry.getValue();
				if (stud.advisingIsSeminar()) studsWithAdAsSem.put(id, stud);
		}	
	}
	
	private static HashMap<Integer,Student> getStudentsWithoutAdAsSem(){
		HashMap<Integer,Student> studs = new HashMap<Integer,Student>();
		//go through current students
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
		{
				Integer id = entry.getKey();
				Student stud = entry.getValue();
				//only consider those who didn't initially have advising as sem
				if(!studsWithAdAsSem.containsKey(id)){
					studs.put(id, stud);
				}
		}	
		return studs;
	}

	private static HashMap<Integer, Student> getStudentsWithoutAd(){
		HashMap<Integer,Student> studs = new HashMap<Integer,Student>();
		
		//get students without advising courses
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
		{
				Integer id = entry.getKey();
				Student stud = entry.getValue();
				//only consider those who dropped advising courses
				if(!stud.hasAdvisingCourse){
					studs.put(id, stud);
				}
		}
		return studs;
	}
	private static HashMap<Integer, Student> getStudentsWith3CoursesToPlace(){
		
		//first get students without advising courses
		HashMap<Integer,Student> studs = getStudentsWithoutAd();

		//add onto students who had advising as seminar
		for(HashMap.Entry<Integer, Student> entry : studsWithAdAsSem.entrySet()){
			Integer id = entry.getKey();
			Student stud = entry.getValue();
			studs.put(id, stud);
		}
		
		return studs;
	}
	
	private static HashMap<Integer, Student> getStudentsWith2CoursesToPlace(){
		HashMap<Integer,Student> studs = new HashMap<Integer,Student>();
		HashMap<Integer,Student> studsToIgnore = getStudentsWith3CoursesToPlace();
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
		{
			Integer id = entry.getKey();
			Student stud = entry.getValue();
			if(!studsToIgnore.containsKey(id)) studs.put(id, stud);
		}
		
		return studs;
	}
	
}
