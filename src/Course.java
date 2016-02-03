import java.util.ArrayList;

public class Course {

	int courseID;
	
	Day[] schedule;
	
	ArrayList<Student> students;
	
	int minSize;
	int maxSize;
	
	int curSize;
	
	public Course(int id, Day[] sch, int min, int max)
	{
		this.courseID = id;
		this.schedule = sch;
		
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
