package com.flashcards.android.flashcards.model;

import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.PriorityQueue;

public class TestModel {
    /*
     *
     */

    // Uses minHeap to create priority Queue
    private PriorityQueue<Card> testQueue;

    public TestModel(Deck deck) {
        testQueue = new PriorityQueue<Card>();
        makeQueue(deck);

        //Set the date of last use to today's date.
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-YYYY");
        Calendar cal = Calendar.getInstance();
        String date = sf.format(cal);
        deck.setLastUsed(date);

        //Update deck usage stats
        deck.incAttempts();
    }

    private void makeQueue(Deck deck) {
        testQueue.addAll(deck.getCards());
    }

    //
    public Card getCard() {
        return testQueue.poll();
    }

    //
    public boolean markCorrect(Card card) {
        card.getProgress().incAttempts();
        card.getProgress().incCorrect();
        card.getProgress().addAnswer(Boolean.TRUE);
        return true;
    }

    //
    public boolean markIncorrect(Card card) {
        card.getProgress().incAttempts();
        card.getProgress().addAnswer(Boolean.FALSE);
        return true;
    }

}
