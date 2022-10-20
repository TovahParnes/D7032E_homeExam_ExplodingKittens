package HomeExam.scr.Main;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import HomeExam.scr.Main.Cards.Card;

public class Deck {
    private static ArrayList<Card> cardDeck;

    /**
     * Creates and shuffles the deck for the game, without 
     * @throws Exception
     */
    public Deck() throws Exception
    {   
        ArrayList<String> numCardsOfEachType = readNumCards();

    }

    private  ArrayList<String> readNumCards() throws Exception
    {   
        ArrayList<String> numCardsOfEachType = new ArrayList<String>();
        //numCardsOfEachType = Files.readAllLines(Paths.get("","NumCardsBaseGame.txt"));
        return numCardsOfEachType;
    }

    private void AddCards(ArrayList<String> numCardsOfEachType){
    }

    public void deckShuffler()
    {
        //Collections.shuffle(cardDeck);
    }


}


