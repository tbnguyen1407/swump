package com.cs2105.swump.gui.multi;

import com.cs2105.swump.core.Player;
import com.cs2105.swump.core.SudokuLogic;
import com.cs2105.swump.core.multiplayer.powerups.PowerUp;
import com.cs2105.swump.gui.SudokuMainUI;
import com.cs2105.swump.gui.misc.FontGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class MultiPlayerStatPanel extends JPanel
{
    private static final long serialVersionUID = -3478952106040900005L;

    private JLabel lblHint;
    private JLabel lblTake;
    private JLabel lblTry;
    private JLabel lblTime;

    private JButton btnTake;
    private JButton btnHint;
    private JButton btnTry;
    private JButton btnTime;

    private StatUI[] playerStat;
    private JPanel playerInfo = new JPanel();

    private class StatUI extends JPanel
    {
        private static final long serialVersionUID = -4750409607717678613L;
        private static final String SCORE_DESC = "Score: ";
        private static final String TRIES_DESC = "Tries Left: ";
        private static final int CURRENT_TURN_BOX_LENGTH = 50;
        private static final int NOT_TURN_BOX_LENGTH = 20;
        private static final int X_OFFSET = 15;
        private static final int SPACE_BETWEEN = 10;
        private boolean isPlayerTurn;
        private String playerName;
        private String score;
        private String triesLeft;
        private Color playerColor;

        public StatUI()
        {
            playerName = "";
            score = "";
            playerColor = Color.BLACK;
        }

        public void setPlayerTurn(boolean turn)
        {
            isPlayerTurn = turn;

            this.setPreferredSize(new Dimension(200, 70));
            this.setMinimumSize(getPreferredSize());
            this.setMaximumSize(getPreferredSize());
        }

        public boolean isPlayerTurn()
        {
            return isPlayerTurn;
        }

        public String getPlayerScore()
        {
            return score;
        }

        public void setPlayerScore(int score)
        {
            this.score = SCORE_DESC + score;
        }

        public Color getPlayerColor()
        {
            return playerColor;
        }

        public void setPlayerColor(Color playerColor)
        {
            this.playerColor = playerColor;
        }

        public String getPlayerName()
        {
            return playerName;
        }

        public void setPlayerName(String name)
        {
            this.playerName = name;
        }

        public String getTriesLeft()
        {
            return triesLeft;
        }

        public void setTriesLeft(int t)
        {
            this.triesLeft = TRIES_DESC + t;
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getPlayerName().equals(""))
                return;

            g.setColor(Color.DARK_GRAY);

            int height = this.getHeight();
            int playerNamefontHeight, scorefontHeight;

            if (isPlayerTurn())
            {
                g.setFont(FontGenerator.generateItalicFont(Font.PLAIN, 12));
                scorefontHeight = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();
                g.setFont(FontGenerator.generateItalicFont(Font.BOLD, 30));
                playerNamefontHeight = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();

                // Draw player's name
                g.drawString(getPlayerName(), CURRENT_TURN_BOX_LENGTH + X_OFFSET + SPACE_BETWEEN, (height - SPACE_BETWEEN - SPACE_BETWEEN - scorefontHeight - scorefontHeight - playerNamefontHeight) / 3 + playerNamefontHeight);

                // Draw player's score
                g.setFont(FontGenerator.generateItalicFont(Font.PLAIN, 12));
                g.drawString(getPlayerScore(), CURRENT_TURN_BOX_LENGTH + X_OFFSET + SPACE_BETWEEN, (height - SPACE_BETWEEN - SPACE_BETWEEN - scorefontHeight - scorefontHeight - playerNamefontHeight) / 3 + playerNamefontHeight + SPACE_BETWEEN + scorefontHeight);

                // Draw player's tries left
                g.drawString(getTriesLeft(), CURRENT_TURN_BOX_LENGTH + X_OFFSET + SPACE_BETWEEN, (height - SPACE_BETWEEN - SPACE_BETWEEN - scorefontHeight - scorefontHeight - playerNamefontHeight) / 3 + playerNamefontHeight + SPACE_BETWEEN + scorefontHeight + SPACE_BETWEEN + scorefontHeight);

                // Paint player's colour
                g.setColor(getPlayerColor());
                g.fillRoundRect(X_OFFSET, (height - CURRENT_TURN_BOX_LENGTH) / 2, CURRENT_TURN_BOX_LENGTH, CURRENT_TURN_BOX_LENGTH, 8, 8);
            }
            else
            {
                g.setFont(FontGenerator.generateItalicFont(Font.PLAIN, 12));
                scorefontHeight = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();
                g.setFont(FontGenerator.generateItalicFont(Font.BOLD, 16));
                playerNamefontHeight = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();

                // Draw player's name
                g.drawString(getPlayerName(), CURRENT_TURN_BOX_LENGTH + X_OFFSET + SPACE_BETWEEN, (height - SPACE_BETWEEN - scorefontHeight - playerNamefontHeight) / 2 + playerNamefontHeight);

                // Draw player's score
                g.setFont(FontGenerator.generateItalicFont(Font.PLAIN, 12));
                g.drawString(getPlayerScore(), CURRENT_TURN_BOX_LENGTH + X_OFFSET + SPACE_BETWEEN, (height - SPACE_BETWEEN - scorefontHeight - playerNamefontHeight) / 2 + playerNamefontHeight + SPACE_BETWEEN + scorefontHeight);

                // Paint player's colour
                g.setColor(getPlayerColor());
                g.fillRoundRect( ((CURRENT_TURN_BOX_LENGTH - NOT_TURN_BOX_LENGTH) / 2) + X_OFFSET, (height - NOT_TURN_BOX_LENGTH) / 2, NOT_TURN_BOX_LENGTH, NOT_TURN_BOX_LENGTH, 8, 8);
            }
        }
    }

    public MultiPlayerStatPanel()
    {
        // lblHint
        lblHint = new JLabel("");
        lblHint.setVerticalAlignment(SwingConstants.BOTTOM);
        lblHint.setHorizontalAlignment(SwingConstants.CENTER);

        // lblTake
        lblTake = new JLabel("");
        lblTake.setVerticalAlignment(SwingConstants.BOTTOM);
        lblTake.setHorizontalAlignment(SwingConstants.CENTER);

        // lblTry
        lblTry = new JLabel("");
        lblTry.setVerticalAlignment(SwingConstants.BOTTOM);
        lblTry.setHorizontalAlignment(SwingConstants.CENTER);

        // lblTime
        lblTime = new JLabel("");
        lblTime.setVerticalAlignment(SwingConstants.BOTTOM);
        lblTime.setHorizontalAlignment(SwingConstants.CENTER);

        // btnHint
        btnHint = new JButton(getIcon("img/iconHint.png"));
        btnHint.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnHint.setHorizontalTextPosition(AbstractButton.CENTER);
        btnHint.setToolTipText("Get a free hint!");
        btnHint.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    SudokuLogic.getInstance().usePowerUp(PowerUp.Type.HINT);
                    SudokuMainUI.main.getMultiPlayerPanel().refreshButtons();
                    SudokuMainUI.main.updateProgressBar();
                    SudokuMainUI.main.sudokuBoard.repaint();

                    if (SudokuLogic.getInstance().getState() == 1)
                        SudokuMainUI.main.showWin(0);
                }
                catch (Exception ex)
                {
                }
            }
        });

        // btnTake
        btnTake = new JButton(getIcon("img/iconTakeOver.png"));
        btnTake.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnTake.setHorizontalTextPosition(AbstractButton.CENTER);
        btnTake.setToolTipText("Take over a cell!");
        btnTake.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    if (btnTake.isSelected() == true)
                    {
                        btnTake.setSelected(false);
                        SudokuMainUI.main.sudokuBoard.repaint();
                    }
                    else
                    {
                        btnTake.setSelected(true);
                        SudokuMainUI.main.sudokuBoard.repaint();
                    }
                    SudokuMainUI.main.getMultiPlayerPanel().refreshButtons();
                }
                catch (Exception ex)
                {
                }
            }
        });

        // btnTry
        btnTry = new JButton(getIcon("img/iconTries.png"));
        btnTry.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnTry.setHorizontalTextPosition(AbstractButton.CENTER);
        btnTry.setToolTipText("Add more tries to yourself this turn!");
        btnTry.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    SudokuLogic.getInstance().usePowerUp(PowerUp.Type.TRY);
                    SudokuMainUI.main.getMultiPlayerPanel().refreshButtons();
                }
                catch (Exception ex)
                {
                }
            }
        });

        // btnTime
        btnTime = new JButton(getIcon("img/iconTime.png"));
        btnTime.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnTime.setHorizontalTextPosition(AbstractButton.CENTER);
        btnTime.setToolTipText("Extend your turn time!");
        btnTime.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    SudokuLogic.getInstance().usePowerUp(PowerUp.Type.TIME);
                    SudokuMainUI.main.getMultiPlayerPanel().refreshButtons();
                }
                catch (Exception ex)
                {
                }
            }
        });

        // PlayerInfo
        playerInfo.setLayout(new BoxLayout(playerInfo, BoxLayout.PAGE_AXIS));
        playerStat = new StatUI[4];
        for (int i = 0; i < playerStat.length; i++)
        {
            playerStat[i] = new StatUI();
            playerInfo.add(playerStat[i]);
        }

        // ControlBtn
        JPanel controlBtn = new JPanel();
        controlBtn.setLayout(new GridLayout(2, 4));
        controlBtn.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 15));
        controlBtn.add(lblHint);
        controlBtn.add(lblTake);
        controlBtn.add(lblTry);
        controlBtn.add(lblTime);
        controlBtn.add(btnHint);
        controlBtn.add(btnTake);
        controlBtn.add(btnTry);
        controlBtn.add(btnTime);

        // Populate side ui components
        this.setLayout(new BorderLayout());
        this.add(playerInfo, BorderLayout.NORTH);
        this.add(controlBtn, BorderLayout.SOUTH);
        this.refresh();
    }

    private ImageIcon getIcon(String imgPath)
    {
        ImageIcon icon = new ImageIcon(imgPath);
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }

    public void refresh()
    {
        LinkedList<Player> play = SudokuLogic.getInstance().retrieveListOfPlayers();

        int total = play.size();
        for (int i = 0; i < total; i++)
        {
            Player tPlayer = play.remove();
            playerStat[i].setPlayerTurn(i == 0);
            playerStat[i].setPlayerScore((tPlayer.getScore() == null) ? 0 : tPlayer.getScore().getCellsOwned());
            playerStat[i].setPlayerName(tPlayer.getPlayerName());
            playerStat[i].setPlayerColor(tPlayer.getColor());
            playerStat[i].setTriesLeft(tPlayer.getTurnTries());
        }

        this.repaint();
    }

    public JButton getTakeOver()
    {
        return btnTake;
    }

    public void refreshButtons()
    {
        int pwHint = SudokuLogic.getInstance().getCurrentPlayer().numHintPowerUp;
        lblHint.setText(pwHint + "");
        lblHint.setEnabled(pwHint > 0);
        btnHint.setEnabled(pwHint > 0);

        int pwTake = SudokuLogic.getInstance().getCurrentPlayer().numTakeOverPowerUp;
        lblTake.setText(pwTake + "");
        lblTake.setEnabled(pwTake > 0);
        btnTake.setEnabled(pwTake > 0);

        int pwTry  = SudokuLogic.getInstance().getCurrentPlayer().numTryPowerUp;
        lblTry.setText(pwTry + "");
        lblTry.setEnabled(pwTry > 0);
        btnTry.setEnabled(pwTry > 0);

        int pwTime = SudokuLogic.getInstance().getCurrentPlayer().numTimePowerUp;
        lblTime.setText(pwTime + "");
        lblTime.setEnabled(pwTime > 0);
        btnTime.setEnabled(pwTime > 0);
    }
}
