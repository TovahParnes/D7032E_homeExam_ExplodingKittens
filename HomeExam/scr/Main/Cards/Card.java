package HomeExam.scr.Main.Cards;

abstract public class Card {

    protected String name;
    protected String description;
    protected Boolean isPlayable;
    protected Boolean isDealable = true;
    protected Boolean hasTarget = false;

    public void onDraw() {
    }

    public void onPlay() {
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
