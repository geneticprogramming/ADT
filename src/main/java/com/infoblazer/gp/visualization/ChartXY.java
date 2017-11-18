/*
 * MIT License
 *
 * Copyright (c) 2014-2018 David Moskowitz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.infoblazer.gp.visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.date.SerialDate;
import org.springframework.stereotype.*;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by David on 10/30/2014.
 */


public class ChartXY extends JFrame {



    private JFreeChart chart;
    private XYSeriesCollection dataset;
    private XYSeriesCollection regimeDataset;
    private TimeSeriesCollection timeseriesDataset;
    private TimeSeriesCollection timeseriesRegimeDataset;






    public void init() {

        String chartTitle = "Genetic Programming";
        String xAxisLabel = "X";
        String yAxisLabel = "Y";


        dataset = new XYSeriesCollection();
        chart = ChartFactory.createXYLineChart(chartTitle,xAxisLabel, yAxisLabel, dataset);

        JPanel chartPanel = new ChartPanel(chart);

        final XYPlot plot = chart.getXYPlot();
        final NumberAxis axis1 = new NumberAxis("Val");
        plot.setRangeAxis(0, axis1);
        final NumberAxis axis2 = new NumberAxis("Regime");
        axis2.setRange(-1, 9);
        plot.setRangeAxis(1, axis2);
        regimeDataset =  new XYSeriesCollection();
        XYLineAndShapeRenderer rendererRegime = new XYLineAndShapeRenderer();
        rendererRegime.setBaseShapesVisible(false);
        plot.setDataset(1, regimeDataset);
        plot.setRenderer(1, rendererRegime);
        plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(0, Color.BLACK);
        plot.setDataset(0, dataset);
        plot.mapDatasetToRangeAxis(0, 0);//1st dataset to 1st y-axis
        plot.mapDatasetToRangeAxis(1, 1); //2nd dataset to 2nd y-axis


        add(chartPanel, BorderLayout.CENTER);

        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);


    }


    public void initTimeseries() {
        String chartTitle = "Genetic Programming";
        String xAxisLabel = "Date";
        String yAxisLabel = "Value";


        timeseriesDataset =  new TimeSeriesCollection();
        JFreeChart jFreeChart = ChartFactory.createTimeSeriesChart(
                chartTitle, xAxisLabel, yAxisLabel, timeseriesDataset, true, true, false);

        JPanel chartPanel = new ChartPanel(jFreeChart);

        final XYPlot plot = jFreeChart.getXYPlot();
        final NumberAxis axis1 = new NumberAxis("Value");
        plot.setRangeAxis(0, axis1);
        final NumberAxis axis2 = new NumberAxis("Regime");
        axis2.setRange(-1,9);
        plot.setRangeAxis(1, axis2);
        timeseriesRegimeDataset =  new TimeSeriesCollection();
        plot.setDataset(1, timeseriesRegimeDataset);
        plot.setDataset(0,timeseriesDataset);
        plot.mapDatasetToRangeAxis(0, 0);//1st dataset to 1st y-axis
        plot.mapDatasetToRangeAxis(1, 1); //2nd dataset to 2nd y-axis


        add(chartPanel, BorderLayout.CENTER);

        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);


    }

    public void addTimeseries(String title, Object[] xVals, Double[] yVals) {


        TimeSeries series = new TimeSeries(title);




        removeSeries(title);


        for (int i = 0; i < xVals.length; i++) {
            if (xVals[i] != null && yVals[i] != null) {
                Date date;
                if (xVals[i] instanceof LocalDate){
                    LocalDate ld = (LocalDate) xVals[i];
                    Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                    date = Date.from(instant);
                }  else{
                    date = (Date) xVals[i];
                }

                SerialDate sd = SerialDate.createInstance(date);
                Day d = new Day(sd);
                series.add(d, yVals[i]);



            }
        }

        try {
            timeseriesDataset.addSeries(series);

        }catch (Exception e){
            e.printStackTrace();

        }



    }

    public void addRegimeSeries(String title, Object[] xVals, Double[] regimeVals) {
       //regimes can be charted against a timeseries or numeric series
        removeSeries(title);

        XYSeries xySeries =null;
        TimeSeries timeSeries = null;
        for (int i = 0; i < xVals.length; i++) {
            if (xVals[i] != null && regimeVals[i] != null) {
                if (xVals[i] instanceof LocalDate || xVals[i] instanceof Date){
                    Date date = null;
                    if (xVals[i] instanceof LocalDate){
                        LocalDate ld = (LocalDate) xVals[i];
                        Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                        date = Date.from(instant);
                    }else{
                        date = (Date) xVals[i];
                    }

                    SerialDate sd = SerialDate.createInstance(date);
                    Day d = new Day(sd);
                    if (timeSeries==null){
                        timeSeries = new TimeSeries(title);
                    }
                    timeSeries.add(d, regimeVals[i].intValue());
                } else {
                    if (xySeries==null){
                        xySeries = new XYSeries(title);
                    }

                    xySeries.add((Number) xVals[i], regimeVals[i].intValue());
                }
            }
        }
        try {
            if (timeSeries==null) {
                regimeDataset.addSeries(xySeries);
            }else{
                timeseriesRegimeDataset.addSeries(timeSeries);
            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public void addSeries(String title, Object[] xVals, Double[] yVals) {




        removeSeries(title);

        XYSeries series = new XYSeries(title);
        for (int i = 0; i < xVals.length; i++) {
            if (xVals[i] != null && yVals[i] != null) {
                    series.add((Number) xVals[i], yVals[i]);
                }
            }
         try {
             dataset.addSeries(series);
         }catch (Exception e){
             e.printStackTrace();

         }

    }

    public void removeSeries(String title) {

        if (dataset!=null){
            for (int i = 0;i<dataset.getSeriesCount();i++) {
                if (dataset.getSeries(i).getKey().equals(title)) {
                    dataset.removeSeries(i);
                }
            }

        }
        if (timeseriesDataset!=null) {
            for (int i = 0; i < timeseriesDataset.getSeriesCount(); i++) {
                if (timeseriesDataset.getSeries(i).getKey().equals(title)) {
                    timeseriesDataset.removeSeries(i);
                }

            }
        }
        if (regimeDataset!=null) {
            for (int i = 0; i < regimeDataset.getSeriesCount(); i++) {
                if (regimeDataset.getSeries(i).getKey().equals(title)) {
                    regimeDataset.removeSeries(i);
                }

            }
        }

        if (timeseriesRegimeDataset!=null) {
            for (int i = 0; i < timeseriesRegimeDataset.getSeriesCount(); i++) {
                if (timeseriesRegimeDataset.getSeries(i).getKey().equals(title)) {
                    timeseriesRegimeDataset.removeSeries(i);
                }

            }
        }




    }

    public void clear() {

        if (timeseriesDataset!=null) {
            timeseriesDataset.removeAllSeries();
        }
        if (dataset!=null) {
            dataset.removeAllSeries();
        }

    }


}
