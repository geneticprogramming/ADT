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

import com.infoblazer.gp.application.fitness.AbstractFitnessEvaluator;
import com.infoblazer.gp.application.fitness.FitnessEvaluator;
import com.infoblazer.gp.evolution.primitives.*;
import com.infoblazer.gp.evolution.primitives.functions.Adf;
import com.infoblazer.gp.evolution.primitives.functions.AdfImpl;
import com.infoblazer.gp.evolution.primitives.functions.Function;
import com.infoblazer.gp.evolution.primitives.terminals.RandomInteger;
import com.infoblazer.gp.evolution.primitives.terminals.TerminalFalse;
import com.infoblazer.gp.evolution.primitives.terminals.TerminalTrue;
import com.infoblazer.gp.evolution.primitives.terminals.Variable;
import com.infoblazer.gp.evolution.selectionstrategy.AbstractSelectionStrategy;
import com.infoblazer.gp.evolution.selectionstrategy.SelectionStrategy;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by David on 5/24/2015.
 */
public abstract class AbstractProgram implements Program {

    private final static Logger logger = Logger.getLogger(AbstractProgram.class.getName());
    protected Integer id;
    protected Primitive root;


    private List<Adf> adfs;
    private Double fitness;
    private Double probability;
    private Integer nodeCount;
    private Integer totalAdfNodeCount;
    private Integer maxAdfNodeCount;
    private Integer maxAdfDepth;
    private Integer depth;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Primitive getRoot() {
        return root;
    }

    public void setRoot(Primitive root) {
        this.root = root;
    }

    public Double getFitness() {
        return fitness;
    }

    public void setFitness(Double fitness) {
        this.fitness = fitness;
    }

    public List<Adf> getAdfs() {
        return adfs;
    }

    public void setAdfs(List<Adf> adfs) {
        this.adfs = adfs;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        if (probability.equals(Double.NaN) || probability.equals(Double.POSITIVE_INFINITY) ||
                probability.equals(Double.NEGATIVE_INFINITY)) {
            this.probability = 0d;
        } else {
            this.probability = probability;
        }
    }

    public static Primitive generatePrimitive(GP_TYPES returnType, FunctionSet functionSet, FunctionSet arityFunctionSet, TerminalSet terminalSet, List<String> series,
                                              Integer remainingDepth, Integer maxDepth, GrowMethod growMethod, boolean functionsOnly,
                                              Function forceFunction, boolean noVariables) {
        Primitive expr = null;
        Random random = new Random();
        FunctionSet actualFunctionSet = functionSet;
        if (arityFunctionSet != null) {
            actualFunctionSet = arityFunctionSet;
        }

        double prob = random.nextDouble();
        double terminalProb = (terminalSet.getLength() / (Double.valueOf(terminalSet.getLength() + actualFunctionSet.getLength())));
        boolean selectFunction = chooseFunction(forceFunction, functionsOnly, remainingDepth, maxDepth, growMethod, prob, terminalProb);
        if (!selectFunction) {
            expr = chooseRandom(terminalSet, series, returnType, noVariables ? Variable.class : null);
        } else {
            Function function = null;
            if (forceFunction == null) {
                Primitive randomPrimitive = chooseRandom(actualFunctionSet, series, returnType, null);
                if (randomPrimitive instanceof Function) {
                    function = (Function) randomPrimitive;
                } else {
                    Primitive returnTerminal = randomPrimitive;//couldn't find function
                    return returnTerminal;
                }

            } else {
                function = forceFunction;
            }
            logger.trace("chose function " + function);
            if (function instanceof Adf) {
                AdfImpl adf = (AdfImpl) function;
                //This is generating the call to the adf in the RPB.
                // Need to generate new adfs for each program
                for (int i = 0; i < function.getArity(); i++) {
                    //Function root = (Function) adf.getRoot(0);
                    //GP_TYPES paramType = root.getParameterReturnTypes()[i];
                    GP_TYPES paramType = adf.getSymbolicParameters().getItems()[i].getReturnType();
                    Primitive primitive = generatePrimitive(paramType, functionSet, null, terminalSet, series,
                            remainingDepth == null ? null : remainingDepth - 1, maxDepth, growMethod, false, null, true);
                    function.getParameters()[i] = primitive; //These are what values are passed into the adf. The adf  holds the implementation
                }
            } else {


                for (int i = 0; i < function.getParameters().length; i++) {
                    GP_TYPES paramType = function.getParameterReturnTypes()[i];
                    Primitive primitive = generatePrimitive(paramType, functionSet, null, terminalSet, series,
                            remainingDepth == null ? null : remainingDepth - 1, maxDepth, growMethod, false, null, noVariables);
                    function.getParameters()[i] = primitive;
                }
            }
            expr = function;


        }
        if (expr == null) {
            logger.warn("generated null expression. Check the sufficiency of the primitive set");
        }
        return expr;
    }
    //TODO need unit test
    public static boolean chooseFunction(Function forceFunction,
                                         boolean functionsOnly,
                                         Integer remainingDepth,
                                         int maxDepth,
                                         GrowMethod growMethod,
                                         double rndProb,
                                         double terminalProb) {
        boolean chooseFunction;
        //If function is forced, choose function
        if (forceFunction != null || functionsOnly) {
            chooseFunction = true;
        }
        //If this is root, choose function
        else if (remainingDepth != null && remainingDepth >= (maxDepth)) {
            chooseFunction = true;

        } else if (remainingDepth != null &&
                (remainingDepth.equals(1))) {
            chooseFunction = false;
        } else if (growMethod == GrowMethod.GROW && rndProb < terminalProb) {
            chooseFunction = false;
        }else{
            chooseFunction = true;
        }
        return chooseFunction;
    }

    private static Adf getAdfByName(List<Adf> adfList, String name) {
        Adf result = null;
        for (Adf adf : adfList) {
            if (adf.getName().equals(name)) {
                result = adf;
                break;
            }
        }

        return result;
    }

    //TODO this trial and error is not very optimal?

    /**
     * @param primitiveSet
     * @param series
     * @param returnType
     * @param excludeClass optional class that won't be selected. This could be exanded to a lssdsit
     * @return
     */
    private static Primitive chooseRandom(PrimitiveSet primitiveSet, List<String> series, GP_TYPES returnType, Class excludeClass) {
        Random random = new Random();
        Primitive primitive = null;
        int tries = 0;
        int maxAttempts = primitiveSet.getItems().length * 2;
        while (tries++ < maxAttempts && (primitive == null
                || (excludeClass != null && primitive.getClass().getName().equals(excludeClass.getClass().getName())))) {
            int selected = random.nextInt(primitiveSet.getLength());
            Primitive stereoType = primitiveSet.getItems()[selected];
            if (returnType == null || returnType == stereoType.getReturnType()) {
                primitive = stereoType.newInstance(series);
            }
        }
        if (primitive == null) {

            if (returnType == GP_TYPES.BOOLEAN) {
                //Note: This is appropriate to avoid having to include constants in terminal set anyway
                logger.debug("Didn't find primitive: " + returnType + ". Generating Boolean");
                if (random.nextInt(2) == 0) {
                    primitive = new TerminalFalse();
                } else {
                    primitive = new TerminalTrue();
                }

            } else {
                logger.debug("Didn't find primitive: " + returnType + ". Generating Constant");
                if (random.nextInt(2) == 0) {
                    primitive = new RandomInteger(1);
                } else {
                    primitive = new RandomInteger(0);
                }
            }

        }


        return primitive;
    }

    public boolean replace(Primitive oldprimitive, Primitive newprimitive, int level, Integer maxLevel) {
        boolean found = false;
        if (this.root == oldprimitive) {

            this.root = newprimitive;
            found = true;
        } else {
            found = replace(this.root, oldprimitive, newprimitive, level + 1, maxLevel);
        }

        return found;
    }

    public boolean replaceAdf(int regime, Primitive oldprimitive, Primitive newprimitive, Integer maxLevel) {
        boolean found = false;
        for (Adf adf : this.getAdfs()) {
            if (!found) {
                found = replace(adf.getRoot(regime), oldprimitive, newprimitive, 0, maxLevel);
                if (found) {
                    break; //not sure which adf this is in, but it is only in one
                }
            }

        }


        return found;
    }

    public static boolean replace(Primitive root, final Primitive oldPrimitive, final Primitive newPrimitive, int level, Integer maxLevel) {

        boolean found = false;
        if (root == oldPrimitive) {//terminal only
            //Primitive replacement = (Primitive) GpUtils.getKyroInstance().copy(newPrimitive);
            root = newPrimitive;
            found = true;
        } else if (root instanceof Function) {
            Function function = (Function) root;
            for (int i = 0; i < function.getParameters().length; i++) {
                if (!found) {
                    if (function.getParameters()[i] == oldPrimitive) {
                        //          Primitive replacement = (Primitive) GpUtils.getKyroInstance().copy(newPrimitive);
                        function.getParameters()[i] = newPrimitive;
                        found = true;
                        break;
                    } else {
                        found = replace(function.getParameters()[i], oldPrimitive, newPrimitive, level + 1, maxLevel);
                    }
                }
            }

        }
        return found;

    }


    public static AbstractProgram findFittest(List<? extends AbstractProgram> population, SelectionStrategy.Direction direction) {


        AbstractProgram bestSoFar = null;
        for (AbstractProgram program : population) {
            if (program != null && program.getFitness() == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("attempting to find fittest when fitness is not calculated");
                }
            }
            if (bestSoFar == null) {
                bestSoFar = program;
            } else {
                Boolean fitter = AbstractFitnessEvaluator.isFitter(program, bestSoFar, direction);
                if (fitter != null && fitter) {
                    bestSoFar = program;
                }

            }
        }
        if (bestSoFar != null && bestSoFar.getFitness() != null) {
            return bestSoFar;
        } else {
            return null;
        }
    }

    public static AbstractProgram findWorst(List<AbstractProgram> population, SelectionStrategy.Direction direction, FitnessEvaluator fitnessEvaluator
    ) {

        return findFittest(population, direction.reverse());

    }

    public String asLanguageString(int maxDepth) {


        StringBuilder sb = new StringBuilder();
        if (root != null) {

            sb.append("main->").append(root.asLanguageString(0, maxDepth));

            if (adfs != null) {
                for (Adf adf : adfs) {
                    sb.append("\n").append(adf.getName()).append("->").append(adf.getRoot(0).asLanguageString(0, maxDepth));
                }
            }

        }

        return sb.toString();

    }

    public Primitive simplify() {
        Primitive simplified = getRoot().simplify();
        return simplified;

    }

    public ResultProducingProgram simplifyResultProducingProgram() {
        ResultProducingProgram resultProducingProgram = new ResultProducingProgram();
        Primitive simplified = getRoot().simplify();
        resultProducingProgram.setRoot(simplified);

        if (adfs != null) {
            resultProducingProgram.setAdfs(new ArrayList<Adf>());
            for (Adf adf : adfs) {
                Adf adf1 = new AdfImpl();
                for (int regime = 0; regime < adf.getNumberOfRoots(); regime++) {
                    Primitive simpleAdf = adf.getRoot(regime).simplify();
                    adf1.setRoot(simpleAdf, regime);
                }
                resultProducingProgram.getAdfs().add(adf1);
            }
        }
        return resultProducingProgram;

    }

    public Integer getNodeCount() {
        return nodeCount;
    }


    public Integer getDepth() {
        return depth;
    }


    public Integer getTotalAdfNodeCount() {
        return totalAdfNodeCount;
    }


    public Integer getMaxAdfNodeCount() {
        return maxAdfNodeCount;
    }


    public Integer getMaxAdfDepth() {
        return maxAdfDepth;
    }

    public void setMaxAdfDepth(Integer maxAdfDepth) {
        this.maxAdfDepth = maxAdfDepth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }



    /**
     * fill in depth and node count
     */
    public void calculateMetrics() {
        List<Primitive> primitivesTmp = new ArrayList<Primitive>();
        int check1Depth = AbstractSelectionStrategy.addPrimitives(primitivesTmp, root, null);
        depth = check1Depth;
        nodeCount = primitivesTmp.size();

    }

    public void calculateAdfMetrics(int regimes) {

        Integer totalNodeSize = null;
        Integer maxNodeSize = null;
        Integer maxDepth = null;

        if (adfs != null) {
            totalNodeSize = 0;
            maxNodeSize = 0;
            maxDepth = 0;
            for (int regime = 0; regime < regimes; regime++)
                for (Adf adf : adfs) {
                    List<Primitive> primitivesTmp = new ArrayList<Primitive>();
                    int depth = AbstractSelectionStrategy.addPrimitives(primitivesTmp, adf.getRoot(regime), null);
                    Integer nodeSize = primitivesTmp.size();
                    adf.setDepth(depth);
                    if (depth > maxDepth) {
                        maxDepth = depth;
                    }
                    if (nodeSize > maxNodeSize) {
                        maxNodeSize = nodeSize;
                    }
                    adf.setNodeCount(nodeSize);
                    totalNodeSize += nodeSize;

                }
        }

        totalAdfNodeCount = totalNodeSize;
        maxAdfDepth = maxDepth;
        maxAdfNodeCount = maxNodeSize;
    }

    public boolean hasValidFitness(Double maxOverride) {
        boolean result = fitness != null &&
                !Double.isNaN(fitness)
                && !Double.isInfinite(fitness)
                && !fitness.equals(Double.MAX_VALUE)
                && (maxOverride == null || fitness <= maxOverride);
        return result;

    }
}
