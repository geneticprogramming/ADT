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
import com.infoblazer.gp.evolution.primitives.GP_TYPES;
import com.infoblazer.gp.evolution.primitives.Primitive;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by David on 6/11/2015.
 */
public class AatImpl extends AbstractFunction implements Aat {
    private boolean modified = false;
    private Integer libaryKey;

    private GP_TYPES returnType;
    private final static Logger logger = Logger.getLogger(AatImpl.class.getName());

    public AatImpl(Integer libaryKey) {
        this.libaryKey = libaryKey;
    }

    @Override
    public Integer getLibaryKey() {
        return this.libaryKey;
    }


    public AatImpl() {

    }

    @Override
    protected String getRepresentation(int MaxDepth) {
        return null;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String asLanguageString(int level, Integer maxLevel) {
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for (int i = 0; i < level; i++) {
            sb.append('\t');
        }
        sb.append("(AAT").append(libaryKey).append('\n');
        int regime = 0;
        for (Primitive primitive : parameters) {
            for (int i = 0; i < level; i++) {
                sb.append('\t');
            }
            regime++;
            sb.append('[').append(regime).append(": ");
            sb.append(primitive.asLanguageString(level, maxLevel));
            sb.append("]\n");
        }

        sb.append(')');
        return sb.toString();
    }

    @Override
    public Primitive newInstance(List<String> series) {
        return null;
    }

    //need access to the global library to evel this
    @Override
    public Object evaluate(boolean ignoreCurrent, Integer regime, Map<String, Object> parameters, Map<String, Adf> adfs, Library library, int level, Integer maxLevel) {

        EvaluationLogger.dataAccessOperation();
        Object result;
        if (level > maxLevel) {
            logger.debug("Hit max level evaluating AAT. Returning true/1");
            if (returnType == GP_TYPES.BOOLEAN) {
                result = true;
            } else {
                result = 1;
            }
        }   else {
            Primitive[] primitives = library.getPrimitiveById(libaryKey);
            result = primitives[regime].evaluate(ignoreCurrent, regime, parameters, adfs, library, level + 1, maxLevel);
        }
        return result;

    }

    @Override
    public GP_TYPES getReturnType() {

        return returnType;
    }

    public void setReturnType(GP_TYPES returnType) {
        this.returnType = returnType;
    }

    @Override
    public Primitive simplify() {
        return this;

    }

    public boolean isModified() {
        return this.modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    @Override
    public void setParams(String[] paramVals) {

    }

    @Override
    public GP_TYPES[] getParameterReturnTypes() {
        return new GP_TYPES[0];
    }
}
