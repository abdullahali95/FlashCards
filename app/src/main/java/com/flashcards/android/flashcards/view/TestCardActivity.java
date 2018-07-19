package com.flashcards.android.flashcards.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;
import com.flashcards.android.flashcards.ViewModel.TestModel;
import com.transitionseverywhere.*;

import java.util.List;

import static com.flashcards.android.flashcards.R.color.*;

/**
 * Created by Abdullah Ali *
 *
 *
 */
public class TestCardActivity extends AppCompatActivity {
    ViewGroup transitionsContainer;
    TestModel model;
    WebView card;
    String question;
    String answer;

    Button skipButton;
    Button flipButton;
    View cardView;

    Button correctButton;
    Button incorrectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_card);

        loadViews();

        // Get info of clicked deck
        Bundle bundle = getIntent().getExtras();
        String deckId = bundle.getString("deckId");
        Log.d(deckId, "onCreate: ");

        model = ViewModelProviders.of(this).get(TestModel.class);

        model.setTestDeck(deckId);

        model.getAllCards(deckId).observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable List<Card> cards) {
                if (cards.size() > 0 && model.getCurrentCard() == null) {
                    model.initQueue(cards);
                    model.setTestCards(cards);
                    question = model.getCurrentCard().getQuestion();
                    card.loadUrl("about:blank");
                    card.loadData(question, "text/html", "utf-8");
                    Log.d("updated QUEUE", "onChanged: ");

                }
            }
        });


        // TODO: add method to look through webview String for '#' and replace it with '%23'


        // Add Event listeners to buttons
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });

        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipToAnswer();
            }
        });

    }

    /**
     * Loads layout components
     */
    public void loadViews() {
        transitionsContainer = (ViewGroup) findViewById(R.id.root_question_test);
        skipButton = (Button) findViewById(R.id.btn_skip_test);
        flipButton = (Button) findViewById(R.id.btn_flip_test);
        cardView = (View) findViewById(R.id.ll_card_test);

        //TODO: Test if WebView scrolls
        card = (WebView) findViewById(R.id.tv_question_test);

        // Set Editor
        card.setBackgroundColor(getResources().getColor(cardBackground));
        card.getSettings().setTextZoom(200);

    }

    /**
     * Loads a new card from the model, and displays its question,
     * Used for skip, correct, and incorrect buttons
     */
    public void getNewCard () {

        // TODO: Add slide animation for card
        question = model.getCard().getQuestion();
        card.loadUrl("about:blank");
        card.loadData(question, "text/html", "utf-8");

    }



    /**
     * Gets a new card from the model
     */
    public void skip () {
        model.skip();
        getNewCard();
    }

    /**
     * Marks the card incorrect and moves to next card.
     */
    public void incorrect() {
        model.markIncorrect();
        getNewCard();
        flipToQuestion();
    }

    /**
     * Marks the card correct and moves to next card.
     */
    public void correct () {
        model.markCorrect();
        getNewCard();
        flipToQuestion();
    }


    /**
     * Changes the buttons from:
     *      Incorrect --> FLIP
     *      Correct --> SKIP
     * Used when an answer has been marked correct/incorrect,
     * and we are resestting the view for a new question
     */
    public void flipToQuestion() {

        // Change Incorrect --> FLIP Button
        TransitionManager.beginDelayedTransition(transitionsContainer, new TransitionSet()
                .addTransition(new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))
                .addTransition(new Recolor())
                .addTransition(new ChangeBounds()));


        incorrectButton.setText("FLIP");

        TransitionManager.beginDelayedTransition(transitionsContainer, new Recolor());
        incorrectButton.setBackgroundColor(getResources().getColor(colorAccent));

        flipButton = incorrectButton;
        incorrectButton.setOnClickListener(null);
        incorrectButton = null;

        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipToAnswer();
            }
        });


        // Change Correct --> SKIP Button
        TransitionManager.beginDelayedTransition(transitionsContainer, new TransitionSet()
                .addTransition(new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))
                .addTransition(new Recolor())
                .addTransition(new ChangeBounds()));


        correctButton.setText("SKIP");

        TransitionManager.beginDelayedTransition(transitionsContainer, new Recolor());
        correctButton.setBackgroundColor(getResources().getColor(colorAccent));

        skipButton = correctButton;
        correctButton.setOnClickListener(null);
        correctButton = null;

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });

    }


    /**
     *  Flips the view from question to answer.
     */
    public void flipToAnswer() {

        final View cardView = (findViewById(R.id.ll_card_test));

        // Change Answer

        cardView.setCameraDistance(10000);
        cardView.animate().withLayer()
            .rotationY(90)
            .setDuration(200)
            .withEndAction(
                new Runnable() {
                    @Override public void run() {

                        answer = model.getCurrentCard().getAnswer();
                        card.loadUrl("about:blank");
                        card.loadData(answer, "text/html", "utf-8");

                        // second quarter turn
                        cardView.setRotationY(-90);
                        cardView.animate().withLayer()
                            .rotationY(0)
                            .setDuration(200)
                            .start();
                    }
                }
            ).start();


        // Change Flip --> Incorrect Button
        TransitionManager.beginDelayedTransition(transitionsContainer, new TransitionSet()
                        .addTransition(new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))
                        .addTransition(new Recolor())
                        .addTransition(new ChangeBounds()));


        flipButton.setText("Incorrect");

        TransitionManager.beginDelayedTransition(transitionsContainer, new Recolor());
        flipButton.setBackgroundColor(getResources().getColor(red));

        incorrectButton = flipButton;
        flipButton.setOnClickListener(null);
        flipButton = null;

        incorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incorrect();
            }
        });


        // Change Skip --> Correct Button
        TransitionManager.beginDelayedTransition(transitionsContainer, new TransitionSet()
                .addTransition(new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))
                .addTransition(new Recolor())
                .addTransition(new ChangeBounds()));


        skipButton.setText("Correct");

        TransitionManager.beginDelayedTransition(transitionsContainer, new Recolor());
        skipButton.setBackgroundColor(getResources().getColor(green));

        correctButton = skipButton;
        skipButton.setOnClickListener(null);
        skipButton = null;

        correctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct();
            }
        });


    }



}
