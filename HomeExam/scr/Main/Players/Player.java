package HomeExam.scr.Main.Players;

import java.io.*;
import java.net.*;
import java.util.*;

import HomeExam.scr.Main.View;
import HomeExam.scr.Main.CardStack.CardStack;
import HomeExam.scr.Main.CardStack.Hand;
import HomeExam.scr.Main.Cards.Card;

public class Player {

    protected int PLAYER_ID;
    protected Hand hand;
    protected Socket connection;
    protected boolean exploded = false;
    public ObjectInputStream inFromClient;
    public ObjectOutputStream outToClient;
    Scanner in = new Scanner(System.in);
    protected View view;

    public void addCard(Card card){
        hand.addCard(card);
    }

    public void removeCard(Card card){
        hand.removeCard(card);
    }

    public Boolean containsCard(Card card){
        return hand.containsCard(card);
    }

    public Hand getHand()
    {
        return hand;
    }

    public void setHand(CardStack hand) {
        this.hand = new Hand(hand);
	}

    public int getPlayerId(){
        return PLAYER_ID;
    }
    
    public class Bot extends Player{}

	
}
