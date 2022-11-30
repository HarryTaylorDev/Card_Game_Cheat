/*****************************************************************************

 File        : BasicStrategy.java

 Date        : 08/01/2020

 Description : A java class that implements strategy and provides basic
               simulation for how a player might play the game cheat

 Author      : 100239986

 ******************************************************************************/
package question2;

import question1.*;

import java.util.Random;

public class BasicStrategy implements Strategy {
    protected static final Random rand = new Random();

    //finds the lowest rank that is above the rank based in
    protected static Card.Rank findLowestRank(Card.Rank r, Hand h) {
        Card.Rank pastRank = null;
        for (Card i : h) {
            if (i.getRank()==r || i.getRank()==r.getNext()) {
                if (pastRank==null || i.getRank().ordinal() - pastRank.ordinal() <= 0) {
                    pastRank = i.getRank();
                }
            }
        }
        return pastRank;
    }

    //decides if the player is going to cheat
    @Override
    public boolean cheat(Bid b, Hand h) {
        //call cheat if hand has no rank higher than the bid rank
        Card.Rank lowestRank = findLowestRank(b.getRank(), h);
        if (lowestRank != null) {
            return false;
        }
        return true;
    }

    //Chooses cards to put in the bid based on if the player is cheating or not
    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) throws Hand.PositionNotInRangeOfHand {
        //if cheating play one random card
        Hand playedCards = new Hand();
        Card temp;
        if (cheat) {
            temp = h.remove(rand.nextInt(h.size()));
            playedCards.add(temp);
            b.setRank(b.getRank().getNext());

        } else { //if not cheating play the max no of lowest value cards
            b.setRank(findLowestRank(b.getRank(), h));
            for (Card i : h) {
                if (i.getRank().ordinal() == b.getRank().ordinal()) {
                    playedCards.add(i);
                }
            }
            h.remove(playedCards);
        }
        b.setHand(playedCards);
        return b;
    }

    //Decides if the player is going to call cheat on the last bid
    @Override
    public boolean callCheat(Hand h, Bid b) {
        if (b.getCount() + h.countRank(b.getRank()) > 4) {
            return true;
        }
        return false;
    }
}
