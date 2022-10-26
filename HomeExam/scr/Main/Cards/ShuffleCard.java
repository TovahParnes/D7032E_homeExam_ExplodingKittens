package HomeExam.scr.Main.Cards;

import HomeExam.scr.Main.Server;

public class ShuffleCard extends Card {

    public ShuffleCard() {
        this.name = "Shuffle";
        this.description = "This card can be played to shuffle the deck.";
        this.isPlayable = true;
    }

    public void onPlay(Server server) {
        server.getDeck().shuffle();
    }

}
