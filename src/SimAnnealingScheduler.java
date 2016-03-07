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
			
			//Get Array of all course sections from Student's pref List that are compatible and not enrolled & ignoring the 4th course if there is one 
			Course[] prefs = studToCheck.getRemainingCompPrefs(courses);	
			
			if(prefs == null)
			{
				System.out.println("No other possible choices!");
				studToCheck = students.topElement();
				System.out.println("switching to: "+studToCheck.id);
				students.pop();
				continue;
			}
			
			for(Course pref : prefs)
			{
				System.out.println(pref);
				//Get students in order of satisfaction score 
				PriorityQueue studentsHeap = constructStudentHeapFromCourse(pref);
				
				//Course may be filled with non-freshmen
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
	private static PriorityQueue constructStudentHeapFromCourse(Course section)
	{
		//declare Min Q
		PriorityQueue toReturn = new PriorityQueue(false); 
		
		for(Student stud : section.students)
			toReturn.push(stud.satisfactionScore, stud);
		
		if(toReturn.isEmpty())
			System.out.println("No students found in course: "+section.toString());
		return toReturn;
	}
	
	
	
}


