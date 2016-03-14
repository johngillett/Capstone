
import java.util.ArrayList;
import java.util.HashMap;

public class Student{

	int id;
	int satisfactionScore; //to gauge how many top choices are placed
	String[] prefs;
	int indexOfNextCourseToCheck;
	
	ArrayList<Course> courses;

	ArrayList<Day> schedule;
	
	ArrayList<Course> toIgnore;
	
	//New Constructor
	public Student(int id, String[] prefs)
	{
	this.id = id;
	this.prefs = prefs;
		
	//this.satisfactionScore = Integer.MAX_VALUE;
	if(Constants.SAT == Constants.SAT_SCALE.Linear)
		{
			this.satisfactionScore = (Constants.NUM_PREFS+1)*Constants.STUD_COURSE_LIMIT;
		}
	if(Constants.SAT == Constants.SAT_SCALE.Geometric)
	{
		this.satisfactionScore = (int)Math.pow(2,(Constants.NUM_PREFS+1))*Constants.STUD_COURSE_LIMIT;
	}
	this.courses = new ArrayList<Course>();	
	
	this.indexOfNextCourseToCheck = 0;
	
	this.schedule = new ArrayList<Day>();
	
	toIgnore = new ArrayList<Course>();
	
	
	}

	//Copy Constructor
//	public Student(Student st)
//	{
//	this.id = st.id;
//	this.prefs = st.prefs;
//	this.satisfactionScore = st.satisfactionScore;
//	this.indexOfNextCourseToCheck = st.indexOfNextCourseToCheck;
//	
//	this.courses = new ArrayList<Course>();
//	
//	for(Course c: st.courses)
//		this.courses.add(c);
//	
//	this.schedule
//	
//	for(Day d: st.schedule)
//		this.
//		
//	}
	
	//adds course to student's schedule and updates satisfaction score
	public void enrollInCourse(Course c)
	{
	courses.add(c);	
	
	//Add class to Student's schedule
	for(Day cDay: c.schedule)
	{
		this.schedule.add(cDay);
	}
	
	
	int newScore = 0;
	
	int classCount = 0;
	
	for(int i = 0; i < prefs.length; i++)
	{
		for(Course co : courses)
		{
			if(co.isLab)
				continue;
			
			if(co.isSameCourse(prefs[i]))	
			{
				classCount++;
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
	
	if(classCount < 4)
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
	
	//checks if course has no conflicts with student's schedule
	public boolean addIfFitsInSchedule(Course courseToCheck)
	{	
		//System.out.println(courseToCheck);
		//System.out.println(this);
		
		for(Day day1 : schedule)
		{
			for(Day day2 : courseToCheck.schedule)
			{
				//if course falls on same day
				if(day1.day.equals(day2.day))
				{
					//if one course starts & ends before another, then it is compatible, otherwise it isn't
					if(!((day1.startTime < day2.startTime && day1.endTime < day2.startTime ) || (day2.startTime < day1.startTime && day2.endTime < day1.startTime)))
					{	
						//System.out.println("\tMain course conflict!");
						return false;
					}
				}
			}
		}
		
		
		if(courseToCheck.hasLab)
		{
			//System.out.println("checking for lab");
			
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
						//if course falls on same day
						if(day1.day.equals(day2.day))
						{
							//if one course starts & ends before another, then it is compatible, otherwise it isn't
							if(((day1.startTime < day2.startTime && day1.endTime < day2.startTime ) || (day2.startTime < day1.startTime && day2.endTime < day1.startTime)))
							{	
							noConflicts = false;
							break;
							}
						}
					
					}
					
					if(!noConflicts)
						break;
					
				}		
				if(noConflicts)
				{	
					foundLab = true;
					enrollInCourse(lab);
					lab.addStudent(this);
					break;
				}
				
				
			}
			
			if(!foundLab)
			{

				//System.out.println("\tNo available lab!");
				return false;
			}
		}
		
		//System.out.println("everything went as planned...");
		courseToCheck.addStudent(this);
		enrollInCourse(courseToCheck);
		return true;
	}
	
	public void unenroll(Course course) {
		// TODO Auto-generated method stub
		
		//System.out.println("\tStarting with: "+this.schedule.toString());
		
		Course[] toCheck = new Course[courses.size()];
		toCheck = courses.toArray(toCheck);
		
		for(Course c : toCheck)
		{
		if(!c.getID().equals(course.getID()))
			continue;
		
		c.removeStudent(this);
		this.courses.remove(c);
		
			for(Day d: c.schedule)
			{
				this.schedule.remove(d);
			}
			
			
		}
		

		//System.out.println("\tEnded with: "+this.schedule.toString());
	}
	
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
				//if course falls on same day
				if(day1.day.equals(day2.day))
				{
					//if one course starts & ends before another, then it is compatible, otherwise it isn't
					if(!((day1.startTime < day2.startTime && day1.endTime < day2.startTime ) || (day2.startTime < day1.startTime && day2.endTime < day1.startTime)))
						return false;
				}
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
						//if course falls on same day
						if(day1.day.equals(day2.day))
						{
							//if one course starts & ends before another, then it is compatible, otherwise it isn't
							if(((day1.startTime < day2.startTime && day1.endTime < day2.startTime ) || (day2.startTime < day1.startTime && day2.endTime < day1.startTime)))
							{	
							noConflicts = false;
							break;
							}
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
	
	public Course[] getRemainingCompPrefs(HashMap<String,ArrayList<Course>> hashCourses)
	{
		ArrayList<Course> toBuild = new ArrayList<Course>();
		
		if(this.getClassCount() >= Constants.STUD_COURSE_LIMIT)
		{

			Course[] ourCourses = new Course[this.courses.size()];
			ourCourses = this.courses.toArray(ourCourses);
			
			outerLoop:
			for(int i = prefs.length-1; i >= 0;i--)
			{
				
				for(Course c : ourCourses)
				{
					if(prefs[i].equals(c.getID()))
					{
					IgnorePrefCourse(i+1);
					break outerLoop;
					}
				}
				
			}
			
		}
		
		for(String id : this.prefs)
		{
			if(this.hasCourse(id))
				continue;
			
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

	//Returns the preference number of the given course, if the student does not list it as a preference returns 9
	public int getPrefNumber(String courseID) {
		// TODO Auto-generated method stub
		int toReturn = 9;
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

	//returns the preference number of the least preferred course the student is enrolled in. 9 if they are missing at least one course. 
	public int getLastEnrolledPrefNumber()
	{
		if(this.getClassCount() < Constants.STUD_COURSE_LIMIT)
			return 9;
		
		for(int i = prefs.length-1; i >= 0;i--)
		{
			for(Course c : this.courses)
			{
				if(c.getID().equals(prefs[i]))
						return i+1;
				
			}
			
		}
		
		return 9;
	}
	
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

	

	

	
}
