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

	private static ArrayList<Integer> simAnnealingSemList;
	private static ArrayList<Integer> simAnnealingRegList;

	private static ArrayList<Integer> greedyList;
	
	private static int simCounter;
	private static int greedyCounter;
	
	//Initializes AlgTracker
	public static void init()
	{
		greedyCounter = 0;
		simCounter = 0;
		
		simAnnealingSemList = new ArrayList<Integer>();
		simAnnealingRegList = new ArrayList<Integer>();
		
		greedyList = new ArrayList<Integer>();
	}
	
	//Records every TRACK_FIDELITY entries 
	public static void addGreedyEntry(int entry)
	{
		greedyCounter++;	

		if(greedyCounter < Constants.TRACK_FIDELITY)	
			return;

		greedyList.add(entry);
		greedyCounter = 0;
	}
	
	//Records every TRACK_FIDELITY entries 
	public static void addSimAnnealEntry(int entry)
	{
		
		simCounter++;	
		
		if(simCounter < Constants.TRACK_FIDELITY)	
			return;
		
		if(!Driver.doingSeminar)
			simAnnealingRegList.add(entry);
		else
		{
			//System.out.println(entry-Constants.TOT_SAT_TO_SEM_MOD);
			simAnnealingSemList.add(entry);//-Constants.TOT_SAT_TO_SEM_MOD);
		}
		simCounter = 0;
	}
		
	
	
	public static int[] getSimAnnealRegArray()
	{
		int[] toReturn = new int[simAnnealingRegList.size()];
		

		for(int i = 0; i < simAnnealingRegList.size();i++){
			
				toReturn[i] = simAnnealingRegList.get(i);
			}
		
		return toReturn;
	}

	public static int[] getSimAnnealSemArray()
	{
		int[] toReturn = new int[simAnnealingSemList.size()];
		

		for(int i = 0; i < simAnnealingSemList.size();i++){
			
				toReturn[i] = simAnnealingSemList.get(i);
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
