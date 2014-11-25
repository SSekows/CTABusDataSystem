package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

/**
 * This class manages the main database operations.
 * @author Steve
 *
 */
public class DBUtils {
	
	Connection conn;
	DirectionUtils du;
	MapUtils mu;
	
	public DBUtils(){
		conn = null;
		du = new DirectionUtils();
		mu = new MapUtils();
	}
	
	/**
	 * Starts the connection to the sequel server. Localhost URL and username/password may vary
	 * among individual machines.
	 * @throws SQLException
	 */
	public void startConnection() throws SQLException{
		String myUrl = "jdbc:mysql://localhost:3306/mysql?zeroDateTimeBehavior=convertToNull";
        this.conn = DriverManager.getConnection(myUrl, "root", "");
	}
	
	/**
	 * Closes the connection. Usually called before exiting the program.
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException{
		conn.close();
	}
	
	/**
	 * Gives the direction to the server to use a certain database.
	 * Default database for this project is "chicago_buses".
	 * @param database
	 * @throws SQLException
	 */
	public void executeUse(String database) throws SQLException{
        String query = "USE " + database;
        ResultSet rs = null;
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.execute();
	}
	
	/**
	 * Gets the stop at the specified compass direction extreme. "Extreme" here means the 
	 * Northernmost stop (by latitude), the Westernmost stop (by longitude), etc.
	 * Stops at diagonal extremes were determined by taking various sums of the squares of
	 * the latitudes and longitudes. These stops might not be perfectly Northwest/Southwest/etc.
	 * of Chicago (after all, the stops are unevenly distributed, and Chicago is bordered by
	 * Lake Michigan to the east...)
	 * @param direction to be queried
	 * @throws SQLException
	 */
	public void getExtremeInCompassDirections(String direction) throws SQLException {
		String[] directionAndQuery = du.getDirectionAndQuery(direction);
		this.executeUse("chicago_buses");
		ResultSet rs = null;
        PreparedStatement preparedStmt = conn.prepareStatement(directionAndQuery[1]);
        rs = preparedStmt.executeQuery();
        rs.absolute(1);
        String message = directionAndQuery[0] + " stop: " + rs.getString(1) + "/" + rs.getString(2);
        JOptionPane.showMessageDialog(null, message);
        //System.out.println(directionAndQuery[0] + " stop: " + rs.getString(1) + "/" + rs.getString(2));
        //System.out.println();
	}
	
	/**
	 * Gets the stop with the maximum number of routes passing through it. Can be modified to
	 * return all stops with their respective counts of routes passing through them.
	 * @throws SQLException
	 */
	public void getStopRouteCounts(String printNum) throws SQLException{
		this.executeUse("chicago_buses");
		String query = "SELECT routes, on_street, cross_street FROM stopinfo";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();
        Map<String, Integer> stopRouteCounts = new HashMap<String, Integer>();
        while(rs.next()){
        	String routes = rs.getString("routes");
        	if (routes != null){
        		String[] indRoutes = routes.split(",");
        		String routeStop = rs.getString("on_street") + "/" + rs.getString("cross_street");
        		stopRouteCounts.put(routeStop, indRoutes.length);
        	}
        }
        stopRouteCounts = mu.sortByValue(stopRouteCounts);
        String[] outputLabels = {"Max # of routes", "At stop"};
        int pNum = 0;
        if (printNum.equalsIgnoreCase("all"))
        	pNum = stopRouteCounts.size() + 1;
        else
        	pNum = Integer.parseInt(printNum);
        mu.printMap(stopRouteCounts, outputLabels, pNum);
	}
	
	/**
	 * Gets the route that is the longest by number of stops. Can be modified to return all routes
	 * and their lengths by number of stops.
	 * @throws SQLException
	 */
	public void getRouteStopCounts(String printNum) throws SQLException{
		this.executeUse("chicago_buses");
		String query = "SELECT routes, on_street, cross_street FROM stopinfo";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();
        Map<String, Integer> routeStopCounts = new HashMap<String, Integer>();
        while(rs.next()){
        	String routes = rs.getString("routes");
        	if (routes != null){
        		String[] indRoutes = routes.split(",");
        		for (String s : indRoutes){
        			Integer count = routeStopCounts.get(s);
        			if (count == null)
        				routeStopCounts.put(s, 1);
        			else
        				routeStopCounts.put(s, count + 1);
        		}
        	}
        }
        routeStopCounts = mu.sortByValue(routeStopCounts);
        String[] outputLabels = {"Max # of stops", "On route"};
        int pNum = 0;
        if (printNum.equalsIgnoreCase("all"))
        	pNum = routeStopCounts.size() + 1;
        else
        	pNum = Integer.parseInt(printNum);
        mu.printMap(routeStopCounts, outputLabels, pNum);
	}
	
	/**
	 * Calculates the approximate distance in miles (Pythagorean, not Manhattan) between two valid
	 * bus stops.
	 * @param os1 on_street #1
	 * @param cs1 cross_street #1
	 * @param os2 on_street #2
	 * @param cs2 cross_street #2
	 * @throws SQLException
	 */
	public void calcDistance(String os1, String cs1, String os2, String cs2) throws SQLException {
		this.executeUse("chicago_buses");
		
		String query = "SELECT latitude, longitude, on_street FROM stopinfo WHERE on_street='" + os1 + "' AND cross_street='" + cs1 + "'";
		PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();
        if (!rs.next()){
        	JOptionPane.showMessageDialog(null, "ERROR: Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
        	return;
        }    
        rs.absolute(1);
        double lat1 = rs.getDouble(1), long1 = rs.getDouble(2);
        
        query = "SELECT latitude, longitude, on_street FROM stopinfo WHERE on_street='" + os2 + "' AND cross_street='" + cs2 + "'";
		preparedStmt = conn.prepareStatement(query);
        rs = preparedStmt.executeQuery();
        if (!rs.next()){
        	JOptionPane.showMessageDialog(null, "ERROR: Invalid Input", "Error", JOptionPane.ERROR_MESSAGE);
        	return;
        }  
        rs.absolute(1);
        double lat2 = rs.getDouble(1), long2 = rs.getDouble(2);
        
        double dist = Math.sqrt(Math.pow(Math.abs(lat2 - lat1)*69, 2) //approx 69 miles to a degree of latitude
        		+ Math.pow(Math.abs(long2 - long1)*51.52, 2)); //at average Chicago latitude (42 deg. N), 51.52 miles to a degree of longitude
        DecimalFormat myFormat = new DecimalFormat("0.00000");
        String distString = myFormat.format(dist);
        String message = "Distance between " + os1 + "/" + cs1 + " and " + os2 + "/" + cs2 + "\n"
        		+ " is approximately " + distString + " miles";
        JOptionPane.showMessageDialog(null, message);
        
        //System.out.println("Distance between " + os1 + "/" + cs1 + " and " + os2 + "/" + cs2
        	//	+ " is approximately " + distString + " miles");
        //System.out.println();
	}
	
	/**
	 * Gets all data from the database. Used for debugging.
	 * @throws SQLException
	 */
	public void getAll() throws SQLException{
		this.executeUse("chicago_buses");
		String query = "SELECT * FROM stopinfo";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        ResultSet rs = preparedStmt.executeQuery();
        int j = 1;
        while (rs.next() && j < 6){
        	//System.out.println(rs.getObject(7));
        	j++;
        }
	}
	
	/**
	 * Builds the database containing the bus stop data. Should only need to be run once when 
	 * connecting to a new localhost.
	 * @throws SQLException
	 */
	public void buildDatabase() throws SQLException {
		String query = "CREATE DATABASE chicago_buses";
		PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.execute();
        
        this.executeUse("chicago_buses");
        query = "CREATE TABLE stopinfo("
        		+ "stop_id int NOT NULL auto_increment, "
        		+ "on_street varchar(100) default '', "
        		+ "cross_street varchar(100) default '', "
        		+ "routes varchar(50) default '', "
        		+ "boardings double default 0, "
        		+ "alightings double default 0, "
        		+ "month datetime, "
        		+ "daytype varchar(20) default '', "
        		+ "latitude double default 0, "
        		+ "longitude double default 0, "
        		+ "primary key(stop_id) "
        		+ ")";
		preparedStmt = conn.prepareStatement(query);
        preparedStmt.execute();
        
        this.executeUse("chicago_buses");
        query = "load xml local infile '/Users/Steve/Desktop/rows.xml' "
        		+ "into table stopinfo(stop_id, on_street, cross_street, routes, boardings, "
        		+ "alightings, month, daytype, location)";
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.execute();
	}
	
}

