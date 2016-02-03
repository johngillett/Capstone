
public class GreedyScheduler {

	public static void greedySchedule(Student[] students, Course[] courses)
	{
		
		for(Student st: students)
		{
			Course[] prefs = st.prefs;
			
			for(int i = st.indexOfNextCourseToCheck; i < prefs.length;i++)
			{
				if(prefs[i].hasRoom()) //&& schedule is compatible
				{
					prefs[i].addStudent(st);
					st.courses.add(prefs[i]);
					st.indexOfNextCourseToCheck = i+1;
					break;
				}
				
			}
				
		}
		
		
	}
	
	
	
}
