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
	
	int scoreToAdd = 0;
	
	for(int i = 0; i < prefs.length;i++)
	{
		if(prefs[i].getID() == c.getID())
			scoreToAdd = (int) Math.pow(2,i+1);
	}
	
	if(satisfactionScore == Integer.MAX_VALUE)
		satisfactionScore = scoreToAdd;
	else
		satisfactionScore += scoreToAdd;
	
	
	
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
