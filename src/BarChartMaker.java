import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 

/**
 * Class for creating a bar chart.
 * Modeled after tutorialspoint.com/jfreechart
 * 
 * @author Anna Dovzhik & John Gillett
 *
 */
public class BarChartMaker extends ApplicationFrame
{
   public BarChartMaker( String applicationTitle , String chartTitle, String xAxis, String yAxis, double[] prefCount, int toAdd, boolean doingSeminar )
   {
      super( applicationTitle );        
      JFreeChart barChart = ChartFactory.createBarChart(
         chartTitle,           
         xAxis,            
         yAxis,            
         createDataset(prefCount, toAdd, doingSeminar),          
         PlotOrientation.VERTICAL,           
         true, true, false);
         
      ChartPanel chartPanel = new ChartPanel( barChart );        
      chartPanel.setPreferredSize(new java.awt.Dimension( 1500 , 1000 ) );        
      setContentPane( chartPanel ); 
      
      //normalize bar charts to be out of 1
      CategoryPlot plot = (CategoryPlot) barChart.getPlot();
      ValueAxis yAxis1 = plot.getRangeAxis();
      yAxis1.setRange(0.0, 1.0);
      
   }
   

   private CategoryDataset createDataset( double[] count, int toAdd, boolean doingSeminar)
   {
	   final DefaultCategoryDataset dataset = new DefaultCategoryDataset( ); 
	   
	   // row keys
       final String series1 = "Number of Students";
       
       
       if(doingSeminar){
    	   dataset.addValue(count[0], series1, "Advising ");
           for(int i= 1; i < count.length; i++)
           {
        	   //i +toAdd gives pref number
        	   dataset.addValue(count[i], series1, "" +(i));
           } 
       }
       
       else{
           for(int i= 0; i < count.length; i++)
           {
        	   //i +toAdd gives pref number
        	   dataset.addValue(count[i], series1, "" +(i+toAdd));
           }
       }
       
       
      return dataset; 
   }
   
   public static void makeBarChartPrefs(double[] prefCount, boolean doingSeminar)
   {
	  String title;
	  
	  if(doingSeminar)
		  title = "Seminar Preference Placements";
	  else
		  title = "Regular Preference Placements";
	  
      BarChartMaker chart = new BarChartMaker(title, title, "Preference ID", "Number of Students", prefCount,1, doingSeminar);
      chart.pack( );        
      RefineryUtilities.centerFrameOnScreen( chart );        
      chart.setVisible( true ); 
   }
   
   public static void makeBarChartScores(double[] satCount, boolean doingSeminar)
   {
      BarChartMaker chart = new BarChartMaker("Distribution of Satisfaction Scores", "Number of Students with Each Satisfaction Score", "Satisfaction Score", "Number of Students",satCount,Constants.MIN_SAT_LINEAR, doingSeminar);
      chart.pack( );        
      RefineryUtilities.centerFrameOnScreen( chart );        
      chart.setVisible( true ); 
   }
   
   public static void makeAlgTrackerChart(double[] satCount, boolean doingSeminar)
   {
      BarChartMaker chart = new BarChartMaker("Progression of Simulated Annealing Approach", "Progression of Simulated Annealing Approach", "Time", "Total Satisfaction Score",satCount,Constants.MIN_SAT_LINEAR, doingSeminar);
      chart.pack( );        
      RefineryUtilities.centerFrameOnScreen( chart );        
      chart.setVisible( true ); 
   }
   
   
   
}