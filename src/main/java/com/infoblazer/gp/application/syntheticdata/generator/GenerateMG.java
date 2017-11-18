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

package com.infoblazer.gp.application.syntheticdata.generator;

import java.util.Random;

/**
 * Created by David on 9/20/2015.
 */
public class GenerateMG implements SyntheticDataGenerator {

    @Override
    public Double[][] generate() {
        Random random = new Random();
        Double[] mg = new Double[1200];
        for (int i = 0; i < 30; i++) {
            mg[i] = random.nextDouble();
        }
        for (int i = 30; i < 1200; i++) {
            mg[i] = mg[i - 1] + 0.2 * mg[i - 30] / (1 + Math.pow(mg[i - 30], 10)) - 0.1 * mg[i - 1];
        }


        Double[][] result = new Double[400][2];
        Double y;
        for (int x = 0; x < 400; x++) {
            result[x][0] = Double.valueOf(x);
            if (x <= 200) { //LG
                y = mg[999 + x];

            } else {
                y = result[x - 1][1] + 0.2 * result[x - 30][1] / (1 + Math.pow(result[x - 30][1], 10)) - 0.1 * result[x - 1][1];
            }
            result[x][1] = y;


        }
        return result;
    }
}
