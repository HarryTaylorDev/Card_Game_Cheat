/*****************************************************************************

 File        : ThinkerStrategy.java

 Date        : 15/01/2020

 Description : A java class that implements strategy and provides a more
               advanced simulation for how a player might play the game cheat,
               more advanced than basic strategy

 Author      : 100239986

 ******************************************************************************/
package question2;

import question1.*;


public class ThinkerStrategy extends BasicStrategy {
    //private Hand discarded = new Hand();
    private int[] discarded = new int[13];

    //decides if the player is going to cheat
    @Override
    public boolean cheat(Bid b, Hand h) {
        //cheat if necessary
        //also cheat sometimes when it doesnt have too
        Card.Rank lowestRank = findLowestRank(b.getRank(), h);
        if (lowestRank != null) {
            if (rand.nextInt(100) >= 10) {
                return false;
            }
        }
        return true;
    }

    private static int[] generateProbabilityArray(Hand h){
        int arraySize = (h.size()*(h.size()+1))/2;
        int[] probArr = new int[arraySize];
        int counter = 0;
        for(int i = 0; i<h.size();i++){
            for (int j = 0; j < i+1;j++){
                probArr[counter]=i;
                counter++;
            }
        }
        return probArr;
    }

    //Chooses cards to put in the bid based on if the player is cheating or not
    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) throws Hand.PositionNotInRangeOfHand {
        //if cheating, be more likely too choose higher cards than lower
        Hand playedCards = new Hand();
        if (cheat) {
            int randomAmount=rand.nextInt(((h.size()-1)%4)+1)+1;
            for (int i = 0;i<randomAmount;i++) {
                int[] probArray = generateProbabilityArray(h);
                Card temp = h.remove(probArray[rand.nextInt(probArray.length)]);
                discarded[temp.getRank().ordinal()]++;
                playedCards.add(temp);
            }
            b.setHand(playedCards);
            b.setRank(b.getRank().getNext());

        } else {//if not, play max number of cards most of the time but sometimes not
            b.setRank(findLowestRank(b.getRank(), h));
            boolean firstCardCheck = true;
            for (Card i : h) {
                if (i.getRank().ordinal() == b.getRank().ordinal()) {
                    if (rand.nextInt(100) < 90 || firstCardCheck) {
                        playedCards.add(i);
                        discarded[b.getRank().ordinal()]++;
                        firstCardCheck = false;
                    }
                }
            }
            h.remove(playedCards);
            b.setHand(playedCards);
        }
        return b;
    }

    //Decides if the player is going to call cheat on the last bid
    @Override
    public boolean callCheat(Hand h, Bid b) {
        //make informed decision
        //store all cards placed in discard pile by itself
        h.sortAscending();
        int p = 2;
        int rankCount = b.getCount() + h.countRank(b.getRank()) + discarded[b.getRank().ordinal()];
        //always call cheat if not possible based on past play
        //also call cheat based on probability p dependant on how many of the current rank is in the discard pile
        int probability = p*(int) Math.pow(rankCount, 2);
        int chance = rand.nextInt(100);
        if (chance <= probability) {
            return true;
        }
        return false;
    }

    public void cheatCalled() {
        discarded = new int[13];
    }

    public static void main(String[] args) {
        Card high = new Card(Card.Rank.TEN, Card.Suit.DIAMONDS);
        Card middle = new Card(Card.Rank.TEN, Card.Suit.SPADES);
        Card low = new Card(Card.Rank.TWO, Card.Suit.CLUBS);
        Card a = new Card(Card.Rank.SIX, Card.Suit.DIAMONDS);
        Card b = new Card(Card.Rank.SIX, Card.Suit.HEARTS);
        Hand testHand = new Hand();
        testHand.add(high);
        testHand.add(middle);
        testHand.add(low);
        testHand.add(a);
        testHand.add(b);


        int[] test = ThinkerStrategy.generateProbabilityArray(testHand);
        for (int i = 0; i<test.length;i++) {
            System.out.println(test[i]);
        }
    }
}
