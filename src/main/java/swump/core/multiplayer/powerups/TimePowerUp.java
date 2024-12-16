package swump.core.multiplayer.powerups;

import swump.core.Player;
import swump.core.Timer;

public class TimePowerUp implements PowerUp {
    // region fields

    private Player player;

    // endregion

    // region constructors

    public TimePowerUp() {
    }

    // endregion

    // region public methods

    public Type getType() {
        return PowerUp.Type.TIME;
    }

    public void setUser(Player p) {
        this.player = p;
    }

    @Override
    public boolean use() {
        if (player != null) {
            Timer.getInstance().addToTime(-10000);
            return true;
        }
        return false;
    }

    // endregion
}
