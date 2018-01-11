package com.cs2105.swump.core;

public class Cell
{
    private int value = 0;
    private int[] pencilMarks;
    private boolean isFilled;
    private Player owner;
    private int regionId = 0;
    private boolean isMarked;

    // Constructor for Solver cells
    public Cell(int val, boolean filled, int regionid)
    {
        this.value = val;
        this.isFilled = filled;
        this.regionId = regionid;
    }

    public Cell(int val, boolean filled)
    {
        this.isFilled = filled;
        this.value = val;
        this.pencilMarks = new int[9];
    }

    public boolean isFilled()
    {
        return this.isFilled;
    }

    public int getValue()
    {
        return this.value;
    }

    public boolean isMarked()
    {
        return this.isMarked;
    }

    public void setMarked()
    {
        this.isMarked = !this.isMarked;
    }

    public void setValue(int val)
    {
        this.value = val;
    }

    public int[] getPencilMarks()
    {
        return this.pencilMarks;
    }

    public void setPencilMarks(int[] pencilMarks)
    {
        this.pencilMarks = pencilMarks;
    }

    public void addPencilmark(int mark)
    {
        this.pencilMarks[mark - 1] = 1;
    }

    public void setFilled()
    {
        this.isFilled = true;
    }

    public void setUnfilled()
    {
        this.isFilled = false;
    }

    public void setOwner(Player owner)
    {
        this.owner = owner;
    }

    public Player getOwner()
    {
        return this.owner;
    }

    public void setRegionId(int regionid)
    {
        this.regionId = regionid;
    }

    public int getRegionId()
    {
        return this.regionId;
    }
}
