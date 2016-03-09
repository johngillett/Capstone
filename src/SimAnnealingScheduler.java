import java.util.ArrayList;
import java.util.HashMap;

public class SimAnnealingScheduler {

	private static HashMap<String,ArrayList<Course>> courses;
	private static HashMap<Integer,Student> students;
	
	public static int Schedule(HashMap<Integer,Student> inStudents,HashMap<String,ArrayList<Course>> inCourses)
	{
		courses = inCourses;
		students = inStudents;
		
		int totalSatScore = getTotalSatScore(students);
		
		PriorityQueue studentsHeap = constructMaxHeapOfAllStudents();
		
		System.out.println("Num of students: "+studentsHeap.size());
		
		return Schedule(studentsHeap,totalSatScore);
	}
	
	//Recursive method
	private static int Schedule(PriorityQueue students, int currTotalSatScore)
	{

		System.out.println("Starting out with a score of "+currTotalSatScore+", aiming for "+Constants.LINEAR_OBJ_THRESHOLD+"!");
		
		
		//If Current total is less than threshold, return
		if(Constants.SAT == Constants.SAT_SCALE.Linear)
		{
			if(currTotalSatScore < Constants.LINEAR_OBJ_THRESHOLD)
				return currTotalSatScore;
		}
		else
		{
			if(currTotalSatScore < Constants.GEOMETRIC_OBJ_THRESHOLD)
				return currTotalSatScore;
		}
		
		students = constructMaxHeapOfAllStudents();
		
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
				
				//Get students in order of satisfaction score 
				PriorityQueue studentsHeap = constructStudentHeapFromCourse(pref);
				
				//Course may be filled with non-freshmen
				if(studentsHeap.isEmpty())
					continue;
				
				Student studInCourse = studentsHeap.topElement();
				studentsHeap.pop();
				
				while(!studentsHeap.isEmpty())
				{
					//stu1 is entering a higher preferred course by replacing stu2
					
					//The preference number of the course we'd be swapping the student into 
					int stu1PrefEntering = studToCheck.getPrefNumber(pref.getID());
					//the preference number of the course we would be taking toRep from to make the swap. 9 if he doesn't have a full course load 
					int stu1PrefLeaving = studToCheck.getLastEnrolledPrefNumber();
					
					//the preference number of the course we'd be taking this student out of 
					int stu2PrefLeaving = studInCourse.getPrefNumber(pref.getID());
					//The preference number of the course we can schedule the replaced student into if we make the swap, 9 if there is none.
					int stu2PrefEntering = getNextPreferredCourseAfterPotentialSwap(studInCourse,stu2PrefLeaving);
					
					int netChange = netChangeFromReplacingStudent(stu1PrefLeaving,stu1PrefEntering,stu2PrefLeaving,stu2PrefEntering);
					
					if(netChange < 0) // || probability(netChange);
					{
					currTotalSatScore += netChange;	
						
					//System.out.println("Sending "+studToCheck.id +" from pref "+toRepFromPrefPos+" to "+toRepPrefPos+" and student "+studInCourse.id+" from "+toBeRepPrefPos+" to "+repToPrefPos);
					
					//replaces stud2 with stud1 in Course
					//replaceStudentInCourse(Student stud1, int stud1From, Student stud2, int stud2To, Course course)
					replaceStudentInCourse(studToCheck,stu1PrefLeaving,studInCourse,stu2PrefEntering,pref);
					return Schedule(students,currTotalSatScore);
						
					}
					
					studInCourse = studentsHeap.topElement();
					studentsHeap.pop();
					
				}
				
				
			}
			
			studToCheck = students.topElement();
			//System.out.println("switching to: "+studToCheck.id);
			students.pop();
		}
		
		//System.out.println("\t***Got to end of students, trying again!");
		//return Schedule(students,currTotalSatScore);
		
		
		return currTotalSatScore;
	}
	
	private static int netChangeFromReplacingStudent(int stud1From, int stud1To, int stud2From, int stud2To)
	{
		//System.out.println("Checking if swapping stud1 from pref num: "+stud1From+" to "+stud1To+ " and stud2 from pref num: "+stud2From+" to "+stud2To);
		
		//Convert vals to geometric if enabled
		if(Constants.SAT == Constants.SAT_SCALE.Geometric){
			stud1From = (int)Math.pow(2, stud1From);
			stud1To = (int)Math.pow(2, stud1To);
			
			stud2From = (int)Math.pow(2, stud2From);
			stud2To = (int)Math.pow(2, stud2To);
			
		}
		
		int netChange = stud1To - stud1From;
		
		netChange += (stud2To - stud2From);
		
		//System.out.println("Net Change will be: "+netChange);
		
		return netChange;
	}
	
	//replaces stud2 with stud1 in Course
	private static void replaceStudentInCourse(Student stud1, int stud1From, Student stud2, int stud2To, Course course)
	{
		
		
		
		//Unenroll stud1 from original course if there is one
		if(stud1From != 9)
		{
			stud1.unenrollFromPrefCourse(stud1From);
		}
		
	
		stud2.unenroll(course);
	
		boolean foundCourse= true;
		//Enroll stud2 
		if(stud2To != 9)
		{
		if(stud2.hasCourse(stud2.prefs[stud2To-1]))
			System.out.println("Trying to place student into class they already have!");	
			
		foundCourse = false;	
		ArrayList<Course> sections = courses.get(stud2.prefs[stud2To-1]);
		
			for(Course s : sections)
			{
				
				
				if(s.hasRoom() && stud2.addIfFitsInSchedule(s))
				{
					foundCourse = true;
					break;
				}
			}
		}
		
		if(!foundCourse)
			System.out.println("\tcouldn't place replaced student in new course!");
		
		if(stud1.hasCourse(course.getID()))
			System.out.println("Trying to place student into class they already have!");	
			
		//System.out.println("Attempting to add student...");
		if(!stud1.addIfFitsInSchedule(course))
			System.out.println("couldnt add student??");
		
	}
	
	
	//imagining not being in toMask, returns the preference number of the next preferred course the student could be scheduled in. 9 if there is none. 
	private static int getNextPreferredCourseAfterPotentialSwap(Student toCheck, int toMask) {
		// TODO Auto-generated method stub
		
		int toReturn = 9;
		
		toCheck.IgnorePrefCourse(toMask);
		
		//check following preferred courses 
		for(int i = toMask; i< toCheck.prefs.length;i++)
		{
			if(toCheck.hasCourse(toCheck.prefs[i]))
				continue;
			
			ArrayList<Course> sections = courses.get(toCheck.prefs[i]);
			
			boolean foundOne = false;
			
			for(Course c : sections)
			{
				if(c.hasRoom() && toCheck.fitsInSchedule(c))
				{
					toReturn = i+1;
					foundOne = true;
					break;
				}
			}
			
			if(foundOne)
				break;
			
		}
		
		toCheck.stopIgnoringCourse();
		
		return toReturn;
	}
	
	
	public static int getTotalSatScore(HashMap<Integer, Student> students)
	{
		int score = 0;
		
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
			{
					Student stud = entry.getValue();
					score += stud.satisfactionScore;
			}
		
		//System.out.print("Initial Total score is: "+score);
		return score;
	}

	//returns a min priority queue of all students in a course
	private static PriorityQueue constructStudentHeapFromCourse(Course section)
	{
		//declare Min Q
		PriorityQueue toReturn = new PriorityQueue(false); 
		
		for(Student stud : section.students)
			toReturn.push(stud.satisfactionScore, stud);
		
		//if(toReturn.isEmpty())
			//System.out.println("No students found in course: "+section.toString());
		return toReturn;
	}
	
	private static PriorityQueue constructMaxHeapOfAllStudents()
	{
	//Construct heap of students
			PriorityQueue studentsHeap = new PriorityQueue(true); 
			for(HashMap.Entry<Integer, Student> entry : students.entrySet())
			{
					Student stud = entry.getValue();
					studentsHeap.push(stud.satisfactionScore, stud);
			}
			
			return studentsHeap;
	}
	
}


