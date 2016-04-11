import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Creates new students & randomly places them into advising courses
 * @author Anna Dovzhik & John Gillett
 *
 */
public class StudentGenerator {

	public static HashMap<Integer,Student> generateStudents(ArrayList<Course> advisingCourses)
	{
		HashMap<Integer,Student> students = new HashMap<Integer, Student>();
		Random rand = new Random();
		
		stuLoop:
		for(int i = 0; i < Constants.NUM_STUDENTS;i++)
		{
			String[] prefs = new String[Constants.NUM_PREFS];
			for(int j= 0; j<prefs.length;j++){
				prefs[j] = Constants.NULL_PREF;
			}
			Student stu = new Student(i,prefs);
			students.put(i,stu);
			
			boolean flag = true;
			while(flag)
			{
				int num = rand.nextInt(advisingCourses.size());
				Course c = advisingCourses.get(num);
				if(c.hasRoom() && stu.addIfFitsInSchedule(c))
				{
					stu.hasAdvisingCourse = true;
					stu.lockCourse(c.getID());
					flag = false;
				}
			}
			
			//for(Course c : advisingCourses)
/*			{
				if(c.hasRoom()) 
				{
					System.out.println("Course size for " + c.getID() + " is " + c.curSize + " out of " +  c.maxSize);

					//System.out.println("we're in");
					stu.addIfFitsInSchedule(c);
					//c.addStudent(stu);
					continue stuLoop;
				}
			}*/
			//System.out.print(" NO! ");
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
