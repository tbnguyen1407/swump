package com.cs2105.swump.core.solver;

import org.jacop.constraints.*;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.*;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    public IntVar[][] elements;
    private ArrayList<IntVar> vars = new ArrayList<IntVar>();
    private Store store = new Store();
    public Search label;

    ///
    /// Puzzle modeling
    ///
    public void modelPuzzle(int[][] puzzle, int[][] customRegionGrid, String markerType, boolean[][] markerGrid,
            boolean useDiagonalConstraint) {
        store = new Store();
        vars = new ArrayList<IntVar>();

        elements = new IntVar[puzzle.length][puzzle.length];

        /** Creating variables and assigning constraints to them */
        for (int i = 0; i < puzzle.length; i++)
            for (int j = 0; j < puzzle.length; j++)
                if (puzzle[i][j] == 0) {
                    elements[i][j] = new IntVar(store, "CELL(" + i + ", " + j + ")", 1, 9);
                    vars.add(elements[i][j]);
                } else
                    elements[i][j] = new IntVar(store, "CELL(" + i + ", " + j + ")" + i + j, puzzle[i][j],
                            puzzle[i][j]);

        // Horizontal contraint
        for (int r = 0; r < puzzle.length; r++) {
            store.impose(new Alldistinct(elements[r]));
        }

        // Vertical constraint
        for (int col = 0; col < puzzle.length; col++) {
            IntVar[] curCol = new IntVar[puzzle.length];
            for (int row = 0; row < puzzle.length; row++) {
                curCol[row] = elements[row][col];
            }
            store.impose(new Alldistinct(curCol));
        }

        // Region constraint
        if (customRegionGrid != null) {
            IntVar[][] customRegConstraints = new IntVar[puzzle.length][puzzle.length];
            int[] regionIdxMatric = new int[puzzle.length];
            // populate constraints
            for (int row = 0; row < puzzle.length; row++) {
                for (int col = 0; col < puzzle.length; col++) {
                    int regionIndex = customRegionGrid[row][col] - 1;
                    customRegConstraints[regionIndex][regionIdxMatric[regionIndex]] = elements[row][col];
                    regionIdxMatric[regionIndex]++;
                }
            }
            // impose constraints
            for (IntVar[] aConstraint : customRegConstraints) {
                store.impose(new Alldistinct(aConstraint));
            }
        } else { // default region constraint
            int numRegions = puzzle.length / 3;
            int regionSize = puzzle.length / 3;
            for (int regRow = 0; regRow < numRegions; regRow++) {
                for (int regCol = 0; regCol < numRegions; regCol++) {
                    List<IntVar> reg = new ArrayList<>();
                    for (int col = 0; col < regionSize; col++) {
                        for (int row = 0; row < regionSize; row++) {
                            reg.add(elements[regRow * regionSize + col][regCol * regionSize + row]);
                        }
                    }
                    store.impose(new Alldistinct(reg));
                }
            }
        }

        // Marked constraint
        if (markerGrid != null) {
            List<IntVar> markedVars = new ArrayList<>();
            for (int i = 0; i < markerGrid.length; i++)
                for (int j = 0; j < markerGrid[i].length; j++)
                    if (markerGrid[i][j])
                        markedVars.add(elements[i][j]);

            IntVar[][] marConstraint = new IntVar[1][markedVars.size()];
            for (int i = 0; i < markedVars.size(); i++)
                marConstraint[0][i] = markedVars.get(i);

            for (IntVar[] aConstraint : marConstraint) {
                if (aConstraint != null && aConstraint.length > 0) {
                    IntVar CONST0 = new IntVar(store, "const0", 0, 0);
                    IntVar CONST1 = new IntVar(store, "const1", 1, 1);
                    IntVar CONST2 = new IntVar(store, "const2", 2, 2);

                    switch (markerType.toLowerCase()) {
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
            }
        }

        // Diagonal constraint
        if (useDiagonalConstraint) {
            IntVar[] diagonalA = new IntVar[puzzle.length];
            IntVar[] diagonalB = new IntVar[puzzle.length];

            for (int row = 0; row < puzzle.length; row++) {
                diagonalA[row] = elements[row][row];
                diagonalB[row] = elements[row][puzzle.length - row - 1];
            }

            store.impose(new Alldistinct(diagonalA));
            store.impose(new Alldistinct(diagonalB));
        }
    }

    ///
    /// Search for solution
    ///

    public boolean searchSmallestDomain() {
        SelectChoicePoint select = new SimpleSelect(vars.toArray(new IntVar[1]), new SmallestDomain(),
                new IndomainMin());

        label = new DepthFirstSearch();
        label.setPrintInfo(false);

        return label.labeling(store, select);
    }

    public boolean searchAll() {
        SelectChoicePoint select = new SimpleSelect(vars.toArray(new IntVar[1]), new SmallestDomain(),
                new IndomainRandom());

        label = new DepthFirstSearch();
        label.setPrintInfo(false);
        label.getSolutionListener().searchAll(true);

        boolean result = label.labeling(store, select);
        return result;
    }
}
