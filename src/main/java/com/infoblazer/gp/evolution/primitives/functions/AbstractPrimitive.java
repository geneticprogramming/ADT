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

import com.infoblazer.gp.evolution.primitives.Primitive;
import com.infoblazer.gp.evolution.primitives.terminals.*;

import java.util.List;
import java.util.Random;

/**
 * Created by David on 5/22/2014.
 */
public abstract class AbstractPrimitive implements Primitive {


    protected String name;
    protected  Integer id;
    protected String seriesCode;
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    protected boolean oneEquivalent(Primitive parameter){
        boolean result = false;
        if (parameter instanceof TerminalOne){
            result = true;
        }else    if (parameter instanceof RandomInteger){
            RandomInteger randomInteger = (RandomInteger) parameter;
            if (randomInteger.getValue().equals(1)){
                result = true;
            }
        }
        else if (parameter instanceof RandomDouble){
            RandomDouble randomDouble = (RandomDouble) parameter;
            if (randomDouble.getValue().equals(1.00d)){
                result = true;
            }
        }

        return result;
    }
    protected boolean zeroEquivalent(Primitive parameter){
        boolean result = false;
        if (parameter instanceof TerminalZero){
            result = true;
        }
        else if (parameter instanceof RandomInteger){
            RandomInteger randomInteger = (RandomInteger) parameter;
            if (randomInteger.getValue().equals(0)){
                result = true;
            }
        }
        else if (parameter instanceof RandomDouble){
            RandomDouble randomDouble = (RandomDouble) parameter;
            if (randomDouble.getValue().equals(0.0d)){
                result = true;
            }
        }

        return result;
    }

    protected boolean equivalent(Primitive parameter0, Primitive parameter1) {
        boolean result = false;
        if (zeroEquivalent(parameter0) && zeroEquivalent(parameter1)){
          result = true;
        }else if (oneEquivalent(parameter0) && oneEquivalent(parameter1)){
            result = true;
        }   else {
            //check values
            Number val1 = null;
            if (parameter0 instanceof RandomInteger) {
                RandomInteger randomInteger = (RandomInteger) parameter0;
                val1 = randomInteger.getValue();
            } else if (parameter0 instanceof RandomDouble) {
                RandomDouble randomDouble = (RandomDouble) parameter0;
                val1 = randomDouble.getValue();
            }
            Number val2 = null;
            if (parameter1 instanceof RandomInteger) {
                RandomInteger randomInteger = (RandomInteger) parameter1;
                val2 = randomInteger.getValue();
            } else if (parameter1 instanceof RandomDouble) {
                RandomDouble randomDouble = (RandomDouble) parameter1;
                val2 = randomDouble.getValue();
            }
            if (val1!=null && val2!=null && val1.equals(val2)){
                result = true;
            }
            if (!result && parameter0 instanceof Variable && parameter1 instanceof Variable){
                Variable v0 = (Variable) parameter0;
                Variable v1 = (Variable) parameter1;
                if (v0.getVariableName().equals(v1.getVariableName())){
                    result =  true;
                }
            }
        }

        return  result;
    }

    protected String randomSeries(List<String> series) {
        Random random = new Random();
        int i = random.nextInt(series.size());
        return  series.get(i);
    }


}
