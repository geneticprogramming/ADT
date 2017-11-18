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

import com.infoblazer.gp.evolution.selectionstrategy.SelectionStrategy;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by David on 4/27/2015.
 */

public class ProgramComparator implements Comparator<ResultProducingProgram> {

    private SelectionStrategy.Direction direction;

    public ProgramComparator(SelectionStrategy.Direction direction) {
        this.direction = direction;
    }

    @Override

            public int compare(ResultProducingProgram o1, ResultProducingProgram o2) {
                //These should not be null
                Double fitness1 = o1.getFitness();
                Double fitness2 = o2.getFitness();
                if (direction == SelectionStrategy.Direction.ASCENDING) {
                    return fitness1.compareTo(fitness2);
                } else {
                    return fitness2.compareTo(fitness1);
                }
            }

    }


