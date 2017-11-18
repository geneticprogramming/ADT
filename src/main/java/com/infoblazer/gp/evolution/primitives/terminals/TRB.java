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

/**
 * Indicator from FGP
 * Created by David on 5/24/2014.
 */
@Component
@Scope("prototype")
public class TRB extends AbstractTerminal implements Terminal {
    private Integer window; //moving average window

    public TRB() {
    }

    @Override
    public boolean allowInContext(FunctionContext functionContext) {
        return true;
    }


    public TRB(String seriesCode,Integer window) {
        this.seriesCode = seriesCode;
        this.window = window;

    }

    @Override
    public String asLanguageString(int level, Integer maxLevel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append('\t');
        }
        sb.append("TRB_").append(window);
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

    @Override
    public Object evaluate(boolean ignoreCurrent, Integer regime, Map<String, Object> evaluationParams, Map<String, Adf> adfs,Library library, int level, Integer maxLevel) {

        EvaluationLogger.dataAccessOperation();
        List<Double> series = ( List<Double>) evaluationParams.get(seriesCode);



        int endPos =  series.size()-1; // offset 0 is end pos
        if (endPos<0){
            endPos = 0;
        }

        int startPos = endPos-window;    //window 0 = endpos only. window 1 = 2 values
        if (startPos<0){
            startPos = 0;
        }



        double maximum = (-1.0 * Double.MAX_VALUE);


        for (int i = startPos; i < endPos; i++) {
            if (series.get(i) > maximum) {
                maximum = series.get(i);
            }
        }
        Double val = series.get(endPos);


        return val > maximum;
    }

    @Override
    public Primitive newInstance(List<String> series) {

        return new TRB(seriesCode,window);

    }


    @Override
    public GP_TYPES getReturnType() {
        return GP_TYPES.BOOLEAN;
    }

    @Override
    public void setParams(String[] paramVals) {
        this.seriesCode = paramVals[0];
        this.window = Integer.valueOf(paramVals[1]);


    }


}
