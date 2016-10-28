

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Course {

	public int courseID;
	public String title;
	//public String description;
	public ArrayList<String> semesters = new ArrayList<>();
	//public String programBelongsTo;
	//public String onlineOrOncampus;
	public ArrayList<Integer> prereqs = new ArrayList<>();
	//public int numberOfSeats = 0;
	
	static String line = "";
	static String cvsSplitBy = ",";
	
	static ArrayList<Course> createCourseList(FileInputStream courseFis){
	ArrayList<Course> courseList = new ArrayList<Course>();
	//String line = "";
	//String cvsSplitBy = ",";
	try (BufferedReader courseBr = new BufferedReader(new InputStreamReader(courseFis)))
	{
		
		while ((line = courseBr.readLine()) != null){
			
			// use comma as separator
			String [] courseLine = line.split(cvsSplitBy);
			Course course = new Course();
			//add first two strings to object
			course.courseID = Integer.parseInt(courseLine[0]);
			course.title = courseLine[1];
			
			for(int i = 2; i < courseLine.length; i++)
			{							
				course.semesters.add(courseLine[i]);
			}
								
			
			courseList.add(course);
			
			/*if(course.semesters.contains("Fall")){
				System.out.println("good");
			}*/
								
		}
		courseBr.close();
	
	} catch (FileNotFoundException e) {
        e.printStackTrace();        
	} catch (IOException e) {
		System.out.println("course error");
		e.printStackTrace();
	}
	return courseList;
	}//end createCourseList method
	
	
	static int countEmptyCourses(ArrayList<StudentRecord> recordList, ArrayList<Course> courseList){
	int emptyCourses = 0;
	for(int i =0; i < courseList.size(); i++)  //for each course object in the array
	{
		int inClass = 0;
		for(int x = 0; x < recordList.size(); x++)
		{
			if(recordList.get(x).courseID == courseList.get(i).courseID)
			{
				x = recordList.size(); //if here then this course has students and we can move on to the next course object
				inClass = 1;
			}
		}
		if (inClass == 0)
		{
			emptyCourses++;
		}
	}
	return emptyCourses;
	}//empty countEmptyCourses method
	
	static int[] semesterCourseCount(ArrayList<Course> courseList){
	 int fallCourseCount = 0;
	 int springCourseCount = 0;
	 int summerCourseCount = 0;
	 int[] courseCounts = new int[3];
	
	for(int i = 0; i < courseList.size(); i++)
	{
			if(courseList.get(i).semesters.contains("Fall"))
			{
				fallCourseCount++;
			}
			if(courseList.get(i).semesters.contains("Spring"))
			{
				springCourseCount++;
			}
			if(courseList.get(i).semesters.contains("Summer"))
			{
				summerCourseCount++;
			}		
	}
	courseCounts[0] = fallCourseCount;
	courseCounts[1] = springCourseCount;
	courseCounts[2] = summerCourseCount;
	
	return courseCounts;
	}//end semesterCourseCount method
	
	
	static void preregUpload(FileInputStream prereqFis, ArrayList<Course> courseList){
		try (BufferedReader courseBr = new BufferedReader(new InputStreamReader(prereqFis)))
		{
			
			while ((line = courseBr.readLine()) != null){
				
				// use comma as separator
				String [] prereqLine = line.split(cvsSplitBy);
				//add first two strings to object
				int prereqID = Integer.parseInt(prereqLine[0]);
				int courseID = Integer.parseInt(prereqLine[1]);
				for (Course course : courseList){
					if(courseID == course.courseID){
						course.prereqs.add(prereqID);
					}
				}
					//System.out.println(student.studentID);
				}
			courseBr.close();
			
		} catch (FileNotFoundException e) {
		        e.printStackTrace();        
		} catch (IOException e) {
				System.out.println("course error");
				e.printStackTrace();
		}
	}//end addprereq method
	
	
	static ArrayList<Integer> hasPrereqs(Integer courseID, ArrayList<Course> courseList){		
		ArrayList<Integer> prereqs = new ArrayList<Integer>();
		for (Course course : courseList){
			if(courseID == course.courseID){
				if(course.prereqs != null){
					for(Integer p : course.prereqs){
						prereqs.add(p);
					}					
				}
			}		
		}
		return prereqs;
	}//end hasprereqs method
		
	
	public static String[] getCourseData(int courseID, ArrayList<Course> courseList){
		String[] courseData = new String[2];
		for(Course c : courseList){
			if(courseID == c.courseID){
				courseData[0] = String.valueOf(c.courseID);
				courseData[1] = c.title;			
			}
		}
		return courseData;
	}//end getCourseData method
	
}//end of class
