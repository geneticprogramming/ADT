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
 * Created by David on 8/28/2015.
 */
public class TimeseriesSet {
    private Timeseries timeseries;
    private Normalization normalization;
    private int step;
    private int normalizationWindow;

    public Timeseries getTimeseries() {
        return this.timeseries;
    }

    public void setTimeseries(Timeseries timeseries) {
        this.timeseries = timeseries;
    }

    public Normalization getNormalization() {
        return this.normalization;
    }

    public void setNormalization(Normalization normalization) {
        this.normalization = normalization;
    }

    public int getStep() {
        return this.step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getNormalizationWindow() {
        return this.normalizationWindow;
    }

    public void setNormalizationWindow(int normalizationWindow) {
        this.normalizationWindow = normalizationWindow;
    }
}
