import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class reads a text file containing all actual Fall 2015 freshmen
 * and returns a HashMap of all freshmen, organized by student ID
 * @author Anna Dovzhik & John Gillett
 *
 */
public class FreshmanParser {
	
	/**
	 * Reads all freshmen from a text file, returns a HashMap of the students.
	 * Automatically adds the students to their advising courses.
	 * 
	 * @param courses the possible courses the students can be enrolled in
	 * @return a HashMap of all students
	 */
	public static HashMap<Integer,Student> parseFreshmen(HashMap<String,ArrayList<Course>> courses)
	{
		HashMap<Integer, Student> freshmen = new HashMap<Integer,Student>();
		
		try {			
		FileReader input = new FileReader("freshmen.txt");
		BufferedReader bufRead = new BufferedReader(input);
		String nextLine = null;
	
		//skip first line
		bufRead.readLine();	
		
			//Line Format:
			//0	   1  
			//ID   Class
		
			//one line = one class in a student's schedule
			while ( (nextLine = bufRead.readLine()) != null)
			{   
			    String[] data = nextLine.split(",");
			    
			    //create student
			    int id = Integer.parseInt(data[0]);
			   
			    if (!freshmen.containsKey(id))
			    {
			    	//create a student with null preferences
			    	String[] prefs = new String[Constants.NUM_PREFS];
			    	
			    	for(int i= 0; i <prefs.length; i++)
			    	{
			    		prefs[i] = Constants.NULL_PREF;
			    	}
			    	Student newStudent = new Student(id, prefs);
			    	freshmen.put(id, newStudent);
			    	//System.out.println("Student with id " +id+ " was added");
			    }
			    
			    //find seminar course
			    String[] course = data[1].split(" ");
			    // course = "DEPT  NUM"
			    String dept = course[0];
			    String courseID = course[0]+course[2];
			    String section = course[3];
			    
			    //if this course was actually in our spreadsheet
			    if(courses.containsKey(courseID))
			    {
			    	//find their actual seminar course
		    		if(dept.equals("SSI1"))
		    		{
		    			Student currStudent = freshmen.get(id);
		    			currStudent.setActualSeminar(courseID);
		    		}
		    		
			    }

			    			   	
			}
		
		bufRead.close();
		input.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return freshmen;
	}
	
	
	public static void setAdvisingCourses(HashMap<Integer,Student> students, HashMap<String, ArrayList<Course>> courses){
		try {			
		FileReader input = new FileReader("freshmen_advising.txt");
		BufferedReader bufRead = new BufferedReader(input);
		String nextLine = null;
	
		//skip first line
		bufRead.readLine();	
		
			//Line Format:
			//0	     1  
			//CSID   Advising Class
		
			//Typical Line:
			//6730910,ARTS 101A
		
			//one line = one student's advising class
			while ( (nextLine = bufRead.readLine()) != null)
			{   
			    String[] data = nextLine.split(",");
			    
			    int id = Integer.parseInt(data[0]);
			  
			    //skip those students who didn't input questionnaire responses
			    if(!students.containsKey(id)) continue; 
			    
			    Student curr = students.get(id);
			    
			    //find advising course
			    String[] courseArray = data[1].split(" ");
			    // course = "DEPT  NUM+SEC"
			    String dept = courseArray[0];
			    String courseID = courseArray[1].substring(0, 3);
			    String sectionID = courseArray[1].substring(3, 4);

			    //add the actual course to the student's schedule
			    ArrayList<Course> courseArrayList = courses.get(dept+courseID);
			    advisingLoop:
			    for(Course sec : courseArrayList){
			    	if(sec.isSameSection(sectionID)){
			    		if(curr.addIfFitsInSchedule(sec)){
			    			curr.advisingCourse = sec;
			    			curr.hasAdvisingCourse = true;
			    			curr.lockCourse(sec.getID());
			    			break advisingLoop;
			    		}
			    	}
			    }
			    
			    			   	
			}
		
		bufRead.close();
		input.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	 
}
