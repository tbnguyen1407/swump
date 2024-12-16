package swump.core.generator;

import swump.core.multiplayer.powerups.HintPowerUp;
import swump.core.multiplayer.powerups.PowerUp;
import swump.core.multiplayer.powerups.TakeOverPowerUp;
import swump.core.multiplayer.powerups.TimePowerUp;
import swump.core.multiplayer.powerups.TryPowerUp;

public class PowerUpGenerator {
    // region public methods

    public static PowerUp generate() {
        int random = RandomGenerator.getRandomPowerUpVal();
        switch (random) {
            case 0:
                return (PowerUp) new TakeOverPowerUp();
            case 1:
                return (PowerUp) new HintPowerUp();
            case 2:
                return (PowerUp) new TryPowerUp();
            case 3:
                return (PowerUp) new TimePowerUp();
        }
        return null;
    }

    // endregion
}
