package HomeExam.scr.Main.Cards;

public class SkipCard extends Card {

    public void main() {
        this.name = "Skip";
        this.description = "Immediately end your turn without drawing a card.";
        this.isPlayable = true;
    }

    public void onPlay(int PLAYER_ID){
        //endOneTurn(PLAYER_ID);
    }

}
