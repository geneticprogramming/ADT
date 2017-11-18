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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David on 9/21/2015.
 */
public class GenerateExpression implements SyntheticDataGenerator {
    private String expression;

    private Matcher matcher;

    public GenerateExpression(String expression) {
        this.expression = expression;
        Pattern  pattern = Pattern.compile("x");
        matcher =pattern.matcher(expression);



    }

    @Override
    public Double[][] generate()  {
        Double[][] result = new Double[100][2];
        double y;
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        String translatedExpression;
        try {
            for (int x = 0; x < 100; x++) {
                translatedExpression =matcher.replaceAll(String.valueOf(x));
                result[x][0] = Double.valueOf(x);
                y = (double) engine.eval(translatedExpression);
                result[x][1] = y;
             //  System.out.println(result[x][0 ] + "\t" + result[x][1 ]);
            }
        }catch (ScriptException e){
            System.err.println("Couldn't evaluate expression: " + expression + ". exiting!");
            System.exit(1);
        }

        return result;
    }
}
