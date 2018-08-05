package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.flashcards.android.flashcards.data.repo.CardEditRepo;
import com.flashcards.android.flashcards.lib.model.Card;

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


    public void addCard(Card card) {
        Card newCard = card;
        AddCardTask task = new AddCardTask();
        task.execute(newCard);
    }

    public LiveData<Card> getCard(int cardId, String deckId) {
        return repo.getCard(cardId, deckId);
    }

    private class AddCardTask extends AsyncTask<Card, Void, Void> {

            @Override
            protected Void doInBackground(Card... cards) {
                repo.createCard(cards[0]);
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

    public void deleteCard (Card card) {
        DeleteCardTask task = new DeleteCardTask();
        task.execute(card);
    }

        private class DeleteCardTask extends AsyncTask<Card, Void, Void> {

            @Override
            protected Void doInBackground(Card... cards) {
                repo.deleteCard(cards[0]);
                return null;
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
