package com.flashcards.android.flashcards.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.DeckInfoModel;
import com.flashcards.android.flashcards.lib.misc.ProgressTransition;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;
import com.transitionseverywhere.TransitionManager;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Abdullah Ali
 *
 * This is the view class for the Deck Information Activity
 */

public class DeckInfoActivity extends AppCompatActivity {

    // Recycler View
    private DeckInfoModel deckInfoModel;
    private RecyclerView recyclerView;
    private CardsAdapter adapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private static int columns = 2;

    // View components
    private TextView deckName, deckInfo, progressText;
    private FloatingActionButton fab;
    private Button reviseButton, testButton;
    private ImageButton editButtion;
    private ProgressBar progressBar;
    private ViewGroup deckInfoHeader;
    private Context context;

    // Model objects
    private Deck deck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            public void onChanged(@Nullable final Deck deck) {
                setDeck(deck);

                // Progress Set with a delay to allow for animation to be visible.
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int progress = (int) Math.round(deck.getLs()*20);
                        TransitionManager.beginDelayedTransition(deckInfoHeader, new ProgressTransition());
                        progressBar.setProgress(progress);
                        progressText.setText("Deck Learnt: " + progress + "%");
                    }
                }, 1000);
            }
        });

        deckInfoModel.getAllCards(deckId).observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable List<Card> cards) {
                deckInfoModel.setCards(cards);
                adapter.setCards(cards);
                adapter.notifyDataSetChanged();
                recyclerView.refreshDrawableState();
            }
        });



    }

    private void initView() {
        fab = findViewById(R.id.fab_deck_info);
        reviseButton = findViewById(R.id.btn_revise_deck_info);
        testButton = findViewById(R.id.btn_test_deck_info);
        editButtion = findViewById(R.id.edit_name_btn);
        deckName = findViewById(R.id.tv_deck_name);
        deckInfo = findViewById(R.id.tv_info_deck);
        progressText = findViewById(R.id.progress_text_deck_info);
        progressBar = findViewById(R.id.progressBar_deck_info);
        deckInfoHeader = findViewById(R.id.deck_info_header);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCard();

            }
        });

        reviseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deck.getDeckSize() < 2) {
                    // Error message
                    Toast.makeText(context, "The deck must have atleast 2 cards for the revise mode", Toast.LENGTH_LONG).show();
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

        editButtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rename();
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.deck_info_rec);
        adapter = new CardsAdapter(deckInfoModel, deckInfoModel.getCards(), this, this);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(columns, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.setAdapter(adapter);

    }

    public void setDeck(Deck deck) {
        this.deck = deck;
        if (this.deck != null) {
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

            } catch (ParseException ignored) {

            }
        }
    }

    public void createCard() {
        deckInfoModel.createCard();
        Card newCard = deckInfoModel.lastEmptyCard();

        if (newCard != null) {
            //Switch view
            Intent intent = new Intent(DeckInfoActivity.this, CardEditActivity.class);
            intent.putExtra("cardId", newCard.getCardId());
            intent.putExtra("deckId", newCard.getDeckId());
            startActivity(intent);
        }
    }


    public void rename() {
        final String[] deckName = {deck.getName()};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Rename Deck");

        // Set up the input
        final EditText input = new EditText(context);
        input.setHint(deckName[0]);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        // Set up the buttons
        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deckName[0] = input.getText().toString();
                deck.setName(deckName[0]);
                deckInfoModel.renameDeck(deck);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
