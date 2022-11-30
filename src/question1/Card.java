/*****************************************************************************

 File        : Card.java

 Date        : 02/01/2020

 Description : Java class defining the functionality for a single card

 Author      : 100239986
 ******************************************************************************/
package question1;

import java.io.*;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.lang.Math;

public class Card implements Serializable, Comparable<Card> {

    private static final long serialVersionUID = 100239986;

    public Card(Rank rank, Suit suit) {
        //rank and suit set
        this.rank = rank;
        this.suit = suit;
    }

    public enum Rank {
        //all ranks and their values
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
        SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(10), QUEEN(10), KING(10), ACE(11);

        private static final Rank[] ranks = values();
        private int value;

        //sets the value of a rank
        Rank(int v) {
            value = v;
        }

        //returns previous rank
        public Rank getPrevious() {
            return ranks[(ordinal() + 12) % 13];
        }

        //returns next rank
        public Rank getNext() {
            return ranks[(ordinal() + 1) % 13];
        }

        //returns the value of a rank
        public int getValue() {
            return value;
        }
    }

    public enum Suit {
        //suit values
        CLUBS, DIAMONDS, HEARTS, SPADES;
        private static final Suit[] suits = values();
        private static final Random rand = new Random();

        //returns a random suit
        public static Suit randSuit() {
            return suits[rand.nextInt(4)];
        }
    }

    private Rank rank;
    private Suit suit;

    //returns the cards rank
    public Rank getRank() {
        return rank;
    }

    //returns the cards suit
    public Suit getSuit() {
        return suit;
    }

    //converts a card to a string for printing to output
    public String toString() {
        return rank.toString() + " of " + suit.toString();
    }

    //compares current card to a given card based on rank and suit, compares in descending order
    @Override
    public int compareTo(Card c) {
        return (this.suit.ordinal() + 1 + c.rank.ordinal() * 4) - (c.suit.ordinal() + 1 + this.rank.ordinal() * 4);
    }

    //To use a collections .contains method with objects that have the same attributes but are not the same
    // exact object, equals and hashcode must be overridden, i followed code from here:
    //https://stackoverflow.com/questions/12697407/arraylist-remove-is-not-removing-an-object
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Card)) {
            return false;
        }
        Card other = (Card) o;
        return this.rank.equals(other.rank) && this.suit.equals(other.suit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rank, this.suit);
    }

    //returns the difference in ranks between two cards
    public static int difference(Card c1, Card c2) {
        return Math.abs(c1.getRank().ordinal() - c2.getRank().ordinal()) + 1;
    }

    //returns the difference in values between two cards
    public static int differenceValue(Card c1, Card c2) {
        return Math.abs(c1.getRank().getValue() - c2.getRank().getValue());
    }

    //comparator that allows for the sorting of cards in ascending order
    public static class CompareAscending implements Comparator<Card> {
        @Override
        public int compare(Card card1, Card card2) {
            return (card1.suit.ordinal() + 1 + card1.rank.ordinal() * 4) - (card2.suit.ordinal() + 1 + card2.rank.ordinal() * 4);
        }
    }

    //comparator that allows for sorting in order of suits
    public static class CompareSuit implements Comparator<Card> {
        @Override
        public int compare(Card card1, Card card2) {
            return (card1.suit.ordinal() * 13 + card1.rank.ordinal() + 1) - (card2.suit.ordinal() * 13 + card2.rank.ordinal() + 1);
        }
    }

    //Testing method to show knowledge of lambdas
    public static void selectTest(Card testCard) {
        Card[] exampleCards = new Card[4];
        exampleCards[0] = new Card(Rank.NINE, Suit.DIAMONDS);
        exampleCards[1] = new Card(Rank.KING, Suit.HEARTS);
        exampleCards[2] = new Card(Rank.ACE, Suit.CLUBS);
        exampleCards[3] = new Card(Rank.TWO, Suit.CLUBS);
        int ans;
        CompareAscending compAscend = new CompareAscending();
        System.out.println("Comparing in ascending order:\n");
        for (int i = 0; i < 4; i++) {
            ans = compAscend.compare(testCard, exampleCards[i]);
            System.out.println(testCard + (ans > 0 ? " is larger than " : (ans < 0 ? " is smaller than " : "is equal too ")) + exampleCards[i] + "\n");
        }

        CompareSuit compSuit = new CompareSuit();
        System.out.println("Comparing in suit order:\n");
        for (int i = 0; i < 4; i++) {
            ans = compSuit.compare(testCard, exampleCards[i]);
            System.out.println(testCard + (ans > 0 ? " is larger than " : (ans < 0 ? " is smaller than " : "is equal too ")) + exampleCards[i] + "\n");
        }

        Comparator<Card> testLambda = (Card a, Card b) -> {
            if (a.compareTo(b) <= 0) {
                return 1;
            } else if (compSuit.compare(a, b) < 0) {
                return 1;
            } else {
                return -1;
            }
        };

        System.out.println("Comparing with the lambda:\n");
        for (int i =0; i<4;i++) {
            ans = testLambda.compare(testCard, exampleCards[i]);
            System.out.println(testCard + (ans > 0 ? " is larger than " : (ans < 0 ? " is smaller than " : " is equal too ")) + exampleCards[i] + "\n");
        }
    }

    //function to write a single card to file
    public void writeFile(String filename) throws IOException {
        filename = filename + ".ser";
        FileOutputStream file = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(this);
        out.close();
    }

    //function to read in a single card from a file
    public static Card readFile(String filename) throws IOException, ClassNotFoundException {
        filename = filename + ".ser";
        FileInputStream file = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(file);
        return (Card) in.readObject();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        /**TO DO**/

        //New card test
        System.out.println("\nNew card test");
        System.out.println("\nnew card should be created with rank 2, suit clubs: ");
        Card newCard = new Card(Rank.TWO, Suit.CLUBS);
        //Card should be output because of toString being overridden
        System.out.println(newCard);

        //Previous rank test
        System.out.println("\nPrevious rank test");
        System.out.println("\nShould output ace as the previous rank to two:");
        Rank two = Rank.TWO;
        System.out.println(two.getPrevious());

        //next rank test
        System.out.println("\nNext rank test");
        System.out.println("\nShould output two as the next rank to ace:");
        Rank ace = Rank.ACE;
        System.out.println(ace.getNext());

        //rank value test
        System.out.println("\nRank value test");
        System.out.println("\nShould output 11 for the value of ace");
        System.out.println(ace.getValue());

        //Random suit test
        System.out.println("\nRandom suit test");
        System.out.println("\nShould output 3 random ranks");
        for (int i = 0; i < 3; i++) {
            System.out.println(Suit.randSuit());
        }

        //getRank test
        System.out.println("\nget rank test");
        System.out.println("\nShould output the rank two");
        System.out.println(newCard.getRank());

        //getSuit test
        System.out.println("\nget suit test");
        System.out.println("\nshould output the suit clubs");
        System.out.println(newCard.getSuit());

        Card high = new Card(Rank.ACE, Suit.SPADES);
        Card middle = new Card(Rank.SIX, Suit.HEARTS);
        Card low = new Card(Rank.TWO, Suit.CLUBS);

        //compareTo test
        System.out.println("\nCompare Descending test");
        System.out.println("\nshould output high is smaller, high and high are equal and low is larger than high for \n" +
                "comparing high to low, high to high and low to high respectively:");;
        System.out.println("high: " + high + " Low: " + low + "\n");
        int ans = high.compareTo(low);
        System.out.println(high + (ans > 0 ? " is larger than " : (ans < 0 ? " is smaller than " : "is equal too ")) + low + "\n");
        ans = high.compareTo(high);
        System.out.println(high + (ans > 0 ? " is larger than " : (ans < 0 ? " is smaller than " : " is equal too ")) + high + "\n");
        ans = low.compareTo(high);
        System.out.println(low + (ans > 0 ? " is larger than " : (ans < 0 ? " is smaller than " : "is equal too ")) + high + "\n");


        //difference test
        System.out.println("\nDifference test");
        System.out.println("\nshould output 13 as ace is 13 ranks higher than two, " +
                "this should be done twice to show it works either way");
        System.out.println(Card.difference(high, low));
        System.out.println(Card.difference(low, high));

        //difference in value test
        System.out.println("\nDifference in value test");
        System.out.println("\nshould output 9 as 11(ace) is 9 higher that 2(two), again " +
                "done twice to show it is the absolute value");
        System.out.println(Card.differenceValue(high, low));
        System.out.println(Card.differenceValue(low, high));

        //Compare ascending test
        CompareAscending compAscend = new CompareAscending();
        System.out.println("\nCompare ascending test");
        System.out.println("\nshould output high is larger, high and high are equal and low is smaller than high for \n" +
                "comparing high to low, high to high and low to high respectively:");;
        System.out.println("high: " + high + " Low: " + low + "\n");
        ans = compAscend.compare(high, low);
        System.out.println(high + (ans > 0 ? " is larger than " : (ans < 0 ? " is smaller than " : "is equal too ")) + low + "\n");
        ans = compAscend.compare(high, high);
        System.out.println(high + (ans > 0 ? " is larger than " : (ans < 0 ? " is smaller than " : " is equal too ")) + high + "\n");
        ans = compAscend.compare(low, high);
        System.out.println(low + (ans > 0 ? " is larger than " : (ans < 0 ? " is smaller than " : "is equal too ")) + high + "\n");

        //Compare Suit test
        CompareSuit compSuit = new CompareSuit();
        System.out.println("\nCompare Suit test");
        System.out.println("\nshould output high is larger, high and high are equal and low is smaller than high for \n" +
                "comparing high to low, high to high and low to high respectively:");;
        System.out.println("high: " + high + " Low: " + low + "\n");
        ans = compSuit.compare(high, low);
        System.out.println(high + (ans > 0 ? " is larger than " : (ans < 0 ? " is smaller than " : "is equal too ")) + low + "\n");
        ans = compSuit.compare(high, high);
        System.out.println(high + (ans > 0 ? " is larger than " : (ans < 0 ? " is smaller than " : " is equal too ")) + high + "\n");
        ans = compSuit.compare(low, high);
        System.out.println(low + (ans > 0 ? " is larger than " : (ans < 0 ? " is smaller than " : "is equal too ")) + high + "\n");

        //testing of selectTest method
        Card.selectTest(high);

        //write card test
        System.out.println("write card test");
        boolean check = false;
        try {
            high.writeFile("writeTest");
        } catch(Exception IOException) {
            check = true;
        }
        if (!check) {
            System.out.println("write succeeded");
        }else{
            System.out.println("write failed");
        }

        //read card test
        System.out.println("read card test");
        check = false;
        try {
            Card temp = Card.readFile("writeTest");
            System.out.println(temp);
        } catch(Exception IOException) {
            check = true;
        }
        if (!check) {
            System.out.println("read succeeded");
        }else{
            System.out.println("read failed");
        }

    }
}







