package HomeExam.scr.Main.Players;

import java.io.*;
import java.net.*;
import java.util.*;

import HomeExam.scr.Main.View;
import HomeExam.scr.Main.Cards.Card;

public class Player {

    protected int PLAYER_ID;
    protected ArrayList<Card> hand;
    protected int turnsLeft;
    protected Socket connection;
    protected boolean exploded = false;
    public ObjectInputStream inFromClient;
    public ObjectOutputStream outToClient;
    Scanner in = new Scanner(System.in);
    protected View view;

    public int getTurnsLeft(){
        return turnsLeft;
    }

    public void addCard(Card card){
        hand.add(card);
    }

    public void removeCard(Card card){
        hand.remove(card);
    }

    public Boolean containsCard(Card card){
        return hand.contains(card);
    }

    public ArrayList<Card> getHand()
    {
        return hand;
    }
    
    public class Bot extends Player{}
        
}
