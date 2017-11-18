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

package com.infoblazer.gp.application.syntheticdata;

import com.infoblazer.gp.application.data.model.Timeseries;
import com.infoblazer.gp.application.data.model.TimeseriesSet;

import java.util.*;

/**
 * Created by David on 8/28/2015.
 */
public class XYSeriesSet {
    private Map<String,TimeseriesSet> timeseriesMap ;
    private Map<String,XYSeries> xySeriesMap ;
    private XYSeries targetSeries;
    private TimeseriesSet targetTimeseries;
    public XYSeries getXYSeries(String key){
        if (xySeriesMap==null) {
            return null;
        }else{
            return xySeriesMap.get(key);
        }
    }

    public TimeseriesSet getTargetTimeseries() {
        return this.targetTimeseries;
    }

    public void setTargetTimeseries(TimeseriesSet targetTimeseries) {
        this.targetTimeseries = targetTimeseries;
    }

    public XYSeries getTargetSeries() {
        return this.targetSeries;
    }

    public void setTargetSeries(XYSeries targetSeries) {
        this.targetSeries = targetSeries;
    }

    public void setXYSeries(String key,XYSeries xySeries){
        if (xySeriesMap==null){
            xySeriesMap = new HashMap<>();
        }
          xySeriesMap.put(key,xySeries);
    }
    public void setTimeseries(String key,TimeseriesSet timeseriesSet){
        if (timeseriesMap==null){
            timeseriesMap = new HashMap<>();
        }
        timeseriesMap.put(key,timeseriesSet);
    }
    public TimeseriesSet getSeries(String key){
        return timeseriesMap.get(key);
    }


    public List<String> getSeriesList() {

        List<String> seriesList ;
        if (xySeriesMap!=null) {
            seriesList =  new ArrayList<>(xySeriesMap.keySet());
        }else{
            seriesList= new ArrayList<>(timeseriesMap.keySet());
        }

        return seriesList;
    }
}
