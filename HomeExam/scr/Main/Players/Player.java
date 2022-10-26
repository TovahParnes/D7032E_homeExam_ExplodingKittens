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
    protected ObjectInputStream inFromClient;
    protected ObjectOutputStream outToClient;

    public Hand getHand() {
        return hand;
    }

    public void setHand(CardStack hand) {
        this.hand = new Hand(hand);
    }

    public int getPLAYER_ID() {
        return PLAYER_ID;
    }

    public ObjectInputStream getInFromClient() {
        return inFromClient;
    }

    public ObjectOutputStream getOutToClient() {
        return outToClient;
    }

    /**
     * Set eploded to true
     */
    public void explode() {
        exploded = true;
    }

}
