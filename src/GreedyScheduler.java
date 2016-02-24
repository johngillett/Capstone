import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

//import java.util.*;

public class GreedyScheduler {

	public static void greedyScheduleByPref(ArrayList<Student> students,HashMap<String,ArrayList<Course>> courses)
	{
		
		for(int y = 0; y< Constants.STUD_COURSE_LIMIT;y++)
		{

			for(Student st: students)
			{
			String[] prefs = st.prefs;
			
				for(int i = st.indexOfNextCourseToCheck; i < prefs.length;i++)
				{
					ArrayList<Course> prefCourses = courses.get(prefs[i]);
					
					boolean addedCourse = false;
					
					for(Course prefCourse : prefCourses)
					{
						
						if(prefCourse.hasRoom() && st.addIfFitsInSchedule(prefCourse)) 
						{
							st.indexOfNextCourseToCheck = i+1;
							addedCourse = true;
							break;
						}
						else
							continue;
						
					
					}
					
					if(addedCourse)
						break;
				
				}
				
			}
			
		}
		
		
		
	}
	
	//greedyScheduleByStudent
//	public static void greedyScheduleByStudent(ArrayList<Student> students)
//	{
//		
//
//		for(Student st: students)
//		{
//		Course[] prefs = st.prefs;
//			
//		
//			int numAdded = 0;
//			
//			for(int i = 0; i < prefs.length;i++)
//			{
//				
//				if(prefs[i].hasRoom() && st.addIfFitsInSchedule(prefs[i])) //&& schedule is compatible
//				{
//					prefs[i].addStudent(st);
//					st.enrollInCourse(prefs[i]);
//					numAdded++;
//					
//				}
//				
//				if(numAdded == Constants.STUD_COURSE_LIMIT)
//					break;
//				
//			}
//				
//		}
//	}
		
		
	public static void greedyScheduleByPrefRandomized(ArrayList<Student> students,HashMap<String,ArrayList<Course>> courses)
	{
		
		for(int y = 0; y< Constants.STUD_COURSE_LIMIT;y++)
		{

			for(Student st: students)
			{
			String[] prefs = st.prefs;
			
				for(int i = st.indexOfNextCourseToCheck; i < prefs.length;i++)
				{
					ArrayList<Course> prefCourses = courses.get(prefs[i]);
					
					boolean addedCourse = false;
					
					for(Course prefCourse : prefCourses)
					{
						
						if(prefCourse.hasRoom() && st.addIfFitsInSchedule(prefCourse)) 
						{
							st.indexOfNextCourseToCheck = i+1;
							addedCourse = true;
							break;
						}
						else
							continue;
						
					
					}
					
					if(addedCourse)
						break;
				
				}
				
			}
			
			Collections.shuffle(students);
			
		}
		
		
		
	}
	
	
}
