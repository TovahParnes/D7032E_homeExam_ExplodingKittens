package HomeExam.scr.Main.Cards;

import HomeExam.scr.Main.Server;
import HomeExam.scr.Main.Players.Player;

abstract public class Card {

    protected String name;
    protected String description;
    protected Boolean isPlayable;
    protected Boolean isDealable = true;
    protected Boolean hasTarget = false;

    public void onDraw(Server server, Player player) {
    }

    public void onPlay(Server server) {
        System.out.println("TEMP: Card " + name + " played");
    }

    public void onPlay(Server server, int targetID) {
        System.out.println("TEMP: Card " + name + " played, targetID: " + targetID);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Boolean getIsPlayable() {
        return isPlayable;
    }

    public Boolean getIsDealable() {
        return isDealable;
    }

    public Boolean getHasTarget() {
        return hasTarget;
    }

    public String getCardInfo() {
        return this.name + ": " + this.description;
    }

    public Boolean equals(Card card) {
        return this.name.equals(card.getName());
    }
}
