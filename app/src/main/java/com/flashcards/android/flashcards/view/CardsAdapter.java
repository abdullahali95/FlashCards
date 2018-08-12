package com.flashcards.android.flashcards.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.DeckInfoModel;
import com.flashcards.android.flashcards.lib.model.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for the individual cards displayed on the Deck Info Activity recycler view.
 *
 * Created by Abdullah Ali on 12/07/2018
 */
public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardsViewHolder> {

    private List<Card> cards;
    private DeckInfoModel model;
    private Context context;
    private Activity activity;

    CardsAdapter(DeckInfoModel model, List<Card> cards, Context context, Activity activity) {
        this.model = model;
        this.cards = cards;
        this.context = context;
        this.activity = activity;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardsAdapter.CardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_info_rec_card, parent, false);
    //    cardQuestion = (WebView) view.findViewById(R.id.wv_card_info_question);
        return new CardsViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsAdapter.CardsViewHolder holder, int position) {
        String question = cards.get(position).getQuestion();
        holder.cardView.setText(Html.fromHtml(question));

    }

    @Override
    public int getItemCount() {
        if (cards == null) return 0;
        else return cards.size();
    }

    public class CardsViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private TextView cardView;
        private Context context;
        private Card card;

        private CardsViewHolder(View itemView, Context context) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.wv_card_info_question);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            this.context = context;
        }

        @Override
        public void onClick(View view) {
            openEditor();
        }


        @Override
        public boolean onLongClick(View view) {
            deleteCard();
            return true;
        }

        void openEditor() {
            card = cards.get(getAdapterPosition());

            //Switch view
            Intent intent = new Intent(this.context, CardEditActivity.class);
            intent.putExtra("cardId", card.getCardId());
            intent.putExtra("deckId", card.getDeckId());

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(activity, cardView, "card_transition");

            this.context.startActivity(intent, options.toBundle());
        }

        void deleteCard() {
            card = cards.get(getAdapterPosition());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Are you sure you want to delete this card?");

            // Set up the buttons
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    model.deleteCard(card);

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.show();

            alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(context.getResources().getColor(R.color.redBg));
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.white));

        }
    }
}
