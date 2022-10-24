package HomeExam.scr.Main.CardStack;

import java.io.FileReader;
import java.lang.reflect.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import HomeExam.scr.Main.Options;
import HomeExam.scr.Main.Cards.Card;
import HomeExam.scr.Main.Cards.DefuseCard;
import HomeExam.scr.Main.Cards.SkipCard;

public class Deck extends CardStack {
    /**
     * 
     * @throws Exception
     */
    public Deck(Options options) throws Exception {
        cardStack = new ArrayList<Card>();
        generateDeck(options);
    }

    private void generateDeck(Options options) throws Exception {
        readCardJSON(options);

    }

    private void readCardJSON(Options options) throws Exception {
        String gameCardsFileName = options.getCARDS_JSON_FILE();
        JSONParser parser = new JSONParser();

        JSONObject obj = (JSONObject) parser
                .parse(new FileReader("HomeExam/scr/Main/GameVariables/" + gameCardsFileName + ".json"));
        JSONArray cardsArrayJson = (JSONArray) obj.get("cards");
        for (Object cardJson : cardsArrayJson) {
            JSONObject cardObject = (JSONObject) cardJson;
            String cardType = (String) cardObject.get("type");
            Long cardQuantity = (Long) cardObject.get("quantity");
            addCards(cardType, cardQuantity.intValue());
        }

        JSONArray specialCardsArrayJson = (JSONArray) obj.get("specialCards");
        for (Object cardJson : specialCardsArrayJson) {
            JSONObject cardObject = (JSONObject) cardJson;
            String cardType = (String) cardObject.get("type");

            int cardQuantity = getOptionsQuantity(cardType, options);
            addCards(cardType, cardQuantity);
        }
    }

    private int getOptionsQuantity(String cardType, Options options) {
        int quantity = 0;
        switch (cardType) {
            case "ExplodingKitten":
                quantity = options.getNUM_EXPLODING_KITTENS();
                break;
            case "Defuse":
                quantity = options.getNUM_DEFUSE_CARDS();
                break;
            default:
                break;
        }
        return quantity;
    }

    private void addCards(String type, int quantity) throws Exception {
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

    public CardStack generateHand(Options options) {
        CardStack hand = new CardStack();
        for (int i = 0; i < options.getDEFUSE_CARDS_PER_PERSON(); i++) {
            hand.addCard(new DefuseCard());
        }
        for (int i = 0; i < options.getNUM_CARDS_IN_HAND(); i++) {
            Card card = drawCard();
            if (card.getIsDealable()) {
                hand.addCard(card);
            } else {
                addCardInPlace(card, getCardStackLength() - 1);
                i--;
            }
        }
        shuffle();
        return hand;
    }
}
