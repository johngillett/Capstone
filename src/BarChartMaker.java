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
   public BarChartMaker( String applicationTitle , String chartTitle, int[] prefCount )
   {
      super( applicationTitle );        
      JFreeChart barChart = ChartFactory.createBarChart(
         chartTitle,           
         "Preference ID",            
         "Number of Students",            
         createDataset(prefCount),          
         PlotOrientation.VERTICAL,           
         true, true, false);
         
      ChartPanel chartPanel = new ChartPanel( barChart );        
      chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );        
      setContentPane( chartPanel ); 
   }
   private CategoryDataset createDataset( int[] prefCount)
   {
	   final DefaultCategoryDataset dataset = new DefaultCategoryDataset( ); 

	   // row keys...
       final String series1 = "Number of Students";

       // column keys...
       final String category1 = "1";
       final String category2 = "2";
       final String category3 = "3";
       final String category4 = "4";
       final String category5 = "5";
       final String category6 = "6";
       final String category7 = "7";
       final String category8 = "8";
       
       dataset.addValue(prefCount[0], series1, category1);
       dataset.addValue(prefCount[1], series1, category2);
       dataset.addValue(prefCount[2], series1, category3);
       dataset.addValue(prefCount[3], series1, category4);
       dataset.addValue(prefCount[4], series1, category5);
       dataset.addValue(prefCount[5], series1, category6);
       dataset.addValue(prefCount[6], series1, category7);
       dataset.addValue(prefCount[7], series1, category8);
       

//	   for(int i = 0; i <prefCount.length;i++)
//	   {
//		   //addValue takes in a value, row key, and column key
//		   dataset.addValue(prefCount[i], "i", "i");
//	   }

      return dataset; 
   }
   public static void makeBarChart(int[] prefCount)
   {
      BarChartMaker chart = new BarChartMaker("Preference Placements", "Number of Students Placed in Each Preference", prefCount);
      chart.pack( );        
      RefineryUtilities.centerFrameOnScreen( chart );        
      chart.setVisible( true ); 
   }
}