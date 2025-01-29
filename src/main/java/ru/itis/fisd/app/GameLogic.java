package ru.itis.fisd.app;

import lombok.Getter;
import lombok.Setter;
import ru.itis.fisd.entity.Card;
import ru.itis.fisd.entity.Deck;

@Getter
@Setter
public class GameLogic {

    private Deck deck;

    public boolean handleMove(Card playerCard, Card currentCard) {
        if (currentCard == null) {
            GameState.currentCard = playerCard;
            return true;
        }
        System.out.println("PROVERKA" + (playerCard.value() == currentCard.value() || playerCard.color() == currentCard.color()));
        return playerCard.value() == currentCard.value() || playerCard.color() == currentCard.color();
    }
}
