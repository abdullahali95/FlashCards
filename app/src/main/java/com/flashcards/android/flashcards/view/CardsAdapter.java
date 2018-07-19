package com.flashcards.android.flashcards.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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

    public CardsAdapter(DeckInfoModel model, ArrayList<Card> cards, Context context) {
        this.model = model;
        this.cards = cards;
        this.context = context;

    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public CardsAdapter.CardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_info_rec_card, parent, false);
    //    cardQuestion = (WebView) view.findViewById(R.id.wv_card_info_question);
        return new CardsViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(CardsAdapter.CardsViewHolder holder, int position) {
        String question = cards.get(position).getQuestion();
        holder.cardView.loadUrl("about:blank");
        holder.cardView.loadData(question, "text/html", "utf-8");

    }

    @Override
    public int getItemCount() {
        if (cards == null) return 0;
        else return cards.size();
    }

    public class CardsViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener, View.OnTouchListener {
        private WebView cardView;
        private Context context;
        private Card card;

        public CardsViewHolder(View itemView, Context context) {
            super(itemView);
            this.cardView = (WebView) itemView.findViewById(R.id.wv_card_info_question);
            cardView.getSettings().setTextZoom(110);
            cardView.setBackgroundColor(itemView.getResources().getColor(R.color.cardBackground));

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            cardView.setOnTouchListener(this);

            this.context = context;
        }

        // TODO: Find a better way to delete then long press.
        @Override
        public void onClick(View view) {
            openEditor();
        }


        @Override
        public boolean onLongClick(View view) {
            deleteCard();
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP){
                openEditor();
            }

            return false;
        }


        public void openEditor() {
            card = cards.get(getAdapterPosition());

            //Switch view
            Intent intent = new Intent(this.context, CardEditActivity.class);
            intent.putExtra("cardId", card.getCardId());
            intent.putExtra("deckId", card.getDeckId());
            this.context.startActivity(intent);
        }

        public void deleteCard() {
            card = cards.get(getAdapterPosition());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Are you sure you want to delete this card?");

            // Set up the buttons
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    model.deleteCard(card);

                    // TODO: this should show up as a confirmation from Room db
                    String alert = "Card deleted";
                    Toast.makeText(context, alert, Toast.LENGTH_SHORT).show();
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
}
