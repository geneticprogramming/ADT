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

package com.infoblazer.gp.evolution.primitives;

import com.infoblazer.gp.evolution.primitives.functions.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by David on 5/22/2014.
 */
public class FunctionSet extends PrimitiveSet {

    public FunctionSet(Function[] functions) {
        this.functions = functions;
    }
    public int getLength(){
        return  functions.length;
    }
    private Function[] functions;

    public FunctionSet() {
    }

    public Function[] getItems() {
        return functions;
    }

    public static FunctionSet reduceArity(FunctionSet functionSet, final int arity) {

        List<Function> functionList = new ArrayList<>(functionSet.functions.length);
        for (Function function: functionSet.functions){
            if (function.getArity()==arity){
                functionList.add(function);
            }
        }
        Function[] functions = new Function[functionList.size()];
        functionList.toArray(functions);
        return new FunctionSet(functions);
    }
}
