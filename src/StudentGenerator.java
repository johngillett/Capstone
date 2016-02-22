import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class StudentGenerator {

	public static ArrayList<Student> generateStudents(int numStudents, ArrayList<Course> courses)
	{
		ArrayList<Student> students = new ArrayList<Student>();
		
		for(int i = 0; i < numStudents;i++)
		{
			
			Course[] prefs = new Course[Constants.NUM_PREFS];
			
			int[] toShuffle = new int[courses.size()];
			
			for(int x = 0; x < courses.size(); x++)
				toShuffle[x] = x;	
		
			shuffleArray(toShuffle);
			
			for(int y = 0; y < Constants.NUM_PREFS; y ++)
				prefs[y] = courses.get(toShuffle[y]);
		
			students.add(new Student(i,prefs));
			
		}
		
		
		
		
		return students;
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
