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
    private Player currentPlayer;

    public Server(Options options, View view, boolean testBool) throws Exception {
        this.view = view;
        this.options = options;

        if (options.getNUM_PLAYERS() < options.getMIN_NUM_PLAYERS()
                || options.getNUM_PLAYERS() > options.getMAX_NUM_PLAYERS()) {
            throw new Exception("Not a valid amount of players"); // XX: Add so that handles error and handles total num
        }

        deck = new Deck(options);
        cardsInGame = deck.getUniqueCards();
        cardsInGame = new CardStack();
        deck.shuffle();
        addOnlinePlayers(options.getNUM_ONLINE_PLAYERS(), view);
        alivePlayers = new ArrayList<Player>(allPlayers);

        for (Player player : alivePlayers) {
            player.setHand(deck.generateHand(options));
        }

        currentPlayer = getStartingPlayer();
        view.writePlayersID(allPlayers);

        boolean gameOver = false;
        if (!testBool) {
            startGameLoop(gameOver);
        }
    }

    /**
     * Starts the game by running the game loop
     * 
     * @param finished boolean that checks if the game is still running or not
     * @throws Exception
     */
    private void startGameLoop(boolean finished) throws Exception {
        view.printServer("Started game loop");
        numTurns++;
        while (!finished) {
            view.writeNewRoundsToPlayers(alivePlayers, currentPlayer, numTurns);

            String playerInput = view.readMessage(currentPlayer);
            String[] input = playerInput.split(" ", -1);
            view.printServer("Player input: " + playerInput);
            Boolean viableOption = viableOption(input);
            if (!viableOption) {
                view.sendMessage(currentPlayer, "Invalid input, try again");
            }
            view.printServer("Viable option = " + viableOption);
        }
    }

    public boolean viableOption(String[] input) {
        Boolean viableOption = false;

        // Input Pass
        if (input.length == 1 && input[0].equals("Pass")) {
            viableOption = true;

            pass();

        } else if (input.length == 1) // Input Card
        {
            String cardName = input[0];
            Boolean containsCard = currentPlayer.getHand().contains(cardName);
            if (!containsCard) {
                return false;
            }

            Boolean isPlayable;
            CardStack a = currentPlayer.getHand();
            System.out.println(a.getCardStackString());
            Card b = a.getCard(cardName);
            System.out.println(b.getName());
            isPlayable = b.getIsPlayable();
            Boolean hasTarget = currentPlayer.getHand().getCard(cardName).getHasTarget();
            if (!isPlayable || hasTarget) {
                return false;
            }

            viableOption = true;
            currentPlayer.getHand().getCard(cardName).onPlay(this);

        } else if (input.length == 2) // Input <Card> <Target>
        {
            String cardName = input[0];
            Boolean containsCard = currentPlayer.getHand().contains(cardName);
            if (!containsCard) {
                return false;
            }
            Boolean isPlayable = currentPlayer.getHand().getCard(cardName).getIsPlayable();
            Boolean hasTarget = currentPlayer.getHand().getCard(cardName).getHasTarget();
            if (!isPlayable || !hasTarget) {
                return false;
            }
            int targetID = Integer.parseInt(input[2]);
            Boolean viableTarget = viableTarget(targetID);
            if (!viableTarget) {
                return false;
            }

            viableOption = true;
            currentPlayer.getHand().getCard(cardName).onPlay(this, targetID);

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
            Boolean containsCard = currentPlayer.getHand().contains(cardName, numCards);
            if (!containsCard) {
                return false;
            }
            int targetID = Integer.parseInt(input[2]);
            Boolean viableTarget = viableTarget(targetID);
            if (!viableTarget) {
                return false;
            }

            viableOption = true;
            play2Cards(cardName, targetID);

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
            Boolean containsCard = currentPlayer.getHand().contains(cardName, numCards);
            if (!containsCard) {
                return false;
            }
            int targetID = Integer.parseInt(input[2]);
            Boolean viableTarget = viableTarget(targetID);
            if (!viableTarget) {
                return false;
            }
            String targetCard = input[3];
            Boolean viableTargetCard = cardsInGame.contains(targetCard);
            if (!viableTargetCard) {
                return false;
            }

            viableOption = true;
            play3Cards(cardName, targetID, targetCard);
        }
        return viableOption;
    }

    private boolean viableTarget(int targetID) {
        if (targetID >= 0 && targetID < alivePlayers.size() && targetID != currentPlayer.getPLAYER_ID()) {
            return true;
        } else
            return false;
    }

    public void pass() {
        Card card = deck.drawCard();
        view.writeDrawCard(currentPlayer, card);
        card.onDraw(this, currentPlayer);
        currentPlayer.getHand().addCard(card);
        endTurn();
    }

    public void endTurn() {
        numTurns--;
        if (numTurns >= 0) {
            currentPlayer = getNextPlayer();
            numTurns++;
        }
    }

    public void explodePlayer() {

    }

    public void play2Cards(String cardName, int targetID) {
        System.out.println("TEMP: Play 2 cards");

    }

    public void play3Cards(String cardName, int targetID, String targetCard) {
        System.out.println("TEMP: Play 3 cards");
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

    public Player getStartingPlayer() {
        int id = ThreadLocalRandom.current().nextInt(alivePlayers.size());
        return alivePlayers.get(id);
    }

    private Player getNextPlayer() {
        Player player;
        int id = alivePlayers.indexOf(currentPlayer);
        id = (id + 1) % alivePlayers.size();
        return alivePlayers.get(id);
    }

    public void expodePlayer() {
        alivePlayers.remove(currentPlayer);
        view.explodePlayer(allPlayers, currentPlayer);
    }

    public void defuseExplodingKitten() {
        view.youDefusedExplodingKitten(currentPlayer, deck.getCardStackLength());
        String input = view.readMessage(currentPlayer);
        try {

        } catch (Exception e) {
            // TODO: handle exception
        }
        view.defuseExplodingKitten(allPlayers, currentPlayer);
    }
}
