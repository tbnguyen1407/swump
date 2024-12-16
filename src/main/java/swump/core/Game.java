package swump.core;

public class Game {
    // region fields

    private Puzzle puzzle;
    private int timeElapsed;
    private String gameName;
    private String timeStamped;

    // endregion

    // region constructors

    public Game(Puzzle puzzle, int timeElapsed, String gameName) {
        this.puzzle = puzzle;
        this.timeElapsed = timeElapsed;
        this.gameName = gameName;
    }

    // endregion

    // region accessors

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public int getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(int timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getTimeStamped() {
        return timeStamped;
    }

    public void setTimeStamped(String timeStamped) {
        this.timeStamped = timeStamped;
    }

    // endregion
}
