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


import com.infoblazer.gp.visualization.model.ChartModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * Created by David on 4/19/2015.
 */
@Component
public class Chart {
    private final static Logger logger = Logger.getLogger(Chart.class.getName());

    private ChartXY chartXY;
    @Value("${visualize:#{false}}")
    private boolean visualize;
    public void addSeries(final String title, final Object[] xVals, final Double[] yVals) {
      if (visualize) {
          final ChartModel chartModel = new ChartModel();

          chartModel.title = title;
          chartModel.xVals = xVals;
          chartModel.yVals = yVals;

          drawChart(chartModel);
      }



}


    public void drawChart(ChartModel chartModel) {
        if (visualize) {
            if (chartXY == null) {
                chartXY = new ChartXY();
                if (getChartType(chartModel) == ChartModel.ChartType.NUMERIC) {
                    chartXY.init();
                } else {

                    chartXY.initTimeseries();
                }
            }
            if ("Target".equals(chartModel.title)) {
                chartXY.clear();


            }
            if ("Regime".equals(chartModel.title)) {
                chartXY.addRegimeSeries(chartModel.title, chartModel.xVals, chartModel.yVals);
            } else {
                if (getChartType(chartModel) == ChartModel.ChartType.NUMERIC) {
                    chartXY.addSeries(chartModel.title, chartModel.xVals, chartModel.yVals);
                } else {
                    chartXY.addTimeseries(chartModel.title, chartModel.xVals, chartModel.yVals);

                }
            }


        }
    }

    private static ChartModel.ChartType getChartType(ChartModel chartModel) {
        ChartModel.ChartType chartType = null;
        for (Object xval : chartModel.xVals) {
            if (xval != null) {
                if (xval instanceof Number) {
                    chartType = ChartModel.ChartType.NUMERIC;
                } else {
                    chartType = ChartModel.ChartType.TIMESERIES;
                }


            }
            if (chartType != null) {
                break;
            }

        }
        return chartType;

    }

}
