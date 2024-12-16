package swump.core.multiplayer.powerups;

import swump.core.Player;

public class TryPowerUp implements PowerUp {
    // region fields

    private Player player;

    // endregion

    // region constructors

    public TryPowerUp() {
    }

    // endregion

    // region public methods

    public Type getType() {
        return PowerUp.Type.TRY;
    }

    public boolean use() {
        int newTurn = player.getTurnTries() + 1;
        player.setTurnTries(newTurn);
        return true;
    }

    @Override
    public void setUser(Player p) {
        this.player = p;
    }

    // endregion
}
