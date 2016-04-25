import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultsWriter {

	public static void writeResults(HashMap<Integer,Student> studs)
	{
		File f;
		
		f = new File("resultingSchedule.txt");
			
		if(!f.exists())
			try 
			{
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			
			for(HashMap.Entry<Integer, Student> entry : studs.entrySet()){
				Student stud = entry.getValue();
				writer.write(stud.id+"'s Schedule: ");
				writer.newLine();
				
				if(!stud.hasAdvisingCourse)
				{
					//System.out.println("NO ADVISING!"+stud.toString());
					writer.write("\tAdvising:\tNONE!");
				}
				else
				{
					if(stud.advisingIsSeminar()){
						if(!(stud.advisingCourse.getID().charAt(0)=='S')){
							System.out.println("!!!!!!!!!! Student " + stud.id + " has " + stud.advisingCourse.getID());
						}
						writer.write("\tAdvising/Sem:\t" +stud.advisingCourse.getID()+stud.advisingCourse.getSectionID()+": "+stud.advisingCourse.schedToString());

					}
					else
						writer.write("\tAdvising:\t" +stud.advisingCourse.getID()+stud.advisingCourse.getSectionID()+": "+stud.advisingCourse.schedToString());
				}	
				writer.newLine();

				if(!stud.advisingIsSeminar())
					for(Course c: stud.courses)
					{
						if(stud.hasAdvisingCourse && c.isSameCourse(stud.advisingCourse))
							continue;

						if(c.isSeminar)
						{
							writer.write("\tSeminar:\t"+c.getID()+c.getSectionID()+": "+c.schedToString());
							writer.newLine();
						}
					}

				for(Course c: stud.courses)
				{
					if(stud.hasAdvisingCourse && c.isSameCourse(stud.advisingCourse))
						continue;

					if(!c.isSeminar)
					{
						writer.write("\t\t\t"+c.getID()+c.getSectionID()+": "+c.schedToString());
						writer.newLine();
					}
				}

				writer.newLine();
			}



			writer.close();




		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void writeCourseResults(HashMap<String,ArrayList<Course>> courses)
	{
File f;
		
		f = new File("resultingCourses.txt");
			
		if(!f.exists())
			try 
			{
				f.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			
			for(HashMap.Entry<String,ArrayList<Course>> entry : courses.entrySet()){
				ArrayList<Course> sections = entry.getValue();
				
				for(Course c : sections)
				{
					writer.write(c.toString());
					writer.newLine();
					if(c.hasLab)
					{
						for(Course l: c.getLabs())
						{
							writer.write("\t\t"+l.toString());
							writer.newLine();
						}
					}
					
					writer.newLine();
							
				}
				
			}
	
			writer.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
	
	
}
