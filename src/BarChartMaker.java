import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 

public class BarChartMaker extends ApplicationFrame
{
   public BarChartMaker( String applicationTitle , String chartTitle, String xAxis, String yAxis, int[] prefCount, int toAdd )
   {
      super( applicationTitle );        
      JFreeChart barChart = ChartFactory.createBarChart(
         chartTitle,           
         xAxis,            
         yAxis,            
         createDataset(prefCount, toAdd),          
         PlotOrientation.VERTICAL,           
         true, true, false);
         
      ChartPanel chartPanel = new ChartPanel( barChart );        
      chartPanel.setPreferredSize(new java.awt.Dimension( 1500 , 1000 ) );        
      setContentPane( chartPanel ); 
   }
   

   private CategoryDataset createDataset( int[] count, int toAdd)
   {
	   final DefaultCategoryDataset dataset = new DefaultCategoryDataset( ); 

	   // row keys...
       final String series1 = "Number of Students";

//       // column keys...
//       final String category1 = "1";
//       final String category2 = "2";
//       final String category3 = "3";
//       final String category4 = "4";
//       final String category5 = "5";
//       final String category6 = "6";
//       final String category7 = "7";
//       final String category8 = "8";
//       
//       dataset.addValue(prefCount[0], series1, "1");
//       dataset.addValue(prefCount[1], series1, category2);
//       dataset.addValue(prefCount[2], series1, category3);
//       dataset.addValue(prefCount[3], series1, category4);
//       dataset.addValue(prefCount[4], series1, category5);
//       dataset.addValue(prefCount[5], series1, category6);
//       dataset.addValue(prefCount[6], series1, category7);
//       dataset.addValue(prefCount[7], series1, category8);
       
       for(int i= 0; i < count.length; i++)
       {
    	   dataset.addValue(count[i], series1, "" +(i+toAdd));
       }
       
      return dataset; 
   }
   public static void makeBarChartPrefs(int[] prefCount, boolean doingSeminar)
   {
	  String title;
	  
	  if(doingSeminar)
		  title = "Seminar Preference Placements";
	  else
		  title = "Regular Preference Placements";
	  
      BarChartMaker chart = new BarChartMaker(title, title, "Preference ID", "Number of Students", prefCount,1);
      chart.pack( );        
      RefineryUtilities.centerFrameOnScreen( chart );        
      chart.setVisible( true ); 
   }
   
   public static void makeBarChartScores(int[] satCount)
   {
      BarChartMaker chart = new BarChartMaker("Distribution of Satisfaction Scores", "Number of Students with Each Satisfaction Score", "Satisfaction Score", "Number of Students",satCount,Constants.MIN_SAT_LINEAR);
      chart.pack( );        
      RefineryUtilities.centerFrameOnScreen( chart );        
      chart.setVisible( true ); 
   }
   
   public static void makeAlgTrackerChart(int[] satCount)
   {
      BarChartMaker chart = new BarChartMaker("Progression of Simulated Annealing Approach", "Progression of Simulated Annealing Approach", "Time", "Total Satisfaction Score",satCount,Constants.MIN_SAT_LINEAR);
      chart.pack( );        
      RefineryUtilities.centerFrameOnScreen( chart );        
      chart.setVisible( true ); 
   }
   
   
   
}