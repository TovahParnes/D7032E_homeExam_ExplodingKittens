package HomeExam.scr.Main.Cards;

import HomeExam.scr.Main.Server;
import HomeExam.scr.Main.Players.Player;

public class FavorCard extends Card {

    public FavorCard() {
        this.name = "Favor";
        this.description = "This card can be played on another player to force them to give you a card of their choice.";
        this.isPlayable = true;
        this.hasTarget = true;
    }

    public void onPlay(Server server, Player targetPlayer) {
        String cardName = server.readInputCardName(targetPlayer);
        Card card = targetPlayer.getHand().drawCard(cardName);
        server.getCurrentPlayer().getHand().addCard(card);
        server.giveCard(targetPlayer, card);
    }

}
