package com.flashcards.android.flashcards.test;

import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.PriorityQueue;

/**
 * Created by Abdullah Ali on 28/07/2018
 * Creates mock cards, used to test the short-term learning algorithm
 */
public class TestCards {

    private static ArrayList<Card> cards = new ArrayList<Card>();
    private static PriorityQueue<Card> testQueue = new PriorityQueue<Card>();

    public static void main(String[] args) {
        Card c1 = new Card("1");
        c1.setQuestion("card1");
        c1.setAnswer("Answer 1");
        Card c2 = new Card("1");
        c2.setQuestion("card2");
        c2.setAnswer("Answer 2");

        testQueue.add(c1);
        testQueue.add(c2);

        Card currentCard;

        currentCard = testQueue.poll();
        currentCard.addAnswer(Boolean.TRUE);
        System.out.println(currentCard.getLastFive());
        testQueue.add(currentCard);

        currentCard = testQueue.poll();
        currentCard.addAnswer(Boolean.FALSE);
        System.out.println(currentCard.getLastFive());
        testQueue.add(currentCard);

        currentCard = testQueue.poll();
        currentCard.addAnswer(Boolean.TRUE);
        System.out.println(currentCard.getLastFive());
        testQueue.add(currentCard);
        System.out.println(testQueue.peek().getLastFive().toString());


        currentCard = testQueue.poll();
        currentCard.addAnswer(Boolean.FALSE);
        System.out.println(currentCard.getLastFive());
        testQueue.add(currentCard);

        currentCard = testQueue.poll();
        currentCard.addAnswer(Boolean.TRUE);
        System.out.println(currentCard.getLastFive());
        testQueue.add(currentCard);

        currentCard = testQueue.poll();
        currentCard.addAnswer(Boolean.FALSE);
        System.out.println(currentCard.getLastFive());
        testQueue.add(currentCard);


        Deck deck = new Deck("TestDeck");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Calendar cal = Calendar.getInstance();
        String date = sf.format(cal.getTime());

        System.out.println((deck.getNextTestDue() == null));
        deck.setNextTestDue(date);
        System.out.println((deck.getNextTestDue() == null));
    }

}
