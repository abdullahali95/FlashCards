package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.flashcards.android.flashcards.data.TestRepo;
import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.lib.Progress;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.PriorityQueue;

public class TestModel extends AndroidViewModel {
    /*
     * Created by Abdullah Ali
     */

    // Uses minHeap to create priority Queue
    private Deck testDeck;
    private List<Card> testCards;
    private PriorityQueue<Card> testQueue;
    private Card currentCard;
    private Card lastCard;
    private int deckSize;
    private TestRepo repo;

    // TODO: remove deck from constructor arguments
    public TestModel(Application application) {
        super(application);
        this.repo = new TestRepo(application);
        testQueue = new PriorityQueue<Card>();


    }

    public void setTestDeck(String deckId) {
        testDeck = repo.getDeck(deckId).getValue();

    }

    public LiveData<List<Card>> getAllCards(String deckId) {
        testCards = repo.getAllCards(deckId).getValue();
        return repo.getAllCards(deckId);
    }

    public void initQueue (List<Card> cards) {
        testCards = cards;
        makeQueue();

        this.deckSize = testQueue.size();
        initialiseLearntScores();

        this.lastCard = testQueue.poll();
        testQueue.add(lastCard);

        //Set the date of last use to today's date.
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = sf.format(cal.getTime());
//        TODO: testDeck.setLastUsed(date);
//        TODO: repo.setDeck(testDeck);
    }

    public LiveData<List<Card>> getTestCards() {
        return repo.getAllCards(testDeck.getDeckId());
    }



    private void initialiseLearntScores() {
        for (Card card : testQueue) {
            card.getProgress().generateLearntScore(deckSize);
        }
    }

    public void makeQueue() {
        Log.d(String.valueOf(testCards.size()), "makeQueue: ");
        int size = testCards.size();

        for (Card card:testCards) {
            if (card.getProgress() == null) {
                card.setProgress(new Progress(card.getCardId(), card.getDeckId()));
            }

            card.getProgress().generateLearntScore(size);
        }

        testQueue.addAll(testCards);
    }


    public Card getCurrentCard() {
        if (currentCard == null) {
            currentCard = getCard();
        }
            return currentCard;
    }

    public Card getCard() {
        currentCard = testQueue.poll();
        if (currentCard.equals(lastCard)) {
            // If the drawn card is the same as last time, redraw the card.

            Card card2 = testQueue.poll();
            testQueue.add(currentCard);
            currentCard = card2;

        }

        return currentCard;
    }

    //
    public boolean markCorrect() {
        incAttempts();
        incCorrect();
        //TODO: card.getProgress().addAnswer(Boolean.TRUE);
        generateLearntScore();
        lastCard = currentCard;
        testQueue.add(currentCard);
        return true;
    }

    public boolean skip() {

        generateLearntScore();
        lastCard = currentCard;
        testQueue.add(currentCard);
        return true;
    }

    //
    public boolean markIncorrect() {
        incAttempts();
        // TODO: card.getProgress().addAnswer(Boolean.FALSE);
        generateLearntScore();
        lastCard = currentCard;
        testQueue.add(currentCard);
        return true;
    }


    public void incAttempts () {
        IncAttemptsTask task = new IncAttemptsTask();
        task.execute();
    }

    public class IncAttemptsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            repo.incAttempts(currentCard.getCardId(), currentCard.getDeckId());
            return true;
        }
    }


    public void incCorrect () {
        IncCorrectTask task = new IncCorrectTask();
        task.execute();
    }

    public class IncCorrectTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            repo.incCorrect(currentCard.getCardId(), currentCard.getDeckId());
            return true;
        }
    }


    public void generateLearntScore () {
        setProgressTask task = new setProgressTask();
        task.execute();
    }

    public class setProgressTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            currentCard.getProgress().generateLearntScore(deckSize);
            repo.setProgress(currentCard.getProgress());
            return true;
        }
    }

    public void setTestCards(List<Card> testCards) {
        this.testCards = testCards;
    }
}