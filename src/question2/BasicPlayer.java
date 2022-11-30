/*****************************************************************************

 File        : BasicPlayer.java

 Date        : 12/01/2020

 Description : A java class that implements player and provides functionality
               for a single player for the card game cheat

 Author      : 100239986

 ******************************************************************************/
package question2;

import question1.*;

public class BasicPlayer implements Player {
    Hand hand;
    private Strategy strategy;
    private CardGame cardGame;

    //creates a new player and sets their strategy and game type
    public BasicPlayer(Strategy strategy, BasicCheat basicCheat) {
        hand = new Hand();
        this.strategy = strategy;
        cardGame = basicCheat;
    }

    //method to add a single card
    public void addCard(Card c) {
        hand.add(c);
    }


    //method to add all cards from on hand into the players hand
    public void addHand(Hand h) {
        hand.add(h);
    }

    @Override
    //method to return the size of the players hand
    public int cardsLeft() {
        return hand.size();
    }

    @Override
    //method to set a players game type
    public void setGame(CardGame g) {
        cardGame = g;
    }

    @Override
    //method to set a players strategy type
    public void setStrategy(Strategy s) {
        strategy = s;
    }

    @Override
    //method to decide what cards the player is going to play, includes wheather the player is going to cheat or not
    public Bid playHand(Bid b) throws Hand.PositionNotInRangeOfHand {
        return strategy.chooseBid(b, hand, strategy.cheat(b, hand));
    }

    @Override
    //method to decide if a player is going to call cheat or not on the last bid
    public boolean callCheat(Bid b) {
        return strategy.callCheat(hand, b);
    }

    //method to reset the stored discard pile in a thinker strategy
    public void cheatCalled() {
        if (strategy instanceof ThinkerStrategy){
            ((ThinkerStrategy) strategy).cheatCalled();
        }
    }
}
