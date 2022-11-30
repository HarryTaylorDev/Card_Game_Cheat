/*****************************************************************************

 File        : HumanStrategy.java

 Date        : 10/01/2020

 Description : A java class that implements strategy and allows a human to
               input the exact actions to play cheat

 Author      : 100239986

 ******************************************************************************/
package question2;

import question1.*;


import java.util.Scanner;

public class HumanStrategy extends BasicStrategy {

    //decides if the player is going to cheat
    @Override
    public boolean cheat(Bid b, Hand h) {
        System.out.println("Your cards are: \n " + h);
        //finds first rank in hand that is above the the bid rank
        Card.Rank lowestRank = findLowestRank(b.getRank(), h);

        //if there is no rank higher user is given no choice and has too cheat
        if (lowestRank != null) {
            //if there is a higher rank user can decide to cheat or not
            Scanner in = new Scanner(System.in);
            System.out.println("Do you want to cheat: y/n ");
            String str = in.nextLine();
            if (str.equals("Y") || str.equals("y") || str.equals("yes")) {
                return true;
            }
            return false;
        }
        System.out.println("You must cheat as you have no higher cards\n");
        return true;
    }

    //converts a string into a single card, returns null if failed to turn into a string
    private Card stringToCard(String cardInput) {
        //input split into rank and suit
        String[] cardStrings = cardInput.split(" ");
        //checks string was split into only 2 new strings
        if (cardStrings.length==2) {
            String rank = cardStrings[0].toUpperCase();
            String suit = cardStrings[1].toUpperCase();
            //trys to convert strings into a rank and a suit
            try {
                Card.Rank cardRank = Card.Rank.valueOf(rank);
                Card.Suit cardSuit = Card.Suit.valueOf(suit);
                return new Card(cardRank, cardSuit);

                //if the conversion fails the exception is caught and an error message output
            } catch (Exception IllegalArgumentException) {
                System.out.println("Incorrect rank or suit, please enter another card\n");
            }
        }
        return null;
    }

    //Chooses cards to put in the bid based on if the player is cheating or not
    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) {
        Scanner in = new Scanner(System.in);
        Hand playedCards = new Hand();
        Card card;
        String ans;
        int counter = 0;
        Card.Rank cardRank = Card.Rank.ACE;
        boolean check = true;

        do {//loops until at least one card is in the bid and the user does not want to enter anymore cards
            System.out.println("What card do you want to play?\n" +
                    "Enter the rank followed by a space and then the suit of the card you want to play\n");
            String cardInput = in.nextLine();
            card = stringToCard(cardInput);
            //if the string was not converted to a card an error message is output
            // and the user can input another card
            if (card != null) {
                //Checks to see if the player has cheated when they said they were not going too
                if ((counter == 0 && (card.getRank() == b.getRank())||card.getRank()==b.getRank().getNext())||
                        (!cheat
                            && card.getRank().ordinal() == cardRank.ordinal()
                            && (card.getRank() == b.getRank())||card.getRank()==b.getRank().getNext())
                    || cheat) {
                    //try's to remove the card
                    if (h.remove(card)) {
                        playedCards.add(card);
                        cardRank = card.getRank();
                        counter++;
                    } else {
                        System.out.println("your hand does not contain the card " + card + "\n");
                    }
                } else {
                    System.out.println("you must choose cards of the same rank and choose a rank that is higher than the last played rank\n");
                }
            } else {
                System.out.println("Incorrect input\n");
            }

            //lets the user enter another if there is at least one in the hand or less than 4 already
            if (counter > 0 && counter < 4) {
                System.out.println("Do you want to add another card? y/n:\n");
                ans = in.nextLine();
                if (ans.equals("n") && counter >= 1) {
                    check = false;
                }
            }
            //if the user has 4 or more cards the loop ends and they can add no more
            if (counter >= 4) check = false;
        } while (check);


        if (cheat) {
            //if the player is cheating they have to decide what rank to tell the other players
            check = false;

            //Loops until the player enters a valid rank
            while (!check) {
                System.out.println("What rank do you want to declare?\n");
                try {
                    String rank = in.nextLine();
                    rank = rank.toUpperCase();
                    cardRank = Card.Rank.valueOf(rank);
                    check = true;
                    if ((cardRank != b.getRank())&&cardRank!=b.getRank().getNext()){
                        check =false;
                        System.out.println("You must input a the same rank as the bid or one higher, bid rank: "+b.getRank());
                    }

                } catch (Exception IllegalArgumentException) {
                    System.out.println("Could not find rank, please enter your rank again\n");
                }
            }
        }
        //sets the bid rank and hand
        b.setRank(cardRank);
        b.setHand(playedCards);

        return b;
    }

    //Decides if the player is going to call cheat on the last bid
    @Override
    public boolean callCheat(Hand h, Bid b) {
        Scanner in = new Scanner(System.in);
        System.out.println("Do you want to call cheat, enter C to call cheat: ");
        String str = in.nextLine();
        if (str.equals("C") || str.equals("c") || str.equals("cheat")) {
            return true;
        }
        return false;
    }
}
