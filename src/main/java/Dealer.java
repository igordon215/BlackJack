/**
 * Represents the dealer in the Blackjack game.
 */
public class Dealer {
    private Hand hand;
    private static final int DEALER_STAND_VALUE = 17;

    /**
     * Constructor for Dealer.
     */
    public Dealer() {
        this.hand = new Hand();
    }

    /**
     * Adds a card to the dealer's hand.
     * @param card The card to add.
     */
    public void addCard(Card card) {
        hand.addCard(card);
    }

    /**
     * Clears the dealer's hand.
     */
    public void clearHand() {
        hand.clear();
    }

    /**
     * Gets the value of the dealer's hand.
     * @return The value of the hand.
     */
    public int getHandValue() {
        return hand.getValue();
    }

    /**
     * Checks if the dealer's hand is a blackjack.
     * @return true if the hand is a blackjack, false otherwise.
     */
    public boolean hasBlackjack() {
        return hand.isBlackjack();
    }

    /**
     * Determines if the dealer should hit based on the current hand value.
     * @return true if the dealer should hit, false otherwise.
     */
    public boolean shouldHit() {
        return getHandValue() < DEALER_STAND_VALUE;
    }

    /**
     * Gets the dealer's hand.
     * @return The dealer's hand.
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Displays the dealer's hand, optionally hiding the first card.
     * @param hideFirstCard If true, the first card will be hidden.
     * @return A string representation of the dealer's hand.
     */
    public String displayHand(boolean hideFirstCard) {
        StringBuilder sb = new StringBuilder("Dealer's hand: ");
        if (hideFirstCard) {
            sb.append("[Hidden], ").append(hand.getCards().get(1));
        } else {
            sb.append(hand.toString());
        }
        if (!hideFirstCard) {
            sb.append(" (Value: ").append(getHandValue()).append(")");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Dealer's hand: " + hand + " (Value: " + getHandValue() + ")";
    }
}