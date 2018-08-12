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
    void setCard(Card card);

    @Delete
    int deleteCard(Card card);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    Long createCard(Card card);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    Long[] createCard(List<Card> card);

    @Query("UPDATE Card SET deckId = :newDeckId WHERE cardId =:id AND deckId = :oldDeckId")
    void changeDeck(int id, String oldDeckId, String newDeckId);

    @Query("SELECT COUNT(*) FROM Card WHERE deckId = :deckId")
    Integer getAllCardsLength(String deckId);

    @Query("SELECT * FROM Card WHERE deckId = :deckId")
    LiveData<List<Card>> getAllCards(String deckId);

    @Query("SELECT * FROM Card WHERE deckId = :deckId")
    List<Card> getAllDeckCards(String deckId);

    @Query("SELECT * FROM Card WHERE deckId = :deckId ORDER BY cardId DESC LIMIT 1")
    Card getLastCard(String deckId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void setAllCards(List<Card> allCards);

    @Query("SELECT * FROM Card WHERE cardId = :cardId AND deckId = :deckId")
    LiveData<Card> getCard(int cardId, String deckId);

    @Query("UPDATE Card SET question = :question WHERE cardId =:id AND deckId = :deckId")
    void setQuestion(int id, String deckId, String question);

    @Query("UPDATE Card SET answer = :answer WHERE cardId =:id AND deckId = :deckId")
    void setAnswer(int id, String deckId, String answer);


    // Progress related fields

    @Query("UPDATE Card SET attempts = :attempts WHERE cardId =:cardId AND deckId = :deckId")
    void setAttempts(int cardId, String deckId, int attempts);

    @Query("UPDATE Card SET attempts = attempts + 1 WHERE cardId =:cardId AND deckId = :deckId")
    void incAttempts(int cardId, String deckId);

    @Query("UPDATE Card SET correct = :correct WHERE cardId =:cardId AND deckId = :deckId")
    void setCorrect(int cardId, String deckId, int correct);

    @Query("UPDATE Card SET correct = correct + 1 WHERE cardId =:cardId AND deckId = :deckId")
    void incCorrect(int cardId, String deckId);

    @Query("UPDATE Card SET lastFive = :lastFive WHERE cardId =:cardId AND deckId = :deckId")
    void setLastFive(int cardId, String deckId, EvictingQueue<Boolean> lastFive);

    @Query("UPDATE Card SET learntScore = :learntScore WHERE cardId =:cardId AND deckId = :deckId")
    void setLearntScore(int cardId, String deckId, int learntScore);

}
