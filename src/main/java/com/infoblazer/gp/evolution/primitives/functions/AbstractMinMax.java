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
import com.infoblazer.gp.evolution.primitives.GP_TYPES;

import java.util.List;
import java.util.Map;

/**
 * User: davidm
 * Date: 7/1/2015
 * Time: 4:08 PM
 */
public abstract class AbstractMinMax extends AbstractFunction {





    @Override
    public  GP_TYPES[] getParameterReturnTypes() {
        return  new GP_TYPES[]{GP_TYPES.NUMBER};
    }
    @Override
    public Object evaluate(boolean ignoreCurrent,Integer regime,Map<String, Object> evaluationParams, Map<String,Adf> adfs,Library library,int  level,Integer maxLevel)  {
        EvaluationLogger.dataAccessOperation();
        List<Double> series = ( List<Double>) evaluationParams.get(seriesCode);

        Number window = (Number) parameters[0].evaluate(ignoreCurrent,regime,evaluationParams, adfs,library,level+1,maxLevel);

        int endPos =  series.size()-1; // offset 0 is end pos
        if (endPos== series.size()-1 && ignoreCurrent){
            endPos = series.size()-2;
        }
        if (endPos<0){
            endPos = 0;
        }


        int startPos = endPos-Math.abs(window.intValue());    //window 0 = endpos only. window 1 = 2 values
        if (startPos>endPos){
            startPos = endPos-1;
        }
        if (startPos<0){
            startPos = 0;
        }


        double result = compareMinMax(series, endPos, startPos);

        return result;

    }
    protected abstract double compareMinMax(List<Double> series, int endPos, int startPos) ;



    @Override
    public GP_TYPES getReturnType() {
        return GP_TYPES.NUMBER;
    }



}
