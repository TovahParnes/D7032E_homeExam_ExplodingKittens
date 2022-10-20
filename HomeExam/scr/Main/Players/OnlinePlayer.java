package HomeExam.scr.Main.Players;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import HomeExam.scr.Main.View;
import HomeExam.scr.Main.Cards.Card;

public class OnlinePlayer extends Player{

    public OnlinePlayer(int PLAYER_ID, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient, View view)
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

