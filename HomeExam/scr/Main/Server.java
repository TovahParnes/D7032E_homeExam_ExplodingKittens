package HomeExam.scr.Main;

import java.util.*;

public class Server {

    private static ArrayList<PlayedCard> playedCards = new ArrayList<PlayedCard>();
    private ArrayList<Player> players = new ArrayList<Player>();
    private View view;
    private Random random;
    private DeckFactory deckFactory = new DeckFactory();

    private static final int MIN_NUM_PLAYERS = 2;
    private static final int MAX_NUM_PLAYERS = 5;

    private static int numPlayers;

    public Server(int numOnlinePlayers, int numBots, View view, boolean testBool) throws Exception
    {
        this.view = view;
        deckFactory.deckShuffler();
        this.numPlayers = numOnlinePlayers + numBots;

        if (numPlayers < MIN_NUM_PLAYERS || numPlayers > MAX_NUM_PLAYERS) {
            throw new Exception("Not a valid amount of players");       //XX: Add so that handles error and hangles total num players+bots
        }

        addPlayers(numOnlinePlayers, view);
        addBots(numBots, view);
        addServerPlayer(view);
        int currentPlayer = setCurrentPlayer();

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
        while(!finished) {
            
        }
    }

}

