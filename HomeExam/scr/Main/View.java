package HomeExam.scr.Main;

import java.util.*;

import HomeExam.scr.Main.CardStack.CardStack;
import HomeExam.scr.Main.CardStack.Hand;
import HomeExam.scr.Main.Cards.Card;
import HomeExam.scr.Main.Players.Player;

public class View {

    public void printServer(String message){
        System.out.println(message);
    }

    public void sendMessage(Player player, Object message) {
        try {
            player.outToClient.writeObject(message);
            printServer("Sent message to player " + player.getPlayerId() + ": \n" + message);
        } 
        catch (Exception e) 
        {
            printServer("Sending to client failed: " + e.getMessage());
        }
    }

    public String readMessage(Player player) {
        String word = " "; 
        try{
            word = (String) player.inFromClient.readObject();
        } catch (Exception e){
            printServer("Reading from client failed: " + e.getMessage());
        }
        return word;
    }	

    public void printErrorStart(){
        printServer("Something went wrong");
    }

    public void printConnection(int onlineClient){
        printServer("Connected to Player ID: " + (onlineClient));
    }

    public String playerCards(ArrayList<Card> hand){
        return "hand test";
    }

    public String drawCard(Card card){
        return "You drew: " + card.getName();
    }

    public void printDeck(ArrayList<Card> deck) {
        String message = "Deck: \n";
        for (Card card : deck) {
            message += card.getCardInfo() + "\n";
        }
        printServer(message);
    }

    public void printCurrentPlayer(int currentPlayer) {
        printServer("Current player: " + currentPlayer);
    }

    public String stringHand(Player player) {
        String message = "Your hand: \n";
        message += player.getHand().getCardsString();
        return message;
    }

    public void writeNewRoundsToPlayers(ArrayList<Player> players, int currentPlayer, int turnsLeft) {
        printCurrentPlayer(currentPlayer);
        for (Player player : players) {
            if (player.getPlayerId() == currentPlayer) {
                writeYourTurn(player, turnsLeft);
            } else {
                writeNotYourTurn(player, currentPlayer);
            }
        }
    }

    private void writeNotYourTurn(Player player, int currentPlayer) {
        String message = "It is now the turn of player " + currentPlayer;
        sendMessage(player, message);
    }

    private void writeYourTurn(Player player, int turnsLeft) {
        String message = "It is your turn\n\n";
        message += ("\nYou have " + turnsLeft + ((turnsLeft>1)?" turns":" turn") + " to take");
        message += stringHand(player);
        message += stringPlayerOptions(player.getHand());
        sendMessage(player, message);
    }

    public String stringPlayerOptions(Hand hand){

        String yourOptions = "You have the following options:\n";

        Set<Card> handSet = hand.getHandSet();
        for(Card card : handSet) {
            if (card.getIsPlayable()){
                yourOptions += ("\t" + card.getName() + ": " + card.getDescription() + "\n");
            }
            int count = Collections.frequency(hand.getCardStackAsArray(), card);
            if(count>=2)
                yourOptions += "\tTwo " + card.getName() + " [target] (available targets: " + /*otherPlayerIDs +*/ ") (Steal random card)\n";
            if(count>=3)
                yourOptions += "\tThree " + card.getName() + " [target] [Card Type] (available targets: " + /*otherPlayerIDs +*/ ") (Name and pick a card)\n";
        }  
        yourOptions += "\tPass\n";

        return yourOptions;
    }

}
