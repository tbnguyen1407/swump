package com.cs2105.swump.core.solver;

import org.jacop.constraints.*;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.*;

import java.util.ArrayList;

public class Solver
{
    public IntVar[][] elements;
    private ArrayList<IntVar> vars = new ArrayList<IntVar>();
    private Store store = new Store();
    public Search label;

    ///
    /// Constraint generators
    ///

    private IntVar[][] generateMarkedConstraintMatrix(boolean[][] markedGrid, IntVar[][] modelPuzzle)
    {
        ArrayList<IntVar> markedVars = new ArrayList<IntVar>();

        for (int i = 0; i < markedGrid.length; i++)
            for (int j = 0; j < markedGrid[i].length; j++)
                if (markedGrid[i][j])
                    markedVars.add(modelPuzzle[i][j]);

        IntVar[][] constraintMatrix = new IntVar[1][markedVars.size()];
        for (int i = 0; i < markedVars.size(); i++)
            constraintMatrix[0][i] = markedVars.get(i);

        return constraintMatrix;
    }

    private IntVar[][] generateRegionConstraintMatrix(int[][] regionIndexMatrix, IntVar[][] modelPuzzle)
    {
        IntVar[][] constraintMatrix = new IntVar[modelPuzzle.length][modelPuzzle.length];
        int[] indexMatrix = new int[modelPuzzle.length];
        for (int i = 0; i < indexMatrix.length; i++)
            indexMatrix[i] = 0;

        for (int row = 0; row < modelPuzzle.length; row++)
            for (int col = 0; col < modelPuzzle.length; col++)
            {
                int regionIndex = regionIndexMatrix[row][col] - 1;
                constraintMatrix[regionIndex][indexMatrix[regionIndex]] = modelPuzzle[row][col];
                indexMatrix[regionIndex]++;
            }

        return constraintMatrix;
    }

    private IntVar[][] generateDiagonalContraintMatrix(IntVar[][] modelPuzzle)
    {
        IntVar[][] constraintMatrix = new IntVar[2][modelPuzzle.length];

        int numRow = modelPuzzle.length / 3;
        int numCol = modelPuzzle.length / 3;

        IntVar[] diagonalf = new IntVar[numRow * numCol];
        IntVar[] diagonalb = new IntVar[numRow * numCol];

        for (int row = 0; row < numRow * numCol; row++)
        {
            diagonalf[row] = elements[row][row];
            diagonalb[row] = elements[row][numRow * numCol - row - 1];
        }
        constraintMatrix[0] = diagonalb;
        constraintMatrix[1] = diagonalf;

        return constraintMatrix;
    }

    private IntVar[][] generateHorizontalConstraintMatrix(IntVar[][] modelPuzzle)
    {
        IntVar[][] constraintMatrix = new IntVar[modelPuzzle.length][modelPuzzle.length];

        for (int index = 0; index < modelPuzzle.length; index++)
            constraintMatrix[index] = modelPuzzle[index];
        return constraintMatrix;
    }

    private IntVar[][] generateVerticalConstraintMatrix(IntVar[][] modelPuzzle)
    {
        IntVar[][] constraintMatrix = new IntVar[modelPuzzle.length][modelPuzzle.length];

        for (int index = 0; index < modelPuzzle.length; index++)
        {
            IntVar[] column = new IntVar[modelPuzzle.length];
            for (int col = 0; col < modelPuzzle.length; col++)
                column[col] = elements[col][index];

            constraintMatrix[index] = column;
        }
        return constraintMatrix;
    }

    ///
    /// Puzzle modeling
    ///
    public void modelPuzzle(int[][] puzzle, int[][] regionGrid, String markerType, boolean[][] markerGrid, boolean useDiagonalConstraint)
    {
        store = new Store();
        vars = new ArrayList<IntVar>();

        elements = new IntVar[puzzle.length][puzzle.length];

        /** Creating variables and assigning constraints to them */
        for (int i = 0; i < puzzle.length; i++)
            for (int j = 0; j < puzzle.length; j++)
                if (puzzle[i][j] == 0)
                {
                    elements[i][j] = new IntVar(store, "CELL(" + i + ", " + j + ")", 1, 9);
                    vars.add(elements[i][j]);
                }
                else
                    elements[i][j] = new IntVar(store, "CELL(" + i + ", " + j + ")" + i + j, puzzle[i][j], puzzle[i][j]);

        // Horizontal contraint
        IntVar[][] horConstraint = generateHorizontalConstraintMatrix(elements);
        for (IntVar[] aConstraint : horConstraint)
            store.impose(new Alldistinct(aConstraint));

        // Vertical constraint
        IntVar[][] verConstraint = generateVerticalConstraintMatrix(elements);
        for (IntVar[] aConstraint : verConstraint)
            store.impose(new Alldistinct(aConstraint));

        // Region constrant
        IntVar[][] regConstraint = generateRegionConstraintMatrix(regionGrid, elements);
        for (IntVar[] aConstraint : regConstraint)
            store.impose(new Alldistinct(aConstraint));

        // Marked constraint
        IntVar[][] marConstraint = generateMarkedConstraintMatrix(markerGrid, elements);

        for (IntVar[] aConstraint : marConstraint)
            if (aConstraint != null && aConstraint.length > 0)
            {
                IntVar CONST0 = new IntVar(store, "const0", 0, 0);
                IntVar CONST1 = new IntVar(store, "const1", 1, 1);
                IntVar CONST2 = new IntVar(store, "const2", 2, 2);

                switch (markerType.toLowerCase())
                {
                    case "same":
                        for (int i = 0; i < aConstraint.length; i++)
                           for (int j = 0; j < aConstraint.length; j++)
                              if (i < j)
                                  store.impose(new XeqY(aConstraint[i], aConstraint[j]));
                        break;
                    case "distinct":
                        store.impose(new Alldistinct(aConstraint));
                        break;
                    case "even":
                        for (int i = 0; i < aConstraint.length; i++)
                            store.impose(new XmodYeqZ(aConstraint[i], CONST2, CONST0));
                        break;
                    case "odd":
                        for (int i = 0; i < aConstraint.length; i++)
                            store.impose(new XmodYeqZ(aConstraint[i], CONST2, CONST1));
                        break;
                }
            }

        if (useDiagonalConstraint)
        {
            // Diagonal constraint
            IntVar[][] diaConstraint = generateDiagonalContraintMatrix(elements);
            for (IntVar[] aConstraint : diaConstraint)
            store.impose(new Alldistinct(aConstraint));
        }

    }

    ///
    /// Search for solution
    ///

    public boolean searchSmallestDomain()
    {
        SelectChoicePoint select = new SimpleSelect(vars.toArray(new IntVar[1]), new SmallestDomain(), new IndomainMin());

        label = new DepthFirstSearch();
        label.setPrintInfo(false);

        return label.labeling(store, select);
    }

    public boolean searchAll()
    {
        SelectChoicePoint select = new SimpleSelect(vars.toArray(new IntVar[1]), new SmallestDomain(), new IndomainRandom());

        label = new DepthFirstSearch();
        label.setPrintInfo(false);
        label.getSolutionListener().searchAll(true);

        boolean result = label.labeling(store, select);
        return result;
    }
}
