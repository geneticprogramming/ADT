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
public class GenerateLGOZLG implements SyntheticDataGenerator {

    @Override
    public Double[][] generate() {
        Double[][] result = new Double[401][2];
        double yt = 0;
        double ytm1 = 0; //t-1
        double ytp1 = 0;  //t+1
        for (int x = 0; x < 401; x++) {
            result[x][0] = Double.valueOf(x);
            if (x == 0) {    //initial seed value
                yt = 0.9;
                result[x][1] = yt;

            } else {
                if (x <= 200 || x >= 297) { //LG
                    ytp1 = 4 * yt * (1 - yt);
                } else { //OZ
                    ytp1 = 1.8708 * yt - ytm1;
                }
                result[x][1] = ytp1;
                ytm1 = yt;
                yt = ytp1;
            }
        }
        return   result;
    }
}
