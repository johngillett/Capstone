import java.util.ArrayList;
import java.util.HashMap;

public class SimAnnealingScheduler {

	public static void Schedule(HashMap<Integer,Student> students,HashMap<String,ArrayList<Course>> courses)
	{
		//Construct heap of students
		PriorityQueue studentsHeap = new PriorityQueue(true); 
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
		{
				Student stud = entry.getValue();
				studentsHeap.push(stud.satisfactionScore, stud);
		}
		
		int totalSatScore = getTotalSatScore(students);
		
		Schedule(studentsHeap,courses,totalSatScore);
	}
	
	//Recursive method
	private static boolean Schedule(PriorityQueue students,HashMap<String,ArrayList<Course>> courses, int currTotalSatScore)
	{
		//If Current total is less than threshold, return
		if(Constants.SAT == Constants.SAT_SCALE.Linear)
		{
			if(currTotalSatScore < Constants.LINEAR_OBJ_THRESHOLD)
				return true;
		}
		else
		{
			if(currTotalSatScore < Constants.GEOMETRIC_OBJ_THRESHOLD)
				return true;
		}
		
		Student studToCheck = students.topElement();
		students.pop();
		
		while(!students.isEmpty())
		{
			//System.out.println("hi..");
			
			String[] prefs = null;
			//if student needs more courses
			if(studToCheck.courses.size() < Constants.STUD_COURSE_LIMIT)
			{
			prefs = studToCheck.getRemainingCompPrefs(courses);	
			}
			//if student has full course load
			else
			{
				//get remaining compatible prefs ignoring the lowest Preferred 
			prefs = studToCheck.getRemainingCompPrefs(courses);
			}
			
			if(prefs == null)
			{
				System.out.println("No other possible choices!");
				studToCheck = students.topElement();
				System.out.println("switching to: "+studToCheck.id);
				students.pop();
				continue;
			}
			
			for(String pref : prefs)
			{
				System.out.println(pref);
				//Get students in order of satisfaction score 
				PriorityQueue studentsHeap = constructStudentHeapFromCourse(courses.get(pref));
				
				if(studentsHeap.isEmpty())
					continue;
				
				Student studInCourse = studentsHeap.topElement();
				studentsHeap.pop();
				
				while(!studentsHeap.isEmpty())
				{
					//if(replaceisgood(studToCheck,newStudent) || probability)
					//{
					//	replace(studToCheck,newStudent)
					//	return Schedule(students,courses,newTotalSatScore);
						
					//}
					
					studInCourse = studentsHeap.topElement();
					studentsHeap.pop();
					
				}
				
				
			}
			
			studToCheck = students.topElement();
			System.out.println("switching to: "+studToCheck.id);
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

	//returns a min priority queue of all students in a course
	private static PriorityQueue constructStudentHeapFromCourse(ArrayList<Course> courseSections)
	{
		//declare Min Q
		PriorityQueue toReturn = new PriorityQueue(false); 
		
		for(Course c: courseSections)
		{
			for(Student stud : c.students)
				toReturn.push(stud.satisfactionScore, stud);
		}
		if(toReturn.isEmpty())
			System.out.println("No students found in course!");
		return toReturn;
	}
	
	
	
}


