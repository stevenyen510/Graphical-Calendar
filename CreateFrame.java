/** CS151 Programming Assignment 4: GUI Calendar 
* this file contains the create frame that lets user enter new event info
*@author Steven Yen <steven.yen@sjsu.edu>
*@version 1.0
*Copyright 2016 Steven Yen
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Class specifying look of Create Frame
 * That opens when user clicks on the "Create" button on the
 * main window. This Frame allows user to enter event title
 * and event start time and end time.
 * @author Steven Yen
 */
public class CreateFrame extends JFrame{
	
	/**
	 * Constuctor specifying the look of the Create Frame
	 * Also attaches the buttons. This frame contains reference to the 
	 * EventsManager, or "Model", through which the "Controller" can call "Update" method
	 * @param ev the EventsManager associated with this frame, for use by it's components.
	 */
	public CreateFrame(EventsManager ev)
	{
		this.setSize(380,100);
	
		this.setLayout(new FlowLayout());
		
		this.add(new JLabel("Event Title"));
		
		JTextField eventTitleField = new JTextField("Untitled Event.",25);
		//eventTitleField.setSize(50,1);
		
		this.add(eventTitleField);
		
		JTextField startTimeField = new JTextField("00:00");
		
		JTextField endTimeField = new JTextField("00:00");
		
		this.add(new JLabel("(24-HR Format) Time"));
		this.add(startTimeField);
		
		this.add(new JLabel("  to  "));
		this.add(endTimeField);
		
		/**
		 *The SAVE button and the associated ActionListener represents a "Controller"
		 *When clicked, the button passes an event object to the anonymous ActionListener by
		 *Invoking the actionPerformed method. The actionPerformed method then calls the
		 *"Update" mutator method of "Model" (EventsManager ev), changing the data stored in
		 *The EventsManager ev's TreeMap. If there's time conflict or date entered is not valid,
		 *The "Update" is not invoked on "Model", user is prompted to reenter a valid date.
		 */
		JButton saveButton = new JButton("SAVE");
		this.add(saveButton);
		saveButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e)
			{
				//Checks the format of the time to make sure it is valid HH:MM
				//Displays error if it is not, and does not go through with event creation.
				if((startTimeField.getText().length()>5) || (endTimeField.getText().length()>5))
				{
					//System.out.println(startTimeField.getText());
					JOptionPane.showMessageDialog(null,"Invalid, please enter time in 24-HR format HH:MM.","Error Message",JOptionPane.ERROR_MESSAGE,null);
					return; 
				}				
			
				//Once format is validated, begin parsing entered text.
				String[] startTimeArray = startTimeField.getText().split(":");
				GregorianCalendar startTime = new GregorianCalendar(
					ev.getSelectedDate().get(GregorianCalendar.YEAR),
					ev.getSelectedDate().get(GregorianCalendar.MONTH),
					ev.getSelectedDate().get(GregorianCalendar.DAY_OF_MONTH),
					Integer.parseInt(startTimeArray[0]),
					Integer.parseInt(startTimeArray[1]));
			
				String[] endTimeArray = endTimeField.getText().split(":");
				GregorianCalendar endTime = new GregorianCalendar(
					ev.getSelectedDate().get(GregorianCalendar.YEAR),
					ev.getSelectedDate().get(GregorianCalendar.MONTH),
					ev.getSelectedDate().get(GregorianCalendar.DAY_OF_MONTH),
					Integer.parseInt(endTimeArray[0]),
					Integer.parseInt(endTimeArray[1]));																
					
				//System.out.println("Start Date"+startTime);
				//System.out.println("End Date"+endTime);

				ArrayList<Event> temp = ev.getDailyEventList(ev.getSelectedDate());
				
				boolean issue = false;
				
				
				//handle case where entered time has time conflict with existing event.
				//Pops up error message telling user to enter valid time without conflict.
				for(Event el: temp)
				{
					if(((endTime.after(el.getStartTime())) && (endTime.before(el.getEndTime())))||
					((startTime.after(el.getStartTime())) && (startTime.before(el.getEndTime())))||
					((startTime.equals(el.getStartTime())) && (endTime.equals(el.getEndTime())))||
					((startTime.equals(el.getStartTime())) && (endTime.before(el.getEndTime())))||
					((startTime.after(el.getStartTime())) && (endTime.equals(el.getEndTime()))) ||
					((startTime.equals(el.getStartTime())) && (endTime.after(el.getEndTime()))) ||
					((startTime.before(el.getStartTime())) && (endTime.equals(el.getEndTime()))))
					{
						//System.out.println("Time Conflict"); 
						String errorMessage = "The new event time entered conflicts with existing event(s).\n Please enter a different time.";
						JOptionPane.showMessageDialog(null,errorMessage,"Error Message",JOptionPane.ERROR_MESSAGE,null);
						
						issue = true;
						break;
					}
						
					//System.out.println(e.getStartTime());
					//System.out.println(e.getEndTime());
				}
				
				//handle case where user enters a end time that is later than the star time. 
				//Pops up error message telling user to enter valid time.
				if(endTime.before(startTime))
				{
					JOptionPane.showMessageDialog(null,"End time should be after start time! Please enter a different time.","Error Message",JOptionPane.ERROR_MESSAGE,null);
					issue =true;
				}
								
				if(issue==false)
				{
					GregorianCalendar temp1 = ev.getSelectedDate();
					String newDate = (temp1.get(GregorianCalendar.MONTH)+1)+"/"+
							temp1.get(GregorianCalendar.DAY_OF_MONTH)+"/"+
							temp1.get(GregorianCalendar.YEAR);
			
					ev.create(eventTitleField.getText(), newDate, startTimeField.getText(),endTimeField.getText());
					setVisible(false); //not sure how this line knows who it's impliciting calling object is...
				}
				
				
				
			}
		});
		
		//check for conflict

		GregorianCalendar temp2 = ev.getSelectedDate();
		String frameTitle = "Create event on: "+(temp2.get(GregorianCalendar.MONTH)+1)+"/"+temp2.get(GregorianCalendar.DAY_OF_MONTH)+"/"+temp2.get(GregorianCalendar.YEAR);
		
		//this.pack();
		this.setTitle(frameTitle);
		this.setVisible(true);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
	
}
