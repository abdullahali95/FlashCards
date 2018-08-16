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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;

/*
 * Created by Abdullah Ali
 */

public class ReviseModel extends AndroidViewModel {

    private TestRepo repo;
    private String deckId;
    private Deck currentDeck;
    private static PriorityBlockingQueue<Card> testQueue;
    private Card currentCard;
    private Card lastCard;

    private int counter;
    private int aveAttempts;
    private double aveLeitnerScore;
    private boolean aSide;      // Marks if the current side being viewed is the Answer Side

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
        GetCardsTask task = new GetCardsTask();
        try {
            task.execute(deckId).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("Test Model: ",
                    "Error: There was a problem while trying to obtain the cards from the database",
                    e);
        }

    }

    public class GetCardsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            currentDeck = repo.getDeckNow(strings[0]);
            List<Card> allCards = repo.getAllDeckCards(strings[0]);
            int size = allCards.size();

            for(Card card: allCards) {
                aveAttempts += card.getAttempts();
                aveLeitnerScore += Progress.correctFromLastFive(card);
            }
            aveAttempts = aveAttempts/size;
            aveLeitnerScore = aveLeitnerScore/ (double) (size * 5);

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

        if (counter > 3) {

            // Recalculate attempts and leitner score
            int size = 0;

            aveAttempts = 0;
            aveLeitnerScore = 0;
            for(Card card: testQueue) {
                aveAttempts += card.getAttempts();
                aveLeitnerScore += Progress.correctFromLastFive(card);
                size++;
            }
            aveAttempts = (int) Math.round(aveAttempts/ (double) size);
            aveLeitnerScore = aveLeitnerScore/ (double) (size * 5);


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

    public class SaveChangesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            // Save Card info
            List<Card> allCards = new ArrayList<Card>(testQueue);
            repo.setAllCards(allCards);

            return null;
        }
    }


    public void finish() {
        SaveChangesTask task = new SaveChangesTask();
        task.execute();

        SaveDeckTask task2 = new SaveDeckTask();
        task2.execute();

    }

    public class SaveDeckTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // If the deck has been revised, update the Easiness Factor of the deck.
            double ls;
                ls = aveLeitnerScore * 5;
                Log.d(String.valueOf(ls), "ls: ");
            currentDeck.setLs(ls);

            // Update last used date
            currentDeck.setNextInterval();
            repo.setDeck(currentDeck);

            return null;
        }
    }

    // Simple Getter/Setters

    public String currentDeckId () {
        return deckId;
    }

    public int getPercentageLearnt() {
        return (int) Math.round(aveLeitnerScore * 100);
    }

    public boolean isQSide() {
        return !aSide;
    }

    public void setaSide(boolean aSide) {
        this.aSide = aSide;
    }

    // LiveData stuff

    public LiveData<Deck> getDeck(String deckId) {
        return repo.getDeck(deckId);
    }

    public LiveData<List<Card>> getAllCards(String deckId) {
        return repo.getAllCards(deckId);
    }

}