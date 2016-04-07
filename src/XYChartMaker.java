import java.awt.Color; 
import java.awt.BasicStroke; 
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class XYChartMaker extends ApplicationFrame 
{
   public XYChartMaker( String applicationTitle , String chartTitle, String xAxis, String yAxis, int[] prefCount, int toAdd )
   {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         chartTitle ,
         xAxis ,
         yAxis ,
         createDataset(prefCount, toAdd) ,
         PlotOrientation.VERTICAL ,
         true , true , false);
      
      
      ChartPanel chartPanel = new ChartPanel( xylineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 1500 , 1000 ) );
 
      final XYPlot plot = xylineChart.getXYPlot( );
      
      setContentPane( chartPanel ); 
   }
   
   private XYDataset createDataset(int[] count, int toAdd )
   {
	   final XYSeries score = new XYSeries( "Score" );          

	   // row keys...
       final String series1 = "Total Satisfaction Score";

       
       for(int i= 0; i < count.length; i++)
       {
    	   score.add(i, count[i]);
       }
       
      final XYSeriesCollection dataset = new XYSeriesCollection( );          
      dataset.addSeries( score );   
      return dataset; 
   }

   public static void makeAlgTrackerChart(int[] satCount)
   {
      XYChartMaker chart = new XYChartMaker("Progression of Simulated Annealing Approach", "Progression of Simulated Annealing Approach", "Time", "Total Satisfaction Score",satCount,Constants.MIN_SAT_LINEAR);
      chart.pack( );        
      RefineryUtilities.centerFrameOnScreen( chart );        
      chart.setVisible( true ); 
   }
   
}
