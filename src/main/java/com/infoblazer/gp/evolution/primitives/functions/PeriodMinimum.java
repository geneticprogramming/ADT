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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by David on 5/24/2014.
 */
@Component
@Scope("prototype")
public class PeriodMinimum extends AbstractMinMax {
    public PeriodMinimum(String seriesCode) {
        this.seriesCode = seriesCode;
    }


    @Override
    protected String getRepresentation(int maxLevel) {

        return "periodMinimum " + seriesCode + " $1";
    }
    protected double compareMinMax(List<Double> series, int endPos, int startPos) {
        double result = series.get(startPos);
        for (int i = startPos+1;i<=endPos;i++)  {
            if (series.get(i)<result) {
                result = series.get(i);

            }
        }
        return result;
    }
    public PeriodMinimum() {
    }

    @Override
    public Primitive newInstance(List<String> series) {

        return new PeriodMinimum(randomSeries(series));



    }
}
