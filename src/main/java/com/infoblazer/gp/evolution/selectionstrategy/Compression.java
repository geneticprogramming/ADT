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
import com.infoblazer.gp.evolution.primitives.functions.Aat;
import com.infoblazer.gp.evolution.primitives.functions.AatImpl;
import com.infoblazer.gp.evolution.primitives.functions.BinaryNumber;
import com.infoblazer.gp.evolution.primitives.functions.Function;
import com.infoblazer.gp.evolution.primitives.terminals.AbstractTerminal;
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
public class Compression {
    private final Random random = new Random();
    @Value("${minimumCompressionSize:#{null}}")
    private Integer minimumCompressionSize ; //= 200;
    @Autowired
    private ResultLibrary resultLibrary;
    @Autowired
    private RegimeLibrary regimeLibrary;
    private final static Logger logger = Logger.getLogger(Compression.class.getName());

     public Population compress(final ResultProducingProgram winner, final int regimes, final int maxDepth) {
        Population result = new Population();
         int selectedBranch = random.nextInt(2);
         if (selectedBranch==0 || regimes==1) { // program branch
             ResultProducingProgram resultProducingProgram = (ResultProducingProgram) doCompression(winner, regimes, maxDepth, false);
             result.setResultPopulation(Collections.singletonList(resultProducingProgram));

         }else {// regime branch
             RegimeDetectionProgram regimeDetectionProgram = (RegimeDetectionProgram) doCompression(winner.getRegimeDetectionProgram(), 1, maxDepth, true);
             ResultProducingProgram copy = GpUtils.getKyroInstance().copy(winner);
             copy.setRegimeDetectionProgram(regimeDetectionProgram);
             result.setResultPopulation(Collections.singletonList(copy));
         }


        return result;
    }



    private AbstractProgram doCompression(final AbstractProgram program, final int regimes, final int maxDepth, final boolean isRegimeCompression) {
        //Choose a node at random and make it essentially an ADT with regime specific behavior


        AbstractProgram copy = GpUtils.getKyroInstance().copy(program);

            List<Primitive> primitives = new ArrayList<>();
            AbstractSelectionStrategy.addPrimitives(primitives, copy.getRoot(), null);
            if (!primitives.isEmpty()) {  //not a single terminal program


                //Don't add adfs as that is another technique
                //By now the node list should be buit.
                int crossoverPoint = random.nextInt(primitives.size());
                int crossoverAttempts = 0;
                Primitive crossoverPrimitive = null;
                while (crossoverAttempts <= AbstractSelectionStrategy.CROSSOVER_ATTEMPTS
                        && (crossoverPrimitive == null || crossoverPrimitive instanceof BinaryNumber
                        || crossoverPrimitive instanceof AbstractTerminal)) {
                   crossoverPrimitive = primitives.get(crossoverPoint);

                   if (minimumCompressionSize!=null){
                       List<Primitive> primitivesTmp = new ArrayList<>();
                       AbstractSelectionStrategy.addPrimitives(primitivesTmp, crossoverPrimitive, null);
                       if (primitivesTmp.size()<minimumCompressionSize){
                           crossoverPrimitive = null;
                       }
                   }
                    crossoverPoint = random.nextInt(primitives.size());
                    crossoverAttempts++;
                }
                if (crossoverAttempts <= AbstractSelectionStrategy.CROSSOVER_ATTEMPTS) {

                    assert !(crossoverPrimitive instanceof Aat);
                    Primitive newPrimitive = compressFunction((Function) crossoverPrimitive, regimes, isRegimeCompression);
                    copy.replace(crossoverPrimitive, newPrimitive, 0, maxDepth);

                } else {
                    logger.debug("Couldn't find function to compress in " + (isRegimeCompression ? "regime compresssion" : "compression") + "returning original");
                }



            }

            return copy;
        }



    //returns the placeholder pointer to aat
    private Primitive compressFunction(final Function function, final int regimes, final boolean isRegimeCompression) {


        Primitive[] template = new Primitive[regimes];
        for (int i = 0; i < regimes; i++) {
            Primitive compressed = GpUtils.getKyroInstance().copy(function); //Copy the entire tree rooted at the selected tree
            template[i] = compressed;
        }
        Library library;
        library = isRegimeCompression ? regimeLibrary : resultLibrary;

        Integer newKey = library.add(template);
        logger.debug("library size = " + library.getSize());
        AatImpl aat = new AatImpl(newKey);
        aat.setReturnType(function.getReturnType());
        aat.setParameters(function.getParameters());


        return aat;

    }

}
