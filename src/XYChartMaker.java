import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.BasicStroke; 
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 * Class for creating an x,y plot
 * Modeled after tutorialspoint.com/jfreechart
 * 
 * @author Anna Dovzhik & John Gillett
 *
 */
public class XYChartMaker extends ApplicationFrame 
{
   public XYChartMaker( String applicationTitle , String chartTitle, String xAxis, String yAxis, int[] prefCount, boolean doingSem)
   {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         chartTitle ,
         xAxis ,
         yAxis ,
         createDataset(prefCount,doingSem) ,
         PlotOrientation.VERTICAL ,
         true , true , false);
      
      
      ChartPanel chartPanel = new ChartPanel( xylineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 2000 , 1500 ) );
 
      final XYPlot plot = xylineChart.getXYPlot( );
      
      //changing legend
      //((AbstractRenderer) plot.getRenderer()).setBaseLegendShape(new Rectangle(30,30));
      xylineChart.getLegend().setItemFont(new Font("SansSerif", Font.BOLD, 17));
      
      //changing plot label sizes
      Font font3 = new Font("SansSerif", Font.BOLD, 17); 
      plot.getDomainAxis().setLabelFont(font3);
      plot.getRangeAxis().setLabelFont(font3);
      
      setContentPane( chartPanel ); 
      
      ValueAxis yAxis1 = plot.getRangeAxis();
      if(doingSem)
    	  yAxis1.setRange(10500,12000);
      else
    	  yAxis1.setRange(2600, 5000);
      
      

   }
   
   private XYDataset createDataset(int[] count,boolean doingSem)
   {
	   final XYSeries score = new XYSeries( "Total Satisfaction Score" );          

	   // row keys
       final String series1 = "Total Satisfaction Score";

       
       for(int i= 0; i < count.length; i++)
       {
    	   score.add(i, count[i]);
       }
       
       //add min satisfaction score goal
       final XYSeries objective = new XYSeries( "Minimum Possible Score" );   
       
       //add max
       final XYSeries maxSeries = new XYSeries("Maximum Possible Score");
       
       int obj;
       int max;
       if(Constants.SAT.equals(Constants.SAT_SCALE.Geometric))
       {
    	   max = 0;
    	   obj = Constants.GEOMETRIC_OBJ_THRESHOLD;
       }
       
       else
       {
    	   if(doingSem){
    		   obj = Constants.TOT_SAT_TO_SEM_MOD + Constants.LINEAR_OBJ_SEM_THRESHOLD;
    		   max = Constants.LINEAR_OBJ_MAX_THRESHOLD;
    	   }

    	   else{
    		   obj = Constants.LINEAR_OBJ_THRESHOLD;
    		   max = Constants.LINEAR_OBJ_MAX_THRESHOLD;
    	   }
       }


       for(int i= 0; i < count.length; i++)
       {
    	   objective.add(i, obj);
    	   maxSeries.add(i,max);
       }
       
       final XYSeries greedy = new XYSeries("Result of Greedy Algorithm");
       for(int i= 0; i < count.length; i++)
       {
    	   if(doingSem)
    		   greedy.add(i,Driver.greedyTotalSemSat);
    	   else
    	   greedy.add(i, Driver.greedyTotalSat);
       }
       
      final XYSeriesCollection dataset = new XYSeriesCollection( );          
      dataset.addSeries(greedy);
      dataset.addSeries( score );   
      dataset.addSeries( objective );
      dataset.addSeries(maxSeries);
      return dataset; 
   }

   public static void makeAlgTrackerChart(int[] satCount, boolean doingSem)
   {
	  if(!doingSem)
      {
	  XYChartMaker chart = new XYChartMaker("Progression of our Algorithm on Regular Courses", "Progression of our Algorithm on Regular Courses", "Iterations", "Total Satisfaction Score",satCount,doingSem);
      chart.pack( );        
      RefineryUtilities.centerFrameOnScreen( chart );        
      chart.setVisible( true ); 
   
      }
	  else
	  {
		  XYChartMaker chart = new XYChartMaker("Progression of our Algorithm on Seminars", "Progression of our Algorithm on Seminars", "Iterations", "Total Satisfaction Score",satCount,doingSem);
	      chart.pack( );        
	      RefineryUtilities.centerFrameOnScreen( chart );        
	      chart.setVisible( true ); 
	     
	  }
   }
}
