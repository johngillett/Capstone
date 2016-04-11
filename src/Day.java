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
	
}
