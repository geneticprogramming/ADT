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


import com.infoblazer.gp.application.data.model.Normalization;
import com.infoblazer.gp.application.data.model.Timeseries;

import java.time.LocalDate;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by David on 10/29/2014.
 */

public class DateXYSeries implements XYSeries{



    private String expression;
    private LocalDate[] xVals;
    private Double [] yVals;


    @Override
    public void setY(int i, Double val) {
        yVals[i] = val;
    }

    @Override
    public void setX(int i, Object val) {
        xVals[i] = (LocalDate) val;
    }

    @Override
    public void setX(Object[] objects) {
        xVals = (LocalDate[]) objects;
    }

    @Override
    public void setY(Double[] doubles) {
      yVals = doubles;
    }

    public DateXYSeries(Timeseries timeseries, Normalization normalization,       int step,int dataWindow ) {


        SortedMap<LocalDate, Double> data;
        if (normalization!=Normalization.NONE || step!=1){
            data = timeseries.normalize(normalization,step,dataWindow);
        } else{
            data = timeseries.getData();
        }

        Integer start = 0;
        Integer end = data.size()-1;

        xVals = new LocalDate[end-start+1];
        yVals = new Double[end-start+1];
        int i = 0;
        int counter = 0;
        for (Map.Entry<LocalDate, Double> entry:data.entrySet()) {
            if (counter>=start && counter<=end) { //only relevant if a subseries is enabled
                xVals[i] = entry.getKey();
                yVals[i] = entry.getValue();
                i++;
            }
            counter++;
        }

    }




    @Override
    public Double getY(int i) {
        return yVals[i];
    }

    @Override
    public Double[] getY() {
        return yVals;
    }

    @Override
    public  Object[] getX() {
        return xVals;
    }


    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public int getLength(){
        return  xVals.length;
    }

    @Override
    public LocalDate getX(int i) {
        return xVals[i];
    }

    public void generate( String series){
//Get this from the db based on the expression or the name eventually





    }
}
