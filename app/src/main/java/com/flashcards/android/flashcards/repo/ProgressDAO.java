package com.flashcards.android.flashcards.repo;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.flashcards.android.flashcards.lib.Progress;
import com.google.common.collect.EvictingQueue;

import java.util.List;

/**
 * Created by abdul on 15/07/2018
 */

@Dao
public interface ProgressDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addProgress(int cardId, String deckId);

    @Query("SELECT * FROM 'Progress' WHERE 'cardId' = :cardId AND 'deckId' = :deckId")
    public LiveData<Progress> getProgress(int cardId, String deckId);

    @Query("UPDATE Card SET deckId = :newDeckId WHERE 'cardId' =:cardId AND 'deckId' = :oldDeckId")
    public void setDeckId(int cardId, String oldDeckId, String newDeckId);

    @Query("UPDATE Card SET attempts = :attempts WHERE 'cardId' =:cardId AND 'deckId' = :deckId")
    public void setAttempts(int cardId, String deckId, int attempts);

    @Query("UPDATE Card SET attempts = attempts + 1 WHERE 'cardId' =:cardId AND 'deckId' = :deckId")
    public void incAttempts(int cardId, String deckId);

    @Query("UPDATE Card SET correct = :correct WHERE 'cardId' =:cardId AND 'deckId' = :deckId")
    public void setCorrect(int cardId, String deckId, int correct);

    @Query("UPDATE Card SET correct = correct + 1 WHERE 'cardId' =:cardId AND 'deckId' = :deckId")
    public void incCorrect(int cardId, String deckId);

    @Query("UPDATE Card SET lastTen = :lastTen WHERE 'cardId' =:cardId AND 'deckId' = :deckId")
    public void setLastTen(int cardId, String deckId, List<Boolean> lastTen);

    @Query("UPDATE Card SET learntScore = :learntScore WHERE 'cardId' =:cardId AND 'deckId' = :deckId")
    public void setLearntScore(int cardId, String deckId, int learntScore);
}
