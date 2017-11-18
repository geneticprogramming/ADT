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

public class GenerateMGHENMG implements SyntheticDataGenerator {


    public Double[][] generate() {

        Double randomVals[] = new Double[] {
            0.8440964138066118,
                    0.9532661055224593,
                    0.06636753866027945,
                    0.8620971393379627,
                    0.2100850778694464,
                    0.4894196903422636,
                    0.11588139131258257,
                    0.47264417635200906,
                    0.08408151444136325,
                    0.5657337444733351,
                    0.695537452821834,
                    0.6371301993098292,
                    0.11797947216718663,
                    0.6643411155833825,
                    0.35853611038911015,
                    0.8617986087216263,
                    0.028791106927193777,
                    0.8136196388158929,
                    0.01620324342793167,
                    0.6569433423303102,
                    0.2871394142441621,
                    0.10838147285823774,
                    0.43001491262576674,
                    0.47020747316392664,
                    0.5455742442961969,
                    0.859267154912505,
                    0.5342809689449174,
                    0.3258840447272048,
                    0.5628455094036957,
                    0.46854285555526787,
                    0.6234535818110518
        } ;



        Double[] mg = new Double[1200];
        for (int i = 0; i < 31; i++) {
            mg[i] = randomVals[i];

        }
        for (int i = 31; i < 1200; i++) {
            mg[i] = mg[i - 1] + 0.2 * mg[i - 31] / (1 + Math.pow(mg[i - 31], 10)) - 0.1 * mg[i - 1];
        }


        Double[][] result = new Double[401][2];
        Double y;
        for (int x = 0; x < 401; x++) {
            result[x][0] = Double.valueOf(x);
            if (x <= 200) { //LG  , seeded off initial generation
                y = mg[999 + x];
            } else if (x > 300) {
                y = result[x - 1][1] + 0.2 * result[x - 31][1] / (1 + Math.pow(result[x - 31][1], 10)) - 0.1 * result[x - 1][1];
            } else { //OZ
                y = 0.3 * result[x - 2][1] + 1 - 1.4 * (result[x - 1][1] * result[x - 1][1]);
            }
            result[x][1] = y;


        }
        return result;
    }
}
