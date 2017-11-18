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
public class Prediction  extends AbstractFitnessMetrics  implements HasFitnessMetrics{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column  private Integer id;
    @Column  private Integer GpRunId;
    @Column  private Date predictionStart;
    @Column  private Date predictionEnd;



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

    public Date getPredictionStart() {
        return this.predictionStart;
    }

    public void setPredictionStart(Date predictionStart) {
        this.predictionStart = predictionStart;
    }

    public Date getPredictionEnd() {
        return this.predictionEnd;
    }

    public void setPredictionEnd(Date predictionEnd) {
        this.predictionEnd = predictionEnd;
    }

    public void addMetrics(Metrics metrics) {

    }
}
