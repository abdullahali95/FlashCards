package com.flashcards.android.flashcards.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.PriorityQueue;

public class TestModel implements Parcelable {
    /*
     * Created by Abdullah Ali
     */

    // Uses minHeap to create priority Queue
    private PriorityQueue<Card> testQueue;
    private Card lastCard;
    private int deckSize;

    public TestModel(Deck deck) {
        testQueue = new PriorityQueue<Card>();
        makeQueue(deck);
        this.deckSize = testQueue.size();
        initialiseLearntScores();

        this.lastCard = testQueue.poll();
        testQueue.add(lastCard);

        //Set the date of last use to today's date.
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = sf.format(cal.getTime());
        deck.setLastUsed(date);

        //Update deck usage stats
        deck.incAttempts();
    }

    private void initialiseLearntScores() {
        for (Card card : testQueue) {
            card.getProgress().generateLearntScore(deckSize);
        }
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

    public boolean skip(Card card) {

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


    protected TestModel(Parcel in) {
        testQueue = (PriorityQueue) in.readValue(PriorityQueue.class.getClassLoader());
        lastCard = (Card) in.readValue(Card.class.getClassLoader());
        deckSize = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(testQueue);
        dest.writeValue(lastCard);
        dest.writeInt(deckSize);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TestModel> CREATOR = new Parcelable.Creator<TestModel>() {
        @Override
        public TestModel createFromParcel(Parcel in) {
            return new TestModel(in);
        }

        @Override
        public TestModel[] newArray(int size) {
            return new TestModel[size];
        }
    };
}