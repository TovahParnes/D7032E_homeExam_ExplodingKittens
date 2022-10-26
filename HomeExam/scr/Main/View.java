package HomeExam.scr.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import HomeExam.scr.Main.CardStack.Hand;
import HomeExam.scr.Main.Cards.Card;
import HomeExam.scr.Main.Players.Player;

public class View {

    /**
     * Prints a message to the server console
     * 
     * @param message
     */
    public void printServer(String message) {
        System.out.println(message);
    }

    /**
     * Prints a message to the client console
     * 
     * @param player  Player - the player to print to
     * @param message Object - the message to print
     */
    public void sendMessage(Player player, Object message) {
        try {
            player.outToClient.writeObject(message);
        } catch (Exception e) {
            printServer("Sending to client failed: " + e.getMessage());
        }
    }

    /**
     * Reads a message from the client console
     * 
     * @param player Player - the player to print to
     * @return String - the read message
     */
    public String readMessage(Player player) {
        String word = " ";
        try {
            word = (String) player.inFromClient.readObject();
        } catch (Exception e) {
            printServer("Reading from client failed: " + e.getMessage());
        }
        return word;
    }

    /**
     * Reads an interruptable message from the client console
     * 
     * @param player  Player - the player to print to
     * @param seconds int - for how many seconds to read
     * @return String - the read message
     */
    public String readMessageInterruptible(Player player, int seconds)
            throws IOException, InterruptedException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int millisecondsWaited = 0;
            while (!br.ready() && millisecondsWaited < (seconds * 1000)) {
                Thread.sleep(200);
                millisecondsWaited += 200;
            }
            if (br.ready())
                return br.readLine();
        } catch (Exception e) {
            printServer("Interruptible reading from client failed: " + e.getMessage());
        }
        return null;
    }

    /**
     * Prints that a client is connected to the client and srver
     * 
     * @param player Player - the player to print to
     */
    public void printConnection(Player player) {
        sendMessage(player, "You are connected to the server as player " + player.getPLAYER_ID());
        printServer("Connected to Player ID: " + player.getPLAYER_ID());
    }

    /**
     * Prints the deck
     * 
     * @param deck ArrayList<Card> - the deck to print
     */
    public void printDeck(ArrayList<Card> deck) {
        String message = "Deck: \n";
        for (Card card : deck) {
            message += card.getCardInfo() + "\n";
        }
        printServer(message);
    }

    /**
     * Prints the current player to the server
     * 
     * @param currentPlayer player - the current player
     */
    public void printCurrentPlayer(Player currentPlayer) {
        printServer("\nCurrent player: " + currentPlayer.getPLAYER_ID());
    }

    /**
     * Gets the hand as a string
     * 
     * @param player player - the player
     * @return String - the hand as a string
     */
    public String stringHand(Player player) {
        String message = "Your hand: \n";
        message += player.getHand().getCardStackString() + "\n";
        return message;
    }

    /**
     * Writes the the new round to all players and server
     * 
     * @param players       ArrayList<Player> - the players
     * @param currentPlayer player - the player
     * @param turnsLeft     int - the number of turns
     */
    public void writeNewRoundsToPlayers(ArrayList<Player> players, Player currentPlayer, int turnsLeft,
            String targets) {
        printCurrentPlayer(currentPlayer);
        for (Player player : players) {
            if (player.getPLAYER_ID() == currentPlayer.getPLAYER_ID()) {
                writeYourTurn(player, turnsLeft, targets);
            } else {
                writeNotYourTurn(player, currentPlayer);
            }
        }
    }

    /**
     * Writed whos turn it is to the player and server
     * 
     * @param player        player - the player
     * @param currentPlayer player - the current player
     */
    private void writeNotYourTurn(Player player, Player currentPlayer) {
        String message = "\nIt is now the turn of player " + currentPlayer.getPLAYER_ID();
        sendMessage(player, message);
    }

    /**
     * Writes your turn, how many turns you have, your hand and your options
     * 
     * @param player    player - the player
     * @param turnsLeft int - the number of turns left
     * @param targets   String - the availible targets
     */
    private void writeYourTurn(Player player, int turnsLeft, String targets) {
        String message = "It is your turn\n\n";
        message += ("You have " + turnsLeft + ((turnsLeft > 1) ? " turns" : " turn") + " to take\n");
        message += stringHand(player);
        message += stringPlayerOptions(player.getHand(), targets);
        sendMessage(player, message);
    }

    /**
     * Writes the players options
     * 
     * @param hand    Hand - the hand
     * @param targets String - the availible targets
     * @return String - the player options
     */
    public String stringPlayerOptions(Hand hand, String targets) {
        ArrayList<String> uniqueCards = new ArrayList<String>();

        String yourOptions = "You have the following options:\n";

        for (Card card : hand.getCardStackAsArray()) {
            if (!uniqueCards.contains(card.getName())) {
                uniqueCards.add(card.getName());
                int count = hand.getCardCount(card);
                if (count >= 1) {
                    if (card.getIsPlayable()) {
                        if (card.getHasTarget()) {
                            yourOptions += ("\t" + card.getName() + " [target] (available targets: " + targets + ") - "
                                    + card.getDescription() + "\n");
                        } else {
                            yourOptions += ("\t" + card.getName() + " -  " + card.getDescription() + "\n");
                        }
                    }
                }
                if (count >= 2)
                    yourOptions += "\t2 " + card.getName() + " [target] (available targets: " + targets
                            + ") - Steal random card\n";
                if (count >= 3)
                    yourOptions += "\t3 " + card.getName() + " [target] [Card Type] (available targets: " + targets
                            + ") - Name and pick a card\n";
            }
        }
        yourOptions += "\tPass\n";

        return yourOptions;
    }

    /**
     * Writes the players id to each player
     * 
     * @param players ArrayList<Player> - the players
     */
    public void writePlayersID(ArrayList<Player> players) {
        for (Player player : players) {
            sendMessage(player, "You are player ID: " + player.getPLAYER_ID() + "\n");
        }
    }

    /**
     * Writes invalid input, please try again to the player
     * 
     * @param player Player - the player
     */
    public void invalidInput(Player player) {
        sendMessage(player, "Invalid input, please try again");
    }

    /**
     * Writes the drawn card to the player and server
     * 
     * @param player Player - the player
     * @param card   Card - the card
     */
    public void writeDrawCard(Player player, Card card) {
        String messagePlayer = "You drew: " + card.getName();
        String messageServer = "Player " + player.getPLAYER_ID() + " drew: " + card.getName();
        sendMessage(player, messagePlayer);
        printServer(messageServer);
    }

    /**
     * Writes that a player exploded to the players and server
     * 
     * @param players        ArrayList<Player> - the players
     * @param explodedPlayer Player - the exploded player
     */
    public void explodePlayer(ArrayList<Player> players, Player explodedPlayer) {
        for (Player player : players) {
            if (player.getPLAYER_ID() == explodedPlayer.getPLAYER_ID()) {
                sendMessage(player, "You exploded");
            } else {
                sendMessage(player, "Player " + explodedPlayer.getPLAYER_ID() + " exploded");
            }
            printServer("Player " + explodedPlayer.getPLAYER_ID() + " exploded");
        }
    }

    /**
     * Writed to the player that they defused a kitten and needs to place it in the
     * deck
     * 
     * @param player     Player - the player
     * @param deckLength int - the length of the deck
     */
    public void defusedExplodingKittenPlacementNeeded(Player player, int deckLength) {
        sendMessage(player,
                "You defused the exploding kitten. Where do you want to place it? [0 ... " + deckLength + "]");
        printServer("Player " + player.getPLAYER_ID() + " defused the exploding kitten");
    }

    /**
     * Writes that the player defused the exploding kitten and placed in in the deck
     * at cardPlacement
     * 
     * @param player        Player - the player
     * @param cardPlacement int - the placement of the card in the deck
     */
    public void defusedExplodingKittenPlacementSuccessful(Player player, int cardPlacement) {
        sendMessage(player,
                "You successfully placed the exploding kitten at position " + cardPlacement + " in the deck");
    }

    /**
     * Writes that currentPlayer exploded a kitten to players and server
     * 
     * @param players       ArrayList<Player> - the players
     * @param currentPlayer Player - the player
     * @param cardPlacement int - the placement of the card in the deck
     */
    public void writeDefuseExplodingKitten(ArrayList<Player> players, Player currentPlayer, int cardPlacement) {
        for (Player player : players) {
            if (player.getPLAYER_ID() == currentPlayer.getPLAYER_ID()) {
                defusedExplodingKittenPlacementSuccessful(player, cardPlacement);
            } else {
                sendMessage(player, "Player " + currentPlayer.getPLAYER_ID() + " Defused an ExplodingKitten");
            }
        }
        printServer("Player " + currentPlayer.getPLAYER_ID() + " successfully placed the exploding kitten at position "
                + cardPlacement + " in the deck");
    }

    /**
     * Write that currentPlayer won the game to all players and server
     * 
     * @param players       ArrayList<Player> - the players
     * @param currentPlayer Player - the player
     */
    public void writeWinner(ArrayList<Player> players, Player currentPlayer) {
        String message = "Player " + currentPlayer.getPLAYER_ID() + " won the game!\nThank you for playing.";
        for (Player player : players) {
            if (player.getPLAYER_ID() == currentPlayer.getPLAYER_ID()) {
                sendMessage(player, "Congratulations! You won the game!\nThank you for playing.");
            } else {
                sendMessage(player, message);
            }
        }
        printServer(message);
    }

    /**
     * Writes that player stole card from targetPlayer
     * 
     * @param player Player - the player
     * @param card   Card - the card
     * @param target Player - the target player
     */
    public void writeStealCard(Player player, Card card, Player target) {
        sendMessage(player, "You stole: " + card.getName());
        sendMessage(target, "Player " + player.getPLAYER_ID() + " stole " + card.getName() + " from you");
        printServer("Player " + player.getPLAYER_ID() + " stole: " + card.getName() + " from player "
                + target.getPLAYER_ID());
    }

    /**
     * Writes that player picked card from targetPlayer and got pickedCard
     *
     * @param players        ArrayList<Player> - the players
     * @param currentPlayer  Player - the player that players
     * @param targetID       int - the id of the target player
     * @param pickedCardName String - the name of the picked card
     */
    public void writePickCard(ArrayList<Player> players, Player currentPlayer, int targetID, String pickedCardName,
            String gotCardName) {
        String message = " picked " + pickedCardName + " from player " + targetID + " and got " + gotCardName;
        for (Player player : players) {
            if (player.getPLAYER_ID() == currentPlayer.getPLAYER_ID()) {
                sendMessage(player, "You" + message);
            } else {
                sendMessage(player, "Player " + currentPlayer.getPLAYER_ID() + message);
            }
        }
        printServer("Player " + currentPlayer.getPLAYER_ID() + message);
    }

    /**
     * Writes that player passed to players and server
     * 
     * @param players       ArrayList<Player> - the players
     * @param currentPlayer Player - the player that passed
     */
    public void writePlayerPassed(ArrayList<Player> players, Player currentPlayer) {
        String message = "Player " + currentPlayer.getPLAYER_ID() + " Passed";
        for (Player player : players) {
            if (player.getPLAYER_ID() != currentPlayer.getPLAYER_ID()) {
                sendMessage(player, message);
            }
        }
        printServer(message);
    }

    /**
     * Writes that player needs to give a card to targetPlayer
     * 
     * @param player       Player - the player that needs to give a card
     * @param targetPlayer int - the player will recieve the card
     */
    public void writeGiveCardToPlayer(Player player, int targetPlayer) {
        String message = stringHand(player);
        message += "Give a card to Player " + targetPlayer + " by writing the name of the card\n";
        sendMessage(player, message);
    }

    /**
     * Writes to players and server that targetPlayer gave a card to currentPlayer
     * 
     * @param allPlayers    ArrayList<Player> - the players
     * @param currentPlayer Player - the player that recieves the card
     * @param targetPlayer  int - the player that gave the card
     * @param cardName      String - the name of the card
     */
    public void writeGiveCard(ArrayList<Player> allPlayers, int currentPlayer, int targetPlayer, String cardName) {
        for (Player player : allPlayers) {
            if (player.getPLAYER_ID() == targetPlayer) {
                sendMessage(player, "You gave " + cardName + " to Player " + currentPlayer);
            } else if (player.getPLAYER_ID() == currentPlayer) {
                sendMessage(player, "You drew " + cardName + " from player " + currentPlayer);
            } else {
                sendMessage(player, "Player " + targetPlayer + " gave a card to Player " + currentPlayer);
            }
        }
        printServer("Player " + targetPlayer + " gave " + cardName + " to Player " + currentPlayer);
    }

    /**
     * Writes that player played numCards cardName
     * 
     * @param allPlayers    ArrayList<Player> - the players
     * @param currentPlayer int - the player that player
     * @param cardName      String - the name of the card
     * @param numCards      int - the number of cards
     */
    public void writePlayCard(ArrayList<Player> allPlayers, int currentPlayer, String cardName, int numCards) {
        String message = "Player " + currentPlayer + " played " + numCards + " " + cardName
                + (numCards > 1 ? " cards" : " card");
        for (Player player : allPlayers) {
            if (player.getPLAYER_ID() == currentPlayer) {
                sendMessage(player, "You played: " + cardName);
            } else {
                sendMessage(player, message);
            }
        }
        printServer(message);
    }

    /**
     * Writes that player can play nope and read their input
     * 
     * @param player             Player - the player to read from
     * @param secondsToInterrupt int - the number of seconds to interrupt
     * @return String - the input
     */
    public String writeNope(Player player, int secondsToInterrupt) {
        sendMessage(player, "Press <Enter> to play Nope");
        String message = null;
        try {
            message = readMessageInterruptible(player, secondsToInterrupt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

}
