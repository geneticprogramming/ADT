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

package com.infoblazer.gp.evolution.geneticprogram;

import com.infoblazer.gp.application.data.model.FitnessEvaluation;
import com.infoblazer.gp.application.fitness.FitnessEvaluator;
import com.infoblazer.gp.evolution.model.ResultProducingProgram;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by David on 5/9/2015.
 */
@Component
public class PredictionProgram extends AbstractPredictionProgram {






        private final static Logger logger = Logger.getLogger(PredictionProgram.class.getName());

    @Override
    public Double predict(PredictionState predictionState, ResultProducingProgram program,
                          int startPos, int predictionPos, FitnessEvaluator evaluator, Map<String, Object> params) {

        FitnessEvaluation result = evaluator.evaluate(program,startPos, predictionPos,maxDepth,selectionStrategy.getDirection());
        predictionState.setRegime(result.getRegimeXyArray().getY(predictionPos));
        return result.getXyArray().getY(predictionPos);

    }

    @Override
    protected PredictionState initializePredictionState(int predictionPos) {


        return new PredictionStateImpl(fitnessEvaluator.getTargetSeries().getLength());


    }
}
