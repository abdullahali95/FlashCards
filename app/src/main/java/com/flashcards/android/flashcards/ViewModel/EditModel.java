package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.flashcards.android.flashcards.data.repo.CardEditRepo;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Progress;

/**
 * Created by Abdullah Ali on 12/07/2018
 */
public class EditModel extends AndroidViewModel {
    private LiveData<Card> currentCard;
    private String question;
    private String answer;
    private CardEditRepo repo;

    public EditModel (Application application) {
        super(application);
        this.repo = new CardEditRepo(application);

    }

    // Methods dependant of repo


    public void addCard(Card card) {
        //TODO: check if these need to be initialised better
        Card newCard = card;
        AddCardTask task = new AddCardTask();
        task.execute(newCard);
    }

    public LiveData<Card> getCard(int cardId, String deckId) {
        currentCard = repo.getCard(cardId, deckId);
        return currentCard;
    }

    private class AddCardTask extends AsyncTask<Card, Void, Void> {

            @Override
            protected Void doInBackground(Card... cards) {
                repo.createCard(cards[0]);
                return null;
            }
        }

    public void updateQuestion(String question) {
        Card newCard = currentCard.getValue();
        newCard.setQuestion(question);
        UpdateCardTask task = new UpdateCardTask();
        task.execute(newCard);
    }

    public void updateAnswer (String answer) {
        Card newCard = currentCard.getValue();
        newCard.setAnswer(answer);
        UpdateCardTask task = new UpdateCardTask();
        task.execute(newCard);
    }

        private class UpdateCardTask extends AsyncTask<Card, Void, Void> {

            @Override
            protected Void doInBackground(Card... cards) {
                repo.setCard(cards[0]);
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
}
