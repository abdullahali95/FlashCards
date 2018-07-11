package com.flashcards.android.flashcards.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.model.TestModel;

import static com.flashcards.android.flashcards.R.color.cardBackground;

public class TestCardActivity extends AppCompatActivity {
    Deck testDeck;
    TestModel model;
    Card currentCard;
    WebView card;
    Button skipButton;
    Button flipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_card);

        skipButton = findViewById(R.id.btn_skip_test);
        flipButton = findViewById(R.id.btn_flip_test);

        card = findViewById(R.id.tv_question_test);
        card.setBackgroundColor(getResources().getColor(cardBackground));
        card.getSettings().setTextZoom(200);


        Bundle bundle = getIntent().getExtras();
        testDeck = bundle.getParcelable("Test Deck");
        model = new TestModel(testDeck);

        currentCard = model.getCard();

        //TODO: test if this works with all html tags
       // question.setText(Html.fromHtml(currentCard.getQuestion()));

        String q = currentCard.getQuestion();
        card.loadData(q, "text/html", "utf-8");


        // Add Event listeners to buttons
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.skip(currentCard);
                currentCard = model.getCard();
                String q = currentCard.getQuestion();
                card.loadData(q, "text/html", "utf-8");

            }
        });


        // TODO: combine question and answer activities into 1
        flipButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

    }
}
