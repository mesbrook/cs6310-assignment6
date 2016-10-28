

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Instructor {

	public int instructorUUID;
	public String instructorName;
	public String address;
	public String phone;
	//public ArrayList<Integer> competencies;
	
	static ArrayList<Instructor> createInstructorList(FileInputStream instructorFis){
	ArrayList<Instructor> instructorList = new ArrayList<Instructor>();
	String line = "";
	String cvsSplitBy = ",";
	try (BufferedReader instructorBr = new BufferedReader(new InputStreamReader(instructorFis)))
	{
		
		while ((line = instructorBr.readLine()) != null){
			
			// use comma as separator
			String [] instructorLine = line.split(cvsSplitBy);
			
			//create a student object for each student and add it to the student arrayList
			Instructor instructor = new Instructor();
			instructor.instructorUUID = Integer.parseInt(instructorLine[0]);
			instructor.instructorName = instructorLine[1];
			instructor.address = instructorLine[2];
			instructor.phone = instructorLine[3];
			
			instructorList.add(instructor);				
							
		}
		instructorBr.close();
	} catch (FileNotFoundException e) {
        e.printStackTrace();        
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	return instructorList;
	}//end method
	
	
	static int instructorsNotTeaching(ArrayList<StudentRecord> recordList, ArrayList<Instructor> instructorList){
	int instructorsNotTeaching = 0;
	for(int i =0; i < instructorList.size(); i++)  //for each teacher object in the array
	{
		int inClass = 0;
		for(int x = 0; x < instructorList.size(); x++)
		{
			if(recordList.get(x).instructorUUID == instructorList.get(i).instructorUUID)
			{
				x = recordList.size(); //if here then this instructor is teaching and we can move on to the next teacher object
				inClass = 1;
			}
		}
		if (inClass == 0)
		{
			instructorsNotTeaching++;
		}
	}
	return instructorsNotTeaching;
}//end method
}
