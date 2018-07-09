package com.flashcards.android.flashcards.lib;

import android.support.annotation.NonNull;

public class Card implements Comparable<Card> {
    private int id;
    private String question;
    private String answer;
    private Progress progress;

    public Card(int id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.progress = new Progress();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }


    //TODO: test the comparator

    /** Comparator for ranking cards by 'learnt score'
     *
     * @param o Card being compared
     * @return integer corresponding to the comparison of the two cards
     */
    @Override
    public int compareTo(@NonNull Card o) {
        Integer thisScore = this.progress.getLearntScore();
        Integer otherScore = o.progress.getLearntScore();

        return thisScore.compareTo(otherScore);
    }


    @Override
    public String toString() {
        return "Question='" + question + '\'' +
                ", Answer='" + answer + '\'';
    }
}
