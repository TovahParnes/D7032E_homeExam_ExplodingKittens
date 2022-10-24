package HomeExam.scr.Main.Cards;

import HomeExam.scr.Main.Server;
import HomeExam.scr.Main.Players.Player;

public class ExplodingKittenCard extends Card {

    public ExplodingKittenCard() {
        this.name = "ExplodingKitten";
        this.description = "You must show this card immediately. Unless you have a Defuse Card, you’re dead.";
        this.isPlayable = false;
        this.isDealable = false;
    }

    public void onDraw(Server server, Player player) {
        if (player.getHand().contains("Defuse")) {
            defuseExpodingKitten(server, player);
        } else {
            player.explode();
            server.expodePlayer();
        }
    }

    private void defuseExpodingKitten(Server server, Player player) {
        Card defuseCard = new DefuseCard();
        player.removeCard(defuseCard);
        defuseCard.onPlay(server);
    }

}
