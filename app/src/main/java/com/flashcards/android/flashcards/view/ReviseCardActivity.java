package com.flashcards.android.flashcards.view;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.ReviseModel;
import com.flashcards.android.flashcards.lib.misc.ProgressTransition;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.Recolor;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import static com.flashcards.android.flashcards.R.color.cardBackground;
import static com.flashcards.android.flashcards.R.color.colorAccent;
import static com.flashcards.android.flashcards.R.color.green;
import static com.flashcards.android.flashcards.R.color.red;
import static com.flashcards.android.flashcards.R.color.white;
import static com.flashcards.android.flashcards.R.color.white_teal;

public class ReviseCardActivity extends AppCompatActivity {

    ViewGroup transitionsContainer;
    ReviseModel model;
    WebView card;
    String question;
    String answer;
    boolean aDisplayed;

    ProgressBar progressBar;
    Button skipButton;
    Button flipButton;
    View cardView;

    Button correctButton;
    Button incorrectButton;

    GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        setContentView(R.layout.activity_revise_card);
        loadViews();

        // Get info of clicked deck
        Bundle bundle = getIntent().getExtras();
        String deckId = bundle.getString("deckId");

        model = ViewModelProviders.of(ReviseCardActivity.this).get(ReviseModel.class);
        if (model.currentDeckId() == null || !model.currentDeckId().equals(deckId)) {
            model.initQueue(deckId);
            question = model.getCurrentCard().getQuestion();
            card.loadUrl("about:blank");
            card.loadData(question, "text/html", "utf-8");
        } else if (!model.isaSide()) {
            question = model.getCurrentCard().getQuestion();
            card.loadUrl("about:blank");
            card.loadData(question, "text/html", "utf-8");
        } else {
            flipToAnswer();
        }

        gestureDetector = new GestureDetectorCompat(this, new MyGestureListener());
        initQButtons();
        updateProgress();

    }

    /**
     * Loads layout components
     */
    public void loadViews() {
        transitionsContainer = findViewById(R.id.root_revise);
        skipButton = findViewById(R.id.btn_skip_revise);
        flipButton = findViewById(R.id.btn_flip_revise);
        cardView = findViewById(R.id.ll_card_revise);
        progressBar = findViewById(R.id.progressBar);
        card = findViewById(R.id.tv_question_revise);

        // Set Editor
        card.setBackgroundColor(getResources().getColor(cardBackground));
        card.getSettings().setTextZoom(200);

    }

    /**
     * Sets listeners for Question side buttons if questions side is being viewed.
     */
    public void initQButtons() {
        if (!model.isaSide()) {
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
        question = model.skip().getQuestion();
        card.loadUrl("about:blank");
        card.getSettings().setTextZoom(200);
        card.loadData(question, "text/html", "utf-8");
    }

    /**
     * Marks the card incorrect and moves to next card.
     */
    public void incorrect() {

        question = model.markIncorrect().getQuestion();
        card.loadUrl("about:blank");
        card.getSettings().setTextZoom(200);
        card.loadData(question, "text/html", "utf-8");
        updateProgress();
        flipToQuestion();
    }

    /**
     * Marks the card correct and moves to next card.
     */
    public void correct () {
        question = model.markCorrect().getQuestion();
        card.loadUrl("about:blank");
        card.getSettings().setTextZoom(200);
        card.loadData(question, "text/html", "utf-8");
        updateProgress();
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

        model.setaSide(false);
        aDisplayed = false;

        incorrectButton.setText(R.string.flip_btn_text);
        incorrectButton.setBackgroundColor(getResources().getColor(colorAccent));

        flipButton = incorrectButton;
        incorrectButton.setOnClickListener(null);
        incorrectButton = null;

        correctButton.setText(R.string.skip_btn_text);
        correctButton.setTextColor(getResources().getColor(colorAccent));
        correctButton.setBackgroundColor(getResources().getColor(white_teal));

        skipButton = correctButton;
        correctButton.setOnClickListener(null);
        correctButton = null;
        initQButtons();

        // A bug in Android framework occasionally caused the skip button text colour change to not work.
        // This therefore forces the button to be redrawn and repainted.
        // This seems to fix the issue.
        skipButton.invalidate();
        skipButton.setTextColor(getResources().getColor(R.color.colorAccent));

    }

    public void flipToAnswer() {
        flipToAnswer(1);
    }

    /**
     *  Flips the view from question to answer.
     */
    public void flipToAnswer(int axis) {

//        final View cardView = (findViewById(R.id.ll_card_revise));
        model.setaSide(true);

        // Change Answer
        animateFlip(axis);

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
                .addTransition(new Recolor())
                .addTransition(new ChangeBounds())
                .addTransition(new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_IN)));

        skipButton.setText(R.string.correct_btn_text);
        skipButton.setBackgroundColor(getResources().getColor(green));
        skipButton.setTextColor(getResources().getColor(white));

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

    /**
     * This causes the card to do a flip animation and swap the display with question/answer.
     * @param axis The direction of the flip. 1 = left to right, -1 = right to left
     */
    public void animateFlip(int axis) {
        final int rotation = 90 * axis;
        final String cardText;

        if (aDisplayed){
            question = model.getCurrentCard().getQuestion();
            cardText = question;
            aDisplayed = false;
        } else {
            answer = model.getCurrentCard().getAnswer();
            cardText = answer;
            aDisplayed = true;
        }

        // Phones can't seem to handle the graphics of moving a card as large as one in landscape
        // When rotating. To make rotation easier in that case, camera is moved further,
        // To reduce complexity of animation.
        boolean isPortrait = (getResources().getConfiguration().orientation == 1);
        int camDistance = (isPortrait) ? 20000 : 50000;
        cardView.setCameraDistance(camDistance);
        cardView.animate().withLayer()
                .rotationY(rotation)
                .setDuration(200)
                .withEndAction(
                        new Runnable() {
                            @Override public void run() {
                                card.loadUrl("about:blank");
                                card.loadData(cardText, "text/html", "utf-8");
                                if (aDisplayed) card.getSettings().setTextZoom(150);
                                else card.getSettings().setTextZoom(200);

                                // second quarter turn
                                cardView.setRotationY(rotation * -1);
                                cardView.animate().withLayer()
                                        .rotationY(0)
                                        .setDuration(200)
                                        .start();
                            }
                        }
                ).start();
    }

    public void updateProgress() {
        TransitionManager.beginDelayedTransition(transitionsContainer, new ProgressTransition());
        progressBar.setProgress(model.getPercentageLearnt());
    }

    // Clean up methods
    @Override
    public void onBackPressed() {
        model.finish();
        finish();
    }
    @Override
    protected void onStop() {
        super.onStop();
        model.finish();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        model.finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float dist = e2.getX() - e1.getX();
            boolean triggered = false;
            int axis = 1;
            if (velocityX < -4000 && dist < -100) {
                axis = -1;
                triggered = true;
            } else if (velocityX > 4000 && dist > 100) {
                axis = 1;
                triggered = true;
            }

            if (triggered) {
                if (!model.isaSide()) {
                    flipToAnswer(axis);
                } else {
                    animateFlip(axis);
                }
            }
            return true;
        }
    }



}
