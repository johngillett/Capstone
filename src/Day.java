import java.io.Serializable;

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
	
	public static boolean isConflictFree(Day day1, Day day2)
	{
		if(day1.day != day2.day)
			return true;
		
		if(day2.endTime < day1.startTime)
			return true;
		
		if(day1.endTime < day2.startTime)
			return true;
		
		if((day1.startTime < day2.endTime && day1.startTime > day2.startTime) || (day1.endTime > day2.startTime && day1.endTime < day2.endTime))
			return false;

		if(day1.startTime == day2.startTime)
			return false;
		
		if(day1.endTime == day2.endTime)
			return false;
		
		
		//if one course starts & ends before another, then it is compatible, otherwise it isn't
		//if(!((day1.startTime < day2.startTime && day1.endTime < day2.startTime ) || (day2.startTime < day1.startTime && day2.endTime < day1.startTime)))
		//	return false;
		
		if((day1.startTime < day2.endTime && day1.startTime > day2.startTime) || (day1.endTime > day2.startTime && day1.endTime < day2.endTime))
			return false;
		
		if(day2.endTime < day1.endTime && day2.endTime > day1.startTime) 
			return false;
		
		if(day2.endTime > day1.startTime && day2.startTime < day1.startTime)
			return false;
		
		
		//4 POSSIBLE CONFLICTS:
		if(day2.startTime < day1.endTime && day2.endTime > day1.endTime)
			return false;
		
		if(day1.startTime < day2.endTime && day1.endTime > day2.endTime)
			return false;
		
		if(day1.startTime < day1.startTime && day1.endTime > day2.endTime)
			return false;
		
		if(day2.startTime < day1.startTime && day2.endTime > day1.endTime)
			return false;
		
		
		
		return false;
	}
	
	
}
