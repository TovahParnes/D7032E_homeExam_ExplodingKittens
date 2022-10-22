package HomeExam.scr.Main;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import HomeExam.scr.Main.CardStack.Deck;
import HomeExam.scr.Main.Players.OnlinePlayer;
import HomeExam.scr.Main.Players.Player;

public class Server {

    private ArrayList<Player> players = new ArrayList<Player>();
    private View view;
    private Options options;
    private Random random;
    private Deck deck;

    private static int numTurns;

    public Server(Options options, View view, boolean testBool) throws Exception
    {
        this.view = view;
        this.options = options;

        if (options.getNUM_PLAYERS() < options.getMAX_NUM_PLAYERS() || options.getNUM_PLAYERS() > options.getMAX_NUM_PLAYERS()) {
            throw new Exception("Not a valid amount of players");       //XX: Add so that handles error and hangles total num players+bots
        }

        deck = new Deck(options);
        deck.shuffle();
        addOnlinePlayers(options.getNUM_ONLINE_PLAYERS(), view);
        System.out.println("Deck length: " + deck.getCardStackLength());

        for (Player player : players) {
            player.setHand(deck.generateHand(7));
            view.sendMessage(player, view.printHand(player));
            
        }

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

