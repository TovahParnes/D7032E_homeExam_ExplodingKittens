package HomeExam.scr.Main;

import java.util.*;

import HomeExam.scr.Main.CardStack.CardStack;
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

    public String playerTurn(Player player){
        String message = ("\nYou have " + player.getTurnsLeft() + ((player.getTurnsLeft()>1)?" turns":" turn") + " to take");
        message += printHand(player);
        message += playerOptions(player.getHand().getCardStackAsArray());
        return message;
    }

    public String playerCards(ArrayList<Card> hand){
        return "hand test";
    }

    public String playerOptions(ArrayList<Card> hand){

        String yourOptions = "You have the following options:\n";
        Set<Card> handSet = new HashSet<Card>(hand);
        for(Card card : handSet) {
            if (card.getIsPlayable()){
                yourOptions += ("\t" + card.getName() + ": " + card.getDescription() + "\n");
            }
            int count = Collections.frequency(hand, card);
            if(count>=2)
                yourOptions += "\tTwo " + card.getName() + " [target] (available targets: " + /*otherPlayerIDs +*/ ") (Steal random card)\n";
            if(count>=3)
                yourOptions += "\tThree " + card.getName() + " [target] [Card Type] (available targets: " + /*otherPlayerIDs +*/ ") (Name and pick a card)\n";
        }  
        yourOptions += "\tPass\n";

        return yourOptions;
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

    public String printHand(Player player) {
        String message = "Your hand: \n";
        CardStack hand = player.getHand();
        for (Card card : hand.getCardStackAsArray()) {
            message += card.getCardInfo() + "\n";
        }
        return message;
    }

}
