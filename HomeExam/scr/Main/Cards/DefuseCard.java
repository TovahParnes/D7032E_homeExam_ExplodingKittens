package HomeExam.scr.Main.Cards;

import HomeExam.scr.Main.Server;
import HomeExam.scr.Main.Players.Player;

public class DefuseCard extends Card {

    public DefuseCard() {
        this.name = "Defuse";
        this.description = "This card can be played on an Exploding Kitten card to defuse it.";
        this.isPlayable = false;
    }

    public void onPlay(Server server, Player player) {
        server.defuseExplodingKitten();
    }

}
