package question1;

public class Main {

    public static void main(String[] args) throws Hand.PositionNotInRangeOfHand {
        Deck deck = new Deck();
        Hand hand = new Hand();
        //deck.shuffle();
//        hand.add(deck.deal());
//        hand.add(deck.deal());
//        hand.add(deck.deal());
//        hand.add(deck.deal());
//        System.out.println(hand);
//        hand.remove(2);
//        System.out.println(hand);
//        System.out.println(hand.isStraight());
        for (Card i:deck){
            System.out.println(i);
        }



//        ArrayList<Card> arr = new ArrayList<>();
//        Card temp;
//        for (Card.Suit j : Card.Suit.values()) {
//            for (Card.Rank i : Card.Rank.values()) {
//                temp = new Card(i,j);
//                arr.add(temp);
//            }
//        }

//
//        Collections.sort(arr, new Card.CompareSuit());
//        for (Card i:arr) {
//            System.out.println(i);
//        }



//        ArrayList<Card> test = new ArrayList<>();
//        test.add(new Card(Card.Rank.TEN, Card.Suit.DIAMONDS));
//        test.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
//        test.add(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
//        test.add(new Card(Card.Rank.SIX, Card.Suit.DIAMONDS));
//        test.add(new Card(Card.Rank.TWO, Card.Suit.CLUBS));
//        Collections.sort(test, new Card.CompareSuit());
//        for (Card i:test) {
//            System.out.println(i);
//        }

//        Card test = new Card(Card.Rank.KING, Card.Suit.DIAMONDS);
//        System.out.println(test);
//        System.out.println(test.getRank().getPrevious());
    }
}
