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
	
	//Constructor
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
	
	//Course can fit more students
	public boolean hasRoom()
	{
		return curSize < maxSize;
	}
	
	//Gives this course a Lab
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

	//Prints out all Labs connected to this Course
	public void printLabs()
	{
		for(Course c: labs)
		{
			System.out.println("\t"+c.toString());
		}
	}
	
	//Enroll student in Course
	public void addStudent(Student stud)
	{		
		students.add(stud);	
		curSize++;	
	}
	
	//Unenroll student from Course
	public void removeStudent(Student stud)
	{
		students.remove(stud);
		curSize--;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	//Returns if this course has a schedule conflict or not with another given course
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
	
	//Determines if this Course is the same as another (Not Section specific)
	public boolean isSameCourse(Course c)
	{
		return this.dept.equals(c.dept) && this.courseNum == c.courseNum;		
	}
	
	//Determines if this Course is the same as another (Not Section specific)
	public boolean isSameCourse(String dept, int cN)
	{
		return this.dept.equals(dept) && this.courseNum == cN;
	}
	
	//Determines if this Course is the same as another (Not Section specific)
	public boolean isSameCourse(String id)
	{
		return id.equals(this.dept+this.courseNum);
	}
	
	//Determines if this Course is the same section as another
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
	
	//Adds a scheduled meeting time to this Course
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

	//Returns this courses schedule as a string
	public String schedToString()
	{
		String toReturn = "";
		for(Day d : schedule)
		{
			toReturn += d.toString()+" ";
		}
		return toReturn;
		
	}
	
	//Returns true if a given student is enrolled in this course
	public boolean hasStudent(Student stud) 
	{
		return this.students.contains(stud);
	}
	
	public boolean isSeminar()
	{
		return this.isSeminar;	
	}
	
	public boolean isAdvising()
	{
		return this.isAdvising;
	}
	
	//Adds a course that shouldn't be given to a student that also has this course
	public void addToConflictingCourses(String courseID){
		this.conflictingCourses.add(courseID);
	}
	
	//Determines if a student shouldn't be enrolled in this and courseID.
	public boolean courseConflicts(String courseID){
		
		for(String c : this.conflictingCourses){
			if(courseID.equals(c)){
				//System.out.println("FOUND A COURSE CONFLICT");
				return true;
			}
		}
		return false;
	}
	
}
