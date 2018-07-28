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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;

public class TestModel extends AndroidViewModel {

    private TestRepo repo;
    private String deckId;
    private static PriorityQueue<Card> testQueue = new PriorityQueue<Card>();
    private Card currentCard;
    private Card lastCard;

    public TestModel(@NonNull Application application) {
        super(application);
        this.repo = new TestRepo(application);
    }


    public boolean queueReady() {
        return (testQueue.size() > 0) ? true : false;
    }

    public void initQueue(String deckId) {
        this.deckId = deckId;

       // TODO: run the ASYNCTASK properly
        GetCardsTask task = new GetCardsTask();
        try {
            task.execute(deckId).get();
        } catch (InterruptedException e) {
            Log.e("Test Model: ",
                    "Error: There was a problem while trying to obtain the cards from the database",
                    e);
        } catch (ExecutionException e) {
            Log.e("Test Model: ",
                    "Error: There was a problem while trying to obtain the cards from the database",
                    e);
        }


    }



    public class GetCardsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            testQueue.addAll(repo.getAllDeckCards(strings[0]));

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
        return testQueue.poll();
    }

    public void skip() {

    }

    public Card markIncorrect() {
        Log.d("markIncorrect: ", currentCard.getLastFive().toString());
        currentCard.addAnswer(Boolean.FALSE);
        Log.d("markIncorrect: ", currentCard.getLastFive().toString());

        lastCard = currentCard;
        currentCard = getNewCard();
        Log.d("markIncorrectNewCard: ", currentCard.getLastFive().toString());

        testQueue.add(lastCard);
        return currentCard;
    }

    public Card markCorrect() {
        currentCard.addAnswer(Boolean.TRUE);
        lastCard = currentCard;
        currentCard = getNewCard();
        testQueue.add(lastCard);

        return currentCard;
    }
    /*
     * Created by Abdullah Ali
     */

    public void finish() {
        deckId = null;
        testQueue.clear();
        currentCard = null;
        lastCard = null;

        // TODO: Save changes
    }

    // Simple Getter/Setters

    public String currentDeckId () {
        return deckId;
    }


    // LiveData stuff

    public LiveData<Deck> getDeck(String deckId) {
        return repo.getDeck(deckId);
    }

    public LiveData<List<Card>> getAllCards(String deckId) {
        return repo.getAllCards(deckId);
    }

}