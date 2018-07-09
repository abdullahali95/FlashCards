package com.flashcards.android.flashcards.test;

import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.model.TestModel;
import com.flashcards.android.flashcards.repo.MockDeck;

public class MockView {
    private Deck mock;
    private static TestModel model;

    public MockView(Deck mock, TestModel model) {
        this.mock = MockDeck.getFakeDeck();
        this.model = new TestModel(mock);
    }
    
    public static void main (String[] args) {
        Card card;
        for (int i = 0; i < 10; i++) {
            // Generate random cards
            card = model.getCard();
            System.out.println(card);

            // Randomly mark them correct/incorrect
            if (Math.random() < 0.5) {
                model.markCorrect(card);
                System.out.println("Correct Answer!");
            } else {
                model.markIncorrect(card);
                System.out.println("Inorrect Answer!");
            }
        }
    }
}
