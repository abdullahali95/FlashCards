package com.flashcards.android.flashcards.ViewModel;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.lib.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for the individual cards displayed on the Deck Info Activity recycler view.
 *
 * Created by Abdullah Ali on 12/07/2018
 */
public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

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
    public CardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_info_rec_card, parent, false);
    //    cardQuestion = (WebView) view.findViewById(R.id.wv_card_info_question);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(CardsAdapter.ViewHolder holder, int position) {
        String question = cards.get(position).getQuestion();
        holder.cardView.loadUrl("about:blank");
        holder.cardView.loadData(question, "text/html", "utf-8");
    }

    @Override
    public int getItemCount() {
        if (cards == null) return 0;
        else return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private WebView cardView;
        private Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.cardView = (WebView) itemView.findViewById(R.id.wv_card_info_question);
            cardView.getSettings().setTextZoom(120);

            this.context = context;
        }
    }
}
