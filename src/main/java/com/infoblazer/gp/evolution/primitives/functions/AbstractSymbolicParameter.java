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
import com.infoblazer.gp.evolution.primitives.FunctionContext;
import com.infoblazer.gp.evolution.primitives.GP_TYPES;
import com.infoblazer.gp.evolution.primitives.Primitive;
import com.infoblazer.gp.evolution.primitives.terminals.SymbolicParameter;

import java.util.Map;
import java.util.Random;

/**
 * Created by David on 6/21/2014.
 */

public abstract class AbstractSymbolicParameter implements SymbolicParameter {


    protected String name;

    public AbstractSymbolicParameter() {
    }

    @Override
    public String asLanguageString(int level,Integer maxLevel) {
        return name;
    }



    public String getName() {
        return name;
    }

    public AbstractSymbolicParameter(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(boolean ignoreCurrent,Integer regime,Map<String, Object> evaluationParams, Map<String, Adf> adfs,Library library,int  level,Integer maxLevel) {
        EvaluationLogger.dataAccessOperation();
        return evaluationParams.get(name);
    }

    private Integer id;
    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public void setParams(String[] paramVals) {
          this.name = paramVals[0];
    }

    @Override
    public boolean allowInContext(FunctionContext functionContext) {
        return true;
    }

    @Override
    public Primitive simplify() {
        return  this;

    }

    public static SymbolicParameter buildParmeter(String name,GP_TYPES gp_types) {

        if (gp_types==GP_TYPES.BOOLEAN) {
            return new SymbolicParameterBoolean(name);
        }else if (gp_types==GP_TYPES.NUMBER){

            return new SymbolicParameterNumeric(name);
        }else {
            Random random = new Random();
            if (random.nextInt(2)==0){

                return new SymbolicParameterBoolean(name);
            }  else{
                return new SymbolicParameterNumeric(name);
            }
        }
    }
}
