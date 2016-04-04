import java.util.ArrayList;

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
