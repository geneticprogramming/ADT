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

import com.infoblazer.gp.application.data.service.MetricsService;
import com.infoblazer.gp.application.fitness.FitnessEvaluator;
import com.infoblazer.gp.application.syntheticdata.XYSeriesSet;
import com.infoblazer.gp.evolution.geneticprogram.AbstractGeneticProgram;
import com.infoblazer.gp.evolution.geneticprogram.LinearRegressionProgram;
import com.infoblazer.gp.evolution.geneticprogram.PredictionProgram;

import com.infoblazer.gp.evolution.primitives.FunctionContext;
import com.infoblazer.gp.evolution.primitives.FunctionSet;
import com.infoblazer.gp.evolution.primitives.GP_TYPES;
import com.infoblazer.gp.evolution.primitives.TerminalSet;
import com.infoblazer.gp.evolution.primitives.functions.Function;
import com.infoblazer.gp.evolution.primitives.terminals.Terminal;
import com.infoblazer.gp.evolution.selectionstrategy.SelectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 11/3/2014.
 */
public abstract class AbstractGpApp implements GpApp {


    final XYSeriesSet xySeriesSet = new XYSeriesSet();

    @Autowired
    private LinearRegressionProgram linearRegressionProgram;

    @Autowired
    private PredictionProgram predictionProgram;



    @Autowired
    MetricsService metricsService;


    @Value("${terminals}")
    private String[] terminalNames;
    @Value("${functions}")
    private String[] functionNames;
    @Value("${regimeFunctions:#{null}}")
    private String[] regimeFunctionNames;


    @Value("${returnType}")
    private String returnTypeArg;


    @Value("${selectionStrategy}")
    private String selectionStrategyName;


    @Value("${programType}")
    private String programType;
    @Value("${direction}")
    private String directionString;
    private List<String> series;
    private TerminalSet terminalSet;
    private FunctionSet functionSet;
    private FunctionSet regimeFunctionSet;
    private FitnessEvaluator fitnessEvaluator;
    private GP_TYPES returnType;
    @Autowired
    private ConfigurableApplicationContext context;
    private SelectionStrategy selectionStrategy;

    public void init() {


        terminalSet = buildTerminalSet(context, terminalNames, FunctionContext.RPB);

        functionSet = buildFunctionSet(context, functionNames);

        if (regimeFunctionNames != null) {
            regimeFunctionSet = buildFunctionSet(context, regimeFunctionNames);
        } else {
            regimeFunctionSet = functionSet;
        }


        fitnessEvaluator = buildFitnessEvaluator(xySeriesSet);


        if (returnTypeArg.equals("Boolean")) {
            returnType = GP_TYPES.BOOLEAN;
        } else {
            returnType = GP_TYPES.NUMBER;
        }


        series = xySeriesSet.getSeriesList();

        selectionStrategy = buildSelectionStrategy(context, selectionStrategyName, fitnessEvaluator,
                functionSet, regimeFunctionSet, terminalSet, series);


        metricsService.setApplicationName(getClass().getName());


    }

    @Override
    public void runApp() {
        AbstractGeneticProgram geneticProgram = null;

        if (programType.equals("LinearRegression")) {
            geneticProgram = linearRegressionProgram;
        } else if (programType.equals("Prediction")) {
            geneticProgram = predictionProgram;
        }  
        geneticProgram.init(functionSet, regimeFunctionSet, terminalSet, fitnessEvaluator, selectionStrategy, returnType, series);
        geneticProgram.run();

        System.out.println("Done");
    }

    private SelectionStrategy buildSelectionStrategy(ApplicationContext context, String selectionStrategyName,
                                                     FitnessEvaluator fitnessEvaluator,
                                                     FunctionSet functionSet,
                                                     FunctionSet regimefunctionSet,
                                                     TerminalSet terminalSet,
                                                     List<String> series) {
        SelectionStrategy selectionStrategy = (SelectionStrategy) context.getBean(selectionStrategyName);

        selectionStrategy.setFitnessEvaluator(fitnessEvaluator);

        selectionStrategy.setSeries(series);


        selectionStrategy.setFunctionSet(functionSet);
        selectionStrategy.setRegimeFunctionSet(regimefunctionSet);
        selectionStrategy.setTerminalSet(terminalSet);

        if (directionString.equals("asc")) {
            selectionStrategy.setDirection(SelectionStrategy.Direction.ASCENDING);
        } else if (directionString.equals("desc")) {
            selectionStrategy.setDirection(SelectionStrategy.Direction.DESCENDING);
        } else {
            //error
        }
        return selectionStrategy;
    }


    protected abstract FitnessEvaluator buildFitnessEvaluator(XYSeriesSet xySeries);


    private FunctionSet buildFunctionSet(ApplicationContext context, String[] functionNames) {
        Function[] functions = new Function[functionNames.length];
        int i = 0;
        for (String functionName : functionNames) {
            Function function = parseFunction(context, functionName);
            functions[i++] = function;
        }

        FunctionSet functionSet = new FunctionSet(functions);
        return functionSet;
    }

    private Function parseFunction(ApplicationContext context, String functionName) {
        Function function = null;
        //parse mv(window)
        if (functionName.contains("(")) {
            String parsedName = functionName.substring(0, functionName.indexOf('('));
            String paramString = functionName.substring(functionName.indexOf('(') + 1, functionName.length() - 1);
            String[] params = paramString.split(",");
            String[] paramVals = new String[params.length];
            for (int i = 0; i < params.length; i++) {

                paramVals[i] = params[i];

            }
            function = (Function) context.getBean(parsedName);
            function.setParams(paramVals);


        } else {
            function = (Function) context.getBean(functionName);
        }
        return function;

    }


    private TerminalSet buildTerminalSet(ApplicationContext context, String[] terminalNames, FunctionContext functionContext) {
        List<Terminal> terminals = new ArrayList<Terminal>();
        int i = 0;
        for (String terminalName : terminalNames) {
            Terminal terminal = parseTerminal(context, terminalName);
            if (terminal.allowInContext(functionContext)) {
                terminals.add(terminal);
            }


        }

        Terminal[] terminalArray = new Terminal[terminals.size()];
        terminalArray = terminals.toArray(terminalArray);


        TerminalSet terminalSet = new TerminalSet(terminalArray);
        return terminalSet;
    }

    private Terminal parseTerminal(ApplicationContext context, String terminalName) {
        Terminal terminal = null;
        //parse randomInteger(lowRandom highRadom)
        if (terminalName.contains("(")) {
            String parsedName = terminalName.substring(0, terminalName.indexOf('('));
            String paramString = terminalName.substring(terminalName.indexOf('(') + 1, terminalName.length() - 1);
            String[] params = paramString.split(" ");
            String[] paramVals = new String[params.length];
            for (int i = 0; i < params.length; i++) {
                paramVals[i] = params[i];
            }
            terminal = (Terminal) context.getBean(parsedName);
            terminal.setParams(paramVals);


        } else {
            terminal = (Terminal) context.getBean(terminalName);
        }
        return terminal;
    }


}
