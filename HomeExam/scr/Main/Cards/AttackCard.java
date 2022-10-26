package HomeExam.scr.Main.Cards;

import HomeExam.scr.Main.Server;

public class AttackCard extends Card {

    public AttackCard() {
        this.name = "Attack";
        this.description = "Do not draw any cards. Instead, immediately force the next player to take 2 turns in a row.";
        this.isPlayable = true;
    }

    public void onPlay(Server server) {
        server.setNextPlayer();
        if (server.getNumTurns() == 1) {
            server.increaseNumTurns(1);
        } else {
            server.increaseNumTurns(2);
        }
    }

}
