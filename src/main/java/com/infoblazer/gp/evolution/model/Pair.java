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

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 8/9/2014.
 */
public class Pair<T extends AbstractProgram> {
    private final static Logger logger = Logger.getLogger(Pair.class.getName());

    private T program1;
    private T program2;

    public Pair(T program1, T program2) {
        this.program1 = program1;
        this.program2 = program2;

    }

    public T getProgram1() {
        return this.program1;
    }

    public void setProgram1(T program1) {
        this.program1 = program1;
    }

    public T getProgram2() {
        return this.program2;
    }

    public void setProgram2(T program2) {
        this.program2 = program2;
    }

    public List<ResultProducingProgram> asResultProducingList() {
        int arraySize = 0;
        if (program1!=null){
            arraySize++;
        }
        if (program2!=null){
            arraySize++;
        }
        List<ResultProducingProgram>result = new ArrayList<>(arraySize);
        int counter = 0;
        if (program1!=null){
            result.add((ResultProducingProgram) program1);
        }
        if (program2!=null){
            result.add((ResultProducingProgram) program2);
        }
        return result;

    }
    public List<RegimeDetectionProgram> asRegimeDetectionList() {
        int arraySize = 0;
        if (program1!=null){
            arraySize++;
        }
        if (program2!=null){
            arraySize++;
        }
        List<RegimeDetectionProgram>result = new ArrayList<>(arraySize);
        int counter = 0;
        if (program1!=null){
            result.add((RegimeDetectionProgram) program1);
        }
        if (program2!=null){
            result.add((RegimeDetectionProgram) program2);
        }
        return result;

    }
}
