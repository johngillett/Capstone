import java.util.ArrayList;
import java.util.Collections;

//import java.util.*;

public class GreedyScheduler {

	public static void greedyScheduleByPref(ArrayList<Student> students, ArrayList<Course> courses)
	{
		//ArrayList<Student> studentsToPlace = new ArrayList<Student>(Arrays.asList(students));	
		//ArrayList<Student> placedStudents = new ArrayList<Student>(); 
		
		for(int y = 0; y< Constants.STUD_COURSE_LIMIT;y++){

			for(Student st: students)
			{
			Course[] prefs = st.prefs;
			
				for(int i = st.indexOfNextCourseToCheck; i < prefs.length;i++)
				{
				
					if(prefs[i].hasRoom() && st.fitsInSchedule(prefs[i])) 
					{
						prefs[i].addStudent(st);
						
						st.enrollInCourse(prefs[i]);
						st.indexOfNextCourseToCheck = i+1;
						break;
					}
				
				
				}
				
			}
			
		}
		
		
		
	}
	
	//greedyScheduleByStudent
	public static void greedyScheduleByStudent(ArrayList<Student> students, ArrayList<Course> courses)
	{
		

		for(Student st: students)
		{
		Course[] prefs = st.prefs;
			
		
			int numAdded = 0;
			
			for(int i = 0; i < prefs.length;i++)
			{
				
				if(prefs[i].hasRoom() && st.fitsInSchedule(prefs[i])) //&& schedule is compatible
				{
					prefs[i].addStudent(st);
					st.enrollInCourse(prefs[i]);
					numAdded++;
					
				}
				
				if(numAdded == Constants.STUD_COURSE_LIMIT)
					break;
				
			}
				
		}
	}
		
		
	public static void greedyScheduleByPrefRandomized(ArrayList<Student> students, ArrayList<Course> courses)
	{
		//ArrayList<Student> studentsToPlace = new ArrayList<Student>(Arrays.asList(students));	
		//ArrayList<Student> placedStudents = new ArrayList<Student>(); 
		
		for(int y = 0; y< Constants.STUD_COURSE_LIMIT;y++){

			for(Student st: students)
			{
			Course[] prefs = st.prefs;
			
				for(int i = st.indexOfNextCourseToCheck; i < prefs.length;i++)
				{
				
					if(prefs[i].hasRoom() && st.fitsInSchedule(prefs[i])) 
					{
						prefs[i].addStudent(st);
						
						st.enrollInCourse(prefs[i]);
						st.indexOfNextCourseToCheck = i+1;
						break;
					}
				
				
				}
				
			}
			Collections.shuffle(students);
			
		}
		
		
		
	}
	
	
}
