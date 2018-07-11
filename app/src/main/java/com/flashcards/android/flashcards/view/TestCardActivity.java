package com.flashcards.android.flashcards.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.model.TestModel;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.Recolor;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import static com.flashcards.android.flashcards.R.color.cardBackground;
import static com.flashcards.android.flashcards.R.color.colorAccent;
import static com.flashcards.android.flashcards.R.color.green;
import static com.flashcards.android.flashcards.R.color.red;

public class TestCardActivity extends AppCompatActivity {
    ViewGroup transitionsContainer;
    Deck testDeck;
    TestModel model;
    Card currentCard;
    WebView card;
    String question;
    String answer;

    Button skipButton;
    Button flipButton;

    Button correctButton;
    Button incorrectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_card);

        loadViews();

        currentCard = model.getCard();

        question = currentCard.getQuestion();
        card.loadUrl("about:blank");
        card.loadData(question, "text/html", "utf-8");


        // Add Event listeners to buttons
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();

            }
        });


        // TODO: combine question and answer activities into 1
        // TODO: when first clicked, should change the buttons
        flipButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flipToAnswer();
            }
        });

    }


    private void loadViews() {
        transitionsContainer = findViewById(R.id.root_question_test);
        skipButton = findViewById(R.id.btn_skip_test);
        flipButton = findViewById(R.id.btn_flip_test);
        card = findViewById(R.id.tv_question_test);

        // Set Editor
        card.setBackgroundColor(getResources().getColor(cardBackground));
        card.getSettings().setTextZoom(200);

        Bundle bundle = getIntent().getExtras();
        testDeck = bundle.getParcelable("Test Deck");
        model = new TestModel(testDeck);
    }

    private void getNewCard () {
        currentCard = model.getCard();
        question = currentCard.getQuestion();
        card.loadUrl("about:blank");
        card.loadData(question, "text/html", "utf-8");

    }

    private void skip () {
        model.skip(currentCard);
        getNewCard();
    }

    private void flipToQuestion() {

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


    private void flipToAnswer() {

        final View cardView = (findViewById(R.id.ll_card_test));

        // Change Answer

        cardView.setCameraDistance(10000);
        cardView.animate().withLayer()
            .rotationY(90)
            .setDuration(200)
            .withEndAction(
                new Runnable() {
                    @Override public void run() {

                        answer = currentCard.getAnswer();
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

    private void incorrect() {
        model.markIncorrect(currentCard);
        getNewCard();
        flipToQuestion();
    }

    private void correct () {
        model.markCorrect(currentCard);
        getNewCard();
        flipToQuestion();
    }

}
