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

package com.infoblazer.gp.evolution.primitives.functions;

import com.infoblazer.gp.application.data.service.EvaluationLogger;
import com.infoblazer.gp.evolution.library.Library;
import com.infoblazer.gp.evolution.primitives.FunctionSet;
import com.infoblazer.gp.evolution.primitives.Primitive;

import java.util.Map;

/**
 * Created by David on 5/22/2014.
 */
public abstract class AbstractFunction extends AbstractPrimitive implements Function {




    protected AbstractFunction() {
        if (getArity()!=null) {
            parameters = new Primitive[getArity()];
        }

    }

    protected abstract  String getRepresentation(int MaxDepth );

    protected Primitive[] parameters;  //init to arity

    public Primitive[] getParameters() {
        return parameters;
    }

    public void setParameters(Primitive[] parameters) {
        this.parameters = parameters;
    }



    public static FunctionSet addAll(Function[]... sets) {

        int elements = 0;
        for (Function[] set : sets) {
            if (set != null) {
                elements += set.length;
            }
        }
        Function[] combinedSet = new Function[elements];
        int element = 0;
        for (Function[] set : sets) {
            if (set != null) {
                for (int i = 0; i < set.length; i++) {
                    combinedSet[element] = set[i];
                    element++;

                }
            }
        }
        return new FunctionSet(combinedSet);
    }

    public   Integer getArity() {

            return getParameterReturnTypes().length;

    }
    @Override
    public void setParams(String[] paramVals) {

    }
    @Override
    public String asLanguageString(int level, Integer maxLevel  )  {
        String programString = null;
        if (maxLevel==null || level <= maxLevel) {


            StringBuilder sb = new StringBuilder();
            sb.append('\n');
            for (int i = 0; i < level; i++) {
                sb.append('\t');
            }
            sb.append('(');
            sb.append(getRepresentation(maxLevel));
            for (int i = 0; i < parameters.length; i++) {
                String pattern = "$" + (i + 1);
                int patternIndex = sb.indexOf(pattern);
                if (parameters[i] != null) {
                    int count = 1;
                    while (count < 100 && patternIndex > 0) {
                        count++;
                        String patternString = parameters[i].asLanguageString(level + 1, maxLevel) + ' ';
                        sb.replace(patternIndex, patternIndex + 2, patternString);
                        patternIndex = sb.indexOf(pattern);
                    }
                }
                sb.append(' ');


            }
            //sb.append("\windowSize");
            //for (int i = 0;i<level;i++){
            //    sb.append("\t");
            // }
            sb.append(')');
            programString =  sb.toString();
        }
        return programString;
    }
    @Override
    public Primitive simplify() {
          return this; //default case
    }


}
