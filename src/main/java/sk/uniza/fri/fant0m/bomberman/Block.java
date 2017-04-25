package sk.uniza.fri.fant0m.bomberman;

/**
 * Block tile class. Block can be destroyed by a bomb.
 * @author fant0m
 */
public class Block extends Tile {
    /**
     * Is under the block hidden exit.
     */
    private boolean hasExit = false;

    /**
     * Block constructor.
     * @param x x
     * @param y y
     * @param type type
     * @param walkable is it walkable
     */
    public Block(
        final int x,
        final int y,
        final int type,
        final boolean walkable
    ) {
        super(x, y, type, walkable);
    }

    /**
     * Block constructor with exit.
     * @param x x
     * @param y y
     * @param type type
     * @param walkable is it walkable
     * @param hasExit has exit
     */
    public Block(
        final int x,
        final int y,
        final int type,
        final boolean walkable,
        final boolean hasExit
    ) {
        super(x, y, type, walkable);
        this.hasExit = hasExit;
    }

    /**
     * Set exit.
     * @param hasExit exit
     */
    public final void setHasExit(final boolean hasExit) {
        this.hasExit = hasExit;
    }

    /**
     * Has exit.
     * @return has exit
     */
    public final boolean hasExit() {
        return hasExit;
    }
}
