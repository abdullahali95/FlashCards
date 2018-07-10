package com.flashcards.android.flashcards.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.model.DeckAdapter;
import com.flashcards.android.flashcards.repo.MockDeck;

import java.util.ArrayList;
import java.util.List;

import static com.flashcards.android.flashcards.repo.MockDeck.getFakeDeck;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Deck> decks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rec_main_activity);

        // Populate mock decks
        decks = new ArrayList<>();
        Deck mockDeck;
        for (int i = 0; i < 10; i++) {
            int size =(int) ((double) Math.random()*50);
            String name = "Deck no: " + i;
            mockDeck = getFakeDeck(name, size);
            decks.add(mockDeck);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeckAdapter(decks, this);
        recyclerView.setAdapter(adapter);



    }
}
