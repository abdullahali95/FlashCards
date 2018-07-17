package com.flashcards.android.flashcards.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.DeckInfoModel;
import com.flashcards.android.flashcards.ViewModel.MainModel;
import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.ViewModel.CardsAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.flashcards.android.flashcards.data.MockDeck.getFakeDeck;

public class DeckInfoActivity extends AppCompatActivity {

    // Recycler View
    private DeckInfoModel deckInfoModel;
    private RecyclerView recyclerView;
    private CardsAdapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private static int columns = 2;

    // View components
    private TextView deckName;
    private TextView deckInfo;
    private FloatingActionButton fab;
    private Button testButton;

    // Model objects
    private Deck deck;
    private ArrayList<Card> cards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_info_floating);

        // Add model
        deckInfoModel = ViewModelProviders.of(this).get(DeckInfoModel.class);


        // Get info of clicked deck
        Bundle bundle = getIntent().getExtras();
        String deckId = bundle.getString("Deck");

        final Context context = this;

//        String test = deckInfoModel.getDeck(deckId).getValue().getName();
//        Toast.makeText(this, test, Toast.LENGTH_SHORT).show();
        Log.d(deckId, "onCreate: ");

        // Add observer for changes to decks
        deckInfoModel.getDeck(deckId).observe(this, new Observer<Deck>() {
            @Override
            public void onChanged(@Nullable Deck deck) {
                setDeck(deck);
                if (deck != null) {
                    String test = deck.getName();
                    Toast.makeText(context, test, Toast.LENGTH_SHORT).show();
                }
            }
        });

        initView();

        initRecyclerView();

        deckInfoModel.getCards().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable List<Card> cards) {
                adapter.setCards(cards);
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab_deck_info);
        testButton = (Button) findViewById(R.id.btn_test_deck_info);
        deckName = (TextView) findViewById(R.id.tv_deck_name);
        deckInfo = (TextView) findViewById(R.id.tv_info_deck);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deckInfoModel.createCard();
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.deck_info_rec);
        adapter = new CardsAdapter(deckInfoModel, cards, (Context) this);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(columns, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.setAdapter(adapter);
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
        if (this.deck == null) {
            return;
        } else {
            deckInfoModel.setCurrentDeck(deck);
            deckName.setText(deck.getName());
            //TODO: add deck info and graph
        }


    }
}
