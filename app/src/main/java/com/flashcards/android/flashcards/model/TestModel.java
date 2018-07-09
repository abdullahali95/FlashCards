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
    private Card lastCard;
    private int deckSize;

    public TestModel(Deck deck) {
        testQueue = new PriorityQueue<Card>();
        makeQueue(deck);
        this.deckSize = testQueue.size();

        //Set the date of last use to today's date.
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-YYYY");
        Calendar cal = Calendar.getInstance();
        String date = sf.format(cal.getTime());
        deck.setLastUsed(date);

        //Update deck usage stats
        deck.incAttempts();
    }

    private void makeQueue(Deck deck) {
        testQueue.addAll(deck.getCards());        
    }

    //
    public Card getCard() {
        Card card = testQueue.poll();
        if (card.equals(lastCard)) {
        	// If the drawn card is the same as last time, redraw the card.
        	
        	Card card2 = testQueue.poll();
        	testQueue.add(card);
        	
        	
        	return card2;
        } else return card;
    }

    //
    public boolean markCorrect(Card card) {
        card.getProgress().incAttempts();
        card.getProgress().incCorrect();
        card.getProgress().addAnswer(Boolean.TRUE);
        card.getProgress().generateLearntScore(deckSize);
        lastCard = card;
        testQueue.add(card);
        return true;
    }

    //
    public boolean markIncorrect(Card card) {
        card.getProgress().incAttempts();
        card.getProgress().addAnswer(Boolean.FALSE);
        card.getProgress().generateLearntScore(deckSize);
        lastCard = card;
        testQueue.add(card);
        return true;
    }

}
