import java.util.ArrayList;

public class Student {

	int id;
	double satisfactionScore; //to gauge how many top choices are placed
	Course[] prefs;
	int indexOfNextCourseToCheck;
	
	ArrayList<Course> courses;

	ArrayList<Day> schedule;
	
	public Student(int id, Course[] prefs)
	{
	this.id = id;
	this.prefs = prefs;
		
	this.satisfactionScore = Integer.MAX_VALUE;
	
	this.courses = new ArrayList<Course>();	
	
	this.indexOfNextCourseToCheck = 0;
	
	this.schedule = new ArrayList<Day>();
	
	}

	//adds course to student's schedule and updates satisfaction score
	public void enrollInCourse(Course c)
	{
	courses.add(c);	
	
	int newScore = 0;
	
	for(int i = 0; i < prefs.length; i++)
	{
		for(Course co : courses)
		{
			if(co.isSameCourse(prefs[i]))	
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
	public boolean addIfFitsInSchedule(Course courseToCheck)
	{	
		for(Day day1 : schedule)
		{
			for(Day day2 : courseToCheck.schedule)
			{
				//if course falls on same day
				if(day1.day.equals(day2.day))
				{
					//if one course starts & ends before another, then it is compatible, otherwise it isn't
					if(!((day1.startTime < day2.startTime && day1.endTime < day2.startTime ) || (day2.startTime < day1.startTime && day2.endTime < day1.startTime)))
						return false;
				}
			}
		}
		
		if(courseToCheck.hasLab)
		{
			
			
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
