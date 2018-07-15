package com.flashcards.android.flashcards.repo;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.flashcards.android.flashcards.lib.Card;

import java.util.List;

/**
 * Created by Abdullah Ali on 14/07/2018
 */

@Dao
public interface CardsDAO {

    @Query("SELECT 'deckId' FROM CARD WHERE 'id' = :id ")
    public LiveData<String> getDeckId(int id);

    @Query("UPDATE Card SET deckId = :newDeckId WHERE 'id' =:id AND 'deckId' = :oldDeckId")
    public void changeDeck(int id, String oldDeckId, String newDeckId);

    @Query("SELECT COUNT(*) FROM 'Card' WHERE 'deckId' = :deckId")
    public LiveData<Integer> getDeckSize(String deckId);

    @Query("SELECT * FROM 'Card' WHERE 'deckId' = :deckId")
    public LiveData<List<Card>> getAllCards (String deckId);

    @Query("SELECT * FROM CARD WHERE 'id' = :id AND 'deckId' = :deckId")
    public LiveData<Card> getCard(int id, String deckId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void setCard(Card card);

    @Delete
    public void deleteCard(int id, String deckid);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public void createCard(String deckId, String question, String answer);

    @Query("UPDATE Card SET question = :question WHERE 'id' =:id AND 'deckId' = :deckId")
    public void setQuestion(int id, String deckId, String question);

    @Query("UPDATE Card SET answer = :answer WHERE 'id' =:id AND 'deckId' = :deckId")
    public void setAnswer(int id, String deckId, String answer);


}