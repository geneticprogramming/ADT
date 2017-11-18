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

package com.infoblazer.gp.application.data.model.jpa;

import com.infoblazer.gp.application.data.model.Metrics;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by David on 9/17/2015.
 */
@Entity
@Table
public class PredictionData  extends AbstractFitnessMetrics  implements HasFitnessMetrics {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private Integer predictionId;
    @Column
    private Double x;
    @Column
    private Double yActual;
    @Column
    private Double yPredicted;
    @Column
    private Integer regime;
    @Column private Integer invalidPredictionCount;

    @Column private Integer predictionGenerations;

    @Column
    private String fittestProgram;
    @Column
    private String fittestRegimeProgram;
    @Column
    private Integer fittestNodecount;
    @Column
    private Integer fittestAdfNodecount;
    @Column
    private Integer fittestDepth;

    @Column
    private Date created_on;

    public Integer getInvalidPredictionCount() {
        return this.invalidPredictionCount;
    }

    public void setInvalidPredictionCount(Integer invalidPredictionCount) {
        this.invalidPredictionCount = invalidPredictionCount;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getPredictionId() {
        return this.predictionId;
    }

    public void setPredictionId(Integer predictionId) {
        this.predictionId = predictionId;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getyActual() {
        return yActual;
    }

    public void setyActual(Double yActual) {
        this.yActual = yActual;
    }

    public Double getyPredicted() {
        return yPredicted;
    }

    public void setyPredicted(Double yPredicted) {
        this.yPredicted = yPredicted;
    }

    public Integer getRegime() {
        return regime;
    }

    public void setRegime(Integer regime) {
        this.regime = regime;
    }




    public String getFittestProgram() {
        return fittestProgram;
    }

    public void setFittestProgram(String fittestProgram) {
        this.fittestProgram = fittestProgram;
    }



    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    public Integer getFittestNodecount() {
        return this.fittestNodecount;
    }

    public void setFittestNodecount(Integer fittestNodecount) {
        this.fittestNodecount = fittestNodecount;
    }

    public Integer getFittestDepth() {
        return this.fittestDepth;
    }

    public void setFittestDepth(Integer fittestDepth) {
        this.fittestDepth = fittestDepth;
    }

    public Integer getPredictionGenerations() {
        return this.predictionGenerations;
    }

    public void setPredictionGenerations(Integer predictionGenerations) {
        this.predictionGenerations = predictionGenerations;
    }


    public Integer getFittestAdfNodecount() {
        return this.fittestAdfNodecount;
    }

    public void setFittestAdfNodecount(Integer fittestAdfNodecount) {
        this.fittestAdfNodecount = fittestAdfNodecount;
    }

    public String getFittestRegimeProgram() {
        return this.fittestRegimeProgram;
    }

    public void setFittestRegimeProgram(String fittestRegimeProgram) {
        this.fittestRegimeProgram = fittestRegimeProgram;
    }

    public void addMetrics(Metrics metrics) {

        regime = metrics.getRegime();

        fittestNodecount = metrics.getFittestNodeCount();
        fittestAdfNodecount = metrics.getFittestAdfNodeCount();
        fittestDepth = metrics.getFittestDepth();


    }
}



