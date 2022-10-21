package HomeExam.scr.Main;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import HomeExam.scr.Main.Players.OnlinePlayer;
import HomeExam.scr.Main.Players.Player;

public class Server {

    private static ArrayList<PlayedCard> playedCards = new ArrayList<PlayedCard>();
    private ArrayList<Player> players = new ArrayList<Player>();
    private View view;
    private Random random;
    private Deck deck;

    private static final int MIN_NUM_PLAYERS = 2;
    private static final int MAX_NUM_PLAYERS = 5;
    private static int numPlayers;

    public Server(int numOnlinePlayers, int numBots, View view, boolean testBool) throws Exception
    {
        this.view = view;
        //deckFactory.deckShuffler();
        Server.numPlayers = numOnlinePlayers + numBots;

        if (numPlayers < MIN_NUM_PLAYERS || numPlayers > MAX_NUM_PLAYERS) {
            throw new Exception("Not a valid amount of players");       //XX: Add so that handles error and hangles total num players+bots
        }

        addOnlinePlayers(numOnlinePlayers, view);

        deck = new Deck("originalGameCards");
        deck.shuffle();

        int currentPlayer = setCurrentPlayer();
        view.printCurrentPlayer(currentPlayer);
        
        boolean gameOver = false;
        if(!testBool)
        {
            startGameLoop(gameOver, currentPlayer, numPlayers);
        }
    }

    /**
     * Starts the game by running the game loop
     * @param finished boolean that checks if the game is still running or not
     * @param currentPlayer playerId of the current player
     * @param numPlayers the number of online players
     * @throws Exception
     */
    private void startGameLoop(boolean finished, int currentPlayer, int numPlayers) throws Exception{
        view.printServer("Started game loop");
        while(!finished) {
            
        }
    }


    public void addOnlinePlayers(int numPlayers, View view) throws Exception
    {
        ServerSocket aSocket = new ServerSocket(2048);
        for(int onlineClient=0; onlineClient<numPlayers; onlineClient++)
        {
            Socket connectionSocket = aSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            players.add(new OnlinePlayer(onlineClient, connectionSocket, inFromClient, outToClient, view));
            view.printConnection(onlineClient);
        }
    }

    public int setCurrentPlayer()
    {
        return ThreadLocalRandom.current().nextInt(players.size());
    }


}

