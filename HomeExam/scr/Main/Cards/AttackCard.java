package HomeExam.scr.Main.Cards;

public class AttackCard extends Card{

    public AttackCard(){
        this.name = "Attack";
        this.description = "Do not draw any cards. Instead, immediately force the next player to take 2 turns in a row.";
        this.isPlayable = true;
    }
    
}
