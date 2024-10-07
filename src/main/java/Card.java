/**
 * Represents a playing card in a deck.
 */
public class Card {
    private final Rank rank;
    private final Suit suit;

    /**
     * Constructor for Card.
     * @param rank The rank of the card.
     * @param suit The suit of the card.
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Get the rank of the card.
     * @return The rank of the card.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Get the suit of the card.
     * @return The suit of the card.
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Get the value of the card.
     * @return The numerical value of the card.
     */
    public int getValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    /**
     * Enum representing the rank of a card.
     */
    public enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(10), QUEEN(10), KING(10), ACE(11);

        private final int value;

        Rank(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Enum representing the suit of a card.
     */
    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES
    }
}