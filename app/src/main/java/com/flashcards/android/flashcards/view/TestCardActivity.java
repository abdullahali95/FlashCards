package com.flashcards.android.flashcards.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.TestModel;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.Recolor;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import static com.flashcards.android.flashcards.R.color.cardBackground;
import static com.flashcards.android.flashcards.R.color.colorAccent;
import static com.flashcards.android.flashcards.R.color.colorPrimary;
import static com.flashcards.android.flashcards.R.color.green;
import static com.flashcards.android.flashcards.R.color.red;

/**
 * Created by Abdullah Ali *
 *
 */
public class TestCardActivity extends AppCompatActivity {
    ViewGroup transitionsContainer;
    TestModel model;
    WebView card;
    String question;
    String answer;
    View cardView;
    Button skipButton, flipButton, correctButton, incorrectButton;
    TextView cardsTested, cardsTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }

        setContentView(R.layout.activity_test_card);

        loadViews();

        // Get info of clicked deck
        Bundle bundle = getIntent().getExtras();
        String deckId = bundle.getString("deckId");

        model = ViewModelProviders.of(TestCardActivity.this).get(TestModel.class);

        if (model.currentDeckId() == null || !model.currentDeckId().equals(deckId)) {
            model.initQueue(deckId);
            question = model.getCurrentCard().getQuestion();
            card.loadUrl("about:blank");
            card.loadData(question, "text/html", "utf-8");

            String cTotal = "/ " + model.getDeckSize();
            cardsTotal.setText(cTotal);
        } else if (model.isQSide()) {
            question = model.getCurrentCard().getQuestion();
            card.loadUrl("about:blank");
            card.loadData(question, "text/html", "utf-8");

            String cTested = String.valueOf(model.getTotalAttempted() + 1);
            cardsTested.setText(cTested);
            String cTotal = "/ " + model.getDeckSize();
            cardsTotal.setText(cTotal);
        } else {
            flipToAnswer();
            String cTested = String.valueOf(model.getTotalAttempted() + 1);
            cardsTested.setText(cTested);
            String cTotal = "/ " + model.getDeckSize();
            cardsTotal.setText(cTotal);

        }

        initQButtons();

    }

    /**
     * Loads layout components
     */
    public void loadViews() {
        transitionsContainer = findViewById(R.id.root_test);
        skipButton = findViewById(R.id.btn_skip_test);
        flipButton = findViewById(R.id.btn_flip_test);
        cardView = findViewById(R.id.ll_card_test);
        card = findViewById(R.id.tv_question_test);
        cardsTested = findViewById(R.id.tv_cards_tested);
        cardsTotal = findViewById(R.id.tv_cards_total);

        // Set Editor
        card.setBackgroundColor(getResources().getColor(cardBackground));
        card.getSettings().setTextZoom(200);

    }

    public void initQButtons () {
        if (model.isQSide()) {
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
    }

    /**
     * Gets a new card from the model
     */
    public void skip () {
        if (model.queueEmpty()) {
            Toast.makeText(this, "Last card cannot be skipped", Toast.LENGTH_LONG).show();
        } else {
            question = model.skip().getQuestion();
            card.loadUrl("about:blank");
            card.getSettings().setTextZoom(200);
            card.loadData(question, "text/html", "utf-8");
        }
    }

    /**
     * Marks the card incorrect and moves to next card.
     */
    public void incorrect() {
        if (model.queueEmpty()) {
            model.markIncorrect();
            endTest();
        } else {
            question = model.markIncorrect().getQuestion();
            card.loadUrl("about:blank");
            card.getSettings().setTextZoom(200);
            card.loadData(question, "text/html", "utf-8");
            flipToQuestion();
        }
    }

    /**
     * Marks the card correct and moves to next card.
     */
    public void correct () {
        if (model.queueEmpty()) {
            model.markCorrect();
            endTest();
        } else {
            question = model.markCorrect().getQuestion();
            card.loadUrl("about:blank");
            card.getSettings().setTextZoom(200);
            card.loadData(question, "text/html", "utf-8");
            flipToQuestion();
        }
    }

    private void endTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You scored " + model.getScore()+ "%");

        int correct = model.getCorrect();
        int total = model.getTotalAttempted();
        builder.setMessage(correct + " out of " + total + " answers were correct.");

        // Set up the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alert = builder.show();
        alert.setCancelable(false);

    }


    /**
     * Changes the buttons from:
     *      Incorrect --> FLIP
     *      Correct --> SKIP
     * Used when an answer has been marked correct/incorrect,
     * and we are resestting the view for a new question
     */
    public void flipToQuestion() {
        model.setaSide(false);
        // Change Incorrect --> FLIP Button
        TransitionManager.beginDelayedTransition(transitionsContainer, new TransitionSet()
                .addTransition(new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))
                .addTransition(new Recolor())
                .addTransition(new ChangeBounds()));


        incorrectButton.setText(R.string.flip_btn_text);
        incorrectButton.setBackgroundColor(getResources().getColor(colorAccent));

        flipButton = incorrectButton;
        incorrectButton.setOnClickListener(null);
        incorrectButton = null;

        // Change Correct --> SKIP Button
        TransitionManager.beginDelayedTransition(transitionsContainer, new TransitionSet()
                .addTransition(new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))
                .addTransition(new Recolor())
                .addTransition(new ChangeBounds()));


        correctButton.setText(R.string.skip_btn_text);
        correctButton.setBackgroundColor(getResources().getColor(colorPrimary));

        skipButton = correctButton;
        correctButton.setOnClickListener(null);
        correctButton = null;

        initQButtons();

        // Update cardsTested
        String cTested = String.valueOf(model.getTotalAttempted() + 1);
        cardsTested.setText(cTested);
    }


    /**
     *  Flips the view from question to answer.
     */
    public void flipToAnswer() {
        model.setaSide(true);
        final View cardView = (findViewById(R.id.ll_card_test));

        // Phones can't seem to handle the graphics of moving a card as large as one in landscape
        // When rotating. To make rotation easier in that case, camera is moved further,
        // To reduce complexity of animation.
        boolean isPortrait = (getResources().getConfiguration().orientation == 1);
        int camDistance = (isPortrait) ? 20000 : 50000;
        cardView.setCameraDistance(camDistance);
        cardView.animate().withLayer()
            .rotationY(90)
            .setDuration(200)
            .withEndAction(
                new Runnable() {
                    @Override public void run() {

                        answer = model.getCurrentCard().getAnswer();
                        card.loadUrl("about:blank");
                        card.getSettings().setTextZoom(150);
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


        flipButton.setText(R.string.incorrect_btn_text);
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


        skipButton.setText(R.string.correct_btn_text);
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
