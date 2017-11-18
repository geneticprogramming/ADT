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

package com.infoblazer.gp.evolution.library;

import com.infoblazer.gp.evolution.primitives.Primitive;
import com.infoblazer.gp.evolution.primitives.functions.Aat;
import com.infoblazer.gp.evolution.primitives.functions.AatImpl;
import com.infoblazer.gp.evolution.selectionstrategy.AbstractSelectionStrategy;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: davidm
 * Date: 10/15/2015
 * Time: 11:19 AM
 */
public class AbstractLibrary implements Library {
    private final static Logger logger = Logger.getLogger(AbstractLibrary.class.getName());

    protected Map<Integer, Primitive[]> library = new HashMap<>();


    private AtomicInteger counter = new AtomicInteger(0);

    public Integer add(Primitive[] primitives) {
        int id = counter.addAndGet(1);
        for (Primitive primitive : primitives) {
            primitive.setId(id);
        }
        library.put(id, primitives);
        return id;

    }

    public void setPrimitive(Integer regime, Primitive primitive, Integer id) {
        Primitive[] primitives = library.get(id);
        primitives[regime] = primitive;
        library.put(id, primitives);

    }

    public Primitive[] getPrimitiveById(Integer id) {
        Primitive[] primitives = library.get(id);
        if (primitives == null) {
            logger.error("Found null looking for library id " + id);
        }
        return primitives;
    }

    @Override
    public Integer getSize() {
        return library.size();
    }

    @Override
    /**
     * remove any library functions not in list and not used by other library functions
     */
    public void retainAll(Set<Integer> libaryInUse) {
        Set<Integer> remove = new HashSet<>();
        Set<Integer> doNotRemove = new HashSet<>(); //other libraries
        for (Map.Entry<Integer, Primitive[]> entry : library.entrySet()) {
            if (!libaryInUse.contains(entry.getKey())) {
                remove.add(entry.getKey());
            }
            // this is only relevant if libraries can contain aat's . may need to failsafe evaluate if so to avoid infinite loop
            List<Primitive> primitives = new ArrayList<>();
            Primitive[] libaryPrimitives = entry.getValue();
            for (int i = 0; i < libaryPrimitives.length; i++) {
                AbstractSelectionStrategy.addPrimitivesTyped(primitives, libaryPrimitives[i], AatImpl.class);
                for (Primitive primitive : primitives) {
                    Aat aat = (Aat) primitive;
                    doNotRemove.add(aat.getLibaryKey());
                    logger.trace("retaining used in Library " + aat.getLibaryKey());
                }

            }
        }
        logger.trace("removing " + remove.size() + " of " + library.size());
        remove.removeAll(doNotRemove);
        for (Integer id : remove) {
            library.remove(id);
            logger.trace("removing " + id);
        }
        logger.trace("After GC. Size = " + library.size());


    }
}
