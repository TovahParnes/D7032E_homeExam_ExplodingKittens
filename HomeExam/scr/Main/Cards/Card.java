package HomeExam.scr.Main.Cards;

abstract public class Card {

    protected String name;
    protected String description;
    protected Boolean isPlayable;
    
    public void onDraw(){}

    public void onPlay(){}

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public Boolean getIsPlayable(){
        return isPlayable;
    }
    
}
