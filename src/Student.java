import java.util.ArrayList;

public class Student {

	int id;
	double satisfactionScore; //to gauge how many top choices are placed
	String[] prefs;
	int indexOfNextCourseToCheck;
	
	ArrayList<Course> courses;

	ArrayList<Day> schedule;
	
	public Student(int id, String[] prefs)
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
	
	//Add class to Student's schedule
	for(Day cDay: c.schedule)
	{
		this.schedule.add(cDay);
	}
	
	
	int newScore = 0;
	
	int classCount = 0;
	
	for(int i = 0; i < prefs.length; i++)
	{
		for(Course co : courses)
		{
			if(co.isLab)
				continue;
			
			if(co.isSameCourse(prefs[i]))	
			{
				classCount++;
				newScore += (int) Math.pow(2,i+1);
			}
		}
	}
	
	if(classCount < 4)
	{
		newScore += (4 - classCount) * Math.pow(2, 9);
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
			ArrayList<Course> labs = courseToCheck.getLabs();
			
			boolean foundLab = false;
			
			for(Course lab : labs)
			{
				if(!lab.hasRoom())
					continue;
				
				boolean noConflicts = true;	
				
				for(Day day1 : schedule)
				{
					for(Day day2 : lab.schedule)
					{
						//if course falls on same day
						if(day1.day.equals(day2.day))
						{
							//if one course starts & ends before another, then it is compatible, otherwise it isn't
							if(((day1.startTime < day2.startTime && day1.endTime < day2.startTime ) || (day2.startTime < day1.startTime && day2.endTime < day1.startTime)))
							{	
							noConflicts = false;
							break;
							}
						}
					
					}
					
					if(!noConflicts)
						break;
					
				}		
				if(noConflicts)
				{	
					foundLab = true;
					enrollInCourse(lab);
					lab.addStudent(this);
					break;
				}
				
				
			}
			
			if(!foundLab)
				return false;
			
		}
		
		courseToCheck.addStudent(this);
		enrollInCourse(courseToCheck);
		return true;
	}

	public String toString()
	{
		String toReturn = "Student: "+id+", Prefs: ";
		
		int i = 0;
		for(i = 0; i < prefs.length;i++)
			toReturn += prefs[i]+", ";
		
		toReturn += " - Enrolled: ";
		
		for(i = 0; i < courses.size();i++)
			toReturn += courses.get(i).getID()+courses.get(i).getSectionID()+", ";
		
		toReturn += " Score: "+satisfactionScore;
		
		return toReturn;
	}
	
}
