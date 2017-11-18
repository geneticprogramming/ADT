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


import com.infoblazer.gp.application.data.model.Series;
import com.infoblazer.gp.application.data.service.DataService;

import java.util.Map;

/**
 * Created by David on 10/29/2014.
 */

public class DbXYSeries implements XYSeries{



    private DataService dataService;

    private String expression;
    private Object [] xVals;
    private Double [] yVals;

    public DbXYSeries( DataService dataService) {
        this.dataService = dataService;
    }

    public void setxVals(Object[] xVals) {
        this.xVals = xVals;
    }

    public Double[] getyVals() {
        return this.yVals;
    }

    public Object[] getxVals() {
        return this.xVals;
    }

    @Override
    public void setY(int i, Double val) {
        yVals[i] = val;
    }

    @Override
    public void setX(int i, Object val) {
        xVals[i] = val;
    }

    @Override
    public void setX(Object[] objects) {
         xVals = objects;
    }

    @Override
    public void setY(Double[] doubles) {
        yVals = doubles;
    }

    public void setyVals(Double[] yVals) {
        this.yVals = yVals;
    }

    @Override
    public Object getX(int i) {
        return xVals[i];
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
    public Object[] getX() {
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
    public void generate( String series){
//Get this from the db based on the expression or the name eventually

        Series dataseries =dataService.getTimeSeries(series);
        Map<Double, Double> data = dataseries.getData();
        int i = 0;
        xVals = new Double[data.size()];
        yVals= new Double[data.size()];
        for (Map.Entry<Double,Double> entry:data.entrySet()){
             xVals[i] = entry.getKey();
            yVals[i] = entry.getValue();
            i++;
        }


    }
}
