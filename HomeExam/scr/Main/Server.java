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

        validAmountOfPlayers(options.getNUM_PLAYERS(), options.getMIN_NUM_PLAYERS(), options.getMAX_NUM_PLAYERS());

        deck = new Deck(options);

        cardsInGame = deck.getUniqueCards();
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

            view.printServer("Player input: " + playerInput);
            Boolean viableOption = viableOption(playerInput);
            if (!viableOption) {
                view.sendMessage(currentPlayer, "Invalid input, try again");
            }
            view.printServer("Viable option = " + viableOption);
        }
    }

    public boolean viableOption(String playerInput) {
        String[] input = playerInput.split(" ", -1);
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
            view.writePlayCard(allPlayers, currentPlayer.getPLAYER_ID(), cardName);
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
            try {
                int targetID = Integer.parseInt(input[1]);
            } catch (Exception e) {
                return false;
            }
            int targetID = Integer.parseInt(input[1]);
            Boolean viableTarget = viableTarget(targetID);
            if (!viableTarget) {
                return false;
            }

            viableOption = true;
            view.writePlayCard(allPlayers, currentPlayer.getPLAYER_ID(), cardName);
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
            try {
                int targetID = Integer.parseInt(input[2]);
            } catch (Exception e) {
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
            try {
                int targetID = Integer.parseInt(input[2]);
            } catch (Exception e) {
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
        view.writePlayerPassed(allPlayers, currentPlayer);
        view.writeDrawCard(currentPlayer, card);
        card.onDraw(this, currentPlayer);
        currentPlayer.getHand().addCard(card);
        endTurn();
    }

    public void endTurn() {
        decreaseNumTurns(1);
        if (numTurns <= 0) {
            nextPlayer();
            increaseNumTurns(1);
        }
    }

    public void increaseNumTurns(int turns) {
        numTurns += turns;
    }

    public void decreaseNumTurns(int turns) {
        numTurns -= turns;
    }

    public int getNumTurns() {
        return numTurns;
    }

    public void play2Cards(String cardName, int targetID) {
        currentPlayer.getHand().removeCard(cardsInGame.getCard(cardName), 2);
        Player target = allPlayers.get(targetID);
        Card card = target.getHand().drawRandomCard();
        currentPlayer.getHand().addCard(card);
        view.writeStealCard(currentPlayer, card, targetID);
    }

    public void play3Cards(String cardName, int targetID, String targetCardName) {
        currentPlayer.getHand().removeCard(cardsInGame.getCard(cardName), 3);
        Player target = allPlayers.get(targetID);
        String pickedCardName;
        if (target.getHand().contains(targetCardName)) {
            pickedCardName = targetCardName;
            Card card = target.getHand().drawCard(targetCardName);
            currentPlayer.getHand().addCard(card);
        } else {
            pickedCardName = "nothing";
        }
        view.writePickCard(allPlayers, currentPlayer, targetID, targetCardName, pickedCardName);
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

    public Player getNextPlayer() {
        Player player;
        int id = alivePlayers.indexOf(currentPlayer);
        id = (id + 1) % alivePlayers.size();
        return alivePlayers.get(id);
    }

    public void nextPlayer() {
        currentPlayer = getNextPlayer();
    }

    public void expodePlayer(Player player) {
        alivePlayers.remove(player);
        view.explodePlayer(allPlayers, player);
        checkEndGame();
    }

    public void checkEndGame() {
        if (alivePlayers.size() == 1) {
            endGame();
        }
    }

    public void endGame() {
        view.writeWinner(allPlayers, alivePlayers.get(0));
        System.exit(0);
    }

    public void defuseExplodingKitten() {
        view.defusedExplodingKittenPlacementNeeded(currentPlayer, deck.getSize());
        String playerInput = view.readMessage(currentPlayer);
        Boolean viablePlacement = viableExplodingKittenPlacement(playerInput);
        if (viablePlacement) {
            String[] input = playerInput.split(" ", -1);
            int cardPlacement = Integer.parseInt(input[0]);
            deck.addCardInPlace(cardsInGame.getCard("ExplodingKitten"), cardPlacement);
            view.writeDefuseExplodingKitten(allPlayers, currentPlayer, cardPlacement);
        } else {
            view.invalidInput(currentPlayer);
            defuseExplodingKitten();
        }
    }

    private Boolean viableExplodingKittenPlacement(String playerInput) {
        String[] input = playerInput.split(" ", -1);
        if (input.length != 1) {
            return false;
        }
        try {
            int cardPlacement = Integer.parseInt(input[0]);
        } catch (Exception e) {
            return false;
        }
        int cardPlacement = Integer.parseInt(input[0]);
        if (cardPlacement < 0 || cardPlacement > deck.getSize()) {
            return false;
        }
        return true;
    }

    public void validAmountOfPlayers(int numPlayers, int minPlayers, int maxPlayers) throws Exception {
        if (numPlayers < minPlayers || numPlayers > maxPlayers) {
            throw new Exception("Invalid amount of players");
        }
    }

    public String readInputCardName(Player player) {
        view.writeGiveCardToPlayer(player, currentPlayer.getPLAYER_ID());
        String playerInput = view.readMessage(player);
        String[] input = playerInput.split(" ", -1);
        Boolean validCardName = validateCardName(input, player);

        if (validCardName) {
            return input[0];
        } else {
            view.invalidInput(player);
            return readInputCardName(player);
        }
    }

    public Boolean validateCardName(String[] input, Player player) {
        if (input.length != 1) {
            return false;
        }
        if (player.getHand().contains(input[0])) {
            return true;
        } else {
            return false;
        }
    }

    public Player getCurrentPLayer() {
        return currentPlayer;
    }

    public View getView() {
        return view;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void giveCard(Player targetPlayer, Card card) {
        currentPlayer.getHand().addCard(card);
        view.writeGiveCard(allPlayers, currentPlayer.getPLAYER_ID(), targetPlayer.getPLAYER_ID(), card.getName());
    }

}
