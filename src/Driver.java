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
		
		courseList = new ArrayList<Course>();
		
		courses =  CourseParser.parseCourses(courseList,Constants.TOT_COURSES);
		freshmenCourseCounts = CourseParser.updateEnrollmentTotals(courses);
		
		students = FreshmanParser.parseFreshmen();
		
		PreferenceGenerator.generatePopPrefs(freshmenCourseCounts, students);
		//PreferenceGenerator.generateRanPrefs(students, courseList);
		
		GreedyScheduler.greedyScheduleByPref(students, courses);
		
		System.out.println(students.get(5075409).toString());
		String[] prefs = students.get(5075409).getRemainingCompPrefs(courses);

		for(String s : prefs)
			System.out.print(s+",");
		
		
		//Simulated Annealing:
		//SimAnnealingScheduler.Schedule(students, courses);
		
		
		
		
		//printCourseCounts();
		//System.out.println("Number of chem students is " + freshmenCourseCounts.get("CHEM110"));
		
		//students = StudentGenerator.generateStudents(Constants.NUM_STUDENTS, courseList);
		
		//GreedyScheduler.greedyScheduleByStudent(students, courses);
		//GreedyScheduler.greedyScheduleByPref(students, courses);
		//GreedyScheduler.greedyScheduleByPrefRandomized(students, courses);
		//System.out.println(courseList.get(0).curSize);
		
		//printStudents();
		//printCourses();
		
		//int[] prefCount = getPrefCount(students);
		//int[] satCount = getLinearSatCount(students);
		
		//BarChartMaker.makeBarChartPrefs(prefCount); 
		//BarChartMaker.makeBarChartScores(satCount);
		
	
		//printCourses();
		//printFreshmenCourseCountTotal();
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
//	static void printStudents()
//	{
//		
//		double avgScore = 0;
//		
//		for(Student stud : students)
//		{
//			System.out.println(stud.toString());
//			avgScore += stud.satisfactionScore;
//		}
//		
//		avgScore = avgScore / students.size();
//		
//		System.out.println("Average Score: "+avgScore);
//			
//	}
	
	
	static void printCourses()
	{
		for(Course cour : courseList)
		{
			System.out.println(cour.toString());
			
			if(cour.hasLab)
				cour.printLabs();
		}	
		
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
