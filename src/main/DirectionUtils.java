package main;

/**
 * Contains functions for dealing with directional (in terms of a compass direction) data
 * @author Steve
 *
 */
public class DirectionUtils {

	private String[] directionAndQuery;
	
	public DirectionUtils(){
		directionAndQuery = new String[2];
	}
	
	/**
	 * Given a direction, this function returns an array containing the appropriate label string
	 * and SQL query for the direction.
	 * @param direction compass direction
	 * @return array containing a label string and SQL query
	 */
	public String[] getDirectionAndQuery(String direction){
		if (direction.equalsIgnoreCase("N")){
			directionAndQuery[0] = "Northernmost";
			directionAndQuery[1] = "SELECT on_street, cross_street, max(latitude) AS lat FROM stopinfo GROUP BY on_street, cross_street "
            		+ "ORDER BY lat DESC LIMIT 1";
		} else if (direction.equalsIgnoreCase("S")){
			directionAndQuery[0] = "Southernmost";
			directionAndQuery[1] = "SELECT on_street, cross_street, min(latitude) AS lat FROM stopinfo GROUP BY on_street, cross_street "
            		+ "ORDER BY lat ASC LIMIT 1";
		} else if (direction.equalsIgnoreCase("W")){
			directionAndQuery[0] = "Westernmost";
			directionAndQuery[1] = "SELECT on_street, cross_street, max(abs(longitude)) as longi FROM stopinfo GROUP BY on_street, cross_street "
            		+ "ORDER BY longi DESC LIMIT 1";
		} else if (direction.equalsIgnoreCase("E")){
			directionAndQuery[0] = "Easternmost";
			directionAndQuery[1] = "SELECT on_street, cross_street, min(ABS(longitude)) AS longi FROM stopinfo GROUP BY on_street, cross_street "
            		+ "ORDER BY longi ASC LIMIT 1";
		} else if (direction.equalsIgnoreCase("NW")){
			directionAndQuery[0] = "North-Westernmost";
			directionAndQuery[1] = "SELECT on_street, cross_street, max(pow(latitude,2) + pow(longitude,2)) AS coord FROM stopinfo "
            		+ "GROUP BY on_street, cross_street ORDER BY coord DESC LIMIT 1";
		} else if (direction.equalsIgnoreCase("SW")){
			directionAndQuery[0] = "South-Westernmost";
			directionAndQuery[1] = "SELECT on_street, cross_street, max(-pow(latitude,2) + pow(longitude,2)) AS coord FROM stopinfo "
            		+ "GROUP BY on_street, cross_street ORDER BY coord DESC LIMIT 1";
		} else if (direction.equalsIgnoreCase("NE")){
			directionAndQuery[0] = "North-Easternmost";
			directionAndQuery[1] = "SELECT on_street, cross_street, max(pow(latitude,2) - pow(longitude,2)) AS coord FROM stopinfo "
            		+ "GROUP BY on_street, cross_street ORDER BY coord DESC LIMIT 1";
		} else if (direction.equalsIgnoreCase("SE")){
			directionAndQuery[0] = "South-Easternmost";
			directionAndQuery[1] = "SELECT on_street, cross_street, max(-pow(latitude,2) - pow(longitude,2)) AS coord FROM stopinfo "
            		+ "GROUP BY on_street, cross_street ORDER BY coord DESC LIMIT 1";
		} else {
			directionAndQuery[0] = null;
			directionAndQuery[1] = null;
		}
		return directionAndQuery;
	}
	
}
