import java.util.ArrayList;

public class Course {

	String title;
	
	String dept;
	
	int courseNum;
	
	String sectionID;
	
	boolean hasLab;
	
	ArrayList<Course> labs;
	
	ArrayList<Day> schedule;
	
	ArrayList<Student> students;
	
	int minSize;
	int maxSize;
	
	int curSize;
	
	public Course(String title, String dep, String sectionID, int cN, ArrayList<Day> schedule, int min, int max)
	{
		this.title = title;
		
		this.dept = dep;
		this.courseNum = cN;
		
		this.sectionID = sectionID;
		
		this.schedule = schedule;
		
		this.minSize = min;
		this.maxSize = max;
		
		this.students = new ArrayList<Student>();
		this.curSize = 0;
		
		this.labs = new ArrayList<Course>();
		
		this.hasLab = false;
		
	}
	
	public boolean hasRoom()
	{
		return curSize < maxSize;
	}
	
	public void addLab(Course c)
	{
	labs.add(c);
	hasLab = true;
	}
	
	public void printLabs()
	{
		for(Course c: labs)
		{
			System.out.println("\t"+c.toString());
		}
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
	
	
	public boolean isCompatible(Course course)
	{
		for(Day dayCourse1 : schedule)
		{
			for(Day dayCourse2 : course.schedule)
			{
				//if course falls on same day
				if(dayCourse1.day.equals(dayCourse2.day))
				{
					//if one course starts & ends before another, then it is compatible, otherwise it isn't
					if(!((dayCourse1.startTime < dayCourse2.startTime && dayCourse1.endTime < dayCourse2.startTime ) || (dayCourse2.startTime < dayCourse1.startTime && dayCourse2.endTime < dayCourse1.startTime)))
						return false;
				}
			}
		}
		
	
		
		//found no conflicts
		return true;
			
	}
	
	public boolean isSameCourse(Course c)
	{
	return this.dept.equals(c.dept) && this.courseNum == c.courseNum;	
		
	}
	
	public boolean isSameCourse(String dept, int cN)
	{
	return this.dept.equals(dept) && this.courseNum == cN;
	}
	
	public boolean isSameSection(String sectID)
	{
	return this.sectionID.equals(sectID);
	}
	
	public String getID()
	{
	return this.dept+this.courseNum;
	}
	
	public void addMeetingTime(ArrayList<Day> d)
	{
		schedule.addAll(d);
	}
	
	
	//This toString method doesn't account for 
	//possibly different start and end times on different days
	public String toString()
	{
		String toReturn = this.dept +this.courseNum +this.sectionID+", ";
		
		//String day = "_";
		//String startTime = "" + schedule.get(0).startTime;
		//String endTime = "" + schedule.get(0).endTime; 
		for(Day d : schedule)
		{
			toReturn += d.toString()+" ";
		}
		return toReturn +", "+title; 
	}
	
}
