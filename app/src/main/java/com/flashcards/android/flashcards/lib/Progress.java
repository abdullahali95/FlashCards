package com.flashcards.android.flashcards.lib;

import com.google.common.collect.EvictingQueue;

import java.util.Iterator;
import java.util.List;

import static java.lang.Boolean.TRUE;

public class Progress {

    //Invariant: must always stay positive
    private int attempts;

    private int correct;
    private EvictingQueue<Boolean> lastTen;     // Pushes out old elements if size exceeds 10
    private int learntScore;


    public Progress() {
        this.attempts = 0;
        this.correct = 0;
        this.lastTen = EvictingQueue.create(10);
        this.learntScore = 500;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) throws IllegalAccessException {
        if (attempts < 0) {
            throw new IllegalAccessException("Attempts must be a positive integer");
        }
        this.attempts = attempts;
    }

    public void incAttempts() {
        attempts++;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public void incCorrect() {
        correct++;
    }

    public EvictingQueue<Boolean> getLastTen() {
        return lastTen;
    }

    public void setLastTen(List<Boolean> list) {
        lastTen.addAll(list);
    }

    public void addAnswer(Boolean answer) {
        lastTen.offer(answer);
    }

    public int getLearntScore() {
        return learntScore;
    }

    public void setLearntScore(int learntScore) {
        this.learntScore = learntScore;
    }

    /**
     * Uses the lastTen list to calculate the ratio of correct answers.
     * @return Ratio of correct answers.
     */
    public double ratioCorrect() {
        int attempts = 10 - lastTen.remainingCapacity();
        int correct = 0;
        for (Iterator i = lastTen.iterator(); i.hasNext(); ) {
            if(i.next() == TRUE) correct++;
        }

        return ((double) correct)/attempts;
    }


    /** Attempts to lower learntScore for cards which have not been viewed often.
     * Uses the function: 1 - (1/(x+1))
     * To save computation, after 30 attempts, 1 is returned.
     *
     * Preconditions: attempts must be a positive number
     * @return The reflex score, between 0 and 1 (inclusive)
     */
    public double reflexScore() {
        int x = attempts;
        double score = 1 - ( (double) 1/x+1);
        return score;
    }

    //TODO: test the learntScore and it's initialisd value
    /** Generates the 'Learnt Score' for the flashcard and updates the local variable
     *
     * @return Learnt score
     */
    public int generateLearntScore() {
        if (attempts == 0) {
            return 500;
        } else {

            double rand = Math.random();
            double m = reflexScore();
            double l = ratioCorrect();

            double s = (2 * l * m) + rand / (double) 3;
            int score = (int) Math.round(s * 1000);

            learntScore = score;
            return score;
        }
    }



}
