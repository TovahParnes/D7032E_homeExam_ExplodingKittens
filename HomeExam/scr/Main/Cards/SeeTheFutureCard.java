package HomeExam.scr.Main.Cards;

import HomeExam.scr.Main.Server;

public class SeeTheFutureCard extends Card {

    private int numberOfCardsToSee;

    public SeeTheFutureCard() {
        this.name = "SeeTheFuture";
        this.description = "This card can be played to see the top 3 cards of the deck.";
        this.isPlayable = true;
        numberOfCardsToSee = 3;
    }

    public void onPlay(Server server) {
        String topCards = server.getDeck().getTopCards(numberOfCardsToSee);
        server.sendToCurrentPlayer("The top " + numberOfCardsToSee + " cards are: " + topCards);
    }

}
