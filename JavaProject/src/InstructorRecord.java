
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class InstructorRecord {
	
	public int instructorUUID;
	public int courseID;
	public String semester;
	//public Date year;
	//public int timesTaught;
	public int capacity;

	static String line = "";
	static String cvsSplitBy = ",";
	
	static ArrayList<InstructorRecord> createInstructorRecordList(FileInputStream instRecFis){
		ArrayList<InstructorRecord> instructorRecordList = new ArrayList<InstructorRecord>();
		
		try (BufferedReader instrRecBr = new BufferedReader(new InputStreamReader(instRecFis)))
		{
			
			while ((line = instrRecBr.readLine()) != null){
				
				// use comma as separator
				String [] instructorLine = line.split(cvsSplitBy);
				
				//create a student object for each student and add it to the student arrayList
				InstructorRecord instructorRecord = new InstructorRecord();
				instructorRecord.instructorUUID = Integer.parseInt(instructorLine[0]);
				instructorRecord.courseID = Integer.parseInt(instructorLine[1]);
				instructorRecord.capacity = Integer.parseInt(instructorLine[2]);
				
				
				instructorRecordList.add(instructorRecord);				
								
			}
			instrRecBr.close();
		} catch (FileNotFoundException e) {
	        e.printStackTrace();        
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return instructorRecordList;
		}//end method
	
	
	public static boolean seatsAvailable(int courseID,  ArrayList<InstructorRecord> instructorRecords){
		boolean openSeats = false;
		boolean removedAseat = false;
		for ( InstructorRecord record : instructorRecords){
			if(courseID == record.courseID && record.capacity > 0 && removedAseat == false){
				record.capacity = record.capacity - 1;
				removedAseat = true;
				openSeats = true;
			}
		}
		return openSeats;
	}//end seatsAvailable method
	
	public static int numberOfOpenSeats(int courseID, ArrayList<InstructorRecord> instructorRecords){
		int openSeats = 0;
		for (InstructorRecord record : instructorRecords){
			if(courseID == record.courseID && record.capacity > 0){
				openSeats = openSeats + record.capacity;
			}
		}
		return openSeats;
	}//end number of seats method
	
	public static ArrayList<String> displaySeats(ArrayList<Course> courseList, ArrayList<InstructorRecord> instrRecList){
		ArrayList<String> dsList = new ArrayList<String>();
		for(Course c : courseList){
			String dsOutput = String.valueOf(c.courseID) + ", " + c.title + ", " + String.valueOf(InstructorRecord.numberOfOpenSeats(c.courseID, instrRecList));
			dsList.add(dsOutput);
		}
		return dsList;
	}//end displaySeats method
	
	public static void addSeats(String[] seatRequest, ArrayList<InstructorRecord> irList){
		for(InstructorRecord ir : irList){
			if(Integer.parseInt(seatRequest[1]) == ir.courseID){				
				ir.capacity = ir.capacity + Integer.parseInt(seatRequest[2]);
			}
		}
	}//end addSeats method
	
}
