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
import com.infoblazer.gp.evolution.primitives.terminals.RandomDouble;
import com.infoblazer.gp.evolution.primitives.terminals.RandomInteger;
import com.infoblazer.gp.evolution.primitives.terminals.TerminalOne;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * Created by David on 5/24/2014.
 */

@Component
@Scope("prototype")
public class Subtract extends AbstractFunction {
    public Subtract() {
    }

    @Override
    protected String getRepresentation(int maxLevel) {
        return " -  $1 $2 ";
    }

    @Override
    public GP_TYPES[] getParameterReturnTypes() {
        return new GP_TYPES[]{GP_TYPES.NUMBER, GP_TYPES.NUMBER};
    }

    @Override
    public Primitive newInstance(List<String> series) {
        return new Subtract();


    }

    @Override
    public Object evaluate(boolean ignoreCurrent,Integer regime,Map<String, Object> evaluationParams, Map<String,Adf> adfs,Library library,int  level,Integer maxLevel)  {
        EvaluationLogger.dataAccessOperation();
        Number val1 = (Number) parameters[0].evaluate(ignoreCurrent,regime,evaluationParams, adfs,library,level+1,maxLevel);
        Number val2 = (Number) parameters[1].evaluate(ignoreCurrent,regime,evaluationParams, adfs,library,level+1,maxLevel);
        return val1.doubleValue()-val2.doubleValue();
    }

    @Override
    public GP_TYPES getReturnType() {
        return GP_TYPES.NUMBER;
    }

    @Override
    public Primitive simplify() {
        Primitive result = this;
        Primitive parameter0 =parameters[0].simplify();
        Primitive parameter1 =parameters[1].simplify();
        if (zeroEquivalent(parameter0)) {
            result = parameter1;

        }else  if (zeroEquivalent(parameter1)) {
            result = parameter0;

        }else  if (equivalent(parameter0,parameter1)) {
            result = new TerminalOne();

        }  else  if (parameter0 instanceof RandomInteger && parameter1 instanceof RandomInteger ) {
            RandomInteger r0 = (RandomInteger) parameter0;
            RandomInteger r1 = (RandomInteger) parameter1;
            Integer val0 =        r0.getValue();
            Integer val1 =r1.getValue();
            result = new RandomInteger(val0-val1);
        }
        else  if (parameter0 instanceof RandomDouble && parameter1 instanceof RandomDouble ) {
            RandomDouble r0 = (RandomDouble) parameter0;
            RandomDouble r1 = (RandomDouble) parameter1;
            Double val0 =        r0.getValue();
            Double val1 =r1.getValue();
            result = new RandomInteger(val0.intValue()-val1.intValue());
        }

        return result;

    }

}
