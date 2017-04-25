package sk.uniza.fri.fant0m.bomberman;

/**
 * Fire tile class.
 * @author fant0m
 */
public class Fire extends Tile {
    /**
     * Animation of fire.
     */
    private Animation fire;
    /**
     * Piece of flame.
     */
    public static final int TYPE_FIRE_1 = 20;
    /**
     * Piece of flame.
     */
    public static final int TYPE_FIRE_2 = 21;
    /**
     * Piece of flame.
     */
    public static final int TYPE_FIRE_3 = 22;
    /**
     * Piece of flame.
     */
    public static final int TYPE_FIRE_4 = 23;
    /**
     * Piece of flame.
     */
    public static final int TYPE_FIRE_5 = 24;
    /**
     * Piece of flame.
     */
    public static final int TYPE_FIRE_6 = 25;
    /**
     * Piece of flame.
     */
    public static final int TYPE_FIRE_7 = 26;
    /**
     * Display time of dying enemy animation.
     */
    public static final int A_FIRE = 80;
    /**
     * Sprite width and height.
     */
    public static final int SPRITE = 16;

    /**
     * Fire constructor.
     * @param x x
     * @param y y
     * @param type type
     * @param walkable is it walkable
     * @param g game instance
     */
    public Fire(
        final int x,
        final int y,
        final int type,
        final boolean walkable,
        final Game g
    ) {
        super(x, y, type, walkable);
        final int move = 16;

        if (type == TYPE_FIRE_1) {
            final int start = 326;
            fire = new Animation();
            fire.addFrame(
                g.getImageFromSprite(start, SPRITE, SPRITE, SPRITE), A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(start + move, SPRITE, SPRITE, SPRITE),
                A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(start + move * 2, SPRITE, SPRITE, SPRITE),
                A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(
                    start + SPRITE + move, SPRITE, SPRITE, SPRITE
                ), A_FIRE
            );
            fire.setMoving(true);
        } else if (type == TYPE_FIRE_2) {
            final int start = 454;
            fire = new Animation();
            fire.addFrame(
                g.getImageFromSprite(start, SPRITE, SPRITE, SPRITE), A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(start + move, SPRITE + 1, SPRITE, SPRITE),
                A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(start + move * 2, SPRITE, SPRITE, SPRITE),
                A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(
                    start + SPRITE + move, SPRITE, SPRITE, SPRITE
                ), A_FIRE
            );
            fire.setMoving(true);
        } else if (type == TYPE_FIRE_3) {
            final int start = 582;
            fire = new Animation();
            fire.addFrame(
                g.getImageFromSprite(start, SPRITE, SPRITE, SPRITE), A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(start + move, SPRITE, SPRITE, SPRITE),
                A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(start + move * 2, SPRITE, SPRITE, SPRITE),
                A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(
                    start + SPRITE + move, SPRITE, SPRITE, SPRITE
                ), A_FIRE
            );
            fire.setMoving(true);
        } else if (type == TYPE_FIRE_4) {
            final int start = 390;
            fire = new Animation();
            fire.addFrame(
                g.getImageFromSprite(start, SPRITE * 2, SPRITE, SPRITE), A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(start + move, SPRITE * 2, SPRITE, SPRITE),
                A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(
                    start + move * 2, SPRITE * 2, SPRITE, SPRITE
                ), A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(
                    start + SPRITE + move, SPRITE * 2, SPRITE, SPRITE
                ), A_FIRE
            );
            fire.setMoving(true);
        } else if (type == TYPE_FIRE_5) {
            final int start = 518;
            fire = new Animation();
            fire.addFrame(
                g.getImageFromSprite(start, SPRITE, SPRITE, SPRITE), A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(start + move, SPRITE, SPRITE, SPRITE),
                A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(start + move * 2, SPRITE, SPRITE, SPRITE),
                A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(
                    start + SPRITE + move, SPRITE, SPRITE, SPRITE
                ), A_FIRE
            );
            fire.setMoving(true);
        } else if (type == TYPE_FIRE_6) {
            final int start = 390;
            fire = new Animation();
            fire.addFrame(
                g.getImageFromSprite(start, SPRITE, SPRITE, SPRITE), A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(start + move, SPRITE, SPRITE, SPRITE),
                A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(start + move * 2, SPRITE, SPRITE, SPRITE),
                A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(
                    start + SPRITE + move, SPRITE, SPRITE, SPRITE
                ), A_FIRE
            );
            fire.setMoving(true);
        } else if (type == TYPE_FIRE_7) {
            final int start = 326;
            fire = new Animation();
            fire.addFrame(
                g.getImageFromSprite(start, SPRITE * 2, SPRITE, SPRITE), A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(
                    start + move, SPRITE * 2, SPRITE, SPRITE
                ), A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(
                    start + move * 2, SPRITE * 2, SPRITE, SPRITE
                ), A_FIRE
            );
            fire.addFrame(
                g.getImageFromSprite(
                    start + SPRITE + move, SPRITE * 2, SPRITE, SPRITE
                ), A_FIRE
            );
            fire.setMoving(true);
        }
    }

    /**
     * Update animation.
     * @param time time
     */
    public final void update(final int time) {
        fire.update(time);
    }

    /**
     * Get fire animation.
     * @return animation
     */
    public final Animation getFire() {
        return fire;
    }
}
