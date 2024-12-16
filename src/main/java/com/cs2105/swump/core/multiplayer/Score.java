package com.cs2105.swump.core.multiplayer;

public class Score {
    // region fields

    public int numCellsOwned = 0;
    public int numHintsUsed = 0;

    // endregion

    // region constructors

    public Score() {
    }

    public Score(int numCellsOwned, int numHintsUsed) {
        this.numCellsOwned = numCellsOwned;
        this.numHintsUsed = numHintsUsed;
    }

    // endregion

    // region accessors

    public void setCellsOwned(int num) {
        numCellsOwned = num;
    }

    public int getCellsOwned() {
        return numCellsOwned;
    }

    public void setHintsUsed(int hintsUsed) {
        this.numHintsUsed = hintsUsed;
    }

    public int getHintsUsed() {
        return numHintsUsed;
    }

    // endregion

    // region public methods

    public void increaseCellsOwned() {
        numCellsOwned++;
    }

    public void decreaseCellsOwned() {
        numCellsOwned--;
    }

    // endregion
}
