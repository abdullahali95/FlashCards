package com.flashcards.android.flashcards.view;

import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);

        question = findViewById(R.id.tv_question_test);
        Bundle bundle = getIntent().getExtras();
        testDeck = bundle.getParcelable("Test Deck");
        model = new TestModel(testDeck);

        currentCard = model.getCard();
        question.setText(currentCard.getQuestion());

    }
}
