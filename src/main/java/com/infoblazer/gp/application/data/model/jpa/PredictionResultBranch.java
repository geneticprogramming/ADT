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
public class PredictionResultBranch extends AbstractFitnessMetrics implements HasFitnessMetrics{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column  private Integer id;
    @Column  private Integer PredictionId;

    @Column  private Double bestFitness;
    @Column  private Double medianFitness;
    @Column  private Double meanFitness;
    @Column  private Double varianceFitness;
    @Column  private Double stddevFitness;
    @Column
    private Double medianNodecount;
    @Column
    private Double meanNodecount;
    @Column
    private Double varianceNodecount;
    @Column
    private Double stddevNodecount;

    @Column
    private Double medianDepth;
    @Column
    private Double meanDepth;
    @Column
    private Double varianceDepth;
    @Column
    private Double stddevDepth;

    @Column
    private Double medianAdfNodecount;
    @Column
    private Double meanAdfNodecount;
    @Column
    private Double varianceAdfNodecount;
    @Column
    private Double stddevAdfNodecount;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPredictionId() {
        return this.PredictionId;
    }

    public void setPredictionId(Integer predictionId) {
        this.PredictionId = predictionId;
    }

    public Double getBestFitness() {
        return bestFitness;
    }

    public void setBestFitness(Double bestFitness) {
        this.bestFitness = bestFitness;
    }

    public Double getMedianFitness() {
        return medianFitness;
    }

    public void setMedianFitness(Double medianFitness) {
        this.medianFitness = medianFitness;
    }

    public Double getMeanFitness() {
        return meanFitness;
    }

    public void setMeanFitness(Double meanFitness) {
        this.meanFitness = meanFitness;
    }

    public Double getVarianceFitness() {
        return varianceFitness;
    }

    public void setVarianceFitness(Double varianceFitness) {
        this.varianceFitness = varianceFitness;
    }

    public Double getStddevFitness() {
        return stddevFitness;
    }

    public void setStddevFitness(Double stddevFitness) {
        this.stddevFitness = stddevFitness;
    }

    public Double getMedianNodecount() {
        return this.medianNodecount;
    }

    public void setMedianNodecount(Double medianNodecount) {
        this.medianNodecount = medianNodecount;
    }

    public Double getMeanNodecount() {
        return this.meanNodecount;
    }

    public void setMeanNodecount(Double meanNodecount) {
        this.meanNodecount = meanNodecount;
    }

    public Double getVarianceNodecount() {
        return this.varianceNodecount;
    }

    public void setVarianceNodecount(Double varianceNodecount) {
        this.varianceNodecount = varianceNodecount;
    }

    public Double getStddevNodecount() {
        return this.stddevNodecount;
    }

    public void setStddevNodecount(Double stddevNodecount) {
        this.stddevNodecount = stddevNodecount;
    }

    public Double getMedianDepth() {
        return this.medianDepth;
    }

    public void setMedianDepth(Double medianDepth) {
        this.medianDepth = medianDepth;
    }

    public Double getMeanDepth() {
        return this.meanDepth;
    }

    public void setMeanDepth(Double meanDepth) {
        this.meanDepth = meanDepth;
    }

    public Double getVarianceDepth() {
        return this.varianceDepth;
    }

    public void setVarianceDepth(Double varianceDepth) {
        this.varianceDepth = varianceDepth;
    }

    public Double getStddevDepth() {
        return this.stddevDepth;
    }

    public void setStddevDepth(Double stddevDepth) {
        this.stddevDepth = stddevDepth;
    }

    public Double getMedianAdfNodecount() {
        return this.medianAdfNodecount;
    }

    public void setMedianAdfNodecount(Double medianAdfNodecount) {
        this.medianAdfNodecount = medianAdfNodecount;
    }

    public Double getMeanAdfNodecount() {
        return this.meanAdfNodecount;
    }

    public void setMeanAdfNodecount(Double meanAdfNodecount) {
        this.meanAdfNodecount = meanAdfNodecount;
    }

    public Double getVarianceAdfNodecount() {
        return this.varianceAdfNodecount;
    }

    public void setVarianceAdfNodecount(Double varianceAdfNodecount) {
        this.varianceAdfNodecount = varianceAdfNodecount;
    }

    public Double getStddevAdfNodecount() {
        return this.stddevAdfNodecount;
    }

    public void setStddevAdfNodecount(Double stddevAdfNodecount) {
        this.stddevAdfNodecount = stddevAdfNodecount;
    }



    public void addMetrics(Metrics metrics) {
        this.bestFitness = safeAddMetric(metrics.getBestFitness());
        this.meanFitness=safeAddMetric(metrics.getMeanFitness());
        this.medianFitness=safeAddMetric(metrics.getMedianFitness());
        this.varianceFitness=safeAddMetric(metrics.getVarianceFitness());
        this.stddevFitness=safeAddMetric(metrics.getStddevFitness());
        this.meanNodecount=safeAddMetric(metrics.getMeanNodeCount());
        this.medianNodecount=safeAddMetric(metrics.getMedianNodeCount());
        this.varianceNodecount=safeAddMetric(metrics.getVarianceNodeCount());
        this.stddevNodecount=safeAddMetric(metrics.getStddevNodeCount());
        this.meanDepth=safeAddMetric(metrics.getMeanDepth());
        this.medianDepth=safeAddMetric(metrics.getMedianDepth());
        this.varianceDepth=safeAddMetric(metrics.getVarianceDepth());
        this.stddevDepth=safeAddMetric(metrics.getStddevDepth());
        this.meanAdfNodecount = safeAddMetric(metrics.getMeanAdfNodeCount());
        this.medianAdfNodecount = safeAddMetric(metrics.getMedianAdfNodeCount());
        this.varianceAdfNodecount  = safeAddMetric(metrics.getVarianceAdfNodeCount());
        this.stddevAdfNodecount = safeAddMetric(metrics.getStddevAdfNodeCount());


    }


}
