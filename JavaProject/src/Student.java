

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Student {

	public int studentUUID;
	public String studentName;
	public String address;
	public String phone;
	//public boolean enrolled;
	//public String programOfStudy;
	//public int numberCurrentlyEnrolledCourses;
	
	
	
		
	//puts the students into an arrayList
	static ArrayList<Student> createStudentList(FileInputStream studentFis){	
		ArrayList<Student> studentList = new ArrayList<Student>();
		String line = "";
		String cvsSplitBy = ",";
	try (BufferedReader studentBr = new BufferedReader(new InputStreamReader(studentFis)))
	{
		
		while ((line = studentBr.readLine()) != null){
			
			// use comma as separator
			String [] studentLine = line.split(cvsSplitBy);
			
			//create a student object for each student and add it to the student arrayList
			Student student = new Student();
			student.studentUUID = Integer.parseInt(studentLine[0]);
			student.studentName = studentLine[1];
			student.address = studentLine[2];
			student.phone = studentLine[3];
			
			studentList.add(student);				
							
		}
		studentBr.close();
	} catch (FileNotFoundException e) {
        e.printStackTrace();        
	} catch (IOException e) {
		
		e.printStackTrace();
	}	
	return studentList;
	}//end method createStudentList
	
	
	static int studentsNotInClass(ArrayList<StudentRecord> recordList, ArrayList<Student> studentList){
		int studentsNotInClass = 0;
		for(int i =0; i < studentList.size(); i++)  //for each student object in the array
		{
			int inClass = 0;
			for(int x = 0; x < recordList.size(); x++)
			{
				if(recordList.get(x).studentUUID == studentList.get(i).studentUUID)
				{
					x = recordList.size(); //if here then this student is enrolled and we can move on to the next student object
					inClass = 1;
				}
			}
			if (inClass == 0)
			{
				studentsNotInClass++;
			}
		}
		
		return studentsNotInClass;
	}
	
	public static String[] getStudentData(int studentUUID, ArrayList<Student> studentList){
		String[] studentData = new String[4];
		for(Student s : studentList){
			if(studentUUID == s.studentUUID){
				studentData[0] = String.valueOf(s.studentUUID);
				studentData[1] = s.studentName;
				studentData[2] = s.address;
				studentData[3] = s.phone;
			}
		}
		return studentData;
	}//end getStudentData method
	
	
}
