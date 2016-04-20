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

	private static ArrayList<Integer> simAnnealingList;

	private static ArrayList<Integer> greedyList;
	
	private static int simCounter;
	private static int greedyCounter;
	
	
	public static void init()
	{
		greedyCounter = 0;
		simCounter = 0;
		
		simAnnealingList = new ArrayList<Integer>();
		greedyList = new ArrayList<Integer>();
	}
	
	public static void addGreedyEntry(int entry)
	{
		greedyCounter++;	

		if(greedyCounter < Constants.TRACK_FIDELITY)	
			return;

		greedyList.add(entry);
		greedyCounter = 0;
	}
	
	public static void addSimAnnealEntry(int entry)
	{
		simCounter++;	

		if(simCounter < Constants.TRACK_FIDELITY)	
			return;

		simAnnealingList.add(entry);
		simCounter = 0;
	}
		
	
	
	public static int[] getSimAnnealArray()
	{
		int[] toReturn = new int[simAnnealingList.size()];
		

		for(int i = 0; i < simAnnealingList.size();i++){
			
				toReturn[i] = simAnnealingList.get(i);
			}
		
		return toReturn;
	}

	public static int[] getGreedyArray()
	{
		int[] toReturn = new int[greedyList.size()];
		

		for(int i = 0; i < greedyList.size();i++){
			
				toReturn[i] = greedyList.get(i);
			}
		
		return toReturn;
	}	
	
}
