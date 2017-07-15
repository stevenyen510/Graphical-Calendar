/** CS151 Programming Assignment 4: GUI Calendar
* This file contains the main method that creates all the GUI components
* This file also contains a few helping methods.
*@author Steven Yen <steven.yen@sjsu.edu>
*@version 1.0
*Copyright 2016 Steven Yen
*/

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;

/**
* enum constants for the calendar months
*/
enum MONTHS
{
	Jan, Feb, March, Apr, May, June, July, Aug, Sep, Oct, Nov, Dec;
}

/**
* enum constants for the days of a week
*/
enum DAYS
{
	Sun, Mon, Tue, Wed, Thur, Fri, Sat ;
}

/**
* A class that creates the GUI components to interact with user.
*/
public class SimpleCalendar {

	/**
	* Main method that creates the display Frames. Also responsible for importing saved
	* events from file titled events.txt.
	*/
    public static void main(String[] args) throws FileNotFoundException 
    {
        
        EventsManager aEventsManager = new EventsManager();  //create a new EventsManager with the data structure. This is also the data model of MVC.
        
        CalendarFrame newFrame = new CalendarFrame(aEventsManager); //this frame is a ChangeListener. It is a view.
        
        aEventsManager.attach(newFrame); //attache the listener/view to the data model. Stores it to dataModel's arrayList of Listeners.
        
        importFromFile("events.txt",aEventsManager); //Calls helping  function to import from text file called event.txt if it exists.
            
    }
       
    /**
     * Imports scheduled event from the file into the a event manager's data structure
     * @param fileName name of the file containing previous scheduled events (include extension).
     * @param someEM name of the EventsManager object into which to import the scheduled events.
     * @throws FileNotFoundException 
     */
    public static void importFromFile(String fileName, EventsManager someEM) throws FileNotFoundException
    {
        File file = new File(fileName);
        
        if(file.exists())
        {
            Scanner fin = new Scanner(new FileInputStream(fileName));
            
            while(fin.hasNextLine())
            {
                String rowOfText = fin.nextLine();
                //String dateString = rowOfText.substring(0,10);
                //System.out.println(dateString); //stmore "10/09/2015" correctly
                
                String[] substrings = rowOfText.split(": ");//first one is date, second one starts with time.
                
                //System.out.println("substrings[0]="+substrings[0]+"<<");
                //System.out.println("substrings[1]="+substrings[1]+"<<");
                //substrings[0] store the date correctly in the format MM/DD/YYYY
                
                String dateString = substrings[0];
            //    System.out.println("dateString="+dateString+"<<");
                //dateString stores the date correctly in the format MM/DD/YYYY
                
                
                String startString = substrings[1].substring(0,5);
            //    System.out.println("startString="+startString+"<<"); //string representing starting time 
                //startString stores start time correctly in the format HH:MM
                
                String endString =null;
                String eventTitle = null;
                
                if (rowOfText.contains(" - "))
                {
                    String[] substrings2 = rowOfText.split(" - ");
                    endString = substrings2[1].substring(0,5);
                    eventTitle = substrings2[1].substring(6,substrings2[1].length());
                }
                else
                {
                    eventTitle = rowOfText.substring(18,rowOfText.length());
                }
                
            //    System.out.println("endString="+endString+"<<");
                //endString correctly stores the time in the format HH:MM //if non, sets it to null
                
            //    System.out.println("eventTitle="+eventTitle+"<<");
                
                someEM.create(eventTitle, dateString, startString, endString);
                
            }

        //    someEM.eventList();
        
            fin.close();
        }
        else
        {
            System.out.println("No existing "+fileName+" file found. This is the first run.");
        }
            
    }
    
    /**
     * Helping method returning an ArrayList containing the labels for the days of the current month
     * This method is used in the CalendarFrame when labeling the days of month.
     * @param c the current day from which to determine the current month
     * @return ArrayList of labels for the days of the current month.
     */
    public static ArrayList<String> getLablesForMonth(GregorianCalendar c)
    {
		
		int currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH); // as DAY OF MONTh
        GregorianCalendar temp = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
        int firstWeekDayOfMonth = temp.get(Calendar.DAY_OF_WEEK);  //as DAY OF WEEK
        
        temp.add(Calendar.MONTH, 1);
        temp.add(Calendar.DAY_OF_YEAR, -1);
        int lastDayOfMonth = temp.get(Calendar.DAY_OF_MONTH);
        
        ArrayList<String> dayLabels = new ArrayList<>();
        //String[] dayLabels = new String[42];
       
         
        for(int i=1; i<firstWeekDayOfMonth;i++)
        {
            //System.out.print("    ");
            dayLabels.add(" ");
        }
        
        for(int i=1; i<=lastDayOfMonth;i++)
        {
            
            if(i==currentDayOfMonth)
                {
            		//System.out.printf("[%2d]",i);
            		dayLabels.add(i+"");
                }
            else
                {
            		//System.out.printf("  %2d",i);
            		dayLabels.add(i+"");
                }
        }
        
        int space2fill = 42-(firstWeekDayOfMonth-1)-lastDayOfMonth;
        
        for(int i=0;i<space2fill;i++)
        {
        	dayLabels.add(" ");
        }
        
        return dayLabels;
   
    }
    
      
}
