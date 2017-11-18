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
public class Training extends AbstractFitnessMetrics implements HasFitnessMetrics {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public Training() {
    }

    public Training(Boolean inPrediction) {
        this.inPrediction = inPrediction;
    }

    @Column
    private Integer GpRunId;
    @Column
    private Date trainingStart;
    @Column
    private Date trainingEnd;
    @Column
    private Integer iteration;
    @Column
    private Integer generation;
    @Column
    private Integer fitnessEvaluations;

    @Column
    private Integer fitnessCalculations;
    @Column
    private Boolean inPrediction;
    @Column
    private Double nodeEvaluations;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGpRunId() {
        return this.GpRunId;
    }

    public void setGpRunId(Integer gpRunId) {
        this.GpRunId = gpRunId;
    }

    public Date getTrainingStart() {
        return this.trainingStart;
    }

    public void setTrainingStart(Date trainingStart) {
        this.trainingStart = trainingStart;
    }

    public Date getTrainingEnd() {
        return this.trainingEnd;
    }

    public void setTrainingEnd(Date trainingEnd) {
        this.trainingEnd = trainingEnd;
    }

    public Integer getIteration() {
        return this.iteration;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }

    public Integer getGeneration() {
        return this.generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
    }

    public Integer getFitnessEvaluations() {
        return this.fitnessEvaluations;
    }

    public void setFitnessEvaluations(Integer fitnessEvaluations) {
        this.fitnessEvaluations = fitnessEvaluations;
    }

    public Integer getFitnessCalculations() {
        return this.fitnessCalculations;
    }

    public void setFitnessCalculations(Integer fitnessCalculations) {
        this.fitnessCalculations = fitnessCalculations;
    }

    public Boolean isInPrediction() {
        return this.inPrediction;
    }

    public void setInPrediction(Boolean inPrediction) {
        this.inPrediction = inPrediction;
    }

    public Double getNodeEvaluations() {
        return this.nodeEvaluations;
    }

    public void setNodeEvaluations(Double nodeEvaluations) {
        this.nodeEvaluations = nodeEvaluations;
    }

    public void addMetrics(Metrics metrics) {
        fitnessEvaluations = metrics.getFitnessEvaluations();
        fitnessCalculations = metrics.getFitnessCalculations();

    }



}
