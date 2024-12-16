package com.cs2105.swump.core;

import java.awt.Color;
import java.util.LinkedList;

import com.cs2105.swump.core.generator.PowerUpGenerator;
import com.cs2105.swump.core.generator.RandomGenerator;
import com.cs2105.swump.core.multiplayer.TurnController;
import com.cs2105.swump.core.multiplayer.powerups.PowerUp;
import com.cs2105.swump.core.storage.SqlStorage;

public class SudokuLogic {
    // region fields

    private static SudokuLogic instance = null;

    private LinkedList<Player> players = new LinkedList<Player>();

    private Puzzle puzzle = null;
    private Player currentPlayer = null;
    private Player winner = null;

    private final int IN_PLAY = 0;
    private final int COMPLETED = 1;
    private final int PRE_GAME = -1;

    private final int SINGLEPLAY = 0;
    private final int MULTIPLAY = 1;

    private int gameState = PRE_GAME;
    private int mode = SINGLEPLAY;

    private int allowableHints;
    private int difficulty;
    private int allowableTries;
    private int penaltyTime = 10000;
    private long turnTime = 30000;
    private Thread turnHandler;
    private int posX;
    private int posY;

    // endregion

    // region constructors

    protected SudokuLogic() {
    }

    public static SudokuLogic getInstance() {
        if (instance == null)
            instance = new SudokuLogic();
        return instance;
    }

    // endregion

    // region accessors

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public int getNumOfHints() {
        return allowableHints;
    }

    public void setNumOfHints(int hints) {
        allowableHints = hints;
        for (Player p : players)
            p.setNumHints(hints);
    }

    public void setTargetPosX(int x) {
        posX = x;
    }

    public void setTargetPosY(int y) {
        posY = y;
    }

    public int getTargetPosX() {
        return posX;
    }

    public int getTargetPosY() {
        return posY;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player player) {
        winner = player;
    }

    public void setState(int state) {
        switch (state) {
            case (IN_PLAY):
                gameState = IN_PLAY;
                break;
            case (COMPLETED):
                gameState = COMPLETED;
                break;
            case (PRE_GAME):
                gameState = PRE_GAME;
                break;
        }
    }

    public int getState() {
        return gameState;
    }

    public void setMode(int mode) {
        switch (mode) {
            case SINGLEPLAY:
                this.mode = SINGLEPLAY;
                break;
            case MULTIPLAY:
                this.mode = MULTIPLAY;
                break;
        }
    }

    public int getMode() {
        return mode;
    }

    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setTurnTime(long time) {
        this.turnTime = time;
    }

    public long getTurnTime() {
        return turnTime;
    }

    private void setTurnTries(int tries) {
        allowableTries = tries;
        for (Player p : players)
            p.setTurnTries(allowableTries);
    }

    private int getTurnTries() {
        return allowableTries;
    }

    public void setTimePenalty(int penalty) {
        penaltyTime = penalty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    // endregion

    // region public methods

    // Starts a new singleplayer game
    public Puzzle startNewGameSP(int difficulty) {
        if (this.getMode() == MULTIPLAY)
            turnHandler.interrupt();

        setMode(SINGLEPLAY);
        setState(IN_PLAY);
        setDifficulty(difficulty);
        resetTimer();
        startTimer();
        currentPlayer = new Player();
        players.add(currentPlayer);
        this.puzzle = SqlStorage.getInstance().retrievePuzzle(difficulty);

        return puzzle;
    }

    // Starts a new multiplayer game
    public Puzzle startNewGameMP(int difficulty, int turnTime, int tries) {
        if ((getMode()) == SINGLEPLAY) {
            turnHandler = new Thread(TurnController.getInstance());
            turnHandler.start();
        }
        setState(IN_PLAY);
        setMode(MULTIPLAY);
        setDifficulty(difficulty);
        resetTimer();
        startTimer();
        setTurnTime((long) turnTime * 1000);
        setTurnTries(tries);
        this.puzzle = SqlStorage.getInstance().retrievePuzzle(difficulty);
        currentPlayer = players.peek();
        currentPlayer.addPowerup(PowerUpGenerator.generate());
        return puzzle;
    }

    // Initializes player specifications
    public void initializePlayer(String name, Color color) {
        if ((getState() == COMPLETED) || (getState() == IN_PLAY)) {
            players.clear();
            setState(PRE_GAME);
        }
        Player newPlayer = new Player(name, color);
        players.add(newPlayer);
    }

    // Current player uses a powerup
    public boolean usePowerUp(PowerUp.Type type) {
        Player currentPlayer = getCurrentPlayer();
        int remaining = 0;
        switch (type) {
            case HINT:
                remaining = currentPlayer.getNumHintPowerUp();
                break;
            case TAKE_OVER:
                remaining = currentPlayer.getNumTakeOverPowerUp();
                break;
            case TRY:
                remaining = currentPlayer.getNumTryPowerUp();
                break;
            case TIME:
                remaining = currentPlayer.getNumTimePowerUp();
                break;
        }
        if (remaining > 0) {
            if (type == PowerUp.Type.TAKE_OVER)
                currentPlayer.takeOver(getTargetPosX(), getTargetPosY());
            else
                return currentPlayer.usePowerUp(type);
        }
        return false;
    }

    // List players who are currently playing
    public LinkedList<Player> retrieveListOfPlayers() {
        LinkedList<Player> playerList = new LinkedList<Player>();
        try {
            for (int i = 0; i < players.size(); i++)
                playerList.add(players.get(i));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return playerList;
    }

    // Start next currentPlayer's turn
    public boolean goNextTurn() {
        try {
            if (gameState == IN_PLAY) {
                currentPlayer = players.removeFirst();
                resetCurrentPlayerTries();
                players.add(currentPlayer);
                setCurrentPlayer(players.peek());
                currentPlayer.addPowerup(PowerUpGenerator.generate());
                resetTimer();
                startTimer();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Insert pencil mark
    public void insertPencilMark(int x, int y, int value) {
        puzzle.addPencilMark(x, y, value);
    }

    // Insert region id
    public void insertRegionId(int x, int y, int regionid) {
        puzzle.addRegionId(x, y, regionid);
    }

    // insert marker
    public void insertMark(int x, int y) {
        puzzle.setMarked(x, y);
    }

    // Refills the current currentPlayer's tries Method called after currentPlayer's
    // turn has ended
    private void resetCurrentPlayerTries() {
        currentPlayer.setTurnTries(getTurnTries());
    }

    // Validate each time currentPlayer fills a cell
    public boolean validateCell(int x, int y, int val) {
        int numTurns = currentPlayer.getTurnTries();
        boolean isValid;

        if (SudokuLogic.getInstance().getDifficulty() == 3)
            isValid = true;
        else {
            int[][] solution = puzzle.getSystemAnswers();
            isValid = (solution[x][y] == val);
        }

        if (isValid) {
            if (SudokuLogic.getInstance().getDifficulty() == 3) {
                this.puzzle.getGrid()[x][y].setValue(val);
                this.puzzle.getGrid()[x][y].setFilled();
            }

            this.puzzle.setUserAnswers(x, y, val);

            switch (mode) {
                case SINGLEPLAY:
                    break;
                case MULTIPLAY:
                    currentPlayer.setTurnTries(--numTurns);
                    puzzle.setOwner(x, y, currentPlayer);
                    currentPlayer.getScore().increaseCellsOwned();
                    break;
            }
        } else {
            switch (mode) {
                case SINGLEPLAY:
                    addToTime(penaltyTime);
                    break;
                case MULTIPLAY:
                    currentPlayer.setTurnTries(--numTurns);
                    break;
            }
        }

        if (mode == MULTIPLAY && currentPlayer.getTurnTries() == 0)
            goNextTurn();

        handleWin();

        return isValid;
    }

    // Handle win
    public void handleWin() {
        if (allCellsFilled()) {
            setState(COMPLETED);
            if (getMode() == MULTIPLAY) {
                determineWinner();
                turnHandler.interrupt();
            } else {
                setWinner(currentPlayer);
                resetTimer();
            }
            SqlStorage.getInstance().deletePuzzle(puzzle.getPuzzleID());
        }
    }

    // Increase timer with a fixed duration
    private void addToTime(int increasedTime) {
        Timer.getInstance().addToTime(increasedTime);
    }

    // Reset timer
    private void resetTimer() {
        Timer.getInstance().resetTimer();
    }

    // Find winner who owns the most cells
    private void determineWinner() {
        for (int i = 0; i < (players.size() - 1); i++) {
            int currentPlayerOwnedCells = players.get(i).getScore().getCellsOwned();
            int nextPlayerOwnedCells = players.get(i + 1).getScore().getCellsOwned();
            if (currentPlayerOwnedCells > nextPlayerOwnedCells)
                setWinner(players.get(i));
            else
                setWinner(players.get(i + 1));
        }
    }

    // get remaining cells
    public int getRemainingCells() {
        int cellsRemaining = 0;
        int[][] userAns = puzzle.getUserAnswers();

        for (int i = 0; i < puzzle.getUserAnswers().length; i++)
            for (int j = 0; j < puzzle.getUserAnswers().length; j++)
                if (userAns[i][j] == 0)
                    cellsRemaining++;

        return cellsRemaining;
    }

    // Check if all cells are filled
    public boolean allCellsFilled() {
        return getRemainingCells() == 0;
    }

    // Display hint
    public void showHint() {
        int[][] userAns = puzzle.getUserAnswers();
        int[][] solution = puzzle.getSystemAnswers();
        int row = RandomGenerator.getRandomRow();
        int col = RandomGenerator.getRandomCol();

        while (userAns[row][col] != 0) {
            row = RandomGenerator.getRandomRow();
            col = RandomGenerator.getRandomCol();
        }
        int numberOfHints = currentPlayer.getNumHints();
        if (userAns[row][col] == 0 && numberOfHints > 0) {
            puzzle.setUserAnswers(row, col, solution[row][col]);
            currentPlayer.setNumHints(numberOfHints - 1);
        }
        handleWin();
    }

    public void updateScoreboard(String playerName, long time) {
        if (getMode() == SINGLEPLAY) {
            SqlStorage.getInstance().updateScore(playerName, getDifficulty(), time);
        }
    }

    public String[][][] retrieveScoreboard() {
        return SqlStorage.getInstance().retrieveScoreboard();
    }

    public boolean saveGame(String gameName) {
        return SqlStorage.getInstance().saveGame(gameName, puzzle, getElapsedTime());
    }

    public String[] loadGameList() {
        return SqlStorage.getInstance().loadGameList();
    }

    public Puzzle loadGame(String gameName) {
        if (turnHandler != null)
            turnHandler.interrupt();

        Game savedGame = SqlStorage.getInstance().loadGame(gameName);
        puzzle = savedGame.getPuzzle();
        if (currentPlayer == null) {
            currentPlayer = new Player();
            players.add(currentPlayer);
        }

        if (puzzle != null) {
            long timeOffset = savedGame.getTimeElapsed();
            resetTimer();
            startTimer();
            Timer.getInstance().addToTime(timeOffset);
            setState(IN_PLAY);
            setMode(SINGLEPLAY);
            return puzzle;
        }
        return null;
    }

    public void deleteGame(String gameName) {
        SqlStorage.getInstance().deleteGame(gameName);
    }

    public void startTimer() {
        new Thread(Timer.getInstance()).start();
    }

    public long getElapsedTime() {
        return Timer.getInstance().getElapsedTime();
    }

    // endregion
}
