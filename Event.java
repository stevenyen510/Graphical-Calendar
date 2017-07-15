/** CS151 Programming Assignment 4: GUI Calendar 
* this file contains Event class definition
*@author Steven Yen <steven.yen@sjsu.edu>
*@version 1.0
*Copyright 2016 Steven Yen
*/

import java.util.GregorianCalendar;
import java.util.Calendar;

/**
* Event class ADT stores information for each event. 
*/
public class Event implements Comparable<Event>{
    private GregorianCalendar start;
    private GregorianCalendar end;
    private String eventName;
          
    /**
     * Constructor that constructs an Event object.
     * @param start starting date and time of event
     * @param end ending date and time of event
     * @param eventName name of the vent
     */
    public Event(GregorianCalendar start, GregorianCalendar end, String eventName)
    {
        this.start = start;
        this.end = end;
        this.eventName = eventName;
    }

    /**
     * accessor method to get start date
     * @return GregorianCalendar object representing the start time (including date and time)
     */
    public GregorianCalendar getStartTime()
    {
    	return start;
    }
    
    /**
     * accessor method to get end date
     * @ return GregorianCalendar object representing the end time (including date and time)
     */
    public GregorianCalendar getEndTime()
    {
    	return end;
    }
        
    /**
     * Prints the time of the event
	 * @return a String representation with start time and end time of the event.
     */
    public String toString()
    {
        DAYS[] arrayOfDays = DAYS.values();
        MONTHS[] arrayOfMonths = MONTHS.values();
        
        String placeholder="";
        if(start.get(Calendar.MINUTE)<10){placeholder="0";}
        
        String ph1=""; //place holder for hour
        if(start.get(Calendar.HOUR_OF_DAY)<10){ph1="0";}
        
        
        if(end!=null)
        {
            String placeholder2="";
            if(end.get(Calendar.MINUTE)<10){placeholder2="0";}
            
            String ph2="";
            if(end.get(Calendar.HOUR_OF_DAY)<10){ph2="0";}

            return ph1+start.get(Calendar.HOUR_OF_DAY)+":"+placeholder+
                    start.get(Calendar.MINUTE)+" - "+ph2+
                    end.get(Calendar.HOUR_OF_DAY)+":"+placeholder2+
                    end.get(Calendar.MINUTE);
        }
        else
        {
            return ph1+start.get(Calendar.HOUR_OF_DAY)+":"+placeholder+
                    start.get(Calendar.MINUTE);            
        }
        
        
    }
    
    /**
     * Compares two events by start time. For sorting arraylist of events on a given day.
     * @param otherEvent the other event
     * @return +1 if GregorianCalendar of calling object precceeds otherEvent
     */
    public int compareTo(Event otherEvent) //compareTo coompares by start date.
    {
        return this.start.compareTo(otherEvent.start);
    }
    
    /**
     * Accessor method to get a String name of the event
     * @return String name of the event
     */
    public String getEvent()
    {
        return eventName;
    }
    
    
    /**
     * Return line of String for the event in format to write to file
     * @return complete listing of a event with date, start time, end time, and event name
	 */
    public String lineListing()
    {
        String dateString = "";
        
        String pl1="";
        String pl2="";
        
        if((start.get(Calendar.MONTH)+1)<10){pl1 ="0";}
        if(start.get(Calendar.DAY_OF_MONTH)<10){pl2 ="0";}
        
        return pl1+(start.get(Calendar.MONTH)+1)+"/"+
                pl2+(start.get(Calendar.DAY_OF_MONTH))+"/"+
                start.get(Calendar.YEAR)+": "+
                toString()+ " "+eventName;
    }
    
}
