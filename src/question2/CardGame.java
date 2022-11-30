package question2;

import question1.*;

public interface CardGame {
/**
 * Initialise the card game	
 */
	public void initialise();	
/**
 * Plays a single turn of the game	
 * @return true if play made
 */
	public boolean playTurn() throws Hand.PositionNotInRangeOfHand;
/**
 * 	
 * @return an integer representing the winner
 */
	public int winner();
}
