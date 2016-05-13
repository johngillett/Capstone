import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.TreeMap;

/**
 * @author Anna Dovzhik & John Gillett
 * @version 4.8.16
 * 
 * This class generates students' preferences for 
 * both seminar and regular courses by either
 * (a) randomly selecting courses or 
 * (b) assigning the most popular courses most often as preferences.
 * 
 * It also reads a standard file for students' preferences
 *
 */
public class PreferenceGenerator {

	/**
	 * Generates preferences for given students based on
	 * how many freshmen were enrolled in each course in
	 * Fall 2015
	 * 
	 * @param freshmenCourseCounts the number of freshmen in a given course
	 * @param students the freshmen we are assigning preferences
	 * @param doingSeminar whether we are looking at only seminar courses
	 */
	public static void generatePopPrefs(HashMap<String, Integer> freshmenCourseCounts, HashMap<Integer,Student> students, boolean doingSeminar)
	{
		double averagePop = 0;
		
		int totCount = 0;
		
		for(HashMap.Entry<String, Integer> c : freshmenCourseCounts.entrySet()){
			int count = c.getValue();
			String id = c.getKey();
			if(doingSeminar)
			{
				//if course is Seminar
				if(id.substring(0, 3).equals("SSI"))
				{
					averagePop += count;
					totCount++;
				}
			}
			else{
				//if course isn't seminar
				if(!id.substring(0, 3).equals("SSI")){
					{
						averagePop += count;
						totCount++;
					}
				}
			}

		}	

		averagePop = averagePop/totCount;

		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		
		int currLowerBound = 0;
		
		//Create structure and assign popularity 
		for(HashMap.Entry<String, Integer> c : freshmenCourseCounts.entrySet()){
			String id = c.getKey();
			int count = c.getValue();
			
			//System.out.println(id.substring(0,3)+", count of: "+count);
			
			if(doingSeminar)
			{
				//if course isn't Seminar
				if(!id.substring(0, 3).equals("SSI"))
				{
					//System.out.println("Doing seminars but this ain't a seminar: "+id);
					continue;
				}	
			}
			else
			{
				//if course is seminar
				if(id.substring(0, 3).equals("SSI")){
					//System.out.println("Doing regulars but this ain't regular: "+id);
					continue;
				}
			}
			
			averagePop += count;
	
			map.put(currLowerBound, id);
			currLowerBound += count;
		}	
		
		//random number generator makes random key
		Random rand = new Random();
		int max = currLowerBound;
		
		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
		Student stud = entry.getValue();
			ArrayList<String> prefs = new ArrayList<String>();
			int currPrefIndex = 0;
			while(currPrefIndex < Constants.NUM_PREFS)
			{
				int random = rand.nextInt(max);
				
				Entry<Integer, String> e = map.floorEntry(random);
				if (e != null && e.getValue() == null) 
				{
					e = map.lowerEntry(random);
				}
				String id = e.getValue();
				if(prefs.contains(id))// || stud.hasCourse(id))
				{
					continue;
				}
				prefs.add(id);
				currPrefIndex++;
			}
			
			String[] newPrefs = new String[prefs.size()];
			
			if(doingSeminar)
			stud.semPrefs = prefs.toArray(newPrefs);
			else
			stud.regPrefs = prefs.toArray(newPrefs);
			
			//System.out.println("Student " + stud.id + " has a first preference of " + stud.prefs[7]);
		}
		

		writeCurrentPrefs(students,doingSeminar);
	}
	
	/**
	 * Loads the files with standard preferences and 
	 * sets these preferences to the corresponding students
	 * @param students the freshmen we are assigning preferences
	 */
	public static void getStandardPrefs(HashMap<Integer,Student> students)
	{

		File f;
		
		if(Driver.doingSeminar)
		f = new File("standardStudentSeminarPrefs.txt");
		else
		f = new File("standardStudentRegularPrefs.txt");
		
		try {
			Scanner sc = new Scanner(f);
			
			while(sc.hasNextLine())
			{
				String stud = sc.nextLine();
				
				String[] parsedLine = stud.split(", ");
				
				int studID = Integer.parseInt(parsedLine[0]);
				
				String[] newprefs = new String[Constants.NUM_PREFS];
				
				for(int i = 1; i <= Constants.NUM_PREFS;i++)
				{
					newprefs[i-1] = parsedLine[i];
				}
				
				Student toChange = students.get(studID);
				
				if(Driver.doingSeminar)
					toChange.semPrefs = newprefs;
				else
					toChange.regPrefs = newprefs;
			
				//System.out.println(studID+toChange.prefsToString());
			}
			
			
			sc.close();
			
			deleteStudsWithoutPrefs(students);

		} 
		
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * Loads the files with seminars preferences and 
	 * sets these preferences to the corresponding students
	 * @param students the freshmen we are assigning preferences
	 */
	public static void getRealSemPrefs(HashMap<Integer,Student> students, ArrayList<String> seminarCourses)
	{

		try {			
			FileReader input = new FileReader("seminar_prefs.txt");
			BufferedReader bufRead = new BufferedReader(input);
			String nextLine = null;

			//skip first line
			bufRead.readLine();	
			
			//each line marks a single preference of a student
			while ( (nextLine = bufRead.readLine()) != null)
			{   
				//example line:
				//CSID,    Question,                     Text,                                                      Rank
				//1499454,Seminar in Scholarly Inquiry 1,SSI-1 174: Lethal Othering: Critiquing Genocidal Prejudice,1

				String[] line = nextLine.split(",");

				//get the student
				String id = line[0];
				int studID = Integer.parseInt(id);
				
				//may be the case that a student dropped out before september!
				if(!students.containsKey(studID)) continue;
				
				Student stud = students.get(studID);
				
				//get the course
				String course = line[2];
				String[] courseArray = course.split(" ");
				String courseID = courseArray[0].replace("-", "") + courseArray[1].replace(":", "");

				//get the rank of this course
				int rank = Integer.parseInt(line[3]);

				//System.out.println("Student is " + studID + ", course is "+ courseID + ", rank is " + rank);
				
				//add to the seminar preferences, if this seminar exists
				//this means students may have null preferences
				boolean foundSeminar = false;
				for(String st : seminarCourses){
					if(st.equals(courseID))
					{
						stud.semPrefs[rank-1] = courseID;
						foundSeminar = true;
						break;
					}
				}
				if(!foundSeminar){
					//System.out.println(stud.semPrefs[rank-1]);
					stud.semPrefs[rank-1] = Constants.NULL_PREF;
					//System.out.println("This course is not in our array " + courseID);
				}
				
			}
			

			bufRead.close();
			input.close();
			deleteStudsWithoutPrefs(students);
			writeCurrentPrefs(students,true);

		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	
	/**
	 * Takes the students and their existing preferences, and
	 * writes this information into a file
	 * @param students the students with already assigned preferences
	 * @param doingSeminar whether we are currently working on seminars
	 */
	private static void writeCurrentPrefs(HashMap<Integer,Student> students, boolean doingSeminar)
	{
		File f;
		
		if(doingSeminar)
			f = new File("currentStudentSeminarPrefs.txt");
		else
			f = new File("currentStudentRegularPrefs.txt");
			
		if(!f.exists())
			try 
			{
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			
			for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
				Student stud = entry.getValue();
				writer.write(stud.id+stud.prefsToString(doingSeminar));
				writer.newLine();
			}
			
			
			writer.close();
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//Removes Students who did not submit a list of preferences. 
	public static void deleteStudsWithoutPrefs(HashMap<Integer,Student> students)
	{
		//remove students who didn't input their preferences
		ArrayList<Integer> studsToRemove = new ArrayList<Integer>();
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
		{
			Student student = entry.getValue();
			int studID = entry.getKey();
			//System.out.println(studID + ": " + student.prefsToString(true));
			boolean allNull = true;
			for(int i = 0; i < Constants.NUM_PREFS; i++)
			{
				if(!student.semPrefs[i].equals(Constants.NULL_PREF)){
					allNull = false;
					break;
				}
			}
			
			if(allNull) studsToRemove.add(studID);
		
		}
		
		//delete these students
		for(Integer studID : studsToRemove){
			students.remove(studID);
		}

	}
	
	/**
	 * Assigns students preferences completely by random
	 * @param students the students we are assigning preferences to
	 * @param courses the courses the students can choose from
	 * @param doingSeminar whether we are working on seminars
	 */
	public static void generateRanPrefs(HashMap<Integer,Student> students, ArrayList<String> courses, boolean doingSeminar)
	{
		
		for(HashMap.Entry<Integer, Student> entry : students.entrySet())
		{
			Student st = entry.getValue();
			
			String[] prefs = new String[Constants.NUM_PREFS];
			
			int[] toShuffle = new int[courses.size()];
			
			for(int x = 0; x < courses.size(); x++)
				toShuffle[x] = x;	
		
			shuffleArray(toShuffle);
			
			for(int y = 0; y < Constants.NUM_PREFS; y ++)
			{
				prefs[y] = courses.get(toShuffle[y]);
			}
		
			if(doingSeminar)
			st.semPrefs = prefs;
			else
			st.regPrefs = prefs;
		}
		
	}
	
	  /**
	 * Implementation of Fisher-Yates shuffle
	 * found via http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
	 * @param ar array
	 */
	static void shuffleArray(int[] ar)
	  {
	    // If running on Java 6 or older, use `new Random()` on RHS here
	    Random rnd = ThreadLocalRandom.current();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }
	  
	
}
