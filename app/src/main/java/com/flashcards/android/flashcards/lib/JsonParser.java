package com.flashcards.android.flashcards.lib;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.flashcards.android.flashcards.data.FlashCardsDatabase;
import com.flashcards.android.flashcards.lib.DAO.CardsDAO;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;
import com.flashcards.android.flashcards.lib.model.SimpleCard;
import com.flashcards.android.flashcards.lib.model.SimpleDeck;
import com.google.gson.Gson;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Abdullah Ali on 21/07/2018
 */
public class JsonParser {

    public static SimpleDeck readJson (String input) {
        SimpleDeck deck = new Gson().fromJson(input, SimpleDeck.class);

        return deck;
    }

    public static String wrtieJson (Deck deck, Context context) {
        // Get cards for the deck
        FlashCardsDatabase db = FlashCardsDatabase.getFlashCardsDB(context);
        CardsDAO cardsDAO = db.cardsDAO();

        List<Card> cards = cardsDAO.getAllDeckCards(deck.getDeckId());


        SimpleDeck sd = new SimpleDeck(deck.getName());
        SimpleCard sc;

        List<SimpleCard> simpleCards = new ArrayList<SimpleCard>();

        for (Card card : cards) {
            sc = new SimpleCard(card.getQuestion(), card.getAnswer());
            simpleCards.add(sc);
        }

        sd.setCards(simpleCards);
        return new Gson().toJson(sd);
    }

}
