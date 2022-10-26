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
    private Deck deck;
    private CardStack cardsInGame;

    private static int numTurns;
    private Player currentPlayer;

    /**
     * @param options - object containing all the game options
     * @param view    - object handling all messages
     * @throws Exception
     */
    public Server(Options options, View view) throws Exception {
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

        startGameLoop();

    }

    /**
     * Starts the game by running the game loop
     * 
     * @throws Exception
     */
    private void startGameLoop() throws Exception {
        view.printServer("Started game loop");
        numTurns++;
        while (true) {
            view.writeNewRoundsToPlayers(alivePlayers, currentPlayer, numTurns, getTargets());

            String playerInput = view.readMessage(currentPlayer);

            view.printServer("Player input: " + playerInput);
            Boolean viableOption = viableOption(playerInput);
            if (!viableOption) {
                view.sendMessage(currentPlayer, "Invalid input, try again");
            }
            view.printServer("Viable option = " + viableOption);
        }
    }

    /**
     * Checks if the input is a viable option and plays the cards if it is
     * 
     * @param playerInput - String the input from the player
     * @return true if the input is viable, false if not
     */
    public boolean viableOption(String playerInput) {
        String[] input = playerInput.split(" ", -1);
        Boolean viableOption = false;

        // Input Pass
        if (input.length == 1 && input[0].equals("Pass")) {
            viableOption = true;
            pass();
        } else if (input.length == 1) {
            viableOption = validateInputLength1(input);
        } else if (input.length == 2) {
            viableOption = validateInputLength2(input);
        } else if (input.length == 3) {
            viableOption = validateInputLength3(input);
        } else if (input.length == 4) {
            viableOption = validateInputLength4(input);
        }
        return viableOption;
    }

    /***
     * Checks if the input is of the form "<card>", the player has the card, the
     * card is playable, the card does not have a target.
     * Plays the card if it is viable
     * 
     * @param input - String array containing the input
     * @return true if the input is viable, false if not
     */
    private Boolean validateInputLength1(String[] input) {
        Boolean viableOption;
        String cardName = input[0];
        Boolean containsCard = currentPlayer.getHand().contains(cardName);
        if (!containsCard) {
            return false;
        }

        Boolean isPlayable = currentPlayer.getHand().getCard(cardName).getIsPlayable();
        Boolean hasTarget = currentPlayer.getHand().getCard(cardName).getHasTarget();
        if (!isPlayable || hasTarget) {
            return false;
        }

        viableOption = true;
        writePlayCard(cardName, 1);
        currentPlayer.getHand().getCard(cardName).onPlay(this);
        return viableOption;
    }

    /***
     * Checks if the input is of the form "<card> <target>", the player has the
     * card, the
     * card is playable, the card has a target, the target is viable.
     * Plays the card if it is viable
     * 
     * @param input - String array containing the input
     * @return true if the input is viable, false if not
     */
    private Boolean validateInputLength2(String[] input) {
        Boolean viableOption;
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
        writePlayCard(cardName, 1);
        currentPlayer.getHand().getCard(cardName).onPlay(this, allPlayers.get(targetID));
        return viableOption;
    }

    /**
     * Checks if the input is of the form "<numCards> <card> <target>", numCards is
     * 2, the player has 2 of the given cards, the cards are playable, the given
     * target i viable.
     * Plays the cards if it is viable
     * 
     * @param input - String array containing the input
     * @return true if the input is viable, false if not
     */
    private Boolean validateInputLength3(String[] input) {
        Boolean viableOption;
        int validInputNumCards = 2;
        try {
            int numCards = Integer.parseInt(input[0]);
        } catch (Exception e) {
            return false;
        }
        int numCards = Integer.parseInt(input[0]);
        if (numCards != validInputNumCards) {
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
        writePlayCard(cardName, validInputNumCards);
        play2Cards(cardName, targetID);
        return viableOption;
    }

    /**
     * Checks if the input is of the form "<numCards> <card> <target> <targetCard>",
     * numCards is
     * 3, the player has 2 of the given cards, the cards are playable, the given
     * target i viable, if the target card is a card.
     * Plays the cards if it is viable
     * 
     * @param input - String array containing the input
     * @return true if the input is viable, false if not
     */
    private Boolean validateInputLength4(String[] input) {
        Boolean viableOption;
        int validInputNumCards = 3;
        try {
            int numCards = Integer.parseInt(input[0]);
        } catch (Exception e) {
            return false;
        }
        int numCards = Integer.parseInt(input[0]);
        if (numCards != validInputNumCards) {
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
        writePlayCard(cardName, validInputNumCards);
        play3Cards(cardName, targetID, targetCard);
        return viableOption;
    }

    /***
     * Writes the played card to all players
     * Plays nope
     * 
     * @param cardName String - name of the card played
     * @param numCards int - number of cards played
     * @return true if the target is viable, false if not
     */
    private void writePlayCard(String cardName, int numCards) {
        view.writePlayCard(allPlayers, currentPlayer.getPLAYER_ID(), cardName, numCards);
        nope();
    }

    /**
     * Checks if the target is a viable target
     * 
     * @return String of all viable targets
     */
    private String getTargets() {
        String targets = "";
        for (Player player : alivePlayers) {
            if (player.getPLAYER_ID() != currentPlayer.getPLAYER_ID()) {
                targets += player.getPLAYER_ID() + " ";
            }
        }
        return targets;
    }

    /**
     * Checks if the target is a viable target
     * 
     * @param targetID - int the ID of the target
     * @return true if the target is viable, false if not
     */
    private boolean viableTarget(int targetID) {
        if (targetID >= 0 && targetID < alivePlayers.size() && targetID != currentPlayer.getPLAYER_ID()) {
            return true;
        } else
            return false;
    }

    /**
     * Pass the turn, draw a card, end the turn
     */
    private void pass() {
        Card card = deck.drawCard();
        view.writePlayerPassed(allPlayers, currentPlayer);
        view.writeDrawCard(currentPlayer, card);
        card.onDraw(this, currentPlayer);
        currentPlayer.getHand().addCard(card);
        endTurn();
    }

    /**
     * End the turn: decrease the number of turns left, change the current player if
     * applicable
     */
    private void endTurn() {
        decreaseNumTurns(1);
        if (numTurns <= 0) {
            setNextPlayer();
            increaseNumTurns(1);
        }
    }

    /**
     * Play 2 cards to recieve a random card from the target
     * 
     * @param cardName - String the name of the card played
     * @param targetID - int the ID of the target
     */
    private void play2Cards(String cardName, int targetID) {
        currentPlayer.getHand().removeCard(cardsInGame.getCard(cardName), 2);
        Player target = allPlayers.get(targetID);
        Card card = target.getHand().drawRandomCard();
        currentPlayer.getHand().addCard(card);
        view.writeStealCard(currentPlayer, card, allPlayers.get(targetID));
    }

    /**
     * Play 3 cards to ask for a card from the target
     * 
     * @param cardName       - String the name of the card played
     * @param targetID       - int the ID of the target
     * @param targetCardName - String the name of the card the target is asked for
     */
    private void play3Cards(String cardName, int targetID, String targetCardName) {
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

    /**
     * Explode a player
     * 
     * @param player Player - the player that exploded
     */
    public void expodePlayer(Player player) {
        alivePlayers.remove(player);
        view.explodePlayer(allPlayers, player);
        checkEndGame();
        setNumTurns(1);
        setNextPlayer();
    }

    /**
     * Checks if the game is over
     */
    private void checkEndGame() {
        if (alivePlayers.size() == 1) {
            endGame();
        }
    }

    /**
     * End the game
     */
    private void endGame() {
        view.writeWinner(allPlayers, alivePlayers.get(0));
        System.exit(0);
    }

    /**
     * Defuse an exploding kitten
     */
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

    /**
     * Checks if the given exploding kitten placement is within the deck
     * 
     * @param playerInput String - the input of the player
     * @return true if the placement is viable, false if not
     */
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

    /**
     * Checks if the inputted ammount of players are allowed
     * 
     * @param numPlayers - int the number of players
     * @param minPlayers - int the minimum number of players
     * @param maxPlayers - int the maximum number of players
     * @return true if the ammount of players is allowed, false if not
     */
    private void validAmountOfPlayers(int numPlayers, int minPlayers, int maxPlayers) throws Exception {
        if (numPlayers < minPlayers || numPlayers > maxPlayers) {
            throw new Exception("Invalid amount of players");
        }
    }

    /**
     * Checks if the inputted name is allowed, if it is not, it will ask for a new
     * inout until it is accepted
     * 
     * @param player Player - the player that inputs th name
     * @return true if the name is allowed, false if not
     */
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

    /**
     * Checks if the input only contains one string and the inputted card name
     * exists in players hand
     * 
     * @param input  String[] - the inputted card name
     * @param player Player - the player whose hand is checked
     * @return true if the card name is allowed, false if not
     */
    private Boolean validateCardName(String[] input, Player player) {
        if (input.length != 1) {
            return false;
        }
        if (player.getHand().contains(input[0])) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * plays nope for each player
     *
     * @return true if the target is allowed, false if not
     */
    private void nope() {
        System.out.println("TEMP: NOPE HERE");
        for (Player player : alivePlayers) {
            // playerPlayNope(player);
        }
    }

    /**
     * Checks if the player can play nope, read input if the player wants to play
     * nope and plays the nope if the player wants to
     * 
     * @param player Player - the player to play nope
     * @return true if the target is allowed, false if not
     */
    public void playerPlayNope(Player player) {
        if (player.getHand().contains("Nope")) {
            String input = view.writeNope(player, options.getSECONDS_TO_PLAY_NOPE());
            if (input.equals("y")) {
                player.getHand().removeCard(cardsInGame.getCard("Nope"), 1);
            }
        }
    }

    /**
     * Adds the online players to the game
     * 
     * @param numPlayers int - the number of players in the game
     * @param view       View - the view of the game
     * @throws Exception
     */
    public void addOnlinePlayers(int numPlayers, View view) throws Exception {
        ServerSocket aSocket = new ServerSocket(2048);
        for (int onlineClient = 0; onlineClient < numPlayers; onlineClient++) {
            Socket connectionSocket = aSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            Player player = new OnlinePlayer(onlineClient, connectionSocket, inFromClient, outToClient, view);
            allPlayers.add(player);
            view.printConnection(player, onlineClient);
        }
    }

    /**
     * Getters, Setters and other related methods
     */

    /* allPlayers */
    /**
     * @return ArrayList<Player> allPlayers
     */
    public ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }

    /* alivePlayers */
    /**
     * @return ArrayList<Player> alivePlayers
     */
    public ArrayList<Player> getAlivePlayers() {
        return alivePlayers;
    }

    /* view */
    /**
     * @return View view
     */
    public View getView() {
        return view;
    }

    /* options */
    /**
     * @return Options options
     */
    public Options getOptions() {
        return options;
    }

    /* deck */
    /**
     * @return Deck deck
     */
    public Deck getDeck() {
        return deck;
    }

    /* cardsInGame */
    /**
     * @return CardStack cardsInGame
     */
    public CardStack getCardsInGame() {
        return cardsInGame;
    }

    /* numTurns */
    /**
     * @return int numTurns
     */
    public int getNumTurns() {
        return numTurns;
    }

    /**
     * Sets the number of turns
     * 
     * @param numTurns int - the number of turns
     */
    public void setNumTurns(int numTurns) {
        this.numTurns = numTurns;
    }

    /**
     * @param turns int - the number of turns to increase the number of turns with
     */
    public void increaseNumTurns(int turns) {
        numTurns += turns;
    }

    /**
     * @param turns int - the number of turns to decrease the number of turns with
     */
    public void decreaseNumTurns(int turns) {
        numTurns -= turns;
    }

    /* currentPlayer */
    /**
     * @return Player - the currentPlayer
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @param message String - the message to be sent to the current player
     */
    public void sendToCurrentPlayer(String message) {
        view.sendMessage(currentPlayer, message);
    }

    /**
     * 
     * @return Player - the current player
     */
    public Player getStartingPlayer() {
        int id = ThreadLocalRandom.current().nextInt(alivePlayers.size());
        return alivePlayers.get(id);
    }

    /**
     * @return Player - the next player
     */
    public Player getNextPlayer() {
        Player player;
        int id = alivePlayers.indexOf(currentPlayer);
        id = (id + 1) % alivePlayers.size();
        return alivePlayers.get(id);
    }

    /**
     * Sets the currentPlayer to the next player
     */
    public void setNextPlayer() {
        currentPlayer = getNextPlayer();
    }
}
