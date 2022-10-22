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

    public void removeCard(Card card) {
        this.cardStack.remove(card);
    }

    public Card getCard() {
        return this.cardStack.remove(0);
    }

    public Boolean containsCard(Card card) {
        return this.cardStack.contains(card);
    }

    public void addCardInPlace(Card card, int index) {
        this.cardStack.add(index, card);
    }

    public void shuffle() {
        Collections.shuffle(cardStack);
    }

    public ArrayList<Card> getCardStackAsArray() {
        return cardStack;
    }

    public int getCardStackLength() {
        return cardStack.size();
    }

    public String getCardsString() {
        String cardsString = "[";
        for (Card card : cardStack) {
            cardsString += card.getName() + ", ";
        }
        cardsString += "]";
        return cardsString;
    }

    public int getCardCount(Card card) {
        int count = 0;
        for (Card cardInStack : cardStack) {
            if (cardInStack.getName().equals(card.getName())) {
                count++;
            }
        }
        return count;
    }
}
