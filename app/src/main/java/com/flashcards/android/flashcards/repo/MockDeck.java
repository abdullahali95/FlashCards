package com.flashcards.android.flashcards.repo;

import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;

public class MockDeck {

    public static Deck getFakeDeck() {
        Deck fakeDeck = new Deck("Fake Deck");

        Card fakeCard;
        for (int i = 0; i < 20; i++) {
            fakeCard = new Card(i,
                    "<span style=\"color: rgb(90, 155, 1);\">Am</span> <b>I card</b> # " + i + "?",
                    "Card #: " + i + "");
            fakeDeck.addCard(fakeCard);
        }

        return fakeDeck;
    }

    public static Deck getFakeDeck(String deckName, int deckSize) {
        Deck fakeDeck = new Deck(deckName);

        Card fakeCard;
        for (int i = 0; i < deckSize; i++) {
            fakeCard = new Card(i,
                    "<span style=\"color: rgb(90, 155, 1);\">Am</span> <b>I card</b> # " + i + "?",
                    "Card #: " + i + "");
            fakeDeck.addCard(fakeCard);
        }

        return fakeDeck;
    }
}
