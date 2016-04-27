import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
//import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;


/**
 * Reads through a text file containing all courses,
 * writes these courses into a HashMap
 * @author Anna Dovzhik & John Gillett
 *
 */
public class CourseParser {
	static ArrayList<ArrayList<String>> conflictingCourses;

	/**
	 * Parses a set text file assumed to be comma-delimited
	 * with each line describing a course.
	 * 
	 * @param courses an ArrayList of courses we want to update
	 * @param numCourses how many courses we want to add to the course HashMap
	 * @return all the courses we will be working with
	 */
	public static HashMap<String,ArrayList<Course>> parseCourses(ArrayList<Course> courses)
	{
		ArrayList<Course> courseList = new ArrayList<Course>();
		
		try {			
		FileReader input = new FileReader("courses_with_advising.txt");
		BufferedReader bufRead = new BufferedReader(input);
		String nextLine = null;
	
		//skip first line
		bufRead.readLine();	
		
		//int  numSSIs = 0;
		
			//Line Format:
			//0		     1	 2	 3	4	  5	     6	  7	  8	   9  10	11	 12	   13		14		  15     16	   17			
			//Class Nbr,Dept,Crs,Sc,Title,Limit,Enrld,W/L,CO,Type,Units,Days,Times,Facil ID,Component,Term,Session,Advising
			while ( (nextLine = bufRead.readLine()) != null)
			{   
			    String[] courseData = nextLine.split(",");
			    
			    //System.out.println("Course: "+courseData[3]);
			    
			    String title = courseData[4];
			    
			    //System.out.println("Working on: "+title);
			
			    int maxSize = Integer.parseInt(courseData[5]);
			    
			    int curSize =  Integer.parseInt(courseData[6]);
			    
			    //System.out.println("The course size of " + title + " is " + curSize);
			    
			    if(maxSize == 0)
			    	continue;
			    
			    ArrayList<Day> schedule = new ArrayList<Day>();
			    String dayData = courseData[11];
			    String[] timeData = (courseData[12].split("- "));
			    
				//Skip classes with no schedule
			   	if(timeData[0].equals("-"))
			   		continue;	
			    	
			    //System.out.println(Double.parseDouble(timeData[1]));
			   	
			    //parse times
			   	int startTime = Integer.parseInt(timeData[0].substring(0,2)+timeData[0].substring(3,5));
			    int endTime = Integer.parseInt(timeData[1].substring(0,2)+timeData[1].substring(3,5));
			   
			    //parse Days
			   	if(dayData.charAt(1) != '_')
			   		schedule.add(new Day(Day.Slot.M,startTime,endTime));
			   	if(dayData.charAt(2) != '_')
			   		schedule.add(new Day(Day.Slot.T,startTime,endTime));
			   	if(dayData.charAt(3) != '_')
			   		schedule.add(new Day(Day.Slot.W,startTime,endTime));
			   	if(dayData.charAt(4) != '_')
			   		schedule.add(new Day(Day.Slot.Th,startTime,endTime));
			   	if(dayData.charAt(5) != '_')
			   		schedule.add(new Day(Day.Slot.F,startTime,endTime));   
			   	
			   	//parse char Identifiers
			   	String dep = courseData[1];
			   
			   	int cNum = Integer.parseInt(courseData[2].trim());
			   	String sectID = courseData[3];
			   	
			   	int prevID = courseList.size()-1;
			   	
			   	//If we're not on the first line
			   	if(prevID >= 0)
			   	{
				   	Course prev = courseList.get(courseList.size()-1);
				   	
				   	//Check if course has multiple meeting times
				   	if(prev.isSameCourse(dep, cNum) && prev.isSameSection(sectID))
				   	{
				   		prev.addMeetingTime(schedule);
				   		continue;
				   	}
				   	
				   	//Check if this is a Lab
				   	if(prev.isSameCourse(dep, cNum) && sectID.length() == 2)
				   	{
				   		//Section Specific Lab
				   		if(prev.isSameSection(sectID.substring(0,1)))
				   		{
				   			Course newLab = new Course(title,dep,sectID,cNum,schedule,0,maxSize, curSize,false,false);
						    prev.addLab(newLab);
						    continue;
				   		}
				   		//Class specific Lab
				   		else
				   		{
				   			Course newLab = new Course(title,dep,sectID,cNum,schedule,0,maxSize, curSize,false,false);
						    
				   			ArrayList<Course> coursesWithSameLab = new ArrayList<Course>();
				   			
				   			Course toCheck = prev;
				   			
				   			while(toCheck.isSameCourse(dep, cNum))
				   			{
				   				coursesWithSameLab.add(toCheck);
				   				
				   				prevID--;
				   				toCheck = courseList.get(prevID);
				   			}
				   			
				   			addLabLoop:
				   			for(Course cs: coursesWithSameLab)
				   			{
				   				for(Day d1 : cs.schedule)
				   				{
				   					for(Day d2: newLab.schedule)
				   					{
				   						if(!Day.isConflictFree(d1, d2))
				   							continue addLabLoop;
				   						
				   					}
				   				}
				   					
				   					
				   				cs.addLab(newLab);
				   			}
				   			
				   			continue;
				   		}
				   		
				   		
				   	}
				   	
				   	
			   	}
			   	
			   	boolean isSeminar = courseData[8].equals("SSI");
			   	
			   	boolean isAdvising = false;
			   	
			   	//if the isAdvising field is filled in
			   	if (courseData.length == 18)
			   	{
			   		isAdvising = (courseData[17].equals("Y"));
			   	}

			   	//if(isAdvising) System.out.println(title+ " is an advising course");
			   	
			   	//if(isSeminar)
			   		//numSSIs++;
			   	
			    //Add the course!
			   	//(String title, String dep, String sectionID, int cN, ArrayList<Day> schedule, int min, int max, int curSize, boolean isSeminar, boolean isAdvising)
			    Course toAdd = new Course(title,dep,sectID,cNum,schedule,0,maxSize, curSize,isSeminar,isAdvising);
			    courseList.add(toAdd);
			 	
			    //System.out.println(courseID+", "+dayData+", "+timeData[0]+"-"+timeData[1]+", "+title);

			}
		
		//System.out.println("# Seminars: " +numSSIs);	
		
		//Hard-code in BUS 101 as an advising class:
		ArrayList<Day> schedule = new ArrayList<Day>();
		Course toAdd = new Course("Business Leadership Seminar","BUS","A",101,schedule,0,30, 23,false,true);	
		courseList.add(toAdd);

		
		bufRead.close();
		input.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	
		int numCourses = courseList.size();
		
		
		//System.out.println(toReturn.size());
		HashMap<String,ArrayList<Course>> toReturn = new HashMap<String,ArrayList<Course>>();
		
		for(int i = 0; i < numCourses; i ++ )
		{
			Course curC = courseList.get(i);
			String curID = curC.getID();

			if(!toReturn.containsKey(curID))
			{
				toReturn.put(curID, new ArrayList<Course>());
				toReturn.get(curID).add(curC);
				courses.add(curC);
			}
			else
			{
				toReturn.get(curID).add(curC);	
			}
			//toReturn.add(courseList.get(i));
		}
		//System.out.println(reallyReturn.size());
		
		return toReturn;
	}
	

	/**
	 * Using a modified excel sheet that we created, this method
	 * updates course totals to exclude the freshmen that were enrolled during fall 2015
	 * @param courses the courses we are looking at
	 * @return a HashMap that gives the count of freshmen in each course, by ID
	 */
	public static HashMap<String,Integer> updateEnrollmentTotals(HashMap<String,ArrayList<Course>> courses)
	{
		// key is course ID, value is how many freshmen are in the course
		HashMap<String,Integer> freshmenCourseCounts = new HashMap<String, Integer>();
		try
		{
			FileReader input = new FileReader("freshman_counts.txt");
			BufferedReader bufRead = new BufferedReader(input);
			String nextLine = null;
		
			//skip first line
			bufRead.readLine();	
			
			//Line Format:
			//0		         1	             2	 
			//Main Class,  Class Section,  Total
			// CHEM 110,CHEM 110 A,30
			//,CHEM 110 B,28
			
			while ( (nextLine = bufRead.readLine()) != null)
			{   
				String[] courseData = nextLine.split(",");
				String mainClass = courseData[0];
				String classSection = courseData[1];
				int total = Integer.parseInt(courseData[2]);
				
				//if classSection is empty, we're counting course totals 
				//use this info for generating prefs
				if(classSection.isEmpty())
				{
					String[] mainClassArray = mainClass.split(" ");
					String id = mainClassArray[0]+mainClassArray[1];
					if(courses.containsKey(id))
					{
						freshmenCourseCounts.put(id, total);
					}
				}
				
				//else we're counting section totals
				else
				{
					String[] classSectionArray = classSection.split(" ");
					String id = classSectionArray[0]+classSectionArray[1];
					String sectionID = classSectionArray[2];
					//System.out.println(id + " " + sectionID);
					
					//if their courses show up in our courses HashMap
					if(courses.containsKey(id))
					{
						//find the course with this (dept+courseNum)
						ArrayList<Course> coursesToCheck = courses.get(id);
						
						//if this is a lab
						if(sectionID.length()==2)
						{
							//find the course(s) that have this class as a lab			
							for(Course c : coursesToCheck)
							{
								for(Course lab : c.getLabs())
								{
									if (lab.sectionID.equals(sectionID))
									{
										lab.curSize = lab.curSize - total;
									}
								}
							}
						}
						
						//if this is a normal class section
						else
						{
							//find the course with this section
							for(Course c : coursesToCheck)
							{
								if(c.sectionID.equals(sectionID))
								{
									//subtract away the freshmen
									c.curSize = c.curSize - total;
								}
							}
						}
					}
						
				}
				
			}
		bufRead.close();
		input.close();
		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return freshmenCourseCounts;
	}
	
	
	public static void findConflictingCourses(){
		//for now, just make an arrayList of arrayLists,
		//where each arrayList is courses that are mutually conflicting
		conflictingCourses = new ArrayList<ArrayList<String>>();
		
		try{			
			FileReader input = new FileReader("course_conflicts.txt");
			BufferedReader bufRead = new BufferedReader(input);
			String nextLine = null;

			while ( (nextLine = bufRead.readLine()) != null)
			{ 
				String[] line = nextLine.split(",");
				ArrayList<String> lineOfConCourses = new ArrayList<String>();
				for(int i = 0; i < line.length; i++){
					lineOfConCourses.add(line[i]);
				}
				
				conflictingCourses.add(lineOfConCourses);
				
			}
			bufRead.close();
			input.close();
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
//	public static boolean courseConflicts(String course1, String course2)
//	{
//		for(ArrayList<String> list : conflictingCourses){
//			for(String c1 : list){
//				if(c1.equals(course1)){
//					for(String c2 : list){
//						if(c2.equals(course2)) return true;
//					}
//				}
//			}
//		}
//	return false;
//	}
	
	public static void setConflictingCourses(HashMap<String,ArrayList<Course>> courses){
		for(ArrayList<String> list : conflictingCourses){
			for(String c1 : list){
				ArrayList<Course> sections = courses.get(c1);
				for(Course c2 : sections){
					for(String c3 : list){
						c2.addToConflictingCourses(c3);
					}
				}
			}
		}

	}
}
