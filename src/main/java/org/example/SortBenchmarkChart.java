package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import org.jfree.chart.ChartUtils;
import java.io.File;
import java.io.IOException;

public class SortBenchmarkChart {
    private static void showChart(String title, String[] labels, double[] sizes, double[] nearly, double[] random, double[] reverse, boolean addQuadratic) {
        XYSeries nearlySeries = new XYSeries("Nearly");
        XYSeries randomSeries = new XYSeries("Random");
        XYSeries reverseSeries = new XYSeries("Reverse");
        for (int i = 0; i < sizes.length; i++) {
            nearlySeries.add(sizes[i], nearly[i]);
            randomSeries.add(sizes[i], random[i]);
            reverseSeries.add(sizes[i], reverse[i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(nearlySeries);
        dataset.addSeries(randomSeries);
        dataset.addSeries(reverseSeries);
        // Removed O(n^2) quadratic chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "Array Size",
                "Execution Time (ms/op)",
                dataset
        );
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new XYSplineRenderer());
        JFrame frame = new JFrame(title);
        frame.setContentPane(new ChartPanel(chart));
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        // Save chart as PNG
        try {
            ChartUtils.saveChartAsPNG(new File(title.replace(" ","") + ".png"), chart, 800, 600);
        } catch (IOException e) {
            System.err.println("Error saving chart: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        double[] sizes = {100, 1000, 10000};
        double[] insertionNearly = {0.001, 0.022, 0.679};
        double[] insertionRandom = {0.001, 0.044, 0.916};
        double[] insertionReverse = {0.001, 0.011, 0.144};
        double[] selectionNearly = {0.002, 0.183, 16.716};
        double[] selectionRandom = {0.002, 0.216, 16.647};
        double[] selectionReverse = {0.008, 0.882, 17.280};
        String[] labels = {"100", "1000", "10000"};
        SwingUtilities.invokeLater(() -> {
            showChart("Insertion Sort Benchmark", labels, sizes, insertionNearly, insertionRandom, insertionReverse, false);
            showChart("Selection Sort Benchmark", labels, sizes, selectionNearly, selectionRandom, selectionReverse, false);
        });
    }
}
