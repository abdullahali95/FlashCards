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
import com.flashcards.android.flashcards.lib.model.Progress;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;


/*
 * Created by Abdullah Ali
 */


public class ReviseModel extends AndroidViewModel {

    private TestRepo repo;
    private String deckId;
    private static PriorityBlockingQueue<Card> testQueue;
    private Card currentCard;
    private Card lastCard;

    private int counter;
    private int aveAttempts;
    private double aveLeitnerScore;

    public ReviseModel(@NonNull Application application) {
        super(application);
        this.repo = new TestRepo(application);
        testQueue = new PriorityBlockingQueue<Card>();
    }


    public boolean queueReady() {
        return testQueue.size() > 0;
    }

    public void initQueue(String deckId) {
        this.deckId = deckId;

       // TODO: run the ASYNCTASK properly
        GetCardsTask task = new GetCardsTask();
        try {
            task.execute(deckId).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
//            Log.e("Test Model: ",
//                    "Error: There was a problem while trying to obtain the cards from the database",
//                    e);
        }

    }

    public class GetCardsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            List<Card> allCards = repo.getAllDeckCards(strings[0]);
            int size = allCards.size();

            for(Card card: allCards) {
                aveAttempts += card.getAttempts();
                aveLeitnerScore += Progress.leitnerScore(card);
            }
            aveAttempts = aveAttempts/size;
            aveLeitnerScore = aveLeitnerScore/ (double) size;

            for(Card card: allCards) {
                card.getLearntScore(size, aveAttempts, aveLeitnerScore);
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
        return testQueue.poll();
    }

    public Card skip() {
        Log.d(String.valueOf(currentCard.getLearntScore()), "skip: ");
        currentCard.skip(testQueue.size()+1, aveAttempts, aveLeitnerScore);
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


    private void incAttempts() {
        currentCard.setAttempts(currentCard.getAttempts()+1);
    }


    private void incCorrect() {
        currentCard.setCorrect(currentCard.getCorrect()+1);
    }

    private void generateLearntScore() {
        currentCard.getLearntScore(testQueue.size()+1, aveAttempts, aveLeitnerScore);
    }

    private void incCounter() {
        counter++;

        if (counter > 5) {

            // Recalculate attempts and leitner score
            int size = testQueue.size()+1;  // +1 as currentcared is not in the queue
            for(Card card: testQueue) {
                aveAttempts += card.getAttempts();
                aveLeitnerScore += Progress.leitnerScore(card);
            }
            aveAttempts = aveAttempts/size;
            aveLeitnerScore = aveLeitnerScore/ (double) size;


            // Periodic reshuffle of cards
            PriorityBlockingQueue<Card> newQueue = new PriorityBlockingQueue<Card>();
            for(Card card: testQueue) {
                card.getLearntScore(size, aveAttempts, aveLeitnerScore);
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

            // Update last used date
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            Calendar cal = Calendar.getInstance();
            String date = sf.format(cal.getTime());

            repo.setLastUsed(deckId, date);
            repo.setEf(deckId, aveLeitnerScore);

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