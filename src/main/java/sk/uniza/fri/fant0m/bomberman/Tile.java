package sk.uniza.fri.fant0m.bomberman;

/**
 * Game tile (block) class.
 * @author fant0m
 */
public class Tile {
    /**
     * X coordination.
     */
    private int x;
    /**
     * Y coordination.
     */
    private int y;
    /**
     * Type of tile.
     * (
     *  0-grass, 1-wall, 2-destroyable block, 3-bomb,
     *  20-26-fire, 30-exit, 31-33-power-up
     * )
     */
    private int type;
    /**
     * Can player walk through the tile?
     */
    private boolean walkable;

    /**
     * Type grass.
     */
    public static final int TYPE_GRASS = 0;
    /**
     * Type wall.
     */
    public static final int TYPE_WALL = 1;
    /**
     * Type block.
     */
    public static final int TYPE_BLOCK = 2;
    /**
     * Type bomb.
     */
    public static final int TYPE_BOMB = 3;

    /**
     * Tile constructor.
     * @param x horizontal position
     * @param y vertical position
     * @param type tile type
     * @param walkable is it walkable
     */
    public Tile(
        final int x, final int y, final int type, final boolean walkable
    ) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.walkable = walkable;
    }

    /**
     * X getter.
     * @return x
     */
    public final int getX() {
        return x;
    }

    /**
     * Y getter.
     * @return y
     */
    public final int getY() {
        return y;
    }

    /**
     * Type getter.
     * @return type
     */
    public final int getType() {
        return type;
    }

    /**
     * Walkable getter.
     * @return walkable
     */
    public final boolean isWalkable() {
        return walkable;
    }

    /**
     * Walkable setter.
     * @param walkable is it walkable
     */
    public final void setWalkable(final boolean walkable) {
        this.walkable = walkable;
    }
}
