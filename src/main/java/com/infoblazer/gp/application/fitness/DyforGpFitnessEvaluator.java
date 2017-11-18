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
import com.infoblazer.gp.evolution.geneticprogram.PredictionProgram;
import com.infoblazer.gp.evolution.model.Population;
import com.infoblazer.gp.evolution.model.ProgramComparator;
import com.infoblazer.gp.evolution.model.RegimeDetectionProgram;
import com.infoblazer.gp.evolution.model.ResultProducingProgram;
import com.infoblazer.gp.evolution.selectionstrategy.SelectionStrategy;
import com.infoblazer.gp.evolution.utils.GpUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * Created by David on 5/31/2014.
 */
@Component
public class DyforGpFitnessEvaluator extends NumericFitnessEvaluator implements FitnessEvaluator,DyforGpEvaluator {

    private final static Logger logger = Logger.getLogger(DyforGpFitnessEvaluator.class.getName());

    //Dyfor Params , should be passeed in
    @Value("${maxWindowSize:#{null}}")
    private Integer maxWindowSize; //= 200;
    @Value("${predictionSize:#{1}}")
    private int predictionSize; //10;
    @Value("${minWindowSize:#{null}}")
    private Integer minWindowSize; //20;
    @Value("${windowDifference:#{null}}")
    private Integer windowDifference; //20;
    @Value("${startWindowSize:#{null}}")
    private Integer startWindowSize; //80; //smaller of two windows . start of prediction phase. may be same as training window
    private int currentWindowSmall;
    private int currentWindowLarge;



    @Value("${resetOnNoTrend:#{false}}")
    private boolean resetOnNoTrend;


    @Value("${saveoff:#{1}}")
    private Integer saveOff;

    private int n = 0; //number of consecutive expansions/contractions so far
    @Value("${N:#{null}}")
    private Integer N; //number of consecutive  expansions/contractions before save
    private Trend trend = null;
    private ResultProducingProgram[] potentiallyDormantSolutions;
    private ResultProducingProgram[] dormant;
    private ResultProducingProgram[] injectable;


    private enum Trend {CONTRACTION, EXPANSION, NONE}


    @PostConstruct
    public void init() {
        if (startWindowSize != null) { //not a dyfor app
            potentiallyDormantSolutions = new ResultProducingProgram[saveOff];
            currentWindowSmall = startWindowSize;
            currentWindowLarge = currentWindowSmall + windowDifference;
        }
    }


    @Override
    public FitnessEvaluation evaluate(ResultProducingProgram program,
                                      final Integer pWindowStart, final Integer pWindowEnd, final int maxDepth, SelectionStrategy.Direction direction) {
        //window start is the non-dyfor start

        if (program == null) {
            logger.error("Attempting to evaluate fitness with a null program");
            FitnessEvaluation fitnessEvaluation = new FitnessEvaluation(null);
            fitnessEvaluation.setFitness(Double.POSITIVE_INFINITY);
        }

        Integer windowStart = null;

        int programId = program.getId();

        if ((programId & 1) == 1) {  //ids should be evenly divided between odd and even just about
            windowStart = pWindowEnd - currentWindowSmall;
        } else {

            windowStart = pWindowEnd - currentWindowLarge;
        }


        if (windowStart < 0) {
            logger.debug("setting window start to 0. Was " + windowStart);
            windowStart = 0;
        }
        return super.evaluate(program,  windowStart, pWindowEnd, maxDepth, direction);
    }



     /*

      */

    @Override
    public void afterGeneration(Population population, SelectionStrategy.Direction direction, int generation,
                                int trainingGenerations, Integer maxLevel, Integer windowEnd,boolean lastTrainingThisGeneration) {
        if (lastTrainingThisGeneration && generation > trainingGenerations) {//pass in or set, initial training period   plus 20 rounds per move
            comparePrograms(population, direction, maxLevel, windowEnd);
        }
    }

    private void comparePrograms(Population population, SelectionStrategy.Direction direction, Integer maxLevel, int predictionPos) {

        ResultProducingProgram bestSmall = DyforGpEvaluator.findBestSmall(population.getResultPopulation(), direction);
        if (bestSmall == null) {
            logger.error("There are no programs using the smaller training window");
        }
        ResultProducingProgram bestLarge = DyforGpEvaluator.findBestLarge(population.getResultPopulation(), direction);
        if (bestLarge == null) {
            logger.error("There are no programs using the larger training window");
        }
        int start = predictionPos + 1;
        int end = predictionPos + predictionSize;
        if (start > xySeriesSet.getTargetSeries().getLength()) {
            start = xySeriesSet.getTargetSeries().getLength();
        }
        if (end > xySeriesSet.getTargetSeries().getLength()) {
            end = xySeriesSet.getTargetSeries().getLength();
        }
        //Reevaluate looking forward
        //really need to prediction here

        //Call default evaluation method (don't use window sizing)
        FitnessEvaluation fitnessEvaluationSmall = super.evaluate(bestSmall,  start, end, maxLevel, direction);//Can choose any regime program, they are irrelevant for DyforS
        FitnessEvaluation fitnessEvaluationLarge = super.evaluate(bestLarge,  start, end, maxLevel, direction);
        double fitnessSmall = fitnessEvaluationSmall.getFitness();
        double fitnessLarge = fitnessEvaluationLarge.getFitness();
        //now make a prediciton with the best series
        resizeWindows(direction, fitnessSmall, fitnessLarge);

        //see about saving out solutions
        if (n > N) {
            saveSolutions(population, direction);
        }


    }

    public void resizeWindows(SelectionStrategy.Direction direction, double fitnessSmall, double fitnessLarge) {
        if ((direction == SelectionStrategy.Direction.DESCENDING && fitnessSmall > fitnessLarge) ||
                (direction == SelectionStrategy.Direction.ASCENDING && fitnessSmall < fitnessLarge)) {
            shrinkWindows();
        } else if ((direction == SelectionStrategy.Direction.DESCENDING && fitnessLarge > fitnessSmall) ||
                (direction == SelectionStrategy.Direction.ASCENDING && fitnessLarge < fitnessSmall)) {
            expandWindows();
        } else {
            logger.info("Keeping Window stable");
            if (resetOnNoTrend) {
                trend = Trend.NONE;
                n = 0; //reset counter of exansion/contraction
            }
        }

        validateWindows();
    }

    public void expandWindows() {
        //expand  windows

        logger.info("Expanding Window");
        currentWindowLarge = currentWindowLarge + windowDifference;
        currentWindowSmall = currentWindowSmall + windowDifference;
        if (trend == Trend.EXPANSION) {
            n++;
        } else {
            n = 0;
        }
        trend = Trend.EXPANSION;
    }

    public void shrinkWindows() {
        //shrink windows
        logger.info("Shrinking Window");
        currentWindowLarge = currentWindowLarge - windowDifference;
        currentWindowSmall = currentWindowSmall - windowDifference;
        if (trend == Trend.CONTRACTION) {
            n++;
        } else {
            n = 0;
        }
        trend = Trend.CONTRACTION;
    }

    public void validateWindows() {
        if (currentWindowSmall < minWindowSize) {
            currentWindowSmall = minWindowSize;
            currentWindowLarge = currentWindowSmall + windowDifference;
        }
        if (currentWindowLarge > maxWindowSize) {
            currentWindowLarge = maxWindowSize;
            currentWindowSmall = currentWindowLarge - windowDifference;

        }

        logger.debug("currentWindowSmall=" + currentWindowSmall);
        logger.debug("currentWindowLarge=" + currentWindowLarge);
    }

    public void saveSolutions(Population population, SelectionStrategy.Direction direction) {
        Collections.sort(population.getResultPopulation(), new ProgramComparator(direction));
        if (trend == Trend.EXPANSION) {
            //TODO should probably do this just once after population creation
            for (int i = 0; i < saveOff; i++) {
                ResultProducingProgram nextBest = population.getResultPopulation().get(i);
                ResultProducingProgram copy = GpUtils.getKyroInstance().copy(nextBest);
                potentiallyDormantSolutions[i] = copy;

            }
        } else if (trend == Trend.CONTRACTION) {//contracting ,
            //Inject dormant  ``
            //store new dormans from current environment
            if (n == N + 1) { //Initial contraction
                injectable = dormant;
                dormant = potentiallyDormantSolutions;   //now switch dormancies
                potentiallyDormantSolutions = new ResultProducingProgram[saveOff];
            }
            inject(population.getResultPopulation()); //Keep injecting dormant , per Wagner et al 2007
        }
    }


    public void inject(List<ResultProducingProgram> sortedPopulation) {
        //replace the worst in the population with the dormant solutions
        if (injectable != null) {
            for (int i = 0; i < injectable.length; i++) {
                ResultProducingProgram dormant = injectable[i];
                if (dormant != null) {
                    logger.info("Injecting Dormant solutions");
                    sortedPopulation.set(sortedPopulation.size() - i - 1, dormant);
                } else {
                    logger.error("Attemped to inject a null program");
                }
            }
        }


    }


}
