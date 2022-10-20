package HomeExam.scr.Main.Cards;

public class SkipCard extends Card {

    public SkipCard() {
        this.name = "Skip";
        this.description = "Immediately end your turn without drawing a card.";
    }

    public void onPlay(int PLAYER_ID){
        //endOneTurn(PLAYER_ID);
    }

}
