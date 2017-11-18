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

/**
 * Created by David on 9/20/2015.
 */
public class DataGeneratorFactory {
    public static SyntheticDataGenerator getDataGenerator(String series) {
        if ("LGOZLG".equals(series)){
            return new GenerateLGOZLG();
        }
        if ("HN".equals(series)){
            return new GenerateHN();
        }
        if ("LG".equals(series)){
            return new GenerateLG();
        }
        if ("MG".equals(series)){
            return new GenerateMG();
        }
        if ("MGHENMG".equals(series)){
            return new GenerateMGHENMG();
        }
        if ("OZ".equals(series)){
            return new GenerateOZ();
        }
        if ("SINE".equals(series)){
            return new GenerateSINE();
        }
        if ("SINE1".equals(series)){
            return new GenerateSINE1();
        }
        if ("SINE3".equals(series)){
            return new GenerateSINE3();
        }
        //else evalute javascript expression
            return new GenerateExpression(series);

    }
}
