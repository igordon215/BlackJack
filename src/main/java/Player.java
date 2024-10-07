/**
 * Represents a player in the Blackjack game.
 */
public class Player {
    private String name;
    private int money;
    private Hand hand;
    private int currentBet;
    private BettingStrategy bettingStrategy;

    /**
     * Constructor for Player.
     * @param name The name of the player.
     * @param initialMoney The initial amount of money the player has.
     * @param bettingStrategy The betting strategy to use.
     */
    public Player(String name, int initialMoney, BettingStrategy bettingStrategy) {
        this.name = name;
        this.money = initialMoney;
        this.hand = new Hand();
        this.currentBet = 0;
        this.bettingStrategy = bettingStrategy;
    }

    /**
     * Places a bet using the player's betting strategy.
     * @param minBet The minimum bet allowed in the game.
     * @param maxBet The maximum bet allowed in the game.
     * @return true if the bet was successfully placed, false otherwise.
     */
    public boolean placeBet(int minBet, int maxBet) {
        int betAmount = bettingStrategy.determineBet(money, minBet, maxBet);
        if (betAmount <= 0 || betAmount > money) {
            return false;
        }
        currentBet = betAmount;
        money -= betAmount;
        return true;
    }

    /**
     * Adds a card to the player's hand.
     * @param card The card to add.
     */
    public void addCard(Card card) {
        hand.addCard(card);
    }

    /**
     * Clears the player's hand and resets the current bet.
     */
    public void clearHand() {
        hand.clear();
        currentBet = 0;
    }

    /**
     * Gets the value of the player's hand.
     * @return The value of the hand.
     */
    public int getHandValue() {
        return hand.getValue();
    }

    /**
     * Checks if the player's hand is a blackjack.
     * @return true if the hand is a blackjack, false otherwise.
     */
    public boolean hasBlackjack() {
        return hand.isBlackjack();
    }

    /**
     * Checks if the player has busted (hand value over 21).
     * @return true if the player has busted, false otherwise.
     */
    public boolean hasBusted() {
        return getHandValue() > 21;
    }

    /**
     * Checks if the player has a blackjack or has busted.
     * @return true if the player has a blackjack or has busted, false otherwise.
     */
    public boolean hasBlackjackOrBusted() {
        return hasBlackjack() || hasBusted();
    }

    /**
     * Wins the bet.
     * @param multiplier The multiplier for the winnings (e.g., 2 for regular win, 2.5 for blackjack).
     */
    public void winBet(double multiplier) {
        money += currentBet * multiplier;
        currentBet = 0;
    }

    /**
     * Loses the bet.
     */
    public void loseBet() {
        currentBet = 0;
    }

    /**
     * Pushes (ties) the bet.
     */
    public void pushBet() {
        money += currentBet;
        currentBet = 0;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public Hand getHand() {
        return hand;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setBettingStrategy(BettingStrategy bettingStrategy) {
        this.bettingStrategy = bettingStrategy;
    }

    @Override
    public String toString() {
        return name + "'s hand: " + hand + " (Value: " + getHandValue() + ")";
    }
}