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

    public Server(Options options, View view, boolean testBool) throws Exception {
        this.view = view;
        this.options = options;

        if (options.getNUM_PLAYERS() < options.getMIN_NUM_PLAYERS()
                || options.getNUM_PLAYERS() > options.getMAX_NUM_PLAYERS()) {
            throw new Exception("Not a valid amount of players"); // XX: Add so that handles error and handles total num
        }

        deck = new Deck(options);
        deck.shuffle();
        addOnlinePlayers(options.getNUM_ONLINE_PLAYERS(), view);

        for (Player player : players) {
            player.setHand(deck.generateHand(options));
        }

        int currentPlayer = setCurrentPlayer();
        view.writePlayersID(players);

        boolean gameOver = false;
        if (!testBool) {
            startGameLoop(gameOver, currentPlayer);
        }
    }

    /**
     * Starts the game by running the game loop
     * 
     * @param finished      boolean that checks if the game is still running or not
     * @param currentPlayer playerId of the current player
     * @throws Exception
     */
    private void startGameLoop(boolean finished, int currentPlayer) throws Exception {
        view.printServer("Started game loop");
        while (!finished) {
            numTurns++;

            view.writeNewRoundsToPlayers(players, currentPlayer, numTurns);

            String playerInput = view.readMessage(players.get(currentPlayer));
            String[] input = playerInput.split(" ", -1);
            view.printServer("Player input: " + playerInput);
            view.printServer("Viable option = " + viableOption(players.get(currentPlayer), input));
            while (!viableOption(players.get(currentPlayer), input)) {
                view.failedInput(players.get(currentPlayer));
                // playerInput = view.readMessage(players.get(currentPlayer));
                view.printServer("Player input: " + playerInput);
            }

            currentPlayer = getNextPlayer(currentPlayer);

            numTurns--;
        }
    }

    public boolean viableOption(Player player, String[] input) {
        int currentPlayer = player.getPLAYER_ID();
        Boolean viableOption = false;
        if (input.length == 1 && input[0].equals("Pass")) {
            viableOption = true;
        } else if (input.length == 1) {
            String cardName = input[0];
            Boolean containsCard = player.getHand().contains(cardName);
            Boolean isPlayable = player.getHand().getCard(cardName).getIsPlayable();
            Boolean hasTarget = player.getHand().getCard(cardName).getHasTarget();
            if (containsCard && isPlayable && !hasTarget) {
                viableOption = true;
            }
        } else if (input.length == 2) {
            String cardName = input[0];
            Boolean containsCard = player.getHand().contains(cardName);
            Boolean isPlayable = player.getHand().getCard(cardName).getIsPlayable();
            Boolean hasTarget = player.getHand().getCard(cardName).getHasTarget();
            int targetID = Integer.parseInt(input[2]);
            Boolean viableTarget = viableTarget(targetID, currentPlayer);

            if (containsCard && isPlayable && hasTarget && viableTarget) {
                viableOption = true;
            }

            // card with no arguemnts
        } else if (input.length == 3) {
            int numCards = Integer.parseInt(input[0]);
            String cardName = input[1];
            Boolean containsCard = player.getHand().contains(cardName, numCards);
            int targetID = Integer.parseInt(input[2]);
            Boolean viableTarget = viableTarget(targetID, currentPlayer);

            if (containsCard && viableTarget) {
                viableOption = true;
            }

        } else if (input.length == 4) {
            int numCards = Integer.parseInt(input[0]);
            String cardName = input[1];
            Boolean containsCard = player.getHand().contains(cardName, numCards);
            int targetID = Integer.parseInt(input[2]);
            Boolean viableTarget = viableTarget(targetID, currentPlayer);
            String targetCard = input[3];
            Boolean viableTargetCard = players.get(targetID).getHand().contains(targetCard);

            if (containsCard && viableTarget && viableTargetCard) {
                viableOption = true;
            }
        }
        return viableOption;
    }

    private boolean viableTarget(int targetID, int currentPlayer) {
        if (targetID >= 0 && targetID < players.size() && targetID != currentPlayer) {
            return true;
        } else
            return false;
    }

    public void addOnlinePlayers(int numPlayers, View view) throws Exception {
        ServerSocket aSocket = new ServerSocket(2048);
        for (int onlineClient = 0; onlineClient < numPlayers; onlineClient++) {
            Socket connectionSocket = aSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            players.add(new OnlinePlayer(onlineClient, connectionSocket, inFromClient, outToClient, view));
            view.printConnection(onlineClient);
        }
    }

    public int setCurrentPlayer() {
        return ThreadLocalRandom.current().nextInt(players.size());
    }

    private int getNextPlayer(int currentPlayer) {
        return (currentPlayer + 1) % players.size();
    }

}
