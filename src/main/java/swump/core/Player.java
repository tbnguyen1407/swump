package swump.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import swump.core.multiplayer.Score;
import swump.core.multiplayer.powerups.PowerUp;
import swump.core.multiplayer.powerups.TakeOverPowerUp;

public class Player {
    // region fields

    private Score score;
    private long timeGiven;
    private Color color;
    private String name;
    private int hints = 0;
    private int allowedTries = 0;
    private int numTakeOverPowerUp = 0;
    private int numHintPowerUp = 0;
    private int numTryPowerUp = 0;
    private int numTimePowerUp = 0;
    private List<PowerUp> powerUpStore = new ArrayList<PowerUp>();

    // endregion

    // region constructors

    public Player() {
        score = new Score();
    }

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        score = new Score();
    }

    // endregion

    // region accessors

    public int getNumTakeOverPowerUp() {
        return numTakeOverPowerUp;
    }

    public int getNumHintPowerUp() {
        return numHintPowerUp;
    }

    public int getNumTimePowerUp() {
        return numTimePowerUp;
    }

    public int getNumTryPowerUp() {
        return numTryPowerUp;
    }

    public void setTimeGiven(long time) {
        timeGiven = time;
    }

    public long getTimeGiven() {
        return timeGiven;
    }

    public String getPlayerName() {
        return name;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public Score getScore() {
        return score;
    }

    public void setColor(Color playerColor) {
        this.color = playerColor;
    }

    public Color getColor() {
        return color;
    }

    public void setNumHints(int hints) {
        this.hints = hints;
    }

    public int getNumHints() {
        return this.hints;
    }

    public void setTurnTries(int allowedTries) {
        this.allowedTries = allowedTries;
    }

    public int getTurnTries() {
        return allowedTries;
    }

    // endregion

    // region public methods

    public boolean usePowerUp(PowerUp.Type t) throws NullPointerException {
        PowerUp p = null;

        switch (t) {
            case HINT:
                this.numHintPowerUp--;
                p = retrievePowerUp(PowerUp.Type.HINT);
                break;
            case TRY:
                this.numTryPowerUp--;
                p = retrievePowerUp(PowerUp.Type.TRY);
                break;
            case TIME:
                this.numTimePowerUp--;
                p = retrievePowerUp(PowerUp.Type.TIME);
                break;
            default:
                break;
        }
        if (p != null) {
            p.setUser(this);
            return p.use();
        }
        return false;
    }

    public void takeOver(int x, int y) {
        TakeOverPowerUp p = null;
        this.numTakeOverPowerUp--;
        p = (TakeOverPowerUp) retrievePowerUp(PowerUp.Type.TAKE_OVER);
        p.setTargetPosX(x);
        p.setTargetPosY(y);
        p.use();
    }

    public PowerUp retrievePowerUp(PowerUp.Type t) {
        for (PowerUp p : powerUpStore) {
            if (p.getType() == t) {
                powerUpStore.remove(p);
                return p;
            }
        }
        return null;
    }

    public boolean addPowerup(PowerUp pu) {

        if (powerUpStore.size() < 3 && pu != null) {
            powerUpStore.add(pu);
            switch (pu.getType()) {
                case HINT:
                    this.numHintPowerUp++;
                    break;
                case TRY:
                    this.numTryPowerUp++;
                    break;
                case TAKE_OVER:
                    this.numTakeOverPowerUp++;
                    break;
                case TIME:
                    this.numTimePowerUp++;
                    break;
            }
            return true;
        } else
            return false;
    }

    // endregion
}
