import java.util.ArrayList;
import java.util.HashMap;

	public class Driver
	{
		
	static ArrayList<Course> courseList;
	static HashMap<String,ArrayList<Course>> courses;
	static ArrayList<Student> students;	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		courseList = new ArrayList<Course>();
		
		courses =  CourseParser.parseCourses(courseList,Constants.TOT_COURSES);
		
		students = StudentGenerator.generateStudents(Constants.NUM_STUDENTS, courseList);
		
		//GreedyScheduler.greedyScheduleByStudent(students, courses);
		//GreedyScheduler.greedyScheduleByPref(students, courses);
		GreedyScheduler.greedyScheduleByPrefRandomized(students, courses);
		
		
		printStudents();
		printCourses();
		
		int[] prefCount = getPrefCount(students);
		
		BarChartMaker.makeBarChart(prefCount); 

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
		
		//Optimal printing of info
		for(int i = 0; i <prefCount.length; i++)
		{
			System.out.println("There are " + prefCount[i] + " students in their preference " + i + " class.");
		}
		
		return prefCount;
		
	}
	
	
}
