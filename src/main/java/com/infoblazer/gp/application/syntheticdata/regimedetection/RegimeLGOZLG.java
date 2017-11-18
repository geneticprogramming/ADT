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

package com.infoblazer.gp.application.syntheticdata.regimedetection;

import com.infoblazer.gp.evolution.library.Library;
import com.infoblazer.gp.evolution.primitives.GP_TYPES;
import com.infoblazer.gp.evolution.primitives.Primitive;
import com.infoblazer.gp.evolution.primitives.functions.AbstractFunction;
import com.infoblazer.gp.evolution.primitives.functions.Adf;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by David on 11/14/2015.
 */

@Component
@Scope("prototype")
public class RegimeLGOZLG extends AbstractFunction {
    public RegimeLGOZLG() {
    }

    @Override
    protected String getRepresentation(int MaxDepth) {
        return  "Regime LGOZLG x";
    }

    @Override
    public GP_TYPES[] getParameterReturnTypes() {
        return new GP_TYPES[0];
    }

    @Override
    public Primitive newInstance(List<String> series) {
        return new RegimeLGOZLG();
    }

    @Override
    public Object evaluate(boolean ignoreCurrent, Integer regime, Map<String, Object> parameters, Map<String, Adf> adfs, Library library, int level, Integer maxLevel) {
        Double x= (Double) parameters.get("x");
        if (x <= 200 || x >= 297) { //LG
            return true;
        } else { //OZ
                return false;
        }

    }

    @Override
    public GP_TYPES getReturnType() {
        return   GP_TYPES.BOOLEAN;
    }



}
