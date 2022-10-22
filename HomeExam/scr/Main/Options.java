package HomeExam.scr.Main;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Options {

    private static int MIN_NUM_PLAYERS;
    private static int MAX_NUM_PLAYERS;
    private static int DIFFUSE_CARDS_PER_PERSON;
    private static int NUM_CARDS_IN_HAND;
    private static int SECONDS_TO_PLAY_NOPE;
    private static int MUN_EXPLODING_KITTENS_PER_PLAYERS;
    private static int DIFFUSE_CARDS_IN_DECK;
    private static String CARDS_JSON_FILE;

    private static int NUM_ONLINE_PLAYERS;
    private static int NUM_BOTS;
    private static int NUM_PLAYERS = NUM_ONLINE_PLAYERS + NUM_BOTS;

    public Options(String fileName, int numOnlinePlayers, int numBots) throws Exception{
        NUM_ONLINE_PLAYERS = numOnlinePlayers;
        NUM_BOTS = numBots;
        readOptionsJSON(fileName, NUM_PLAYERS);
    }

    public int getMIN_NUM_PLAYERS() {
        return MIN_NUM_PLAYERS;
    }

    public int getMAX_NUM_PLAYERS() {
        return MAX_NUM_PLAYERS;
    }

    public int getDIFFUSE_CARDS_PER_PERSON() {
        return DIFFUSE_CARDS_PER_PERSON;
    }

    public int getNUM_CARDS_IN_HAND() {
        return NUM_CARDS_IN_HAND;
    }

    public int getSECONDS_TO_PLAY_NOPE() {
        return SECONDS_TO_PLAY_NOPE;
    }

    public int getMUN_EXPLODING_KITTENS_PER_PLAYERS() {
        return MUN_EXPLODING_KITTENS_PER_PLAYERS;
    }

    public int getDIFFUSE_CARDS_IN_DECK() {
        return DIFFUSE_CARDS_IN_DECK;
    }

    public int getNUM_ONLINE_PLAYERS() {
        return NUM_ONLINE_PLAYERS;
    }

    public int getNUM_BOTS() {
        return NUM_BOTS;
    }

    public int getNUM_PLAYERS() {
        return NUM_PLAYERS;
    }

    public static void readOptionsJSON(String fileName, int numPlayers) throws Exception
    {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("HomeExam/scr/Main/GameVariables/" + fileName + ".json"));
            JSONObject jsonObject = (JSONObject) obj;
            MIN_NUM_PLAYERS = Integer.parseInt(jsonObject.get("MIN_NUM_PLAYERS").toString());
            MAX_NUM_PLAYERS = Integer.parseInt(jsonObject.get("MAX_NUM_PLAYERS").toString());
            DIFFUSE_CARDS_PER_PERSON = Integer.parseInt(jsonObject.get("DIFFUSE_CARDS_PER_PERSON").toString());
            NUM_CARDS_IN_HAND = Integer.parseInt(jsonObject.get("NUM_CARDS_IN_HAND").toString());
            SECONDS_TO_PLAY_NOPE = Integer.parseInt(jsonObject.get("SECONDS_TO_PLAY_NOPE").toString());
            MUN_EXPLODING_KITTENS_PER_PLAYERS = numPlayers + Integer.parseInt(jsonObject.get("MUN_EXPLODING_KITTENS_PER_PLAYERS").toString());
            CARDS_JSON_FILE = jsonObject.get("CARDS_JSON_FILE").toString();
            JSONArray diffuseCardsArrayJson = (JSONArray) jsonObject.get("DIFFUSE_CARDS_IN_DECK");
            System.out.println(diffuseCardsArrayJson.get(numPlayers));
            DIFFUSE_CARDS_IN_DECK = (int) diffuseCardsArrayJson.get(numPlayers);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    
}

