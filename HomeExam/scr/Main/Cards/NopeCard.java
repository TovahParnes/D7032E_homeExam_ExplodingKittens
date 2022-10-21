package HomeExam.scr.Main.Cards;

public class NopeCard extends Card{

    public NopeCard() {
        this.name = "Nope";
        this.description = "This card can be played on any other card to negate its effect, except for an Exploding Kitten or Diffuse card.";
        this.isPlayable = false;
    }
    
    
}
