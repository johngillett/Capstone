import java.util.ArrayList;
import java.util.HashMap;

	public class Driver
	{
		
	static ArrayList<Course> courseList;
	static HashMap<String,ArrayList<Course>> courses;
	static ArrayList<Course> advisingCourses;
	static ArrayList<String> seminarcourses;
	static ArrayList<String> regularcourses;
	static HashMap<Integer, Student> students;
	static HashMap<String, Integer> freshmenCourseCounts;
	
	static boolean doingSeminar;
	
	public static void main(String[] args) {
		
		//Setup
		SetupCourses();
		GenerateStudents();
		
		//Schedule
		RunSchedulingProcess();

		//Results
		PrintOutDataInfo();
	}
	
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
		lockAllStudentSeminarCourses(students);
		setStudentPrefsToRegular();
		
		GreedyScheduler.greedyScheduleByPref(students, courses,doingSeminar);
				
		sol = SimAnnealingScheduler.ScheduleUnbiased(students, courses);
		
		long endTime = System.currentTimeMillis();
		
		float resTime = (float)(endTime - startTime)/1000;
		
		System.out.println("Scheduler ran in "+resTime+" seconds.");
		
		
	}
	
	private static void PrintOutDataInfo()
	{

		printSeminarInfo();
		
		printCourseData();
		
		//getGraphs();
		getAlgTrackerGraph();
		
		//printStudents();
		//printCourses();
		//printFreshmenCourseCountTotal();
		
		printSatisfactionScoreInfo();

	}
	
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
	
	private static void GenerateStudents()
	{
		//Generate students
		//students = FreshmanParser.parseFreshmen(courses);
		students = StudentGenerator.generateStudents(advisingCourses);
			
		//Generate Seminar Preferences
		setStudentPrefsToSeminar();
		PreferenceGenerator.generatePopPrefs(freshmenCourseCounts, students,doingSeminar);
		//PreferenceGenerator.generateRanPrefs(students, seminarcourses, doingSeminar);
				
		//Generate Regular Preferences
		setStudentPrefsToRegular();
		PreferenceGenerator.generatePopPrefs(freshmenCourseCounts, students,doingSeminar);	
		//PreferenceGenerator.generateRanPrefs(students, regularcourses, doingSeminar);
				
		//PreferenceGenerator.getStandardPrefs(students);
		//PreferenceGenerator.generateRanPrefs(students, courseList);

	}
	
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

	static void getGraphs()
	{
		if(Constants.SAT == Constants.SAT_SCALE.Linear)
		{
			int[] satCount = getLinearSatCount(students);
			BarChartMaker.makeBarChartScores(satCount);
		}
		int[] prefCount = getPrefCount(students,true);
		BarChartMaker.makeBarChartPrefs(prefCount,true);
		
		prefCount = getPrefCount(students,false);
		BarChartMaker.makeBarChartPrefs(prefCount,false); 
		
		
	}
	
	static void getAlgTrackerGraph()
	{
		int[] algTrackerResults = AlgTracker.getArray();
		BarChartMaker.makeAlgTrackerChart(algTrackerResults);
		XYChartMaker.makeAlgTrackerChart(algTrackerResults);
	}
	
	static void printFreshmenCourseCountTotal()
	{
		int totalCount = 0;
		for(HashMap.Entry<String, Integer> c : freshmenCourseCounts.entrySet()){
			int count = c.getValue();
			totalCount += count;
		}	
		//System.out.println("The total number is " + totalCount);
	}
	
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
			
			System.out.println(stud.toString());
			avgScore += stud.satisfactionScore;
		}
		
		avgScore = avgScore / students.size();
		
		System.out.println("Number of students without full course load: "+numStudsWithoutFullCourseLoad);
		System.out.println("Average Score: "+avgScore);
			
	}
	
	static void printSeminarInfo()
	{
		
		int count = 0;
		int lockedCount;
		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			Student stu = entry.getValue();
			if(stu.hasSeminarCourse())
				count++;
		}
		
		System.out.println("Number of students in a seminar course: "+count);
	}
	
	
	static void printCourses()
	{
		for(HashMap.Entry<String, ArrayList<Course>> entry : courses.entrySet()){
			ArrayList<Course> sections = entry.getValue();
			
			for(Course section : sections)
				System.out.println(section);
		}	
	}
	
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
	
	static void printAdvisingCourses()
	{
		for(Course c: advisingCourses){
			System.out.println(c);
		}
	}
	
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
	
	static void printCourseCounts()
	{
		for(HashMap.Entry<String, Integer> c : freshmenCourseCounts.entrySet()){
			String id = c.getKey();
			int count = c.getValue();
			System.out.println("Course "+ id + " has "+ count + " freshmen");
		}		
		
	}
	
	static int[] getPrefCount(HashMap<Integer,Student> students,boolean doingSeminar)
	{
		int[] prefCount = new int[Constants.NUM_PREFS];
		
		for(int i = 0; i <prefCount.length; i++)
		{
			for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
				Student stu = entry.getValue();
				
				String prefID;
				
				if(doingSeminar)
				prefID= stu.semPrefs[i];
				else
				prefID = stu.regPrefs[i];
				
				if(stu.hasCourse(prefID))
					{
						prefCount[i]  += 1;
					}
			}
				
		}
		
		//Optional printing of info
		for(int i = 0; i <prefCount.length; i++)
		{
			System.out.println("There are " + prefCount[i] + " students in their preference " + (i+1) + " class.");
		}
		
		return prefCount;
		
	}
	
	static int[] getLinearSatCount(HashMap<Integer,Student> students)
	{
		int[] satCount = new int[Constants.MAX_SAT_LINEAR-Constants.MIN_SAT_LINEAR];
		

		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			Student stu = entry.getValue();
				satCount[(int) (stu.satisfactionScore - Constants.MIN_SAT_LINEAR)] += 1;
			}
		
		return satCount;
		
	}
	
	static void seperateCourses()
	{
		seminarcourses = new ArrayList<String>();
		regularcourses = new ArrayList<String>();
		
		for(HashMap.Entry<String, ArrayList<Course>> entry : courses.entrySet()){
			ArrayList<Course>cs = entry.getValue();
			
			if(cs.get(0).isSeminar())
				seminarcourses.add(cs.get(0).getID());
			else
				regularcourses.add(cs.get(0).getID());
		}
	}
	
	static void setStudentPrefsToSeminar()
	{
		doingSeminar = true;
		
		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			Student stu = entry.getValue();
			stu.prefs = stu.semPrefs;
		}
	}
	
	static void setStudentPrefsToRegular()
	{
		doingSeminar = false;
		
		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			Student stu = entry.getValue();
			stu.prefs = stu.regPrefs;
		}
	}
	
	static void lockAllStudentCourses()
	{

		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
				Student stu = entry.getValue();
				stu.lockCourses();
		}
		
	}
	
	private static void lockAllStudentSeminarCourses(HashMap<Integer,Student> inStudents)
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
	
	
}
