package swump.core;

public class Puzzle {
    // region fields

    private final long puzzleID;
    private int difficulty;
    private Cell[][] grid;

    // endregion

    // region constructors

    public Puzzle() {
        grid = new Cell[9][9];
        puzzleID = 1;
        difficulty = 3;

        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                grid[i][j] = new Cell(0, false);

        // Generate default square constraint
        int blockIndex = 1;

        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++) {
                for (int k = 0; k < 3; k++)
                    for (int m = 0; m < 3; m++)
                        grid[row * 3 + k][col * 3 + m].setRegionId(blockIndex);
                blockIndex++;
            }
    }

    public Puzzle(long id, int diff, String solution, String givens) {
        grid = new Cell[9][9];
        puzzleID = id;
        difficulty = diff;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int value = Integer.parseInt(solution.charAt((9 * i) + j) + "");
                boolean isFilled = Integer.parseInt(givens.charAt((9 * i) + j) + "") != 0;

                grid[i][j] = new Cell(value, isFilled);
            }
        }
    }

    // endregion

    // region accessors

    public void setGrid(int[][] values) {
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j].setValue(values[i][j]);
                grid[i][j].setFilled();
            }
    }

    public int getDifficulty() {
        return difficulty;
    }

    public long getPuzzleID() {
        return puzzleID;
    }

    public int[][] getSystemAnswers() {
        int[][] cellValue = new int[9][9];
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                cellValue[i][j] = getSystemAnswers(i, j);
        return cellValue;
    }

    public int getSystemAnswers(int x, int y) {
        return grid[x][y].getValue();
    }

    public int[][] getUserAnswers() {
        int[][] userAnswers = new int[9][9];
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                userAnswers[i][j] = getUserAnswer(i, j);
        return userAnswers;
    }

    public int getUserAnswer(int x, int y) {
        return grid[x][y].isFilled() ? grid[x][y].getValue() : 0;
    }

    public int[][] getRegionGrid() {
        int[][] regionGrid = new int[9][9];
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid.length; j++)
                regionGrid[i][j] = getRegionId(i, j);
        return regionGrid;
    }

    public boolean[][] getMarkedGrid() {
        boolean[][] markedGrid = new boolean[9][9];
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid.length; j++)
                markedGrid[i][j] = grid[i][j].isMarked();
        return markedGrid;
    }

    public Player getOwner(int x, int y) {
        return grid[x][y].getOwner();
    }

    public void setOwner(int x, int y, Player player) {
        grid[x][y].setOwner(player);
    }

    public int[][][] getPencilMarks() {
        int[][][] pencilMarks = new int[9][9][9];
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                pencilMarks[i][j] = getPencilMarks(i, j);
        return pencilMarks;
    }

    public int[] getPencilMarks(int x, int y) {
        return grid[x][y].getPencilMarks();
    }

    public boolean getMarked(int x, int y) {
        return grid[x][y].isMarked();
    }

    public int getRegionId(int x, int y) {
        return grid[x][y].getRegionId();
    }

    public void setUserAnswers(int[][] userAnswers) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (userAnswers[i][j] == 0)
                    grid[i][j].setUnfilled();
                else
                    grid[i][j].setFilled();
            }
        }
    }

    public void setUserAnswers(int x, int y, int userAnswer) {
        if (SudokuLogic.getInstance().getDifficulty() == 3)
            grid[x][y].setFilled();
        else if (grid[x][y].getValue() == userAnswer)
            grid[x][y].setFilled();
        else
            grid[x][y].setUnfilled();
    }

    public void setPencilMarks(int[][][] pencilMarks) {
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                grid[i][j].setPencilMarks(pencilMarks[i][j]);
    }

    public void setMarked(int x, int y) {
        grid[x][y].setMarked();
    }

    public void addPencilMark(int x, int y, int value) {
        grid[x][y].addPencilmark(value);
    }

    public void addRegionId(int x, int y, int regionid) {
        grid[x][y].setRegionId(regionid);
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    // endregion
}
