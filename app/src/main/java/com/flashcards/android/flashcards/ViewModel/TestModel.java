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
    private Card lastCard;
    private int correct;
    private int total;

    public TestModel(@NonNull Application application) {
        super(application);
        this.repo = new TestRepo(application);
        testQueue = new PriorityBlockingQueue<Card>();
        correct = 0;
        total = 0;
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

            for(Card card: allCards) {
                testQueue.add(card);
            }

            return null;
        }
    }

    public Card getCurrentCard() {
        if (currentCard == null) {
            currentCard = getNewCard();
        }

        return currentCard;
    }

    public Card getNewCard() {
        if (testQueue.isEmpty()) return null;
        return testQueue.poll();
    }

    public Card skip() {
        lastCard = currentCard;
        currentCard = getNewCard();
        testQueue.add(lastCard);
        return currentCard;
    }

    public Card markIncorrect() {
        total++;
        currentCard = getNewCard();
        return currentCard;
    }

    public Card markCorrect() {
        correct++;
        total++;
        currentCard = getNewCard();
        return currentCard;
    }

    /**
     *
     * @return The final score as a Percentage
     */
    public int getScore () {
        Log.d(String.valueOf(correct), "correct: ");
        Log.d(String.valueOf(total), "total: ");

        double score = ((double) correct)/total;
        return (int) Math.round(score * 100);
    }


    // Simple Getter/Setters

    public String currentDeckId () {
        return deckId;
    }

    public boolean queueEmpty () {
        return (testQueue.isEmpty())? true : false;
    }


    // LiveData stuff

    public LiveData<Deck> getDeck(String deckId) {
        return repo.getDeck(deckId);
    }

    public LiveData<List<Card>> getAllCards(String deckId) {
        return repo.getAllCards(deckId);
    }

}