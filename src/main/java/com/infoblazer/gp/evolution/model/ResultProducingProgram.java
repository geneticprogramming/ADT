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

package com.infoblazer.gp.evolution.model;

import com.infoblazer.gp.evolution.geneticprogram.AbstractGeneticProgram;
import com.infoblazer.gp.evolution.primitives.FunctionSet;
import com.infoblazer.gp.evolution.primitives.GP_TYPES;
import com.infoblazer.gp.evolution.primitives.Primitive;
import com.infoblazer.gp.evolution.primitives.TerminalSet;
import com.infoblazer.gp.evolution.primitives.functions.AbstractFunction;
import com.infoblazer.gp.evolution.primitives.functions.Adf;
import com.infoblazer.gp.evolution.primitives.functions.Function;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by David on 5/22/2014.
 */
public class ResultProducingProgram extends AbstractProgram {

    private final static Logger logger = Logger.getLogger(ResultProducingProgram.class.getName());

    protected RegimeDetectionProgram regimeDetectionProgram;

    public RegimeDetectionProgram getRegimeDetectionProgram() {
        return this.regimeDetectionProgram;
    }

    public void setRegimeDetectionProgram(RegimeDetectionProgram regimeDetectionProgram) {
        this.regimeDetectionProgram = regimeDetectionProgram;
    }

    public static ResultProducingProgram generateProgram(FunctionSet functionSet, TerminalSet terminalSet, Map<String,FunctionSet> aritySet, String[] adfArities, List<String> series,
                                                         int maxDepth,  int regimes, GrowMethod growMethod, GP_TYPES returnType) {

        ResultProducingProgram program = new ResultProducingProgram();

        FunctionSet combinedFunctionSet = functionSet;
        if (adfArities != null && adfArities.length > 0) {
            List<Adf> adfList = AbstractGeneticProgram.initializeAdf(functionSet,  adfArities,aritySet,series, maxDepth, regimes, growMethod);

            program.setAdfs(adfList);

            Function[] adfSet = new Function[adfList.size()];
            for (int i = 0; i < adfList.size(); i++) {
                adfSet[i] = adfList.get(i);
            }
            combinedFunctionSet = AbstractFunction.addAll(functionSet.getItems(), adfSet);
        }

        program.root = generatePrimitive(returnType, combinedFunctionSet,null, terminalSet,series, maxDepth, maxDepth,growMethod,
                functionSet.getLength() > 0, null, false);

        return program;
    }


}
