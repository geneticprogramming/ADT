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

import com.infoblazer.gp.evolution.library.Library;
import com.infoblazer.gp.evolution.library.RegimeLibrary;
import com.infoblazer.gp.evolution.library.ResultLibrary;
import com.infoblazer.gp.evolution.model.AbstractProgram;
import com.infoblazer.gp.evolution.model.Pair;
import com.infoblazer.gp.evolution.model.RegimeDetectionProgram;
import com.infoblazer.gp.evolution.model.ResultProducingProgram;
import com.infoblazer.gp.evolution.primitives.Primitive;
import com.infoblazer.gp.evolution.primitives.functions.Aat;
import com.infoblazer.gp.evolution.primitives.functions.Adf;
import com.infoblazer.gp.evolution.primitives.functions.BinaryNumber;
import com.infoblazer.gp.evolution.utils.GpUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by David on 8/23/2015.
 */
@Component
public class CrossOver {
    private final static Logger logger = Logger.getLogger(CrossOver.class.getName());
    @Autowired
    private ResultLibrary resultLibrary;
    @Autowired
    private RegimeLibrary regimeLibrary;
    @Value("${freezeRegimes:#{false}}")
    protected Boolean freezeRegimes;
    private Random random = new Random();

    public Pair<AbstractProgram> doCrossOver(final ResultProducingProgram parent1, final ResultProducingProgram parent2, final int maxDepth,
                                             final int maxSize, SelectionStrategy.Direction direction, int regimes,
                                             Integer predictedRegime) {

        //find crossover point.
        //for now, navigate and determine node count
        Pair<AbstractProgram> result = null;
        Pair<AbstractProgram> crossoverResult = null;
        boolean isRegimeCrossover = false;
        AbstractProgram crossoverCopy1 = null;
        AbstractProgram crossoverCopy2 = null;
        int programGroup = random.nextInt(2); // 0 = RP, 1 = regime

        if (regimes==1 || programGroup==0) {
            programGroup= 0;
             crossoverCopy1 = (AbstractProgram) GpUtils.getKyroInstance().copy(parent1);
            crossoverCopy2 = (AbstractProgram) GpUtils.getKyroInstance().copy(parent2);
        }else{
            programGroup= 1;
            isRegimeCrossover = true;
            crossoverCopy1 = (AbstractProgram) GpUtils.getKyroInstance().copy(parent1.getRegimeDetectionProgram());
            crossoverCopy2 = (AbstractProgram) GpUtils.getKyroInstance().copy(parent2.getRegimeDetectionProgram());
        }


        int selectedBranch = random.nextInt(2);

        if (selectedBranch==0 || crossoverCopy1.getAdfs()==null) {
            selectedBranch= 0;
            crossoverResult = crossoverRPB(crossoverCopy1, crossoverCopy2,  maxDepth, maxSize, direction, regimes, isRegimeCrossover,predictedRegime);
        } else {
            selectedBranch= 1;
            crossoverResult = crossoverADF(crossoverCopy1, crossoverCopy2, maxDepth, regimes, predictedRegime);
        }
        if (programGroup==0) {

            result = new Pair(crossoverResult.getProgram1(),crossoverResult.getProgram2() );

        }else{
            ResultProducingProgram Copy1 = (ResultProducingProgram) GpUtils.getKyroInstance().copy(parent1);
            ResultProducingProgram Copy2 = (ResultProducingProgram) GpUtils.getKyroInstance().copy(parent2);
            Copy1.setRegimeDetectionProgram((RegimeDetectionProgram) crossoverResult.getProgram1());
            Copy2.setRegimeDetectionProgram((RegimeDetectionProgram) crossoverResult.getProgram2());
            result = new Pair(Copy1,Copy2 );

        }





        return result;
    }

    private Pair<AbstractProgram> crossoverRPB(AbstractProgram copy1, AbstractProgram copy2,
                                               int maxDepth, int maxSize, SelectionStrategy.Direction direction,
                                               int regimes, boolean isRegimeCrossover,Integer predictedRegime) {
        Pair<AbstractProgram> result = null;

        List<Primitive> primitives1 = new ArrayList<>();
        AbstractSelectionStrategy.addPrimitives(primitives1, copy1.getRoot(), null);
        List<Primitive> primitives2 = new ArrayList<>();
        AbstractSelectionStrategy.addPrimitives(primitives2, copy2.getRoot(), null);
        logger.trace("Evolving RPB");
        //Result producting branch

        Primitive crossoverPrimitive1 = null;
        Primitive crossoverPrimitive2 = null;

        boolean oneTreeCheck = false;
        int onetreeCount = 0;

        while ((!oneTreeCheck && onetreeCount < AbstractSelectionStrategy.CROSSOVER_ATTEMPTS)
                || (crossoverPrimitive1 == null || crossoverPrimitive1 instanceof BinaryNumber)) {
            int crossoverPoint1 = random.nextInt(primitives1.size());
            crossoverPrimitive1 = primitives1.get(crossoverPoint1); //noADF

            crossoverPrimitive2 = AbstractSelectionStrategy.getStrongType(primitives2, crossoverPrimitive1.getReturnType()); //this may be nukk
            if (crossoverPrimitive1 == copy1.getRoot() && crossoverPrimitive2 == crossoverPrimitive2 && crossoverPrimitive2 == copy2.getRoot()) {
                onetreeCount++;
                logger.debug("Avoiding two one tree crossovers: " + onetreeCount);

            } else {
                oneTreeCheck = true;
            }
        }


        if (crossoverPrimitive1 instanceof Aat && crossoverPrimitive2 instanceof Aat) { //if two aats, can crossover within

            crossoverAat((Aat) crossoverPrimitive1, (Aat) crossoverPrimitive2, regimes, maxDepth, maxSize, direction, isRegimeCrossover, predictedRegime);

        } else { //one can still be aat, just straight crossover of function.
            if (crossoverPrimitive2 != null) {  //no regime implementations for RPB
                boolean found = copy1.replace(crossoverPrimitive1, crossoverPrimitive2, 0, maxDepth);
                if (!found) {
                    logger.warn("Didn't find copy1 crossover");
                }
                found = copy2.replace(crossoverPrimitive2, crossoverPrimitive1, 0, maxDepth);

                if (!found) {
                    logger.warn("Didn't find copy2 crossover");
                }
                copy1.setFitness(null);
                copy2.setFitness(null);



            } else {
                copy1.setFitness(null);
                copy2.setFitness(null);
                if (logger.isDebugEnabled()) {
                    logger.debug("Didn't find crossover. Using original");
                }
            }

        }

        result = new Pair(copy1, copy2);


        return result;
    }

    /**
     * to crossover two aats, we instead cross over the functions referenced in the libary
     *
     * @param parent1
     * @param parent2
     * @param regimes
     * @param maxDepth
     * @param direction
     * @param isRegimeCrossover
     * @
     */
    private void crossoverAat(final Aat parent1, final Aat parent2, final int regimes, final int maxDepth, final int maxSize, SelectionStrategy.Direction direction,
                              boolean isRegimeCrossover,Integer predictedRegime) {
        //find crossover point.
        //for now, navigate and determine node count
        int effectiveRegimes = regimes;
        if (isRegimeCrossover) {
            effectiveRegimes = 1;
        }
        Library library;
        if (isRegimeCrossover) {
            library = regimeLibrary;
        } else {
            library = resultLibrary;
        }

        for (int regime = 0; regime < effectiveRegimes; regime++) {
            if (!freezeRegimes || predictedRegime==null || regime==predictedRegime) {
                final Primitive targetPrimitive1 = library.getPrimitiveById(parent1.getLibaryKey())[regime];
                Primitive copy1 = (Primitive) GpUtils.getKyroInstance().copy(targetPrimitive1);
                final Primitive targetPrimitive2 = library.getPrimitiveById(parent2.getLibaryKey())[regime];
                Primitive copy2 = (Primitive) GpUtils.getKyroInstance().copy(targetPrimitive2);

                List<Primitive> primitives1 = new ArrayList<Primitive>();
                AbstractSelectionStrategy.addPrimitives(primitives1, copy1, null);

                //need to favor functions here, but use uniform selection of crossover for now
                int randomizer = primitives1.size();
                int crossoverPoint1 = 0; //shouldnt be
                if (randomizer > 0) {
                    crossoverPoint1 = random.nextInt(randomizer);
                }
                //There are regime dependent implementiaotn of AAT

                logger.trace("Evolving RPB");
                //Result producting branch
                Primitive crossoverPrimitive1 = primitives1.get(crossoverPoint1); //noADF
                List<Primitive> primitives2 = new ArrayList<Primitive>();
                AbstractSelectionStrategy.addPrimitives(primitives2, copy2, null);
                //need to favor functions here, but use uniform selection of crossover for now

                Primitive crossoverPrimitive2 = AbstractSelectionStrategy.getStrongType(primitives2, crossoverPrimitive1.getReturnType());


                if (crossoverPrimitive2 != null) {

                    boolean modified = AbstractProgram.replace(copy1, crossoverPrimitive1, crossoverPrimitive2, 0, maxDepth);

                    if (modified) {
                        parent1.setModified(true);
                    }

                    modified = AbstractProgram.replace(copy2, crossoverPrimitive2, crossoverPrimitive1, 0, maxDepth);

                    if (modified) {
                        parent2.setModified(true);
                    }
                    //now determine max depths
                    List<Primitive> primitivesTmp = new ArrayList<Primitive>();
                    int depth1 = AbstractSelectionStrategy.addPrimitives(primitivesTmp, copy1, null);


                    if (AbstractSelectionStrategy.checkSize(maxDepth, maxSize, depth1, primitivesTmp.size())) {
                        library.setPrimitive(regime, copy1, copy1.getId());
                    } else {
                        logger.debug("Skipping crossver1, too large:");
                    }
                 primitivesTmp = new ArrayList<Primitive>();
                    int depth2 = AbstractSelectionStrategy.addPrimitives(primitivesTmp, copy2, null);
                    if (AbstractSelectionStrategy.checkSize(maxDepth, maxSize, depth2, primitivesTmp.size())) {
                        library.setPrimitive(regime, copy2, copy2.getId());
                    } else {
                        logger.debug("Skipping crossver2, too large:");
                    }
                } else {
                    logger.warn("Didn't find crossover. Keeping originals");
                }
            }
        }

    }


    private Pair<AbstractProgram> crossoverADF(AbstractProgram copy1, AbstractProgram copy2,
                                               int maxDepth,  int regimes,
            Integer predictedRegime) {
        //Add these into a seperate map by arity

        logger.trace("Evolving ADFs");
        Pair<AbstractProgram> result = null;

        Map<Integer, List<Primitive>[]> adfMap1 = new HashMap<>();
        for (Adf adf : copy1.getAdfs()) {
            List<Primitive>[] adfList = adfMap1.get(adf.getArity());
            if (adfList == null) {
                adfList = new List[regimes];
                adfMap1.put(adf.getArity(), adfList);
            }
            for (int regime = 0; regime < regimes; regime++) {
                if (adfList[regime] == null) {
                    adfList[regime] = new ArrayList<Primitive>();
                }
                if (!freezeRegimes || predictedRegime==null || regime==predictedRegime) {
                    AbstractSelectionStrategy.addPrimitives(adfList[regime], adf.getRoot(regime), null);
                }

            }
        }

        Map<Integer, List<Primitive>[]> adfMap2 = new HashMap<>();
        for (Adf adf : copy2.getAdfs()) {
            List<Primitive>[] adfList = adfMap2.get(adf.getArity());
            if (adfList == null) {
                adfList = new List[regimes];
                adfMap2.put(adf.getArity(), adfList);
            }
            for (int regime = 0; regime < regimes; regime++) {
                if (adfList[regime] == null) {
                    adfList[regime] = new ArrayList<Primitive>();
                }
                if (!freezeRegimes || predictedRegime==null || regime==predictedRegime) {
                    AbstractSelectionStrategy.addPrimitives(adfList[regime], adf.getRoot(regime), null);
                }
            }
        }

        for (int regime = 0; regime < regimes; regime++) {

            if (!freezeRegimes || predictedRegime==null || regime==predictedRegime) {
                boolean foundMatch =false;
                int attempts = 0;
                while (!foundMatch && attempts++<10) {
                    AdfCrossoverSelect adfCrossoverSelect = findAdfCrossover(adfMap1, regime);
                    Primitive crossoverPrimitive1 = adfCrossoverSelect.primitive;
                    Primitive crossoverPrimitive2 = AbstractSelectionStrategy.getStrongType(adfMap2.get(adfCrossoverSelect.arity)[regime], crossoverPrimitive1.getReturnType());
                    if (crossoverPrimitive2 == null) {
                        logger.trace("Didn't find crossover with ADF. Trying again");
                    } else {
                        foundMatch = true;
                        boolean found = copy1.replaceAdf(regime, crossoverPrimitive1, crossoverPrimitive2, maxDepth);
                        if (!found) {
                            logger.warn("Didn't find ADF1 crossover");
                        }
                        found = copy2.replaceAdf(regime, crossoverPrimitive2, crossoverPrimitive1, maxDepth);
                        if (!found) {
                            logger.warn("Didn't find ADF2 crossover");
                        }
                    }
                }
                if (!foundMatch) {
                    logger.warn("Didn't find crossover with ADF. Using original");
                }
            }

        }
        copy1.setFitness(null);
        copy2.setFitness(null);


        result = new Pair(copy1, copy2);


        return result;
    }

    private class AdfCrossoverSelect {
        int arity;
        Primitive primitive;
    }

    /**
     * randomly choose a node in the adf set. keep track of the arity of the enclosing adf.
     * the same arity set must be used for the matching crossover node.
     *
     * @param adfMap
     * @param regime
     * @return
     */
    private AdfCrossoverSelect findAdfCrossover(Map<Integer, List<Primitive>[]> adfMap, int regime) {
        AdfCrossoverSelect adfCrossoverSelect = new AdfCrossoverSelect();
        int totalRegimeSize = findAdfRegimeSize(adfMap, regime);
        int selectionPoint = random.nextInt(totalRegimeSize);

        int totalSize = 0;
        for (Map.Entry<Integer, List<Primitive>[]> entry : adfMap.entrySet()) {
            int listlength = entry.getValue()[regime].size();
            if (selectionPoint < totalSize + listlength) {
                adfCrossoverSelect.primitive = entry.getValue()[regime].get(selectionPoint - totalSize); //noADF
                adfCrossoverSelect.arity = entry.getKey();
                break;
            }
            totalSize += listlength;

        }
        return adfCrossoverSelect;
    }


    private int findAdfRegimeSize(Map<Integer, List<Primitive>[]> adfMap, int regime) {
        int totalSize = 0;
        for (List<Primitive>[] list : adfMap.values()) {
            totalSize += list[regime].size();

        }
        return totalSize;
    }


}
