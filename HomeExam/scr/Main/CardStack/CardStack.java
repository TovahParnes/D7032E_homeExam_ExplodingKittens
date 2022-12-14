package HomeExam.scr.Main.CardStack;

import java.util.*;

import HomeExam.scr.Main.Cards.Card;

public class CardStack {

    protected ArrayList<Card> cardStack;

    public CardStack() {
        this.cardStack = new ArrayList<Card>();
    }

    public void sort() {
        this.cardStack.sort(Comparator.comparing(Card::getName));
    }

    public void addCard(Card card) {
        this.cardStack.add(card);
    }

    public void removeCard(Card removeCard) {
        for (Card card : cardStack) {
            if (card.equals(removeCard)) {
                cardStack.remove(card);
                break;
            }
        }
    }

    public void removeCard(Card card, int amount) {
        for (int i = 0; i < amount; i++) {
            removeCard(card);
        }
    }

    public Card drawCard() {
        return this.cardStack.remove(0);
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

    public int getSize() {
        return cardStack.size();
    }

    public String getCardStackString() {
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
            if (cardInStack.equals(card)) {
                count++;
            }
        }
        return count;
    }

    public int getCardCount(String cardName) {
        int count = 0;
        for (Card cardInStack : cardStack) {
            if (cardInStack.getName().equals(cardName)) {
                count++;
            }
        }
        return count;
    }

    public Boolean contains(Card card) {
        return contains(card, 1);
    }

    public Boolean contains(Card card, int num) {
        if (getCardCount(card) >= num) {
            return true;
        } else
            return false;
    }

    public Boolean contains(String name) {
        return contains(name, 1);
    }

    public Boolean contains(String name, int num) {
        if (getCardCount(name) >= num) {
            return true;
        } else
            return false;
    }

    public Card getCard(String name) {
        for (Card card : cardStack) {
            if (card.getName().equals(name)) {
                return card;
            }
        }
        return null;
    }

    public Card drawCard(String name) {
        Card card = getCard(name);
        removeCard(card);
        return card;
    }

    public CardStack getUniqueCards() {
        CardStack uniqueCards = new CardStack();
        for (Card card : getCardStackAsArray()) {
            if (!uniqueCards.contains(card)) {
                uniqueCards.addCard(card);
            }
        }
        return uniqueCards;
    }

}
