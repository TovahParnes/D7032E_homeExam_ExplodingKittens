package HomeExam.scr.Main.CardStack;

import java.util.HashSet;
import java.util.Set;

import HomeExam.scr.Main.Cards.Card;

public class Hand extends CardStack{

    public Hand(CardStack hand){
        this.cardStack = hand.getCardStackAsArray();
    }

    public Set<Card> getHandSet()
    {
        Set<Card> handSet = new HashSet<Card>(cardStack);
        return handSet;
    }
    
}
