/*****************************************************************************

 File        : BasicCheat.java

 Description : Java class defining the functionality for the game cheat

 Author      : Tony Bagnell
 Modified by   : 100239986

 ******************************************************************************/
package question2;

import java.util.*;
import question1.*;


public class BasicCheat implements CardGame {
    private Player[] players;
    private int nosPlayers;
    public static final int MINPLAYERS = 5;
    private int currentPlayer;
    private Hand discards;
    private Bid currentBid;


    public BasicCheat() {
        this(MINPLAYERS);
    }

    public BasicCheat(int n) {
        nosPlayers = n;
        players = new Player[nosPlayers];
        for (int i = 0; i < nosPlayers; i++)
            players[i] = (new BasicPlayer(new ThinkerStrategy(), this));
        currentBid = new Bid();
        currentBid.setRank(Card.Rank.TWO);
        currentPlayer = 0;
    }

//    private Player[] shufflePlayers() {
//        Random rand = new Random();
//        int randInt;
//        Player tempPlayer;
//        Player[] shuffledPLayers = new Player[nosPlayers];
//        shuffledPLayers = this.players;
//        for (int i = 0; i < nosPlayers; i++) {
//            randInt = rand.nextInt(nosPlayers);
//            tempPlayer = shuffledPLayers[i];
//            shuffledPLayers[i]= shuffledPLayers[randInt];
//            shuffledPLayers[randInt]=tempPlayer;
//        }
//        return shuffledPLayers;
//    }

    @Override
    public boolean playTurn() throws Hand.PositionNotInRangeOfHand {
        //Ask player for a play,
        System.out.println("current bid = " + currentBid);
        currentBid = players[currentPlayer].playHand(currentBid);

        System.out.println("Player bid = " + currentBid);
        //Add hand played to discard pile
        discards.add(currentBid.getHand());
        //Offer all other players the chance to call cheat
        boolean cheat = false;
        for (int i = 0; i < players.length && !cheat; i++) {
            //disallow a player to call cheat on them selves
            if (i != currentPlayer) {
                cheat = players[i].callCheat(currentBid);
                if (cheat) {
                    //update each player telling them that cheat has been called
                    for (int j = 0; j < players.length; j++){
                        players[j].cheatCalled();
                    }
                    System.out.println("Player called cheat by Player " + (i + 1));
                    if (isCheat(currentBid)) {
                        //CHEAT CALLED CORRECTLY
                        //Give the discard pile of cards to currentPlayer who then has to play again
                        players[currentPlayer].addHand(discards);
                        System.out.println("Player cheats!");
                        System.out.println("Adding cards to player " + (currentPlayer + 1));

                    } else {
                        //CHEAT CALLED INCORRECTLY
                        //Give cards to caller i who is new currentPlayer
                        System.out.println("Player Honest");
                        currentPlayer = i;
                        players[currentPlayer].addHand(discards);
                        System.out.println("Adding cards to player " + (currentPlayer + 1));
                    }
                    //If cheat is called, current bid reset to an empty bid with rank two whatever
                    //the outcome
                    currentBid = new Bid();
                    currentBid.setRank(Card.Rank.TWO);
                    //Discards now reset to empty
                    discards = new Hand();
                }
            }
        }
        if (!cheat) {
            //Go to the next player
            System.out.println("No Cheat Called");

            currentPlayer = (currentPlayer + 1) % nosPlayers;
        }
        return true;
    }

    //Check all players hands to see if any of them have 0 cards left
    public int winner() {
        for (int i = 0; i < nosPlayers; i++) {
            if (players[i].cardsLeft() == 0)
                return i + 1;
        }
        return -1;

    }

    public void initialise() {
        //Create Deck of cards
        Deck d = new Deck();
        d.shuffle();
        //Chose first player
        currentPlayer = 0;
        //Deal cards to players
        int count = 0;
        while (count<51) {
            Card temp = d.deal();
            //set the first player to be the one who is dealt two of clubs
            if (temp.equals(new Card(Card.Rank.TWO, Card.Suit.CLUBS))) {
                currentPlayer = count % nosPlayers;
            }
            players[count % nosPlayers].addCard(temp);
            count++;
        }
        //Initialise Discards
        discards = new Hand();
        currentBid = new Bid();
        currentBid.setRank(Card.Rank.TWO);
        //sets players strategy based on user input
        Scanner in = new Scanner(System.in);
        Strategy strategy = null;
        String input;
        //Gets input for what strategy each player will use
        for (int i = 0; i < nosPlayers; i++) {
            System.out.println("What strategy would player " + (i + 1) + " like to use?\n");
            do {
                input = in.nextLine();
                //uses the input from the user to generate a strategy in the factory
                strategy = StrategyFactory.setStrategy(input);
                if (strategy == null) {
                    System.out.println("Incorrect input try again\n");
                }
            } while (strategy == null);
            players[i].setStrategy(strategy);
        }
    }


    public void playGame() throws Hand.PositionNotInRangeOfHand {
        initialise();
        int c = 0;
        Scanner in = new Scanner(System.in);
        boolean finished = false;
        while (!finished) {
            //Play a hand
            System.out.println("Cheat turn for player " + (currentPlayer + 1));
            playTurn();
            System.out.println("Current discards =\n" + discards);
            c++;

            //See if the user wants play another round
            System.out.println("Turn " + c + " Complete. Press any key to continue or enter Q to quit>");
            String str = in.nextLine();
            if (str.equals("Q") || str.equals("q") || str.equals("quit"))
                finished = true;

            //check if there is a winner
            int w = winner();
            if (w > 0) {
                System.out.println("The Winner is Player " + (w));
                finished = true;
            }
        }
    }

    //checks if the player is cheating
    public static boolean isCheat(Bid b) {
        for (Card c : b.getHand()) {
            if (c.getRank() != b.getRank())
                return true;
        }
        return false;
    }

    public static void main(String[] args) throws Hand.PositionNotInRangeOfHand {
        System.out.println("How many players do you want to have");

        boolean check = true;
        int input=5;
        //Asks the user how many players they want in the game
        while(check){
            try {
                Scanner in = new Scanner(System.in);
                input = in.nextInt();
                if (input<2){
                    System.out.println("Must be at least two players, enter again: ");
                } else {
                    check = false;
                }
            }catch(Exception InputMismatchException){
                System.out.println("Incorrect input");
                check = true;
            }
        }
        BasicCheat cheat = new BasicCheat(input);
        cheat.playGame();
    }
}
