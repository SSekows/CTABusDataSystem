package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.jfree.ui.RefineryUtilities;

/**
 * Contains functions for describing and manipulating key/value maps.
 * @author Steve
 *
 */
public class MapUtils {

	public MapUtils(){
		
	}
	
	/**
	 * Sorts a map by value (ascending from lowest to highest). 
	 * @param map to sort
	 * @return sorted map
	 */
	public static <K extends Comparable, V extends Comparable> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K,V>> preSort = new LinkedList<Map.Entry<K,V>>(map.entrySet());
        Collections.sort(preSort, new Comparator<Map.Entry<K,V>>() {           
            @Override
            public int compare(Map.Entry<K,V> entry1, Map.Entry<K,V> entry2) {
                return entry1.getValue().compareTo(entry2.getValue());
            }
        });
        
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();
        for (Map.Entry<K,V> entry: preSort) {
        	sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
	
	/**
	 * Prints a map's max key/value pair (and also all key/value pairs if desired).
	 * @param printMap map to be printed
	 * @param outputLabels labels to be used in describing the max key/value pair
	 */
	public void printMap(Map<String, Integer> printMap, String[] outputLabels, int printNum){
		Iterator mapIterator = printMap.entrySet().iterator();
		int j = 1;
		List<String> keys = new ArrayList<String>();
		List<Integer> values = new ArrayList<Integer>();
		String[] chartLabels = this.getChartLabels(outputLabels);
		while(mapIterator.hasNext()){
            Map.Entry pair = (Map.Entry)mapIterator.next();
            if (j > printMap.size() - printNum){
            	System.out.println(pair.getKey() + ": " + (int)pair.getValue() + " routes");
            	keys.add((String) pair.getKey());
            	values.add((Integer) pair.getValue());
            }
            if (!mapIterator.hasNext()){
            	String message = outputLabels[0] + ": " + (int)pair.getValue() + "\n"
            			+ outputLabels[1] + ": " + pair.getKey();
            	JOptionPane.showMessageDialog(null, message);
            	System.out.println(outputLabels[0] + ": " + (int)pair.getValue());
                System.out.println(outputLabels[1] + ": " + pair.getKey());
            }
            j++;
        }
		System.out.println();
		final ChartUtils demo = new ChartUtils("Bar Chart", keys, values, chartLabels);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
	}
	
	/**
	 * Returns an array of labels for the bar chart depending on the data being plotted.
	 * @param outputLabels labels that depend on the data being plotted
	 * @return labels for bar chart
	 */
	private String[] getChartLabels(String[] outputLabels){
		String[] chartLabels = new String[2];
		if (outputLabels[1].equalsIgnoreCase("At stop")){
			chartLabels[0] = "Stops";
			chartLabels[1] = "Routes";
		}
		else {
			chartLabels[0] = "Routes";
			chartLabels[1] = "Stops";
		}
		return chartLabels;
	}
	
}
