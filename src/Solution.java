
import java.util.ArrayList;
import java.util.HashMap;

public class Solution {

	//private static HashMap<String,ArrayList<Course>> courses;
	//private static HashMap<Integer,Student> students;
	
	private int totSatScore;
	
	public Solution(HashMap<String,ArrayList<Course>> inCourses, HashMap<Integer,Student> inStudents, int inScore)
	{
		/*
		 * Ideally this is where we would clone all the course and student objects over. 
		 * 
		 * Unfortunately, this is much more complicated than expected, since we technically have infinite references between courses and students
		 * (For instance, a course object contains a list of students, which all contain a list of courses, which all contain a list of students, etc.)
		 * I tried multiple methods for "Deep Cloning" but haven't had any success.
		 * 
		 * SOOo we might want to think of a new way to represent the output of the data rather than just keeping the hashmaps. 
		 * For instance, we can create a bare-bones student object that just has a list of CourseIDs for their enrolled courses, and the same for the course.
		 * That way our solution object will be much smaller, and we can write methods that can take the solution object and tell us all the 
		 * relevant details. 
		 * 
		 * We could also write a method that would take the final solution object and update all the actual student and course hashmaps to reflect
		 * the data accurately. 
		 */
		
		this.totSatScore = inScore;
		
	}
	
	public int getScore()
	{
		return this.totSatScore;
	}
	
	

}
