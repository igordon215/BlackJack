import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a deck of playing cards.
 */
public class Deck {
    private List<Card> cards;

    /**
     * Constructor for Deck. Initializes a full deck of 52 cards.
     */
    public Deck() {
        initializeDeck();
    }

    /**
     * Initializes the deck with 52 cards (13 ranks x 4 suits).
     */
    private void initializeDeck() {
        cards = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        shuffle();
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draws a card from the top of the deck.
     * @return The top card of the deck.
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Cannot draw from an empty deck");
        }
        return cards.remove(cards.size() - 1);
    }

    /**
     * Gets the number of cards remaining in the deck.
     * @return The number of cards in the deck.
     */
    public int remainingCards() {
        return cards.size();
    }

    /**
     * Resets the deck to its initial state (52 cards) and shuffles it.
     */
    public void reset() {
        initializeDeck();
    }
}