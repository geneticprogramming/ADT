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

package com.infoblazer.gp.evolution.primitives;

import com.infoblazer.gp.evolution.library.Library;
import com.infoblazer.gp.evolution.primitives.functions.Adf;

import java.util.List;
import java.util.Map;

/**
 * Created by David on 5/22/2014.
 */
public interface Primitive {
    void setId(Integer id);
    Integer getId();


    String asLanguageString(int level,Integer maxLevel);

    Primitive newInstance(List<String> series);

     Object evaluate(boolean ignoreCurrent,Integer regime,Map<String, Object> parameters, final  Map<String,Adf> adfs,Library library,
                     int level,Integer maxLevel);

    GP_TYPES getReturnType();
    void setParams(String[] paramVals);

    Primitive simplify();


}
