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

package com.infoblazer.gp.application.data.model;

/**
 * Created by David on 9/15/2015.
 */
public class Metrics {

    private Integer populationSize;
    private Integer invalidPopulationSize;
    private  Integer libraryPopulationSize;


    private Integer regime;
    private Integer fitnessEvaluations;
    private Integer fitnessCalculations;
    private Double bestFitness;
    private Double medianFitness;
    private Double meanFitness;
    private Double varianceFitness;
    private Double stddevFitness;

    private Double medianDepth;
    private Double meanDepth;
    private Double varianceDepth;
    private Double stddevDepth;

    private Integer totalNodeCount;
    private Double medianNodeCount;
    private Double meanNodeCount;
    private Double varianceNodeCount;
    private Double stddevNodeCount;
    private Integer fittestNodeCount;
    private Integer fittestDepth;


    private Integer totalAdfNodeCount;
    private Double medianAdfNodeCount;
    private Double meanAdfNodeCount;
    private Double varianceAdfNodeCount;
    private Double stddevAdfNodeCount;
    private Integer fittestAdfNodeCount;
    private Integer fittestAdfDepth;


    public Integer getRegime() {
        return regime;
    }

    public void setRegime(Integer regime) {
        this.regime = regime;
    }

    public Double getBestFitness() {
        return bestFitness;
    }

    public void setBestFitness(Double bestFitness) {
        this.bestFitness = bestFitness;
    }

    public Double getMedianFitness() {
        return this.medianFitness;
    }

    public void setMedianFitness(Double medianFitness) {
        if (!medianFitness.isNaN()) {
            this.medianFitness = medianFitness.isNaN() ? null : medianFitness;
        }
    }

    public Double getMeanFitness() {
        return meanFitness;
    }

    public void setMeanFitness(Double meanFitness) {
        this.meanFitness = meanFitness.isNaN()?null:meanFitness;
    }

    public Double getVarianceFitness() {
        return varianceFitness;
    }

    public void setVarianceFitness(Double varianceFitness) {
        this.varianceFitness = varianceFitness.isNaN()?null:varianceFitness;
    }

    public Double getStddevFitness() {
        return stddevFitness;
    }

    public void setStddevFitness(Double stddevFitness) {
        this.stddevFitness = stddevFitness.isNaN()?null:stddevFitness;
    }

    public Integer getPopulationSize() {
        return this.populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    public Integer getLibraryPopulationSize() {
        return this.libraryPopulationSize;
    }

    public void setLibraryPopulationSize(Integer libraryPopulationSize) {
        this.libraryPopulationSize = libraryPopulationSize;
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

    public Integer getFitnessEvaluations() {
        return fitnessEvaluations;
    }

    public void setFitnessEvaluations(Integer fitnessEvaluations) {
        this.fitnessEvaluations = fitnessEvaluations;
    }

    public Double getMedianNodeCount() {
        return this.medianNodeCount;
    }

    public void setMedianNodeCount(Double medianNodeCount) {
        this.medianNodeCount = medianNodeCount;
    }

    public Double getMeanNodeCount() {
        return this.meanNodeCount;
    }

    public void setMeanNodeCount(Double meanNodeCount) {
        this.meanNodeCount = meanNodeCount;
    }

    public Double getVarianceNodeCount() {
        return this.varianceNodeCount;
    }

    public void setVarianceNodeCount(Double varianceNodeCount) {
        this.varianceNodeCount = varianceNodeCount;
    }

    public Double getStddevNodeCount() {
        return this.stddevNodeCount;
    }

    public void setStddevNodeCount(Double stddevNodeCount) {
        this.stddevNodeCount = stddevNodeCount;
    }

    public Integer getFittestNodeCount() {
        return this.fittestNodeCount;
    }

    public void setFittestNodeCount(Integer fittestNodeCount) {
        this.fittestNodeCount = fittestNodeCount;
    }

    public Integer getFittestDepth() {
        return this.fittestDepth;
    }

    public void setFittestDepth(Integer fittestDepth) {
        this.fittestDepth = fittestDepth;
    }

    public Integer getFitnessCalculations() {
        return this.fitnessCalculations;
    }

    public void setFitnessCalculations(Integer fitnessCalculations) {
        this.fitnessCalculations = fitnessCalculations;
    }

    public Double getMedianAdfNodeCount() {
        return this.medianAdfNodeCount;
    }

    public void setMedianAdfNodeCount(Double medianAdfNodeCount) {
        this.medianAdfNodeCount = medianAdfNodeCount;
    }

    public Double getMeanAdfNodeCount() {
        return this.meanAdfNodeCount;
    }

    public void setMeanAdfNodeCount(Double meanAdfNodeCount) {
        this.meanAdfNodeCount = meanAdfNodeCount;
    }

    public Double getVarianceAdfNodeCount() {
        return this.varianceAdfNodeCount;
    }

    public void setVarianceAdfNodeCount(Double varianceAdfNodeCount) {
        this.varianceAdfNodeCount = varianceAdfNodeCount;
    }

    public Double getStddevAdfNodeCount() {
        return this.stddevAdfNodeCount;
    }

    public void setStddevAdfNodeCount(Double stddevAdfNodeCount) {
        this.stddevAdfNodeCount = stddevAdfNodeCount;
    }

    public Integer getFittestAdfNodeCount() {
        return this.fittestAdfNodeCount;
    }

    public void setFittestAdfNodeCount(Integer fittestAdfNodeCount) {
        this.fittestAdfNodeCount = fittestAdfNodeCount;
    }

    public Integer getFittestAdfDepth() {
        return this.fittestAdfDepth;
    }

    public void setFittestAdfDepth(Integer fittestAdfDepth) {
        this.fittestAdfDepth = fittestAdfDepth;
    }

    public Integer getInvalidPopulationSize() {
        return this.invalidPopulationSize;
    }

    public void setInvalidPopulationSize(Integer invalidPopulationSize) {
        this.invalidPopulationSize = invalidPopulationSize;
    }

    public Integer getTotalNodeCount() {
        return this.totalNodeCount;
    }

    public void setTotalNodeCount(Integer totalNodeCount) {
        this.totalNodeCount = totalNodeCount;
    }

    public Integer getTotalAdfNodeCount() {
        return this.totalAdfNodeCount;
    }

    public void setTotalAdfNodeCount(Integer totalAdfNodeCount) {
        this.totalAdfNodeCount = totalAdfNodeCount;
    }
}
