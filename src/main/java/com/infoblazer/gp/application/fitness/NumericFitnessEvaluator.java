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
import com.infoblazer.gp.evolution.model.RegimeDetectionProgram;
import com.infoblazer.gp.evolution.model.ResultProducingProgram;
import com.infoblazer.gp.evolution.primitives.functions.Adf;
import com.infoblazer.gp.evolution.primitives.functions.BinaryNumber;
import com.infoblazer.gp.evolution.primitives.terminals.TerminalZero;
import com.infoblazer.gp.evolution.selectionstrategy.SelectionStrategy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 5/31/2014.
 */
@Component
public class NumericFitnessEvaluator extends AbstractFitnessEvaluator {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(NumericFitnessEvaluator.class.getName());


    public FitnessEvaluation evaluate(ResultProducingProgram resultProducingProgram,
                                      final Integer windowStart, final Integer windowEnd, final int maxDepth, SelectionStrategy.Direction direction) {

        //Note these offests are array based, with re
        ///int actualStart=0;
        //int actualEnd = targetSeries.getLength()-1;

        RegimeDetectionProgram regimeDetectionProgram = resultProducingProgram.getRegimeDetectionProgram();
        fitnessEvaluations.incrementAndGet();

        double totalError = 0d;
        int totalPredictions = 0;


        XYSeries targetSeries = xySeriesSet.getTargetSeries();
        int seriesLength = targetSeries.getLength();
        List<String> seriesList = xySeriesSet.getSeriesList();
        Double[] yVals = new Double[seriesLength];
        Double[] errors = new Double[seriesLength];
        Double[] regimeVals = new Double[seriesLength];
        Object[] xVals = new Object[seriesLength];
        Map<String, List<Double>> seriesMap = new HashMap<>();
        for (String seriesCode : seriesList) {
            seriesMap.put(seriesCode, new ArrayList<Double>(windowEnd-windowStart+1));
        }

        Map<String, Adf> adfs = null;
        if (resultProducingProgram.getAdfs() != null) {
            adfs = buildAdfMap(resultProducingProgram.getAdfs());
        }
        Map<String, Object> params = new HashMap<String, Object>();
        int trivialPredictions = 0;
        for (int i = 0; i < windowStart; i++) {  //add any series data prior to evaluation period
            for (String seriesName : seriesList) {
                List<Double> series = seriesMap.get(seriesName);
                series.add(xySeriesSet.getXYSeries(seriesName).getY(i));

            }
        }
        for (String seriesName : seriesList) {
            List<Double> series = seriesMap.get(seriesName);
            params.put(seriesName, series);
        }
        for (int i = windowStart; i <= windowEnd; i++) {
            boolean hitResursionError = false;

            if (i < seriesLength) {
                params.put("x", targetSeries.getX(i));
                for (String seriesName : seriesList) {
                    List<Double> series = seriesMap.get(seriesName);
                    series.add(xySeriesSet.getXYSeries(seriesName).getY(i));
                    params.put(seriesName,series);
                }
                params.put("serieslist", seriesList);
                Number calculated = null;
                Integer regime = null;

                if (regimeDetectionProgram != null) {
                    try {
                        if (!(regimeDetectionProgram.getRoot() instanceof BinaryNumber || regimeDetectionProgram.getRoot() instanceof TerminalZero)) {
                            logger.error("error in regime detection" + regimeDetectionProgram.asLanguageString(maxDepth));

                        }
                        Object regimeNumber = regimeDetectionProgram.getRoot().evaluate(false, 0, params, adfs, regimeLibrary, EVALUATOR_INITIAL_LEVEL, maxDepth);
                        if (regimeNumber != null) {
                            regime = ((Number) regimeNumber).intValue();
                        }
                    } catch (Exception e) {
                        try {

                            logger.error("Exception in regime detection" + regimeDetectionProgram.asLanguageString(maxDepth));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }
                    logger.trace("determined regime: " + regime);
                } else {
                    regime = 0;
                }
                try {
                    if (regime != null) {
                        regimeVals[i] = Double.valueOf(regime);
                        fitnessCalculations.incrementAndGet();
                        Object evaluationResult = resultProducingProgram.getRoot().evaluate(true, regime, params, adfs, resultLibrary, EVALUATOR_INITIAL_LEVEL, maxDepth);
                        if (evaluationResult != null) {
                            calculated = (Number) evaluationResult;
                        }
                    }
                } catch (Exception e) {
                    logger.error("Unexpected error evaluating fitness");
                    e.printStackTrace();

                }

                xVals[i] = targetSeries.getX(i);
                Number expected = targetSeries.getY(i);

                if (calculated == null || calculated.equals(Double.NaN) || calculated.equals(Double.NEGATIVE_INFINITY)
                        || calculated.equals(Double.POSITIVE_INFINITY)) {
                    totalError = Double.POSITIVE_INFINITY;
                    totalPredictions = 0;
                    if (hitResursionError || (calculated != null && (calculated.equals(Double.NEGATIVE_INFINITY)
                            || calculated.equals(Double.POSITIVE_INFINITY)))) {
                        break;
                    }
                    logger.debug("Encountered error evaluating program. Ignoring (or comment out) loop break. calculated=" + calculated);
                    break;// remove this to debug into error
                } else {
                    if (useMeanSquaredError) {
                        totalError = totalError + Math.pow(Math.abs(expected.doubleValue() - calculated.doubleValue()), 2);
                    } else {
                        totalError = totalError + Math.abs(expected.doubleValue() - calculated.doubleValue());
                        errors[i]=Math.abs(expected.doubleValue() - calculated.doubleValue());
                    }
                    totalPredictions++;
                    yVals[i] = calculated.doubleValue();
                    if (!allowTrivialPredictions && i > 0) {
                        Double lastVal = targetSeries.getY(i - 1);
                        if (Math.abs(calculated.doubleValue() - lastVal) < CLOSE_ENOUGH &&
                                Math.abs(expected.doubleValue() - lastVal) >= CLOSE_ENOUGH) {
                            trivialPredictions++;
                        }
                    }
                }

            }


        }

        XYArray xyArray = new XYArray(xVals, yVals);
        double meanSquaredError;
        if (totalPredictions == 0 || !useAverageError) {
            meanSquaredError = totalError;
        } else {
            meanSquaredError = totalError / totalPredictions;
        }
        if (useEma && !Double.isInfinite(meanSquaredError)) {
            double ema = calculateEMA(errors, windowStart, windowEnd);
            meanSquaredError = ema;
        }

        FitnessEvaluation fitnessEvaluation = new FitnessEvaluation(xyArray);
        fitnessEvaluation.setRegimeXyArray(new XYArray(xVals, regimeVals));
        if (!allowTrivialPredictions && (((totalPredictions - trivialPredictions) / Double.valueOf(totalPredictions)) < TRIVIAL_LIMIT_PCT)) {
            logger.debug("no trivial predictions allowed");
            meanSquaredError = direction.getMinFitness();

        }

        fitnessEvaluation.setFitness(meanSquaredError);
        return fitnessEvaluation;
    }

    public Double calculateEMA(Double[] vals, Integer start, Integer end) {


        Double lastEma = null;
        for (int i = start; i <= end; i++) {
            if (vals[i] != null) { //keep ema the same
                int periods = i-start+1;
                double multiplier = 2.0 / (periods + 1);

                double effectiveLastEma =lastEma==null?0:lastEma;
                double ema = ((vals[i] - effectiveLastEma) * multiplier) + effectiveLastEma;
                lastEma = ema;
            }

        }

        return lastEma;

    }


}
