package com.flashcards.android.flashcards.model;

import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;

/**
 * Created by abdul on 12/07/2018
 */
public class EditModel {
    private Deck deck;
    private Card currentCard;
    private String question;
    private String answer;

    public EditModel (String uuid) {
        getDeck(uuid);

    }

    private void getDeck(String uuid) {

    }
}
