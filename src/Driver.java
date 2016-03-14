import java.util.ArrayList;
import java.util.HashMap;

	public class Driver
	{
		
	static ArrayList<Course> courseList;
	static HashMap<String,ArrayList<Course>> courses;
	//static ArrayList<Student> students;	
	static HashMap<Integer, Student> students;
	static HashMap<String, Integer> freshmenCourseCounts;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		 * Hey Anna! Hope you're having a good break.
		 * 
		 * Things I've changed:
		 * -Added the temperature probability to the SimAnneal Scheduler. Check the makeChangeAnyway() function for more comments.
		 * -Added the Solution class. This is now what the scheduler returns. I've made some comments there with info as well. 
		 * -Added a few new constants. I think they should be clear as to what they do.
		 * -After implementing the above, the scheduler consistently had stack overflow errors, so I re-wrote it all non-recursively! 
		 * 		I tried to model it as closely to the pseudo-code we showed in our presentation. 
		 * 
		 * Issues:
		 * -Currently the scheduler makes many many many repeated changes with a net change of 0. I'm not sure how we should handle this.
		 * -Occasionally a replacement is made and stud1 is unable to be added into the new course. This shouldn't be possible, yet its happening. 
		 * 
		 * Notes:
		 * -Its hard to tell but it seems the algorithm works a little better when using the Geometric scale. 
		 * 
		 * Text me any time if you have questions about the changes I made! 
		 * 
		 */
		
		courseList = new ArrayList<Course>();
		
		//Generate Courses
		courses =  CourseParser.parseCourses(courseList,Constants.TOT_COURSES);
		freshmenCourseCounts = CourseParser.updateEnrollmentTotals(courses);
		
		//Generate students
		students = FreshmanParser.parseFreshmen();
		
		//Generate Preferences
		PreferenceGenerator.generatePopPrefs(freshmenCourseCounts, students);
		//PreferenceGenerator.generateRanPrefs(students, courseList);
				
		//Preliminary Greedy Schedule
		GreedyScheduler.greedyScheduleByPref(students, courses);
		//GreedyScheduler.greedyScheduleByStudent(students, courses);
		//GreedyScheduler.greedyScheduleByPrefRandomized(students, courses);
		//System.out.println(courseList.get(0).curSize);
				
		//printCourses();
		printCourseData();
		int startingScore = SimAnnealingScheduler.getTotalSatScore(students);
		
		if(Constants.SAT == Constants.SAT_SCALE.Linear)
		System.out.println("Starting with a score of "+startingScore+", aiming for "+Constants.LINEAR_OBJ_THRESHOLD);
		else
		System.out.println("Starting with a score of "+startingScore+", aiming for "+Constants.GEOMETRIC_OBJ_THRESHOLD);
			
		//printCourseData();
		
		//Simulated Annealing:
		Solution sol = SimAnnealingScheduler.Schedule(students, courses);
		
		System.out.println("Started with: "+startingScore+", ended up with "+sol.getScore());
	
		printCourseData();
		
		//getGraphs();
	
		//printCourses();
		//printFreshmenCourseCountTotal();
	}

	static void getGraphs()
	{
		if(Constants.SAT == Constants.SAT_SCALE.Linear)
		{
			int[] satCount = getLinearSatCount(students);
			BarChartMaker.makeBarChartScores(satCount);
		}
		int[] prefCount = getPrefCount(students);
		BarChartMaker.makeBarChartPrefs(prefCount); 
		
	}
	
	static void printFreshmenCourseCountTotal()
	{
		int totalCount = 0;
		for(HashMap.Entry<String, Integer> c : freshmenCourseCounts.entrySet()){
			int count = c.getValue();
			totalCount += count;
		}	
		System.out.println("The total number is" + totalCount);
	}
	
	//doesn't work because assumes students are an arrayList
	static void printStudents()
	{
		
		double avgScore = 0;
		
		for(HashMap.Entry<Integer, Student> c : students.entrySet()){
			Student stud = c.getValue();
			System.out.println(stud.toString());
			avgScore += stud.satisfactionScore;
		}
		
		avgScore = avgScore / students.size();
		
		System.out.println("Average Score: "+avgScore);
			
	}
	
	
	static void printCourses()
	{
		for(Course cour : courseList)
		{
			System.out.println(cour.toString());
			
			if(cour.hasLab)
				cour.printLabs();
		}	
		
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
	
	static int[] getPrefCount(HashMap<Integer,Student> students)
	{
		int[] prefCount = new int[Constants.NUM_PREFS];
		
		for(int i = 0; i <prefCount.length; i++)
		{
			for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
				Student stu = entry.getValue();
				String prefID = stu.prefs[i];
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
	
	
}
