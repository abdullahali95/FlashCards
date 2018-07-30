package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.flashcards.android.flashcards.data.repo.TestRepo;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.PriorityQueue;
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

    private int counter;

    public TestModel(@NonNull Application application) {
        super(application);
        this.repo = new TestRepo(application);
        testQueue = new PriorityBlockingQueue<Card>();
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
            e.printStackTrace();
//            Log.e("Test Model: ",
//                    "Error: There was a problem while trying to obtain the cards from the database",
//                    e);
        } catch (ExecutionException e) {
            e.printStackTrace();
//            Log.e("Test Model: ",
//                    "Error: There was a problem while trying to obtain the cards from the database",
//                    e);
        }

        SetLastUsedTask task2 = new SetLastUsedTask();
        task2.execute();

    }

    public class GetCardsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            List<Card> allCards = repo.getAllDeckCards(strings[0]);
            int size = allCards.size();
            for(Card card: allCards) {
                card.getLearntScore(size);
                testQueue.add(card);
            }

            return null;
        }
    }

    public class SetLastUsedTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            // Update last used date
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            Calendar cal = Calendar.getInstance();
            String date = sf.format(cal.getTime());

            repo.setLastUsed(deckId, date);
            return true;
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

    public Card skip() {
        Log.d(String.valueOf(currentCard.getLearntScore()), "skip: ");
        currentCard.skip(testQueue.size()+1);
        Log.d(String.valueOf(currentCard.getLearntScore()), "skip: ");

        lastCard = currentCard;
        currentCard = getNewCard();
        Log.d("Last Card: ", lastCard.toString());
        Log.d("Current Card: ", currentCard.toString());
        testQueue.add(lastCard);

        incCounter();
        return currentCard;
    }

    public Card markIncorrect() {
        currentCard.addAnswer(Boolean.FALSE);
        incAttempts();
        incCorrect();
        generateLearntScore();

        lastCard = currentCard;
        currentCard = getNewCard();

        testQueue.add(lastCard);
        incCounter();
        return currentCard;
    }

    public Card markCorrect() {
        currentCard.addAnswer(Boolean.TRUE);
        incAttempts();
        incCorrect();
        generateLearntScore();

        lastCard = currentCard;
        currentCard = getNewCard();
        testQueue.add(lastCard);
        incCounter();

        return currentCard;
    }


    public void incAttempts () {
        currentCard.setAttempts(currentCard.getAttempts()+1);
    }


    public void incCorrect () {
        currentCard.setCorrect(currentCard.getCorrect()+1);
    }

    public void generateLearntScore () {
        currentCard.getLearntScore(testQueue.size()+1);
    }

    private void incCounter() {
        counter++;

        if (counter > 5) {
            // Periodic reshuffle of cards
            int size = testQueue.size();
            PriorityBlockingQueue<Card> newQueue = new PriorityBlockingQueue<Card>();
            for(Card card: testQueue) {
                card.getLearntScore(size);
                newQueue.add(card);
            }
            testQueue = newQueue;

            SaveChangesTask task = new SaveChangesTask();
            task.execute();

            counter = 0;
        }

    }


    public void finish() {
        // TODO: Save changes
        SaveChangesTask task = new SaveChangesTask();
        task.execute();

    }

    public class SaveChangesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            // Save Card info
            List<Card> allCards = new ArrayList<Card>();
            allCards.addAll(testQueue);
            repo.setAllCards(allCards);

            //TODO: update deck stats

            return null;
        }
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