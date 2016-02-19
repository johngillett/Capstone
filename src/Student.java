import java.util.ArrayList;

public class Student {

	int id;
	double satisfactionScore; //to gauge how many top choices are placed
	Course[] prefs;
	int indexOfNextCourseToCheck;
	
	ArrayList<Course> courses;
	
	public Student(int id, Course[] prefs)
	{
	this.id = id;
	this.prefs = prefs;
		
	this.satisfactionScore = Integer.MAX_VALUE;
	
	this.courses = new ArrayList<Course>();	
	
	this.indexOfNextCourseToCheck = 0;
	
	}

	
	public void enrollInCourse(Course c)
	{
	courses.add(c);	
	
	int newScore = 0;
	
	for(int i = 0; i < prefs.length; i++)
	{
		for(Course co : courses)
		{
			if(co.getID() == prefs[i].getID())	
				newScore += (int) Math.pow(2,i+1);
		}
	}
	
	if(courses.size() < 4)
	{
		newScore += (4 - courses.size()) * Math.pow(2, 9);
	}
	
	this.satisfactionScore = newScore;
	
	}
	
	//checks if course has no conflicts with student's schedule
	public boolean fitsInSchedule(Course courseToCheck)
	{	
		for(Course course: courses)
		{
			if(!course.isCompatible(courseToCheck))
			{
				System.out.println(course.title + " conflicts with " + courseToCheck.title);
				return false;
			}
		}
		return true;
	}

	public String toString()
	{
		String toReturn = "Student: "+id+", Prefs: ";
		
		int i = 0;
		for(i = 0; i < prefs.length;i++)
			toReturn += prefs[i].getID()+", ";
		
		toReturn += " - Enrolled: ";
		
		for(i = 0; i < courses.size();i++)
			toReturn += courses.get(i).getID()+", ";
		
		toReturn += " Score: "+satisfactionScore;
		
		return toReturn;
	}
	
}
