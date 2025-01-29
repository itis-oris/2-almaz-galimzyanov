package ru.itis.fisd.entity;

import lombok.Getter;

import java.util.*;

@Getter
public class Deck {

    private Queue<Card> cards;

    public Deck() {
        cards = new LinkedList<>();
        List<Card> cardsList = new ArrayList<>();
        CardColor[] colors = {CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW};

        for (CardColor color : colors) {
            cardsList.add(new Card(0, color));
        }

        for (CardColor color : colors) {
            for (int value = 1; value <= 9; value++) {
                cardsList.add(new Card(value, color));
                cardsList.add(new Card(value, color));
            }
        }

        Collections.shuffle(cardsList, new Random());

        cards.addAll(cardsList);
    }
}
