package sk.uniza.fri.fant0m.bomberman;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * Bomb class.
 * @author fant0m
 */
public class Bomb extends Tile implements ActionListener {
    /**
     * Size of bomb flame.
     */
    private int bombFire;
    /**
     * Is it still ticking.
     */
    private boolean bombed;

    /**
     * Player that laid a bomb.
     */
    private Player player;
    /**
     * Bomb timer.
     */
    private Timer timer;

    /**
     * Milliseconds.
     */
    private static final int MILISECONDS = 1000;

    /**
     * Bomb constructor.
     * @param x horizontal position
     * @param y vertical position
     * @param type tile type (3)
     * @param walkable is it walkable
     *  (when player puts a bomb he needs to go through it for the first time)
     * @param bombTime time of explosion
     * @param bombFire size of fire
     * @param player player that laid a bomb
     */
    public Bomb(
        final int x,
        final int y,
        final int type,
        final boolean walkable,
        final int bombTime,
        final int bombFire,
        final Player player
    ) {
        super(x, y, type, walkable);
        this.bombFire = bombFire;
        this.player = player;
        this.bombed = false;

        timer = new Timer(bombTime * MILISECONDS, this);
        timer.start();
    }

    /**
     * Bomb explosion.
     * @param e event
     */
    @Override
    public final void actionPerformed(final ActionEvent e) {
        timer.stop();
        if (!bombed) {
            player.boom(super.getX(), super.getY(), bombFire);
            setBombed(true);
        }
    }

    /**
     * Get size of fire.
     * @return size
     */
    public final int getBombFire() {
        return bombFire;
    }

    /**
     * Is it bombed.
     * @return bombed
     */
    public final boolean isBombed() {
        return bombed;
    }

    /**
     * Set bombed.
     * @param bombed bombed
     */
    public final void setBombed(final boolean bombed) {
        this.bombed = bombed;
    }
}
