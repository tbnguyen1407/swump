package swump.core.multiplayer.powerups;

import swump.core.Cell;
import swump.core.Player;
import swump.core.SudokuLogic;

public class TakeOverPowerUp implements PowerUp {
    // region fields

    private Player player;
    private Cell[][] grid = SudokuLogic.getInstance().getPuzzle().getGrid();
    private int posX;
    private int posY;

    // endregion

    // region constructors

    public TakeOverPowerUp() {
    }

    // endregion

    // region public methods

    public void setTargetPosX(int x) {
        posX = x;
    }

    public void setTargetPosY(int y) {
        posY = y;
    }

    public int getTargetPosX() {
        return posX;
    }

    public int getTargetPosY() {
        return posY;
    }

    public boolean use() {
        try {
            int x = getTargetPosX();
            int y = getTargetPosY();
            player = SudokuLogic.getInstance().getCurrentPlayer();
            if (grid[x][y].getOwner() != null && grid[x][y].getOwner() != player) {
                Player previousPlayer = grid[x][y].getOwner();
                previousPlayer.getScore().decreaseCellsOwned();
                player.getScore().increaseCellsOwned();
                grid[x][y].setOwner(player);
                SudokuLogic.getInstance().getPuzzle().setGrid(grid);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Type getType() {
        return PowerUp.Type.TAKE_OVER;
    }

    @Override
    public void setUser(Player p) {
        this.player = p;
    }

    // endregion
}
