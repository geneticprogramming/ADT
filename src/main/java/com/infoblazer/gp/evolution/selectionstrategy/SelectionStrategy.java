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

package com.infoblazer.gp.evolution.selectionstrategy;

import com.infoblazer.gp.evolution.model.*;
import com.infoblazer.gp.application.fitness.FitnessEvaluator;
import com.infoblazer.gp.evolution.primitives.FunctionSet;
import com.infoblazer.gp.evolution.primitives.TerminalSet;

import java.util.List;

/**
 * Created by David on 8/7/2014.
 */
public interface SelectionStrategy {

    Direction getDirection();

    void setDirection(Direction direction);

    Pair<AbstractProgram> doCrossOver(final ResultProducingProgram parent1, final ResultProducingProgram parent2, final int maxDepth,
                                      final int maxSize, SelectionStrategy.Direction direction, int regimes, Integer predictedRegime);

    AbstractProgram mutation(ResultProducingProgram program, GrowMethod growMethod,Integer predictedRegime);
    void setFitnessEvaluator(FitnessEvaluator fitnessEvaluator) ;
     void setSeries(List<String> series);


    void setFunctionSet(FunctionSet functionSet);
    void setRegimeFunctionSet(FunctionSet functionSet);
    void setTerminalSet(TerminalSet terminalSet);


    enum Direction {
        ASCENDING, //low fitness better
        DESCENDING; //high fitness better

        public  Double getMinFitness() {
            if (this == ASCENDING) {
                return Double.MAX_VALUE;
            } else {
                return 0.0D;
            }

        }

        public Direction reverse() {
            if (this == ASCENDING) {
                return DESCENDING;
            } else {
                return ASCENDING;
            }
        }
    }

    enum Approach {
        RANK, PROPORTIONAL
    }

   Population selectionNextGeneration(int generation,
                                      int trainingGenerations,
                                      GrowMethod growMethod,
                                      Population population,
                                      Integer maxNodes,
                                      Integer windowStart,
                                      Integer windowEnd,
                                      Integer predictedRegime,
                            boolean lastTrainingThisGeneration);


}
