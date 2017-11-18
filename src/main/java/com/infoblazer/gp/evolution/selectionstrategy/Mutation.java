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
import com.infoblazer.gp.evolution.model.*;
import com.infoblazer.gp.evolution.primitives.FunctionSet;
import com.infoblazer.gp.evolution.primitives.Primitive;
import com.infoblazer.gp.evolution.primitives.TerminalSet;
import com.infoblazer.gp.evolution.primitives.functions.AatImpl;
import com.infoblazer.gp.evolution.primitives.functions.Adf;
import com.infoblazer.gp.evolution.primitives.functions.BinaryNumber;
import com.infoblazer.gp.evolution.utils.GpUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by David on 8/23/2015.
 */
@Component
public class Mutation {
    private final static Logger logger = Logger.getLogger(Mutation.class.getName());
    @Value("${freezeRegimes:#{false}}")
    protected Boolean freezeRegimes;
    @Value("${regimeSelection:#{true}}")
    protected Boolean regimeSelection;  // always use reproduction on regimes
    private Random random = new Random();
    @Autowired
    private ResultLibrary resultLibrary;
    @Autowired
    private RegimeLibrary regimeLibrary;


    public AbstractProgram mutation(ResultProducingProgram program, FunctionSet functionSet, FunctionSet regimeFunctionSet,TerminalSet terminalSet, List<String> series
            , GrowMethod growMethod, int maxInitDepth, int maxDepth,  Integer predictedRegime) {

        //note regime indicator program mutation is constrained to type   BinaryNumber for the root, therefore don't mutate the root
        //in other words, don't ever mutate a binary number as that can only appear in the root of a regime detecting program
        AbstractProgram crossoverCopy = null;
        AbstractProgram copy = null;
        int programGroup = random.nextInt(2); // 0 = RP, 1 = regime
    FunctionSet effectiveFunctionSet = functionSet;
        boolean isRegimeMutation = false;
        if (program.getRegimeDetectionProgram()==null || programGroup==0) { //RP
            programGroup=0;
            crossoverCopy = GpUtils.getKyroInstance().copy(program);
        }else{
            effectiveFunctionSet  = regimeFunctionSet;
            isRegimeMutation= true;
            programGroup=1;
            crossoverCopy = GpUtils.getKyroInstance().copy(program.getRegimeDetectionProgram());
        }
        int selectedBranch = random.nextInt(2);

        if (selectedBranch==0 || crossoverCopy.getAdfs()==null) {
            selectedBranch=0;
            mutateRP(effectiveFunctionSet, terminalSet, series, growMethod, maxInitDepth, maxDepth, crossoverCopy, isRegimeMutation, predictedRegime);
        } else  {
            selectedBranch=1;
            mutateADF(series, growMethod, maxInitDepth, maxDepth, crossoverCopy, predictedRegime);
        }
        if (programGroup==0) {
            copy =  crossoverCopy;

        } else{
            ResultProducingProgram resultProducingProgram =  GpUtils.getKyroInstance().copy(program);
            resultProducingProgram.setRegimeDetectionProgram((RegimeDetectionProgram) crossoverCopy);
            copy = resultProducingProgram;

        }

        copy.setFitness(null);


        return copy;


    }

    private void mutateRP(FunctionSet functionSet, TerminalSet terminalSet, List<String> series, GrowMethod growMethod, int maxInitDepth,
                          int maxDepth, AbstractProgram copy, boolean isRegimeMutation, Integer predictedRegime) {

        logger.trace("Evolving RPB");
        Primitive crossOverPrimitive = null;
        List<Primitive> primitives = new ArrayList<>();
        AbstractSelectionStrategy.addPrimitives(primitives, copy.getRoot(), null);
        //Result producting branch
        while (crossOverPrimitive == null || crossOverPrimitive instanceof BinaryNumber) {   //todo if instance of aat, mutate aat in a regime dependent matter
            int crossoverPoint = random.nextInt(primitives.size());
            crossOverPrimitive = primitives.get(crossoverPoint);

        }
        if (crossOverPrimitive instanceof AatImpl) {
            mutateAAT(functionSet, terminalSet, series, growMethod, maxInitDepth, maxDepth, isRegimeMutation, predictedRegime, crossOverPrimitive);

        } else {
            Primitive primitive = AbstractProgram.generatePrimitive(crossOverPrimitive.getReturnType(), functionSet,null, terminalSet, series,
                    maxInitDepth,maxInitDepth, growMethod, false, null, false);
            boolean found = copy.replace(crossOverPrimitive, primitive, 0, maxDepth);
            if (!found){
                logger.warn("Did not find crossover primitive during mutation");
            }
        }
    }

    private void mutateAAT(FunctionSet functionSet, TerminalSet terminalSet, List<String> series, GrowMethod growMethod,
                           int maxInitDepth, int maxDepth, boolean isRegimeMutation, Integer predictedRegime, Primitive crossOverPrimitive) {
        AatImpl aat = (AatImpl) crossOverPrimitive;


        Integer key = aat.getLibaryKey();
        Library library;
        if (isRegimeMutation) {
            library = regimeLibrary;
        } else {
            library = resultLibrary;
        }
        Primitive[] libraryPrimitives = library.getPrimitiveById(key);
        int regimes = libraryPrimitives.length;
        for (int regime = 0; regime < regimes; regime++) {
            if (!freezeRegimes || predictedRegime == null || regime == predictedRegime) {
                Primitive libraryCopy = GpUtils.getKyroInstance().copy(libraryPrimitives[regime]);
               List<Primitive> primitives = new ArrayList<>();
                AbstractSelectionStrategy.addPrimitives(primitives, libraryCopy, null);

                //need to favor functions here, but use uniform selection of crossover for now
                int randomizer = primitives.size();
                int crossoverPoint = 0; //shouldnt be
                if (randomizer > 0) {
                    crossoverPoint = random.nextInt(randomizer);
                    crossOverPrimitive = primitives.get(crossoverPoint); //noADF
                    Primitive newPrimitive = AbstractProgram.generatePrimitive(crossOverPrimitive.getReturnType(), functionSet, null,terminalSet, series,
                            maxInitDepth, maxInitDepth,growMethod, false, null, false);
                    AbstractProgram.replace(libraryCopy,crossOverPrimitive,newPrimitive,0,maxDepth);
                    libraryPrimitives[regime] = libraryCopy;
                }
            }
        }
    }

    private void mutateADF(List<String> series, GrowMethod growMethod, int maxInitDepth, int maxDepth,
                           AbstractProgram copy, Integer predictedRegime) {
        logger.trace("Evolving ADFs");
        int maxAdfListSize = 0;
        List<Primitive>[] adfList = null;
        Integer regimes = null;
        for (Adf adf : copy.getAdfs()) {
            regimes = adf.getNumberOfRoots();
            if (adfList == null) {
                adfList = new List[regimes];
            }
            for (int regime = 0; regime < regimes; regime++) {
                adfList[regime] = new ArrayList<>();
                if (!freezeRegimes || predictedRegime == null || regime == predictedRegime) {
                    AbstractSelectionStrategy.addPrimitives(adfList[regime], adf.getRoot(regime), null);
                    if (maxAdfListSize == 0 || adfList[regime].size() > maxAdfListSize) {
                        maxAdfListSize = adfList[regime].size();
                    }
                }
            }
        }

        for (int regime = 0; regime < regimes; regime++) {

            boolean found = false;
            if (!freezeRegimes || predictedRegime == null || regime == predictedRegime) {
                //choose a new crossover point as this
                int crossOverADF = random.nextInt(adfList[regime].size());
                Primitive crossOverPrimitive = adfList[regime].get(crossOverADF);
                Primitive newPrimitive = AbstractProgram.generatePrimitive(crossOverPrimitive.getReturnType(),
                        copy.getAdfs().get(0).getFunctionSet(),null, copy.getAdfs().get(0).getTerminalSet(), series, maxInitDepth,maxInitDepth, growMethod, false, null, false);
                for (Adf adf : copy.getAdfs()) {
                    if (!found) {
                        found = AbstractProgram.replace(adf.getRoot(regime),crossOverPrimitive, newPrimitive, 0, maxDepth);
                    }
                }
                if (!found){
                    logger.warn("Mutation !NOT! successul for ADF");
                }
            }

        }


    }

}
