import java.util.ArrayList;
import java.util.List;

/**
 * Represents a hand of cards in the game.
 */
public class Hand {
    private List<Card> cards;

    /**
     * Constructor for Hand.
     */
    public Hand() {
        this.cards = new ArrayList<>();
    }

    /**
     * Adds a card to the hand.
     * @param card The card to add.
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Clears all cards from the hand.
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Gets the value of the hand.
     * @return The total value of the hand.
     */
    public int getValue() {
        int value = 0;
        int aceCount = 0;
        for (Card card : cards) {
            if (card.getRank() == Card.Rank.ACE) {
                aceCount++;
            }
            value += card.getValue();
        }
        // Adjust for Aces
        while (value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;
        }
        return value;
    }

    /**
     * Checks if the hand is a blackjack (two cards totaling 21).
     * @return true if the hand is a blackjack, false otherwise.
     */
    public boolean isBlackjack() {
        return cards.size() == 2 && getValue() == 21;
    }

    /**
     * Checks if the hand is busted (value over 21).
     * @return true if the hand is busted, false otherwise.
     */
    public boolean isBusted() {
        return getValue() > 21;
    }

    /**
     * Gets the list of cards in the hand.
     * @return The list of cards.
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Gets the size of the hand.
     * @return The number of cards in the hand.
     */
    public int size() {
        return cards.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card).append(", ");
        }
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }
}