package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.flashcards.android.flashcards.data.repo.MainRepo;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;
import com.flashcards.android.flashcards.lib.model.SimpleCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdullah Ali on 15/07/2018
 *
 * This is the viewmodel for the Home Activity
 */
public class MainModel extends AndroidViewModel{
    private MainRepo repo;
    private List<Deck> decks;
    private boolean deckImported;

    public MainModel(Application application) {
        super(application);
        this.repo = new MainRepo(application);
    }

    public LiveData<List<Deck>> getAllDecks() {
        return repo.getAllDecks();
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
            card.setHint(simpleCard.getHint());
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

    /**
     * This recursive method checks if the name of the new deck is valid.
     * If not, it corrects the name. It check for if:
     *      There are empty spaces at the end of the name
     *      If a deck of the same name already exists. In this case, a number is added.
     *
     * @param name The deck name requested
     * @return The validated deck name
     */
    public String validateName(String name) {
        int length = name.length();
        if (name.isEmpty() || name.equals("")) {
            return validateName("Untitled");
        } else if (name.charAt(length-1) == ' '){
            return validateName(name.substring(0, length-1));
        } else {
            String newName = name;
            for (Deck deck : decks) {
                if (deck.getName().equalsIgnoreCase(newName)) {
                    newName = enumerateName(name, 2);
                }
            }
            return newName;
        }
    }

    /**
     * Helper recursive method for validateName method
     * Adds a number to the name of deck, to return a deck name that doesn't currently exist
     * @param name String name to be checked
     * @param num The number to be added to the name
     * @return
     */
    private String enumerateName(String name, int num) {
        String newName = name + " " + num;
        for (Deck deck : decks) {
            if (deck.getName().equalsIgnoreCase(newName)) {
                Log.d(name, "name: ");
                Log.d(newName, "new Name: ");
                newName = enumerateName(name, ++num);
            }
        }
        return newName;
    }

    public boolean deckImported() {
        return deckImported;
    }

    public void setDeckImported(boolean deckImported) {
        this.deckImported = deckImported;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }
}
