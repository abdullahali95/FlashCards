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
     * 0-1 correct = 0.4
     * 2 correct = 0.55
     * 3 correct = 0.7
     * 4 correct = 0.85
     * 5 correct = 1.0 (card learnt)
     *
     * @return Customised Leitner Score.
     * PostConditions: The leitner score should be between 0.4 - 1.0
     */
    public static double leitnerScore (Card card) {
        int total = card.getAttempts();
        EvictingQueue<Boolean> lastFive = card.getLastFive();

        // Initialising at 0.5 slows introduction of new cards into the deck,
        // But still keeps the score slightly lower then cards which have been
        // answered correctly atleast once (0.55)
        if (total == 0) return 0.5;
        else {
	        int correct = 0;
            for (Boolean b : lastFive) {
                if (b == TRUE) correct++;
            }
	        // With the queue size 5, interval between each deck is 15%
            // Following equation makes this division
	        double ls = ((15*(correct-1))+40/ (double) 100);

	        // If there are no true answers, min value should be 0.4
	        return (ls >= 0.4)? ls : 0.4;
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
            return 1;         // The function doesn't provide an appropriate value for 0.
        } else if ( attempts > 20) {
            return 1;           // Value close to 1, so saves calculation.
        } else {
            return 1 - ( (double) 1/(attempts +1));
            }
    }

    private static double combinedScore (int sizeOfDeck, Card card) {
        double rand = Math.random();
        double m = reflexScore(card.getAttempts());
        double l = leitnerScore(card);

        // The effect of random function should vary depending on size of deck
        // The smaller the deck, the more random the deck should be shuffled.
        // The effect is determined by function y = (1.5/x)+0.25
        double randWeight = (1.5/sizeOfDeck) + 0.25;

        return ((l * m * (1-randWeight)) + (rand * randWeight))/2;
    }

    //TODO: test the learntScore and it's initialisd value
    /** Generates the 'Learnt Score' for the flashcard and updates the local variable
     *
     * @return Learnt score
     */
    public static int generateLearntScore(int sizeOfDeck, Card card) {
        int scale = sizeOfDeck * 5;     // Determines the variation in the scores
        double s = combinedScore(sizeOfDeck, card);
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
    public static int skipCard (int sizeOfDeck, Card card) {
        int scale = sizeOfDeck * 5;     // Determines the variation in the scores
        double s = combinedScore(sizeOfDeck, card);

        // Score increased by 20%
        if (s < 0.8) {
            s = s*1.2;
        }

        int score = (int) Math.round(s * 2 * scale);
        card.setLearntScore(score);
        return score;
    }

}
