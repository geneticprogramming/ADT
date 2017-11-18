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

package com.infoblazer.gp.evolution.primitives.terminals;

import com.infoblazer.gp.application.data.service.EvaluationLogger;
import com.infoblazer.gp.evolution.library.Library;
import com.infoblazer.gp.evolution.primitives.FunctionContext;
import com.infoblazer.gp.evolution.primitives.GP_TYPES;
import com.infoblazer.gp.evolution.primitives.Primitive;
import com.infoblazer.gp.evolution.primitives.functions.Adf;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by David on 5/24/2014.
 */
@Component
@Scope("prototype")
public class RandomInteger extends AbstractTerminal  implements Terminal {

    @Override
    public boolean allowInContext(FunctionContext functionContext) {
        return true;
    }


    public RandomInteger() {
    }

    public RandomInteger(Integer value) {
        this.value = value;
    }

    @Override
    public String asLanguageString(int level,Integer maxLevel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i<level;i++){
            sb.append('\t');
        }
        sb.append(value.toString());
        return sb.toString();
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
    private Integer lowRange;
    private Integer highRange;
    private Integer value;
    @Override
    public Object evaluate(boolean ignoreCurrent,Integer regime,Map<String, Object> evaluationParams, Map<String,Adf> adfs,Library library,int  level,Integer maxLevel) {
        EvaluationLogger.dataAccessOperation();
        return value;
    }
    public RandomInteger(Integer lowRange, Integer highRange) {
          Random random = new Random();
        this.lowRange = lowRange;
        this.highRange = highRange;
        value = random.nextInt(highRange - lowRange + 1) + lowRange;


    }
    @Override
    public Primitive newInstance(List<String> series) {

      return  new RandomInteger(lowRange,highRange);

    }
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public GP_TYPES getReturnType() {
        return GP_TYPES.NUMBER;
    }
    @Override
    public void setParams(String[] paramVals) {
        Random random = new Random();
        this.lowRange = Integer.valueOf(paramVals[0]);
        this.highRange = Integer.valueOf(paramVals[1]);
        value = random.nextInt(highRange - lowRange + 1) + lowRange;

    }
}
