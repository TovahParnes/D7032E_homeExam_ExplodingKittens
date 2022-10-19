package HomeExam.scr.Main;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import HomeExam.scr.Main.Cards.Card;

public class DeckFactory {
    private static ArrayList<Card> cardDeck;

    /**
     * Creates and shuffles the deck for the game, without 
     * @throws Exception
     */
    public DeckFactory() throws Exception
    {   
        ArrayList<String> numCardsOfEachType = readNumCards();

    }

    private  ArrayList<String> readNumCards() throws Exception
    {   
        ArrayList<String> numCardsOfEachType = new ArrayList<String>(Files.readAllLines(Paths.get("","NumCardsBaseGame.txt"), StandardCharsets.ISO_8859_1));
        return numCardsOfEachType;
    }

    private void AddCards(ArrayList<String> numCardsOfEachType){
        for 
    }

    public void deckShuffler()
    {
        Collections.shuffle(cardDeck);
    }


}


