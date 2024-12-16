package swump.core;

public class Timer implements Runnable {
    // region fields

    private long startTime = 0;
    private long stopTime = 0;
    private long elapsedTime;
    private long pausedTime = 0;
    private long offsetTime = 0;
    private static Timer instance = null;

    // endregion

    // region constructors

    protected Timer() {
    }

    public static Timer getInstance() {
        if (instance == null)
            instance = new Timer();
        return instance;
    }

    // endregion

    // region public methods

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        stopTime = System.currentTimeMillis();
        elapsedTime = pausedTime + (stopTime - startTime) + offsetTime; // why does it keep increasing?!?!?!
        return elapsedTime;
    }

    public void resetTimer() {
        startTime = System.currentTimeMillis();
        stopTime = 0;
        elapsedTime = 0;
        pausedTime = 0;
        offsetTime = 0;
    }

    public void run() {
        startTimer();
    }

    public void addToTime(long addedTime) {
        offsetTime += addedTime;
    }

    // endregion
}
