package HomeExam.scr.Main.Cards;

public class Card {

    protected String name;
    protected String description;
    
    public void onDraw(){}

    public void onPlay(){}

    public class SkipCard extends Card{
        
        public void init(){
            this.name = "skip";
            this.description = "Immediately end your turn without drawing a card.";
        }
        
        

    }
    
}
