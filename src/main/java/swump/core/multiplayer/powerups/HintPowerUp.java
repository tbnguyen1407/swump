package swump.core.multiplayer.powerups;

import swump.core.Cell;
import swump.core.Player;
import swump.core.Puzzle;
import swump.core.SudokuLogic;
import swump.core.generator.RandomGenerator;

public class HintPowerUp implements PowerUp {
    // region fields

    private Player player;
    private Puzzle puzzle;

    // endregion

    // region constructors

    public HintPowerUp() {
    }

    public HintPowerUp(Player player) {
        this.player = player;
    }

    // endregion

    // region public methods

    public boolean use() {
        puzzle = SudokuLogic.getInstance().getPuzzle();
        Cell[][] grid = puzzle.getGrid();
        int[][] userAns = puzzle.getUserAnswers();
        int[][] solution = puzzle.getSystemAnswers();
        int row = RandomGenerator.getRandomRow();
        int col = RandomGenerator.getRandomCol();

        while (userAns[row][col] != 0) {
            row = RandomGenerator.getRandomRow();
            col = RandomGenerator.getRandomCol();
        }

        if (userAns[row][col] == 0) {
            puzzle.setUserAnswers(row, col, solution[row][col]);
            grid[row][col].setOwner(player);
            player.getScore().increaseCellsOwned();
            puzzle.setGrid(grid);
        }
        SudokuLogic.getInstance().handleWin();
        return true;
    }

    public Type getType() {
        return PowerUp.Type.HINT;
    }

    @Override
    public void setUser(Player p) {
        this.player = p;
    }

    // endregion
}
