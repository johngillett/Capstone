/**
 * This class represents one day (M, T, W, Th, F) 
 * with a given start and end time
 * @author Anna Dovzhik & John Gillett
 *
 */
public class Day{

	enum Slot{M,T,W,Th,F};
	
	Slot day;
	
	int startTime;
	int endTime;
	
	//Constructor
	public Day(Slot day, int st, int end)
	{
		this.day = day;
		this.startTime = st;
		this.endTime = end;	
	}
	
	public String toString()
	{
		return this.day + ": "+this.startTime+"-"+this.endTime;
	}
	
	//Determines if two scheduled days conflict with each other
	public static boolean isConflictFree(Day day1, Day day2)
	{

		if(day1.day != day2.day)
			return true;
		
		if(day2.endTime < day1.startTime)
			return true;
		
		if(day1.endTime < day2.startTime)
			return true;
		
		if((day1.startTime == day2.startTime) || (day2.endTime == day2.endTime))
			return false;
		
		if(!((day1.startTime < day2.startTime && day1.endTime < day2.startTime ) || (day2.startTime < day1.startTime && day2.endTime < day1.startTime)))
			return false;
		
		
		return false;
	}
	
	
}
