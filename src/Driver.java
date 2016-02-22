import java.util.ArrayList;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub


		ArrayList<Course> courses =  CourseParser.parseCourses(Constants.TOT_COURSES);

		ArrayList<Student> students = StudentGenerator.generateStudents(Constants.NUM_STUDENTS, courses);

		//GreedyScheduler.greedyScheduleByStudent(students, courses);
		//GreedyScheduler.greedyScheduleByPref(students, courses);
		GreedyScheduler.greedyScheduleByPrefRandomized(students, courses);
		GreedyScheduler.greedyScheduleByStudent(students, courses);

		double avgScore = 0;

		for(Student stud : students)
		{
			System.out.println(stud.toString());
			avgScore += stud.satisfactionScore;
		}

		avgScore = avgScore / students.size();

		System.out.println("Average Score: "+avgScore);

		//for(Course cour : courses)
		//	System.out.println(cour.title+", Size: "+cour.curSize);

//		System.out.println(courses.get(18).isCompatible(courses.get(3)));
//		System.out.println(courses.get(18));
//		System.out.println(courses.get(3));

	}



}
