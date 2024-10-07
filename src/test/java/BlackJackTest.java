import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlackJackTest {
    private BlackJack game;
    private Player player;

    @BeforeEach
    void setUp() {
        game = new BlackJack(1, 1000);
        player = game.getPlayers().get(0);
    }

    @Test
    void testInitialPlayerMoney() {
        assertEquals(1000, player.getMoney());
    }

    @Test
    void testPlayerPlaceBet() {
        player.placeBet(10, 100);
        assertEquals(900, player.getMoney());
        assertEquals(100, player.getCurrentBet());
    }

    @Test
    void testPlayerAddCard() {
        Card card = new Card(Card.Rank.ACE, Card.Suit.SPADES);
        player.addCard(card);
        assertEquals(1, player.getHand().size());
        assertEquals(11, player.getHandValue());
    }

    @Test
    void testDealerAddCard() {
        Card card = new Card(Card.Rank.KING, Card.Suit.HEARTS);
        game.getDealer().addCard(card);
        assertEquals(1, game.getDealer().getHand().size());
        assertEquals(10, game.getDealer().getHandValue());
    }

    @Test
    void testPlayerBlackjack() {
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        assertTrue(player.hasBlackjack());
    }

    @Test
    void testDealerBlackjack() {
        game.getDealer().addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        game.getDealer().addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        assertTrue(game.getDealer().hasBlackjack());
    }

    @Test
    void testPlayerBust() {
        player.addCard(new Card(Card.Rank.KING, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        assertTrue(player.getHandValue() > 21);
    }

    @Test
    void testDealerShouldHit() {
        game.getDealer().addCard(new Card(Card.Rank.SIX, Card.Suit.SPADES));
        game.getDealer().addCard(new Card(Card.Rank.NINE, Card.Suit.HEARTS));
        assertTrue(game.getDealer().shouldHit());
    }

    @Test
    void testDealerShouldStand() {
        game.getDealer().addCard(new Card(Card.Rank.KING, Card.Suit.SPADES));
        game.getDealer().addCard(new Card(Card.Rank.SEVEN, Card.Suit.HEARTS));
        assertFalse(game.getDealer().shouldHit());
    }

    @Test
    void testPlayerHasBusted() {
        player.addCard(new Card(Card.Rank.KING, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        assertTrue(player.hasBusted());
    }

    @Test
    void testPlayerHasNotBusted() {
        player.addCard(new Card(Card.Rank.KING, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        assertFalse(player.hasBusted());
    }

    @Test
    void testPlayerHasBlackjackOrBustedWithBlackjack() {
        player.addCard(new Card(Card.Rank.ACE, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.KING, Card.Suit.HEARTS));
        assertTrue(player.hasBlackjackOrBusted());
    }

    @Test
    void testPlayerHasBlackjackOrBustedWithBust() {
        player.addCard(new Card(Card.Rank.KING, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.QUEEN, Card.Suit.HEARTS));
        player.addCard(new Card(Card.Rank.JACK, Card.Suit.DIAMONDS));
        assertTrue(player.hasBlackjackOrBusted());
    }

    @Test
    void testPlayerHasNotBlackjackOrBusted() {
        player.addCard(new Card(Card.Rank.KING, Card.Suit.SPADES));
        player.addCard(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        assertFalse(player.hasBlackjackOrBusted());
    }

    @Test
    void testMultiplePlayers() {
        BlackJack multiPlayerGame = new BlackJack(3, 1000);
        assertEquals(3, multiPlayerGame.getPlayers().size());
        for (Player p : multiPlayerGame.getPlayers()) {
            assertEquals(1000, p.getMoney());
        }
    }

    @Test
    void testDefaultBettingStrategy() {
        player.placeBet(10, 500);
        assertEquals(100, player.getCurrentBet()); // 10% of 1000
    }
}