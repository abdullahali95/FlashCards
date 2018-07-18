package com.flashcards.android.flashcards.lib;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Abdullah Ali on 14/07/2018
 */

@Dao
public interface CardsDAO {

    @Query("UPDATE Card SET deckId = :newDeckId WHERE cardId =:id AND deckId = :oldDeckId")
    public void changeDeck(int id, String oldDeckId, String newDeckId);

    @Query("SELECT COUNT(*) FROM Card WHERE deckId = :deckId")
    public LiveData<Integer> getDeckSize(String deckId);

    @Query("SELECT * FROM Card WHERE deckId = :deckId")
    public LiveData<List<Card>> getAllCards (String deckId);

    @Query("SELECT * FROM CARD WHERE cardId = :id AND deckId = :deckId")
    public LiveData<Card> getCard(int id, String deckId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void setCard(Card card);

    @Delete
    public int deleteCard(Card card);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public Long createCard(Card card);

    @Query("UPDATE Card SET question = :question WHERE cardId =:id AND deckId = :deckId")
    public void setQuestion(int id, String deckId, String question);

    @Query("UPDATE Card SET answer = :answer WHERE cardId =:id AND deckId = :deckId")
    public void setAnswer(int id, String deckId, String answer);

}
