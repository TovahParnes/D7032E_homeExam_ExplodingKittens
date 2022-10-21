package HomeExam.scr.Main.CardStack;

import java.util.*;

import HomeExam.scr.Main.Cards.Card;

public class CardStack {

    protected ArrayList<Card> cardStack;

    public CardStack() {
        this.cardStack = new ArrayList<Card>();
    }

    public void addCard(Card card) {
        this.cardStack.add(card);
    }

    public void addCards(ArrayList<Card> cards) {
        this.cardStack.addAll(cards);
    }

    public Card getCard() {
        return this.cardStack.remove(0);
    }

    public void addCardInPlace(Card card, int index) {
        this.cardStack.add(index, card);
    }

    public void shuffle()
    {
        Collections.shuffle(cardStack);
    }

    public ArrayList<Card> getCardStack()
    {
        return cardStack;
    }

    public Set<Card> getHandSet()
    {
        Set<Card> handSet = new HashSet<Card>(cardStack);
        return handSet;
    }

}
