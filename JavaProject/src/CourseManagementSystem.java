

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class CourseManagementSystem {

	public static void main(String[] args) throws FileNotFoundException {
				
		String studentPath = "./students.csv";
		FileInputStream studentFis = new FileInputStream(studentPath);
				
		String coursePath = "./courses.csv";
		FileInputStream courseFis = new FileInputStream(coursePath);
				
		String recordPath = "./records.csv";
		FileInputStream recordFis = new FileInputStream(recordPath);
				
		String instructorPath = "./instructors.csv";
		FileInputStream instructorFis = new FileInputStream(instructorPath);
		
		String prereqPath = "./prereqs.csv";
		FileInputStream prereqFis = new FileInputStream(prereqPath);
		
		String instRecPath = "./assignments.csv";
		FileInputStream instRecFis = new FileInputStream(instRecPath);
		
		String requestsPath = "./requests.csv";
		FileInputStream requestsFis = new FileInputStream(requestsPath);
					
		//create array list to hold all students..------------------------------------------------------------------------------------------------
		ArrayList<Student> studentList = Student.createStudentList(studentFis);//imports students
		ArrayList<Course> courseList = Course.createCourseList(courseFis); //import courses
		ArrayList<Instructor> instructorList = Instructor.createInstructorList(instructorFis);//imports instructors
		ArrayList<StudentRecord> recordList = StudentRecord.createRecordList(recordFis);//imports student records
	//----------------------------------------------Assignment6--------------------------------------------	
		//upload assignment6 csv files
		Course.preregUpload(prereqFis, courseList); //adds prereqs to courseList
		ArrayList<InstructorRecord> instructorRecordList = InstructorRecord.createInstructorRecordList(instRecFis); //imports instructor records
		ArrayList<StudentRecord> requestsList = StudentRecord.createRequestsList(requestsFis); //imports request
		
	//------------------------------------------------Testing---------------------------------------------------------------------------------------------------
		/*for (Course course : courseList){
			int ci = course.courseID;
			System.out.print(course.courseID);
			for(Integer p : course.prereqs){				
				System.out.print(", " +p);
			}
			System.out.println("");
		}
			System.out.println("-----");
		
		for (Student student : studentList){
			System.out.println(student.studentUUID);
		}
			System.out.println("-----");*/
		//----------------------------end testing---------------------------------------------------------------
		
		StudentRecord.approveRequest(studentList, courseList, requestsList, recordList, instructorRecordList);
		
		//sizes of lists assignment---------------------------------------------------------------------------
				//System.out.println(recordList.size()); //this is good assingment3
				//System.out.println(studentList.size()); //this is good assignment3
		System.out.println(requestsList.size());
		System.out.println(StudentRecord.howManyValidRequests(requestsList));
		System.out.println(StudentRecord.howManyMissingPrereqs(requestsList));
		System.out.println(StudentRecord.howManyCourseAlreadyTaken(requestsList));
		System.out.println(StudentRecord.howManyNoSeats(requestsList));	
		
//-------------------------Students not in class-------------------------------------------------------------------
		
		//System.out.println(Student.studentsNotInClass(recordList, studentList)); //assignment3
//----------------------------------------------------------------------------------------------------------------------------		
		
		//System.out.println(instructorList.size());//assignment3
		
		//-------------------------Instructors not teaching-------------------------------------------------------------------
				//have bug here need to fix test cases 8 - 11 from assignment 3
			//	System.out.println(Instructor.instructorsNotTeaching(recordList, instructorList));
		//----------------------------------------------------------------------------------------------------------------------------			
		
		//System.out.println(courseList.size());//this is good //assignment3
		
		
		//System.out.println(Course.countEmptyCourses(recordList, courseList)); //this is good //assignment3
//----------------------------------------------------------------------------------------------------------------------------	
		
		
//-------------------------Course Count----------------------------------
		
		/*int[] courseCount = Course.semesterCourseCount(courseList);//assignment3
		
		System.out.println(courseCount[0]);//good
		System.out.println(courseCount[1]);//good
		System.out.println(courseCount[2]);//good*/
	
		
		//-------------------------------------------------------start second part of assignment 6--------------------
		String main = " ";
		Scanner scan = new Scanner(System.in);
		while(!(main.equals("quit"))){
			
			System.out.print("main: ");		
			main = scan.nextLine();
			String commaSplitBy = ",";
			String[] consoleInput = main.split(commaSplitBy);
			if(consoleInput.length == 1){
			//start display_request command
			if(main.equals("display_requests")){
				ArrayList<String> drOutput = StudentRecord.displayRequests(requestsList, recordList, courseList, studentList);
				if(drOutput.size() > 0){
					for(String request : drOutput){
						System.out.println(request);
					}
				}
			} else//end display_request command
			//start display_seats command
			if(main.equals("display_seats")){
				ArrayList<String> dsOutput = InstructorRecord.displaySeats(courseList, instructorRecordList);
				if(dsOutput.size() > 0){
					for(String course : dsOutput){
						System.out.println(course);
					}
				}
			} else//end display_seats command
			//start display_records command
			if(main.equals("display_records")){
				ArrayList<String> drOutput = StudentRecord.displayRecords(recordList);
				if(drOutput.size() > 0){
					for(String sr : drOutput){
						System.out.println(sr);
					}
				}
			}//end display_records
			}//end console length if command
			else{//start parameter commands
				if(consoleInput[0].equals("add_record")){
					StudentRecord.addRecord(consoleInput, recordList);
				} else//end add_record command
					if(consoleInput[0].equals("add_seats")){
						InstructorRecord.addSeats(consoleInput, instructorRecordList);
				} else
					if(consoleInput[0].equals("check_request")){
						String comment = StudentRecord.approveRequest(Integer.parseInt(consoleInput[1]), Integer.parseInt(consoleInput[2]), courseList, requestsList, recordList, instructorRecordList);
						System.out.println(comment);								
					}				
			}//end else
			
			
			
		}//end while loop
		System.out.println("stopping the command loop");	
		scan.close();
		
		
	}//end main method

}//end class
