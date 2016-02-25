import java.util.ArrayList;
import java.util.HashMap;

	
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
	
}
