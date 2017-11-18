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

/**
 * User: davidm
 * Date: 3/11/2015
 * Time: 4:42 PM
 */
public class XYArray {


    private Double[] yVals ;
    private Object[] xVals ;


    public XYArray(Object[] xVals, Double[] yVals) {
        this.yVals = yVals;
        this.xVals = xVals;
    }

    public Double[] getyVals() {
        return this.yVals;
    }

    public void setyVals(Double[] yVals) {
        this.yVals = yVals;
    }

    public Object[] getxVals() {
        return this.xVals;
    }

    public void setxVals(Double[] xVals) {
        this.xVals = xVals;
    }



    public Double getFilledYVal(final Integer pos) {
        int checkPos = pos;
        Double result = yVals[checkPos];

        while (result==null && checkPos > 0){
            result = yVals[--checkPos];
        }
        return  result;
    }

    public Object getX(int pos) {
        return xVals[pos];
    }

    public Double getY(int pos) {
        return yVals[pos];
    }

    public void setxVals(int pos, Object val) {
        this.xVals[pos] = val;
    }

    public void setyVals(int pos, Double val) {
        this.yVals[pos] = val;
    }
}
