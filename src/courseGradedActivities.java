import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class courseGradedActivities {
	
	private String ID;
	private String courseID;
	private double mark, percentage;
	private Date deadline;
	private String validExtension;
	private boolean progActivity;
	private String cString, rString;
	private String validFile, inputFile;
	private int outputLines;
	private String outputFile;

    public BufferedReader inp;
    public BufferedWriter out;
	
	courseGradedActivities (String gID, String cID, int m, int p, Date dDate, String vExtension, boolean programming, String caString, String raString, String vFile, String iFile) {
		ID = gID;
		courseID = cID;
		mark = m;
		percentage = p;
		deadline = dDate;
		validExtension = vExtension;
		progActivity = programming;
		cString = caString;
		rString = raString;
		validFile = vFile;
		inputFile = iFile;
	}
	public String getID () {
		return ID;
	}

	public Date getdeadline () {
		return deadline;
	}
	
	public boolean isProgrammingActivity () {
		return progActivity;
	}
	public void print(String s, String fName) {
		try {
			File file = new File(runMarks.outputFolder+ "/"+courseID+"_"+fName + "_out");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s+"\n");
			outputFile = outputFile + s + "\n";
			outputLines ++;
			bw.close();
			//System.out.println(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public void pipe(String msg) {
        //String ret;
        try {
            out.write( msg + "\n" );
            out.flush();
            //ret = inp.readLine();
            //return ret;
        }
        catch (Exception e) {
			e.printStackTrace();

        }
    }
	
    public static String getRunFileName (String javaSourceFileName) {
    		return javaSourceFileName.replaceAll(".java", "").replaceAll(".c", "").replaceAll(".c++", "").replaceAll(".c#", "").replaceAll(".docx", "").replaceAll(".doc", "").replaceAll(".pdf", "")       ;
    }
    
    public boolean hasValidExtension (String fName) {
    	String[] validExtensions = validExtension.split(",");
    	for (int i=0;i<validExtensions.length;i++)
    		if (fName.indexOf(validExtensions[i]) > 0)
    			return true;
		return false;
    }
	public boolean compile(Submissions.gradedSubmissions gs)  {  
		
	    SysCommandExecutor commExec = new SysCommandExecutor();
	    commExec.setWorkingDirectory(gs.pName);
		try {
			if (commExec.runCommand("cd "+gs.pName) != 0) {
				print("Command retuned non Zero Exit Status!", gs.fName);
				print(commExec.getCommandError(), gs.fName);
			}
			print(commExec.getCommandOutput(), gs.fName);
			if (commExec.runCommand("pwd ") != 0) {
				print("Command retuned non Zero Exit Status!", gs.fName);
				print(commExec.getCommandError(), gs.fName);
			}
			print(commExec.getCommandOutput(), gs.fName);
			if (commExec.runCommand(cString + " " + gs.fName) != 0) {
				print("Command retuned non Zero Exit Status!", gs.fName);
        		if (runMarks.debugLevel > 1)
        			System.out.println("Command retuned non Zero Exit Status!");
				print(commExec.getCommandError(), gs.fName);
				print(commExec.getCommandOutput(), gs.fName);
				return false;
			}
			print(commExec.getCommandOutput(), gs.fName);
    		if (runMarks.debugLevel > 1)
    			System.out.println(commExec.getCommandOutput());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
    }  

    public boolean runTests(Submissions.gradedSubmissions gs)  {  
    	outputLines = 0;
		String rName = getRunFileName(gs.fName);
	    String command = null;
	    if (rString.indexOf("*") > 0)
	    	command = rString.replace("*", rName); 
	    else
	    	command = rString + " " + rName;
	    try {	        
	        //System.out.println(System.getProperty("user.dir"));
	        Runtime runtime = Runtime.getRuntime();
	        
	        Process p = runtime.exec(command, null, new File(gs.pName));

	        
	        
	        inp = new BufferedReader( new InputStreamReader(p.getInputStream()) );
	        out = new BufferedWriter( new OutputStreamWriter(p.getOutputStream()) );

	        if (inputFile != null) 
	        if (inputFile.length() > 0) {
	        	BufferedReader br = null;
				try {
					String sCurrentLine;
					br = new BufferedReader(new FileReader(runMarks.baseFolder+"/"+inputFile));
					while ((sCurrentLine = br.readLine()) != null) {
						//print(pipe(sCurrentLine.trim()), gs.fName);
						pipe(sCurrentLine.trim());
					}
		 
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (br != null)br.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
	        }
	        String line = null;
	        while ((line = inp.readLine()) != null) {
				print(line, gs.fName);
				if (runMarks.debugLevel > 1)
					System.out.println(line);
			}
	        
	        inp.close();
	        out.close();
	    }

	    catch (Exception err) {
			if (runMarks.debugLevel > 1)
				err.printStackTrace();
			Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, err);  
	        return false;
	    } 
	    return true;
    }
    
	
    public void mark(Submissions.gradedSubmissions gs)  { 
        if (validFile != null) 
        if (validFile.length() > 0) {
        	BufferedReader br = null;
			try {
				String Line;
				br = new BufferedReader(new FileReader(runMarks.baseFolder+"/"+validFile));
				while ((Line = br.readLine()) != null) {
					String[] cTokens = Line.split("\t");
					if (cTokens[0].equals("lines")) {
						if (Integer.parseInt(cTokens[1].trim()) <= outputLines)
							gs.mark += Double.parseDouble(cTokens[2].trim()) /100 * mark;						
					}
					if (cTokens[0].equals("contains")) {
						if (outputFile.trim().toLowerCase().indexOf(cTokens[1].trim().toLowerCase()) >= 0 )
							gs.mark += Double.parseDouble(cTokens[2].trim()) /100.00 * mark;		
					}
					// (attention) patern matching not working
					if (cTokens[0].equals("matches")) {
							//Pattern p = Pattern.compile(cTokens[1]);
							//Matcher m = p.matcher(outputFile);
							if (Pattern.matches(cTokens[1], outputFile))
								gs.mark += Double.parseDouble(cTokens[2].trim()) /100.00 * mark;	
						
					}
				}
	 
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			gs.percentage = gs.mark/mark*percentage;
        }
    }
}
