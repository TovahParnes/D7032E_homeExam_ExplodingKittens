package HomeExam.scr.Main.CardStack;

import java.io.FileReader;
import java.lang.reflect.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import HomeExam.scr.Main.Cards.Card;
public class Deck extends CardStack {
    private static ArrayList<Card> cardStack;

    /**
     * 
     * @throws Exception
     */
    public Deck(String cardsFile) throws Exception
    {   
        cardStack = new ArrayList<Card>();
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
        try {
            Class<?> cardClass = Class.forName("HomeExam.scr.Main.Cards." + type + "Card");
            Constructor<?> cardConstructor = cardClass.getConstructor();
            for (int i = 0; i < quantity; i++) {
                Card cardInstance = (Card) cardConstructor.newInstance();
                addCard(cardInstance);
            }
        } catch (Exception e) {
            throw new Exception("Card type not found");
        }
    }

    public CardStack generateHand(int handSize)
    {
        CardStack hand = new CardStack();
        System.out.println("Deck length: " + getCardStackLength());

        for (int i = 0; i < handSize; i++) {
            Card card = getCard();
            if (card.getIsDealable()) {
                hand.addCard(card);
            } else {
                addCardInPlace( card, getCardStackLength() - 1);
                i--;
            }
        }
        shuffle();
        return hand;
    }
}


