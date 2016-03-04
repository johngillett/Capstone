import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.TreeMap;

public class PreferenceGenerator {

	public static void generatePopPrefs(HashMap<String, Integer> freshmenCourseCounts, HashMap<Integer,Student> students)
	{
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
				
		int currLowerBound = 0;
		for(HashMap.Entry<String, Integer> c : freshmenCourseCounts.entrySet()){
			String id = c.getKey();
			int count = c.getValue();
			
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
				if(prefs.contains(id))
				{
					continue;
				}
				prefs.add(id);
				currPrefIndex++;
			}
			
			String[] newPrefs = new String[prefs.size()];
			stud.prefs = prefs.toArray(newPrefs);
			//System.out.println("Student " + stud.id + " has a first preference of " + stud.prefs[7]);
		}
		

		
	}
	
	public static void generateRanPrefs(HashMap<Integer,Student> students, ArrayList<Course> courses)
	{
		for(HashMap.Entry<Integer, Student> entry : students.entrySet()){
			Student st = entry.getValue();
			
			String[] prefs = new String[Constants.NUM_PREFS];
			
			int[] toShuffle = new int[courses.size()];
			
			for(int x = 0; x < courses.size(); x++)
				toShuffle[x] = x;	
		
			shuffleArray(toShuffle);
			
			for(int y = 0; y < Constants.NUM_PREFS; y ++)
				prefs[y] = courses.get(toShuffle[y]).getID();
		
			//students.add(new Student(i,prefs));
			st.prefs = prefs;
			
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
