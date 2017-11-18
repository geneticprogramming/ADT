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

package com.infoblazer.gp.application.fitness;

import com.infoblazer.gp.application.data.model.FitnessEvaluation;
import com.infoblazer.gp.application.data.model.XYArray;
import com.infoblazer.gp.application.syntheticdata.XYSeries;
import com.infoblazer.gp.application.syntheticdata.XYSeriesSet;
import com.infoblazer.gp.evolution.model.*;
import com.infoblazer.gp.evolution.library.RegimeLibrary;
import com.infoblazer.gp.evolution.library.ResultLibrary;
import com.infoblazer.gp.evolution.selectionstrategy.SelectionStrategy;

/**
 * Created by David on 5/26/2014.
 */
public interface FitnessEvaluator {
    FitnessEvaluation evaluate(ResultProducingProgram resultProducingProgram,
                               Integer windowStart, Integer windowEnd, final int maxDepth,
                               final SelectionStrategy.Direction direction);


    FitnessEvaluation calculateProgramFitness(Integer windowStart, Integer windowEnd, Integer maxLevel, ResultProducingProgram program,
                                              SelectionStrategy.Direction direction );


    void drawTargetSeries();

    void drawPredictedSeries(int currentTestingGeneration, XYArray xyArray);

    void drawTrainingRegimeSeries(XYArray xyArray);

    XYSeries getTargetSeries();

    void setTargetSeries(XYSeries xySeries);

    public Integer getAndResetFitnessCalculations();

    public Integer getAndResetFitnessEvaluations();


    void afterGeneration(Population population, SelectionStrategy.Direction direction,
                         int generation, int trainingGenerations, Integer maxLevel, Integer windowEnd, boolean lastTrainingThisGeneration);

    int getSeriesStart();

    int getSeriesEnd();

    void drawTrainingSeries(XYArray xyArray);

    void drawPredictedRegimeSeries(XYArray regimeXyArray);

    void setResultLibrary(ResultLibrary resultLibrary);

    void setRegimeLibrary(RegimeLibrary regimeLibrary);

    ResultLibrary getResultLibrary();

    RegimeLibrary getRegimeLibrary();

    XYSeriesSet getXySeriesSet();

    void setXySeriesSet(XYSeriesSet xySeriesSet);

    void drawTestingSeries(int i, XYArray xyArray);

    void drawTestingRegimeSeries(XYArray regimeXyArray);

}
