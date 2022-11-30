/*****************************************************************************

 File        : MyStrategy.java

 Date        : 18/01/2020

 Description : A java class that implements strategy which cheats if it has to,
              and will also cheat when it doesnt have to depending on how many
              cards it has in its hand. When cheating will always play the max
              number of the lowest rank in the hand but will claim the lowest
              possible rank based on past play. It will also cheat if it only
              has 2 cards left in its hand so it can finish the game and win
              When not cheating will just play max number of lowest rank.
              Calls cheat if bid is not possible and will also call cheat
              based on the turn number, (more often the higher the round)

 Author      : 100239986

 ******************************************************************************/
package question2;

import question1.*;

public class MyStrategy extends BasicStrategy {
    int turnNo = 0;
    @Override
    public boolean cheat(Bid b, Hand h) {
        //cheat if have to
        //cheat more depending on the amount of cards(how badly player is losing)
        //cheat if only two cards are left in hand and can
        //then win the game without raising suspicion
        Card.Rank lowestRank = findLowestRank(b.getRank(), h);
        if (lowestRank != null) {
            if (h.size()<=2){
                return true;
            }
            //size of the hand to the power of 1.165
            //this is because 52^1.165 is roughly equal to 100
            if (rand.nextInt(100)>=Math.pow(h.size(),1.165)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) {
        Hand playedCards = new Hand();
        //Always play the lowest 2 cards in the hand, only playing 2 to avoid suspicion
        if (cheat){
            for (int i =0; i<2&&i<h.size();i++){
                Card pastCard = null;
                for (Card j: h){
                    if (pastCard ==null || pastCard.getRank().ordinal()-j.getRank().ordinal()>=0){
                        pastCard = j;
                    }

                }
                h.remove(pastCard);
                playedCards.add(pastCard);
            }
            b.setRank(b.getRank().getNext());
        } else {//play max number of cards
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

    @Override
    public boolean callCheat(Hand h, Bid b) {
        //call cheat based on how many turns have taken place
        turnNo++;
        if (b.getCount() + h.countRank(b.getRank()) <= 4) {
            //1.05 as exponent as this gives the Strategy 80 turns before it calls cheat every time
            if (rand.nextInt(100)>=(int)Math.pow(turnNo,1.05)){
                return false;
            }
        }
        return true;
    }
}
