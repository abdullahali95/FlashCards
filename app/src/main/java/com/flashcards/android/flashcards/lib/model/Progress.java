package com.flashcards.android.flashcards.lib.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.google.common.collect.EvictingQueue;

import java.util.Iterator;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;
import static java.lang.Boolean.TRUE;
import static java.lang.Math.random;

/**
 * Created by Abdullah Ali
 *
 * Allows the performance of the cards to be measured.
 * Used to calculate the next most appropriate card to test.
 *
 */

public class Progress {

    /**
     * Uses the lastTen list to calculate the ratio of correct answers.
     * @return Ratio of correct answers.
     */
    public static double ratioCorrect(int total, EvictingQueue<Boolean> lastTen) {
        // If less then 10 attempts, this score shouldn't matter, as it may be too inaccurate
    	if (total < 10) return 1;
    	
        else {
	    	int attempts = 10 - lastTen.remainingCapacity();
	        int correct = 0;
	        for (Iterator<Boolean> i = lastTen.iterator(); i.hasNext(); ) {
	            if(i.next() == TRUE) correct++;
	        }
	
	        return ((double) correct)/attempts;
        }
    }


    /** Attempts to lower learntScore for cards which have not been viewed often.
     * Uses the function: 1 - (1/(x+1))
     * To save computation, after 30 attempts, 1 is returned.
     *
     * Preconditions: attempts must be a positive number
     * @return The reflex score, between 0 and 1 (inclusive)
     */
    public static double reflexScore(int attempts) {
    	if (attempts == 0) {
            return 0.5;         // Initial number irrelevant as not used in learntScore
        } else {
	    	int x = attempts;
	        double score = 1 - ( (double) 1/(x+1));
	        return score;
        }
    }

    //TODO: test the learntScore and it's initialisd value
    /** Generates the 'Learnt Score' for the flashcard and updates the local variable
     *
     * @return Learnt score
     */
    public static int generateLearntScore(int sizeOfDeck, Card card) {
        int attempts = card.getAttempts();
        int scale = sizeOfDeck * 5;     // Determines the variation in the scores

        if (attempts == 0) {

            // Initialised with values between 400 and 600
            int initialVal =(int) ( ((double) Math.random() * (scale*0.2)) + (scale*0.4));
            card.setLearntScore(initialVal);
            return initialVal;
        } else {

            double rand = random();
            
            double m = reflexScore(card.getAttempts());
            
            double l = ratioCorrect(card.getAttempts(), card.getLastTen());
            
            // The effect of random function should vary depending on size of deck
            // The smaller the deck, the more random the deck should be shuffled.
            // The effect is determined by function y = (1.5/x)+0.25
            
            double randWeight = (1.5/sizeOfDeck) + 0.25;
            
            double s = ((l * m * (1-randWeight)) + (rand * randWeight))/2;

            int score = (int) Math.round(s * 2 * scale);

            card.setLearntScore(score);
            return score;
        }
    }



}
