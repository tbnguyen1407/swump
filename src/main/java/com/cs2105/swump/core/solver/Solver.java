package com.cs2105.swump.core.solver;

import java.util.ArrayList;
import java.util.List;

import org.jacop.constraints.Alldistinct;
import org.jacop.constraints.XeqY;
import org.jacop.constraints.XmodYeqZ;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.IndomainRandom;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;

public class Solver {
    // region fields

    private IntVar[][] elements;
    private ArrayList<IntVar> vars = new ArrayList<IntVar>();
    private Store store = new Store();
    private Search<IntVar> label;

    // endregion

    // region accessors

    public IntVar[][] getElements() {
        return elements;
    }

    public Search<IntVar> getLabel() {
        return label;
    }

    // endregion

    // region public methods

    // model puzzle with custom region and markers
    public void modelPuzzle(int[][] puzzleGrid, int[][] customRegionGrid, String markerType, boolean[][] markerGrid,
            boolean useDiagonalConstraint) {
        store = new Store();
        vars = new ArrayList<IntVar>();

        elements = new IntVar[puzzleGrid.length][puzzleGrid.length];

        /** Creating variables and assigning constraints to them */
        for (int i = 0; i < puzzleGrid.length; i++)
            for (int j = 0; j < puzzleGrid.length; j++)
                if (puzzleGrid[i][j] == 0) {
                    elements[i][j] = new IntVar(store, "CELL(" + i + ", " + j + ")", 1, 9);
                    vars.add(elements[i][j]);
                } else
                    elements[i][j] = new IntVar(store, "CELL(" + i + ", " + j + ")" + i + j, puzzleGrid[i][j],
                            puzzleGrid[i][j]);

        // Horizontal contraint
        for (int r = 0; r < puzzleGrid.length; r++) {
            store.impose(new Alldistinct(elements[r]));
        }

        // Vertical constraint
        for (int col = 0; col < puzzleGrid.length; col++) {
            IntVar[] curCol = new IntVar[puzzleGrid.length];
            for (int row = 0; row < puzzleGrid.length; row++) {
                curCol[row] = elements[row][col];
            }
            store.impose(new Alldistinct(curCol));
        }

        // Region constraint
        if (customRegionGrid != null) {
            IntVar[][] customRegConstraints = new IntVar[puzzleGrid.length][puzzleGrid.length];
            int[] regionIdxMatric = new int[puzzleGrid.length];
            // populate constraints
            for (int row = 0; row < puzzleGrid.length; row++) {
                for (int col = 0; col < puzzleGrid.length; col++) {
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
            int numRegions = puzzleGrid.length / 3;
            int regionSize = puzzleGrid.length / 3;
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
            IntVar[] diagonalA = new IntVar[puzzleGrid.length];
            IntVar[] diagonalB = new IntVar[puzzleGrid.length];

            for (int row = 0; row < puzzleGrid.length; row++) {
                diagonalA[row] = elements[row][row];
                diagonalB[row] = elements[row][puzzleGrid.length - row - 1];
            }

            store.impose(new Alldistinct(diagonalA));
            store.impose(new Alldistinct(diagonalB));
        }
    }

    // search for solution
    public boolean searchSmallestDomain() {
        SelectChoicePoint<IntVar> select = new SimpleSelect<>(
                vars.toArray(new IntVar[1]),
                new SmallestDomain<>(),
                new IndomainMin<>());

        label = new DepthFirstSearch<>();
        label.setPrintInfo(false);

        return label.labeling(store, select);
    }

    // search for all solutions
    public boolean searchAll() {
        SelectChoicePoint<IntVar> select = new SimpleSelect<>(
                vars.toArray(new IntVar[1]),
                new SmallestDomain<>(),
                new IndomainRandom<>());

        label = new DepthFirstSearch<>();
        label.setPrintInfo(false);
        label.getSolutionListener().searchAll(true);

        boolean result = label.labeling(store, select);
        return result;
    }

    // endregion
}
