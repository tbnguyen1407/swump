package com.cs2105.swump.core;

public class Timer implements Runnable
{
    private long startTime = 0;
    private long stopTime = 0;
    private long elapsedTime;
    private long pausedTime = 0;
    private long offsetTime = 0;
    private static Timer INSTANCE = null;

    protected Timer()
    {
    }

    public static Timer getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new Timer();
        return INSTANCE;
    }

    /**
     * Starts the timer *
     */
    public void startTimer()
    {
        startTime = System.currentTimeMillis();
    }

    /**
     * @return total elapsed time from when timer was started
     */
    public long getElapsedTime()
    {
        stopTime = System.currentTimeMillis();
        elapsedTime = pausedTime + (stopTime - startTime) + offsetTime; //why does it keep increasing?!?!?!
        return elapsedTime;
    }

    /**
     * Resets the timer; everything is reset to zero
     */
    public void resetTimer()
    {
        startTime = System.currentTimeMillis();
        stopTime = 0;
        elapsedTime = 0;
        pausedTime = 0;
        offsetTime = 0;
    }


    public void run()
    {
        startTimer();
    }

    /**
     * used for imposing penalty, logic will handle impose penalty method
     */
    public void addToTime(long addedTime)
    {
        offsetTime += addedTime;
    }
}
