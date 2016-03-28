import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class FreshmanParser {
	
	public static HashMap<Integer,Student> parseFreshmen(HashMap<String,ArrayList<Course>> courses)
	{
		HashMap<Integer, Student> freshmen = new HashMap<Integer,Student>();
		
		try {			
		FileReader input = new FileReader("freshmen.txt");
		BufferedReader bufRead = new BufferedReader(input);
		String nextLine = null;
	
		//skip first line
		bufRead.readLine();	
		
		//number of students with advising courses
		int numAdvisees = 0;
		
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
			    	//create a student with no preferences
			    	Student newStudent = new Student(id, new String[Constants.NUM_PREFS]);
			    	freshmen.put(id, newStudent);
			    	//System.out.println("Student with id " +id+ " was added");
			    }
			    
			    //find advising course
			    String[] course = data[1].split(" ");
			    //course[0] = subject, course[1] = space, course[2] = courseNum, course[3] = section
			    String courseID = course[0]+course[2];
			    String section = course[3];
			    
			    //if this course was actually in our spreadsheet
			    if(courses.containsKey(courseID))
			    {
				    ArrayList<Course> possibleCourses = courses.get(courseID);
				    //for all sections
				    for(Course courseSec : possibleCourses)
				    {
				    	//if we found the section the student is in
				    	if(courseSec.getSectionID().equals(section))
				    	{
				    		if (courseSec.isAdvising())
				    		{
				    			//System.out.println(courseSec.getTitle() + " is " + data[0] + "'s advising course");
				    			Student currStudent = freshmen.get(id);
				    			//if(!currStudent.hasCourse(courseSec.getID()))
				    			if(!currStudent.hasAdvisingCourse())
				    			{
				    				currStudent.enrollInAdvisingCourse(courseSec);
				    				currStudent.hasAdvisingCourse = true;
				    				//System.out.println("student " + data[0] + " has an advising course: " + currStudent.hasAdvisingCourse());
				    				numAdvisees ++;
				    			}
				    		}
				    	}
				    }
			    }

			    			   	
			}
			
		System.out.println("The number of students in an advising course is " + numAdvisees);
		
		//finding students without an advising course
		int numStu = 0;
		for(HashMap.Entry<Integer, Student> c : freshmen.entrySet()){
			Student stud = c.getValue();
			if(stud.getClassCount() == 0){
				numStu++;
				System.out.println(stud);
			}
		}
		System.out.println("There " + numStu + " students without advising classes");
			
		
		bufRead.close();
		input.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return freshmen;
	}
	
	 
}
