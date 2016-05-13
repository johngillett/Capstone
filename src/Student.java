import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents one student, who has an id,
 * two sets of preferences, a satisfaction score, courses, and a schedule
 * 
 * 
 * @author Anna Dovzhik & John Gillett
 *
 */
public class Student{

	int id;
	int satisfactionScore; //to gauge how many top choices are placed
	
	int prefSatScore;
	int regSatScore;
	
	String[] prefs;
	
	String[] semPrefs;
	String[] regPrefs;
	
	int indexOfNextCourseToCheck;
	
	ArrayList<Course> courses;

	ArrayList<String> lockedCourses;
	
	ArrayList<Day> schedule;
	
	ArrayList<Course> toIgnore;
	boolean hasAdvisingCourse;
	
	Course advisingCourse;
	
	String actualSeminar;
	
	//New Constructor
	public Student(int id, String[] prefs)
	{
		this.id = id;
		this.prefs = prefs;

		if(Constants.SAT == Constants.SAT_SCALE.Linear)
		{
			this.satisfactionScore = (Constants.NUM_PREFS+1)*Constants.STUD_COURSE_LIMIT;
		}
		if(Constants.SAT == Constants.SAT_SCALE.Geometric)
		{
			this.satisfactionScore = (int)Math.pow(2,(Constants.NUM_PREFS+1))*Constants.STUD_COURSE_LIMIT;
		}

		this.prefSatScore = this.satisfactionScore;
		this.regSatScore = this.satisfactionScore;

		this.courses = new ArrayList<Course>();	

		this.regPrefs = new String[Constants.NUM_PREFS];
		this.semPrefs = new String[Constants.NUM_PREFS];

		for(int i = 0; i < Constants.NUM_PREFS;i++)
		{
			regPrefs[i] = Constants.NULL_PREF;
			semPrefs[i] = Constants.NULL_PREF;
		}

		this.lockedCourses = new ArrayList<String>();

		this.indexOfNextCourseToCheck = 0;

		this.schedule = new ArrayList<Day>();

		toIgnore = new ArrayList<Course>();

		this.hasAdvisingCourse = false;
	
	}
	
	public boolean hasAdvisingCourse()
	{
		return this.hasAdvisingCourse;
	}
	
	
	//adds course to student's schedule and updates satisfaction score
	public void enrollInCourse(Course c)
	{
	courses.add(c);	
	
	//Add class to Student's schedule
	for(Day cDay: c.schedule)
	{
		this.schedule.add(cDay);
	}
	
	updateSatisfactionScore();
	}
	
	//checks if course has no conflicts with student's schedule, and immediately enrolls them if so
	public boolean addIfFitsInSchedule(Course courseToCheck)
	{	
		
		//can't enroll in a course that would conflict with another course
		for(Course course : courses){
			if(course.courseConflicts(courseToCheck.getID())) return false;
		}
		
		for(Day day1 : schedule)
		{
			for(Day day2 : courseToCheck.schedule)
			{
				if(!Day.isConflictFree(day1, day2))
					return false;
			}
		}
		
		
		if(courseToCheck.hasLab)
		{
			//System.out.println("checking for lab");
			
			ArrayList<Course> labs = courseToCheck.getLabs();
			
			boolean foundLab = false;
			
			for(Course lab : labs)
			{
				if(!lab.hasRoom()){
					//System.out.println("\t No room!");
					continue;
				}
					
				boolean noConflicts = true;	
				
				for(Day day1 : schedule)
				{
					for(Day day2 : lab.schedule)
					{
						if(!Day.isConflictFree(day1, day2))
						{	
						noConflicts = false;
						break;
						}
					
					}
					
					if(!noConflicts)
						break;
					
				}		
				if(noConflicts)
				{	
					foundLab = true;
					
					enrollInCourse(courseToCheck);
					courseToCheck.addStudent(this);
					
					
					enrollInCourse(lab);
					lab.addStudent(this);
					
					
					return true;
					
				}
				
				
			}
			
			if(!foundLab)
			{
				//System.out.println(this.id);
				//System.out.println("\tNo available lab!");
				return false;
			}
		}
		enrollInCourse(courseToCheck);
		courseToCheck.addStudent(this);
		//System.out.println("everything went as planned...");
		return true;
	}
	
	//Removes the student from a given course
	public void unenroll(Course course) {
		// TODO Auto-generated method stub
		
		//System.out.println("\tStarting with: "+this.schedule.toString());
		
		Course[] toCheck = new Course[courses.size()];
		toCheck = courses.toArray(toCheck);
		
		for(Course c : toCheck)
		{
		if(!c.getID().equals(course.getID()))
			continue;
		
		if(!this.courseNotLocked(c.getID()))
			System.out.println("THIS SHOULD NEVER HAPPEN");
		
		c.removeStudent(this);
		this.courses.remove(c);
		
			for(Day d: c.schedule)
			{
				this.schedule.remove(d);
			}
			
			
		}
		
		updateSatisfactionScore();
		//System.out.println("\tEnded with: "+this.schedule.toString());
	}
	
	//Unenrolls the student from a course indicated by its preference number
	public void unenrollFromPrefCourse(int prefNum) {
		// TODO Auto-generated method stub
		
		Course toUnenrollFrom = null;
		for(Course c : this.courses)
		{
			if(c.getID().equals(prefs[prefNum-1]))
			{
				toUnenrollFrom = c;
				break;
			}
			
		}
		
		unenroll(toUnenrollFrom);
		
	}
	
	
	//checks if course has no conflicts with student's schedule
	public boolean fitsInSchedule(Course courseToCheck)
	{	
		for(Day day1 : schedule)
		{
			for(Day day2 : courseToCheck.schedule)
			{
				if(!Day.isConflictFree(day1, day2))
					return false;
			}
		}
		
		
		if(courseToCheck.hasLab)
		{
			ArrayList<Course> labs = courseToCheck.getLabs();
			
			boolean foundLab = false;
			
			for(Course lab : labs)
			{
				if(!lab.hasRoom())
					continue;
			
				boolean noConflicts = true;	
				
				for(Day day1 : schedule)
				{
					for(Day day2 : lab.schedule)
					{
						
						if(!Day.isConflictFree(day1, day2))
						{
						noConflicts = false;
						break;
						}
						
					
					}
					
					if(!noConflicts)
						break;
					
				}		
				if(noConflicts)
				{	
					foundLab = true;
					break;
				}
				
				
			}
			
			if(!foundLab)
				return false;
			
		}
		
		return true;
	}
	
	//Returns if a student is enrolled in a given course
	public boolean hasCourse(String courseID)
	{
		for(Course course : courses)
		{
			if(courseID.equals(course.getID())) return true;
		}
		return false;
	}

	//Returns num of all courses ignoring labs
	public int getClassCount()
	{
		int classCount = 0;
		
		for(Course nL : this.courses)
			if(!nL.isLab)
				classCount++;
		
		return classCount;
	}
	
	//Returns an array of all the remaining courses a student could theoretically be enrolled into, imagining that they aren't enrolled in their least preferred course
	public Course[] getRemainingCompPrefs(HashMap<String,ArrayList<Course>> hashCourses)
	{
		ArrayList<Course> toBuild = new ArrayList<Course>();
		
		String idToSkip = "";
		
		if(Driver.doingSeminar)
		{
			if(this.hasSeminarCourse() && !this.advisingIsSeminar())	
			{
			outerLoop:
				for(int i = prefs.length-1; i >= 0;i--)
				{
					if(Student.isNullPrerence(prefs[i]) || !this.courseNotLocked(prefs[i]))
						continue;
						
					for(Course c : this.courses)
					{
						//if(prefs[i].equals(c.getID()) && this.courseNotLocked(prefs[i]))
							if(prefs[i].equals(c.getID()))
							{
							IgnorePrefCourse(i+1);
							//System.out.println("Ignoring: "+prefs[i]);
							idToSkip = prefs[i];
							break outerLoop;
							}
					}
				}		
			}
		}
		else
		{
			//if they have a full courseload, then we need to ignore their least preferred course
			if(this.getClassCount() >= Constants.STUD_COURSE_LIMIT)
			{

				Course[] ourCourses = new Course[this.courses.size()];
				ourCourses = this.courses.toArray(ourCourses);
				
				outerLoop:
				for(int i = prefs.length-1; i >= 0;i--)
				{
					if(Student.isNullPrerence(prefs[i]) || !this.courseNotLocked(prefs[i]))
						continue;
					
					for(Course c : ourCourses)
					{
						//if(prefs[i].equals(c.getID()) && this.courseNotLocked(prefs[i]))
						if(prefs[i].equals(c.getID()))
						{
						IgnorePrefCourse(i+1);
						idToSkip = prefs[i];
						break outerLoop;
						}
					}
					
				}
				
			}
			
		}
		
		//finding non-enrolled preferred courses that are compatible with their schedule
		prefLoop:
		for(String id : this.prefs)
		{
			if(id.equals(Constants.NULL_PREF) || this.hasCourse(id) || idToSkip.equals(id))
				continue;
			
			//don't add courses that conflict with existing schedule
			for(Course c : courses){
				if(c.courseConflicts(id)) continue prefLoop;
			}
			
			ArrayList<Course> curPrefSections = hashCourses.get(id);
			
			for(Course c : curPrefSections)
			{
				if(fitsInSchedule(c))
				{
					toBuild.add(c);
				}	
			}	
		}
		
		Course[] toReturn = new Course[toBuild.size()];
		toReturn =toBuild.toArray(toReturn);
		
		stopIgnoringCourse();
		
		return toReturn;
	}
	
	
	public String toString()
	{
		String toReturn = "Student: "+id+", Prefs: ";
		
		int i = 0;
		for(i = 0; i < prefs.length;i++)
			toReturn += prefs[i]+", ";
		
		toReturn += " - Enrolled: ";
		
		for(i = 0; i < courses.size();i++)
			toReturn += courses.get(i).getID()+courses.get(i).getSectionID()+", ";
		
		toReturn += " Score: "+satisfactionScore;
		
		return toReturn;
	}

	//Returns the preference number of the given course, if the student does not list it as a preference returns 
	public int getPrefNumber(String courseID) {
		// TODO Auto-generated method stub
		int toReturn = Constants.NUM_PREFS+1;
		int curIndex = 1;
		
		for(String id: this.prefs)
		{
			if(courseID.equals(id))
			{
				toReturn = curIndex;
				break;
			}
			curIndex++;
		}
		return toReturn;
	}

	//Returns the preference number of the given course, if the student does not list it as a preference returns 
	public int getSemPrefNumber(String courseID) {
		// TODO Auto-generated method stub
		int toReturn = Constants.NUM_PREFS+1;
		int curIndex = 1;
		
		for(String id: this.semPrefs)
		{
			if(courseID.equals(id))
			{
				toReturn = curIndex;
				break;
			}
			curIndex++;
		}
		return toReturn;
	}
	
	//returns the preference number of the least preferred course the student is enrolled in. 9 if they are missing at least one course. 
	public int getLastEnrolledPrefNumber()
	{
		if(!Driver.doingSeminar){
		if(this.getClassCount() < Constants.STUD_COURSE_LIMIT)
			return (Constants.NUM_PREFS+1);
		}
		if(Driver.doingSeminar)
		{
			if(!this.hasSeminarCourse())
			{
				return (Constants.NUM_PREFS+1);
			}
		}
		
		for(int i = prefs.length-1; i >= 0;i--)
		{
			for(Course c : this.courses)
			{
				if(c.getID().equals(prefs[i]) && courseNotLocked(c.getID()))
						return i+1;
			}
			
		}
		
		return Constants.NUM_PREFS+1;
	}
	
	//Temporarily unenrolls a student from a course so schedule checking with other courses can be done
	public void IgnorePrefCourse(int toMask) {
		// TODO Auto-generated method stub
		Course[] toCheck = new Course[courses.size()];
		toCheck = courses.toArray(toCheck);
		
		//Take toMask out of our courselist to ignore it
		for(Course c: toCheck)
		{
			if(c.getID().equals(this.prefs[toMask-1]))
			{
				toIgnore.add(c);
				courses.remove(c);
				
				for(Day d: c.schedule)
					schedule.remove(d);
			}
		}
		
		
	}

	//Re-enrolls students in courses we were previously ignoring
	public void stopIgnoringCourse()
	{
		for(Course c : toIgnore)
		{
			this.courses.add(c);

			for(Day d : c.schedule)
				this.schedule.add(d);
		}
		
		toIgnore = new ArrayList<Course>();
	}

	
	//Returns a student's preferred courses in a String
	public String prefsToString(boolean doingSeminar)
	{
		String toReturn = "";
		
		String[] toCheck;
		
		if(doingSeminar)
			toCheck = this.semPrefs;
		else
			toCheck = this.regPrefs;
		
		for(String s: toCheck)
			toReturn += ", "+s;
		
		return toReturn;
		
	}
	
	//Returns if a given course is locked, i.e. cannot be removed 
	public boolean courseNotLocked(String id)
	{
		if(id.equals(Constants.NULL_PREF))
			return true;
		
		for(String lid : lockedCourses)
		{
			
			if(lid.equals(id))
			{
				//System.out.println(lid + " is locked.");
				return false;
			}
		}
		return true;
	}
	
	//Locks in a course as a final decision
	public void lockCourse(String id)
	{
		lockedCourses.add(id);
	}
	
	
	public static boolean isNullPrerence(String name)
	{
		return name.equals(Constants.NULL_PREF);
	}
	
	//Re-calculates this student's satisfaction score
	private void updateSatisfactionScore()
	{
		int newScore = 0;
		
		for(int i = 0; i < prefs.length; i++)
		{
			for(Course co : courses)
			{
				if(co.isLab)
					continue;
				
				if(this.hasAdvisingCourse && co.isSameCourse(this.advisingCourse))
						continue;
				
				if(co.isSameCourse(regPrefs[i]))	
				{
					if(Constants.SAT == Constants.SAT_SCALE.Geometric)
					{
						newScore += (int) Math.pow(2,i+1);
					}
					else if(Constants.SAT == Constants.SAT_SCALE.Linear)
					{
						newScore += i+1;
					}
				}
				if(co.isSameCourse(semPrefs[i]))	
				{
					if(Constants.SAT == Constants.SAT_SCALE.Geometric)
					{
						newScore += (int) Math.pow(2,i+1);
					}
					else if(Constants.SAT == Constants.SAT_SCALE.Linear)
					{
						newScore += i+1;
					}
				}
				
			}
		}
		
		int classCount = this.getClassCount();
		
		if(classCount < Constants.STUD_COURSE_LIMIT)
		{
			if(Constants.SAT == Constants.SAT_SCALE.Geometric)
			{
				newScore += (4 - classCount) * Math.pow(2, (Constants.NUM_PREFS+1));
			}
			else if(Constants.SAT == Constants.SAT_SCALE.Linear)
			{
				newScore += (4 - classCount) * (Constants.NUM_PREFS+1);
			}
		}
		this.satisfactionScore = newScore;
	}
	
		
	public boolean advisingIsSeminar(){
		if(this.hasAdvisingCourse){
			return this.advisingCourse.isSeminar();
		}
		return false;
	}
	
	public boolean hasSeminarCourse()
	{
		for(Course c : courses)
		{
			if(c.isSeminar())
				return true;
		}
		
		return false;
	}
	
	public void setActualSeminar(String c)
	{
		this.actualSeminar = c;
	}
	
	public String getActualSeminar()
	{
		return this.actualSeminar;
	}
	
	public String getEnrollmentString()
	{
		String toReturn ="\n\t";
		
		if(this.hasAdvisingCourse)
		{
			Course advisingCourse = this.courses.get(0);
			if(!advisingCourse.isSeminar())
			toReturn += "Advising Course: "+advisingCourse.getID()+advisingCourse.getSectionID()+": "+advisingCourse.schedToString();
			else
			toReturn += "Advising/Sem Course: "+advisingCourse.getID()+advisingCourse.getSectionID()+ ": "+advisingCourse.schedToString();
				
			
		}
		
		toReturn += "\n\t";
		
		for(Course c: this.courses)
		{
			if(c.isSeminar() && !c.isAdvising)
			{
				toReturn += "Seminar: "+c.getID()+c.getSectionID()+": "+ c.schedToString();
				break;
			}
		}
		toReturn += "\n\tRemaining: ";
		
		for(Course c: this.courses)
		{
			
			if(!c.isSeminar() && !c.isAdvising())
			{
				toReturn += c.getID()+c.getSectionID()+": "+c.schedToString()+",";
			}
		}
		
		return toReturn;
		
	}
	

	
	
}
