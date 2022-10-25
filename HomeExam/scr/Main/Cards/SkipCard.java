package HomeExam.scr.Main.Cards;

import HomeExam.scr.Main.Server;

public class SkipCard extends Card {

    public SkipCard() {
        this.name = "Skip";
        this.description = "Immediately end your turn without drawing a card.";
        this.isPlayable = true;
    }

    public void onPlay(Server server) {
        server.endTurn();
    }

}
