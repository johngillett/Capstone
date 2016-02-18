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
	
}
