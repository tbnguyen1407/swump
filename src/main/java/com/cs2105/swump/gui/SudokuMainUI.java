package com.cs2105.swump.gui;

import com.cs2105.swump.core.SudokuLogic;
import com.cs2105.swump.gui.board.CellUI;
import com.cs2105.swump.gui.board.InputUI;
import com.cs2105.swump.gui.board.SudokuBoard;
import com.cs2105.swump.gui.misc.About;
import com.cs2105.swump.gui.misc.Scoreboard;
import com.cs2105.swump.gui.misc.SidePanel;
import com.cs2105.swump.gui.misc.ThemeSelector;
import com.cs2105.swump.gui.multi.MultiPlayerInit;
import com.cs2105.swump.gui.multi.MultiPlayerStatPanel;
import com.cs2105.swump.gui.single.SingleInit;
import com.cs2105.swump.gui.single.SinglePlayerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuMainUI extends JFrame {
    private JProgressBar progressBar;
    private static final long serialVersionUID = 6199126953768687537L;

    public static SudokuMainUI main = null;
    public SudokuBoard sudokuBoard;
    private SidePanel sidePanel;
    private SinglePlayerPanel singlePlayerPanel;
    private MultiPlayerStatPanel multiPlayerPanel;

    public Thread gameUIUpdateThread;

    private AttentionSeeker attentionSeeker;
    private LogoPanel logoPanel;
    private CellUI activeCell;
    private InputUI inputUI;

    private boolean isValueInput;
    private boolean isPencilMarkInput;
    private boolean isRegionInput;
    private boolean isMarkInput;

    private boolean isNewMultiPlayerGameAndFirstPlayer;
    private String firstPlayerName;
    private String previousPlayerName;
    private boolean pauseTimer;

    public SudokuMainUI() {
        createLogoPanel();
        createAttentionSeeker();
        createInputUI();

        this.setLayout(new BorderLayout());
        createControlPanel();

        sudokuBoard = new SudokuBoard();
        this.add(sudokuBoard, BorderLayout.CENTER);

        sidePanel = new SidePanel();
        this.add(sidePanel, BorderLayout.EAST);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setIconImage(new ImageIcon("img/icon.png").getImage());
        this.setTitle("Swump!");

        main = this;
    }

    private void createLogoPanel() {
        logoPanel = new LogoPanel();
        this.add(logoPanel);
    }

    private void createAttentionSeeker() {
        attentionSeeker = new AttentionSeeker("", 100, Font.PLAIN, 50);
        this.add(attentionSeeker);
    }

    private void createInputUI() {
        inputUI = new InputUI();
        this.add(inputUI);
    }

    private void createControlPanel() {
        // SinglePlayer button
        JButton btnNewSingle = new JButton("Singleplayer", getIcon("img/iconSingleplayer.png"));
        btnNewSingle.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnNewSingle.setHorizontalTextPosition(AbstractButton.CENTER);
        btnNewSingle.setIconTextGap(-8);
        btnNewSingle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SingleInit().setVisible(true);
                SudokuLogic.getInstance().setNumOfHints(3);
                SudokuMainUI.main.repaint();
            }
        });

        // MultiPlayer button
        JButton btnNewMulti = new JButton("Multiplayer", getIcon("img/iconMultiplayer.png"));
        btnNewMulti.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnNewMulti.setHorizontalTextPosition(AbstractButton.CENTER);
        btnNewMulti.setIconTextGap(-8);
        btnNewMulti.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new MultiPlayerInit().setVisible(true);
                    SudokuMainUI.main.repaint();
                } catch (Exception ex) {
                }
            }
        });

        // LoadGame button
        JButton btnLoadGame = new JButton("Load game", getIcon("img/iconLoad.png"));
        btnLoadGame.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnLoadGame.setHorizontalTextPosition(AbstractButton.CENTER);
        btnLoadGame.setIconTextGap(-8);
        btnLoadGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String gameList[] = SudokuLogic.getInstance().loadGameList();

                try {
                    String response = (String) JOptionPane.showInputDialog(null, "Select a saved game", "Load game",
                            JOptionPane.PLAIN_MESSAGE, null, gameList, "");
                    resumeSinglePlayerGame(response);
                } catch (NullPointerException f) {
                }
            }
        });

        // SaveGame button
        JButton btnSaveGame = new JButton("Save game", getIcon("img/iconSave.png"));
        btnSaveGame.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnSaveGame.setHorizontalTextPosition(AbstractButton.CENTER);
        btnSaveGame.setIconTextGap(-8);
        btnSaveGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (SudokuLogic.getInstance().getState() != 0) {
                    JOptionPane.showMessageDialog(null, "There is no game in progress.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (SudokuLogic.getInstance().getMode() == 0) {
                    Object result = JOptionPane.showInputDialog(SudokuMainUI.main, "Enter game name:");
                    try {
                        if (result != null && !result.toString().equals(""))
                            SudokuLogic.getInstance().saveGame(result.toString());
                        else if (result.toString().equals(""))
                            JOptionPane.showMessageDialog(null, "Please enter a game name!");
                    } catch (NullPointerException f) {
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Saving is not available in multiplayer mode.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ScoreBoard button
        JButton btnScoreBoard = new JButton("Scoreboard", getIcon("img/iconScore.png"));
        btnScoreBoard.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnScoreBoard.setHorizontalTextPosition(AbstractButton.CENTER);
        btnScoreBoard.setIconTextGap(-8);
        btnScoreBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Scoreboard().setVisible(true);
            }
        });

        // ChangeTheme button
        JButton btnChangeTheme = new JButton("Themes", getIcon("img/iconTheme.png"));
        btnChangeTheme.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnChangeTheme.setHorizontalTextPosition(AbstractButton.CENTER);
        btnChangeTheme.setIconTextGap(-8);
        btnChangeTheme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ThemeSelector().setVisible(true);
            }
        });

        // About button
        JButton btnAbout = new JButton("About", getIcon("img/iconAbout.png"));
        btnAbout.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnAbout.setHorizontalTextPosition(AbstractButton.CENTER);
        btnAbout.setIconTextGap(-8);
        btnAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new About();
            }
        });

        // ControlPanel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 7));
        controlPanel.add(btnNewSingle);
        controlPanel.add(btnNewMulti);
        // controlPanel.add(btnSaveGame);
        // controlPanel.add(btnLoadGame);
        controlPanel.add(btnScoreBoard);
        controlPanel.add(btnChangeTheme);
        controlPanel.add(btnAbout);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // ProgressBar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        progressBar.setMaximum(81);

        // Populate main ui components
        this.add(controlPanel, BorderLayout.NORTH);
        this.add(progressBar, BorderLayout.SOUTH);
    }

    private ImageIcon getIcon(String imgPath) {
        ImageIcon icon = new ImageIcon(imgPath);
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);

    }

    public void updateProgressBar() {
        progressBar.setValue(81 - SudokuLogic.getInstance().getRemainingCells());
    }

    public SinglePlayerPanel getSinglePlayerPanel() {
        return singlePlayerPanel;
    }

    public MultiPlayerStatPanel getMultiPlayerPanel() {
        return multiPlayerPanel;
    }

    public InputUI getInputUI() {
        return inputUI;
    }

    public CellUI getActiveCell() {
        return activeCell;
    }

    public void setActiveCell(CellUI activeCell) {
        this.activeCell = activeCell;
    }

    public boolean getIsValueInput() {
        return isValueInput;
    }

    public boolean getIsPencilMarkInput() {
        return isPencilMarkInput;
    }

    public boolean getIsRegionInput() {
        return this.isRegionInput;
    }

    public boolean getIsMarkInput() {
        return this.isMarkInput;
    }

    public void setIsValueInput() {
        isMarkInput = false;
        isPencilMarkInput = false;
        isRegionInput = false;
        isValueInput = true;
    }

    public void setIsPencilMarkInput() {
        isMarkInput = false;
        isPencilMarkInput = true;
        isRegionInput = false;
        isValueInput = false;
    }

    public void setIsRegionInput() {
        isMarkInput = false;
        isPencilMarkInput = false;
        isRegionInput = true;
        isValueInput = false;
    }

    public void setIsMarkInput() {
        isMarkInput = true;
        isPencilMarkInput = false;
        isRegionInput = false;
        isValueInput = false;
    }

    public LogoPanel getLogoPanel() {
        return logoPanel;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public SidePanel getSidePanel() {
        return sidePanel;
    }

    public void showWin(int time) {
        stopTimer();
        if (SudokuLogic.getInstance().getMode() == 0) {
            JOptionPane.showMessageDialog(SudokuMainUI.main, "Congratulations!\nYou have completed the puzzle!");
            String name = JOptionPane.showInputDialog(null, "Please enter your name:");
            if (name != null && name.trim().length() > 0)
                SudokuLogic.getInstance().updateScoreboard(name, time);
        } else
            JOptionPane.showMessageDialog(SudokuMainUI.main,
                    SudokuLogic.getInstance().getWinner().getPlayerName() + " wins!");

        logoPanel.setVisible(true);
        sudokuBoard.setVisible(false);
        progressBar.setVisible(false);
        sidePanel.setVisible(false);
    }

    public void activateMultiPlayerPanel() {
        multiPlayerPanel = new MultiPlayerStatPanel();
        sidePanel.setPanel(multiPlayerPanel);
        startTimer();
        updateProgressBar();
        SudokuMainUI.main.setIsValueInput();
        SudokuMainUI.main.repaint();
    }

    public void activateSinglePlayerPanel(int difficulty) {
        singlePlayerPanel = new SinglePlayerPanel();
        sidePanel.setPanel(singlePlayerPanel);
        startTimer();

        String diff = "";
        switch (difficulty) {
            case 0:
                diff = "Easy";
                break;
            case 1:
                diff = "Normal";
                break;
            case 2:
                diff = "Hard";
                break;
            case 3:
                diff = "Blank";
                break;
        }
        SudokuMainUI.main.getSinglePlayerPanel().setDifficulty(diff);
        SudokuMainUI.main.setIsValueInput();
        SudokuMainUI.main.repaint();
    }

    private void startTimer() {
        pauseTimer = false;

        isNewMultiPlayerGameAndFirstPlayer = true;
        firstPlayerName = SudokuLogic.getInstance().getCurrentPlayer().getPlayerName();
        previousPlayerName = SudokuLogic.getInstance().getCurrentPlayer().getPlayerName();

        logoPanel.setVisible(false);
        sudokuBoard.setVisible(true);
        progressBar.setVisible(true);
        sidePanel.setVisible(true);

        if (gameUIUpdateThread != null)
            return;
        gameUIUpdateThread = new Thread(new Runnable() {
            public void run() {
                while (!pauseTimer) {
                    try {
                        long time = SudokuLogic.getInstance().getElapsedTime();
                        long elapsedTime;
                        if (SudokuLogic.getInstance().getMode() == 0) // SINGLEPLAYER
                            elapsedTime = time;
                        else {
                            String currentPlayerName = SudokuLogic.getInstance().getCurrentPlayer().getPlayerName();

                            if (multiPlayerPanel != null)
                                multiPlayerPanel.refresh();
                            elapsedTime = SudokuLogic.getInstance().getTurnTime() - time + 1000;
                            if (SudokuMainUI.main.getMultiPlayerPanel() != null)
                                SudokuMainUI.main.getMultiPlayerPanel().refreshButtons();

                            if (!firstPlayerName.equals(currentPlayerName))
                                isNewMultiPlayerGameAndFirstPlayer = false;

                            attentionSeeker.setLocation(main.sudokuBoard.getLocation());
                            attentionSeeker.setSize(main.sudokuBoard.getSize());
                            if (elapsedTime / 1000 <= 5) {
                                attentionSeeker.setText(elapsedTime / 1000 + "", 100, Font.PLAIN, 500);
                                attentionSeeker.setVisible(true);
                                main.repaint();
                            } else if ((elapsedTime / 1000 + 1 > SudokuLogic.getInstance().getTurnTime() / 1000)
                                    && (!previousPlayerName.equals(currentPlayerName))) {
                                SudokuMainUI.main.getMultiPlayerPanel().getTakeOver().setSelected(false);
                                attentionSeeker.setText("Next!", 150, Font.BOLD, 140);
                                attentionSeeker.setVisible(true);
                                main.repaint();
                            } else if ((elapsedTime / 1000 + 1 > SudokuLogic.getInstance().getTurnTime() / 1000)
                                    && isNewMultiPlayerGameAndFirstPlayer) {
                                isNewMultiPlayerGameAndFirstPlayer = false;
                                attentionSeeker.setText("Start!", 150, Font.BOLD, 140);
                                attentionSeeker.setVisible(true);
                                main.repaint();
                            } else
                                attentionSeeker.setVisible(false);
                            previousPlayerName = currentPlayerName;
                        }
                        SudokuMainUI.main.sidePanel.setTime(elapsedTime);

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
        gameUIUpdateThread.start();
    }

    public void stopTimer() {
        if (gameUIUpdateThread != null) {
            gameUIUpdateThread = null;
            pauseTimer = true;
        }
    }

    public void startNewSinglePlayerGame(int difficulty) {
        SudokuLogic.getInstance().startNewGame(difficulty);
        attentionSeeker.setVisible(false);
        activateSinglePlayerPanel(difficulty);
        SudokuLogic.getInstance().setNumOfHints(3);
        updateProgressBar();
    }

    public void resumeSinglePlayerGame(String name) {
        SudokuLogic.getInstance().loadGame(name);
        activateSinglePlayerPanel(SudokuLogic.getInstance().puzzle.getDifficulty());
        SudokuLogic.getInstance().setNumOfHints(3);
        updateProgressBar();
    }
}
