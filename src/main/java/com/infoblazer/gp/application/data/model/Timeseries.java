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

package com.infoblazer.gp.application.data.model;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by David on 6/29/2014.
 */

public class Timeseries {
    private Long id;
    private String seriesCode;
    private String title;
    private String description;
    private String freq;
    private String uri;
    private SortedMap<LocalDate, Double> rawData = new TreeMap<LocalDate, Double>();
    private SortedMap<LocalDate, Double> data = new TreeMap<LocalDate, Double>();
    private SortedMap<LocalDate, Double> normalizedData = new TreeMap<LocalDate, Double>();
    private Double[] dataArray = null;
    private Map<LocalDate, Integer> offsetMap = null;


    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Timeseries.class.getName());

    public int getLength() {
        return dataArray.length;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeriesCode() {
        return seriesCode;
    }

    public void setSeriesCode(String seriesCode) {
        this.seriesCode = seriesCode;
    }

    public Double getValueAtNoFill(LocalDate currentDate) {

        return data.get(currentDate);
    }

    public Double getValueAt(LocalDate currentDate) {

        LocalDate marketDate = currentDate;
        //Fill at most 4 days
        Double value = data.get(marketDate);
        if (value == null) {
            int fillCount = 0;
            while (value == null && fillCount < 4) {
                marketDate = marketDate.minusDays(1);
                fillCount++;
                value = data.get(marketDate);
            }
        }


        if (value == null) {
            logger.warn("value is null for " + this.getTitle() + " on " + currentDate);
        }
        return value;
    }

    public Double getNormalizedValueAt(final LocalDate currentDate) {
        LocalDate marketDate = currentDate;
        //Fill at most 4 days
        Double value = normalizedData.get(marketDate);
        if (value == null) {
            int fillCount = 0;
            while (value == null && fillCount < 4) {
                marketDate = marketDate.minusDays(1);
                fillCount++;
                value = normalizedData.get(marketDate);
            }
        }


        if (value == null) {
            logger.warn("normalized value is null for " + this.getTitle() + " on " + currentDate);
        }
        return value;
    }

    public void putData(LocalDate localDate, double value) {

        data.put(localDate, value);
        rawData.put(localDate, value);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    public double[] valuesAsDouble(int windowStart, int windowEnd) {


        Object[] valuesArray = data.values().toArray();

        double[] values = new double[windowEnd - windowStart + 1];
        Double lastValue = null;
        for (int i = 0; i < windowEnd - windowStart + 1; i++) {
            Double value = (Double) valuesArray[i + windowStart];
            if (value != null) {
                values[i] = value;
            } else {
                values[i] = lastValue;
            }
            lastValue = value;

        }
        return values;
    }


    public long[] datesAsLong(int windowStart, int windowEnd) {

        long[] values = new long[windowEnd - windowStart + 1];

        for (int i = 0; i < windowEnd - windowStart + 1; i++) {
            values[i] = i + windowStart;

        }
        return values;
    }


    public double[] valuesAsDouble(LocalDate marketDate, int windowSize) {
        //fill days
        logger.trace("called valuesAsDouble " + marketDate + " " + windowSize);

        Integer windowStart = null;
        Integer windowEnd = offsetMap.get(marketDate);
        int fillcount = 0;
        while (windowEnd == null && fillcount <= windowSize) {
            marketDate = marketDate.minusDays(1);
            if (marketDate.getDayOfWeek() == DayOfWeek.SATURDAY || marketDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                marketDate = marketDate.minusDays(1);
            }
            if (marketDate.getDayOfWeek() == DayOfWeek.SATURDAY || marketDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                marketDate = marketDate.minusDays(1);
            }
            windowEnd = offsetMap.get(marketDate);
            fillcount++;
        }
        if (windowEnd != null) {
            windowStart = windowEnd - windowSize;
        }
        if (windowStart != null && windowEnd != null && windowStart >= 0) {

            return valuesAsDouble(windowStart, windowEnd);
        } else {
            logger.debug("returning null " + marketDate + " " + windowSize);
            return null;
        }
    }


    public long[] datesAsLong(LocalDate marketDate, int windowSize) {

        Integer windowStart = null;
        Integer windowEnd = offsetMap.get(marketDate);
        int fillcount = 0;
        while (windowEnd == null && fillcount <= windowSize) {
            marketDate = marketDate.minusDays(1);
            if (marketDate.getDayOfWeek() == DayOfWeek.SATURDAY || marketDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                marketDate = marketDate.minusDays(1);
            }
            if (marketDate.getDayOfWeek() == DayOfWeek.SATURDAY || marketDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                marketDate = marketDate.minusDays(1);
            }
            windowEnd = offsetMap.get(marketDate);
            fillcount++;
        }
        if (windowEnd != null) {
            windowStart = windowEnd - windowSize;
        }
        if (windowStart != null && windowEnd != null && windowStart >= 0) {
            return datesAsLong(windowStart, windowEnd);
        } else {
            logger.debug("returning null " + marketDate + " " + windowSize);
            return null;
        }
    }

    public void initializeArrays() {

        dataArray = new Double[data.size()];
        offsetMap = new HashMap<LocalDate, Integer>();

        int i = 0;
        for (Map.Entry<LocalDate, Double> entry : data.entrySet()) {

            dataArray[i] = entry.getValue();
            offsetMap.put(entry.getKey(), i);
            i++;

        }


    }


    public void fill() {
        LocalDate currentDate = rawData.firstKey();
        Double lastValue = rawData.get(rawData.firstKey()); //running value
        data.put(currentDate, lastValue);
        currentDate = currentDate.plusDays(1);

        while (currentDate.isBefore(data.lastKey())) {
            if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                Double value = rawData.get(currentDate);
                if (value != null) {
                    data.put(currentDate, value);
                    lastValue = value;
                } else {
                    data.put(currentDate, lastValue);
                }

            }

            currentDate = currentDate.plusDays(1);
        }


    }

    public SortedMap<LocalDate, Double> normalize(Normalization normalization, int step, int dataWindow) {
        //(Price - Mean)/StandardDeviation
        //do we use raw of filled data for this

        switch (normalization) {
            case NONE:
                stepSeries(step);
                break;
            case RETURN:case LOG_RETURN:
                normalizeReturn(step,normalization);
                break;
            case MEAN:case STD_DEV:
                normalizeMean(dataWindow, normalization);
                break;
        }
        return normalizedData;

    }


    /**
     * daily to weekly, etc
     *
     * @param step
     */
    private void stepSeries(int step) {
        int counter = 0;


        for (Map.Entry<LocalDate, Double> entry : data.entrySet()) {
            if (counter != 0 && counter % step == 0) {
                Double val = entry.getValue();
                normalizedData.put(entry.getKey(), val);
            }
            counter++;
        }
    }

    private void normalizeReturn(int step,Normalization normalization) {

        int counter = 0;
        Double lastVal = null;
        Double periodReturn;

        for (Map.Entry<LocalDate, Double> entry : data.entrySet()) {
            Double val = entry.getValue();
            if (counter>0 && counter % step == 0) {
                if (normalization==Normalization.RETURN) {
                    periodReturn = (val - lastVal) / lastVal;
                }else{
                    periodReturn = Math.log(val/ lastVal);
                }
                normalizedData.put(entry.getKey(), periodReturn * 100);
                lastVal = val;
            }
            if (lastVal==null){
                lastVal = val;
            }
            counter++;

        }
    }

    public void normalizeStdDev(final int normalizationWindow) {
        double mean = 0d;
        double stDev = 1d;
        int counter = 0;
        double total = 0;
        double[] window = new double[normalizationWindow];
        double[] diffSq = new double[normalizationWindow];
        double sumDiffSq = 0;
        int pointer = 0;

        for (Map.Entry<LocalDate, Double> entry : data.entrySet()) {
            Double value = entry.getValue();
            window[pointer] = value;

            if (counter >= normalizationWindow - 1) {
                mean = total / normalizationWindow;
                if (counter + 1 == normalizationWindow) { //back fill first window
                    for (int i = 0; i < normalizationWindow; i++) {
                        diffSq[i] = Math.abs(diffSq[i] - mean);
                        sumDiffSq += diffSq[i] * diffSq[i];
                    }
                }

                diffSq[pointer] = Math.abs(value - mean) * Math.abs(value - mean);
                sumDiffSq = sumDiffSq + diffSq[pointer];
                double variance = sumDiffSq / normalizationWindow;
                stDev = Math.sqrt(variance);
                normalizedData.put(entry.getKey(), (entry.getValue() - mean) / stDev);
                pointer = (pointer + 1) % normalizationWindow;
                total = total - window[pointer];
                sumDiffSq = sumDiffSq - diffSq[pointer];

            }
            total += value;
            if (counter + 1 < normalizationWindow) {
                diffSq[pointer] = value;
                pointer = (pointer + 1) % normalizationWindow;
            }

            counter++;

        }
    }

    public void normalizeMean(final int normalizationWindow, final Normalization normalization) {
        double mean;

        int counter = 1;
        double total = 0;
        double sumDiffSq = 0;
        double[] window = new double[normalizationWindow - 1];
        double[] diffSq = new double[normalizationWindow - 1];
        int pointer = 0;
        int meanValues = 0;
        for (Map.Entry<LocalDate, Double> entry : data.entrySet()) {

            if (meanValues < normalizationWindow) {
                meanValues++;
            } else {
                meanValues = normalizationWindow;
            }
            Double value = entry.getValue();
            total += value;
            mean = total / meanValues;
            double diffsq = Math.abs(value - mean) * Math.abs(value - mean);
            sumDiffSq += diffsq;
            switch (normalization) {
                case MEAN:
                    if (mean != 0) {
                        double normalizedValue = value / mean;
                        normalizedData.put(entry.getKey(), normalizedValue);
                    }
                    break;
                case STD_DEV:

                    double variance = sumDiffSq / meanValues;
                    double stDev = (Math.sqrt(variance));
                    if (stDev != 0) {
                        double normalizedValue = (value - mean) / stDev;
                        normalizedData.put(entry.getKey(), normalizedValue );
                    }

                    break;
            }

            if (counter >= normalizationWindow) {
                total = total - window[pointer];
                sumDiffSq = sumDiffSq - diffSq[pointer];
            }
            window[pointer] = value;
            diffSq[pointer] = diffsq;
            pointer = (pointer + 1) % (normalizationWindow - 1);
            counter++;
        }
    }


    public Double getMovingAverage(LocalDate date, int days) {

        if (date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.minusDays(1);
        }
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.minusDays(1);
        }
        int endPos = offsetMap.get(date);
        double total = 0d;
        for (endPos = days; endPos > 0; endPos--) {
            total = total + dataArray[endPos];
        }
        double ma = total / Double.valueOf(days);
        return ma;
    }

    public Double getMinimum(LocalDate date, int days) {

        if (date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.minusDays(1);
        }
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.minusDays(1);
        }
        int endPos = offsetMap.get(date);
        double min = Double.MAX_VALUE;
        for (endPos = days; endPos > 0; endPos--) {
            if (dataArray[endPos] < min) {
                min = dataArray[endPos];
            }
        }

        return min;
    }

    public Double getMaximum(LocalDate date, int days) {


        if (date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.minusDays(1);
        }
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.minusDays(1);
        }
        int endPos = offsetMap.get(date);
        double max = (-1 * Double.MAX_VALUE);
        for (endPos = days; endPos > 0; endPos--) {
            if (dataArray[endPos] > max) {
                max = dataArray[endPos];
            }
        }

        return max;
    }

    public SortedMap<LocalDate, Double> getData() {
        return data;
    }

    public SortedMap<LocalDate, Double> getNormalizedData() {
        return normalizedData;
    }

}
