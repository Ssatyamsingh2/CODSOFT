import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class NumberGame extends JFrame {
    private JTextField txtGuess;
    private JLabel lblMessage, lblTimer, lblProximity, lblInstruction;
    private JComboBox<String> cboDifficulty;
    private JButton btnHint, btnPlayAgain;
    private int targetNumber, timeLimit, maxNumber, hintsUsed;
    private Timer gameTimer;
    private boolean gameActive;
    private static final int MAX_HINTS = 3;

    public NumberGame() {
        setTitle("Number Guessing Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(550, 350);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(85, 21, 77));
        try { setIconImage(new ImageIcon("D:\\Java project\\20250414152835.png").getImage()); } catch (Exception e) {}
        initializeComponents();
        startNewGame();
    }

    private void initializeComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        lblInstruction = new JLabel("Guess the correct number", SwingConstants.CENTER);
        lblInstruction.setFont(new Font("Arial", Font.BOLD, 16));
        lblInstruction.setForeground(new Color(245, 255, 225));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3; gbc.insets = new Insets(10,10,10,10);
        add(lblInstruction, gbc);

        JLabel lblDifficulty = new JLabel("Difficulty:", SwingConstants.CENTER);
        lblDifficulty.setFont(new Font("Arial", Font.BOLD, 12));
        lblDifficulty.setForeground(new Color(245, 245, 220));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.insets = new Insets(5,10,5,5);
        add(lblDifficulty, gbc);

        String[] difficulties = {"Easy (1-50, 45s)", "Medium (1-100, 30s)", "Hard (1-200, 20s)"};
        cboDifficulty = new JComboBox<>(difficulties);
        cboDifficulty.setSelectedIndex(1);
        cboDifficulty.setFont(new Font("Arial", Font.PLAIN, 11));
        cboDifficulty.addActionListener(e -> startNewGame());
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; gbc.insets = new Insets(5,5,5,10);
        add(cboDifficulty, gbc);

        lblTimer = new JLabel("Time left: 30 seconds", SwingConstants.CENTER);
        lblTimer.setFont(new Font("Arial", Font.BOLD, 14));
        lblTimer.setForeground(new Color(255, 182, 193));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; gbc.insets = new Insets(5,10,5,10);
        add(lblTimer, gbc);

        txtGuess = new JTextField(15);
        txtGuess.setHorizontalAlignment(JTextField.CENTER);
        txtGuess.setFont(new Font("Arial", Font.PLAIN, 16));
        txtGuess.setBackground(new Color(255, 248, 220));
        txtGuess.setForeground(new Color(139, 69, 19));
        txtGuess.setPreferredSize(new Dimension(200, 35));
        txtGuess.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) checkGuess();
            }
        });
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3; gbc.insets = new Insets(10,10,10,10);
        add(txtGuess, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 20, 147));

        JButton btnCheck = new JButton("Check Guess");
        btnCheck.setFont(new Font("Arial", Font.BOLD, 12));
        btnCheck.setBackground(new Color(205, 92, 92));
        btnCheck.setForeground(Color.WHITE);
        btnCheck.setPreferredSize(new Dimension(120, 35));
        btnCheck.addActionListener(e -> checkGuess());
        buttonPanel.add(btnCheck);

        btnHint = new JButton("Hint (" + (MAX_HINTS - hintsUsed) + "/" + MAX_HINTS + ")");
        btnHint.setFont(new Font("Arial", Font.BOLD, 12));
        btnHint.setBackground(new Color(255, 140, 0));
        btnHint.setForeground(Color.WHITE);
        btnHint.setPreferredSize(new Dimension(120, 35));
        btnHint.addActionListener(e -> showHint());
        buttonPanel.add(btnHint);

        btnPlayAgain = new JButton("Play Again");
        btnPlayAgain.setFont(new Font("Arial", Font.BOLD, 12));
        btnPlayAgain.setBackground(new Color(34, 139, 34));
        btnPlayAgain.setForeground(Color.WHITE);
        btnPlayAgain.setPreferredSize(new Dimension(120, 35));
        btnPlayAgain.addActionListener(e -> startNewGame());
        buttonPanel.add(btnPlayAgain);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; gbc.insets = new Insets(10,10,10,10);
        add(buttonPanel, gbc);

        lblMessage = new JLabel("", SwingConstants.CENTER);
        lblMessage.setFont(new Font("Arial", Font.BOLD, 14));
        lblMessage.setForeground(new Color(245, 245, 220));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3; gbc.insets = new Insets(5,10,5,10);
        add(lblMessage, gbc);

        lblProximity = new JLabel("", SwingConstants.CENTER);
        lblProximity.setFont(new Font("Arial", Font.ITALIC, 12));
        lblProximity.setForeground(new Color(255, 215, 0));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3; gbc.insets = new Insets(5,10,10,10);
        add(lblProximity, gbc);
    }

    public void startNewGame() {
        if (gameTimer != null) gameTimer.stop();
        switch (cboDifficulty.getSelectedIndex()) {
            case 0: maxNumber = 50; timeLimit = 45; break;
            case 1: maxNumber = 100; timeLimit = 30; break;
            case 2: maxNumber = 200; timeLimit = 20; break;
        }
        targetNumber = (int) (Math.random() * maxNumber) + 1;
        hintsUsed = 0; gameActive = true;
        lblInstruction.setText("Enter a number between 1 and " + maxNumber);
        lblMessage.setText(""); lblProximity.setText("");
        btnHint.setText("Hint (" + (MAX_HINTS - hintsUsed) + "/" + MAX_HINTS + ")");
        btnHint.setEnabled(true);
        txtGuess.setText(""); txtGuess.setEnabled(true); txtGuess.requestFocus();
        startTimer();
    }

    public void checkGuess() {
        if (!gameActive) return;
        try {
            int guess = Integer.parseInt(txtGuess.getText());
            if (guess < 1 || guess > maxNumber) { lblMessage.setText("Enter a number between 1 and " + maxNumber); return; }
            if (guess == targetNumber) {
                lblMessage.setText("Congratulations! You guessed the number!");
                lblProximity.setText(" You won with " + (MAX_HINTS - hintsUsed) + " hints remaining!");
                blinkColor();
                endGame();
            } else {
                lblMessage.setText(guess < targetNumber ? "Too low!" : "Too high!");
                lblProximity.setText(proximityMessage(guess));
            }
            txtGuess.setText(""); txtGuess.requestFocus();
        } catch (NumberFormatException e) {
            lblMessage.setText("Please enter a valid number."); lblProximity.setText("");
        }
    }

    public void showHint() {
        if (!gameActive || hintsUsed >= MAX_HINTS) return;
        hintsUsed++;
        btnHint.setText("Hint (" + (MAX_HINTS - hintsUsed) + "/" + MAX_HINTS + ")");
        String hint = "";
        switch (hintsUsed) {
            case 1: hint = "The number is " + (targetNumber % 2 == 0 ? "even" : "odd"); break;
            case 2:
                int range = maxNumber / 4;
                hint = targetNumber <= range ? "The number is in the lower quarter (1-" + range + ")" :
                       targetNumber <= range * 2 ? "The number is in the second quarter (" + (range + 1) + "-" + (range * 2) + ")" :
                       targetNumber <= range * 3 ? "The number is in the third quarter (" + (range * 2 + 1) + "-" + (range * 3) + ")" :
                       "The number is in the upper quarter (" + (range * 3 + 1) + "-" + maxNumber + ")";
                break;
            case 3:
                int sum = 0, temp = targetNumber;
                while (temp > 0) { sum += temp % 10; temp /= 10; }
                hint = "Sum of digits: " + sum; break;
        }
        JOptionPane.showMessageDialog(this, hint, "Hint " + hintsUsed, JOptionPane.INFORMATION_MESSAGE);
        if (hintsUsed >= MAX_HINTS) btnHint.setEnabled(false);
    }

    public void endGame() {
        gameActive = false;
        if (gameTimer != null) gameTimer.stop();
        txtGuess.setEnabled(false);
        btnHint.setEnabled(false);
    }

    public void startTimer() {
        gameTimer = new Timer(1000, new ActionListener() {
            int timeRemaining = timeLimit;
            public void actionPerformed(ActionEvent e) {
                if (timeRemaining > 0 && gameActive) {
                    timeRemaining--;
                    lblTimer.setText("Time left: " + timeRemaining + " seconds");
                } else {
                    ((Timer)e.getSource()).stop();
                    lblTimer.setText("Time's up!");
                    lblMessage.setText("The number was " + targetNumber);
                    lblProximity.setText("Better luck next time!");
                    endGame();
                }
            }
        });
        gameTimer.start();
    }

    public String proximityMessage(int guess) {
        int diff = Math.abs(targetNumber - guess);
        return diff == 0 ? "Congratulations! You guessed the number!" :
               diff <= 3 ? "Very close!" :
               diff <= 10 ? "Close!" : "Not close.";
    }

    public void blinkColor() {
        Timer blinkTimer = new Timer(500, new ActionListener() {
            boolean isGold = false;
            public void actionPerformed(ActionEvent e) {
                lblMessage.setForeground(isGold ? new Color(245, 245, 220) : new Color(255, 215, 0));
                isGold = !isGold;
            }
        });
        blinkTimer.setRepeats(true);
        blinkTimer.setInitialDelay(0);
        blinkTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NumberGame().setVisible(true));
    }
}
