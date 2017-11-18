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

import com.infoblazer.gp.application.data.service.EvaluationLogger;
import com.infoblazer.gp.evolution.library.Library;
import com.infoblazer.gp.evolution.primitives.FunctionSet;
import com.infoblazer.gp.evolution.primitives.GP_TYPES;
import com.infoblazer.gp.evolution.primitives.Primitive;
import com.infoblazer.gp.evolution.primitives.TerminalSet;
import com.infoblazer.gp.evolution.primitives.terminals.SymbolicParameter;
import com.infoblazer.gp.evolution.primitives.terminals.Terminal;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 6/18/2014.
 */
public class AdfImpl extends AbstractFunction implements Adf {
    private final static Logger logger = Logger.getLogger(AdfImpl.class.getName());

    private int arity;
    private FunctionSet functionSet;
    private TerminalSet terminalSet;
    private TerminalSet symbolicParameters;
    private Primitive[] root; //root per regime

    private Integer nodeCount;
    private Integer depth;

    public static Logger getLogger() {
        return AdfImpl.logger;
    }

    public Primitive[] getRoot() {
        return this.root;
    }

    @Override
    public Integer getNodeCount() {
        return this.nodeCount;
    }

    @Override
    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }

    @Override
    public Integer getDepth() {
        return this.depth;
    }

    @Override
    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getNumberOfRoots(){
        return root.length;
    }
    public AdfImpl() {
    }

    @Override
    public void initializeRoots(int regimes) {
        root = new Primitive[regimes];
    }

    public TerminalSet getTerminalSet() {
        return terminalSet;
    }

    public void setArity(int arity) {
        this.arity = arity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object evaluate(boolean ignoreCurrent,Integer regime,Map<String, Object> evaluationParams, final Map<String, Adf> adfs,
                           Library library, int  level,Integer maxLevel)  {
        EvaluationLogger.dataAccessOperation();
        if (logger.isTraceEnabled()) {
            logger.trace("entering evaluate");
        }


            //search for this adf and call with supplied params
            // Adf evalAdf = adfs.get(this.name);
            Primitive regimeRoot = getRoot(regime);
        List<String> seriesList = (List<String>)evaluationParams.get("serieslist");
            //pass in new symbolic params arg0....argn
            Map<String, Object> adfEvaluationParams = new HashMap<String,Object>(symbolicParameters.getItems().length+seriesList.size()+1);
            int paramCount = 0;
            for (Terminal terminal :symbolicParameters.getItems()) {
                String paramName = ((SymbolicParameter) terminal).getName();
                Object val = parameters[paramCount].evaluate(ignoreCurrent, regime, evaluationParams, adfs,library,0,maxLevel);
                adfEvaluationParams.put(paramName, val);
                paramCount++;
            }
            if (evaluationParams.get(seriesCode)!=null){
                adfEvaluationParams.put(seriesCode,evaluationParams.get(seriesCode));  //pass long the series
            }
            for (String series:seriesList){
                adfEvaluationParams.put(series,evaluationParams.get(series));  //pass long the series
            }


            Object evalResult = null;
          try {
              evalResult = regimeRoot.evaluate(ignoreCurrent, regime, adfEvaluationParams, adfs, library, 1, maxLevel); //Star level count again
          }catch (Exception e){
              throw  e;
          }

        if (logger.isTraceEnabled()) {
            logger.trace("returning " + evalResult);
        }
            return evalResult;

    }

    public void setTerminalSet(TerminalSet terminalSet) {
        this.terminalSet = terminalSet;
    }

    public TerminalSet getSymbolicParameters() {
        return symbolicParameters;
    }

    public void setSymbolicParameters(TerminalSet symbolicParameters) {
        this.symbolicParameters = symbolicParameters;
    }

    public FunctionSet getFunctionSet() {
        //Always return parameter arguments with functinset
        return functionSet;
    }

    public void setFunctionSet(FunctionSet functionSet) {
        this.functionSet = functionSet;
    }

    public AdfImpl(int arity) {
        this.arity = arity;
        parameters = new Primitive[arity];

    }



    @Override
    public String asLanguageString(int level,Integer maxLevel)  {
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for (int i = 0; i < level; i++) {
            sb.append('\t');
        }
        sb.append('(');
        sb.append(name).append(' ');
        for (int i = 0; i < parameters.length; i++) {
            sb.append(parameters[i].asLanguageString(level + 1,maxLevel)).append(' ');
        }
        //sb.append("\windowSize");
        //for (int i = 0;i<level;i++){
        //    sb.append("\t");
        // }
        sb.append(')');
        return sb.toString();
    }


    @Override
    public GP_TYPES[] getParameterReturnTypes() {         //TODO could cache this
        GP_TYPES[] gp_types = new GP_TYPES[arity];
        for (int i = 0;i<arity;i++){
            gp_types[i] = root[0].getReturnType();
        }
        return gp_types;
    }

    public Integer getArity(){
        return arity;
    }



    @Override
    public Primitive newInstance(List<String> series) {
        //real
        Adf adf = new AdfImpl(arity);
        adf.setRoot(root);
        adf.setName(name);
        adf.setTerminalSet(terminalSet);
        adf.setSymbolicParameters(symbolicParameters);
        adf.setFunctionSet(functionSet);
        return adf;
    }

    @Override
    public Primitive getRoot(int regime) {
        return root[regime];
    }

    @Override
    public void setRoot(Primitive[] primitive) {
        root = primitive;

    }

    @Override
    public void setRoot(Primitive primitive, int regime) {
        root[regime]=primitive;
    }

    @Override
    protected String getRepresentation(int maxLevel) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Primitive primitive : parameters) {

                sb.append(primitive.asLanguageString(0,maxLevel)).append(' ');


        }

        sb.append(')');

        return sb.toString();
    }

    @Override
    public GP_TYPES getReturnType() {
        //Note all regime implementation sneed to be of the same ype
        return root[0].getReturnType();
    }


    @Override
    public Primitive simplify() {
        return  this;

    }
}
