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

import com.infoblazer.gp.application.fitness.AbstractFitnessEvaluator;
import com.infoblazer.gp.application.fitness.DyforGpFitnessEvaluator;
import com.infoblazer.gp.application.syntheticdata.XYSeries;
import com.infoblazer.gp.application.fitness.FitnessEvaluator;
import com.infoblazer.gp.application.syntheticdata.XYSeriesSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


/**
 * Main ap for Dyfor Sythetic time series 1
 * Created by David on 5/22/2014.
 */
@Component
@Qualifier("linearRegressionDyforGp")
public class LinearRegressionDyforGp extends LinearRegressionApp {

   @Autowired
   private ApplicationContext context;


    @Autowired
    private DyforGpFitnessEvaluator fitnessEvaluator;



    @Override
    protected final FitnessEvaluator buildFitnessEvaluator(XYSeriesSet xySeriesSet) {
        fitnessEvaluator.setXySeriesSet(xySeriesSet);
        return fitnessEvaluator;
    }








}