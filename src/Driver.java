import java.util.ArrayList;
import java.util.HashMap;

	public class Driver
	{
		
	static ArrayList<Course> courseList;
	static HashMap<String,ArrayList<Course>> courses;
	static ArrayList<Student> students;	
	static HashMap<String, Integer> courseCounts;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		courseList = new ArrayList<Course>();
		
		courses =  CourseParser.parseCourses(courseList,Constants.TOT_COURSES);
		courseCounts = CourseParser.updateEnrollmentTotals(courses);
		//printCourseCounts();
		
		//students = StudentGenerator.generateStudents(Constants.NUM_STUDENTS, courseList);
		
		//GreedyScheduler.greedyScheduleByStudent(students, courses);
		//GreedyScheduler.greedyScheduleByPref(students, courses);
		//GreedyScheduler.greedyScheduleByPrefRandomized(students, courses);
		
		
		//printStudents();
		//printCourses();
		
		//int[] prefCount = getPrefCount(students);
		//int[] satCount = getLinearSatCount(students);
		
		//BarChartMaker.makeBarChartPrefs(prefCount); 
		//BarChartMaker.makeBarChartScores(satCount);
		HashMap<Integer, Student> students = new HashMap<Integer,Student>();
		students = FreshmanParser.parseFreshmen();
		
	
		printCourses();
		
	}

	static void printStudents()
	{
		
		double avgScore = 0;
		
		for(Student stud : students)
		{
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
	
	static void printCourseCounts()
	{
		for(HashMap.Entry<String, Integer> c : courseCounts.entrySet()){
			String id = c.getKey();
			int count = c.getValue();
			System.out.println("Course "+ id + " has "+ count + " freshmen");
		}		
		
	}
	
	static int[] getPrefCount(ArrayList<Student> students)
	{
		int[] prefCount = new int[Constants.NUM_PREFS];
		
		for(int i = 0; i <prefCount.length; i++)
		{
			for(Student stu : students)
			{
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
			System.out.println("There are " + prefCount[i] + " students in their preference " + i + " class.");
		}
		
		return prefCount;
		
	}
	
	static int[] getLinearSatCount(ArrayList<Student> students)
	{
		int[] satCount = new int[Constants.MAX_SAT_LINEAR-Constants.MIN_SAT_LINEAR];
		

			for(Student stu : students)
			{
				satCount[(int) (stu.satisfactionScore - Constants.MIN_SAT_LINEAR)] += 1;
			}
		
		return satCount;
		
	}
	
	
}
