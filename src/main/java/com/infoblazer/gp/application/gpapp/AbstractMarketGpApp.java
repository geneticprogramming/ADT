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

package com.infoblazer.gp.application.gpapp;

import com.infoblazer.gp.application.data.model.Normalization;
import com.infoblazer.gp.application.data.model.Timeseries;
import com.infoblazer.gp.application.data.model.TimeseriesSet;
import com.infoblazer.gp.application.data.service.DataService;
import com.infoblazer.gp.application.fitness.AbstractFitnessEvaluator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

/**
 * Created by David on 6/21/2015.
 */
public abstract class AbstractMarketGpApp extends AbstractGpApp {
    @Autowired
    protected DataService dataService;
    @Value("${target}")
    private String series;
    @Value("${predictors:#{null}}")
    private String[] predictorArray;
    private final static Logger logger = Logger.getLogger(AbstractMarketGpApp.class.getName());

    @Override
    public final void init() {


        TimeseriesSet target = buildTimeSeries(series);
        if (target.getTimeseries()==null){
            logger.error("Couldn't not load stock series. Make sure the series code is correct");
            System.exit(1);
        }
        xySeriesSet.setTargetTimeseries(target);

        if (predictorArray != null) {
            for (String predictorName:predictorArray) {
                TimeseriesSet predictor = buildTimeSeries(predictorName);
                xySeriesSet.setTimeseries(predictorName, predictor);
            }
        }else{
            xySeriesSet.setTimeseries(series, target);
        }
        super.init();

    }       //else can pass in a custom syntechitc series

    private TimeseriesSet buildTimeSeries(String series) {
        ///.n don't normalize
        //.r return series
        //.l log return series
        //.r.7 7 day return
        //.m30.7
        Normalization normalization = Normalization.NONE;
        int normalizationWindow = 1;
        int step = 1;
        if (series.contains(".")) {
            String[] transformation = series.split("\\.");
            series = transformation[0];
             normalization = Normalization.parse(transformation[1].charAt(0));


            if (transformation[1].length() > 1) {
                  normalizationWindow = Integer.parseInt(transformation[1].substring(1, transformation[1].length()));
            }


            if (transformation.length > 2) {
                 step = Integer.parseInt(transformation[2]);
            }
        }
        TimeseriesSet timeseriesSet =  new TimeseriesSet() ;
        Timeseries timeseries = dataService.getSeries(series);
        timeseriesSet.setTimeseries(timeseries);
        timeseriesSet.setNormalization(normalization);
        timeseriesSet.setNormalizationWindow(normalizationWindow);
        timeseriesSet.setStep(step);

        return timeseriesSet;
    }

}
