

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StudentRecord {

	public int studentUUID;
	public int courseID;
	public int instructorUUID;
	public String instructorComment;
	//public String semester;
	//public Date year;	
	public String grade;
	public Boolean requestStatus;
	public String requestComment;
	//public boolean havePrereqs;
	//public boolean allowedToTake;
	
	static String line = "";
	static String cvsSplitBy = ",";
	
	static ArrayList<StudentRecord> createRecordList(FileInputStream recordFis){
	ArrayList<StudentRecord> recordList = new ArrayList<StudentRecord>();
	try (BufferedReader recordBr = new BufferedReader(new InputStreamReader(recordFis)))
	{
		
		while ((line = recordBr.readLine()) != null){
			
			// use comma as separator
			String [] recordLine = line.split(cvsSplitBy);
			
			//create a student object for each student and add it to the student arrayList
			StudentRecord record = new StudentRecord();
			record.studentUUID = Integer.parseInt(recordLine[0]);
			record.courseID = Integer.parseInt(recordLine[1]);
			record.instructorUUID = Integer.parseInt(recordLine[2]);
			record.instructorComment = recordLine[3];
			record.grade = recordLine[4];
			
			recordList.add(record);				
							
		}
		recordBr.close();
	} catch (FileNotFoundException e) {
        e.printStackTrace();        
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	return recordList;
}//end method
	
	
	
	
	static ArrayList<StudentRecord> createRequestsList(FileInputStream requestsFis){
		ArrayList<StudentRecord> requestsList = new ArrayList<StudentRecord>();
		
		try (BufferedReader requestsBr = new BufferedReader(new InputStreamReader(requestsFis)))
		{
			
			while ((line = requestsBr.readLine()) != null){
				
				// use comma as separator
				String [] requestsLine = line.split(cvsSplitBy);
				
				//create a student object for each student and add it to the student arrayList
				StudentRecord requests = new StudentRecord();
				requests.studentUUID = Integer.parseInt(requestsLine[0]);
				requests.courseID = Integer.parseInt(requestsLine[1]);					
				
				requestsList.add(requests);				
								
			}
			requestsBr.close();
		} catch (FileNotFoundException e) {
	        e.printStackTrace();        
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return requestsList;
		}//end method
	
	//bulk import check
	public static void approveRequest(ArrayList<Student> studentList, ArrayList<Course> courseList, ArrayList<StudentRecord> requestsList, ArrayList<StudentRecord> recordsList, ArrayList<InstructorRecord> instructorRecordsList){
		//boolean prereqsCompleted;
		//String requestComment;
		for (StudentRecord request : requestsList){
			if(request.requestStatus == null){
				ArrayList<Integer> prereqs = new ArrayList<Integer>(Course.hasPrereqs(request.courseID, courseList)); //begin prereq check
				if(prereqs.size() > 0){
					
					if(!StudentRecord.prereqesCompleted(prereqs, request.studentUUID, recordsList)){
					//System.out.println(Integer.toString(request.studentUUID) + ' ' + String.valueOf(prereqsCompleted));
						request.requestStatus = false;
						request.requestComment = "student is missing one or more prerquisites";
					}
				}//end prereq check
			}
			if(request.requestStatus == null && !(StudentRecord.retakingCourseCheck(request.studentUUID, request.courseID, recordsList))){//start retakeCheck
				request.requestStatus = false;
				request.requestComment = "student has already taken the course with a grade of C or higher";
			}//end retakeCheck
			if(request.requestStatus == null && !(InstructorRecord.seatsAvailable(request.courseID, instructorRecordsList))){
				request.requestStatus = false;
				request.requestComment = "no remaining seats available for the course at this time";
			}else if(request.requestStatus == null){
				request.requestStatus = true;
				request.requestComment = "request is valid";
			}
			//System.out.println(Integer.toString(request.studentUUID) + ' ' + Integer.toString(request.courseID) + ' ' + String.valueOf(request.requestStatus) + ' ' + request.requestComment);
		}
	}//end approveRequest method
	
	//override of approveRequest for single student and single course
	public static String approveRequest(int studentUUID, int courseID, ArrayList<Course> courseList, ArrayList<StudentRecord> requestsList, ArrayList<StudentRecord> recordsList, ArrayList<InstructorRecord> instructorRecordsList){
		String requestComment = "";
		requestsList = StudentRecord.addRequest(studentUUID, courseID, requestsList);
		for (StudentRecord request : requestsList){
			if(request.studentUUID == studentUUID && request.courseID == courseID){
				if(request.requestStatus == null){
					ArrayList<Integer> prereqs = new ArrayList<Integer>(Course.hasPrereqs(request.courseID, courseList)); //begin prereq check
					if(prereqs.size() > 0){				
						if(!StudentRecord.prereqesCompleted(prereqs, request.studentUUID, recordsList)){
							request.requestStatus = false;
							request.requestComment = "student is missing one or more prerquisites";
						}
					}//end prereq check
				}
				if(request.requestStatus == null && !(StudentRecord.retakingCourseCheck(request.studentUUID, request.courseID, recordsList))){//start retakeCheck
					request.requestStatus = false;
					request.requestComment = "student has already taken the course with a grade of C or higher";
				}//end retakeCheck
				if(request.requestStatus == null && !(InstructorRecord.seatsAvailable(request.courseID, instructorRecordsList))){
					request.requestStatus = false;
					request.requestComment = "no remaining seats available for the course at this time";
				}else if(request.requestStatus == null){
					request.requestStatus = true;
					request.requestComment = "request is valid";
				}
				requestComment = request.requestComment;
			}//if		
		}//for
		return requestComment;
	}//end approveRequest method for single student
	
	//this method takes the list of prereqs from a course, the studentUUId, and the courseList and 
	//checks to see if the student has all the required courses and that he has a D or higher in them
	private static boolean prereqesCompleted(ArrayList<Integer> prereqs, int studentUUID, ArrayList<StudentRecord> records){
		boolean completed = false;
		int studentHasPrereqs = 0;
		for (Integer i : prereqs){
			for (StudentRecord record : records){
				//System.out.println(record.grade);
				if(i == record.courseID && studentUUID == record.studentUUID && record.grade.charAt(0) != 'F'){
					studentHasPrereqs++;
				}
			}
		}
		if(studentHasPrereqs == prereqs.size()){
			completed = true;
		}
		return completed;
	}//end prereqsCompleted check
	
	
	private static boolean retakingCourseCheck(int studentUUID, int courseID, ArrayList<StudentRecord> records){
		boolean retakeCheckPass = true;
		for (StudentRecord record : records){
			if(studentUUID == record.studentUUID && courseID == record.courseID && (record.grade.charAt(0) == 'A' || record.grade.charAt(0) == 'B' || record.grade.charAt(0) == 'C')){
				retakeCheckPass = false;
			}
		}
		return retakeCheckPass;
	}//end retakeCheck method
	
	public static int howManyValidRequests(ArrayList<StudentRecord> requests){
		int valid = 0;
		for ( StudentRecord s : requests){
			if(s.requestStatus == true){
				valid++;
			}
		}
		return valid;
	}//end howManyValid method
	
	public static int howManyMissingPrereqs(ArrayList<StudentRecord> requests){
		int numMissing = 0;
		for ( StudentRecord s : requests){
			if(s.requestComment == "student is missing one or more prerquisites"){
				numMissing++;
			}
		}
		return numMissing;
	}
	
	public static int howManyCourseAlreadyTaken(ArrayList<StudentRecord> requests){
		int numTaken = 0;
		for ( StudentRecord s : requests){
			if(s.requestComment == "student has already taken the course with a grade of C or higher"){
				numTaken++;
			}
		}
		return numTaken;
	}
	
	public static int howManyNoSeats(ArrayList<StudentRecord> requests){
		int numNoSeats = 0;
		for ( StudentRecord s : requests){
			if(s.requestComment == "no remaining seats available for the course at this time"){
				numNoSeats++;
			}
		}
		return numNoSeats;
	}//end how many course have no open seats
	
	
	public static ArrayList<String> displayRequests(ArrayList<StudentRecord> requestsList, ArrayList<StudentRecord> recordList, ArrayList<Course> courseList, ArrayList<Student> studentList){
		ArrayList<String> requestsOutput = new ArrayList<String>();
		for ( StudentRecord request : requestsList){
			//System.out.println(request.studentUUID + " "  + request.courseID + " " + String.valueOf(request.requestStatus));
			if(request.requestStatus){
				String[] studentData = Student.getStudentData(request.studentUUID, studentList);
				String[] courseData = Course.getCourseData(request.courseID, courseList);
				String reqOutput = new String();
				reqOutput = String.valueOf(request.studentUUID) + ", " + studentData[1] + ", " + String.valueOf(request.courseID) + ", " + courseData[1];
				requestsOutput.add(reqOutput);
			}
		}
		return requestsOutput;
	}//end displayRequests method
	
	public static ArrayList<String> displayRecords(ArrayList<StudentRecord> studentList){
		ArrayList<String> drList = new ArrayList<String>();
		for(StudentRecord s : studentList){
			String drOutput = String.valueOf(s.studentUUID) + ", " + String.valueOf(s.courseID) + ", " + String.valueOf(s.instructorUUID) + ", " + s.instructorComment + ", " + String.valueOf(s.grade);
			drList.add(drOutput);
		}
		return drList;
	}//end displayRecord method
	
	public static void addRecord(String[] srData, ArrayList<StudentRecord> recordsList){
		//create a student object for each student and add it to the student arrayList
		StudentRecord record = new StudentRecord();
		record.studentUUID = Integer.parseInt(srData[1]);
		record.courseID = Integer.parseInt(srData[2]);
		record.instructorUUID = Integer.parseInt(srData[3]);
		record.instructorComment = srData[4];
		record.grade = srData[5];
		
		recordsList.add(record);	
	}
	
	public static ArrayList<StudentRecord> addRequest(int studentUUID, int courseID, ArrayList<StudentRecord> requestsList){
		boolean exists = false;
		for(StudentRecord r : requestsList){
			if((studentUUID == r.studentUUID && courseID == r.courseID)){
				r.requestStatus = null;
				exists = true;					
			}
		}
		if(!exists){
			StudentRecord sr = new StudentRecord();
			sr.studentUUID = studentUUID;
			sr.courseID = courseID;
			requestsList.add(sr);
		}
		return requestsList;
	}
	
	
	
}//end class
