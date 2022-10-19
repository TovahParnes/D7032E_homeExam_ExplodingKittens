package HomeExam.scr.Main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;

import HomeExam.scr.Main.Cards.Card;

public class Player {

    protected int PLAYER_ID;
    protected ArrayList<Card> hand;
    protected Socket connection;
    protected BufferedReader inFromClient;
    protected DataOutputStream outToClient;
    protected View view;

    public void addCard(Card card){
        hand.add(card);
    }

    public void removeCard(Card card){
        hand.remove(card);
    }

    public Boolean containsCard(Card card){
        return hand.contains(card);
    }

    public class OnlinePlayer extends Player{

        public void initOnlinePlayer(int PLAYER_ID, ArrayList<Card> hand, Socket connection, BufferedReader inFromClient, DataOutputStream outToClient, View view)
        {
            this.PLAYER_ID = PLAYER_ID;
            this.connection = connection;
            this.inFromClient = inFromClient;
            this.outToClient = outToClient;
            this.view = view;
        }

        public void playCard(Card card){
            Server.DiscardedCards().add(new PlayedCard(PLAYER_ID, card));
            hand.remove(card);
        }

    }

    public class Bot extends Player{}
        
}
