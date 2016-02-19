import java.util.ArrayList;

public class Course {

	String title;
	
	int courseID;
	
	ArrayList<Day> schedule;
	
	ArrayList<Student> students;
	
	int minSize;
	int maxSize;
	
	int curSize;
	
	public Course(String title, int id, ArrayList<Day> schedule, int min, int max)
	{
		this.title = title;
		
		this.courseID = id;
		this.schedule = schedule;
		
		this.minSize = min;
		this.maxSize = max;
		
		this.students = new ArrayList<Student>();
		this.curSize = 0;
	}
	
	public boolean hasRoom()
	{
		return curSize < maxSize;
	}
	
	public void addStudent(Student stud)
	{
	curSize++;	
	students.add(stud);	
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public int getID()
	{
		return this.courseID;
	}
	
	public boolean isCompatible(Course course)
	{
		for(Day dayCourse1 : schedule)
		{
			for(Day dayCourse2 : course.schedule)
			{
				//if course falls on same day
				if(dayCourse1.day.equals(dayCourse2.day))
				{
					//check if times overlap
					//if they start at the same time
					//if(dayCourse1.startTime == dayCourse2.startTime) return false;
					
					//if one course starts & ends before another, then it is compatible, otherwise it isn't
					if(!((dayCourse1.startTime < dayCourse2.startTime && dayCourse1.endTime < dayCourse2.startTime ) || (dayCourse2.startTime < dayCourse1.startTime && dayCourse2.endTime < dayCourse1.startTime)))
						return false;
				}
			}
		}
		
		//found no conflicts
		return true;
			
	}
	
	//This toString method doesn't account for 
	//possibly different start and end times on different days
	public String toString()
	{
		String toReturn = "";
		String day = "_";
		String startTime = "" + schedule.get(0).startTime;
		String endTime = "" + schedule.get(0).endTime; 
		for(Day d : schedule)
		{
			day += d.day + "_";
		}
		return toReturn + courseID + ", " +day+ ", " +startTime+ "-" + endTime + ", "+ title; 
	}
	
}
