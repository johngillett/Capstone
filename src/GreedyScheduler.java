import java.util.*;

public class GreedyScheduler {

	public static void greedyScheduleByPref(Student[] students, Course[] courses)
	{
		ArrayList<Student> studentsToPlace = new ArrayList<Student>(Arrays.asList(students));	
		ArrayList<Student> placedStudents = new ArrayList<Student>(); 
		
		while(studentsToPlace.size() != 0){

		for(Student st: studentsToPlace)
		{
			Course[] prefs = st.prefs;
			
			for(int i = st.indexOfNextCourseToCheck; i < prefs.length;i++)
			{
				if(prefs[i].hasRoom()) //&& schedule is compatible
				{
					prefs[i].addStudent(st);
					st.courses.add(prefs[i]);
					//if()
					st.indexOfNextCourseToCheck = i+1;
					break;
				}
				
			}
				
		}
		}
		
		
	}
	
	//greedyScheduleByStudent
	
	
	
}
