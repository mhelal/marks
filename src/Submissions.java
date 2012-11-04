import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Submissions {
	public static int submissionsCount = 0;
	private String Path;
	List <gradedSubmissions> badSubmissions;
	List <gradedSubmissions> gSubmissions;
	List <Thread> threads;

	/* Class: theLock
     * a mutux to control shared variables between threads */
    static class theLock extends Object {
    }
    
    static public theLock lockObject = new theLock();
    
	private static class processSubmission implements Runnable {
		private Submissions.gradedSubmissions gs;
		courseGradedActivities g;
		processSubmission (Submissions.gradedSubmissions gSub, courseGradedActivities gA) {
			gs = gSub;	
			g = gA;
		}
		
		public void run() {
			if (g.compile(gs)) {
				g.runTests(gs);
		        g.mark(gs);
			} /* else will need to define keywords and sentences to mark the essay questions, or a format for the multiple choice*/
			if (runMarks.debugLevel > 0)
				synchronized (lockObject) {
					Submissions.submissionsCount ++;
					System.out.println("["+submissionsCount+"]"+gs.courseID + "\t" + gs.pName + "\t" + gs.fName + "\t" + gs.studentID + "\t" + gs.submissionID + "\t" + gs.sDate + "\t"+gs.daysDelay+"\t"+gs.mark+"\t"+gs.percentage);
		
				}
		}
	}

	
	public class gradedSubmissions {
		String courseID, studentID, submissionID, fName, pName;
		Date sDate;
		int daysDelay;
		double mark, percentage;
	}
	Submissions (String PathtoScan) {
		submissionsCount = 0;
		Path = PathtoScan;
		threads = new ArrayList<Thread> ();
		badSubmissions = new ArrayList<gradedSubmissions> ();
		gSubmissions = new ArrayList<gradedSubmissions>();
	}
	public void setPath(String PathtoScan) {
		Path = PathtoScan;
	}
	
	public String getStudentID (String fname, int offset) {
		//String StudentID = null;
		StringBuilder StudentID = new StringBuilder();
		int k = -1;
		for (int i = offset; i< fname.length();i++) {
			if (Character.isDigit(fname.charAt(i)) && ((k == -1) || (i-k == offset))) {
				if (k == -1) {
					k++;
					offset = i-k;
				}	
				StudentID.append(fname.charAt(i));
				k++;
			}
		}
		if ((k>0) && (offset < fname.length()) && (StudentID.length() < runMarks.minStudentIDLen))
			return getStudentID (fname, offset+k);
		return StudentID.toString();
	}

	public String getCourseIDFromPath (String pName) {
		String courseID = null;
		int offset = 0;
		if (((offset = pName.indexOf(runMarks.submissionsFolder)) >= 0) && (pName.length() > runMarks.submissionsFolder.length()+runMarks.minCourseIDLen))
			courseID = pName.substring(offset+runMarks.submissionsFolder.length()+1, offset+runMarks.submissionsFolder.length()+runMarks.minCourseIDLen+2);
		return courseID;
	}

	public String getSubmissionIDFromPath (String courseID, String pName) {
		String submissionID = null;
		for (Iterator<Course> i = runMarks.courses.iterator(); i.hasNext(); ) {
			Course c = i.next();
			String cID = c.getID();
			if (cID.toLowerCase().trim().equalsIgnoreCase(courseID.toLowerCase().trim())) {
				for (Iterator<courseGradedActivities> j = c.gradedActivities.iterator(); j.hasNext(); ) {
					courseGradedActivities g = j.next();
					if (pName.toLowerCase().trim().indexOf(g.getID().trim().toLowerCase()) >= 0)
						return g.getID().toLowerCase();					
				}				
			}
		}
		return submissionID;
	}

	public courseGradedActivities validateSubmissionID (String courseID, String fName, String pName) {
		if (fName == null)
			return null;
		if (fName.length() <= 0)
			return null;
		for (Iterator<Course> i = runMarks.courses.iterator(); i.hasNext(); ) {
			Course c = i.next();
			String cID = c.getID();
			if (cID.toLowerCase().trim().equalsIgnoreCase(courseID.toLowerCase().trim())) {
				for (Iterator<courseGradedActivities> j = c.gradedActivities.iterator(); j.hasNext(); ) {
					courseGradedActivities g = j.next();
					String cPrefix = g.getID().trim().toLowerCase().substring(0, 3);
					String gNum = g.getID().trim().toLowerCase().substring(3, g.getID().trim().toLowerCase().length());
					if ((fName.toLowerCase().trim().indexOf(cPrefix) >= 0) && (fName.toLowerCase().trim().indexOf(gNum) > 0))
						return g;
					else if (fName.toLowerCase().trim().indexOf(g.getID()) >= 0)
						return g;
					else if (pName.toLowerCase().trim().indexOf(g.getID()) >= 0)
						return g;
				}				
			}
		}
		return null;
	}

	public courseGradedActivities getGradedActivity (String courseID, String fName, String pName) {
		//String StudentID = null;
		/*StringBuilder SubmissionID = new StringBuilder();
		String rName = courseGradedActivities.getRunFileName(fName);
		int k = -1;
		int offset = 0;
		for (int i = 0; i< rName.length();i++) {
			if (Character.isLetter(rName.charAt(i)) && ((k == -1) || (i-k == offset))) {
				if (k == -1) {
					k++;
					offset = i-k;
				}	
				SubmissionID.append(rName.charAt(i));
				k++;
			}
		}
		if ((offset+k < fName.length()-1) && (offset+k > 1)) {
			int i = offset+k;
			int sIDOffset = fName.indexOf(studentID, offset);
			if (sIDOffset < i)
				sIDOffset = rName.length();
			int j = 0;
			while ((i < rName.length()) && (i < sIDOffset)) {
				j ++;
				if (Character.isDigit(rName.charAt(i)))
					SubmissionID.append(rName.charAt(i));
				if (j == 2)
					i = sIDOffset; //to exit the loop, only 2 positions for the activity name are allowed to get its number
				i++;
			}
		}
		String subID = SubmissionID.toString();
		if (subID == null)
			subID = getSubmissionIDFromPath (courseID, pName) ;
		if (subID.length() <= 0)
			subID = getSubmissionIDFromPath (courseID, pName) ;
		
		courseGradedActivities g = validateSubmissionID (courseID, subID, pName);
		return g;
		*/
		if (fName == null)
			return null;
		if (fName.length() <= 0)
			return null;
		for (Iterator<Course> i = runMarks.courses.iterator(); i.hasNext(); ) {
			Course c = i.next();
			String cID = c.getID();
			if (cID.toLowerCase().trim().equalsIgnoreCase(courseID.toLowerCase().trim())) {
				for (Iterator<courseGradedActivities> j = c.gradedActivities.iterator(); j.hasNext(); ) {
					courseGradedActivities g = j.next();
					String cPrefix = g.getID().trim().toLowerCase().substring(0, 3);
					String gNum = g.getID().trim().toLowerCase().substring(3, g.getID().trim().toLowerCase().length());
					if (fName.toLowerCase().trim().indexOf(g.getID()) >= 0)
						return g;
					else if (pName.toLowerCase().trim().indexOf(g.getID()) >= 0)
						return g;
					//else if ((fName.toLowerCase().trim().indexOf(cPrefix) >= 0) && (fName.toLowerCase().trim().indexOf(gNum) > 0))
					//	return g;
				}				
			}
		}
		return null;
		
	}

	public boolean isValidSubmission (courseGradedActivities g, String courseID,String studentID, String subID, String fName) {
	
		if ((courseID.length() >= runMarks.minCourseIDLen) && (courseID.length() <= runMarks.maxCourseIDLen) &&
				(studentID.length() >= runMarks.minStudentIDLen) && (studentID.length() <= runMarks.maxStudentIDLen) &&
				(subID.length() >= runMarks.minSubIDLen) && (subID.length() <= runMarks.maxSubIDLen) && g.hasValidExtension(fName))
			return true;
		return false;
		
	}
	
	public void listSubmissions (String Path) {
		File folder = new File(Path);
		File[] submissions = folder.listFiles();
		if (runMarks.debugLevel == 0)
			System.out.println ("Path = "+ Path + " has " + submissions.length + " items: " + submissions.toString());
		for (int i = 0; i < submissions.length; i++) {
        	//String submissionID = null;
        	if (submissions[i].isFile()) {		        
        		gradedSubmissions gs = new gradedSubmissions();
        		gs.fName = submissions[i].getName();
        		gs.pName = submissions[i].getPath().substring(0, submissions[i].getPath().lastIndexOf('/'));
        		
        		gs.studentID = getStudentID(gs.pName+"/"+gs.fName , 0);
	        	if (gs.studentID.length() > runMarks.minStudentIDLen) {
	        		gs.sDate = new Date(submissions[i].lastModified());
		        	gs.courseID = getCourseIDFromPath (gs.pName);
		        	courseGradedActivities g = getGradedActivity(gs.courseID, gs.fName, gs.pName);
		        	if (g != null) {
		        		gs.submissionID = g.getID();
			        	if (isValidSubmission(g, gs.courseID, gs.studentID, g.getID(), gs.fName)) {
			        		
			        		//String sDate = DateFunctions.getCalendarString(submissions[i].lastModified());
			        		gs.daysDelay = DateFunctions.daysDifference(g.getdeadline().getTime(), submissions[i].lastModified());
			        		if (g.isProgrammingActivity()) {
			        			// The multithreading causes SysCommandExecutor to confuse the current execution directory, will seek other alternatives later
			        	        //Thread t = new Thread(new processSubmission(gs, g));
			        			//t.start();
			        			//threads.add(t);
			        			//Thread.sleep(60000);
			        			
			        			if (g.compile(gs)) {
			        				g.runTests(gs);
			        		        g.mark(gs);
			        			} /* else will need to define keywords and sentences to mark the essay questions, or a format for the multiple choice*/
			        			if (runMarks.debugLevel > 0)
		        					Submissions.submissionsCount ++;
		        					System.out.println("["+submissionsCount+"]"+gs.courseID + "\t" + gs.pName + "\t" + gs.fName + "\t" + gs.studentID + "\t" + gs.submissionID + "\t" + gs.sDate + "\t"+gs.daysDelay+"\t"+gs.mark+"\t"+gs.percentage);

			        		}

			        		gSubmissions.add(gs);
			        	}
			        	else
			        		badSubmissions.add(gs);
		        	}
		        	else
		        		badSubmissions.add(gs);
		        }
		    } else if (submissions[i].isDirectory()) {
		    	listSubmissions(submissions[i].getPath());
		    }			
		}
	}
	
	public boolean saveMarks (String courseFile, String sPath) {
		
		
		for (int i= 0 ; i < threads.size();i++) {
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Date now = new Date(System.currentTimeMillis());
			String FileName = "marks_" + now.getDate() + "_" + now.getMonth() + "_" + now.getYear() + "_" + now.getHours() +"_" + now.getMinutes();
			File file = new File(runMarks.outputFolder+ FileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Marks Output for courses File: "+ courseFile + " For submissions in Folder: " + sPath + " on " + new Date(System.currentTimeMillis()).toString() + "\n");
			bw.write ("Bad Submissions: \n");
			for (Iterator<gradedSubmissions> i = badSubmissions.iterator(); i.hasNext(); ) {
				gradedSubmissions bs = i.next();
				bw.write (bs.courseID+"\t" + bs.submissionID+"\t"+bs.studentID+"\t"+bs.sDate.toString()+"\t"+bs.daysDelay+"\t"+bs.mark+"\t"+bs.percentage +"\t" + bs.fName + "\t" + bs.pName + "\n");
			}
			bw.write ("Good Submissions: \n");
			for (Iterator<gradedSubmissions> i = gSubmissions.iterator(); i.hasNext(); ) {
				gradedSubmissions gs = i.next();
				bw.write (gs.courseID+"\t" + gs.submissionID+"\t"+gs.studentID+"\t"+gs.sDate.toString()+"\t"+gs.daysDelay+"\t"+gs.mark+"\t"+gs.percentage +"\t" + gs.fName + "\t" + gs.pName + "\n");
			}
			bw.write("\n\n");
			bw.close();
			//System.out.println(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return true;
		
	}

}
