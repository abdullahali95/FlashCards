package com.flashcards.android.flashcards.lib;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.common.collect.EvictingQueue;

import java.io.Serializable;
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
 * Ideally should implement Parcellable, but since it contains EvictingQueue, this is difficult to do.
 * Therefore, Serializable is implemented.
 * This should not alter/compromise the performance in any way.
 */

@Entity (foreignKeys = {
            @ForeignKey(
                entity = Deck.class,
                parentColumns = "deckId",
                childColumns = "deckId",
                onDelete = CASCADE),
            @ForeignKey(
                    entity = Card.class,
                    parentColumns = "cardId",
                    childColumns = "cardId",
                    onDelete = CASCADE)},
            primaryKeys = {"cardId", "deckId"})
public class Progress {

    @NonNull
    private int cardId;
    @NonNull
    private String deckId;
    private int attempts;   //Invariant: must always stay positive
    private int correct;

    //TODO: handle storing this in db
    @Ignore
    private EvictingQueue<Boolean> lastTen;     // Pushes out old elements if size exceeds 10
    private int learntScore;


    public Progress(int cardId, String deckId) {
        this.cardId = cardId;
        this.deckId = deckId;
        this.attempts = 0;
        this.correct = 0;
        this.lastTen = EvictingQueue.create(10);
        this.learntScore = 100;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
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
        // Score inaccurate for 0-1 attempts
    	if (attempts < 2) return 0.5;
    	
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
    public double reflexScore() {
    	if (attempts == 0) {
            return 500;
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
    public int generateLearntScore(int size) {
        if (attempts == 0) {

            int initialVal =(int) ( ((double) Math.random() * 200) + 400);
            learntScore = initialVal;
            return initialVal;
        } else {

            double rand = random();
            
            double m = reflexScore();
            
            double l = ratioCorrect();            
            
            // The effect of random function should vary depending on size of deck
            // The smaller the deck, the more random the deck should be shuffled.
            // The effect is determined by function y = (1.5/x)+0.25
            
            double randWeight = (1.5/size) + 0.25;
            
            double s = ((l * m * (1-randWeight)) + (rand * randWeight))/2;
            
            // 200 is chosen as a reasonable size for deck. Too large a number may cause
            // Too big a variation in the deck and may cause card to keep showing/never show.
            int score = (int) Math.round(s * 2 * 1000);
            
            learntScore = score;
            return score;
        }
    }



}
