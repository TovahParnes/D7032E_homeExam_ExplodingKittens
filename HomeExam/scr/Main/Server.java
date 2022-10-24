package HomeExam.scr.Main;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import HomeExam.scr.Main.CardStack.Deck;
import HomeExam.scr.Main.CardStack.CardStack;
import HomeExam.scr.Main.Players.OnlinePlayer;
import HomeExam.scr.Main.Players.Player;
import HomeExam.scr.Main.Cards.Card;

public class Server {

    private ArrayList<Player> allPlayers = new ArrayList<Player>();
    private ArrayList<Player> alivePlayers;
    private View view;
    private Options options;
    private Random random;
    private Deck deck;
    private CardStack cardsInGame;

    private static int numTurns;

    public Server(Options options, View view, boolean testBool) throws Exception {
        this.view = view;
        this.options = options;

        if (options.getNUM_PLAYERS() < options.getMIN_NUM_PLAYERS()
                || options.getNUM_PLAYERS() > options.getMAX_NUM_PLAYERS()) {
            throw new Exception("Not a valid amount of players"); // XX: Add so that handles error and handles total num
        }

        deck = new Deck(options);
        cardsInGame = deck.getUniqueCards();
        System.out.println("TEMP: unique cards:" + cardsInGame.getCardStackString());
        cardsInGame = new CardStack();
        deck.shuffle();
        addOnlinePlayers(options.getNUM_ONLINE_PLAYERS(), view);
        alivePlayers = new ArrayList<Player>(allPlayers);

        for (Player player : alivePlayers) {
            player.setHand(deck.generateHand(options));
        }

        int currentPlayer = getStartingPlayer();
        view.writePlayersID(alivePlayers);

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

            view.writeNewRoundsToPlayers(alivePlayers, currentPlayer, numTurns);

            String playerInput = view.readMessage(alivePlayers.get(currentPlayer));
            String[] input = playerInput.split(" ", -1);
            view.printServer("Player input: " + playerInput);
            Boolean viableOption = viableOption(alivePlayers.get(currentPlayer), input);
            view.printServer("Viable option = " + viableOption);

            currentPlayer = getNextPlayer(currentPlayer);

            numTurns--;
        }
    }

    public boolean viableOption(Player player, String[] input) {
        int currentPlayer = player.getPLAYER_ID();
        Boolean viableOption = false;

        // Input Pass
        if (input.length == 1 && input[0].equals("Pass")) {
            viableOption = true;

            pass(player);

        } else if (input.length == 1) // Input Card
        {
            String cardName = input[0];
            Boolean containsCard = player.getHand().contains(cardName);
            if (!containsCard) {
                return false;
            }
            Boolean isPlayable = player.getHand().getCard(cardName).getIsPlayable();
            Boolean hasTarget = player.getHand().getCard(cardName).getHasTarget();
            if (!isPlayable || hasTarget) {
                return false;
            }

            viableOption = true;
            player.getHand().getCard(cardName).onPlay(this);

        } else if (input.length == 2) // Input <Card> <Target>
        {
            String cardName = input[0];
            Boolean containsCard = player.getHand().contains(cardName);
            if (!containsCard) {
                return false;
            }
            Boolean isPlayable = player.getHand().getCard(cardName).getIsPlayable();
            Boolean hasTarget = player.getHand().getCard(cardName).getHasTarget();
            if (!isPlayable || !hasTarget) {
                return false;
            }
            int targetID = Integer.parseInt(input[2]);
            Boolean viableTarget = viableTarget(targetID, currentPlayer);
            if (!viableTarget) {
                return false;
            }

            viableOption = true;
            player.getHand().getCard(cardName).onPlay(this, targetID);

        } else if (input.length == 3) // Input <NumCards> <Card> <Target>
        {
            try {
                int numCards = Integer.parseInt(input[0]);
            } catch (Exception e) {
                return false;
            }
            int numCards = Integer.parseInt(input[0]);
            if (numCards != 2) {
                return false;
            }
            String cardName = input[1];
            Boolean containsCard = player.getHand().contains(cardName, numCards);
            if (!containsCard) {
                return false;
            }
            int targetID = Integer.parseInt(input[2]);
            Boolean viableTarget = viableTarget(targetID, currentPlayer);
            if (!viableTarget) {
                return false;
            }

            viableOption = true;
            play2Cards(player, cardName, targetID);

        } else if (input.length == 4) // Input <NumCards> <Card> <Target> <TargetCards>
        {
            try {
                int numCards = Integer.parseInt(input[0]);
            } catch (Exception e) {
                return false;
            }
            int numCards = Integer.parseInt(input[0]);
            if (numCards != 3) {
                return false;
            }
            String cardName = input[1];
            Boolean containsCard = player.getHand().contains(cardName, numCards);
            if (!containsCard) {
                return false;
            }
            int targetID = Integer.parseInt(input[2]);
            Boolean viableTarget = viableTarget(targetID, currentPlayer);
            if (!viableTarget) {
                return false;
            }
            String targetCard = input[3];
            Boolean viableTargetCard = cardsInGame.contains(targetCard);
            if (!viableTargetCard) {
                return false;
            }

            viableOption = true;
            play3Cards(player, cardName, targetID, targetCard);
        }
        return viableOption;
    }

    private boolean viableTarget(int targetID, int currentPlayer) {
        if (targetID >= 0 && targetID < alivePlayers.size() && targetID != currentPlayer) {
            return true;
        } else
            return false;
    }

    public void pass(Player player) {
        Card card = deck.drawCard();
        view.writeDrawCard(player, card);
        card.onDraw(this, player);
    }

    public void explodePlayer(Player player) {

    }

    public void play2Cards(Player player, String cardName, int targetID) {

    }

    public void play3Cards(Player player, String cardName, int targetID, String targetCard) {

    }

    public void addOnlinePlayers(int numPlayers, View view) throws Exception {
        ServerSocket aSocket = new ServerSocket(2048);
        for (int onlineClient = 0; onlineClient < numPlayers; onlineClient++) {
            Socket connectionSocket = aSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            allPlayers.add(new OnlinePlayer(onlineClient, connectionSocket, inFromClient, outToClient, view));
            view.printConnection(onlineClient);
        }
    }

    public int getStartingPlayer() {
        return ThreadLocalRandom.current().nextInt(alivePlayers.size());
    }

    private int getNextPlayer(int currentPlayer) {
        return (currentPlayer + 1) % alivePlayers.size();
    }

    public void expodePlayer(Player player) {
        alivePlayers.remove(player);

    }

}
