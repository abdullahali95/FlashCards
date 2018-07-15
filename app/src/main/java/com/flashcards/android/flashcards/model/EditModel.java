package com.flashcards.android.flashcards.model;

import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;

/**
 * Created by Abdullah Ali on 12/07/2018
 */
public class EditModel {
    private Deck deck;
    private Card currentCard;
    private String question;
    private String answer;

    public EditModel (String uuid) {
        this.deck = findDeck(uuid);

    }

    public Deck findDeck(String uuid) {
        return null;
    }

    public void addCard(Card card) {
        //TODO: progress has to be manually set once the card is created
    }

    public void viewCard(Card currentCard) {
        this.currentCard = currentCard;
        this.question = currentCard.getQuestion();
        this.answer = currentCard.getAnswer();
    }

    public void updateQuestion(int id, String question) {

    }

    public void updateAnswer (int id, String question) {

    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
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
