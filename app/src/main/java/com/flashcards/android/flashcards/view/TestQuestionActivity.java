package com.flashcards.android.flashcards.view;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.model.TestModel;

public class TestQuestionActivity extends AppCompatActivity {
    Deck testDeck;
    TestModel model;
    Card currentCard;
    TextView question;
    Button skipButton;
    Button flipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);

        skipButton = findViewById(R.id.btn_skip_test);
        flipButton = findViewById(R.id.btn_flip_test);

        question = findViewById(R.id.tv_question_test);
        Bundle bundle = getIntent().getExtras();
        testDeck = bundle.getParcelable("Test Deck");
        model = new TestModel(testDeck);

        currentCard = model.getCard();
        question.setText(currentCard.getQuestion());


        // Add Event listeners to buttons
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.skip(currentCard);
                currentCard = model.getCard();
                question.setText(currentCard.getQuestion());
            }
        });


        // TODO: combine question and answer activities into 1
        flipButton.setOnClickListener(new View.OnClickListener() {
            public Context context;

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestQuestionActivity.this, TestAnswerActivity.class);
                intent.putExtra("Test Deck", testDeck);
                intent.putExtra("Test card", currentCard);
                intent.putExtra("Test model", model);
                startActivity(intent);
            }
        });

    }
}
