package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.flashcards.android.flashcards.data.repo.TestRepo;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.PriorityQueue;

public class TestModelBackup extends AndroidViewModel {
    /*
     * Created by Abdullah Ali
     */

    private Deck testDeck;
    private List<Card> testCards;
    private PriorityQueue<Card> testQueue; // Uses minHeap to create priority Queue
    private Card currentCard;
    private Card lastCard;
    private int deckSize;
    private TestRepo repo;

    public TestModelBackup(Application application) {
        super(application);
        this.repo = new TestRepo(application);
        testQueue = new PriorityQueue<Card>();


    }

    public LiveData<Deck> getDeck(String deckId) {
        return repo.getDeck(deckId);
    }

    public void setTestDeck(Deck deck) {
        testDeck = deck;

        // Update last used date
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Calendar cal = Calendar.getInstance();
        String date = sf.format(cal.getTime());

        testDeck.setLastUsed(date);

        SetLastUsedTask task = new SetLastUsedTask();
        task.execute();

    }

    public class SetLastUsedTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            repo.setLastUsed(testDeck.getDeckId(), testDeck.getLastUsed());
            return true;
        }
    }


    public LiveData<List<Card>> getAllCards(String deckId) {
        testCards = repo.getAllCards(deckId).getValue();
        return repo.getAllCards(deckId);
    }

    public void initQueue (List<Card> cards) {
        testCards = cards;
        makeQueue();

        this.deckSize = testQueue.size();

    }

    public LiveData<List<Card>> getTestCards() {
        return repo.getAllCards(testDeck.getDeckId());
    }


    public void makeQueue() {
//        int size = testCards.size();
//
//        for (Card card:testCards) {
//            card.getLearntScore(size);
//        }
//
//        testQueue.addAll(testCards);

        Log.d("resetting queue", "onChanged: ");

        Card c1 = new Card("1", "card1", "Answer 1");
        Card c2 = new Card("1", "card2", "Answer 2");

        testQueue.add(c1);
        testQueue.add(c2);

    }


    public Card getCurrentCard() {
        if (currentCard == null) {
            currentCard = getNewCard();
        }
            return currentCard;
    }

    public Card getNewCard() {
        if (currentCard != null) {
            lastCard = currentCard;
        }

        Log.d("getNewCard: ", testQueue.toString());
        currentCard = testQueue.poll();
        Log.d(currentCard.getLastFive().toString(), "getNewCard: ");

        if (lastCard != null) {
            testQueue.add(lastCard);
            Log.d(lastCard.getLastFive().toString(), "getNewCard: LastFive");
            Log.d("getNewCard: LastFive", testQueue.peek().getLastFive().toString());
        }
        return currentCard;
    }

    //
    public boolean markCorrect() {
        incAttempts();
        incCorrect();
        currentCard.addAnswer(Boolean.TRUE);

        AddAnswerTask task = new AddAnswerTask();
        task.execute();

        generateLearntScore();
        return true;
    }

    public boolean skip() {
        generateLearntScore();
        return true;
    }

    //
    public boolean markIncorrect() {
        incAttempts();
        Log.d(currentCard.getLastFive().toString(), "markIncorrect: ");
        currentCard.addAnswer(Boolean.FALSE);
        Log.d(currentCard.getLastFive().toString(), "markIncorrectAfter: ");
        Log.d(String.valueOf(currentCard.getLastFive().size()), "markIncorrectSizeAfter: ");

        AddAnswerTask task = new AddAnswerTask();
        task.execute();

        generateLearntScore();
        return true;
    }


    public void incAttempts () {
        currentCard.setAttempts(currentCard.getAttempts()+1);
        SetAttemptsTask task = new SetAttemptsTask();
        task.execute();
    }

    public class SetAttemptsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            repo.setAttempts(currentCard.getCardId(), currentCard.getDeckId(), currentCard.getAttempts());
            return true;
        }
    }


    public void incCorrect () {
        currentCard.setCorrect(currentCard.getCorrect()+1);
        IncCorrectTask task = new IncCorrectTask();
        task.execute();
    }

    public class IncCorrectTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            repo.setCorrect(currentCard.getCardId(), currentCard.getDeckId(), currentCard.getCorrect());
            return true;
        }
    }

    public class AddAnswerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... bools) {
            repo.setLastFive(currentCard.getCardId(), currentCard.getDeckId(), currentCard.getLastFive());
            return true;
        }
    }


    public void generateLearntScore () {
        currentCard.getLearntScore(deckSize);
        setLearntScoreTask task = new setLearntScoreTask();
        task.execute();
    }

    public class setLearntScoreTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            repo.setLearntScore(currentCard.getCardId(),
                    currentCard.getDeckId(), currentCard.getLearntScore());
            return true;
        }
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public void setTestCards(List<Card> testCards) {
        this.testCards = testCards;
    }

}