package com.flashcards.android.flashcards.lib.model;

import android.util.Log;

import com.google.common.collect.EvictingQueue;
import static java.lang.Boolean.TRUE;

/**
 * Created by Abdullah Ali
 *
 * This class contains multiple static methods for Card objects
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
     * PostConditions: The leitner score should be between 0.4 - 1.0
     */
    public static double leitnerScore (Card card) {
        int total = card.getAttempts();

        // Initial value slightly higher then min value (0.2). This is so:
        // Initial incorrect answers can be identified by the slightly lower then average leitner score
        if (total == 0) return 0.3;
        else {
	        int correct = correctFromLastFive(card);
	        // With the queue size 5, interval between each deck is 20%
            // Following equation makes this division
	        double ls = (0.2*(correct-1))+0.2;

	        // If there are no true answers, min value should be 0.2
	        return (ls >= 0.2)? ls : 0.2;
        }
    }

    /**
     * Helper method for leitner score method.
     * Determines the number of answers correct in the LastFive Queue
     * @param card Card whose LastFive is to be determined
     * @return number of correct answers
     * Postconditions: Returns integer between 0 - 5 inclusive
     */
    public static int correctFromLastFive (Card card) {
        EvictingQueue<Boolean> lastFive = card.getLastFive();
        int correct = 0;
        for (Boolean b : lastFive) {
            if (b == TRUE) correct++;
        }
        return correct;
    }


    /** Attempts to lower learntScore for cards which have not been viewed often.
     * Uses the function: 1 - (1/(x+1))
     * To save computation, after 30 attempts, 1 is returned.
     *
     * Preconditions: attempts must be a positive number
     * @return The reflex score, between 0 and 1 (inclusive)
     */
    public static double reflexScore(Card card, int aveAttempts, double aveLeitnerScore) {
        int attempts = card.getAttempts();
        double center = 2 - (2*aveLeitnerScore);
        double attemptsDiff = attempts - aveAttempts;
        double rs;
        if (attemptsDiff < center-1.05 || attemptsDiff > center+1.05) {
            rs = 1;
        } else {
            rs = (Math.pow((attemptsDiff-center),2)/2.0) + 0.4;
        }
        return rs;
    }

    private static double random(Card card, int aveAttempts) {
        int attempts = card.getAttempts();
        double attemptsDiff = attempts - aveAttempts;
        double rand = Math.random();
        if (attemptsDiff > -1) {
            return rand;
        } else {
            double randBias = 0.1 - (1.0/(attemptsDiff-0.1));
            return rand * randBias;
        }
    }

    private static double combinedScore (int sizeOfDeck, Card card, int aveAttempts, double aveLeitnerScore) {
        double r = random(card, aveAttempts);
        double m = reflexScore(card, aveAttempts, aveLeitnerScore);
        double l = leitnerScore(card);

        double combinedScore = (l + m + r)/3.0;
        return combinedScore;
    }

    /** Generates the 'Learnt Score' for the flashcard and updates the local variable
     *
     * @return Learnt score
     */
    public static int generateLearntScore(int sizeOfDeck, Card card, int aveAttempts, double aveLeitnerScore) {
        int scale = sizeOfDeck * 5;     // Determines the variation in the scores
        double s = combinedScore(sizeOfDeck, card, aveAttempts, aveLeitnerScore);
        int score = (int) Math.round(s * 2 * scale);

        card.setLearntScore(score);
        return score;
    }

    /**
     * To stop a card that is being skipped from repeatedly coming up in quick sucession,
     * the learnt score has to be artificially increased.
     *
     * In this case, it is being increased by 20%
     * @param sizeOfDeck
     * @param card
     * @return
     */
    public static int skipCard (int sizeOfDeck, Card card, int aveAttempts, double aveLeitnerScore) {

        int scale = sizeOfDeck * 5;     // Determines the variation in the scores
        double s = combinedScore(sizeOfDeck, card, aveAttempts, aveLeitnerScore);

        // Score increased by 20%
        if (s < 0.8) {
            s = s*1.2;
        }

        int score = (int) Math.round(s * 2 * scale);
        card.setLearntScore(score);
        return score;
    }

}
