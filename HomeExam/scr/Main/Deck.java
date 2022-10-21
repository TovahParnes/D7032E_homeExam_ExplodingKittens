package HomeExam.scr.Main;

import java.io.FileReader;
import java.lang.reflect.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
        readCardJSON(cardsFile);
    }

    private void readCardJSON(String gameCardsFileName) throws Exception
    {
        JSONParser parser = new JSONParser();

        JSONObject a = (JSONObject) parser.parse(new FileReader("HomeExam/scr/Main/GameVariables/" + gameCardsFileName + ".json"));
        JSONArray cardsArrayJson = (JSONArray) a.get("cards");
        for (Object cardJson : cardsArrayJson) {
            JSONObject cardObject = (JSONObject) cardJson;
            String cardType = (String) cardObject.get("type");
            Long cardQuantity = (Long) cardObject.get("quantity");
            addCards(cardType, cardQuantity.intValue());
        }
    }

    private void addCards(String type, int quantity) throws Exception
    {   
        boolean cardFound = false;
        try {
            Class<?> cardClass = Class.forName("HomeExam.scr.Main.Cards." + type + "Card");
            Constructor<?> cardConstructor = cardClass.getConstructor();
            for (int i = 0; i < quantity; i++) {
                Card cardInstance = (Card) cardConstructor.newInstance();
                cardDeck.add(cardInstance);
            }
            cardFound = true;
        } catch (Exception e) {
            //throw new Exception("Card type not found");
        }
        if (!cardFound) {
            System.out.println("Card type " + type + " not found");
        }
    }

    public void shuffleDeck()
    {
        Collections.shuffle(cardDeck);
    }

    public ArrayList<Card> getDeck()
    {
        return cardDeck;
    }


}


