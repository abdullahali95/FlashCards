package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.flashcards.android.flashcards.data.repo.TestRepo;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;


/*
 * Created by Abdullah Ali
 */

public class TestModel extends AndroidViewModel {

    private TestRepo repo;
    private String deckId;
    private static PriorityBlockingQueue<Card> testQueue;
    private Card currentCard;
    private int correct;
    private int totalAttempted;
    private int deckSize;
    private boolean aSide;      // Marks if the current side being viewed is the Answer Side

    public TestModel(@NonNull Application application) {
        super(application);
        this.repo = new TestRepo(application);
        testQueue = new PriorityBlockingQueue<Card>();
        correct = 0;
        totalAttempted = 0;
    }

    public void initQueue(String deckId) {
        this.deckId = deckId;

       // TODO: run the ASYNCTASK properly
        GetCardsTask task = new GetCardsTask();
        try {
            task.execute(deckId).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class GetCardsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            List<Card> allCards = repo.getAllDeckCards(strings[0]);
            testQueue.addAll(allCards);
            deckSize = testQueue.size();
            return null;
        }
    }

    public Card getCurrentCard() {
        if (currentCard == null) {
            currentCard = getNewCard();
        }

        return currentCard;
    }

    private Card getNewCard() {
        if (testQueue.isEmpty()) return null;
        return testQueue.poll();
    }

    public Card skip() {
        Card lastCard = currentCard;
        currentCard = getNewCard();
        testQueue.add(lastCard);
        return currentCard;
    }

    public Card markIncorrect() {
        totalAttempted++;
        currentCard = getNewCard();
        return currentCard;
    }

    public Card markCorrect() {
        correct++;
        totalAttempted++;
        currentCard = getNewCard();
        return currentCard;
    }

    /**
     *
     * @return The final score as a Percentage
     */
    public int getScore () {
        Log.d(String.valueOf(correct), "correct: ");
        Log.d(String.valueOf(totalAttempted), "totalAttempted: ");

        double score = ((double) correct)/ totalAttempted;
        return (int) Math.round(score * 100);
    }


    // Simple Getter/Setters

    public String currentDeckId () {
        return deckId;
    }

    public boolean queueEmpty () {
        return testQueue.isEmpty();
    }

    public int getCorrect() {
        return correct;
    }

    public int getTotalAttempted() {
        return totalAttempted;
    }

    public boolean isQSide() {
        return !aSide;
    }

    public void setaSide(boolean aSide) {
        this.aSide = aSide;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }

    // LiveData stuff

    public LiveData<Deck> getDeck(String deckId) {
        return repo.getDeck(deckId);
    }

    public LiveData<List<Card>> getAllCards(String deckId) {
        return repo.getAllCards(deckId);
    }

}