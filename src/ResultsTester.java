import java.util.ArrayList;
import java.util.HashMap;

public class ResultsTester {

	public static void verifySchedules(HashMap<Integer,Student> students){
	
	boolean noConflicts = true;
	
	for(HashMap.Entry<Integer, Student> c : students.entrySet()){
		Student stud = c.getValue();
	
		if(!schedConflictFree(stud.courses))
		{
			System.out.println("Conflict in student "+stud.id+"'s schedule!: "+stud.toString());
			noConflicts = false;
		}
		
	}
	
		if(noConflicts)
		{
		System.out.println("Double checked for conflicts in all Student's schedules, none found.");		
		}
	
	}
	
	
	private static boolean schedConflictFree(ArrayList<Course> courses)
	{				
		for(int x = 0; x < courses.size(); x ++)
		{
			Course c1 = courses.get(x);
			
			for(int y = 0; y < courses.size();y++)
			{
				if(y == x)
					continue;
				
				Course c2 = courses.get(y);
				
				for(Day d1 : c1.schedule)
				{
					for(Day d2 : c2.schedule)
					{
						if(d1.day == d2.day)
						{
							if((d1.startTime < d2.endTime && d1.startTime > d2.startTime) || (d1.endTime > d2.startTime && d1.endTime < d2.endTime))
							{
								//Conflict!
								System.out.println("Conflict found between courses: "+c1.getID()+c1.getSectionID()+" and "+c2.getID()+c2.getSectionID());
								System.out.println("\t"+d1.toString()+", "+d2.toString());
								return false;
							}
							
							if(d1.startTime < d2.startTime && !(d1.endTime < d2.startTime))
							{
								//Conflict!
								System.out.println("Conflict found between courses: "+c1.getID()+c1.getSectionID()+" and "+c2.getID()+c2.getSectionID());
								System.out.println("\t"+d1.toString()+", "+d2.toString());
								return false;
							}
							
							if(d1.endTime > d2.endTime && !(d1.startTime > d2.endTime))
							{
								//Conflict!
								System.out.println("Conflict found between courses: "+c1.getID()+c1.getSectionID()+" and "+c2.getID()+c2.getSectionID());
								System.out.println("\t"+d1.toString()+", "+d2.toString());
								return false;
							}
							
							
							
						}
					}
				}
			}
		}		
		return true;
	}
}
