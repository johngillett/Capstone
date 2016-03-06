import java.util.ArrayList;
import java.util.HashMap;

public class SimAnnealingScheduler {

	public static void Schedule(HashMap<Integer,Student> students,HashMap<String,ArrayList<Course>> courses)
	{
		//Construct heap of students
		PriorityQueue studentsHeap = new PriorityQueue(); 
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
		{
				Student stud = entry.getValue();
				studentsHeap.push(stud.satisfactionScore, stud);
		}
		
		int totalSatScore = getTotalSatScore(students);
		
		
	}
	
	//Recursive method
	private static boolean Schedule(PriorityQueue students,HashMap<String,ArrayList<Course>> courses, int currTotalSatScore)
	{
		//If Current total is less than threshold, return
		if(Constants.SAT == Constants.SAT_SCALE.Linear)
		{
			if(currTotalSatScore < Constants.LINEAR_OBJ_THRESHOLD)
				return;
		}
		else
		{
			if(currTotalSatScore < Constants.GEOMETRIC_OBJ_THRESHOLD)
				return;
		}
		
		Student studToCheck = students.topElement();
		students.pop();
		
		while(studToCheck != null)
		{
			
			String[] prefs;
			//if student needs more courses
			if(studToCheck.courses.size() < Constants.STUD_COURSE_LIMIT)
			{
			prefs = studToCheck.getRemainingCompPrefs();	
			}
			
			for(Students Prefs)
			{
				for(highest scores students in that course)
				{
					if(replaceisgood(studToCheck,newStudent) || probability)
					{
						replace(studToCheck,newStudent)
						return Schedule(students,courses,newTotalSatScore);
						
					}
					
					
				}
				
				
			}
			
			studToCheck = students.topElement();
			students.pop();
		}
		
		return false;
	}
	
	private static int getTotalSatScore(HashMap<Integer, Student> students)
	{
		int score = 0;
		
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
			{
					Student stud = entry.getValue();
					score += stud.satisfactionScore;
			}
		
		System.out.print("Initial Total score is: "+score);
		return score;
	}
	
}
