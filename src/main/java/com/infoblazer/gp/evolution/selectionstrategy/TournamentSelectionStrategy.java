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

package com.infoblazer.gp.evolution.selectionstrategy;

import com.infoblazer.gp.application.fitness.AbstractFitnessEvaluator;
import com.infoblazer.gp.evolution.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**  Random regression can be built with tournament of 1
 * Created by David on 8/7/2014.
 */
@Component
public class TournamentSelectionStrategy extends AbstractSelectionStrategy implements SelectionStrategy {
    private final static Logger logger = Logger.getLogger(TournamentSelectionStrategy.class.getName());


    Integer selectionPressure;
    public TournamentSelectionStrategy() {
    }




    protected ResultProducingProgram selectWinners(Population population) {


        ResultProducingProgram resultProducingProgram = (ResultProducingProgram) runTournament(population.getResultPopulation(), tournamentSize);
        return resultProducingProgram;
    }

    protected AbstractProgram runTournament(List<? extends AbstractProgram> programs, int participantCount) {

        AbstractProgram[] participants = new AbstractProgram[participantCount];
        Collection<Integer> addedSet =null;
        if (programs.size()>=participantCount){
            addedSet =   new HashSet<>();
        }else{
            addedSet =   new ArrayList<>();
        }
        int added = 0;
        while  (addedSet.size() < participantCount) {
            int p = random.nextInt(programs.size());
            if (programs.size()<participantCount || !addedSet.contains(p)) {
                participants[added] = programs.get(p);
                addedSet.add(p);
                added++;
            }
        }

        Double bestFitness = participants[0].getFitness();
        AbstractProgram winner = participants[0];
        for (int i = 1; i < participantCount; i++) {
            Double fitness = participants[i].getFitness();

            Boolean comparison = AbstractFitnessEvaluator.isFitter(fitness, bestFitness, direction);
            if (comparison == null) {
                if (AbstractFitnessEvaluator.isProbablyBetterProgramNoNulls(participants[i], winner)) {
                    winner = participants[i];
                    bestFitness = fitness;
                }
            } else if (comparison) {
                winner = participants[i];
                bestFitness = fitness;

            }

        }
        return winner;
    }



}
