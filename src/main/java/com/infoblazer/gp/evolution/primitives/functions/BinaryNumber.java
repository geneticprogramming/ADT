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
import com.infoblazer.gp.evolution.primitives.Primitive;

import java.util.List;
import java.util.Map;

/** Currently not configured as a spring bean. only used for regime processing
 * Created by David on 5/24/2014.
 */
public class BinaryNumber extends AbstractFunction {
    private GP_TYPES[] gp_types ;

    public BinaryNumber() {
    }

    public BinaryNumber(int regimes) {
        int binaryIndicators =Integer.toBinaryString(regimes-1).length();
        gp_types = new GP_TYPES[binaryIndicators];
        for (int i = 0;i<binaryIndicators;i++){
            gp_types[i] = GP_TYPES.BOOLEAN;
        }
        parameters = new Primitive[binaryIndicators];


    }

    @Override
    public void setParameters(Primitive[] parameters) {
        for (int i = 0;i<parameters.length;i++){
            if (parameters[i].getReturnType()!=gp_types[i]){
                System.exit(1);
            }
        }
        super.setParameters(parameters);
    }

    @Override
    protected String getRepresentation(int maxLevel) {
        StringBuilder sb = new StringBuilder(" BinaryNumber ");
        for (int i = 1;i<=parameters.length;i++) {
            sb.append(" $").append(i);
        }
        return sb.toString();
    }

    @Override
    public Primitive newInstance(List<String> series) {
       throw new UnsupportedOperationException("Cannot instantiate binaryNumber with newInstance") ;


    }
    public   Integer getArity() {

        if (gp_types !=null) {

            return gp_types.length;
         }else{
             return  null;
         }

    }


    @Override
    public GP_TYPES[] getParameterReturnTypes() {

        return gp_types;

    }



    @Override
    public Object evaluate(boolean ignoreCurrent,Integer regime,Map<String, Object> evaluationParams, Map<String,Adf> adfs,Library library,int  level,Integer maxLevel)  {
        EvaluationLogger.dataAccessOperation();
           StringBuilder binaryString = new StringBuilder();
           for (int i = 0; i < parameters.length; i++) {
               Boolean val = (Boolean) parameters[i].evaluate(ignoreCurrent, regime, evaluationParams, adfs,library,level+1,maxLevel);
               binaryString.append(val ? '1' : '0');
           }

           return Integer.valueOf(binaryString.toString(), 2);


    }

    @Override
    public GP_TYPES getReturnType() {
        return GP_TYPES.NUMBER;
    }
}
