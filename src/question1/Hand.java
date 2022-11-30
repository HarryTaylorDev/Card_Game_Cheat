/*****************************************************************************

 File        : Hand.java

 Date        : 05/01/2020

 Description : A java class file to define a group of cards, for example the
               cards a player has in their 'hand'

 Author      : 100239986

 ******************************************************************************/
package question1;

import java.io.*;
import java.util.*;

public class Hand implements Iterable<Card>, Serializable {
    private static final long serialVersionUID = 100239986 + 10;
    private transient ArrayList<Card> hand;
    private ArrayList<Card> handOrder;
    private int[] rankCount;

    //creates an empty hand
    public Hand() {
        hand = new ArrayList<>();
        handOrder = new ArrayList<>();
        rankCount = new int[13];
    }

    //creates a hand from an array or cards
    public Hand(Card[] cards) {
        this();
        for (Card i : cards) {
            hand.add(i);
            handOrder.add(i);
            ++rankCount[i.getRank().ordinal()];
        }
    }

    //creates a hand from another hand
    public Hand(Hand hand) {
        this();
        for (Card i : hand) {
            this.hand.add(i);
            handOrder.add(i);
            ++rankCount[i.getRank().ordinal()];
        }
    }

    //adds a single card to the hand
    public void add(Card card) {
        hand.add(card);
        handOrder.add(card);
        ++rankCount[card.getRank().ordinal()];
    }

    //adds all cards in a collection to the hand
    public void add(Collection<Card> cards) {
        for (Card i : cards) {
            hand.add(i);
            handOrder.add(i);
            ++rankCount[i.getRank().ordinal()];
        }
    }

    //adds all cards in another hand to the hand
    public void add(Hand hand) {
        for (Card i : hand) {
            this.hand.add(i);
            handOrder.add(i);
            ++rankCount[i.getRank().ordinal()];
        }
    }

    //removes a single card from the hand, returns false if the card is not in the hand
    public boolean remove(Card card) {
        if (hand.contains(card)) {
            --rankCount[card.getRank().ordinal()];
            hand.remove(card);
            handOrder.remove(card);
            return true;
        }
        return false;
    }

    //removes all the cards in a hand from the hand, returns false if not all the cards were in the hand
    //removes all the cards that were in the hand
    public boolean remove(Hand hand) {
        boolean check = true;
        for (Card i : hand) {
            if (this.hand.contains(i)) {
                --rankCount[i.getRank().ordinal()];
                this.hand.remove(i);
                handOrder.remove(i);
            } else {
                check =  false;
            }
        }
        return check;
    }

    //removes a card at a specific position
    public Card remove(int position) throws PositionNotInRangeOfHand {
        //checks to see if the position is in bounds of the hand
        if (position < hand.size() && position>=0) {
            Card temp = hand.get(position);
            --rankCount[temp.getRank().ordinal()];
            handOrder.remove(temp);
            hand.remove(position);
            return temp;
        } else {//throws exception if position is not in range of the hand
            throw new PositionNotInRangeOfHand("Position is not in range of the hand");
        }
    }

    //Custom exception for when a given position is out of bounds of a given hand
    public class PositionNotInRangeOfHand extends Exception {
        public PositionNotInRangeOfHand(String errorMessage) {
            super(errorMessage);
        }
    }

    @Override
    //iterator that goes through the cards in the order that they were added
    public Iterator<Card> iterator() {
        return new handIterator();
    }

    public class handIterator implements Iterator<Card> {
        private int current = 0;

        @Override
        //checks to see if there is another card left in the hand
        public boolean hasNext() {
            if (current < handOrder.size()) {
                return true;
            }
            return false;
        }

        @Override
        //returns the next card in the hand (in the order it was added)
        public Card next() {
            if (this.hasNext()) {
                return handOrder.get(current++);
            }
            throw new NoSuchElementException();
        }
    }

    //sorts the hand into descending order based on rank and then suit
    public void sortDescending() {
        Collections.sort(hand);
    }

    //sorts the hand into ascending order based on rank and then suit
    public void sortAscending() {
        Collections.sort(hand, new Card.CompareAscending());
    }

    //sorts the hand into ascending order based on suit and then rank
    public void sortSuit() {
        Collections.sort(hand, new Card.CompareSuit());
    }

    //returns the amount the hand has of a specific rank
    public int countRank(Card.Rank rank) {
        return rankCount[rank.ordinal()];
    }

    //returns the sum of the values of each card in the hand
    public int handValue() {
        int count = 0;
        for (Card i : hand) {
            count += i.getRank().getValue();
        }
        return count;
    }

    //Converts all the cards in the hand into a single string
    public String toString() {
        String output = "";
        for (Card i : hand) {
            output +=  i.toString() + "\n";
        }
        return output;
    }

    //checks to see if the hand is of the same suit
    public boolean isFlush() {
        if (hand.get(0) != null) {
            Card.Suit suit = hand.get(0).getSuit();
            for (Card i : hand) {
                if (i.getSuit() != suit) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    ///checks to see if the cards in the hand are consecutive
    ///
    ///
    ///
    ///      PLEASE FIX ME
    ///
    ///

    public boolean isStraight() {
        Collections.sort(hand,new Card.CompareAscending());
        Card previousCard = hand.get(0);
        //check if there is only one card in hand
        if (hand.size()==1){
            return false;
        }

        //check to see if each card is consecutive
        for (int i = 1; i < hand.size(); i++) {
            if ( !(i==hand.size()-1 && hand.get(i).getRank()== Card.Rank.ACE && hand.get(0).getRank()== Card.Rank.TWO)) {
                //check to see if each card is consecutive
                if (previousCard.getRank().getNext() != hand.get(i).getRank()) {
                    return false;
                }
            }else if (hand.get(i).getRank().getNext()!=hand.get(0).getRank()){
                return false;
            }
            previousCard = hand.get(i);
        }
        return true;
    }

    //returns the amount of cards in the hand
    public int size() {
        return hand.size();
    }

    //Method to write a hand to file
    public void writeFile(String filename) throws IOException {
        filename = filename + ".ser";
        FileOutputStream file = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(this);
        out.close();
    }

    //Method to read a hand from a file
    public static Hand readFile(String filename) throws IOException, ClassNotFoundException {
        filename = filename + ".ser";
        FileInputStream file = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(file);
        Hand temp = (Hand) in.readObject();
        //The hands list of cards is set as only the list of cards in order they were added is saved
        temp.hand = temp.handOrder;
        return temp;
    }

    //Testing
    public static void main(String[] args) throws PositionNotInRangeOfHand {

        Card a = new Card(Card.Rank.TEN, Card.Suit.DIAMONDS);
        Card b = new Card(Card.Rank.TEN, Card.Suit.SPADES);
        Card c = new Card(Card.Rank.TWO, Card.Suit.CLUBS);
        Card e = new Card(Card.Rank.SIX, Card.Suit.HEARTS);
        Card d = new Card(Card.Rank.SIX, Card.Suit.DIAMONDS);

        //New hand testing
        System.out.println("New hand testing, empty hand");
        Hand testHand = new Hand();
        System.out.println("Hand created");

        System.out.println("\nNew hand from array of cards");
        Card[] cardArr = new Card[5];
        cardArr[0]=a;
        cardArr[1]=b;
        cardArr[2]=c;
        cardArr[3]=d;
        cardArr[4]=e;
        Hand testHandFromArray = new Hand(cardArr);
        System.out.println("Should output these test cards:");
        for (int i =0; i<5; i++){
            System.out.println(cardArr[i]);
        }
        System.out.println("\nNew hand:");
        System.out.println(testHandFromArray);


        System.out.println("\nNew hand from hand of cards");
        System.out.println("Should output the test cards again");
        Hand testHandFromHand = new Hand(testHandFromArray);
        System.out.println(testHandFromHand);

        //Add methods testing
        System.out.println("\nAdd methods testing");
        System.out.println("Add single card");
        System.out.println("should output single card after being added");
        testHand.add(a);
        System.out.println(testHand);

        System.out.println("\nAdd a collection of cards");
        System.out.println("Should output test cards");
        ArrayList<Card> testCollection = new ArrayList<>();
        for (int i =0; i<5;i++) {
            testCollection.add(cardArr[i]);
        }
        testHand=new Hand();
        testHand.add(testCollection);
        System.out.println(testHand);

        System.out.println("\nAdd hand to current hand");
        System.out.println("Should output test cards and jack of clubs");
        testHand = new Hand();
        testHand.add(new Card(Card.Rank.JACK, Card.Suit.CLUBS));
        testHand.add(testHandFromHand);
        System.out.println(testHand);

        //remove methods testing
        System.out.println("\nremove methods testing");
        System.out.println("Remove single card");
        System.out.println("Should output above hand with out the jack of clubs");
        testHand.remove(new Card(Card.Rank.JACK, Card.Suit.CLUBS));
        System.out.println(testHand);

        System.out.println("\nRemove hand from hand");
        System.out.println("Should output only ten of diamonds");
        testHandFromHand.remove(a);
        testHand.remove(testHandFromHand);
        System.out.println(testHand);

        System.out.println("\nRemove via position");
        System.out.println("Should output 0 as no cards left in the hand");
        testHand.remove(0);
        System.out.println(testHand.size());

        //Sorting methods testing
        System.out.println("\nSorting methods testing");
        System.out.println("Descending order:");
        testHand.add(testCollection);
        testHand.sortDescending();
        System.out.println(testHand);

        System.out.println("\nAscending order:");
        testHand.sortAscending();
        System.out.println(testHand);

        System.out.println("\nSuit order:");
        testHand.sortSuit();
        System.out.println(testHand);

        //Iterator testing
        System.out.println("\nIterator testing");
        System.out.println("should print out the cards in the order they were added and not in sorted order");
        Iterator<Card> it = testHand.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }

        //count rank testing
        System.out.println("\nCount rank testing");
        System.out.println("Should output 2 for the 2 tens and then 0 for the 0 aces in the hand");
        System.out.println("Tens: "+testHand.countRank(Card.Rank.TEN));
        System.out.println("Ace: "+testHand.countRank(Card.Rank.ACE));

        //Hand value testing
        System.out.println("\nHand value testing");
        System.out.println("Should output 34 for the sum of the values of the test cards");
        System.out.println(testHand.handValue());

        //Is flush testing
        System.out.println("\nTesting if the hand is a flush");
        System.out.println("Should output false");
        System.out.println(testHand.isFlush());
        System.out.println("should output true after SIX of HEARTS,TEN of SPADES and TWO of CLUBS are removed");
        testHand.remove(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        testHand.remove(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        testHand.remove(new Card(Card.Rank.TWO, Card.Suit.CLUBS));
        System.out.println(testHand.isFlush());

        //Is straight testing
        System.out.println("\nTesting if the hand is a straight");
        System.out.println("Should output false for the test cards");
        testHand = new Hand(cardArr);
        System.out.println(testHand.isStraight());

        System.out.println("\nShould output true with only ten of diamonds and nine of diamonds in the hand");
        testHand = new Hand();
        testHand.add(new Card(Card.Rank.NINE, Card.Suit.DIAMONDS));
        testHand.add(a);
        System.out.println(testHand.isStraight());

        System.out.println("\nShould output true with only ace of diamonds and two of diamonds in the hand");
        testHand = new Hand();
        testHand.add(new Card(Card.Rank.ACE, Card.Suit.DIAMONDS));
        testHand.add(new Card(Card.Rank.TWO, Card.Suit.DIAMONDS));
        System.out.println(testHand.isStraight());

        //Size testing
        System.out.println("\nSize testing");
        System.out.println("should output two for the ace and two in the hand");
        System.out.println(testHand.size());

        System.out.println("\nShould output 0 after both cards are removed");
        testHand.remove(0);
        testHand.remove(0);
        System.out.println(testHand.size());

        //Write hand to file test
        System.out.println("\nWrite hand to file test");
        testHand = new Hand(cardArr);
        testHand.sortAscending();
        boolean check = false;
        try {
            testHand.writeFile("writeHandTest");
        } catch(Exception IOException) {
            check = true;
        }
        if (!check) {
            System.out.println("write succeeded");
        }else{
            System.out.println("write failed");
        }

        //read hand file test
        System.out.println("\nread hand file test");
        System.out.println("Should output hand in the test card order as it should " +
                "be saved only by the order they were added to the hand");
        check = false;
        try {
            Hand temp = Hand.readFile("writeHandTest");
            for (Card i :temp) {
                System.out.println(i);
            }
        } catch(Exception IOException) {
            check = true;
        }
        if (!check) {
            System.out.println("read succeeded");
        }else{
            System.out.println("read failed");
        }

        testHand = new Hand();
        testHand.add(new Card(Card.Rank.ACE, Card.Suit.CLUBS));
        testHand.add(new Card(Card.Rank.KING, Card.Suit.CLUBS));
        System.out.println(testHand.isStraight());
    }
}
