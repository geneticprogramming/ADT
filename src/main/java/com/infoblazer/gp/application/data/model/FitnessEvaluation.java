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

import com.infoblazer.gp.application.syntheticdata.XYSeries;

/**
 * Created by David on 5/18/2015.
 */
public class FitnessEvaluation {
    private XYArray xyArray;
    private XYArray regimeXyArray;
    private double fitness;


    public FitnessEvaluation(XYArray xyArray) {

        this.xyArray = xyArray;
    }

    public XYArray getXyArray() {
        return this.xyArray;
    }

    public void setXyArray(XYArray xyArray) {
        this.xyArray = xyArray;
    }

    public XYArray getRegimeXyArray() {
        return this.regimeXyArray;
    }

    public void setRegimeXyArray(XYArray regimeXyArray) {
        this.regimeXyArray = regimeXyArray;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }
}
