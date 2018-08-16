package com.flashcards.android.flashcards.lib.misc;

import android.content.Context;

import com.flashcards.android.flashcards.data.FlashCardsDatabase;
import com.flashcards.android.flashcards.lib.DAO.CardsDAO;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;
import com.flashcards.android.flashcards.lib.model.SimpleCard;
import com.flashcards.android.flashcards.lib.model.SimpleDeck;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdullah Ali on 21/07/2018
 */
public class JsonParser {

    public static SimpleDeck readJson (String input) {
        return new Gson().fromJson(input, SimpleDeck.class);
    }

    public static String wrtieJson (Deck deck, Context context) {
        // Get cards for the deck
        FlashCardsDatabase db = FlashCardsDatabase.getFlashCardsDB(context);
        CardsDAO cardsDAO = db.cardsDAO();

        List<Card> cards = cardsDAO.getAllDeckCards(deck.getDeckId());


        SimpleDeck sd = new SimpleDeck(deck.getName());
        SimpleCard sc;

        List<SimpleCard> simpleCards = new ArrayList<>();

        for (Card card : cards) {
            sc = new SimpleCard(card.getQuestion(), card.getAnswer());
            simpleCards.add(sc);
        }

        sd.setCards(simpleCards);
        return new Gson().toJson(sd);
    }

}
