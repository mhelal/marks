import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;



public class DateFunctions {
    /*function: getDateTime
     * returns a formated string for current date and time, or the passed date argument
     * */
    public static String  getDateTime (Date initDate) {
  	  Calendar currentDate = Calendar.getInstance();
  	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
  	  String dateNow;
	  if (initDate == null)
		  dateNow = formatter.format(currentDate.getTime());
	  else
		  dateNow = formatter.format(initDate);
  	  return dateNow;
  }

    /*function: getCalendarString
     * returns a formated string for the passed Calendar argument
     * */
    public static String  getCalendarString (long datetime) { 
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
	  	long nowMillis = System.currentTimeMillis();
	  	int days = daysDifference(nowMillis, datetime);
	  	int months = monthsDifference(nowMillis, datetime);
	  	Calendar cal = Calendar.getInstance();
	  	//Date now = Date.UTC(cal.YEAR, cal.MONTH, cal.DATE, cal.HOUR, cal.MINUTE, cal.SECOND);
	  	Date date = new Date( datetime);
	  	
	  	
	  	String dateNow = formatter.format(date);
	  	return dateNow;
	  }

    public static String  getCalendarString (Calendar datetime) { 
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
	  	long calVal = datetime.getTimeInMillis();
	  	long now = System.currentTimeMillis();
	  	int days = daysDifference(now, calVal);
	  	int months = monthsDifference(System.currentTimeMillis(), datetime.getTimeInMillis());
	  	Date date = datetime.getTime();
 		int secs = date.getSeconds();
 		int mins = date.getMinutes();
 		int hours = date.getHours();
 		int day = date.getDate();
 		int month = date.getMonth();
 		int year = date.getYear();
 		
	  	//String dateNow = formatter.format(datetime.getTime());
	  	String dateNow = formatter.format(date);
	  	return dateNow;
	  }

    /*function: DaysDifference
     * returns a difference in days between two Calendar dates
     * */
 	public static int monthsDifference(long a, long b) {
 		Calendar aCal = Calendar.getInstance();
 		aCal.setTimeInMillis(a);
 		Calendar bCal = Calendar.getInstance();
 		bCal.setTimeInMillis(b);
 		
 		Date aDate = aCal.getTime();
 		Date bDate = bCal.getTime();
 		int aMonth = aDate.getMonth();
 		int bMonth = bDate.getMonth();
 		return (int) aMonth-bMonth;
 		
 	}

 	public static int daysDifference(Date a, Date b) {
 		int difference = 0;
 		TimeZone DefaultTimeZone = TimeZone.getDefault();
 		Locale DefaultLocale = Locale.getDefault();//new Locale("Arabic","Egypt") 
 		Calendar bCalendar = GregorianCalendar.getInstance(DefaultTimeZone, DefaultLocale);
		bCalendar.setTime(b);
 		
 		if (a.getYear() == b.getYear()) {
 			if (a.getMonth() == b.getMonth()) {
 				difference = a.getDate() - b.getDate();
 			}
 			else {
 				int numberOfDaysInMonth = bCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
 				int remDaysInMonth = numberOfDaysInMonth - bCalendar.get(Calendar.DATE);
 				difference = remDaysInMonth + a.getDate();
 			}
 		}
 		else {
				int numberOfDaysInYear = bCalendar.getActualMaximum(Calendar.DAY_OF_YEAR);
 				int remDaysInYear = numberOfDaysInYear - bCalendar.get(Calendar.DAY_OF_YEAR);
 				Calendar aCalendar = GregorianCalendar.getInstance(DefaultTimeZone, DefaultLocale);
 				aCalendar.setTime(a);
 				difference = remDaysInYear + aCalendar.get(Calendar.DAY_OF_YEAR);

 		}
 		
 		return difference;
 		
 	}

 	public static int daysDifference(long a, long b) {
 		long difference = 0;
 		difference = a-b;
 		difference = difference/(24 * 60 * 60 * 1000) % 60; // days from milliseconds;
 		
 		return (int) difference;
 		
 	}

 	public static int hoursDifference(long a, long b) {
 		long difference = 0;
 		difference = a-b;
 		difference = difference/(60 * 60 * 1000) % 60; // hours from milliseconds
 		
 		return (int) difference;
 		
 	}
 	
 	public static int minsDifference(long a, long b) {
 		long difference = 0;
 		difference = a-b;
 		difference = difference/(60 * 1000) % 60; // minutes from milliseconds
 		
 		return (int) difference;
 		
 	}
	
 	public static int secsDifference(long a, long b) {
 		long difference = 0;
 		difference = a-b;
 		difference = difference/1000 % 60; ; // seconds from milliseconds
 		return (int) difference;
 		
 	}
 	
 	public static int DaysDifference(Calendar a, Calendar b) {
	    int tempDifference = 0;
	    int difference = 0;
	    Calendar earlier = Calendar.getInstance();
	    Calendar later = Calendar.getInstance();

	    if (a.compareTo(b) < 0)
	    {
	        earlier = a;
	        later = b;
	    }
	    else
	    {
	        earlier = b;
	        later = a;
	    }

	    while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR))
	    {
	        tempDifference = 365 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
	        difference += tempDifference;

	        earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
	    }

	    if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR))
	    {
	        tempDifference = later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
	        difference += tempDifference;

	        earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
	    }

	    return difference;
	}
	
    
    /*function: MinutesDifference
     * returns a difference in minutes between two Calendar dates
     * */
	public static double MinutesDifference(Calendar a, Calendar b)
	{
	    double difference = 0;
	    
	    int daysDiff = DaysDifference (a, b);
	  
	    
    	if ((daysDiff > 0) && (b.get(Calendar.HOUR) < a.get(Calendar.HOUR))) {
    		difference += (daysDiff - 1) * 24 * 60 * 60;
    		difference += (12 - a.get(Calendar.HOUR)) * 60;
    		difference += b.get(Calendar.HOUR) * 60;
    	}
    	else if (daysDiff > 0) {
    		difference += daysDiff * 24 * 60 * 60;
    		difference += (b.get(Calendar.HOUR) - a.get(Calendar.HOUR)) * 60;
    	}
    	else {
    		difference += (b.get(Calendar.HOUR) - a.get(Calendar.HOUR)) * 60;    		
    	}
    	
    	if  (b.get(Calendar.MINUTE) < a.get(Calendar.MINUTE)) {
    		difference += 60 - a.get(Calendar.MINUTE);
    		difference += b.get(Calendar.MINUTE);
    	}
    	else {
    		difference += (b.get(Calendar.MINUTE) - a.get(Calendar.MINUTE));    		
    	}

    	return difference;
	    
	}
    
    /*function: setCalendar
     * returns a Calendar object for the passed datetime String argument
     * */
    public static Calendar setCalendar (String datetime) {
	     try {
	     
		    Date date ; 
		    DateFormat  formatter = new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
			date = (Date)formatter.parse(datetime);
			Calendar cal=Calendar.getInstance();
		    cal.setTime(date);
		    return cal;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("ParseException Message: " + e.getMessage());
			e.printStackTrace();
		} 
	    return null;
   }

    public static Calendar setCalendar (Date datetime) {
	     try {
	     
		    Date date ; 
		    DateFormat  formatter = new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
			date = (Date)formatter.parse(datetime.toString());
			Calendar cal=Calendar.getInstance();
		    cal.setTime(date);
		    return cal;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("ParseException Message: " + e.getMessage());
			e.printStackTrace();
		} 
	    return null;
    }
    public static Calendar setCalendar (Long millis) {
    	Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return cal;
    }

    public static Date setDate (String datetime, String format) {
    	String[] tokens = datetime.split("[/: ]");
    	int year = 0, month = 0, day = 0, hour = 0, min = 0, i = 0;
    	if (tokens.length > i) {
			day = Integer.parseInt(tokens[i]);
			i++;
		}
    	if (tokens.length > i) {
			month = Integer.parseInt(tokens[i]);
			i++;
		}
    	if (tokens.length > i) {
			year = Integer.parseInt(tokens[i]);
			i++;
		}
    	if (tokens.length > i) {
			hour = Integer.parseInt(tokens[i]);
			i++;
		}
    		
    	if (tokens.length > i) {
			min = Integer.parseInt(tokens[i]);
			i++;
		}
    	Date date = new Date();
    	date.setDate(day);
    	date.setMonth(month-1);
    	date.setYear(year);
    	date.setHours(hour);
    	date.setMinutes(min);
    	return date;
	   /*  	    
		try {
		    DateFormat  formatter = new SimpleDateFormat(format);
			Date date = (Date)formatter.parse(datetime);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;*/
	}
}
