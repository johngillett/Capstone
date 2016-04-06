import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

//import java.util.*;

public class GreedyScheduler {

	public static void greedyScheduleByPref(HashMap<Integer,Student> students,HashMap<String,ArrayList<Course>> courses, boolean doingSeminar)
	{

		AlgTracker.addEntry(14000);

		
		int courseAmt;
		
		if(doingSeminar)
			courseAmt = Constants.STUD_SEM_LIMIT;
		else
			courseAmt = Constants.STUD_COURSE_LIMIT;
		
		for(int y = 0; y< courseAmt;y++)
		{

			for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			Student st = entry.getValue();
			
			if(doingSeminar && st.hasSeminarCourse())
				continue;
			
			if(st.getClassCount() >= Constants.STUD_COURSE_LIMIT)
				continue;
			
			String[] prefs = st.prefs;
			//System.out.println("Working on student " + st.id);
				for(int i = st.indexOfNextCourseToCheck; i < prefs.length;i++)
				{
					ArrayList<Course> prefCourses = courses.get(prefs[i]);
					
					boolean addedCourse = false;
					
					for(Course prefCourse : prefCourses)
					{
						//System.out.println("\t"+ prefCourse);
						if(prefCourse.hasRoom() && st.addIfFitsInSchedule(prefCourse)) 
						{
							st.indexOfNextCourseToCheck = i+1;
							AlgTracker.addEntry(SimAnnealingScheduler.getTotalSatScore(students));
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
		
		AlgTracker.addEntry(14000);
		
		
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
		
		
	public static void greedyScheduleByPrefRandomized(HashMap<Integer,Student> inStudents,HashMap<String,ArrayList<Course>> courses)
	{
		
		ArrayList<Student> students = new ArrayList<Student>();
		
		for(HashMap.Entry<Integer, Student> entry : inStudents.entrySet()){
			Student st = entry.getValue();
			students.add(st);
		}
		
		
		for(int y = 0; y< Constants.STUD_COURSE_LIMIT;y++)
		{
			for(Student st : students){
			String[] prefs = st.prefs;
			
				for(int i = st.indexOfNextCourseToCheck; i < prefs.length;i++)
				{
					ArrayList<Course> prefSections = courses.get(prefs[i]);
					
					boolean addedCourse = false;
					
					for(Course prefCourse : prefSections)
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
