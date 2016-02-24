import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
//import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;

public class CourseParser {

	public static HashMap<String,ArrayList<Course>> parseCourses(ArrayList<Course> courses,int numCourses)
	{
		ArrayList<Course> courseList = new ArrayList<Course>();
		
		try {			
		FileReader input = new FileReader("courses.txt");
		BufferedReader bufRead = new BufferedReader(input);
		String nextLine = null;
	
		//skip first line
		bufRead.readLine();	
		
			//Line Format:
			//0		1	2	3		4		5		6	7	8	9		10		11	12			13			14		15		16			17		18		19		20
			//Dept, Crs, Sc, Title, Limit, Enrld, W/L, CO, Type, Units, Days, Times, Facil ID, Instructor, Notes, Component, Comb Sect, Acad Org, Term, Session, Career
			while ( (nextLine = bufRead.readLine()) != null)
			{   
			    String[] courseData = nextLine.split(",");
			    //System.out.println("Course: "+courseData[3]);
			    
			    String title = courseData[3];
			    
			    //System.out.println("Working on: "+title);
			
			    int maxSize = Integer.parseInt(courseData[4]);
			    
			    if(maxSize == 0)
			    	continue;
			    
			    ArrayList<Day> schedule = new ArrayList<Day>();
			    String dayData = courseData[10];
			    String[] timeData = (courseData[11].split("- "));
			    
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
			   	String dep = courseData[0];
			   
			   	int cNum = Integer.parseInt(courseData[1].trim());
			   	String sectID = courseData[2];
			   	
			   	int prevID = courseList.size()-1;
			   	
			   	//If We're not on the first line
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
				   			Course newLab = new Course(title,dep,sectID,cNum,schedule,0,Constants.COURSE_SEATS);
						    prev.addLab(newLab);
						    continue;
				   		}
				   		//Class specific Lab
				   		else
				   		{
				   			Course newLab = new Course(title,dep,sectID,cNum,schedule,0,Constants.COURSE_SEATS);
						    
				   			ArrayList<Course> coursesWithSameLab = new ArrayList<Course>();
				   			
				   			Course toCheck = prev;
				   			
				   			while(toCheck.isSameCourse(dep, cNum))
				   			{
				   				coursesWithSameLab.add(toCheck);
				   				
				   				prevID--;
				   				toCheck = courseList.get(prevID);
				   			}
				   			
				   			for(Course cs: coursesWithSameLab)
				   			{
				   				cs.addLab(newLab);
				   			}
				   			
				   			continue;
				   		}
				   		
				   		
				   	}
				   	
				   	
			   	}
			   	
			    //(String title, String dep, String sectionID, int cN, ArrayList<Day> schedule, int min, int max)
			    Course toAdd = new Course(title,dep,sectID,cNum,schedule,0,Constants.COURSE_SEATS);
			    courseList.add(toAdd);
			 	
			    //System.out.println(courseID+", "+dayData+", "+timeData[0]+"-"+timeData[1]+", "+title);

			}
		
		bufRead.close();
		input.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		if(Constants.PARSE_ALL_COURSES)
		{	
			numCourses = courseList.size();
		}
		else
		{
		Collections.shuffle(courseList);
		}
		
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
	
	 
	
}
