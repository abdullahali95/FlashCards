package com.flashcards.android.flashcards.lib.model;

//import android.support.annotation.NonNull;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.common.collect.EvictingQueue;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity (foreignKeys = @ForeignKey(entity = Deck.class,
        parentColumns = "deckId",
        childColumns = "deckId",
        onDelete = CASCADE))
public class Card implements Comparable<Card> {

    /* Ideally we would have a composite key of deckId and cardId, but Room does not
     * allow a composite key to be autoGenerated
     */
    @PrimaryKey(autoGenerate = true)
    private int cardId;
    @NonNull
    private String deckId;
    private String question;
    private String answer;
    private String hint;

    private int attempts;   //Invariant: must always stay positive
    private int correct;
    private EvictingQueue<Boolean> lastFive;     // Pushes out old elements if size exceeds 5
    private int learntScore;


    /**
     * A class to store Flash cards objects.
     * A corresponding Progress Object must be setup after a Card is created.
     * @param deckId
     * @param question
     * @param answer
     *
     */
    public Card(@NonNull String deckId, String question, String answer) {
        this.deckId = deckId;
        this.question = question;
        this.answer = answer;
        this.lastFive = EvictingQueue.create(5);
    }

    /**
     * For creating an empty card
     * @param deckId
     */
    @Ignore
    public Card (@NonNull String deckId) {
        this.deckId = deckId;
        this.question = "";
        this.answer = "";
        this.hint = "";
        this.attempts = 0;
        this.correct = 0;
        this.lastFive = EvictingQueue.create(5);
        this.learntScore = 100;
    }

    //TODO: progress has to be manually set once the card is created

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    @NonNull
    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(@NonNull String deckId) {
        this.deckId = deckId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
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

    public EvictingQueue<Boolean> getLastFive() {
        return lastFive;
    }

    public void setLearntScore(int learntScore) {
        this.learntScore = learntScore;
    }

    public void setLastFive(EvictingQueue<Boolean> lastFive) {
        this.lastFive = lastFive;
    }

    public void setLastTen(List<Boolean> list) {
        lastFive.addAll(list);
    }

    public void addAnswer(Boolean answer) {
        lastFive.add(answer);
    }

    public int getLearntScore(int deckSize, int aveAttempts, double aveLeitnerScore) {
        learntScore = Progress.generateLearntScore(deckSize, this, aveAttempts, aveLeitnerScore);
        return learntScore;
    }

    public int skip(int deckSize, int aveAttempts, double aveLeitnerScore) {
        learntScore = Progress.skipCard(deckSize, this, aveAttempts, aveLeitnerScore);
        return learntScore;
    }

    public int getLearntScore() {
        return learntScore;
    }


    //TODO: test the comparator

    /** Comparator for ranking cards by 'learnt score'
     *
     * @param o Card being compared
     * @return integer corresponding to the comparison of the two cards
     */
    @Override
    public int compareTo(@NonNull Card o) {
        Integer thisScore = this.getLearntScore();
        Integer otherScore = o.getLearntScore();

        return thisScore.compareTo(otherScore);
    }

    @Override
    public boolean equals(Object o) {
        return o != null && ((Integer) this.cardId).equals(((Card) o).getCardId());
    }


    @Override
    public String toString() {
        return "Question='" + question + '\'' +
                ", Answer='" + answer + '\'';
    }


}