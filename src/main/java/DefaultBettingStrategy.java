/**
 * A default betting strategy that always bets a fixed percentage of the available money.
 */
public class DefaultBettingStrategy implements BettingStrategy {
    private static final double BET_PERCENTAGE = 0.1; // 10% of available money

    @Override
    public int determineBet(int availableMoney, int minBet, int maxBet) {
        int bet = (int) (availableMoney * BET_PERCENTAGE);
        return Math.max(minBet, Math.min(bet, maxBet));
    }
}