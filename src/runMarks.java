import java.io.*;
import java.util.*;


public class runMarks {
	static int debugLevel = 1;
	static List <Course> courses;
	static String baseFolder = "../AAST";
	static String coursesFile = "../AAST/courses.txt";
	static String submissionsFolder = "../AAST/submissions";
	static String outputFolder = "../AAST/output";
	static int minCourseIDLen = 4;
	static int maxCourseIDLen = 5;
	static int minStudentIDLen = 5;
	static int maxStudentIDLen = 9;
	static int minSubIDLen = 3;
	static int maxSubIDLen = 6;
	
	public static boolean createCourses (String cFileName) {
		 
		try {
			int k = 0;
			Course course = null;
			FileInputStream fstream = new FileInputStream(cFileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String Line;
			//Read Courses File Line By Line
			courses = new ArrayList<Course> ();
			while ((Line = br.readLine()) != null)   {
				// Print the content on the console
				if (Line.indexOf("Course") == 0) {
					/* Add the previous course if this is not the first*/
					if (k>0)
						courses.add(course);
					k++;	
					/* Define New Course Object*/
					String[] cTokens = Line.split("\t");
					if (cTokens.length > 2) {
						course = new Course(cTokens[1], cTokens[2]);
						course.gradedActivities = new ArrayList<courseGradedActivities> ();
					} else {
						System.out.println("Faulty Courses file format! Exiting");
						System.exit(-1);
					}
				}
				else {
					/* Add new courseGradedActivities to last Course Object*/
					String[] cTokens = Line.split("\t");
					if (cTokens.length > 7) {
						Date sDate = DateFunctions.setDate(cTokens[3], "d/m/yyyy HH:mm");
						String vFile = null;
						if (cTokens.length > 8)
							vFile = cTokens[8];
						String iFile = null;
						if (cTokens.length > 9)
							iFile = cTokens[9];
						courseGradedActivities gradedActivity = new  courseGradedActivities(cTokens[0], course.getID(), Integer.parseInt(cTokens[1]), Integer.parseInt(cTokens[2]), sDate, cTokens[4], cTokens[5].toLowerCase().equals("true"), cTokens[6], cTokens[7], vFile, iFile);
						course.gradedActivities.add(gradedActivity);
					} else {
						System.out.println("Faulty Courses file format! Exiting");
						System.exit(-1);
					}
				}
			}
			courses.add(course);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}	
	

  
	
	public static void main (String[] args) {
		
		if (args.length > 0)
			baseFolder  = args[0];
		if (args.length > 1)
			coursesFile = args[1];
		if (args.length > 2)
			submissionsFolder = args[2];
		if (args.length > 3)
			outputFolder  = args[3];
		
		// Read Courses File: contain courses and graded activities, compile, run statements, input files, output validation rules
		createCourses (coursesFile);
		// List Submissions, compile, run and mark
		Submissions subs = new Submissions(submissionsFolder);
		System.out.println("Course\tFile Name\t Student ID\tSubmission\tDate\tDays Diff");
		subs.listSubmissions(submissionsFolder);
		// Save Results in tab delimited file
		// (attention) some assignments blocks execution and it never reaches saving now.
		subs.saveMarks(coursesFile, submissionsFolder);
	}

}
