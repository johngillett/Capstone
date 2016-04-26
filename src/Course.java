import java.util.ArrayList;

/**
 * This class represents one course, which must be a section
 * of a course offered at the university. This course is
 * uniquely identified by its department, course number, and section ID.
 * Each course has a schedule and a listing of the students enrolled in it.
 * 
 * @author Anna Dovzhik & John Gillett
 * @version 4.8.16
 *
 */
public class Course{

	String title;
	
	String dept;
	
	int courseNum;
	
	String sectionID;
	
	String ID;
	
	boolean hasLab;
	
	boolean isLab;
	
	ArrayList<Course> labs;
	
	ArrayList<Day> schedule;
	
	ArrayList<Student> students;
	
	ArrayList<String> conflictingCourses;
	
	int minSize;
	int maxSize;
	
	int curSize;
	
	boolean isSeminar;
	boolean isAdvising;
	
	public Course(String title, String dep, String sectionID, int cN, ArrayList<Day> schedule, int min, int max, int curSize, boolean isSeminar, boolean isAdvising)
	{
		this.title = title;
		
		this.dept = dep;
		this.courseNum = cN;
		
		this.ID = this.dept+this.courseNum;
		
		this.sectionID = sectionID;
		
		this.schedule = schedule;
		
		this.minSize = min;
		this.maxSize = max;
		this.curSize = curSize;
		
		this.students = new ArrayList<Student>();
		
		this.labs = new ArrayList<Course>();
		
		this.conflictingCourses = new ArrayList<String>();
		
		this.hasLab = false;
		this.isLab = false;
		
		this.isSeminar = isSeminar;
		this.isAdvising = isAdvising;
	}
	

	public boolean hasRoom()
	{
		return curSize < maxSize;
	}
	
	public void addLab(Course c)
	{
		for(Course l : this.labs)
		{
			if(c.isSameCourse(l) && l.isSameSection(c.getSectionID()))
			{
				//System.out.println("Repeat Lab: "+c.getID()+c.getSectionID()+", "+l.getID()+l.sectionID);
				return;
			}
		}
		
		
		c.isLab = true;
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
		students.add(stud);	
		curSize++;	
	}
	
	public void removeStudent(Student stud)
	{
		students.remove(stud);
		curSize--;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	
	private boolean isCompatible(Course course)
	{
		for(Day dayCourse1 : schedule)
		{
			for(Day dayCourse2 : course.schedule)
			{
				//if course falls on same day
				if(dayCourse1.day ==dayCourse2.day)
				{
					//if one course starts & ends before another, then it is compatible, otherwise it isn't
					if(!((dayCourse1.startTime < dayCourse2.startTime && dayCourse1.endTime < dayCourse2.startTime ) || (dayCourse2.startTime < dayCourse1.startTime && dayCourse2.endTime < dayCourse1.startTime)))
						return false;
					
					if((dayCourse1.startTime < dayCourse2.endTime && dayCourse1.startTime > dayCourse2.startTime) || (dayCourse1.endTime > dayCourse2.startTime && dayCourse1.endTime < dayCourse2.endTime))
						return false;
					
					if(dayCourse1.startTime == dayCourse2.startTime)
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
	
	public boolean isSameCourse(String id)
	{
		return id.equals(this.dept+this.courseNum);
	}
	
	public boolean isSameSection(String sectID)
	{
		return this.sectionID.equals(sectID);
	}
	
	public String getID()
	{
		return this.ID;
	}
	
	public String getSectionID()
	{
		return this.sectionID;
	}
	
	public void addMeetingTime(ArrayList<Day> d)
	{
		d1Loop:
		for(Day d1 : d)
		{
			for(Day d2: this.schedule)
			{
				if(d1.day.equals(d2.day))
				{
					if(d1.startTime == d2.startTime && d1.endTime == d2.endTime)
					//notRepeat = false;
					continue d1Loop;
				}
				
			}
			
			schedule.add(d1);				
			
		}
		
	}
	
	public boolean isLab()
	{
		return this.isLab;
	}
	
	public ArrayList<Course> getLabs()
	{
		return this.labs;
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
		return toReturn +", "+title+ ", # Enrolled: "+this.curSize+"/"+this.maxSize+", with "+this.students.size()+" freshmen. Has room: "+this.hasRoom();
		
	}

	public String schedToString()
	{
		String toReturn = "";
		for(Day d : schedule)
		{
			toReturn += d.toString()+" ";
		}
		return toReturn;
		
	}

	public boolean hasStudent(Student stud2) 
	{
		return this.students.contains(stud2);
	}
	
	public boolean isSeminar()
	{
		return this.isSeminar;	
	}
	
	public boolean isAdvising()
	{
		return this.isAdvising;
	}
	
	public void addToConflictingCourses(String courseID){
		this.conflictingCourses.add(courseID);
	}
	
	public boolean courseConflicts(String courseID){
		
		
		for(String c : this.conflictingCourses){
			if(courseID.equals(c)){
				//System.out.println("FOUND A COURSE CONFLICT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				return true;
			}
		}
		return false;
	}
	
}
