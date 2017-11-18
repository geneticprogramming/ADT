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

import com.infoblazer.gp.evolution.primitives.FunctionSet;
import com.infoblazer.gp.evolution.primitives.Primitive;
import com.infoblazer.gp.evolution.primitives.TerminalSet;
import com.infoblazer.gp.evolution.primitives.terminals.SymbolicParameter;

/**
 * Created by David on 6/18/2014.
 */
public interface Adf extends Function {

    String getName();
    void setName(String name);
    Integer getNodeCount();
    Integer getDepth();
    void setNodeCount(Integer nodeCount);
    void setDepth(Integer nodeDepth);
    FunctionSet getFunctionSet();
    TerminalSet getTerminalSet();
    TerminalSet getSymbolicParameters();
    void setSymbolicParameters(TerminalSet symbolicParameters );
    void setFunctionSet(FunctionSet functionSet);
    void  setTerminalSet(TerminalSet terminalSet);
    Primitive getRoot(int regime);
    void setRoot(Primitive primitive,int regime);
    void setRoot(Primitive[] primitive);
     Integer getNumberOfRoots();
    void initializeRoots(int regimes);
}
