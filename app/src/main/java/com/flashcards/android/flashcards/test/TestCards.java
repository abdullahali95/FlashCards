package com.flashcards.android.flashcards.test;

import com.flashcards.android.flashcards.lib.model.Card;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Created by abdul on 28/07/2018
 */
public class TestCards {

    private static ArrayList<Card> cards = new ArrayList<Card>();
    private static PriorityQueue<Card> testQueue = new PriorityQueue<Card>();

    public static void main(String[] args) {
        Card c1 = new Card("1", "card1", "Answer 1");
        Card c2 = new Card("1", "card2", "Answer 2");

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
    }

}
