import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Course {
	private String ID, Title;
	String[] studentsIDs;
	List <courseGradedActivities> gradedActivities;

	Course (String cID, String cTitle) {
		ID = cID;
		Title = cTitle;
	}
	
	public String getID () {
		return ID;
	}
} // end class Course
