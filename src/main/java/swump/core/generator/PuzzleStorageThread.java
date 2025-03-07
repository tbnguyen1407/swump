package swump.core.generator;

import swump.core.storage.Storage;

public class PuzzleStorageThread implements Runnable {
    // region fields

    private Storage storage;
    private int difficulty = 0;
    private final int BLANK = 0;
    private final int EASY = 38;
    private final int NORMAL = 33;
    private final int HARD = 28;

    // endregion

    // region constructors

    public PuzzleStorageThread(Storage storage, int difficulty) {
        this.storage = storage;
        this.difficulty = difficulty;
    }

    // endregion

    // region public methods

    public void setPuzzleDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getPuzzleDifficulty() {
        return difficulty;
    }

    public void run() {
        int givens = 0;
        PuzzleGenerator generator = PuzzleGenerator.getInstance();
        switch (difficulty) {
            case 0:
                givens = EASY;
                break;
            case 1:
                givens = NORMAL;
                break;
            case 2:
                givens = HARD;
                break;
            case 3:
                givens = BLANK;
                break;
        }
        try {
            if (storage.getNumberOfPuzzles(difficulty) > 500)
                Thread.sleep(1800000);
            else {
                while (!Thread.interrupted()) {
                    Thread.sleep(1000);
                    generator.generate(givens);
                    if (generator.getPuzzleSolution() == null)
                        return;
                    else
                        storage.addPuzzle(generator.getPuzzleSolution(), generator.getPuzzle(), difficulty);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // endregion
}
