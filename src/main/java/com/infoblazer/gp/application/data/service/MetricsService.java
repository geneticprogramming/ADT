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

package com.infoblazer.gp.application.data.service;

import com.infoblazer.gp.application.data.jdbc.TrainingDataDao;
import com.infoblazer.gp.application.data.model.FitnessEvaluation;
import com.infoblazer.gp.application.data.model.Metrics;
import com.infoblazer.gp.application.data.model.XYArray;
import com.infoblazer.gp.application.data.model.jpa.*;
import com.infoblazer.gp.application.data.repository.*;
import com.infoblazer.gp.application.fitness.FitnessEvaluator;
import com.infoblazer.gp.application.syntheticdata.XYSeries;
import com.infoblazer.gp.evolution.geneticprogram.PredictionState;

import com.infoblazer.gp.evolution.model.AbstractProgram;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;

/**
 * Created by David on 9/13/2015.
 */
@Service
public class MetricsService {

    private final static Logger logger = Logger.getLogger(MetricsService.class.getName());

  @Autowired
    TrainingDataDao trainingDataDao;
    @Autowired
    private GpRunRepository gpRunRepository;
    @Autowired
    private PredictionDataRepository predictionDataRepository;
    @Autowired
    private PredictionRepository predictionRepository;
    @Autowired
    private TrainingDataRepository trainingDataRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    TrainingResultBranchRepository trainingResultBranchRepository;
    @Autowired
    TrainingRegimeBranchRepository trainingRegimeBranchRepository;
    @Autowired
    PredictionResultBranchRepository predictionResultBranchRepository;
    @Autowired
    PredictionRegimeBranchRepository predictionRegimeBranchRepository;
    @Value("${target}")
    String target;

    @Value("${programType}")
    String programType;
    private String applicationName;
    @Value("${logMetrics:#{false}}")
    private boolean logMetrics;

    @Value("${description:#{null}}")
    private String description;


    /**
     * @return id of new run
     */
    public Integer recordNewRun() {
        Integer logId = null;
        if (logMetrics) {
            GPRun gpRun = new GPRun();
            gpRun.setApplicationName(applicationName);
            gpRun.setProgramType(programType);
            gpRun.setXyseriesTitle(target);
            gpRun.setRunDate(new Date());
            gpRun.setDescription(description);
            gpRunRepository.save(gpRun);
            logId = gpRun.getId();

        }
        return logId;
    }

    public void endRun(Integer runId) {
        if (logMetrics) {
            GPRun xySeriesRun = gpRunRepository.findOne(runId);
            xySeriesRun.setEndDate(new Date());
            gpRunRepository.save(xySeriesRun);

        }
    }

    public void addMetric(Integer metricId) {

    }

    public void addTraining(boolean isPrediction, Integer metricId, int testingGeneration, int generation, Date startTime, Date endTime,
                            FitnessEvaluation fitnessEvaluation,
                            XYSeries targetSeries,
                            Metrics metrics, Metrics regimeMetrics, AbstractProgram program, AbstractProgram regimeProgram) {
        //add the current xy best regression for a training generation
        if (logMetrics) {

            Training training = new Training(isPrediction);
            training.setGpRunId(metricId);
            training.setGeneration(generation);
            training.setIteration(testingGeneration);
            training.setTrainingStart(startTime);
            training.setTrainingEnd(endTime);
            training.addMetrics(metrics);
            training.setNodeEvaluations(EvaluationLogger.getAndResetEvaluations());



            try {
                trainingRepository.save(training);
            } catch (Exception e) {
                logger.error("Erro saving training. Aborting save metrics ", e);
                return;

            }

            String programString = program.asLanguageString(100);
            TrainingResultBranch trainingResultBranch = new TrainingResultBranch();
            trainingResultBranch.setTrainingId(training.getId());
            trainingResultBranch.setFittestProgram(programString);
            trainingResultBranch.addMetrics(metrics);
            //save result training
            try {
                trainingResultBranchRepository.save(trainingResultBranch);
            } catch (Exception e) {
                logger.error("Erro saving trainingResultBranch " + trainingResultBranch.toString(), e);

            }

            if (regimeProgram != null) {
                TrainingRegimeBranch trainingRegimeBranch = new TrainingRegimeBranch();
                trainingRegimeBranch.setTrainingId(training.getId());
                String regimeProgramString = regimeProgram.asLanguageString(100);
                trainingRegimeBranch.setFittestProgram(regimeProgramString);
                trainingRegimeBranch.addMetrics(regimeMetrics);
                //save regime training
                try {
                    trainingRegimeBranchRepository.save(trainingRegimeBranch);
                } catch (Exception e) {
                    logger.error("Erro saving trainingRegimeBranch " + trainingRegimeBranch.toString(), e);
                }

            }



            XYArray xyArray = fitnessEvaluation.getXyArray();
          XYArray regimeArray = fitnessEvaluation.getRegimeXyArray();

            //save regime training

            int pos = 0;
            List<TrainingData> trainingDataList = new ArrayList<>();
            Boolean isNumericData = null;
            for (Object xVal : xyArray.getxVals()) {
                if (xVal != null) {
                    if (isNumericData==null){
                        isNumericData = xVal instanceof Double;
                    }
                    TrainingData trainingData = new TrainingData();
                    trainingData.setTrainingId(training.getId());

                    if (isNumericData) {
                        trainingData.setX((Double) xVal);
                    }
                    Double yPredicted = xyArray.getY(pos);
                    if (validDouble(yPredicted)) {
                        trainingData.setyPredicted(yPredicted);
                    } else {
                        logger.debug("Not a valid double predicted" + yPredicted);
                    }
                    trainingData.setRegime(regimeArray.getY(pos).intValue());
                    trainingData.setyActual(targetSeries.getY(pos));
                    trainingDataList.add(trainingData);
                }
                pos++;
            }
            if (trainingDataList.size() > 0) {
                try {
                    trainingDataDao.insert(trainingDataList);

                } catch (Exception e) {
                    logger.error("Erro saving trainingDataRepository ", e);
                }
            }


        }

    }


    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }


    public Integer recordNewPrediction(Integer metricId) {
        Integer predictionId = null;
        if (logMetrics) {
            Prediction xySeriesPrediction = new Prediction();
            xySeriesPrediction.setGpRunId(metricId);
            xySeriesPrediction.setPredictionStart(new Date());

            predictionRepository.save(xySeriesPrediction);
            predictionId = xySeriesPrediction.getId();


        }
        return predictionId;
    }

    public void addPrediction(Integer predictionId, PredictionState predictionState, int x, Object xVal, Double prediction, Double actual, int invalidPredictionCount,
                              int predictionGenerations, Metrics metrics, AbstractProgram program, AbstractProgram regimeProgram) {
        if (logMetrics) {
            String programString = program.asLanguageString(100);
            String regimeProgramString = null;
            if (regimeProgram != null) {
                regimeProgramString= regimeProgram.asLanguageString(100);
            }


            PredictionData predictionData = new PredictionData();




            predictionData.setPredictionId(predictionId);

            predictionData.setX(Double.valueOf(x));
            if (validDouble(prediction)) {
                predictionData.setyPredicted(prediction);
            }
            predictionData.setyActual(actual);

            


            predictionData.setCreated_on(new Date());
            predictionData.setFittestProgram(programString);
            predictionData.setFittestRegimeProgram(regimeProgramString);
            predictionData.setInvalidPredictionCount( invalidPredictionCount);
            predictionData.setPredictionGenerations(predictionGenerations);
            predictionData.addMetrics(metrics);

            try {
                predictionDataRepository.save(predictionData);
            } catch (Exception e) {
                logger.error("Error saving predictionData ", e);
            }


        }
    }

    public void endPrediction(Integer predictionId, Metrics metrics, Metrics regimeMetrics) {
        if (logMetrics) {
            Prediction prediction = predictionRepository.findOne(predictionId);
            prediction.addMetrics(metrics);
            prediction.setPredictionEnd(new Date());
            try {
                predictionRepository.save(prediction);
            } catch (Exception e) {
                logger.error("Erro saving prediction ", e);
            }

            PredictionResultBranch predictionResultBranch = new PredictionResultBranch();
            predictionResultBranch.setPredictionId(prediction.getId());
            predictionResultBranch.addMetrics(metrics);
            try {
                predictionResultBranchRepository.save(predictionResultBranch);
            } catch (Exception e) {
                logger.error("Erro saving predictionResultBranch ", e);
            }


            PredictionRegimeBranch predictionRegimeBranch = new PredictionRegimeBranch();
            predictionRegimeBranch.setPredictionId(prediction.getId());
            predictionRegimeBranch.addMetrics(regimeMetrics);
            try {
                predictionRegimeBranchRepository.save(predictionRegimeBranch);
            } catch (Exception e) {
                logger.error("Erro saving predictionRegimeBranch ", e);
            }


        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    private boolean validDouble(Double val) {
        boolean result = false;

        if (val != null &&
                !val.isNaN() &&
                !val.isInfinite() &&
                val < Double.MAX_VALUE &&
                val > (-1*Double.MAX_VALUE)) {
            result = true;
        }
        return result;


    }
}

