import java.util.ArrayList;

/**
 * A helper class that keeps track of the total satisfaction score
 * as our simulated annealing algorithm runs
 * 
 * @author Anna Dovzhik & John Gillett
 * @version 4.8.16
 *
 */
public class AlgTracker {

	private static ArrayList<Integer> list;
	
	private static int counter;
	
	public static void init()
	{
		counter = 0;
		list = new ArrayList<Integer>();
	}
	
	public static void addEntry(int entry)
	{
		counter++;	

		if(entry == 14000)
		{
			//list.add(entry);
			return;
		}

		if(counter < Constants.TRACK_FIDELITY)	
			return;

		list.add(entry);
		counter = 0;
	}
	
	public static int[] getArray()
	{
		int[] toReturn = new int[list.size()];
		

		for(int i = 0; i < list.size();i++){
			
				toReturn[i] = list.get(i);
			}
		
		return toReturn;
	}

	
	
}
