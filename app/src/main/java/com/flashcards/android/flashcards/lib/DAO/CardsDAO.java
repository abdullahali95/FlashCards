package com.flashcards.android.flashcards.lib.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.flashcards.android.flashcards.lib.model.Card;
import com.google.common.collect.EvictingQueue;

import java.util.List;

/**
 * Created by Abdullah Ali on 14/07/2018
 */

@Dao
public interface CardsDAO {

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void setCard(Card card);

    @Delete
    public int deleteCard(Card card);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public Long createCard(Card card);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public Long[] createCard(List<Card> card);

    @Query("UPDATE Card SET deckId = :newDeckId WHERE cardId =:id AND deckId = :oldDeckId")
    public void changeDeck(int id, String oldDeckId, String newDeckId);

    @Query("SELECT COUNT(*) FROM Card WHERE deckId = :deckId")
    public Integer getAllCardsLength(String deckId);

    @Query("SELECT * FROM Card WHERE deckId = :deckId")
    public LiveData<List<Card>> getAllCards (String deckId);

    @Query("SELECT * FROM Card WHERE deckId = :deckId")
    public List<Card> getAllDeckCards (String deckId);

    @Query("SELECT * FROM Card WHERE cardId = :cardId AND deckId = :deckId")
    public LiveData<Card> getCard(int cardId, String deckId);

    @Query("UPDATE Card SET question = :question WHERE cardId =:id AND deckId = :deckId")
    public void setQuestion(int id, String deckId, String question);

    @Query("UPDATE Card SET answer = :answer WHERE cardId =:id AND deckId = :deckId")
    public void setAnswer(int id, String deckId, String answer);


    // Progress related fields

    @Query("UPDATE Card SET attempts = :attempts WHERE cardId =:cardId AND deckId = :deckId")
    public void setAttempts(int cardId, String deckId, int attempts);

    @Query("UPDATE Card SET attempts = attempts + 1 WHERE cardId =:cardId AND deckId = :deckId")
    public void incAttempts(int cardId, String deckId);

    @Query("UPDATE Card SET correct = :correct WHERE cardId =:cardId AND deckId = :deckId")
    public void setCorrect(int cardId, String deckId, int correct);

    @Query("UPDATE Card SET correct = correct + 1 WHERE cardId =:cardId AND deckId = :deckId")
    public void incCorrect(int cardId, String deckId);

    //TODO: fix the storage of these
    @Query("UPDATE Card SET lastFive = :lastFive WHERE cardId =:cardId AND deckId = :deckId")
    public void setLastFive(int cardId, String deckId, EvictingQueue<Boolean> lastFive);

    @Query("UPDATE Card SET learntScore = :learntScore WHERE cardId =:cardId AND deckId = :deckId")
    public void setLearntScore(int cardId, String deckId, int learntScore);

}
