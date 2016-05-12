
import java.util.ArrayList;
import java.util.HashMap;

//Class for Storing solution information. Currently Only stores the total satisfaction score. 
public class Solution {

	private int totSatScore;
	
	//Constructor
	public Solution(HashMap<String,ArrayList<Course>> inCourses, HashMap<Integer,Student> inStudents, int inScore)
	{
		
		this.totSatScore = inScore;
		
	}
	
	public int getScore()
	{
		return this.totSatScore;
	}
	
	

}
