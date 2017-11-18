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

package com.infoblazer.gp.application.data.service;


import com.infoblazer.gp.application.data.model.Series;
import com.infoblazer.gp.application.data.model.Timeseries;
import com.infoblazer.gp.application.data.model.jpa.DbTimeSeries;
import com.infoblazer.gp.application.data.model.jpa.DbTimeSeriesdata;
import com.infoblazer.gp.application.data.repository.DbTimeSeriesRepository;
import com.infoblazer.gp.application.data.repository.DbTimeSeriesDataRepository;
import com.infoblazer.gp.application.syntheticdata.generator.DataGeneratorFactory;
import com.infoblazer.gp.application.syntheticdata.generator.SyntheticDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by David on 6/29/2014.
 */
@Service
public class DataService {
    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DataService.class.getName());

    @Autowired
    private DbTimeSeriesRepository dbTimeSeriesRepository;
    @Autowired
    private DbTimeSeriesDataRepository dbTimeSeriesDataRepository;

    public Timeseries getSeries(String seriesCode) {

        Timeseries timeseries = null;
        DbTimeSeries dbTimeSeries = dbTimeSeriesRepository.findBySeriesCode(seriesCode);
        List<DbTimeSeriesdata> timeSeriesdata = dbTimeSeriesDataRepository.findByTimeseriesId(dbTimeSeries.getId());

        for (DbTimeSeriesdata data:timeSeriesdata){

            if (timeseries == null) {
                timeseries = new Timeseries();
                timeseries.setId(Long.valueOf(dbTimeSeries.getId()));
                timeseries.setSeriesCode(dbTimeSeries.getSeriesCode());
                timeseries.setDescription(dbTimeSeries.getDescription());
                timeseries.setTitle(dbTimeSeries.getTitle());
            }

            LocalDate date = data.getDataDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            timeseries.putData(date, data.getDataValue());
        }
        if (timeseries!=null) {
            timeseries.fill();
            timeseries.initializeArrays();
        }
        return  timeseries;
    }

    public Double valueOn(String timeSeriesCode, LocalDate date) {
        return valueAgo(timeSeriesCode, date, 0);

    }

    public Double movingAverage(String timeSeriesCode, LocalDate date, int days) {


        logger.trace("Calling movingAverage:" + timeSeriesCode + " " + date + " " + days);


        Timeseries timeseries = getSeries(timeSeriesCode);
        Double value = timeseries.getMovingAverage(date, days);

        logger.trace("exiting value Ago:");
        return value;

    }

    public Double maximum(String timeSeriesCode, LocalDate date, int days) {
        logger.trace("Calling movingAverage:" + timeSeriesCode + " " + date + " " + days);


        Timeseries timeseries =getSeries(timeSeriesCode);
        Double value = timeseries.getMaximum(date, days);

        logger.trace("exiting value Ago:");
        return value;

    }

    public Double minimum(String timeSeriesCode, LocalDate date, int days) {
        logger.trace("Calling movingAverage:" + timeSeriesCode + " " + date + " " + days);


        Timeseries timeseries = getSeries(timeSeriesCode);
        Double value = timeseries.getMinimum(date, days);

        logger.trace("exiting value Ago:");
        return value;

    }

    public List<Integer> findPredictors() {

        List<DbTimeSeries> dbTimeSeries = dbTimeSeriesRepository.findByPredictor(true);
        List<Integer> predictorList  = new ArrayList<>();
        for (DbTimeSeries timeSeries:dbTimeSeries){
            predictorList.add(timeSeries.getId());

        }


        return predictorList;

    }

    public Double valueAgo(final String timeSeriesCode, LocalDate date, int offset) {
        logger.trace("Calling valueAgo:" + timeSeriesCode + " " + date + " " + offset);


        Timeseries timeseries = getSeries(timeSeriesCode);
        Double value = timeseries.getNormalizedValueAt(date);

        logger.trace("exiting value Ago:");
        return value;

    }



    public Series getTimeSeries(String title) {

        Series series = null;
        SyntheticDataGenerator dataGenerator = DataGeneratorFactory.getDataGenerator(title);
        SortedMap<Double, Double> data = new TreeMap<Double, Double>();

        Double[][] doubles = dataGenerator.generate();
        for (int i = 0;i<doubles.length;i++) {
            if (series == null) {
                series = new Series();
                series.setTitle(title);
            }
            data.put(doubles[i][0], doubles[i][1]);
        }

        series.setData(data);
        return series;
    }



}




