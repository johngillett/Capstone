import java.util.ArrayList;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub


		ArrayList<Course> courses =  CourseParser.parseCourses(100);
		
		ArrayList<Student> students = StudentGenerator.generateStudents(250, courses);
		
		//GreedyScheduler.greedyScheduleByStudent(students, courses);
		GreedyScheduler.greedyScheduleByPref(students, courses);
		
		for(Student stud : students)
			System.out.println(stud.toString());
		
//		System.out.println(courses.get(18).isCompatible(courses.get(3)));
//		System.out.println(courses.get(18));
//		System.out.println(courses.get(3));
		
	}

	
	
}
