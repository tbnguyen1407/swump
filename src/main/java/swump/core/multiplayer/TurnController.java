package swump.core.multiplayer;

import swump.core.SudokuLogic;
import swump.core.Timer;

public class TurnController implements Runnable {
    // region fields

    private static Timer timer = Timer.getInstance();
    private static TurnController instance;

    // endregion

    // region constructors

    protected TurnController() {
    }

    public static TurnController getInstance() {
        if (instance == null) {
            instance = new TurnController();
        }
        return instance;
    }

    // endregion

    // region public methods

    // checks if timeLimit has been reached or exceeded
    public boolean checkExceed(long elapsedTime, long timeLimit) {
        return elapsedTime >= timeLimit;
    }

    public void run() {
        while (!Thread.interrupted()) {
            if (checkExceed(timer.getElapsedTime(), SudokuLogic.getInstance().getTurnTime())) {
                SudokuLogic.getInstance().goNextTurn();
                timer.startTimer();
            }
        }
    }

    // endregion
}
