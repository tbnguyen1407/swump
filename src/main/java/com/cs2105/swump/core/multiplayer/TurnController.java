package com.cs2105.swump.core.multiplayer;

import com.cs2105.swump.core.SudokuLogic;
import com.cs2105.swump.core.Timer;

public class TurnController implements Runnable
{
    static Timer timer = Timer.getInstance();
    public static final TurnController INSTANCE = new TurnController();

    protected TurnController()
    {
    }

    public static TurnController getInstance()
    {
        return TurnController.INSTANCE;
    }

    /**
     * checks if timeLimit has been reached or exceeded
     */
    public boolean checkExceed(long elapsedTime, long timeLimit)
    {
        return elapsedTime >= timeLimit;
    }

    public void run()
    {
        while (!Thread.interrupted())
        {
            if (checkExceed(timer.getElapsedTime(), SudokuLogic.getInstance().getTurnTime()))
            {
                SudokuLogic.getInstance().goNextTurn();
                timer.startTimer();
            }
        }
    }
}
