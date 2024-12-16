package swump.gui.multi;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import swump.core.SudokuLogic;
import swump.gui.SudokuMainUI;
import swump.gui.misc.FontGenerator;

public class MultiPlayerInit extends JDialog {
    // region fields

    private int currentCard = 1;
    private JPanel cardPanel;
    private CardLayout cl;
    private JButton previousBtn;
    private JButton nextBtn;

    // endregion

    // region constructors

    public MultiPlayerInit() {
        super(SudokuMainUI.main, "Multiplayer game", true);

        this.setSize(300, 300);
        cardPanel = new JPanel();

        cl = new CardLayout();
        cardPanel.setLayout(cl);

        // Panel Declaration
        JPanel jp1 = new JPanel();
        final JPanel jp2 = new JPanel();
        // jp1
        jp1.setLayout(new BorderLayout());
        JPanel gameSetting = new JPanel();
        gameSetting.setLayout(new GridLayout(5, 2, 10, 10));
        gameSetting.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Player
        String choice[] = { "2", "3", "4" };
        JLabel lblNumPlayer = new JLabel("Number of players:");
        final JComboBox<String> cboChoice = new JComboBox<>(choice);

        // Select turn time
        String turn[] = { "30", "45", "60", "90" };
        JLabel lblNumTurn = new JLabel("Turn duration:");
        final JComboBox<String> cboTurn = new JComboBox<>(turn);
        cboTurn.setSelectedIndex(0);

        // Select try
        String tries[] = { "5", "6", "7", "8", "9", "10" };
        JLabel lblTry = new JLabel("Tries per turn:");
        final JComboBox<String> cboTry = new JComboBox<>(tries);
        cboTry.setSelectedIndex(0);

        // Select difficulty
        String difficulty[] = { "Blank", "Easy", "Normal", "Hard" };
        JLabel lblDifficulty = new JLabel("Difficulty level:");
        final JComboBox<String> cboDiff = new JComboBox<>(difficulty);
        cboDiff.setSelectedIndex(0);

        // Adding control to game panel
        gameSetting.add(lblNumPlayer);
        gameSetting.add(cboChoice);
        gameSetting.add(lblDifficulty);
        gameSetting.add(cboDiff);
        gameSetting.add(lblNumTurn);
        gameSetting.add(cboTurn);
        gameSetting.add(lblTry);
        gameSetting.add(cboTry);

        JLabel lblGame = new JLabel("Please select game settings");
        lblGame.setHorizontalAlignment(JLabel.CENTER);
        lblGame.setFont(FontGenerator.generateStdFont(Font.PLAIN, 20));
        jp1.add(lblGame, BorderLayout.NORTH);
        jp1.add(gameSetting, BorderLayout.CENTER);

        // Storage for player detail UI
        final List<InputPlayer> players = new ArrayList<>();

        // jp2
        jp2.setLayout(new BorderLayout());
        final JPanel playerDetail = new JPanel();
        playerDetail.setLayout(new GridLayout(4, 1));
        for (int i = 0; i < 2; i++) {
            InputPlayer player = new InputPlayer(i + 1);
            playerDetail.add(player);
            players.add(player);
        }

        // Create player detail UI
        cboChoice.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                players.clear();
                int selection = Integer.parseInt((String) cboChoice.getSelectedItem());
                playerDetail.removeAll();
                for (int i = 0; i < selection; i++) {
                    InputPlayer player = new InputPlayer(i + 1);
                    playerDetail.add(player);
                    players.add(player);
                }
            }
        });

        // jp2
        JLabel lblPlayer = new JLabel("Enter players' details");
        lblPlayer.setHorizontalAlignment(JLabel.CENTER);
        lblPlayer.setFont(FontGenerator.generateStdFont(Font.PLAIN, 20));
        jp2.add(lblPlayer, BorderLayout.NORTH);
        jp2.add(playerDetail, BorderLayout.CENTER);

        cardPanel.add(jp1, "1");
        cardPanel.add(jp2, "2");

        JPanel buttonPanel = new JPanel();

        nextBtn = new JButton("Next");
        previousBtn = new JButton("Previous");
        previousBtn.setVisible(false);
        buttonPanel.add(previousBtn);
        buttonPanel.add(nextBtn);

        nextBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (currentCard == 1) {
                    nextBtn.setText("Start Game!");
                    previousBtn.setVisible(true);
                    currentCard += 1;
                    cl.show(cardPanel, "" + (currentCard));
                } else if (currentCard == 2) {
                    boolean check = false;
                    currentCard += 1;
                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getName() != null)
                            check = true;
                        else {
                            check = false;
                            i = players.size();
                        }

                    }
                    if (check) {
                        if (checkSimilarColor()) {
                            if (checkName())
                                currentCard = 0;
                            else {
                                currentCard -= 1;
                                JOptionPane.showMessageDialog(SudokuMainUI.main, "Ensure the names are unique.");
                            }
                        } else {
                            currentCard -= 1;
                            JOptionPane.showMessageDialog(SudokuMainUI.main, "Ensure the colours are unqiue.");
                        }
                    } else {
                        currentCard -= 1;
                        JOptionPane.showMessageDialog(SudokuMainUI.main, "Ensure that all names are entered.");
                    }
                }
                if (currentCard == 0) {
                    setVisible(false);
                    int diff = 0;
                    String diffLevel = cboDiff.getSelectedItem().toString();
                    if (diffLevel.compareTo("Easy") == 0)
                        diff = 0;
                    else if (diffLevel.compareTo("Normal") == 0)
                        diff = 1;
                    else if (diffLevel.compareTo("Hard") == 0)
                        diff = 2;
                    else if (diffLevel.compareTo("Blank") == 0)
                        diff = 3;

                    for (int i = 0; i < players.size(); i++)
                        SudokuLogic.getInstance().initializePlayer(players.get(i).getName(), players.get(i).getColor());
                    SudokuLogic.getInstance().startNewGameMP(diff,
                            Integer.parseInt(cboTurn.getSelectedItem().toString()),
                            Integer.parseInt(cboTry.getSelectedItem().toString()));
                    SudokuMainUI.main.activateMultiPlayerPanel();
                }
            }

            private boolean checkSimilarColor() {
                String tmp, cur;
                for (int i = 0; i < players.size(); i++) {
                    tmp = players.get(i).getColor().toString();
                    for (int j = 0; j < players.size(); j++) {
                        cur = players.get(j).getColor().toString();
                        if (i != j && tmp.equalsIgnoreCase(cur))
                            return false;
                    }
                }
                return true;
            }

            private boolean checkName() {
                String tmp, cur;
                for (int i = 0; i < players.size(); i++) {
                    tmp = players.get(i).getName();
                    for (int j = 0; j < players.size(); j++) {
                        cur = players.get(j).getName();
                        if (i != j && tmp.equals(cur)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        });

        previousBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (currentCard > 1) {
                    nextBtn.setText("Next");
                    previousBtn.setVisible(currentCard != 2);
                    currentCard -= 1;
                    cl.show(cardPanel, "" + (currentCard));
                }
            }
        });

        getContentPane().add(cardPanel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(SudokuMainUI.main);
    }

    // endregion
}
