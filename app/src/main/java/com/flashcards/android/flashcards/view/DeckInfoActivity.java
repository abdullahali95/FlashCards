package com.flashcards.android.flashcards.view;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Button;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.ViewModel.CardsAdapter;

import static com.flashcards.android.flashcards.data.MockDeck.getFakeDeck;

public class DeckInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardsAdapter adapter;
    private Deck deck;
    private static int columns = 2;
    private FloatingActionButton fab;
    private Button testButton;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_info_floating);

        fab = (FloatingActionButton) findViewById(R.id.fab_deck_info);
        testButton = (Button) findViewById(R.id.btn_test_deck_info);

        deck = getFakeDeck();

        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.deck_info_rec);
        adapter = new CardsAdapter(deck.getCards(), (Context) this);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(columns, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.setAdapter(adapter);
    }
}
