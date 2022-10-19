package HomeExam.scr.Main;

class PlayedCard
{
    private int PLAYER_ID;
    private String card;

    public PlayedCard(int PLAYER_ID, String card)
    {
        this.PLAYER_ID = PLAYER_ID;
        this.card = card;
    }

    /**
     *Getters
     */
    public int getPLAYER_ID()
    {
        return PLAYER_ID;
    }

    public String getCard()
    {
        return card;
    }

}