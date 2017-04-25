package sk.uniza.fri.fant0m.bomberman;

/**
 * Special type of tile class.
 * @author fant0m
 */
public class SpecialTile extends Tile {
    /**
     * Special tile.
     */
    private int special;
    /**
     * Special exit.
     */
    public static final int EXIT = 1;
    /**
     * Special speed.
     */
    public static final int SPEED = 2;
    /**
     * Special bomb.
     */
    public static final int BOMB = 3;
    /**
     * Special fire.
     */
    public static final int FIRE = 4;
    /**
     * Type exit.
     */
    public static final int TYPE_EXIT = 30;
    /**
     * Type exit.
     */
    public static final int TYPE_SPEED = 31;
    /**
     * Type exit.
     */
    public static final int TYPE_BOMB = 32;
    /**
     * Type exit.
     */
    public static final int TYPE_FIRE = 33;

    /**
     * Constructor.
     * @param x x position
     * @param y y position
     * @param type type of field
     * @param walkable is it walkable
     * @param special type
     */
    public SpecialTile(
        final int x, final int y, final int type,
        final boolean walkable, final int special
    ) {
        super(x, y, type, walkable);
        this.special = special;
    }

    /**
     * Type getter.
     * @return type
     */
    public final int getSpecial() {
        return special;
    }
}
