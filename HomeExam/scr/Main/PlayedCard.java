package HomeExam.scr.Main;

import HomeExam.scr.Main.Cards.Card;

class PlayedCard
{
    private int PLAYER_ID;
    private Card card;

    public PlayedCard(int PLAYER_ID, Card card)
    {
        this.PLAYER_ID = PLAYER_ID;
        this.card = card;
    }

    /**
     *Getters
    **/
    public int getPLAYER_ID()
    {
        return PLAYER_ID;
    }

    public Card getCard()
    {
        return card;
    }

}