package com.cs2105.swump.core.generator;

import com.cs2105.swump.core.multiplayer.powerups.*;

public class PowerUpGenerator
{
    public static PowerUp generate()
    {
        int random = RandomGenerator.getRandomPowerUpVal();
        switch (random)
        {
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
}
