import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class FreshmanParser {
	
	public static HashMap<Integer,Student> parseFreshmen()
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
			    
			    int id = Integer.parseInt(data[0]);
			    if (!freshmen.containsKey(id))
			    {
			    	//create a student with no preferences
			    	Student newStudent = new Student(id, new String[Constants.NUM_PREFS]);
			    	//System.out.println("Student with id " +id+ " was added");
			    	freshmen.put(id, newStudent);
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
	
	 
}
