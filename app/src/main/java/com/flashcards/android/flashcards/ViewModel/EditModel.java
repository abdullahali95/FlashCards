package com.flashcards.android.flashcards.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.flashcards.android.flashcards.data.CardEditRepo;
import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.lib.Progress;

import java.util.List;

/**
 * Created by Abdullah Ali on 12/07/2018
 */
public class EditModel extends ViewModel {
    private LiveData<Deck> deck;
    private LiveData<List<Card>> cards;
    private Card currentCard;
    private String question;
    private String answer;
    private CardEditRepo repo;

    public EditModel (CardEditRepo repo) {
        this.repo = repo;

    }

    // Methods dependant of repo

    public LiveData<Deck> setDeck(String deckId) {
        deck = repo.getDeck(deckId);
        return deck;
    }

    public LiveData<List<Card>> setAllCards (String deckId) {
        cards = repo.getAllCards(deckId);
        return cards;
    }

    public void addCard(Card card, Progress progress) {
        //TODO: check if these need to be initialised better
        Card newCard = card;
        newCard.setProgress(progress);
        AddCardTask task = new AddCardTask();
        task.execute(newCard);
    }
        private class AddCardTask extends AsyncTask<Card, Void, Void> {

            @Override
            protected Void doInBackground(Card... cards) {
                repo.createCard(cards[0]);
                repo.addProgress(cards[0].getProgress());
                return null;
            }
        }

    public void updateQuestion(String question) {
        currentCard.setQuestion(question);
        UpdateCardTask task = new UpdateCardTask();
        task.execute(currentCard);
    }

    public void updateAnswer (int id, String answer) {
        currentCard.setAnswer(answer);
        UpdateCardTask task = new UpdateCardTask();
        task.execute(currentCard);
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

    public void viewCard(Card currentCard) {
        this.currentCard = currentCard;
        this.question = currentCard.getQuestion();
        this.answer = currentCard.getAnswer();
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

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
