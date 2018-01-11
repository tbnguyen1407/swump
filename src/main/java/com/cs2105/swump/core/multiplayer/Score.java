package com.cs2105.swump.core.multiplayer;

public class Score
{
    public int numCellsOwned = 0;
    public int numHintsUsed = 0;

    public Score()
    {
    }

    public Score(int numCellsOwned, int numHintsUsed)
    {
        this.numCellsOwned = numCellsOwned;
        this.numHintsUsed = numHintsUsed;
    }

    public void setCellsOwned(int num)
    {
        numCellsOwned = num;
    }

    public int getCellsOwned()
    {
        return numCellsOwned;
    }

    public void setHintsUsed(int hintsUsed)
    {
        this.numHintsUsed = hintsUsed;
    }

    public int getHintsUsed()
    {
        return numHintsUsed;
    }

    public void increaseCellsOwned()
    {
        numCellsOwned++;
    }

    public void decreaseCellsOwned()
    {
        numCellsOwned--;
    }
}
