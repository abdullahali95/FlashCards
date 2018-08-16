package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.flashcards.android.flashcards.data.repo.CardEditRepo;
import com.flashcards.android.flashcards.lib.model.Card;

import java.util.concurrent.ExecutionException;

/**
 * Created by Abdullah Ali on 12/07/2018
 */
public class EditModel extends AndroidViewModel {
    private Card currentCard;
    private String question;
    private String answer;
    private CardEditRepo repo;
    private boolean qside; // Indicates which side is being viewed (true = question, false = answer)

    public EditModel (Application application) {
        super(application);
        this.repo = new CardEditRepo(application);

    }

    // Methods dependant of repo

    public LiveData<Card> getCard(int cardId, String deckId) {
        return repo.getCard(cardId, deckId);
    }


    public void createCard() {
        String deckId = currentCard.getDeckId();
        CreateCardTask task = new CreateCardTask();
        task.execute(deckId);
    }

    private class CreateCardTask extends AsyncTask<String, Void, Long> {

        @Override
        protected Long doInBackground(String... strings) {
            Card newCard = new Card(strings[0]);
            repo.createCard(newCard);

            //Update deck size
            int newDeckSize = repo.getAllCardsLength(newCard.getDeckId());
            repo.setDeckSize(newCard.getDeckId(), newDeckSize);
            return null;
        }
    }

    public void updateQuestion(String question) {
        currentCard.setQuestion(question);
        UpdateCardTask task = new UpdateCardTask();
        task.execute();
    }

    public void updateAnswer (String answer) {
        currentCard.setAnswer(answer);
        UpdateCardTask task = new UpdateCardTask();
        task.execute();
    }

        private class UpdateCardTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                repo.setCard(currentCard);
                return null;
            }
        }

    public void deleteIfEmpty() {
        boolean empty = (currentCard != null &&
                currentCard.getQuestion().equals("") &&
                currentCard.getAnswer().equals(""));

        if (empty){
            DeleteCardTask task = new DeleteCardTask();
            task.execute();
        }
    }

    private class DeleteCardTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            repo.deleteCard(currentCard);

            //Update deck size
            int newDeckSize = repo.getAllCardsLength(currentCard.getDeckId());
            repo.setDeckSize(currentCard.getDeckId(), newDeckSize);

            return null;
        }
    }

    /**
     * Checks if the last card is empty
     * @return last card if it is empty. Else returns a null value.
     */
    public Card lastEmptyCard() {
        //TODO: move this to ASyncTask
        LastCardTask task = new LastCardTask();
        Card lastCard = null;

        try {
            lastCard = task.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (lastCard == null) {
            return null;
        } else if (lastCard.getAnswer().equals("") && lastCard.getQuestion().equals("")) {
            return lastCard;
        } else return null;
    }

    private class LastCardTask extends AsyncTask<Void, Void, Card> {

        @Override
        protected Card doInBackground(Void... voids) {
            return repo.getLastCard(currentCard.getDeckId());
        }
    }


    // Methods independant of repo

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public boolean isQside() {
        return qside;
    }

    public void setQside(boolean qside) {
        this.qside = qside;
    }
}
