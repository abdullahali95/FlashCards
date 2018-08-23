package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.flashcards.android.flashcards.data.repo.DeckInfoRepo;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Abdullah Ali on 16/07/2018
 *
 * This is the viewmodel class for the Deck Information Activity.
 */
public class DeckInfoModel extends AndroidViewModel {
    private DeckInfoRepo repo;
    private Deck currentDeck;
    private List<Card> cards;

    public DeckInfoModel(@NonNull Application application) {
        super(application);
        this.repo = new DeckInfoRepo(application);
    }

    /**
     * Sets current deck and its cards in the model
     * @param deckId
     * @return current Deck
     */
    public LiveData<Deck> getDeck (String deckId) {
        currentDeck = repo.getDeck(deckId).getValue();
        getAllCards(deckId);
        return repo.getDeck(deckId);
    }

    public LiveData<List<Card>> getAllCards (String deckId) {
        return repo.getAllCards(deckId);
    }

    public void createCard() {
        String deckId = currentDeck.getDeckId();
        CreateCardTask task = new CreateCardTask();
        task.execute(deckId);
    }

    private class CreateCardTask extends AsyncTask<String, Void, Long> {

        @Override
        protected Long doInBackground(String... strings) {
            Card newCard = new Card(strings[0]);
            Long result = repo.createCard(newCard);

            //Update deck size
            int newDeckSize = repo.getAllCardsLength(newCard.getDeckId());
            repo.setDeckSize(newCard.getDeckId(), newDeckSize);
            return result;
        }
    }


    public void deleteCard(Card card) {
        DeleteCardTask task = new DeleteCardTask();
        task.execute(card);
    }

    private class DeleteCardTask extends AsyncTask<Card, Void, Integer> {

        @Override
        protected Integer doInBackground(Card... cards) {
            Card card = cards[0];
            int result = repo.deleteCard(card);

            //Update deck size
            int newDeckSize = repo.getAllCardsLength(card.getDeckId());
            repo.setDeckSize(card.getDeckId(), newDeckSize);
            return result;
        }
    }

    /**
     * Checks if the last card is empty
     * @return last card if it is empty. Else returns a null value.
     */
    public Card lastEmptyCard() {
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
            return repo.getLastCard(currentDeck.getDeckId());
        }
    }

    public void renameDeck(Deck deck) {
        RenameDeckTask task = new RenameDeckTask();
        task.execute(deck);
    }

    private class RenameDeckTask extends AsyncTask<Deck, Void, Void> {
        @Override
        protected Void doInBackground(Deck... decks) {
            repo.updateDeck(decks[0]);
            return null;
        }
    }

    // DAO independant methods

    public void setCurrentDeck(Deck currentDeck) {
        this.currentDeck = currentDeck;
    }
    public List<Card> getCards() {
        return cards;
    }
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }


}
