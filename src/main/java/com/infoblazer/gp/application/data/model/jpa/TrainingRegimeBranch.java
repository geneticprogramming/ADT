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
public class TrainingRegimeBranch  extends AbstractFitnessMetrics  implements HasFitnessMetrics {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private Integer trainingId;
    @Column
    private Double bestFitness;
    @Column
    private Double medianFitness;
    @Column
    private Double meanFitness;
    @Column
    private Double varianceFitness;
    @Column
    private Double stddevFitness;
    @Column
    private Integer populationSize;
    @Column
    private Integer invalidPopulationSize;
    @Column
    private Integer libraryPopulationSize;

    @Column
    private Double medianDepth;
    @Column
    private Double meanDepth;
    @Column
    private Double varianceDepth;
    @Column
    private Double stddevDepth;
    @Column
    private String fittestProgram;
    @Column
    private Double medianNodecount;
    @Column
    private Double meanNodecount;
    @Column
    private Double varianceNodecount;
    @Column
    private Double stddevNodecount;
    @Column
    private Integer totalNodecount;
    @Column
    private Integer totalAdfNodecount;
    @Column
    private Integer fittestNodecount;
    @Column
    private Integer fittestAdfNodecount;
    @Column
    private Integer fittestDepth;
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

    public Integer getTrainingId() {
        return this.trainingId;
    }

    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }

    public Integer getLibraryPopulationSize() {
        return this.libraryPopulationSize;
    }

    public void setLibraryPopulationSize(Integer libraryPopulationSize) {
        this.libraryPopulationSize = libraryPopulationSize;
    }

    public Integer getFittestAdfNodecount() {
        return this.fittestAdfNodecount;
    }

    public void setFittestAdfNodecount(Integer fittestAdfNodecount) {
        this.fittestAdfNodecount = fittestAdfNodecount;
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

    public Integer getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    public Integer getTotalNodecount() {
        return this.totalNodecount;
    }

    public void setTotalNodecount(Integer totalNodecount) {
        this.totalNodecount = totalNodecount;
    }

    public Integer getTotalAdfNodecount() {
        return this.totalAdfNodecount;
    }

    public void setTotalAdfNodecount(Integer totalAdfNodecount) {
        this.totalAdfNodecount = totalAdfNodecount;
    }

    public Double getMedianDepth() {
        return medianDepth;
    }

    public void setMedianDepth(Double medianDepth) {
        this.medianDepth = medianDepth;
    }

    public Double getMeanDepth() {
        return meanDepth;
    }

    public void setMeanDepth(Double meanDepth) {
        this.meanDepth = meanDepth;
    }

    public Double getVarianceDepth() {
        return varianceDepth;
    }

    public void setVarianceDepth(Double varianceDepth) {
        this.varianceDepth = varianceDepth;
    }

    public Double getStddevDepth() {
        return stddevDepth;
    }

    public void setStddevDepth(Double stddevDepth) {
        this.stddevDepth = stddevDepth;
    }



    public String getFittestProgram() {
        return fittestProgram;
    }

    public void setFittestProgram(String fittestProgram) {
        this.fittestProgram = fittestProgram;
    }

    public Double getMedianNodecount() {
        return medianNodecount;
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

    public Integer getInvalidPopulationSize() {
        return this.invalidPopulationSize;
    }

    public void setInvalidPopulationSize(Integer invalidPopulationSize) {
        this.invalidPopulationSize = invalidPopulationSize;
    }

    @Override
    public String toString() {
        return "TrainingRegimeBranch{" +
                "bestFitness=" + bestFitness +
                ", medianFitness=" + medianFitness +
                ", meanFitness=" + meanFitness +
                ", varianceFitness=" + varianceFitness +
                ", stddevFitness=" + stddevFitness +
                ", populationSize=" + populationSize +
                ", invalidPopulationSize=" + invalidPopulationSize +
                ", libraryPopulationSize=" + libraryPopulationSize +
                ", medianDepth=" + medianDepth +
                ", meanDepth=" + meanDepth +
                ", varianceDepth=" + varianceDepth +
                ", stddevDepth=" + stddevDepth +
                ", medianNodecount=" + medianNodecount +
                ", meanNodecount=" + meanNodecount +
                ", varianceNodecount=" + varianceNodecount +
                ", stddevNodecount=" + stddevNodecount +
                ", fittestNodecount=" + fittestNodecount +
                ", fittestAdfNodecount=" + fittestAdfNodecount +
                ", fittestDepth=" + fittestDepth +
                ", medianAdfNodecount=" + medianAdfNodecount +
                ", meanAdfNodecount=" + meanAdfNodecount +
                ", varianceAdfNodecount=" + varianceAdfNodecount +
                ", stddevAdfNodecount=" + stddevAdfNodecount +
                '}';
    }

    public void addMetrics(Metrics metrics) {
        bestFitness=safeAddMetric(metrics.getBestFitness());
        medianFitness=safeAddMetric(metrics.getMedianFitness());
        meanFitness=safeAddMetric(metrics.getMeanFitness());
        varianceFitness=safeAddMetric(metrics.getVarianceFitness());
        stddevFitness=safeAddMetric(metrics.getStddevFitness());
        populationSize=metrics.getPopulationSize();
        libraryPopulationSize = metrics.getLibraryPopulationSize();
        invalidPopulationSize =metrics.getInvalidPopulationSize();


        medianDepth=safeAddMetric(metrics.getMedianDepth());
        meanDepth=safeAddMetric(metrics.getMeanDepth());
        varianceDepth=safeAddMetric(metrics.getVarianceDepth());
        stddevDepth=safeAddMetric(metrics.getStddevDepth());
        medianNodecount=safeAddMetric(metrics.getMedianNodeCount());
        meanNodecount=safeAddMetric(metrics.getMeanNodeCount());
        varianceNodecount=safeAddMetric(metrics.getVarianceNodeCount());
        stddevNodecount=safeAddMetric(metrics.getStddevNodeCount());
        totalNodecount = metrics.getTotalNodeCount();

        fittestNodecount=metrics.getFittestNodeCount();
        fittestAdfNodecount=metrics.getFittestAdfNodeCount();
        totalAdfNodecount = metrics.getTotalAdfNodeCount();
        fittestDepth=metrics.getFittestDepth();

        meanAdfNodecount = safeAddMetric(metrics.getMeanAdfNodeCount());
        medianAdfNodecount = safeAddMetric(metrics.getMedianAdfNodeCount());
        varianceAdfNodecount  = safeAddMetric(metrics.getVarianceAdfNodeCount());
        stddevAdfNodecount = safeAddMetric(metrics.getStddevAdfNodeCount());




    }
}
