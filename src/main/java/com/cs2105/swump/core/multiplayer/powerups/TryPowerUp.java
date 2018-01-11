package com.cs2105.swump.core.multiplayer.powerups;

import com.cs2105.swump.core.Player;

public class TryPowerUp implements PowerUp
{

    private Player player;

    public TryPowerUp()
    {
    }

    public Type getType()
    {
        return PowerUp.Type.TRY;
    }

    public boolean use()
    {
        int newTurn = player.getTurnTries() + 1;
        player.setTurnTries(newTurn);
        return true;
    }

    @Override
    public void setUser(Player p)
    {
        this.player = p;
    }
}
