package com.cs2105.swump.core.generator;

import com.cs2105.swump.core.solver.Solver;

import org.jacop.core.IntVar;

/**
 * A _very_ simple implementation of a SuDoku puzzle generator. The
 * algorithm is as follows:
 * 1. Firstly, fill three random cells with a random value from 1-9.
 * Each time a cell is filled, check against the solver to make sure it
 * is still solvable.
 * 2. We then solve this puzzle to obtain a solution grid, this will be
 * the base we create our puzzle from.
 * 3. Creating a puzzle involves covering pair cells and checking it
 * against the solver each time to make sure it is still valid(solvable
 * and unique). This procedure is repeated until a specific number of
 * uncovered cells(givens) are left.
 */

public class PuzzleGenerator {
    // region fields

    private int[][] puzzleGrid = new int[9][9];
    private int[][] solutionGrid = new int[9][9];
    private final Solver solver = new Solver();
    private IntVar[][] variables = new IntVar[9][9];
    private long timeout = 2000;
    private int givens = 0;
    private boolean isGenerated = false;
    private static PuzzleGenerator instance = null;

    // endregion

    // region constructors

    protected PuzzleGenerator() {
    }

    public static PuzzleGenerator getInstance() {
        if (instance == null)
            instance = new PuzzleGenerator();

        return instance;
    }

    // endregion

    // region accessors

    public void setTimeOut(long timeout) {
        this.timeout = timeout;
    }

    public void setNumberOfGivens(int givens) {
        this.givens = givens;
    }

    public int getNumberOfGivens() {
        return givens;
    }

    public int[][] getPuzzleSolution() {
        return solutionGrid;
    }

    public int[][] getPuzzle() {
        return puzzleGrid;
    }

    // endregion

    // region public methods

    // Creates a new puzzle with a specified number of givens
    public void generate(int givens) throws Exception {
        try {
            initialize();
            setNumberOfGivens(givens);
            mask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Solve puzzle with region and marked constraint
    public int[][] solvePuzzle(int[][] puzzleToSolve, int[][] regionGrid, String markerType, boolean[][] markerGrid) {
        solver.modelPuzzle(puzzleToSolve, regionGrid, markerType, markerGrid, true);

        if (!solver.searchSmallestDomain()) {
            solver.modelPuzzle(puzzleToSolve, regionGrid, markerType, markerGrid, false);
            solver.searchSmallestDomain();
        }

        int[][] resultGrid = new int[9][9];

        for (int i = 0; i < resultGrid.length; i++)
            for (int j = 0; j < resultGrid[0].length; j++)
                resultGrid[i][j] = solver.getElements()[i][j].value();
        return resultGrid;
    }

    // endregion

    // region private methods

    private void initialize() {
        int inserted = 0;
        /** Insert values into three random unknown cells */
        while (inserted < 3) {
            int value = RandomGenerator.getRandomValue();
            int numRow = RandomGenerator.getRandomRow();
            int numCol = RandomGenerator.getRandomCol();

            if (puzzleGrid[numRow][numCol] == 0)
                puzzleGrid[numRow][numCol] = value;

            solver.modelPuzzle(puzzleGrid, null, null, null, false);

            if (!solver.searchSmallestDomain())
                puzzleGrid[numRow][numCol] = 0;
            else
                inserted++;
        }
        variables = solver.getElements();

        performSwapOperation();

        /** Converting IntVar array to integer array for ease of operation */
        for (int i = 0; i < puzzleGrid.length; i++)
            for (int j = 0; j < puzzleGrid[0].length; j++)
                solutionGrid[i][j] = puzzleGrid[i][j] = variables[i][j].value();
    }

    // Shuffles rows and columns while still making it valid
    private void performSwapOperation() {
        performRowSwap();
        performColumnSwap();
    }

    // A method that shuffles rows
    private void performRowSwap() {
        for (int row = 0; row < variables.length - 3; row += 3) {
            IntVar[] temp = variables[row];
            variables[row] = variables[row + 2];
            variables[row + 2] = temp;
        }
    }

    // A method that shuffles columns
    private void performColumnSwap() {
        for (int row = 0; row < variables.length; row++) {
            for (int col = 0; col < variables[0].length - 3; col += 3) {
                IntVar temp = variables[row][col];
                variables[row][col] = variables[row][col + 2];
                variables[row][col + 2] = temp;
            }
        }
    }

    // A method that performs cell masking based on number of givens
    private boolean mask() {
        long timeStarted;
        int currentNumOfGivens = 81;
        try {
            timeStarted = System.currentTimeMillis();
            while (currentNumOfGivens != givens) {
                int row = RandomGenerator.getRandomRow();
                int col = RandomGenerator.getRandomCol();
                int removedA, removedB;
                removedA = removedB = 0;

                /** check if we're trying to mask already masked cells */
                if (puzzleGrid[row][col] > 0 && puzzleGrid[col][row] > 0) {
                    /** Here, we mask mirrored pair cells */
                    if (currentNumOfGivens - givens >= 2) {
                        removedA = puzzleGrid[row][col];
                        removedB = puzzleGrid[col][row];
                        puzzleGrid[row][col] = puzzleGrid[col][row] = 0;
                    }

                    if (currentNumOfGivens - givens == 1) {
                        removedA = puzzleGrid[row][col];
                        puzzleGrid[row][col] = 0;
                    }

                    /** Verify our modified puzzle grid */
                    solver.modelPuzzle(puzzleGrid, null, null, null, false);
                    boolean isSolvable = solver.searchAll();
                    int numSolutions = solver.getLabel().getSolutionListener().solutionsNo();

                    /**
                     * We are only interested in solvable and unique puzzles,
                     * only accept result if this is true
                     */
                    if (!isSolvable || (numSolutions > 1)) {
                        puzzleGrid[row][col] = removedA;
                        puzzleGrid[col][row] = removedB;
                    }
                    currentNumOfGivens = countGivens(puzzleGrid);

                    if ((System.currentTimeMillis() - timeStarted) > timeout) {
                        isGenerated = false;
                        break;
                    } else
                        isGenerated = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isGenerated;

    }

    private int countGivens(int[][] a) {
        int givens = 0;
        for (int j = 0; j < a.length; j++)
            for (int[] anA : a)
                if (anA[j] != 0)
                    givens++;
        return givens;
    }

    // endregion
}
