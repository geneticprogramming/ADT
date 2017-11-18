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

import com.infoblazer.gp.application.data.model.*;
import com.infoblazer.gp.application.syntheticdata.DateXYSeries;
import com.infoblazer.gp.application.syntheticdata.XYSeries;
import com.infoblazer.gp.application.syntheticdata.XYSeriesSet;
import com.infoblazer.gp.evolution.library.RegimeLibrary;
import com.infoblazer.gp.evolution.library.ResultLibrary;
import com.infoblazer.gp.evolution.model.*;
import com.infoblazer.gp.evolution.primitives.functions.Adf;
import com.infoblazer.gp.evolution.selectionstrategy.SelectionStrategy;
import com.infoblazer.gp.visualization.Chart;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by David on 8/17/2014.
 */
public abstract class AbstractFitnessEvaluator implements FitnessEvaluator {
    private final static Logger logger = Logger.getLogger(AbstractFitnessEvaluator.class.getName());
    public static final int EVALUATOR_INITIAL_LEVEL = 1; // start evaluation at level 1
    protected static Double CLOSE_ENOUGH = 0.0001;
    protected static Double TRIVIAL_LIMIT_PCT = 0.05;


    protected AtomicInteger fitnessEvaluations = new AtomicInteger();
    protected AtomicInteger fitnessCalculations = new AtomicInteger();

    @Value("${meanSquaredError:#{false}}")
    protected boolean useMeanSquaredError;



    @Value("${useAverageError:#{true}}")
    protected boolean useAverageError; // divide total error by predictions
    @Value("${useEma:#{false}}")
    protected boolean useEma; // exponential moving average

    @Autowired
    protected Chart chart;
    @Value("${allowTrivialPredictions:#{true}}")
    protected  boolean allowTrivialPredictions;
    protected XYSeriesSet xySeriesSet = new XYSeriesSet();

    protected Integer seriesStart;
    protected Integer seriesEnd;


    @Autowired
    protected ResultLibrary resultLibrary;
    @Autowired
    protected RegimeLibrary regimeLibrary;

    public void setResultLibrary(ResultLibrary resultLibrary) {
        this.resultLibrary = resultLibrary;
    }


    public void setRegimeLibrary(RegimeLibrary regimeLibrary) {
        this.regimeLibrary = regimeLibrary;
    }

    public XYSeriesSet getXySeriesSet() {
        return xySeriesSet;
    }

    public void setXySeriesSet(XYSeriesSet xySeriesSet) {
        this.xySeriesSet = xySeriesSet;
    }

    @Override
    public int getSeriesStart() {
        if (seriesStart == null) {
            return 0;
        } else {
            return seriesStart;
        }
    }

    @Override
    public int getSeriesEnd() {
        if (seriesStart == null) {
            return xySeriesSet.getTargetSeries().getLength() - 1;
        } else {
            return seriesEnd;
        }
    }


    public XYSeries getTargetSeries() {
        return xySeriesSet.getTargetSeries();
    }

    public void setTargetSeries(XYSeries targetSeries) {
        xySeriesSet.setTargetSeries(targetSeries);
    }

    @Override
    public void afterGeneration(Population population, SelectionStrategy.Direction direction, int generation, int trainingGenerations,
                                Integer maxLevel, Integer windowEnd,boolean lastTrainingThisGeneration) {

    }


    public void buildXYSeriesSet(XYSeriesSet xySeriesSet) {
        TimeseriesSet targetTimeseriesSet = xySeriesSet.getTargetTimeseries();
        Timeseries timeseries = targetTimeseriesSet.getTimeseries();
        Normalization normalization = targetTimeseriesSet.getNormalization();
        int step = targetTimeseriesSet.getStep();
        int normalizationWindow = targetTimeseriesSet.getNormalizationWindow();
        DateXYSeries dateXYSeries = new DateXYSeries(timeseries, normalization, step, normalizationWindow);
        this.xySeriesSet.setTargetSeries(dateXYSeries);
        for (String series : xySeriesSet.getSeriesList()) {
            TimeseriesSet timeseriesSet = xySeriesSet.getSeries(series);
            timeseries = timeseriesSet.getTimeseries();
            normalization = timeseriesSet.getNormalization();
            normalizationWindow =timeseriesSet.getNormalizationWindow();
            step = timeseriesSet.getStep();
            dateXYSeries = new DateXYSeries(timeseries, normalization, step, normalizationWindow);
            this.xySeriesSet.setXYSeries(series, dateXYSeries);
        }
    }

    /**
     * this will return null if the programs have the same fitness  (maybe not a good idea)
     *
     * @param fitness1
     * @param fitness2
     * @param direction
     * @return
     */
    public static Boolean isFitter(Double fitness1, Double fitness2, SelectionStrategy.Direction direction) {
        Boolean fitter = null;

        if (fitness1 != null || fitness2 != null) {

            if (fitness1 == null || Double.isNaN(fitness1) || Double.isInfinite(fitness1)) {
                fitter = false;
            } else if (fitness2 == null || Double.isNaN(fitness2) || Double.isInfinite(fitness2)) {
                fitter = true;

            } else if (!fitness1.equals(fitness2)) {
                if (direction == SelectionStrategy.Direction.ASCENDING) {
                    fitter = (fitness1 < fitness2);
                } else {//descending
                    fitter = (fitness1 > fitness2);
                }
            }
        }

        return fitter;

    }

    /**
     * @param program1  is this program fitter
     * @param program2  than this program
     * @param direction ascending: lowest fitness is fitter, descending: higher fitness is fitter
     * @return
     */
    public static Boolean isFitter(AbstractProgram program1, AbstractProgram program2, SelectionStrategy.Direction direction) {
        Boolean fitter = null;


        if (program1 == null) {
            fitter = false;
        } else if (program2 == null) {
            fitter = true;
        } else {
            fitter = isFitter(program1.getFitness(), program2.getFitness(), direction);
            if (fitter == null) {
                fitter = isProbablyBetterProgram(program1, program2);
            }
        }

        return fitter;

    }

    public static AbstractProgram fittest(AbstractProgram program1, AbstractProgram program2, SelectionStrategy.Direction direction) {
        Boolean compare = isFitter(program1, program2, direction);
        if (compare != null) {
            if (compare) {
                return program1;
            } else {
                return program2;
            }
        }
        return null;

    }


    @Override
    public abstract FitnessEvaluation evaluate(ResultProducingProgram program,
                                               Integer windowStart,
                                               Integer windowEnd,
                                               final int maxDepth,
                                               SelectionStrategy.Direction direction

                                              );

    /**
     * Return fittest so far
     *
     * @param population
     * @return
     */


    public FitnessEvaluation calculateProgramFitness(Integer windowStart, Integer windowEnd, Integer maxLevel,
                                                     ResultProducingProgram program, SelectionStrategy.Direction direction) {

        FitnessEvaluation result = null;

        FitnessEvaluation fitnessEvaluation = evaluate(program, windowStart, windowEnd, maxLevel,direction);
        program.setFitness(fitnessEvaluation.getFitness());
        RegimeDetectionProgram regimeDetectionProgram = program.getRegimeDetectionProgram();
        if (regimeDetectionProgram != null) {
            regimeDetectionProgram.setFitness(fitnessEvaluation.getFitness());
        }
        result = fitnessEvaluation;
        logger.trace("returned fitness: " + fitnessEvaluation.getFitness());

        return result;

    }


    @Override
    public void drawTargetSeries() {

        chart.addSeries("Target", this.xySeriesSet.getTargetSeries().getX(), this.xySeriesSet.getTargetSeries().getY());

    }

    @Override
    public void drawTrainingSeries(XYArray xyArray) {

        chart.addSeries("training", xyArray.getxVals(), xyArray.getyVals());

    }

    @Override
    public void drawPredictedSeries(int currentTestingGeneration, XYArray xyArray) {

        chart.addSeries("Predicted" + currentTestingGeneration, xyArray.getxVals(), xyArray.getyVals());

    }

    @Override
    public void drawTestingSeries(int currentTestingGeneration, XYArray xyArray) {
        chart.addSeries("Testing" + currentTestingGeneration, xyArray.getxVals(), xyArray.getyVals());
    }

    @Override
    public void drawTestingRegimeSeries(XYArray regimeXyArray) {
        chart.addSeries("TestingRegime", regimeXyArray.getxVals(), regimeXyArray.getyVals());
    }

    @Override
    public void drawTrainingRegimeSeries(XYArray xyArray) {

        chart.addSeries("Regime", xyArray.getxVals(), xyArray.getyVals());

    }

    @Override
    public void drawPredictedRegimeSeries(XYArray regimeXyArray) {

        chart.addSeries("Regime", regimeXyArray.getxVals(), regimeXyArray.getyVals());

    }

    public static Map<String, Adf> buildAdfMap(List<Adf> adfs) {
        Map<String, Adf> adfMap = null;
        if (adfs != null) {
            adfMap = new HashMap<String, Adf>();
            for (Adf adf : adfs) {
                adfMap.put(adf.getName(), adf);
            }

        }
        return adfMap;
    }


    /**
     * return 1,2,3,4 for a four way fitness oomparison
     *
     * @param direction
     * @param fitness1
     * @param fitness2
     * @param fitness3
     * @param fitness4
     * @param program1
     * @param program2
     * @param regimeDetectionProgram1
     * @param regimeDetectionProgram2 @return
     */
    public static Integer fittest(SelectionStrategy.Direction direction,
                                  Double fitness1, Double fitness2, Double fitness3, Double fitness4) {

        //TODO how is regime taken into account, add all up?

        Integer result = null;
        Double bestFitness = fitness1;

        //Fitness 2 = program 1, regimeProgram 2
        Boolean comparison = isFitter(fitness2, bestFitness, direction);

        if (comparison != null && comparison) {
            bestFitness = fitness2;
            result = 2;
        }
        //Fitness 3 = program 2, regimeProgram 1
        comparison = isFitter(fitness3, bestFitness, direction);

        if (comparison != null && comparison) {
            bestFitness = fitness3;
            result = 3;
        }
        //Fitness 3 = program 2, regimeProgram 2
        comparison = isFitter(fitness4, bestFitness, direction);
        if (comparison != null && comparison) {
            bestFitness = fitness4;
            result = 4;
        }

        return result;
    }


    public static boolean isProbablyBetterProgramNoNulls(Program program1, Program program2) {
        Boolean result = isProbablyBetterProgram(program1, program2);
        if (result == null) {
            return false;
        } else {
            return result.booleanValue();
        }

    }

    /**
     * check the size of the program to determine which is likley better.
     * Not currently used
     *
     * @param program1
     * @param regimeDetectionProgram1
     * @param program2
     * @param regimeDetectionProgram2
     * @return
     */
    public static Boolean isProbablyBetterProgram(Program program1, Program program2) {
        Boolean firstBetter = null;

        if (program1 == null && program2 == null) {
            return null;
        }
        if (program1 == null) {
            return false;
        }
        if (program2 == null) {
            return true;
        }

        if (program1.getNodeCount() == null) {
            program1.calculateMetrics();

        }
        if (program2.getNodeCount() == null) {
            program2.calculateMetrics();
        }
        if (program1.getNodeCount() != 1 && program1.getNodeCount() < program2.getNodeCount()) {
            firstBetter = true;
        } else if (program2.getNodeCount() != 1 && program2.getNodeCount() < program1.getNodeCount()) {
            firstBetter = false;
        }


        return firstBetter;
    }


    @Override
    public Integer getAndResetFitnessCalculations() {
        return fitnessCalculations.getAndSet(0);
    }

    @Override
    public Integer getAndResetFitnessEvaluations() {
        return fitnessEvaluations.getAndSet(0);
    }


    @Override
    public ResultLibrary getResultLibrary() {
        return resultLibrary;
    }

    @Override
    public RegimeLibrary getRegimeLibrary() {
        return regimeLibrary;
    }
}


