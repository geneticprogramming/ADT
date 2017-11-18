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
import com.infoblazer.gp.evolution.model.AbstractProgram;
import com.infoblazer.gp.evolution.library.Library;
import com.infoblazer.gp.evolution.primitives.GP_TYPES;
import com.infoblazer.gp.evolution.primitives.Primitive;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by David on 5/24/2014.
 */
@Component
@Scope("prototype")
public class Smallest extends AbstractFunction {
    public Smallest() {
    }


    @Override
    protected String getRepresentation(int maxLevel) {
        /*
        Params:
        $1: timeseriesId
        $2: offset

         $4:target sax String, represented as long
         */
        return "minimum $1 marketDate $2 ";
    }
    @Override
    public  GP_TYPES[] getParameterReturnTypes() {
        return  new GP_TYPES[]{GP_TYPES.NUMBER,GP_TYPES.NUMBER};
    }
       @Override
       public Object evaluate(boolean ignoreCurrent,Integer regime,Map<String, Object> evaluationParams, Map<String,Adf> adfs,Library library,int level,Integer maxLevel)  {
           EvaluationLogger.dataAccessOperation();
           Number val1 = (Number) parameters[0].evaluate(ignoreCurrent,regime,evaluationParams, adfs,library,level+1,maxLevel);
           Number val2 = (Number) parameters[1].evaluate(ignoreCurrent,regime,evaluationParams, adfs,library,level+1,maxLevel);
           if (val1.doubleValue()<val2.doubleValue()){
               return val1;
           }else{
               return val2;

           }

       }
    @Override
    public Primitive newInstance(List<String> series) {
        return  new Smallest();


    }

    @Override
    public GP_TYPES getReturnType() {
        return GP_TYPES.NUMBER;
    }
}
