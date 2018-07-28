package com.flashcards.android.flashcards.lib.model;

import android.util.Log;

import com.google.common.collect.EvictingQueue;

import java.util.Iterator;

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
     * Uses the lastFive list to calculate the ratio of correct answers.
     * The values simulate the decks of cards in Leitner system. The decks are valued as follows:
     * 0-1 correct = 0.2
     * 2 correct = 0.4
     * 3 correct = 0.6
     * 4 correct = 0.8
     * 5 correct = 1.0 (card learnt)
     *
     * @return Customised Leitner Score.
     * PostConditions: The leitner score should be between 0.2 - 1.0
     */
    public static double leitnerScore (int total, EvictingQueue<Boolean> lastFive) {
    	if (total == 0) return 0.2;
    	
        else {

            //TODO: fix correct - stuck at counting to 1. Possibly issue with converter
	        int correct = 0;
	        for (Iterator<Boolean> i = lastFive.iterator(); i.hasNext(); ) {
	            if(i.next() == TRUE) correct++;

	        }

	        // 5 is the max size of the Queue
	        double ls = ((double) correct)/5;

            Log.d(String.valueOf(correct), "Correct: ");
            Log.d(String.valueOf(ls), "Leanrt Score: ");

	        // If there are no true answers, min value should be 0.2
	        return (ls > 0)? ls : 0.2;
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
            return 1;         // First value doesn't matter, as only uses random
        } else if ( attempts > 20) {
            return 1;           // Value close to 1, so saves calculation.
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
            
            double l = leitnerScore(card.getAttempts(), card.getLastFive());
            Log.d(String.valueOf(l), card.toString());
            
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
