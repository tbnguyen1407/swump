package com.cs2105.swump.core.multiplayer.powerups;

import com.cs2105.swump.core.Player;

public interface PowerUp {
    enum Type {
        TRY,
        TAKE_OVER,
        HINT,
        TIME
    }

    public boolean use();

    public Type getType();

    public void setUser(Player p);
}
