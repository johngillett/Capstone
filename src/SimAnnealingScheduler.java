import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This class schedules students into courses by
 * minimizing the total satisfaction score using 
 * the simulated annealing algorithm
 * 
 * @author Anna Dovzhik & John Gillett
 * @version 4.8.2016
 *
 */
public class SimAnnealingScheduler {

	private static HashMap<String,ArrayList<Course>> courses;
	private static HashMap<Integer,Student> studentsMap;
	
	static Solution bestSol;
	
	private static float temp;
	
	private static Random rand;
	
	/**
	 * Schedules using simulated annealing, but always tries to make the best possible swap
	 * of students in each iteration of the algorithm
	 * 
	 * @param inStudents the students we are scheduling
	 * @param inCourses the courses the students can be scheduled into
	 * @return the solution, i.e. the courses and students properly scheduled with a low total sat. score
	 */
	public static Solution ScheduleBiased(HashMap<Integer,Student> inStudents,HashMap<String,ArrayList<Course>> inCourses)
	{
		courses = inCourses;
		studentsMap = inStudents;
		
		rand = new Random();
		
		int totalSatScore = Driver.getTotalSatScore();
		
		//Create initial best score
		bestSol = new Solution(inCourses,inStudents,totalSatScore);
		
		temp = Constants.INIT_TEMP_VAL;
		
		ScheduleBiased();
		//ScheduleRecursively(studentsHeap,totalSatScore,1);
		
		return bestSol;
	}
	
	/**
	 * Schedules using simulated annealing, always choosing a random swap of students
	 * in each iteration of the algorithm
	 * 
	 * @param inStudents the students we are scheduling
	 * @param inCourses the courses the students can be scheduled into
	 * @return the solution, i.e. the courses and students properly scheduled with a low total sat. score
	 */
	public static Solution ScheduleUnbiased(HashMap<Integer,Student> inStudents,HashMap<String,ArrayList<Course>> inCourses)
	{
		courses = inCourses;
		studentsMap = inStudents;
		
		rand = new Random();
		
		int totalSatScore = Driver.getTotalSatScore();
		
		//Create initial best score
		bestSol = new Solution(inCourses,inStudents,totalSatScore);
		
		if(Constants.SAT == Constants.SAT_SCALE.Linear)
			System.out.println("Starting with a score of "+bestSol.getScore()+", aiming for "+Constants.LINEAR_OBJ_THRESHOLD);
			else
			System.out.println("Starting with a score of "+bestSol.getScore()+", aiming for "+Constants.GEOMETRIC_OBJ_THRESHOLD);
				
		
		temp = Constants.INIT_TEMP_VAL;
		
		ScheduleUnbiased();
		
		System.out.println("Started with: "+totalSatScore+", ended up with "+bestSol.getScore());
		
		return bestSol;
	}
	
	    //Heuristic Approach (less random) method
		/**
		 * Helper method for biased simulated annealing
		 */
		private static void ScheduleBiased()
		{
	        PriorityQueue students;// = constructMaxHeapOfAllStudents();
			
	        int currTotalSatScore = bestSol.getScore();
	        
	        //mainCondition:
			while(!conditionIsMet())
			{
				students = constructMaxHeapOfAllStudents();
				
				mLoop:
				for(int m = 0; m < Constants.ITERS_BEFORE_TEMP_SCALE; m++)
				{
					//System.out.println("Starting out with a score of "+currTotalSatScore+", aiming for "+Constants.LINEAR_OBJ_THRESHOLD+"!");
					
					if(students.isEmpty())
						break;
					
					Student studToCheck = students.topElement();
					students.pop();
				
					while(!students.isEmpty())
					{
					
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
						
						prefLoop:
						for(Course pref : prefs)
						{
							
							//Get students in order of satisfaction score 
							PriorityQueue studentsHeap = constructStudentHeapFromCourse(pref);
							
							//Course may be filled with non-freshmen
							if(studentsHeap.isEmpty())
							{
								//System.out.println("Skip!");
								continue;
							}
							
							Student studInCourse = studentsHeap.topElement();
							studentsHeap.pop();
							
							while(!studentsHeap.isEmpty())
							{
									
								//The preference number of the course we'd be swapping the student into 
								int stu1PrefEntering = studToCheck.getPrefNumber(pref.getID());
								//the preference number of the course we would be taking toRep from to make the swap. 9 if he doesn't have a full course load 
								int stu1PrefLeaving = studToCheck.getLastEnrolledPrefNumber();
								
								//the preference number of the course we'd be taking this student out of 
								int stu2PrefLeaving = studInCourse.getPrefNumber(pref.getID());
								//The preference number of the course we can schedule the replaced student into if we make the swap, 9 if there is none.
								int stu2PrefEntering = getNextPreferredCourseAfterPotentialSwap(studInCourse,stu2PrefLeaving);
								
								
								int netChange = netChangeFromReplacingStudent(stu1PrefLeaving,stu1PrefEntering,stu2PrefLeaving,stu2PrefEntering);
								
								//If the change was good or our temperature allows it 
								if(netChange < 0 || makeChangeAnyway(netChange)) 
								{
								currTotalSatScore += netChange;	
								
								//System.out.println("Made net change: "+netChange+", new score:"+currTotalSatScore);	
								
								//Update Best Solution if necessary
								if(currTotalSatScore < bestSol.getScore())
									bestSol = new Solution(courses,studentsMap,currTotalSatScore);
								
								//System.out.println("Sending "+studToCheck.id +" from pref "+toRepFromPrefPos+" to "+toRepPrefPos+" and student "+studInCourse.id+" from "+toBeRepPrefPos+" to "+repToPrefPos);
								
								//replaces stud2 with stud1 in Course
								//replaceStudentInCourse(Student stud1, int stud1From, Student stud2, int stud2To, Course course)
								replaceStudentInCourse(studToCheck,stu1PrefLeaving,studInCourse,stu2PrefEntering,pref);
								continue mLoop;
								}
								
								studInCourse = studentsHeap.topElement();
								studentsHeap.pop();
							}
						}
						studToCheck = students.topElement();
						students.pop();
					}
				}
				temp *= Constants.TEMP_SCALE_FACTOR;
				System.out.println("\tNew Temperature: "+temp);
			}
			return;
		}
	
		/**
		 * Helper method for unbiased simulated annealing
		 * randomly chooses students to replace in order to lower the total score
		 */
		private static void ScheduleUnbiased()
		{
	        PriorityQueue students;// = constructMaxHeapOfAllStudents();
			
	        int currTotalSatScore = bestSol.getScore();
	        
	        //mainCondition:
			while(!conditionIsMet())
			{
				students = constructMaxHeapOfAllStudents();
				
				mLoop:
				for(int m = 0; m < Constants.ITERS_BEFORE_TEMP_SCALE; m++)
				{
					//System.out.println("Starting out with a score of "+currTotalSatScore+", aiming for "+Constants.LINEAR_OBJ_THRESHOLD+"!");
					
					if(students.isEmpty())
						break;
					
					Student studToCheck = students.topElement();
					students.pop();
				
					while(!students.isEmpty())
					{
					
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
						
						prefLoop:
						for(Course pref : prefs)
						{
							
							//Get students in order of satisfaction score 
							//PriorityQueue studentsHeap = constructStudentHeapFromCourse(pref);
							ArrayList<Student> studentsList = constructArrayListFromCourse(pref);
							
							
							//Course may be filled with non-freshmen
							if(studentsList.isEmpty())
							{
								//System.out.println("Skip!");
								continue;
							}
							
							Student studInCourse = removeRandomStudent(studentsList);
							while(!studentsList.isEmpty())
							{
									
								//The preference number of the course we'd be swapping the student into 
								int stu1PrefEntering = studToCheck.getPrefNumber(pref.getID());
								//the preference number of the course we would be taking toRep from to make the swap. 9 if he doesn't have a full course load 
								int stu1PrefLeaving = studToCheck.getLastEnrolledPrefNumber();
								
								//the preference number of the course we'd be taking this student out of 
								int stu2PrefLeaving = studInCourse.getPrefNumber(pref.getID());
								
								if(!studInCourse.courseNotLocked(pref.getID()))
								{
									System.out.println("THIS SHOULDNT HAPPEN");
								}
								//The preference number of the course we can schedule the replaced student into if we make the swap, 9 if there is none.
								int stu2PrefEntering = getNextPreferredCourseAfterPotentialSwap(studInCourse,stu2PrefLeaving);
								
								//System.out.println("Got: "+stu2PrefEntering);
								if(!Driver.doingSeminar)
								{
									
									if(stu2PrefEntering == Constants.NUM_PREFS+1)
									{
										//System.out.println("HELLO");
										studInCourse = removeRandomStudent(studentsList);
										continue;
									}
								}
								
								int netChange = netChangeFromReplacingStudent(stu1PrefLeaving,stu1PrefEntering,stu2PrefLeaving,stu2PrefEntering);
								
								//If the change was good or our temperature allows it 
								if(netChange < 0 || makeChangeAnyway(netChange)) 
								{
								currTotalSatScore += netChange;	
								
								//if(netChange != 0 && temp >=.9)
								//if(Driver.doingSeminar && stu1PrefLeaving != stu2PrefEntering)
								//	System.out.println("Made net change: "+netChange+", new score:"+currTotalSatScore +" with Stud1 From "+stu1PrefLeaving+" to "+stu1PrefEntering+ " and Stud2 From "+stu2PrefLeaving+" to "+stu2PrefEntering);	
								
								AlgTracker.addSimAnnealEntry(currTotalSatScore);
								
								//Update Best Solution if necessary
								if(currTotalSatScore < bestSol.getScore())
									bestSol = new Solution(courses,studentsMap,currTotalSatScore);
								
								//System.out.println("Sending "+studToCheck.id +" from pref "+toRepFromPrefPos+" to "+toRepPrefPos+" and student "+studInCourse.id+" from "+toBeRepPrefPos+" to "+repToPrefPos);
								
								//replaces stud2 with stud1 in Course
								//replaceStudentInCourse(Student stud1, int stud1From, Student stud2, int stud2To, Course course)
								replaceStudentInCourse(studToCheck,stu1PrefLeaving,studInCourse,stu2PrefEntering,pref);
								continue mLoop;
								}
								
								studInCourse = removeRandomStudent(studentsList);
								
							}
						}
						studToCheck = students.topElement();
						students.pop();
						
					}
					
				}
				temp *= Constants.TEMP_SCALE_FACTOR;
				System.out.println("\tNew Temperature: "+temp+" new score: "+currTotalSatScore);
	        	
			}
			return;
		}
		

	/**
	 * Checks if the algorithm has succeeded yet or not
	 * Takes into account max possible satisfaction score and min temperature
	 * 
	 * @return whether algorithm should stop
	 */
	private static boolean conditionIsMet()
	{
		if(temp <= Constants.MIN_TEMP) return true;
		
		if(Constants.SAT == Constants.SAT_SCALE.Linear)
		{
			if(bestSol.getScore() < Constants.LINEAR_OBJ_THRESHOLD)
				return true;
		}
		else
		{
			if(bestSol.getScore() < Constants.GEOMETRIC_OBJ_THRESHOLD)
				return true;
		}
		
		return false;
		
	}
	
	//Checks if we should make a change despite it not being strictly 'good' for our solution
	private static boolean makeChangeAnyway(int netChange){
		
		double r = rand.nextDouble();
		
		//this equals 1 when the net change is 0, which means the change will always be made. What should we do about this? Did I do the equation right?
		double prob = Math.pow(Math.E,-1*(netChange/temp));
		
		//if(netChange == 0)
		//	return false; // skip every time
		//	return r < .15f; //skip 85% of the time
		
		//System.out.println("Probability of making change anyway: "+prob);

		return r < prob;	
	}
	
	//returns the net change to the overall satisfaction score if two students were to change courses
	private static int netChangeFromReplacingStudent(int stud1From, int stud1To, int stud2From, int stud2To)
	{
		
		//Convert vals to geometric if enabled
		if(Constants.SAT == Constants.SAT_SCALE.Geometric){
			stud1From = (int)Math.pow(2, stud1From);
			stud1To = (int)Math.pow(2, stud1To);
			
			stud2From = (int)Math.pow(2, stud2From);
			stud2To = (int)Math.pow(2, stud2To);
			
		}
		
		int netChange = stud1To - stud1From;
		
		netChange += (stud2To - stud2From);
		
		//if(netChange < 0)
		//System.out.println("Checking if swapping stud1 from pref num: "+stud1From+" to "+stud1To+ " and stud2 from pref num: "+stud2From+" to "+stud2To+" NEt change: "+netChange);
		//System.out.println("Net Change will be: "+netChange);
		
		return netChange;
	}
	
	//replaces stud2 with stud1 in Course
	private static void replaceStudentInCourse(Student stud1, int stud1From, Student stud2, int stud2To, Course course)
	{
		
		//Unenroll stud1 from original course if there is one
		if(stud1From != (Constants.NUM_PREFS+1))
		{
			stud1.unenrollFromPrefCourse(stud1From);
		}
		
	
		stud2.unenroll(course);
	
		boolean foundCourse= true;
		//Enroll stud2 
		if(stud2To != (Constants.NUM_PREFS+1))
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
			System.out.println("\tcouldnt add student?? "+stud1From+" to "+stud1.getPrefNumber(course.getID()));
		
	}
	
	//imagining not being in toMask, returns the preference number of the next preferred course the student could be scheduled in. 9 if there is none. 
	private static int getNextPreferredCourseAfterPotentialSwap(Student toCheck, int toMask) {
		
		int toReturn = Constants.NUM_PREFS+1;
		
		toCheck.IgnorePrefCourse(toMask);
		
		//check following preferred courses 
		prefLoop:
		for(int i = 0; i< toCheck.prefs.length;i++)
		{
		
			if( toCheck.prefs[i].equals(Constants.NULL_PREF) || toCheck.hasCourse(toCheck.prefs[i]) || i == (toMask-1))
				continue;
			
			//System.out.println(toCheck.prefs[i]+", "+i);
			for(Course c : toCheck.courses){
				if(c.courseConflicts(toCheck.prefs[i])) continue prefLoop;
			}
			
			ArrayList<Course> sections = courses.get(toCheck.prefs[i]);
			
			boolean foundOne = false;
			
			for(Course c : sections)
			{
				if(c.hasRoom() && toCheck.fitsInSchedule(c))
				{
					//System.out.println("FOUND ONE! "+i);
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
		

	//returns a min priority queue of all students in a course
	private static PriorityQueue constructStudentHeapFromCourse(Course section)
	{
		//declare Min Q
		PriorityQueue toReturn = new PriorityQueue(false); 
		
		for(Student stud : section.students)
			toReturn.push(stud.satisfactionScore, stud);
		
		//if(toReturn.isEmpty())
		//	System.out.println("No students found in course: "+section.toString());
		return toReturn;
	}
	
	private static PriorityQueue constructMaxHeapOfAllStudents()
	{
	//Construct heap of students
			PriorityQueue studentsHeap = new PriorityQueue(true); 
			for(HashMap.Entry<Integer, Student> entry : studentsMap.entrySet())
			{
					Student stud = entry.getValue();
					
					if(Driver.doingSeminar)
					{
						if(stud.advisingIsSeminar())
							continue;
						
					}
					
					studentsHeap.push(stud.satisfactionScore, stud);
			}
			
			return studentsHeap;
	}
	
	private static ArrayList<Student> constructArrayListOfAllStudents()
	{
		ArrayList<Student> toReturn = new ArrayList<Student>(); 
		for(HashMap.Entry<Integer, Student> entry : studentsMap.entrySet())
		{
				Student stud = entry.getValue();
				toReturn.add(stud);
		}
		
		return toReturn;
		
	}
	
	private static ArrayList<Student> constructArrayListFromCourse(Course section)
	{
		ArrayList<Student> toReturn = new ArrayList<Student>(); 
		
		for(Student stud : section.students)
		{
			String sectID = section.getID();
			if(stud.courseNotLocked(sectID))
			{
				toReturn.add(stud);	
			}
			
		}
			
		//if(toReturn.isEmpty())
		//	System.out.println("No students found in course: "+section.toString());
		return toReturn;
	}
	
	
	private static Student removeRandomStudent(ArrayList<Student> studs)
	{
		Student toReturn;
		
		int index = rand.nextInt(studs.size());
		
		toReturn = studs.get(index);
		studs.remove(toReturn);
		
		return toReturn;
	}
	
	
	
	
}


