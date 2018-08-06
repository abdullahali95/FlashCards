package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.flashcards.android.flashcards.data.repo.MainRepo;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;
import com.flashcards.android.flashcards.lib.model.SimpleCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdullah Ali on 15/07/2018
 */
public class MainModel extends AndroidViewModel{
    private MainRepo repo;
    private LiveData<List<Deck>> decks;

    public MainModel(Application application) {
        super(application);
        this.repo = new MainRepo(application);
        decks = repo.getAllDecks();
    }

    public LiveData<List<Deck>> getAllDecks() {
        return decks;
    }


    public void insertDeck(Deck deck) {
        InsertDeckTask task = new InsertDeckTask();
        task.execute(deck);

    }

    private class InsertDeckTask extends AsyncTask<Deck, Void, Void> {
        @Override
        protected Void doInBackground(Deck... decks) {
            repo.addDeck(decks[0]);
            return null;
        }
    }


    public void insertSimpleCards(Deck newDeck, List<SimpleCard> simpleCards) {
        List<Card> cards = new ArrayList<Card>();
        String deckId = newDeck.getDeckId();
        Card card;

        for(SimpleCard simpleCard : simpleCards) {
            card = new Card(deckId, simpleCard.getQuestion(), simpleCard.getAnswer());
            cards.add(card);
        }
        InsertCardsTask task = new InsertCardsTask();

        newDeck.setCards((ArrayList<Card>) cards);
        task.execute(newDeck);
    }

    private class InsertCardsTask extends AsyncTask<Deck, Void, Void> {
        @Override
        protected Void doInBackground(Deck... decks) {
            String deckId = decks[0].getDeckId();
            repo.addCards(decks[0].getCards());

            int newDeckSize = repo.getAllCardsLength(deckId);
            repo.setDeckSize(deckId, newDeckSize);

            return null;
        }
    }


    public void deleteDeck(Deck deck) {
        DeleteDeckTask task = new DeleteDeckTask();
        task.execute(deck);

    }
    private class DeleteDeckTask extends AsyncTask<Deck, Void, Void> {
        @Override
        protected Void doInBackground(Deck... decks) {
            repo.deleteDeck(decks[0]);
            return null;
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

    public LiveData<Integer> getDeckSize (String deckId) {
        return repo.getDeckSize(deckId);
    }


}
