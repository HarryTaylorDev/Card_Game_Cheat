/*****************************************************************************

 File        : Deck.java

 Date        : 04/01/2020

 Description : A java class to describe a normal deck of 52 cards

 Author      : 100239986

 ******************************************************************************/
package question1;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class Deck implements Iterable<Card>, Serializable {
    private static final long serialVersionUID = 100239986-10;
    private Card[] deck;
    private static final Random rand = new Random();

    int top;

    //New deck initialized with all cards in a standard deck
    public Deck() {
        deck = new Card[52];
        newDeck();
    }

    //Returns the number of cards left in the deck
    public int size() {
        return top+1;
    }

    //reinitialises the deck with all cards in a standard deck
    public void newDeck() {
        int temp = 0;
        for (Card.Suit j : Card.Suit.values()) {
            for (Card.Rank i : Card.Rank.values()) {
                deck[temp] = new Card(i, j);
                temp++;
            }
        }
        top = 51;
    }

    //returns the top card in the deck
    //returns null if there are no more cards left in the deck
    public Card deal() {
        Card temp = deck[top];
        if (top > 0) {
            top--;
        } else {
            return null;
        }
        return temp;
    }

    //Returns an iterator that goes through in order from top to bottom of the deck
    @Override
    public Iterator<Card> iterator() {return new deckIterator();}

    public class deckIterator implements Iterator<Card> {
        //current card initialized to the top of the deck upon creation
        int current = top;

        //returns true if there are more cards left in the deck, false if not
        @Override
        public boolean hasNext() {
            if (current < 0) {
                return false;
            }
            return true;
        }

        //returns the current card and increments the current card for next time
        @Override
        public Card next() {
            if (this.hasNext()) {
                return deck[current--];
            }
            throw new NoSuchElementException();
        }
    }

    //randomly arranges the deck of cards
    public void shuffle() {
        Card tempCard;
        int randInt;
        //Loops through the deck swapping a random card into each position ensuring the deck is fully random
        for (int i = 0; i < top + 1; i++) {
            randInt = new Random().nextInt(top+1);
            tempCard = deck[i];
            deck[i] = deck[randInt];
            deck[randInt] = tempCard;
        }
    }


    //Returns an iterator that goes through in order of odd positions then even positions from top to bottom of the deck
    public Iterator<Card> oddEvenIterator() {return new oddEvenIterator();}

    public class oddEvenIterator implements Iterator<Card> {
        int current;

        //initialises the current card index
        public oddEvenIterator() {
            if (top % 2 == 0) {
                current = top - 1;
            } else {
                current = top;
            }
        }

        //returns true if there are more cards left in  the deck
        @Override
        public boolean hasNext() {
            if(current<0){
                return false;
            }
            return true;
        }

        //returns the current card and increments the current card for next time in the order of odds first then evens
        @Override
        public Card next() {
            int result=current;
            if (this.hasNext()){
                //checks if at the end of odd positions
                if (current == 1){
                    //sets the first even position
                    if (top%2==0){
                        current = top;
                    }else{
                        current = top-1;
                    }
                } else {
                    current -= 2;
                }
                return deck[result];
            }
            return null;
        }
    }

    //Function to write a deck to file
    public void writeFile(String filename) throws IOException {
        filename = filename + ".ser";
        FileOutputStream file = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(this);
        out.close();
    }

    //function to read a deck from file
    public static Deck readFile(String filename) throws IOException, ClassNotFoundException {
        filename = filename + ".ser";
        FileInputStream file = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(file);
        return (Deck) in.readObject();

    }


    public static void main(String[] args) {

        //New deck test
        System.out.println("\nNew deck test and iterator test");
        System.out.println("Should output all cards in a standard deck");
        Deck testDeck = new Deck();
        for (Card i :testDeck) {
            System.out.println(i);
        }

        //Deck deal test
        System.out.println("\nDeck deal test");
        System.out.println("should output ace of spades");
        System.out.println(testDeck.deal());

        //newDeck test
        System.out.println("\nnew Deck test");
        System.out.println("deck should include ace of spaces again");
        testDeck.newDeck();
        for (Card i :testDeck) {
            System.out.println(i);
        }

        //Deck size test
        System.out.println("\nDeck size test");
        System.out.println("Should output 52");
        System.out.println(testDeck.size());
        System.out.println("Should output 50 after dealing 2 cards");
        System.out.println(testDeck.deal() + " dealt");
        System.out.println(testDeck.deal() + " dealt");
        System.out.println(testDeck.size());

        //shuffle test
        System.out.println("\nshuffle test");
        System.out.println("Should output the deck in a random order");
        testDeck.newDeck();
        testDeck.shuffle();
        for (Card i :testDeck) {
            System.out.println(i);
        }

        System.out.println("\nshuffle test with half deck");
        System.out.println("Should output the half deck in a random order");
        testDeck.newDeck();
        for (int i = 0; i<26;i++){
            testDeck.deal();
        }
        System.out.println(testDeck.size());
        testDeck.shuffle();
        int count=0;
        for (Card i :testDeck) {
            count++;
            System.out.println(count +" "+ i);
        }

        //Odd even iterator test
        System.out.println("\nOdd even iterator test");
        System.out.println("Should output the deck in odd positions and then even positions");
        testDeck.newDeck();
        Iterator<Card> it = testDeck.oddEvenIterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }

        //Write deck to file test
        System.out.println("\nWrite deck to file test");
        boolean check = false;
        try {
            testDeck.writeFile("writeDeckTest");
        } catch(Exception IOException) {
            check = true;
        }
        if (!check) {
            System.out.println("write succeeded");
        }else{
            System.out.println("write failed");
        }

        //read deck file test
        System.out.println("\nread deck file test");
        check = false;
        try {
            Deck temp = Deck.readFile("writeDeckTest");
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

    }

}
