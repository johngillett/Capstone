import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CourseParser {

	public static ArrayList<Course> parseCourses()
	{
		ArrayList<Course> toReturn = new ArrayList<Course>();

		try {			
		FileReader input = new FileReader("courses.txt");
		BufferedReader bufRead = new BufferedReader(input);
		String nextLine = null;
		int courseID = 0;
		
		//skip first line
		bufRead.readLine();	
			
		int x = 0;
		
			//Line Format:
			//0		1	2	3		4		5		6	7	8	9		10		11	12			13			14		15		16			17		18		19		20
			//Dept, Crs, Sc, Title, Limit, Enrld, W/L, CO, Type, Units, Days, Times, Facil ID, Instructor, Notes, Component, Comb Sect, Acad Org, Term, Session, Career
			while ( (nextLine = bufRead.readLine()) != null && x < 40)
			{   
				x++;
			    String[] courseData = nextLine.split(",");
			    //System.out.println("Course: "+courseData[3]);
			    
			    String title = courseData[3];
			    
			    //System.out.println("Working on: "+title);
			
			    int maxSize = Integer.parseInt(courseData[4]);
			    
			    ArrayList<Day> schedule = new ArrayList<Day>();
			    String dayData = courseData[10];
			    String[] timeData = (courseData[11].split("- "));
			    
				//Skip classes with no schedule
			   	if(timeData[0].equals("-"))
			   		continue;	
			   
			    
			    //System.out.println(timeData[0]+", "+timeData[1]);
			
			    //parse times
			   	int startTime = (int)(Double.parseDouble(timeData[0])* 100);
			    int endTime = (int)(Double.parseDouble(timeData[1])* 100);
			    
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
			   	
			  
			   	if(courseID >= 1 && title.equals(toReturn.get(toReturn.size()-1).getTitle()))
				    continue;
			   	
			    //Course(int id, Day[] sch, int min, int max)
			    Course toAdd = new Course(title,courseID,schedule,0,10);
			    toReturn.add(toAdd);
			 	
			    System.out.println(courseID+", "+dayData+", "+timeData[0]+"-"+timeData[1]+", "+title);
			    
			    
			    
			    //if(courseID >= 2 && title.equals(toReturn.get(toReturn.size()-2).getTitle()))
			    //	continue;
			    	
			    courseID++;
			}
		
		bufRead.close();
		input.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		
		
		
		return toReturn;
	}
	
	
	
}
