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
import com.infoblazer.gp.application.data.model.Metrics;
import com.infoblazer.gp.application.data.service.MetricsService;
import com.infoblazer.gp.application.fitness.AbstractFitnessEvaluator;
import com.infoblazer.gp.application.fitness.FitnessEvaluator;
import com.infoblazer.gp.application.syntheticdata.DateXYSeries;
import com.infoblazer.gp.evolution.library.RegimeLibrary;
import com.infoblazer.gp.evolution.library.ResultLibrary;
import com.infoblazer.gp.evolution.model.*;
import com.infoblazer.gp.evolution.primitives.FunctionSet;
import com.infoblazer.gp.evolution.primitives.GP_TYPES;
import com.infoblazer.gp.evolution.primitives.Primitive;
import com.infoblazer.gp.evolution.primitives.TerminalSet;
import com.infoblazer.gp.evolution.primitives.functions.*;
import com.infoblazer.gp.evolution.primitives.terminals.AbstractTerminal;
import com.infoblazer.gp.evolution.primitives.terminals.Terminal;
import com.infoblazer.gp.evolution.selectionstrategy.AbstractSelectionStrategy;
import com.infoblazer.gp.evolution.selectionstrategy.SelectionStrategy;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Created by David on 5/22/2014.
 */


public abstract class AbstractGeneticProgram {

    private final static Logger logger = Logger.getLogger(AbstractGeneticProgram.class.getName());
    @Autowired
    private MetricsService metricsService;
    @Value("${adfArity:#{null}}")
    private String[] adfArities;

    @Value("${maxValidFitness:#{null}}") //ignore any fitness over this
    private Double maxValidFitness;
    @Value("${printTrainingProgram:#{false}}")
    private boolean printTrainingProgram; //output best training program each generation   to stdout

    protected Population population;

    protected SelectionStrategy selectionStrategy;
    @Value("${meanSquaredError:#{false}}")
    protected boolean useMeanSquaredError;

    @Value("${regimeSelection:#{true}}")
    protected Boolean regimeSelection;  // always use reproduction on regimes
    @Value("${drawRegimes:#{true}}")
    protected Boolean drawRegimes;  // always use reproduction on regimes

    @Value("${gcFrequency:#{1}}")
    private int gcFrequency;


    @Value("${populationSize}")
    private int initialPopulationSize;

    @Value("${maxTotalNodes:#{null}}")
    private Integer maxTotalNodes;
    private FunctionSet functionSet;
    private FunctionSet regimeFunctionSet;

    private List<String> series;
    private TerminalSet terminalSet;
    @Value("${maxDepth:#{999999}}")
    protected int maxDepth;
    @Value("${maxSize:#{999999}}")
    private int maxSize;
    @Value("${maxInitDepth}")
    private int maxInitDepth;

    @Value("${regimes:#{1}}")
    private int numberOfRegimes;
    @Value("${maxGenerations:#{999999}}")
    protected int maxGenerations;
    @Value("${testingGenerations:#{1}}")
    private int testingGenerations;
    @Value("${trainingGenerations}")
    private int trainingGenerations;



    @Value("${stagnationLimit:#{null}}")
    private Integer stagnationLimit;

    @Value("${startTrain:#{null}}")
    private String startTrain = null;
    @Value("${endTrain:#{null}}")
    private String endTrain = null;
    @Value("${startTest:#{null}}")
    private String startTest = null;
    @Value("${endTest:#{null}}")
    private String endTest = null;
    @Autowired
    private ResultLibrary resultLibrary;
    @Autowired
    private RegimeLibrary regimeLibrary;

    private GP_TYPES returnType;
    private GrowMethod growMethod = GrowMethod.HALF_HALF; //pass as param
    protected FitnessEvaluator fitnessEvaluator;


    public AbstractGeneticProgram() {
    }

    public void init(FunctionSet functionSet,
                     FunctionSet regimeFunctionSet,
                     TerminalSet terminalSet,
                     FitnessEvaluator fitnessEvaluator,
                     SelectionStrategy selectionStrategy,

                     GP_TYPES returnType,
                     List<String> series) {

        this.selectionStrategy = selectionStrategy;
        this.functionSet = functionSet;
        this.regimeFunctionSet = regimeFunctionSet;
        this.terminalSet = terminalSet;
        this.series = series;
        this.fitnessEvaluator = fitnessEvaluator;
        this.returnType = returnType;


    }


    public Population run() {
        fitnessEvaluator.drawTargetSeries();
        Integer metricId = metricsService.recordNewRun();


        //need a stopping criteria
        int generation = 0;
        int currentTestingGeneration = 0;

        int start = fitnessEvaluator.getSeriesStart();
        int end = fitnessEvaluator.getSeriesEnd();

        int startTrainPos = start + ((end - start) / 3);
        if (startTrain != null) {
            startTrainPos = getPositionParameter(startTrain);
        }
        int endTrainPos = start + (end - start) * 2 / 3;
        if (endTrain != null) {
            endTrainPos = getPositionParameter(endTrain);
        }
        Integer startTestPos = null;
        if (startTest != null) {
            startTestPos = getPositionParameter(startTest);
        }

        Integer endTestPos = null;
        if (endTest != null) {
            endTestPos = getPositionParameter(endTest);
        }


        Map<String, FunctionSet> aritySet = new HashMap<>();
        if (adfArities != null) {
            for (String arityString : adfArities) {
                if (aritySet.get(arityString) == null) {
                    int arity = Integer.parseInt(arityString);
                    FunctionSet arityFunctionSet = FunctionSet.reduceArity(functionSet, arity);
                    aritySet.put(arityString, arityFunctionSet);
                }
            }
        }


        ResultProducingProgram fittestTesting = null;


        while (currentTestingGeneration < testingGenerations && !terminateTraining(fittestTesting)) {
            population = new Population();
            ResultProducingProgram[] resultProducingPrograms = initializePopulation(functionSet, terminalSet, aritySet, adfArities, series, initialPopulationSize, maxInitDepth, maxSize, numberOfRegimes, returnType, growMethod);
            population.setResultPopulation(resultProducingPrograms);
            RegimeDetectionProgram[] regimeDetectionPrograms = null;
            if (numberOfRegimes > 1) {

                regimeDetectionPrograms = initializeRegimePopulation(regimeFunctionSet, terminalSet, aritySet, adfArities, series,
                        initialPopulationSize, maxInitDepth, maxSize, numberOfRegimes, growMethod, regimeSelection);
                //Now pair up both programs for initial pairs. Program sizes must be the same
                for (int i = 0; i < resultProducingPrograms.length; i++) {
                    resultProducingPrograms[i].setRegimeDetectionProgram(regimeDetectionPrograms[i]);
                }
            }


            for (int i = 0; i < resultProducingPrograms.length; i++) {
                ResultProducingProgram resultProducingProgram = resultProducingPrograms[i];
                FitnessEvaluation fitnessEvaluation = fitnessEvaluator.calculateProgramFitness(startTrainPos, endTrainPos, maxDepth,
                        resultProducingProgram, selectionStrategy.getDirection());
                resultProducingProgram.setFitness(fitnessEvaluation.getFitness());

            }

            generation = 0;
            currentTestingGeneration++;

            ResultProducingProgram fittestTraining = null;
            int stagnation = 0;
            while ((stagnationLimit==null || stagnation<stagnationLimit) && generation < trainingGenerations && !terminateTraining(fittestTraining)) {
                generation++;

                fittestTraining = doTraining(false, metricId, generation, currentTestingGeneration, startTrainPos, endTrainPos, null, true);
                //Run the fittest from training against the testing period
                FitnessEvaluation fitnessEvaluation;
                if (startTestPos!=null && endTestPos!=null) {
                    fitnessEvaluation = fitnessEvaluator.calculateProgramFitness(startTestPos, endTestPos, maxDepth, fittestTraining, selectionStrategy.getDirection());
                }else {
                    //otherwise, use training fitness for choosen best so far
                    fitnessEvaluation = fitnessEvaluator.calculateProgramFitness(startTrainPos, endTrainPos, maxDepth, fittestTraining, selectionStrategy.getDirection());
                }


                System.out.println("********************************************");
                fittestTesting = compareFittest(fittestTesting, fittestTraining);
                if (fittestTesting!=fittestTraining){
                    stagnation++;
                    System.out.println("Didn't find better program");
                }else{
                    stagnation=0;
                    printNewFittest("New Fittest RP after training ", fittestTesting, true, false);
                    printNewFittest("New Fittest Regime after training ", fittestTesting.getRegimeDetectionProgram(), true, false);
                    System.out.println("populationSize: " + population.getResultPopulation().size());
                    fitnessEvaluator.drawTestingSeries(0, fitnessEvaluation.getXyArray());
                    if (numberOfRegimes > 1 && drawRegimes) {
                        fitnessEvaluator.drawTestingRegimeSeries(fitnessEvaluation.getRegimeXyArray());

                    }
                }

            }
            if (stagnationLimit!=null && stagnation>=stagnationLimit){
                System.out.println("Aborted training after " + stagnation + " generations of stagnation");
            }

        }
       /* Testing phase in a loop here, then move on to prediction */


        System.out.println("Done with testing.");
        System.out.println("********************************************");
        int starPrediction = endTrainPos+1;
        if (endTestPos!=null){
            starPrediction = endTestPos+1;
        }
        Population result = predict(metricsService,
                metricId,
                testingGenerations + 1,
                startTrainPos,
                generation,
                starPrediction,
                end,
                fittestTesting,
                fittestTesting.getRegimeDetectionProgram());


        metricsService.endRun(metricId);

        return result;
    }

    private void collectGarbage() {    // do every X generations


        if (resultLibrary.getSize() > 0 || regimeLibrary.getSize() > 0) {
            Set<Integer> libaryInUse = new HashSet<>();
            Set<Integer> regimeLibaryInUse = new HashSet<>();
            for (ResultProducingProgram program : population.getResultPopulation()) {
                if (resultLibrary.getSize() > 0) {
                    List<Primitive> primitives = new ArrayList<Primitive>();
                    AbstractSelectionStrategy.addPrimitivesTyped(primitives, program.getRoot(), AatImpl.class);
                    for (Primitive primitive : primitives) {
                        Aat aat = (Aat) primitive;
                        libaryInUse.add(aat.getLibaryKey());
                        logger.trace("retaining " + aat.getLibaryKey());
                    }
                }
                if (regimeLibrary.getSize() > 0) {
                    List<Primitive> regimePrimitives = new ArrayList<Primitive>();
                    AbstractSelectionStrategy.addPrimitivesTyped(regimePrimitives, program.getRegimeDetectionProgram().getRoot(), AatImpl.class);
                    for (Primitive primitive : regimePrimitives) {
                        Aat aat = (Aat) primitive;
                        regimeLibaryInUse.add(aat.getLibaryKey());
                        logger.trace("retaining " + aat.getLibaryKey());
                    }
                }
            }

            resultLibrary.retainAll(libaryInUse);
            regimeLibrary.retainAll(regimeLibaryInUse);
        }


    }


    protected ResultProducingProgram doTraining(final boolean isPrediction, final Integer metricId, final int generation, final int currentTestingGeneration, int startTrainPos,
                                                int endTrainPos, Integer predictedRegime, boolean lastTrainingThisGeneration) {
        Date trainingStart = new Date();

        System.out.println("************* Training Generation " + currentTestingGeneration + "|" + generation + "************************");
        logger.info("Evolving generation " + currentTestingGeneration + "|" + generation);
        ResultProducingProgram fittestTraining = train(generation, startTrainPos, endTrainPos, predictedRegime, lastTrainingThisGeneration);


        //get metrics from population if this is the last round per generation
        Metrics metrics = null;
        Metrics regimeMetrics = null;
        if (lastTrainingThisGeneration) {
            metrics = this.population.getMetrics();
            regimeMetrics = this.population.getRegimeMetrics();

            metrics.setPopulationSize(population.getResultPopulation().size());
            metrics.setLibraryPopulationSize(fitnessEvaluator.getResultLibrary().getSize());
            metrics.setBestFitness(fittestTraining.getFitness());
            metrics.setFittestNodeCount(fittestTraining.getNodeCount());
            metrics.setFittestAdfNodeCount(fittestTraining.getTotalAdfNodeCount());
            metrics.setFittestDepth(fittestTraining.getDepth());


            regimeMetrics.setPopulationSize(population.getResultPopulation().size());
            regimeMetrics.setLibraryPopulationSize(fitnessEvaluator.getRegimeLibrary().getSize());
            if (fittestTraining.getRegimeDetectionProgram() != null) {
                regimeMetrics.setBestFitness(fittestTraining.getRegimeDetectionProgram().getFitness());
                regimeMetrics.setFittestNodeCount(fittestTraining.getRegimeDetectionProgram().getNodeCount());
                regimeMetrics.setFittestAdfNodeCount(fittestTraining.getRegimeDetectionProgram().getTotalAdfNodeCount());
                regimeMetrics.setFittestDepth(fittestTraining.getRegimeDetectionProgram().getDepth());
            }

        }
        System.out.println("populationSize: " + population.getResultPopulation().size());

        printNewFittest("Fittest:", fittestTraining, printTrainingProgram, false);
        System.out.println("Fittest Node Size:" + fittestTraining.getNodeCount());
        System.out.println("Fittest Depth:" + fittestTraining.getDepth());
        printNewFittest("Fittest Regime:", fittestTraining.getRegimeDetectionProgram(), printTrainingProgram, false);
        FitnessEvaluation trainingResult = fitnessEvaluator.evaluate(fittestTraining, startTrainPos, endTrainPos, maxDepth, selectionStrategy.getDirection());
        fitnessEvaluator.drawTrainingSeries(trainingResult.getXyArray());
        Date trainingEnd = new Date();

        if (lastTrainingThisGeneration) {
            metricsService.addTraining(isPrediction, metricId, currentTestingGeneration, generation, trainingStart, trainingEnd,
                    trainingResult, fitnessEvaluator.getTargetSeries(), metrics, regimeMetrics, fittestTraining, fittestTraining.getRegimeDetectionProgram());
        }
        if (numberOfRegimes > 1 && drawRegimes) {
            fitnessEvaluator.drawTrainingRegimeSeries(trainingResult.getRegimeXyArray());

        }
        return fittestTraining;
    }

    protected abstract boolean terminateTraining(ResultProducingProgram program);


    protected abstract Population predict(MetricsService metricsService, Integer metricId, int testingIteration, int startTrainPos, int generation, int startPos, int endPos, ResultProducingProgram fittestSoFar, RegimeDetectionProgram fittestRegimeDectionSoFar);


    private ResultProducingProgram train(int generation, int windowStart, int windowEnd, Integer predictedRegime, boolean lastTrainingThisGeneration) {


        Population nextGeneration = selectionStrategy.selectionNextGeneration(generation, trainingGenerations, growMethod, population, maxTotalNodes, windowStart, windowEnd, predictedRegime, lastTrainingThisGeneration);
        Population nextPoulation = buildNextGen(nextGeneration);
        this.population = nextPoulation;

        if (generation % gcFrequency == 0) {
            collectGarbage();

        }

        ResultProducingProgram winner = findFittest();
        return winner;

    }

    private ResultProducingProgram findFittest() {
        ResultProducingProgram resultProducingProgram = (ResultProducingProgram) AbstractProgram.findFittest(population.getResultPopulation(), selectionStrategy.getDirection());

        if (logger.isDebugEnabled()) {
            logger.debug("Best this round :" + resultProducingProgram.asLanguageString(maxDepth));
            logger.debug("Fitness :" + resultProducingProgram.getFitness());
        }

        return resultProducingProgram;
    }

    private Population buildNextGen(final Population seedPopulation) {

        Metrics regimeMetrics = new Metrics();
        Metrics metrics = new Metrics();

        metrics.setFitnessEvaluations(fitnessEvaluator.getAndResetFitnessEvaluations());
        metrics.setFitnessCalculations(fitnessEvaluator.getAndResetFitnessCalculations());

        Population population = new Population();
        AbstractProgram[] resultProducingPrograms = new ResultProducingProgram[seedPopulation.getRPLength()];
        buildNextGenPrograms(seedPopulation.getResultPopulation(), resultProducingPrograms, metrics);
        population.setResultPopulation((ResultProducingProgram[]) resultProducingPrograms);


        population.setMetrics(metrics);
        population.setRegimeMetrics(regimeMetrics);
        return population;

    }

    private void buildNextGenPrograms(List<? extends AbstractProgram> seedPopulation,
                                      AbstractProgram[] newPrograms,
                                      Metrics metrics) {

        boolean hasAdf = false;
        int invalidPopulationSize = 0;
        Integer totalNodeCount = 0;
        Integer totalAdfNodeCount = null;
        DescriptiveStatistics fitnessStats = new DescriptiveStatistics();
        DescriptiveStatistics nodeStats = new DescriptiveStatistics();
        DescriptiveStatistics depthStats = new DescriptiveStatistics();
        DescriptiveStatistics adfNodeStats = new DescriptiveStatistics();
        for (int i = 0; i < seedPopulation.size(); i++) {
            AbstractProgram program = seedPopulation.get(i);

            if (program.hasValidFitness(maxValidFitness)) {
                Double fitness = program.getFitness();
                if (logger.isTraceEnabled()) {
                    logger.trace("Fitness: " + fitness + " " + program.asLanguageString(100));
                }
                fitnessStats.addValue(fitness);
                totalNodeCount += program.getNodeCount();
                nodeStats.addValue(program.getNodeCount());
                depthStats.addValue(program.getDepth());
                if (program.getTotalAdfNodeCount() != null) {
                    adfNodeStats.addValue(program.getTotalAdfNodeCount());
                    if (totalAdfNodeCount == null) {
                        totalAdfNodeCount = 0;
                    }
                    totalAdfNodeCount += program.getTotalAdfNodeCount();
                    hasAdf = true;
                }
            } else {
                invalidPopulationSize++;
            }
            //    program.setId(i + 1); move this up to before fitness evaluiation, for dyfor purposes
            newPrograms[i] = program;
        }

        metrics.setInvalidPopulationSize(invalidPopulationSize);
        metrics.setMeanFitness(fitnessStats.getMean());
        metrics.setStddevFitness(fitnessStats.getStandardDeviation());
        metrics.setMedianFitness(fitnessStats.getPercentile(50));
        metrics.setVarianceFitness(fitnessStats.getVariance());
        metrics.setMeanNodeCount(nodeStats.getMean());
        metrics.setStddevNodeCount(nodeStats.getStandardDeviation());
        metrics.setMedianNodeCount(nodeStats.getPercentile(50));
        metrics.setVarianceNodeCount(nodeStats.getVariance());
        metrics.setTotalNodeCount(totalNodeCount);

        metrics.setMeanDepth(depthStats.getMean());
        metrics.setStddevDepth(depthStats.getStandardDeviation());
        metrics.setMedianDepth(depthStats.getPercentile(50));
        metrics.setVarianceDepth(nodeStats.getVariance());

        if (hasAdf) {
            metrics.setMeanAdfNodeCount(adfNodeStats.getMean());
            metrics.setTotalAdfNodeCount(totalAdfNodeCount);
            metrics.setStddevAdfNodeCount(adfNodeStats.getStandardDeviation());
            metrics.setMedianAdfNodeCount(adfNodeStats.getPercentile(50));
            metrics.setVarianceAdfNodeCount(adfNodeStats.getVariance());
        }

    }


    public static List<Adf> initializeAdf(FunctionSet functionSet, String[] adfArities, Map<String, FunctionSet> aritySet, List<String> series, int maxDepth, int regimes, GrowMethod growMethod) {
        List<Adf> adfs = new ArrayList<Adf>();

        int adfCounter = 0;
        for (String arityString : adfArities) {
            int symbolicArity = Integer.parseInt(arityString);
            //reduce function set by arity - should save this off as it is resuable
            AdfImpl newAdf = new AdfImpl();
            newAdf.setArity(symbolicArity);
            adfs.add(newAdf);
            newAdf.initializeRoots(regimes);
            newAdf.setName("adf" + adfCounter++);
            Terminal[] symbolicParameters = new Terminal[symbolicArity];
            for (int i = 0; i < symbolicArity; i++) {
                GP_TYPES paramType;      // TODO, seem to need to keep the order the same so params are constant
                if (i % 2 == 0) {
                    paramType = GP_TYPES.BOOLEAN;
                } else {
                    paramType = GP_TYPES.NUMBER;
                }
                symbolicParameters[i] = AbstractSymbolicParameter.buildParmeter("arg" + i, paramType);
            }
            newAdf.setSymbolicParameters(new TerminalSet(symbolicParameters));
            //todo this should be a generic combine function, also make these sets immutable
            //this just serves the purpose of setting the return type of the adf.
            //perhaps there is a better way

            TerminalSet combinedTerminals = AbstractTerminal.addAll(symbolicParameters);
            newAdf.setFunctionSet(functionSet);
            newAdf.setTerminalSet(combinedTerminals);
            int regime = 0;
            GP_TYPES returnType = null;
            while (regime < regimes) {
                FunctionSet arityFunctionSet = aritySet.get(arityString);
                if (arityFunctionSet.getLength() == 0) {
                    System.err.println("FATAL ERROR: There are no available functions specified in the program parameters of arity " + arityString);
                    System.exit(1);
                }
                Primitive root = ResultProducingProgram.generatePrimitive(returnType, functionSet, arityFunctionSet, combinedTerminals, series, maxDepth, maxDepth, growMethod, true, null, true);
                if (regime == 0) {
                    returnType = root.getReturnType();
                }
                newAdf.setRoot(root, regime);
                regime++;


            }
        }


        return adfs;
    }

    public static RegimeDetectionProgram[] initializeRegimePopulation(FunctionSet functionSet, TerminalSet terminalSet, Map<String, FunctionSet> aritySet, String[] adfArities,
                                                                      List<String> series, int populationSize,
                                                                      int maxDepth, int maxSize, int regimes, GrowMethod growMethod, boolean regimeSelection) {
        RegimeDetectionProgram[] population = new RegimeDetectionProgram[populationSize];
        logger.info("Generating initial population");
        //initiaize population


        GrowMethod currentGrowMethod = growMethod;
        int added = 0;
        while (added < populationSize) {
            if (growMethod == GrowMethod.HALF_HALF) {
                if (added % 2 == 0) {
                    currentGrowMethod = GrowMethod.GROW;
                } else {
                    currentGrowMethod = GrowMethod.FULL;
                }
            }
            RegimeDetectionProgram program = RegimeDetectionProgram.generateProgram(functionSet, terminalSet, aritySet, adfArities, series, maxDepth,
                     currentGrowMethod, regimes, regimeSelection);
            if (logger.isTraceEnabled()) {
                System.out.println("*********************************************");
                System.out.println(program.asLanguageString(100));
            }
            List<Primitive> primitivesTmp = new ArrayList<Primitive>();


            int check1Size = AbstractSelectionStrategy.addPrimitives(primitivesTmp, program.getRoot(), null);
            if (AbstractSelectionStrategy.checkSize(maxDepth + 1, maxSize, check1Size, primitivesTmp.size())) { //need to add one to max depth for initial binary number
                program.setId(added + 1);
                population[added] = program;
                added++;
            } else {
                logger.debug("hit size  limit generating program");
            }


        }
        return population;
    }

    public static ResultProducingProgram[] initializePopulation(FunctionSet functionSet, TerminalSet terminalSet, Map<String, FunctionSet> aritySet, String[] adfArities,
                                                                List<String> series, int populationSize, int maxDepth, int maxSize, int regimes, GP_TYPES returnType, GrowMethod growMethod) {
        ResultProducingProgram[] population = new ResultProducingProgram[populationSize];
        logger.info("Generating initial population");
        //initiaize population


        GrowMethod currentGrowMethod = growMethod;
        int added = 0;
        while (added < populationSize) {
            if (growMethod == GrowMethod.HALF_HALF) {
                if (added % 2 == 0) {
                    currentGrowMethod = GrowMethod.GROW;
                } else {
                    currentGrowMethod = GrowMethod.FULL;
                }
            }

            ResultProducingProgram program = ResultProducingProgram.generateProgram(functionSet, terminalSet, aritySet, adfArities, series, maxDepth, regimes, currentGrowMethod, returnType);
            List<Primitive> primitivesTmp = new ArrayList<Primitive>();


            int checkDepth = AbstractSelectionStrategy.addPrimitives(primitivesTmp, program.getRoot(), null);
            if (AbstractSelectionStrategy.checkSize(maxDepth, maxSize, checkDepth, primitivesTmp.size())) {
                program.setId(added + 1);
                population[added] = program;
                added++;
            } else {
                logger.debug("hit size  limit generating program");
            }


        }


        return population;
    }


    protected void printNewFittest(String message, AbstractProgram program, boolean printProgram, boolean simplify) {
        if (program != null) {

            System.out.println(message + ": " + program.getFitness());
            if (printProgram) {

                if (simplify) {
                    System.out.println("Simplified :" + program.simplify().asLanguageString(0, maxDepth));
                } else {
                    System.out.println("Program :" + program.asLanguageString(maxDepth));
                }
                if (program.getAdfs() != null && program.getAdfs().size() > 0) {
                    for (int adfCount = 0; adfCount < program.getAdfs().size(); adfCount++) {
                        System.out.println("ADF" + adfCount);
                        Adf adf = program.getAdfs().get(adfCount);
                        for (int regime = 0; regime < adf.getNumberOfRoots(); regime++) {
                            System.out.println("Regime[" + regime + "]: " + adf.getRoot(regime).asLanguageString(0, maxDepth));
                        }
                    }

                }
            }

        }

    }

    protected RegimeDetectionProgram compareRegimeDetection(RegimeDetectionProgram fittestRegimeDectionSoFar, RegimeDetectionProgram fittestRegimeDetectionThisround) {

        if (numberOfRegimes > 1) {
            if (fittestRegimeDectionSoFar == null) {
                fittestRegimeDectionSoFar = fittestRegimeDetectionThisround;

            } else {
                Program fittest = AbstractFitnessEvaluator.fittest(fittestRegimeDetectionThisround, fittestRegimeDectionSoFar, selectionStrategy.getDirection());
                if (fittest != null && fittest == fittestRegimeDetectionThisround) {
                    fittestRegimeDectionSoFar = fittestRegimeDetectionThisround;

                }
            }
        }
        return fittestRegimeDectionSoFar;
    }

    protected ResultProducingProgram compareFittest(ResultProducingProgram fittestSoFar, ResultProducingProgram fittestThisround) {
        if (fittestSoFar == null) {
            fittestSoFar = fittestThisround;
        } else {

            Program fittest = AbstractFitnessEvaluator.fittest(fittestThisround, fittestSoFar, selectionStrategy.getDirection());
            if (fittest != null && fittest == fittestThisround) {
                fittestSoFar = fittestThisround;
            }
        }
        return fittestSoFar;
    }

    protected int getPositionParameter(String positionParam) {
        int pos = 0;
        if (positionParam.endsWith("%")) {
            pos = fitnessEvaluator.getTargetSeries().getLength() * Integer.parseInt(positionParam.substring(0, positionParam.length() - 1)) / 100;

        } else if (positionParam.contains("/")) {
            DateXYSeries dateXYSeries = (DateXYSeries) fitnessEvaluator.getTargetSeries();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            LocalDate localDate = LocalDate.parse(positionParam, formatter);
            for (int i = fitnessEvaluator.getSeriesStart(); i <= fitnessEvaluator.getSeriesEnd(); i++) {
                if (dateXYSeries.getX(i).isAfter(localDate)) {
                    pos = i;
                    break;
                }
            }

        } else {
            pos = Integer.parseInt(positionParam);
        }
        return pos;
    }


}


