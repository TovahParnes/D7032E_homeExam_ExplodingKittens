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
        printServer("\nCurrent player: " + currentPlayer.getPLAYER_ID());
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
        String message = "\nIt is now the turn of player " + currentPlayer.getPLAYER_ID();
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

    public void invalidInput(Player player) {
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

    public void defusedExplodingKittenPlacementNeeded(Player player, int deckLegth) {
        sendMessage(player,
                "You defused the exploding kitten. Where do you want to place it? [0 ... " + deckLegth + "]");
        printServer("Player " + player.getPLAYER_ID() + " defused the exploding kitten");
    }

    public void defusedExplodingKittenPlacementSuccessful(Player player, int cardPlacement) {
        sendMessage(player,
                "You successfully placed the exploding kitten at position " + cardPlacement + " in the deck");
    }

    public void writeDefuseExplodingKitten(ArrayList<Player> players, Player currentPlayer, int cardPlacement) {
        for (Player player : players) {
            if (player.getPLAYER_ID() == currentPlayer.getPLAYER_ID()) {
                defusedExplodingKittenPlacementSuccessful(player, cardPlacement);
            } else {
                sendMessage(player, "Player " + currentPlayer.getPLAYER_ID() + " defused an exploding kitten");
            }
        }
        printServer("Player " + currentPlayer.getPLAYER_ID() + " successfully placed the exploding kitten at position "
                + cardPlacement + " in the deck");
    }

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

    public void writeStealCard(Player player, Card card, int targetID) {
        String message = "You stole: " + card.getName();
        sendMessage(player, message);
        printServer("Player " + player.getPLAYER_ID() + " stole: " + card.getName() + " from player " + targetID);
    }

    public void writePickCard(ArrayList<Player> players, Player currentPlayer, int targetID, String pickCardName,
            String gotCardName) {
        String message = " picked " + pickCardName + " from player " + targetID + " and got " + gotCardName;
        for (Player player : players) {
            if (player.getPLAYER_ID() == currentPlayer.getPLAYER_ID()) {
                sendMessage(player, "You" + message);
            } else {
                sendMessage(player, "Player " + currentPlayer.getPLAYER_ID() + message);
            }
        }
        printServer("Player " + currentPlayer.getPLAYER_ID() + message);
    }

    public void writePlayerPassed(ArrayList<Player> players, Player currentPlayer) {
        String message = "Player " + currentPlayer.getPLAYER_ID() + " Passed.";
        for (Player player : players) {
            if (player.getPLAYER_ID() != currentPlayer.getPLAYER_ID()) {
                sendMessage(player, message);
            }
        }
        printServer(message);
    }

    public void writeGiveCardToPlayer(Player player, int currentPlayer) {
        String message = "Your hand: ";
        message += stringHand(player);
        message += "Give a card to Player " + currentPlayer;
        sendMessage(player, message);
    }

    public void writeGiveCard(ArrayList<Player> allPlayers, int currentPlayer, int targetPlayer, String cardName) {
        for (Player player : allPlayers) {
            if (player.getPLAYER_ID() == targetPlayer) {
                sendMessage(player, "You gave " + cardName + " to Player " + currentPlayer);
            } else if (player.getPLAYER_ID() == targetPlayer) {
                sendMessage(player, "You drew " + cardName + " from player " + currentPlayer);
            } else {
                sendMessage(player, "Player " + targetPlayer + " gave a card to Player " + currentPlayer);
            }
        }
        printServer("Player " + targetPlayer + " gave " + cardName + " to Player " + currentPlayer);
    }

    public void writePlayCard(ArrayList<Player> allPlayers, int currentPlayer, String cardName) {
        String message = "Player " + currentPlayer + " played: " + cardName;
        for (Player player : allPlayers) {
            if (player.getPLAYER_ID() == currentPlayer) {
                sendMessage(player, "You played: " + cardName);
            } else {
                sendMessage(player, message);
            }
        }
        printServer(message);
    }

}
