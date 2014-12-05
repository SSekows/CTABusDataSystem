package main;

/**
 * Sources used in this project:
 * http://www.java2s.com/Code/Java/Chart/JFreeChartBarChartDemo.htm
 * http://www.jfree.org/jfreechart/ (open source charting program)
 * https://data.cityofchicago.org/Transportation/CTA-Ridership-Avg-Weekday-Bus-Stop-Boardings-in-Oc/mq3i-nnqe
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jfree.ui.RefineryUtilities;

/**
 * Main class to start the database connection and interact with the program.
 * @author Steve
 */
public class SystemRunner {

	final static String[] options = {"Get Stop Route Counts", "Get Route Stop Counts", "Get Furthest Stop"
			+ " In Compass Direction", "Calculate Distance Between Stops", "Exit"};
	
	final static String[] dirOptions = {"N", "S", "W", "E", "NW", "SW", "NE", "SE"};
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	DBUtils d = new DBUtils();
        try {
            d.startConnection(); //start the connection
            d.buildDatabase(); //build the database
            
            JFrame frame = new JFrame("Please select a utility");
            String utilitySelection = (String) JOptionPane.showInputDialog(frame, 
                    "Hi, which utility will you be using today?", "Utility Selection", JOptionPane.QUESTION_MESSAGE, 
                    null, options, options[0]); //select a utility to view
            while(!utilitySelection.equalsIgnoreCase("Exit")) {
            	if (utilitySelection.equalsIgnoreCase("Get Stop Route Counts")){ //get route stop counts
            		String input = JOptionPane.showInputDialog("How many results do you want to see?"
            				+ " For all results, type 'ALL'");
            		d.getStopRouteCounts(input);
            	} else if (utilitySelection.equalsIgnoreCase("Get Route Stop Counts")){ //get route stop counts
            		String input = JOptionPane.showInputDialog("How many results do you want to see?"
            				+ " For all results, type 'ALL'");
            		d.getRouteStopCounts(input);
            	} else if (utilitySelection.equalsIgnoreCase("Get Furthest Stop In Compass Direction")){ //get routes at compass extremes
            		JFrame frame1 = new JFrame("Please select a direction");
                    String dirSelection = (String) JOptionPane.showInputDialog(frame, 
                            "Please select a direction", "Direction Selection", JOptionPane.QUESTION_MESSAGE, 
                            null, dirOptions, dirOptions[0]);
                    d.getExtremeInCompassDirections(dirSelection);
            	} else if (utilitySelection.equalsIgnoreCase("Calculate Distance Between Stops")) { //calculate distance between two stops
            		String os1 = JOptionPane.showInputDialog("Enter on_street #1");
            		String cs1 = JOptionPane.showInputDialog("Enter cross_street #1");
            		String os2 = JOptionPane.showInputDialog("Enter on_street #2");
            		String cs2 = JOptionPane.showInputDialog("Enter cross_street #1");
            		d.calcDistance(os1, cs1, os2, cs2);
            	} else { //exit
            		System.exit(0);
            	}
            	utilitySelection = (String) JOptionPane.showInputDialog(frame, 
                        "Hi, which utility will you be using today?", "Utility Selection", JOptionPane.QUESTION_MESSAGE, 
                        null, options, options[0]); //pick another utility or exit
            }
            d.dropDatabase(); //drop the database
            d.closeConnection(); //close the connection
            System.exit(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
        	try {
        		d.dropDatabase(); //drop the database
				d.closeConnection(); //close the connection
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
    }
    
}

