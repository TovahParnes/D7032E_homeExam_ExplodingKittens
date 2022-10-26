package HomeExam.scr.Main;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Options {

    private static int MIN_NUM_PLAYERS;
    private static int MAX_NUM_PLAYERS;
    private static int DEFUSE_CARDS_PER_PERSON;
    private static int NUM_CARDS_IN_HAND;
    private static int SECONDS_TO_PLAY_NOPE;
    private static int NUM_EXPLODING_KITTENS;
    private static int NUM_DEFUSE_CARDS;
    private static String CARDS_JSON_FILE;

    private static int NUM_ONLINE_PLAYERS;
    private static int NUM_BOTS;
    private static int NUM_PLAYERS;

    /**
     * @param fileName         String - the name of the file to read from
     * @param numOnlinePlayers int - the number of online players
     * @param numBots          int - the number of bots
     * @throws Exception
     */
    public Options(String fileName, int numOnlinePlayers, int numBots) throws Exception {
        NUM_ONLINE_PLAYERS = numOnlinePlayers;
        NUM_BOTS = numBots;
        NUM_PLAYERS = NUM_ONLINE_PLAYERS + NUM_BOTS;
        readOptionsJSON(fileName);
    }

    public int getMIN_NUM_PLAYERS() {
        return MIN_NUM_PLAYERS;
    }

    public int getMAX_NUM_PLAYERS() {
        return MAX_NUM_PLAYERS;
    }

    public int getDEFUSE_CARDS_PER_PERSON() {
        return DEFUSE_CARDS_PER_PERSON;
    }

    public int getNUM_CARDS_IN_HAND() {
        return NUM_CARDS_IN_HAND;
    }

    public int getSECONDS_TO_PLAY_NOPE() {
        return SECONDS_TO_PLAY_NOPE;
    }

    public int getNUM_EXPLODING_KITTENS() {
        return NUM_EXPLODING_KITTENS;
    }

    public int getNUM_DEFUSE_CARDS() {
        return NUM_DEFUSE_CARDS;
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

    public String getCARDS_JSON_FILE() {
        return CARDS_JSON_FILE;
    }

    /**
     * Reads the options from the JSON file
     * 
     * @param fileName
     * @throws Exception
     */
    public static void readOptionsJSON(String fileName) throws Exception {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("HomeExam/scr/Main/GameVariables/" + fileName + ".json"));
            JSONObject jsonObject = (JSONObject) obj;
            MIN_NUM_PLAYERS = ((Long) jsonObject.get("MIN_NUM_PLAYERS")).intValue();
            MAX_NUM_PLAYERS = ((Long) jsonObject.get("MAX_NUM_PLAYERS")).intValue();
            DEFUSE_CARDS_PER_PERSON = ((Long) jsonObject.get("DEFUSE_CARDS_PER_PERSON")).intValue();
            NUM_CARDS_IN_HAND = ((Long) jsonObject.get("NUM_CARDS_IN_HAND")).intValue();
            SECONDS_TO_PLAY_NOPE = ((Long) jsonObject.get("SECONDS_TO_PLAY_NOPE")).intValue();
            NUM_EXPLODING_KITTENS = NUM_PLAYERS
                    + ((Long) jsonObject.get("NUM_EXPLODING_KITTENS_PER_PLAYERS")).intValue();
            CARDS_JSON_FILE = (String) jsonObject.get("CARDS_JSON_FILE");

            JSONObject defuseCardsObject = (JSONObject) jsonObject.get("DEFUSE_CARDS_IN_DECK");
            String numPlayers = Integer.toString(NUM_PLAYERS);
            NUM_DEFUSE_CARDS = ((Long) defuseCardsObject.get(numPlayers)).intValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
