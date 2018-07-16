package com.flashcards.android.flashcards.data;

import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;

public class MockDeck {

    public static Deck getFakeDeck() {
        Deck fakeDeck = new Deck("Fake Deck");

        Card fakeCard;
        for (int i = 0; i < 30; i++) {
            fakeCard = new Card("blah",
                    "<span style=\"color: rgb(90, 155, 1);\">Am</span> <b>I A person, or am I a human? describe this pheonmeonon" +
                            "in as much details as you can</b> %23 " + i + "?",
                    "Card %23: " + i + "");
            fakeDeck.addCard(fakeCard);
        }

        return fakeDeck;
    }

    public static Deck getFakeDeck(String deckName, int deckSize) {
        Deck fakeDeck = new Deck(deckName);

        Card fakeCard;
        for (int i = 0; i < deckSize; i++) {
            fakeCard = new Card("blah",
                    "<span style=\"color: rgb(90, 155, 1);\">Am</span> <b>I card</b> %23 " + i + "?",
                    "Card %23: " + i + "");
            fakeDeck.addCard(fakeCard);
        }

        return fakeDeck;
    }
}
