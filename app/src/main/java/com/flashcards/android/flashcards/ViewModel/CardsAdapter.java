package com.flashcards.android.flashcards.ViewModel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.lib.Card;

import java.util.ArrayList;

/**
 * Created by abdul on 12/07/2018
 */
public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    private ArrayList<Card> cards;
    private Context context;

    public CardsAdapter(ArrayList<Card> cards, Context context) {
        this.cards = cards;
        this.context = context;
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
        return cards.size();
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
