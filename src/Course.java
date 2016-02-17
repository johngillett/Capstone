import java.util.ArrayList;

public class Course {

	int courseID;
	
	ArrayList<Day> schedule;
	
	ArrayList<Student> students;
	
	int minSize;
	int maxSize;
	
	int curSize;
	
	public Course(int id, ArrayList<Day> schedule, int min, int max)
	{
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
	
}
