import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.TreeMap;

public class PreferenceGenerator {

	public static void generatePopPrefs(HashMap<String, Integer> freshmenCourseCounts, HashMap<Integer,Student> students, boolean doingSeminar)
	{
		double averagePop = 0;
		
		int totCount = 0;
		
		for(HashMap.Entry<String, Integer> c : freshmenCourseCounts.entrySet()){
			int count = c.getValue();
			String id = c.getKey();
			if(doingSeminar){
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
			
			if(doingSeminar){
			//if course isn't Seminar
			if(!id.substring(0, 3).equals("SSI"))
			{
				//System.out.println("Doing seminars but this ain't a seminar: "+id);
				continue;
			}	
			}
			else{
			//if course is seminar
			if(id.substring(0, 3).equals("SSI")){
				//System.out.println("Doing regulars but this ain't regular: "+id);
				continue;
			}
			}
			
			if(count >= averagePop)
			{
				int toAdd =(int)(count * Constants.POP_SCALE_FACTOR);
				count += toAdd;
			}
			else 
			{
				int toAdd =(int)(count * Constants.POP_SCALE_FACTOR);
				count -= toAdd;
				
				if(count < 1)
					count = 1;
				
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
				if(prefs.contains(id) || stud.hasCourse(id))
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
	
	public static void getStandardPrefs(HashMap<Integer,Student> students)
	{

		File f = new File("standardStudentsInfo.txt");
		
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
				
				toChange.prefs = newprefs;
				
				//System.out.println(studID+toChange.prefsToString());
				
				
			}
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private static void writeCurrentPrefs(HashMap<Integer,Student> students, boolean doingSeminar)
	{
		File f;
		
		if(doingSeminar)
			f = new File("currentStudentSeminarsPrefs.txt");
		else
			f = new File("currentStudentRegularPrefs.txt");
			
		if(!f.exists())
			try {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void generateRanPrefs(HashMap<Integer,Student> students, ArrayList<String> courses, boolean doingSeminar)
	{
		
		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
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
		
			//students.add(new Student(i,prefs));
			if(doingSeminar)
			st.semPrefs = prefs;
			else
			st.regPrefs = prefs;
		}
		
	}
	
	
	  // Implementing Fisher–Yates shuffle
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
