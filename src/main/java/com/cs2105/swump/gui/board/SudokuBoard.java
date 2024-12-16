package com.cs2105.swump.gui.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class SudokuBoard extends JPanel {
    // region fields

    private static final long serialVersionUID = -6804658013310928037L;
    private static final int GRID_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;

    private CellUI[][] cellMatrix;
    private JPanel[][] subGrid;

    private Color bgColor1;
    private Color bgColor2;

    // endregion

    // region constructors

    public SudokuBoard() {
        createSudokuBoard(null);
        this.setVisible(true);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // endregion

    // region public methods

    public void changeTheme(Color color1, Color color2) {
        this.bgColor1 = color1;
        this.bgColor2 = color2;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (((j >= 0 && j <= 2) || (j >= 6 && j <= 9)) && ((i >= 0 && i <= 2) || (i >= 6 && i <= 9)))
                    cellMatrix[i][j].setBackground(bgColor1);
                else if ((j >= 3 && j <= 5) && (i >= 3 && i <= 5))
                    cellMatrix[i][j].setBackground(bgColor1);
                else
                    cellMatrix[i][j].setBackground(bgColor2);
            }
        }

        this.repaint();
    }

    public void resetTheme() {
        this.changeTheme(bgColor1, bgColor2);
    }

    // endregion

    // region private methods

    private void createSudokuBoard(int[][] array) {
        cellMatrix = new CellUI[GRID_SIZE][GRID_SIZE];
        subGrid = new JPanel[SUBGRID_SIZE][SUBGRID_SIZE];

        if (array == null)
            for (int i = 0; i < GRID_SIZE; i++)
                for (int j = 0; j < GRID_SIZE; j++)
                    cellMatrix[i][j] = new CellUI(i, j, 1, false);
        else
            for (int i = 0; i < GRID_SIZE; i++)
                for (int j = 0; j < GRID_SIZE; j++)
                    cellMatrix[i][j] = new CellUI(i, j, 2, true);

        this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        this.setLayout(new GridLayout(3, 3));
        this.setMinimumSize(new Dimension(500, 500));

        for (int i = 0; i < subGrid.length; i++)
            for (int j = 0; j < subGrid[i].length; j++) {
                subGrid[i][j] = new JPanel(new GridLayout(3, 3));
                subGrid[i][j].setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
                this.add(subGrid[i][j]);
            }

        for (int i = 0; i < cellMatrix.length; i++)
            for (int j = 0; j < cellMatrix[i].length; j++)
                subGrid[i / 3][j / 3].add(cellMatrix[i][j]);

        changeTheme(new Color(222, 235, 247), new Color(158, 202, 225));
    }

    // endregion
}
