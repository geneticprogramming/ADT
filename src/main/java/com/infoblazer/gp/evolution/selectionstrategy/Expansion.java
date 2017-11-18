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
import com.infoblazer.gp.evolution.primitives.Primitive;
import com.infoblazer.gp.evolution.primitives.functions.AatImpl;
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
public class Expansion {
    @Value("${freezeRegimes:#{false}}")
    private Boolean freezeRegimes;
    @Autowired
    private ResultLibrary resultLibrary;
    @Autowired
    private RegimeLibrary regimeLibrary;
    private Random random = new Random();
    private static final Logger logger = Logger.getLogger(Expansion.class.getName());

    public Population expand(ResultProducingProgram winner, int regimes, int maxDepth, Integer predictedRegime) {
        Population result = new Population();
        int selectedBranch = random.nextInt(2);
        if (selectedBranch==0 || regimes==1) {
            ResultProducingProgram resultProducingProgram = (ResultProducingProgram) doExpansion(winner, regimes, maxDepth, false, predictedRegime);
            result.setResultPopulation(Collections.singletonList(resultProducingProgram));
        }   else{
            RegimeDetectionProgram regimeDetectionProgram = (RegimeDetectionProgram) doExpansion(winner.getRegimeDetectionProgram(), 1, maxDepth, true, predictedRegime);
            ResultProducingProgram copy = GpUtils.getKyroInstance().copy(winner);
            copy.setRegimeDetectionProgram(regimeDetectionProgram);
            result.setResultPopulation(Collections.singletonList(copy));

        }




        return result;
    }

    private AbstractProgram doExpansion(AbstractProgram program, int regimes, int maxDepth, boolean isRegimeDetection, Integer predictedRegime) {


        AbstractProgram copy = GpUtils.getKyroInstance().copy(program);
        //will need an instance of that aat here. Otherwise it will just be a reprodunction
        //find a random instance of an aat
        List<Primitive> primitives = new ArrayList<>();
        AbstractSelectionStrategy.addPrimitivesTyped(primitives, copy.getRoot(), AatImpl.class);

        if (!primitives.isEmpty()) {  //not a single terminal program
            int crossoverPoint = random.nextInt(primitives.size());
            AatImpl aat = (AatImpl) primitives.get(crossoverPoint);
            if (!aat.isModified()) {
                Library library;
                library = isRegimeDetection ? regimeLibrary : resultLibrary;
                Primitive[] primitive = library.getPrimitiveById(aat.getLibaryKey());
                Integer randomRegime ;
                if (isRegimeDetection) {
                    randomRegime = 0;
                } else {
                    randomRegime = predictedRegime; // chosen current regime
                    if (!freezeRegimes || predictedRegime == null) {
                        randomRegime = random.nextInt(regimes); //or choose random implemenation
                    }
                }


                Primitive newPrimitive = primitive[randomRegime];
                Primitive copyPrimitive = GpUtils.getKyroInstance().copy(newPrimitive);


               boolean found =  copy.replace(aat, copyPrimitive, 0, maxDepth);
                if (!found){
                    logger.warn("Did not find primitive expand target");
                }


            }


        }

        return copy;

    }

}
