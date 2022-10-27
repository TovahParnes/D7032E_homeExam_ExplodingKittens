package HomeExam.scr.Main.CardStack;

import java.util.HashSet;
import java.util.Set;

import HomeExam.scr.Main.Cards.Card;

public class Hand extends CardStack {

    public Hand(CardStack hand) {
        this.cardStack = hand.getCardStackAsArray();
        sort();
    }

    public void addCard(Card card) {
        this.cardStack.add(card);
        sort();
    }

    public Card drawRandomCard() {
        shuffle();
        Card card = drawCard();
        sort();
        return card;
    }
}
