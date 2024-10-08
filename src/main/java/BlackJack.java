import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * BlackJack class represents the main game logic for a Blackjack game.
 */
public class BlackJack {
    private List<Player> players;
    private Dealer dealer;
    private Deck deck;
    private Scanner scanner;

    private static final int BLACKJACK_VALUE = 21;
    private static final double BLACKJACK_PAYOUT = 2.5;
    private static final int MIN_BET = 10;
    private static final int MAX_BET = 500;

    /**
     * Constructor for BlackJack game.
     * @param numPlayers The number of players in the game.
     * @param initialMoney The initial amount of money each player starts with.
     */
    public BlackJack(int numPlayers, int initialMoney) {
        this.players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            this.players.add(new Player("Player " + (i + 1), initialMoney, new DefaultBettingStrategy()));
        }
        this.dealer = new Dealer();
        this.deck = new Deck();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts and manages the main game loop.
     */
    public void playGame() {
        displayWelcomeMessage();
        boolean playAgain = true;

        while (playAgain && playersHaveMoney()) {
            displayGameState();
            placeBets();
            dealInitialCards();
            displayInitialHands();

            if (!checkForBlackjacks()) {
                playRound();
                determineWinners();
            }

            playAgain = askToPlayAgain();
        }

        displayFinalMessage();
        scanner.close();
    }

    private void displayWelcomeMessage() {
        System.out.println("Welcome to Blackjack!");
        System.out.println("Try to get as close to 21 as possible without going over.");
        System.out.println("Dealer stands on 17 and draws to 16.");
        System.out.println("Good luck!\n");
    }

    private void displayGameState() {
        System.out.println("\n--- Current Game State ---");
        for (Player player : players) {
            System.out.println(player.getName() + "'s bankroll: $" + player.getMoney());
        }
        System.out.println("Cards remaining in the deck: " + deck.remainingCards());
    }

    private void placeBets() {
        for (Player player : players) {
            if (player.getMoney() > 0) {
                System.out.println("\n" + player.getName() + ", it's your turn to bet.");
                player.placeBet(MIN_BET, MAX_BET);
                System.out.println(player.getName() + " bets $" + player.getCurrentBet());
            }
        }
    }

    private void dealInitialCards() {
        for (Player player : players) {
            player.clearHand();
        }
        dealer.clearHand();

        for (int i = 0; i < 2; i++) {
            for (Player player : players) {
                player.addCard(deck.drawCard());
            }
            dealer.addCard(deck.drawCard());
        }
    }

    private void displayInitialHands() {
        System.out.println("\n--- Initial Hands ---");
        for (Player player : players) {
            System.out.println(player);
        }
        System.out.println(dealer.displayHand(true));
    }

    private boolean checkForBlackjacks() {
        boolean someoneHasBlackjack = false;
        for (Player player : players) {
            if (player.hasBlackjack()) {
                System.out.println(player.getName() + " has Blackjack!");
                someoneHasBlackjack = true;
            }
        }

        if (dealer.hasBlackjack()) {
            System.out.println("Dealer has Blackjack!");
            someoneHasBlackjack = true;
        }

        if (someoneHasBlackjack) {
            System.out.println("\n" + dealer);
            for (Player player : players) {
                if (player.hasBlackjack() && dealer.hasBlackjack()) {
                    System.out.println(player.getName() + " pushes.");
                    player.pushBet();
                } else if (player.hasBlackjack()) {
                    System.out.println(player.getName() + " wins with Blackjack!");
                    player.winBet(BLACKJACK_PAYOUT);
                } else {
                    System.out.println(player.getName() + " loses to Dealer's Blackjack.");
                    player.loseBet();
                }
            }
        }

        return someoneHasBlackjack;
    }

    private void playRound() {
        for (Player player : players) {
            if (player.getCurrentBet() > 0) {
                playerTurn(player);
            }
        }

        if (playersInGame()) {
            dealerTurn();
        }
    }

    private void playerTurn(Player player) {
        System.out.println("\n" + player.getName() + "'s turn:");
        while (true) {
            System.out.print("Do you want to (h)it, (s)tand" +
                    (player.getHand().size() == 2 ? ", or (d)ouble down? " : "? "));
            String choice = scanner.nextLine().toLowerCase();
            if (playerAction(player, choice)) {
                break;
            }
            System.out.println("\n" + player);
        }
    }

    private boolean playerAction(Player player, String choice) {
        switch (choice) {
            case "h":
                player.addCard(deck.drawCard());
                if (player.getHandValue() > BLACKJACK_VALUE) {
                    System.out.println("\n" + player);
                    System.out.println(player.getName() + " busts!");
                    return true;
                }
                break;
            case "s":
                return true;
            case "d":
                if (player.getHand().size() == 2 && player.getMoney() >= player.getCurrentBet()) {
                    player.placeBet(MIN_BET, player.getCurrentBet());
                    player.addCard(deck.drawCard());
                    System.out.println("\n" + player);
                    return true;
                } else {
                    System.out.println("You can't double down.");
                }
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    private void dealerTurn() {
        System.out.println("\nDealer's turn:");
        System.out.println(dealer);
        while (dealer.shouldHit()) {
            dealer.addCard(deck.drawCard());
            System.out.println(dealer);
        }
    }

    private void determineWinners() {
        int dealerScore = dealer.getHandValue();

        System.out.println("\n--- Final Hands ---");
        for (Player player : players) {
            System.out.println(player);
        }
        System.out.println(dealer);

        for (Player player : players) {
            int playerScore = player.getHandValue();

            if (playerScore > BLACKJACK_VALUE) {
                System.out.println(player.getName() + " busts. Dealer wins.");
                player.loseBet();
            } else if (dealerScore > BLACKJACK_VALUE) {
                System.out.println("Dealer busts. " + player.getName() + " wins!");
                player.winBet(2);
            } else if (playerScore > dealerScore) {
                System.out.println(player.getName() + " wins!");
                player.winBet(2);
            } else if (playerScore < dealerScore) {
                System.out.println("Dealer wins against " + player.getName() + ".");
                player.loseBet();
            } else {
                System.out.println(player.getName() + " pushes.");
                player.pushBet();
            }
        }
    }

    private boolean askToPlayAgain() {
        while (true) {
            System.out.print("Do you want to play another round? (y/n): ");
            String choice = scanner.nextLine().toLowerCase();
            if (choice.equals("y")) {
                return true;
            } else if (choice.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
            }
        }
    }

    private void displayFinalMessage() {
        System.out.println("\nThank you for playing Blackjack!");
        for (Player player : players) {
            System.out.println(player.getName() + "'s final bankroll: $" + player.getMoney());
        }
    }

    private boolean playersHaveMoney() {
        for (Player player : players) {
            if (player.getMoney() > 0) {
                return true;
            }
        }
        return false;
    }

    private boolean playersInGame() {
        for (Player player : players) {
            if (player.getCurrentBet() > 0 && player.getHandValue() <= BLACKJACK_VALUE) {
                return true;
            }
        }
        return false;
    }

    // Getter methods for testing purposes
    public List<Player> getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public static void main(String[] args) {
        BlackJack game = new BlackJack(2, 1000);
        game.playGame();
    }

    // New methods for GUI interaction
    public void dealInitialCardsForGUI() {
        dealInitialCards();
    }

    public void hitPlayer(Player player) {
        player.addCard(deck.drawCard());
    }

    public void dealerTurnForGUI() {
        dealerTurn();
    }

    public Deck getDeck() {
        return deck;
    }

    public static int getBlackjackValue() {
        return BLACKJACK_VALUE;
    }

    public static double getBlackjackPayout() {
        return BLACKJACK_PAYOUT;
    }

    public static int getMinBet() {
        return MIN_BET;
    }

    public static int getMaxBet() {
        return MAX_BET;
    }

    // New method to reset the deck
    public void resetDeck() {
        this.deck = new Deck();
    }
}