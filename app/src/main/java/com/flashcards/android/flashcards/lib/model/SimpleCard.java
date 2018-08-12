package com.flashcards.android.flashcards.lib.model;

/**
 * A simpler version of Card objects, used for importing/exporting cards and decks
 * Created by Abdullah Ali on 22/07/2018
 */
public class SimpleCard {
    private String question;
    private String answer;

    public SimpleCard(String question, String answer) {
        this.question = question;
        this.answer = answer;
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
}
