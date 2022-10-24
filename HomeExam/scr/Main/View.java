package HomeExam.scr.Main;

import java.util.*;

import HomeExam.scr.Main.CardStack.CardStack;
import HomeExam.scr.Main.CardStack.Hand;
import HomeExam.scr.Main.Cards.Card;
import HomeExam.scr.Main.Players.Player;

public class View {

    public void printServer(String message) {
        System.out.println(message);
    }

    public void sendMessage(Player player, Object message) {
        try {
            player.outToClient.writeObject(message);
        } catch (Exception e) {
            printServer("Sending to client failed: " + e.getMessage());
        }
    }

    public String readMessage(Player player) {
        String word = " ";
        try {
            word = (String) player.inFromClient.readObject();
        } catch (Exception e) {
            printServer("Reading from client failed: " + e.getMessage());
        }
        return word;
    }

    public void printErrorStart() {
        printServer("Something went wrong");
    }

    public void printConnection(int onlineClient) {
        printServer("Connected to Player ID: " + (onlineClient));
    }

    public String playerCards(ArrayList<Card> hand) {
        return "hand test";
    }

    public String drawCard(Card card) {
        return "You drew: " + card.getName();
    }

    public void printDeck(ArrayList<Card> deck) {
        String message = "Deck: \n";
        for (Card card : deck) {
            message += card.getCardInfo() + "\n";
        }
        printServer(message);
    }

    public void printCurrentPlayer(Player currentPlayer) {
        printServer("Current player: " + currentPlayer.getPLAYER_ID());
    }

    public String stringHand(Player player) {
        String message = "Your hand: \n";
        message += player.getHand().getCardStackString() + "\n";
        return message;
    }

    public void writeNewRoundsToPlayers(ArrayList<Player> players, Player currentPlayer, int turnsLeft) {
        printCurrentPlayer(currentPlayer);
        for (Player player : players) {
            if (player.getPLAYER_ID() == currentPlayer.getPLAYER_ID()) {
                writeYourTurn(player, turnsLeft);
            } else {
                writeNotYourTurn(player, currentPlayer);
            }
        }
    }

    private void writeNotYourTurn(Player player, Player currentPlayer) {
        String message = "It is now the turn of player " + currentPlayer.getPLAYER_ID() + "\n";
        sendMessage(player, message);
    }

    private void writeYourTurn(Player player, int turnsLeft) {
        String message = "It is your turn\n\n";
        message += ("You have " + turnsLeft + ((turnsLeft > 1) ? " turns" : " turn") + " to take\n");
        message += stringHand(player);
        message += stringPlayerOptions(player.getHand());
        sendMessage(player, message);
    }

    public String stringPlayerOptions(Hand hand) {
        ArrayList<String> uniqueCards = new ArrayList<String>();

        String yourOptions = "You have the following options:\n";

        for (Card card : hand.getCardStackAsArray()) {
            if (!uniqueCards.contains(card.getName())) {
                uniqueCards.add(card.getName());
                int count = hand.getCardCount(card);
                if (count >= 1) {
                    if (card.getIsPlayable()) {
                        yourOptions += ("\t" + card.getName() + ": " + card.getDescription() + "\n");
                    }
                }
                if (count >= 2)
                    yourOptions += "\t2 " + card.getName() + " [target] (available targets: "
                            + /* otherPlayerIDs + */ ") (Steal random card)\n";
                if (count >= 3)
                    yourOptions += "\t3 " + card.getName() + " [target] [Card Type] (available targets: "
                            + /* otherPlayerIDs + */ ") (Name and pick a card)\n";
            }
        }
        yourOptions += "\tPass\n";

        return yourOptions;
    }

    public void writePlayersID(ArrayList<Player> players) {
        for (Player player : players) {
            sendMessage(player, "You are player ID: " + player.getPLAYER_ID() + "\n");
        }
    }

    public void failedInput(Player player) {
        sendMessage(player, "Invalid input, please try again");
    }

    public void writeDrawCard(Player player, Card card) {
        String messagePlayer = "You drew: " + card.getName();
        String messageServer = "Player " + player.getPLAYER_ID() + " drew: " + card.getName();
        sendMessage(player, messagePlayer);
        printServer(messageServer);
    }

    public void explodePlayer(ArrayList<Player> players, Player currentPlayer) {
        for (Player player : players) {
            if (player.getPLAYER_ID() == currentPlayer.getPLAYER_ID()) {
                sendMessage(player, "You exploded");
            } else {
                sendMessage(player, "Player " + currentPlayer.getPLAYER_ID() + " exploded");
            }
            printServer("Player " + currentPlayer.getPLAYER_ID() + " exploded");
        }
    }

    public void youDefusedExplodingKitten(Player player, int deckLegth) {
        sendMessage(player,
                "You defused the exploding kitten. Where do you want to place it? [0 ... " + deckLegth + "]");
    }

    public void writeDefuseExpodingKitten(ArrayList<Player> players, Player currentPlayer) {
        for (Player player : players) {
            if (player.getPLAYER_ID() == currentPlayer.getPLAYER_ID()) {
                sendMessage(player, "You defused an exploding kitten!");
            } else {
                sendMessage(player, "Player " + currentPlayer.getPLAYER_ID() + " defused an exploding kitten");
            }
            printServer("Player " + currentPlayer.getPLAYER_ID() + " exploded");
        }
    }

    public void defuseExplodingKitten(ArrayList<Player> players, Player currentPlayer) {
        //TODO
    }
}
