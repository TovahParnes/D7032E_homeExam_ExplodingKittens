package HomeExam.scr.Main;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import HomeExam.scr.Main.Cards.Card;

public class Player {

    protected int PLAYER_ID;
    protected ArrayList<Card> hand;
    protected int turnsLeft;
    protected Socket connection;
    protected boolean exploded = false;
    protected ObjectInputStream inFromClient;
    protected ObjectOutputStream outToClient;
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
    public class OnlinePlayer extends Player{

        public void main(int PLAYER_ID, ArrayList<Card> hand, Socket connection, BufferedReader inFromClient, DataOutputStream outToClient, View view)
        {
            this.PLAYER_ID = PLAYER_ID;
            this.connection = connection;
            this.inFromClient = inFromClient;
            this.outToClient = outToClient;
            this.view = view;
        }

        public void playCard(Card card){
            //Server.DiscardedCards().add(new PlayedCard(PLAYER_ID, card));
            hand.remove(card);
        }

    }

    public class Bot extends Player{}
        
}
