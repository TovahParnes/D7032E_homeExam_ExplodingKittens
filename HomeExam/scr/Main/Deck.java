package HomeExam.scr.Main;

import java.io.FileReader;
import java.lang.reflect.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import HomeExam.scr.Main.Cards.Card;

public class Deck {
    private static ArrayList<Card> cardDeck;

    /**
     * 
     * @throws Exception
     */
    public Deck(String cardsFile) throws Exception
    {   
        cardDeck = new ArrayList<Card>();

        generateDeck(cardsFile);
        

    }

    private void generateDeck(String cardsFile) throws Exception
    {
        ArrayList<ArrayList<String>> readCards = readCardFile(cardsFile);
        addCards(readCards);
    }

    private void addCards(ArrayList<ArrayList<String>> gameCards) throws Exception
    {
        for (ArrayList<String> cardNameAndQuantity : gameCards) {
            String cardName = cardNameAndQuantity.get(0);
            int cardQuantity = Integer.parseInt(cardNameAndQuantity.get(1));
            Class<?> cardClass = Class.forName("HomeExam.scr.Main.Cards." + cardName + "Card");
            Constructor<?> cardConstructor = cardClass.getConstructor();
            for (int i = 0; i < cardQuantity; i++) {
                Card cardInstance = (Card) cardConstructor.newInstance();
                cardDeck.add(cardInstance);
            }
            
        }
    }

    private  ArrayList<ArrayList<String>> readCardFile(String gameCardsFileName) throws Exception
    {
        JSONParser parser = new JSONParser();

        JSONArray a = (JSONArray) parser.parse(new FileReader(gameCardsFileName + ".json"));
        System.out.println(a);

        return a;

    }

    public void deckShuffler()
    {
        //Collections.shuffle(cardDeck);
    }


}


