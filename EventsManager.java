/** CS151 Programming Assignment 4: GUI Calendar
* this file contains the EventsManager Class
*@author Steven Yen <steven.yen@sjsu.edu>
*@version 1.0
*Copyright 2016 Steven Yen
*/

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;

import javax.swing.event.ChangeListener;

import java.util.Collections;
import java.util.GregorianCalendar;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import javax.swing.event.*;


/**
* EventsManager ADT stores scheduled events in a TreeMap whose key is of GregorianCalendar type
* This represents the data model portion of the MVC.
* and the value of each entry is an array list containing the scheduled events on the date
* represented by the key of that entry. This class also contains methods for creating, deleting and view events.
*/
public class EventsManager {
    
    private TreeMap<GregorianCalendar,ArrayList<Event>> allEvents; //this is the DataModel
    private GregorianCalendar selectedDate; //this is also the DataModel
    private ArrayList<ChangeListener> cListeners;
    
    public static MONTHS[] arrayOfMonths = MONTHS.values();
    public static DAYS[] arrayOfDays = DAYS.values();
       
    /**
     * Constructor for EventsManage (the Data Model in MVC).
     * It initializes the data, which consist of both allEvents and selectedDate.
     * selectedDate is initialized to be the current date.
     * It also initializes the ArrayList of ChangeListeners.
     */
    EventsManager()
    {
        allEvents = new TreeMap<GregorianCalendar,ArrayList<Event>>();
        cListeners = new ArrayList<ChangeListener>(); //this line is important!! otherwise you get null ptr exception
        //because only variable is declared, no arraylist created
        
        //initialize instance variable selected day to today. But need to exclude the time first, b/c our key is only the date.
        GregorianCalendar temp = new GregorianCalendar();
        selectedDate = new GregorianCalendar(temp.get(GregorianCalendar.YEAR),temp.get(GregorianCalendar.MONTH),temp.get(GregorianCalendar.DAY_OF_MONTH));
    }
    
    /**
     * Select a different date. This is one of the update method of Data Model.
     * It is a mutator that changes the data, and then notifies all views in cListeners.
     * @param d is the date selected.
     */
    public void selectDate(GregorianCalendar d)
    {
    	selectedDate = d;
    	//System.out.println("selectDate");
    	
    	for(ChangeListener l: cListeners)
    	{
    		l.stateChanged(new ChangeEvent(this));
    		//System.out.println("listeners notified");
    	}
    		
    }
    
    /**
     * Accessor method to check value of selectedDate
     * The selectedDate helps the View determine which day's event to show in day view on the right panel.
     * This is one of the getData() methods of this data model.
     * @return return the instance variable selectedDate of EventsManager object.
     */
    public GregorianCalendar getSelectedDate()
    {
    	return selectedDate;
    }
    
    
	/**
	 * Accessor method to get the ArrayList of events corresponding to the date entered.
	 * This ArrayList allows the CreateFrame class to check for time conflicts, by running
	 * through the events and accessing the start and end times.
	 * This is another getData() method of this data model.
	 * @param GregorianCalendar key representing the day for which to retrieve ents.
	 * @return ArrayList<Event> of events on the day entered in the explicit param.
	 */
	public ArrayList<Event> getDailyEventList(GregorianCalendar key)
	{
		if(allEvents.containsKey(key))
		{
			return allEvents.get(key);
		}
		else
		{
			return new ArrayList<Event>();
		}
		
	}
	
	
    
    /**
     * Create a new event on the calendar
     * This is another update method of data model in MVC.
     * It updates a portion of the data (allEvents), and then notifies all view in cListeners.
     * @param title String title of the event
     * @param date String representing the time in the form MM/DD/YYYY (here Oct=10, Jan=01,...)
     * @param startTime String representing the start time in form HH:MM
     * @param endTime String representing the end time in for HH:MM
     */
    public void create(String title, String date, String startTime, String endTime)
    {
        int Year, Month, Day, Hour, Minute;
        
        String[] temp = date.split("/");
        Year = Integer.parseInt(temp[2]);
        Month = Integer.parseInt(temp[0])-1; //because while we rep Oct by 10, GregorianCalendar rep it as 9 (Jan = 0)
        Day = Integer.parseInt(temp[1]);
        
        //System.out.print("User entered date string parsed as:");
        //System.out.print("Year="+Year);
        //System.out.print("Month="+Month);
        //System.out.print("Day="+Day);
        

        
        String[] temp2 = startTime.split(":");
        Hour = Integer.parseInt(temp2[0]);
        Minute = Integer.parseInt(temp2[1]);
        //System.out.println();
        //System.out.print("User entered start time parsed as:");
        //System.out.print("Hour="+Hour);
        //System.out.print("Minute="+Minute);
        //System.out.println();
        
        GregorianCalendar key = new GregorianCalendar(Year,Month,Day);
        //System.out.println(key.get(Calendar.MONTH));
       
        //this way, it'll still work if user enters day like 5/3/2016 or time like 6:24
        GregorianCalendar startDT = new GregorianCalendar(Year,Month,Day,Hour,Minute);
        //System.out.println("start date and time:" + startDT.toString());
        //System.out.println("DAY_OF_WEEK="+startDT.get(Calendar.DAY_OF_WEEK));
        
        
        GregorianCalendar endDT;
        
        if(endTime!=null)
        {
            temp2 = endTime.split(":");
            Hour = Integer.parseInt(temp2[0]);
            Minute = Integer.parseInt(temp2[1]);

            endDT = new GregorianCalendar(Year,Month,Day,Hour,Minute);
           //System.out.println("end date and time:"+endDT.toString());
        }
        else
        {
            endDT=null;
        }
           
        Event newEvent = new Event(startDT,endDT,title);
        
        if(allEvents.containsKey(key))
        {
            allEvents.get(key).add(newEvent);
        }
        else
        {
            allEvents.put(key, new ArrayList<Event>());
            allEvents.get(key).add(newEvent);
        }
        
        Collections.sort(allEvents.get(key)); //sort arrayList for that day (key) 
        //sort using the compareTo method of event, which compares by start date and time
        
    	//Invokes the "notify" method of all view objects attached.
        for(ChangeListener l: cListeners)
    	{
    		l.stateChanged(new ChangeEvent(this));
    		//System.out.println("listeners notified");
    	}
    }
       
    /**
     * This is the other accessor method that combines all the events on a given day
     * in a single String variable. This is for ease of updating the day view of the
     * right panel on the CalendarFrame.
     * @param key the date for which the day view needs to be displayed.
     * @return returns an String ArrayList containing all the events on that day.
	 */
    public String dailyEvents(GregorianCalendar key)
    {
    	//selectDate(key); //i added this line to update view
        
    	String daysEvents = arrayOfDays[key.get(Calendar.DAY_OF_WEEK)-1]+", "+
                arrayOfMonths[key.get(Calendar.MONTH)]+" "+
                key.get(Calendar.DAY_OF_MONTH)+", "+
                key.get(Calendar.YEAR)+"\n";
    	
    	//System.out.println(daysEvents);

        //ArrayList<String> daysEvents = new ArrayList<String>();
        
        if(allEvents.containsKey(key))
        {
            for(Event e:allEvents.get(key))
            {
                //System.out.println(e.getEvent()+" "+e.toString());
                daysEvents= daysEvents+(e.toString()+" "+e.getEvent())+"\n";
            }
        }
        
        
        return daysEvents;
    }
       
    /**
     * Writes scheduled events currently in the calendar to file and save before exit.
     * @param fileName name of the file to write/save scheduled events to (including extension)
     */
    public void saveDataToFile(String fileName) throws FileNotFoundException
    {
        PrintWriter outputStream = new PrintWriter(new FileOutputStream(fileName));
        
        for(GregorianCalendar k: allEvents.keySet())
        {
            for(Event e: allEvents.get(k))
            {
                outputStream.println(e.lineListing());
            }
        }
        
        outputStream.close();
    }
    
    
    /**
     * Attach a listener to the DataModel. All listeners are notified when data is updated.
     * This is the "Attach" method of the MVC model. 
     * @param c the listener representing the "view" portion of MVC.
     */
    public void attach(ChangeListener c)
    {
    	cListeners.add(c);
    }
       
}
