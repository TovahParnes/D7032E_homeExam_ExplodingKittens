package HomeExam.scr.Main;

import java.util.*;

import HomeExam.scr.Main.Cards.Card;
import HomeExam.scr.Main.Players.Player;

public class View {

    public void printServer(String message){
        System.out.println(message);
    }

    public void sendMessage(Player player, Object message) {
        try {player.outToClient.writeObject(message);} 
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
        message += ("\nYour hand: " + playerCards(player.getHand()));
        message += playerOptions(player.getHand());
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
            message += card.getName() + ": " + card.getDescription() + "\n";
        }
        printServer(message);
    }

    public void currentPlayer(int currentPlayer) {
        printServer("Current player: " + currentPlayer);
    }

}
