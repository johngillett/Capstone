import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Schedules students into courses by trying to place as many students into their
 * top preferences as possible.
 * 
 * @author Anna Dovzhik & John Gillett
 * @version 4.8.16
 */
public class GreedyScheduler {

	/**
	 * Schedules students by placing all into their top compatible preferences
	 * 
	 * @param students the students we are assigning courses to
	 * @param courses the courses that students can be placed in
	 * @param doingSeminar whether we are focusing on just seminars
	 */
	public static void greedyScheduleByPref(HashMap<Integer,Student> students,HashMap<String,ArrayList<Course>> courses, boolean doingSeminar)
	{
		//add arbitrarily high value to indicate Greedy
		
		int courseAmt;
		
		if(doingSeminar)
			courseAmt = Constants.STUD_SEM_LIMIT;
		else
			courseAmt = Constants.STUD_COURSE_LIMIT;
		
		//for shuffling students in each iteration
		ArrayList<Student> studentsList = new ArrayList<Student>();
		
		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			Student st = entry.getValue();
			studentsList.add(st);
		}
		
		for(int y = 0; y< courseAmt;y++)
		{

			//for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			//Student st = entry.getValue();
			for(Student st : studentsList){
			
			if(doingSeminar && st.hasSeminarCourse())
				continue;
			
			if(st.getClassCount() >= Constants.STUD_COURSE_LIMIT)
				continue;
			
			String[] prefs = st.prefs;
			//System.out.println("Working on student " + st.id);
				//for(int i = st.indexOfNextCourseToCheck; i < prefs.length;i++)
				for(int i = 0; i < prefs.length;i++)
				{
					if(prefs[i].equals(Constants.NULL_PREF)) continue;
					
					ArrayList<Course> prefCourses = courses.get(prefs[i]);
					
					boolean addedCourse = false;
					
					for(Course prefCourse : prefCourses)
					{
						//System.out.println("\t"+ prefCourse);
						//the course needs to have room, the student shouldn't have been placed any section of it yet, 
						//and it should fit in their schedule
						if(prefCourse.hasRoom() && !st.hasCourse(prefCourse.getID())&& st.addIfFitsInSchedule(prefCourse)) 
						{
							st.indexOfNextCourseToCheck = i+1;
							AlgTracker.addGreedyEntry(Driver.getTotalSatScore());
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
			
			Collections.shuffle(studentsList);
			
		}
		
		
		
	}
		
		
	/**
	 * Schedules students by placing all into their top compatible preferences.
	 * Randomly picks a new ordering of students for each iteration of filling the students' schedules 
	 * 
	 * @param inStudents the students we are assigning courses to
	 * @param courses the courses the students can be placed in
	 */
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
