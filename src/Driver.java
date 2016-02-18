import java.util.ArrayList;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub


		ArrayList<Course> courses =  CourseParser.parseCourses();
		
		ArrayList<Student> students = StudentGenerator.generateStudents(80, courses);
		
		//GreedyScheduler.greedyScheduleByStudent(students, courses);
		GreedyScheduler.greedyScheduleByStudent(students, courses);
		
		for(Student stud : students)
			System.out.println(stud.toString());
		
		
	}

	
	
}
