package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class ChartUtils extends ApplicationFrame {

	/**
     * Creates a new instance.
     *
     * @param title the frame title
     * @param keys array of keys to be plotted
     * @param values array of values to be plotted
     * @param domainRange the X (domain) and Y (range) labels of the bar graph
     */
	public ChartUtils(final String title, List<String> keys, List<Integer> values, String[] domainRange) {
		super(title);
		
		String category = domainRange[1].contains("Stop") ? "Route" : "Stop";
		final CategoryDataset dataset = createDataset(keys, values, category);
        final JFreeChart chart = createChart(dataset, domainRange);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(750, 400));
        setContentPane(chartPanel);
	}
	
	/**
     * Returns a sample dataset.
     * 
     * @return The dataset.
     */
	private CategoryDataset createDataset(List<String> keys, List<Integer> values, String categoryType) {

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (int i = 0;i < keys.size();i++){
        	dataset.addValue(values.get(i), keys.get(i), categoryType + " " + (i+1));
        }
        return dataset;
        
    }

	/**
     * Creates a chart.
     * 
     * @param dataset the data set
     * 
     * @return The chart.
     */
	private JFreeChart createChart(final CategoryDataset dataset, String[] domainRange) {
    
    // create the chart...
    final JFreeChart chart = ChartFactory.createBarChart(
        "Route/Stop Chart",         // chart title
        domainRange[0],               // domain axis label
        domainRange[1],                  // range axis label
        dataset,                  // data
        PlotOrientation.VERTICAL, // orientation
        true,                     // include legend
        true,                     // tooltips?
        false                     // URLs?
    );

    // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

    // set the background color for the chart...
    chart.setBackgroundPaint(Color.white);

    // get a reference to the plot for further customisation...
    final CategoryPlot plot = chart.getCategoryPlot();
    plot.setBackgroundPaint(Color.lightGray);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);

    // set the range axis to display integers only...
    final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

    // disable bar outlines...
    final BarRenderer renderer = (BarRenderer) plot.getRenderer();
    renderer.setDrawBarOutline(false);
    renderer.setMaximumBarWidth(35.0);
    
    // set up gradient paints for series...
    /*
    final GradientPaint gp0 = new GradientPaint(
        0.0f, 0.0f, Color.blue, 
        0.0f, 0.0f, Color.lightGray
    );
    final GradientPaint gp1 = new GradientPaint(
        0.0f, 0.0f, Color.green, 
        0.0f, 0.0f, Color.lightGray
    );
    final GradientPaint gp2 = new GradientPaint(
        0.0f, 0.0f, Color.red, 
        0.0f, 0.0f, Color.lightGray
    );
    renderer.setSeriesPaint(0, gp0);
    renderer.setSeriesPaint(1, gp1);
    renderer.setSeriesPaint(2, gp2);
    */

    final CategoryAxis domainAxis = plot.getDomainAxis();
    domainAxis.setCategoryLabelPositions(
        CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
    );
    // OPTIONAL CUSTOMISATION COMPLETED.
    
    return chart;
    
}
	

}
