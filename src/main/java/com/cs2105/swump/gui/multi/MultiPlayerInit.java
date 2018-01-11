package com.cs2105.swump.gui.multi;

import com.cs2105.swump.core.SudokuLogic;
import com.cs2105.swump.gui.SudokuMainUI;
import com.cs2105.swump.gui.misc.FontGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class MultiPlayerInit extends JDialog
{
    private int currentCard = 1;
    private JPanel cardPanel;
    private CardLayout cl;
    private JButton previousBtn;
    private JButton nextBtn;

    public MultiPlayerInit()
    {
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
        String choice[] = {"2", "3", "4"};
        JLabel lblNumPlayer = new JLabel("Number of players:");
        final JComboBox cboChoice = new JComboBox(choice);

        // Select turn time
        String turn[] = {"30", "45", "60", "90"};
        JLabel lblNumTurn = new JLabel("Turn duration:");
        final JComboBox cboTurn = new JComboBox(turn);
        cboTurn.setSelectedIndex(0);

        // Select try
        String tries[] = {"5", "6", "7", "8", "9", "10"};
        JLabel lblTry = new JLabel("Tries per turn:");
        final JComboBox cboTry = new JComboBox(tries);
        cboTry.setSelectedIndex(0);

        // Select difficulty
        String difficulty[] = {"Blank", "Easy", "Normal", "Hard"};
        JLabel lblDifficulty = new JLabel("Difficulty level:");
        final JComboBox cboDiff = new JComboBox(difficulty);
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
        final ArrayList<InputPlayer> players = new ArrayList<InputPlayer>();

        // jp2
        jp2.setLayout(new BorderLayout());
        final JPanel playerDetail = new JPanel();
        playerDetail.setLayout(new GridLayout(4, 1));
        for (int i = 0; i < 2; i++)
        {
            InputPlayer player = new InputPlayer(i + 1);
            playerDetail.add(player);
            players.add(player);
        }

        // Create player detail UI
        cboChoice.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent event)
            {
                players.clear();
                int selection = Integer.parseInt((String) cboChoice.getSelectedItem());
                playerDetail.removeAll();
                for (int i = 0; i < selection; i++)
                {
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

        nextBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                if (currentCard == 1)
                {
                    nextBtn.setText("Start Game!");
                    previousBtn.setVisible(true);
                    currentCard += 1;
                    cl.show(cardPanel, "" + (currentCard));
                }
                else if (currentCard == 2)
                {
                    boolean check = false;
                    currentCard += 1;
                    for (int i = 0; i < players.size(); i++)
                    {
                        if (players.get(i).name != null)
                            check = true;
                        else
                        {
                            check = false;
                            i = players.size();
                        }

                    }
                    if (check)
                    {
                        if (checkSimilarColor())
                        {
                            if (checkName())
                                currentCard = 0;
                            else
                            {
                                currentCard -= 1;
                                JOptionPane.showMessageDialog(SudokuMainUI.main, "Ensure the names are unique.");
                            }
                        }
                        else
                        {
                            currentCard -= 1;
                            JOptionPane.showMessageDialog(SudokuMainUI.main, "Ensure the colours are unqiue.");
                        }
                    }
                    else
                    {
                        currentCard -= 1;
                        JOptionPane.showMessageDialog(SudokuMainUI.main, "Ensure that all names are entered.");
                    }
                }
                if (currentCard == 0)
                {
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
                        SudokuLogic.getInstance().initializePlayer(players.get(i).name, players.get(i).color);
                    SudokuLogic.getInstance().startNewGame(diff, Integer.parseInt(cboTurn.getSelectedItem().toString()), Integer.parseInt(cboTry.getSelectedItem().toString()));
                    SudokuMainUI.main.activateMultiPlayerPanel();
                }
            }

            private boolean checkSimilarColor()
            {
                String temp, current;
                for (int i = 0; i < players.size(); i++)
                {
                    temp = players.get(i).color.toString();
                    for (int j = 0; j < players.size(); j++)
                    {
                        current = players.get(j).color.toString();
                        if (i != j && temp.equalsIgnoreCase(current))
                            return false;
                    }
                }
                return true;
            }

            private boolean checkName()
            {
                String temp, current;
                for (int i = 0; i < players.size(); i++)
                {
                    temp = players.get(i).name.toString();
                    for (int j = 0; j < players.size(); j++)
                    {
                        current = players.get(j).name.toString();
                        if (i != j && temp.equals(current)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        });

        previousBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                if (currentCard > 1)
                {
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
}
