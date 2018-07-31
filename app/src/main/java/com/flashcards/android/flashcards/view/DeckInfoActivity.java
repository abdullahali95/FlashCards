package com.flashcards.android.flashcards.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.DeckInfoModel;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;
import com.flashcards.android.flashcards.lib.model.Progress;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Button reviseButton;
    private Button testButton;
    private TextView progressText;
    private ProgressBar progressBar;
    private Context context;

    // Model objects
    private Deck deck;
    private ArrayList<Card> cards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_deck_info_floating);

        context = this;

        // Get info of clicked deck
        Bundle bundle = getIntent().getExtras();
        String deckId = bundle.getString("Deck");

        // Add model
        deckInfoModel = ViewModelProviders.of(this).get(DeckInfoModel.class);
        deckInfoModel.getDeck(deckId);

        initView();
        initRecyclerView();

        // Add observer for changes to decks
        deckInfoModel.getDeck(deckId).observe(this, new Observer<Deck>() {
            @Override
            public void onChanged(@Nullable Deck deck) {
                setDeck(deck);
            }
        });

        deckInfoModel.getAllCards(deckId).observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable List<Card> cards) {
                adapter.setCards(cards);
                adapter.notifyDataSetChanged();
                recyclerView.refreshDrawableState();
            }
        });


    }

    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab_deck_info);
        reviseButton = (Button) findViewById(R.id.btn_revise_deck_info);
        testButton = (Button) findViewById(R.id.btn_test_deck_info);
        deckName = (TextView) findViewById(R.id.tv_deck_name);
        deckInfo = (TextView) findViewById(R.id.tv_info_deck);
        progressText = (TextView) findViewById(R.id.progress_text_deck_info);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_deck_info);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deckInfoModel.createCard();

            }
        });

        reviseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deck.getDeckSize() < 2) {
                    // Error message
                    Toast.makeText(context, "The deck must have atleast 2 cards for the test mode", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(DeckInfoActivity.this, ReviseCardActivity.class);
                    intent.putExtra("deckId", deck.getDeckId());
                    startActivity(intent);
                }
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deck.getDeckSize() < 2) {
                    // Error message
                    Toast.makeText(context, "The deck must have atleast 2 cards for the test mode", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(DeckInfoActivity.this, TestCardActivity.class);
                    intent.putExtra("deckId", deck.getDeckId());
                    startActivity(intent);
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.deck_info_rec);
        adapter = new CardsAdapter(deckInfoModel, cards, (Context) this, this);

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

            // Set time since last tested
            Date convertedDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            try {
                convertedDate = dateFormat.parse(deck.getCreated());
                PrettyTime p  = new PrettyTime();
                String datetime= p.format(convertedDate);
                datetime = "Created: " + datetime;
                deckInfo.setText(datetime);

            } catch (ParseException e) {

            }

            //TODO: Fix progress for when cards not properly tested.
            // TODO: Add aveAttempts to Deck class, and if less then 1, set learnt as 0%
            int progress = (int) Math.round(deck.getEf()*100);
            progressBar.setProgress(progress);
            progressText.setText("Deck Learnt: " + progress + "%");


        }


    }
}
