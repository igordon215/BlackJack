/**
 * Interface for different betting strategies in the Blackjack game.
 */
public interface BettingStrategy {
    /**
     * Determines the bet amount based on the player's current money and game state.
     * @param availableMoney The amount of money the player has available to bet.
     * @param minBet The minimum bet allowed in the game.
     * @param maxBet The maximum bet allowed in the game.
     * @return The amount to bet.
     */
    int determineBet(int availableMoney, int minBet, int maxBet);
}