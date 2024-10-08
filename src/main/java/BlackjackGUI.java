import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BlackjackGUI extends JFrame {
    private BlackJack game;
    private JPanel mainPanel;
    private JPanel playerPanels;
    private JPanel dealerPanel;
    private JPanel controlPanel;
    private JButton hitButton;
    private JButton standButton;
    private JButton dealButton;
    private JTextField betAmountField;
    private JButton placeBetButton;
    private JLabel currentBetLabel;

    public BlackjackGUI(int numPlayers, int initialMoney) {
        game = new BlackJack(numPlayers, initialMoney);
        initializeUI();
        startNewGame();
    }

    private void initializeUI() {
        setTitle("Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        playerPanels = new JPanel(new GridLayout(1, game.getPlayers().size()));
        dealerPanel = new JPanel();
        controlPanel = new JPanel(new FlowLayout());

        for (Player player : game.getPlayers()) {
            playerPanels.add(createPlayerPanel(player));
        }

        dealerPanel.add(new JLabel("Dealer: "));
        dealerPanel.add(new JLabel("Hidden"));

        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        dealButton = new JButton("Deal");
        betAmountField = new JTextField(10);
        placeBetButton = new JButton("Place Bet");
        currentBetLabel = new JLabel("Current Bet: $0");

        hitButton.addActionListener(e -> handleHit());
        standButton.addActionListener(e -> handleStand());
        dealButton.addActionListener(e -> startNewGame());
        placeBetButton.addActionListener(e -> handlePlaceBet());

        controlPanel.add(betAmountField);
        controlPanel.add(placeBetButton);
        controlPanel.add(currentBetLabel);
        controlPanel.add(hitButton);
        controlPanel.add(standButton);
        controlPanel.add(dealButton);

        mainPanel.add(dealerPanel, BorderLayout.NORTH);
        mainPanel.add(playerPanels, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setButtonsEnabled(false);
    }

    private JPanel createPlayerPanel(Player player) {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createTitledBorder(player.getName()));
        panel.add(new JLabel("Hand: " + player.getHand().toString()));
        panel.add(new JLabel("Value: " + player.getHandValue()));
        panel.add(new JLabel("Money: $" + player.getMoney()));
        return panel;
    }

    private void startNewGame() {
        resetGameState();
        updateUI();
        setButtonsEnabled(false);
        placeBetButton.setEnabled(true);
        betAmountField.setEnabled(true);
        dealButton.setEnabled(false);
    }

    private void resetGameState() {
        for (Player player : game.getPlayers()) {
            player.clearHand();
        }
        game.getDealer().clearHand();
        game.resetDeck(); // Use the new resetDeck() method
        currentBetLabel.setText("Current Bet: $0");
    }

    private void handlePlaceBet() {
        try {
            int betAmount = Integer.parseInt(betAmountField.getText());
            Player player = game.getPlayers().get(0);
            if (betAmount > 0 && betAmount <= player.getMoney()) {
                if (player.placeBet(betAmount, betAmount)) {
                    currentBetLabel.setText("Current Bet: $" + player.getCurrentBet());
                    placeBetButton.setEnabled(false);
                    betAmountField.setEnabled(false);
                    dealInitialCards();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to place bet. Please try again.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid bet amount. Please enter a value between 1 and " + player.getMoney());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid bet amount. Please enter a valid number.");
        }
    }

    private void dealInitialCards() {
        for (Player player : game.getPlayers()) {
            player.addCard(game.getDeck().drawCard());
            player.addCard(game.getDeck().drawCard());
        }
        game.getDealer().addCard(game.getDeck().drawCard());
        game.getDealer().addCard(game.getDeck().drawCard());
        updateUI();
        setButtonsEnabled(true);
    }

    private void handleHit() {
        Player currentPlayer = game.getPlayers().get(0); 
        currentPlayer.addCard(game.getDeck().drawCard());
        if (currentPlayer.getHandValue() > 21) {
            handleStand();
        }
        updateUI();
    }

    private void handleStand() {
        game.dealerTurnForGUI();
        determineWinner();
        setButtonsEnabled(false);
        dealButton.setEnabled(true);
        updateUI();
    }

    private void determineWinner() {
        Player player = game.getPlayers().get(0);
        int playerScore = player.getHandValue();
        int dealerScore = game.getDealer().getHandValue();

        String result;
        if (playerScore > 21) {
            result = "Player busts. Dealer wins.";
            player.loseBet();
        } else if (dealerScore > 21) {
            result = "Dealer busts. Player wins!";
            player.winBet(2);
        } else if (playerScore > dealerScore) {
            result = "Player wins!";
            player.winBet(player.hasBlackjack() ? 2.5 : 2);
        } else if (playerScore < dealerScore) {
            result = "Dealer wins.";
            player.loseBet();
        } else {
            result = "It's a tie!";
            player.pushBet();
        }

        JOptionPane.showMessageDialog(this, result);
        currentBetLabel.setText("Current Bet: $0");
        updateUI();
    }

    private void updateUI() {
        playerPanels.removeAll();
        for (Player player : game.getPlayers()) {
            playerPanels.add(createPlayerPanel(player));
        }

        dealerPanel.removeAll();
        Hand dealerHand = game.getDealer().getHand();
        if (dealerHand.size() > 0) {
            if (hitButton.isEnabled()) {
                dealerPanel.add(new JLabel("Dealer: " + dealerHand.getCards().get(0) + ", [Hidden]"));
            } else {
                dealerPanel.add(new JLabel("Dealer: " + dealerHand.toString() + " (Value: " + game.getDealer().getHandValue() + ")"));
            }
        } else {
            dealerPanel.add(new JLabel("Dealer: "));
        }

        revalidate();
        repaint();
    }

    private void setButtonsEnabled(boolean enabled) {
        hitButton.setEnabled(enabled);
        standButton.setEnabled(enabled);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BlackjackGUI(1, 1000).setVisible(true));
    }
}