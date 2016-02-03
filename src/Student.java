import java.util.ArrayList;

public class Student {

	int id;
	
	Course[] prefs;
	int indexOfNextCourseToCheck;
	
	ArrayList<Course> courses;
	
	public Student(int id, Course[] prefs)
	{
	this.id = id;
	this.prefs = prefs;
		
	this.courses = new ArrayList<Course>();	
	
	this.indexOfNextCourseToCheck = 0;
	
	}
}
