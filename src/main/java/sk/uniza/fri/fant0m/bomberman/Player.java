package sk.uniza.fri.fant0m.bomberman;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

/**
 * Player class.
 * @author fant0m
 */
public class Player {
    /**
     * X position.
     */
    private double x = 0;
    /**
     * Y position.
     */
    private double y = 0;
    /**
     * X movement.
     */
    private double xMove = 0;
    /**
     * Y movement.
     */
    private double yMove = 0;
    /**
     * Is player alive.
     */
    private boolean isAlive = true;
    /**
     * Is player moving left.
     */
    private boolean movingLeft = false;
    /**
     * Is player moving right.
     */
    private boolean movingRight = false;
    /**
     * Is player moving up.
     */
    private boolean movingUp = false;
    /**
     * Is player moving down.
     */
    private boolean movingDown = false;
    /**
     * List of bombs.
     */
    private ArrayList<Bomb> bombs = new ArrayList<Bomb>();
    /**
     * List of bombs being exploding.
     */
    private ArrayList<Bomb> gonnaBoom = new ArrayList<Bomb>();
    /**
     * List of blocks.
     */
    private ArrayList<Block> blocks = new ArrayList<Block>();
    /**
     * Number of walkable bombs.
     */
    private int numberOfWalkableBombs = 0;

    /**
     * Game instance.
     */
    private Game game;
    /**
     * Player speed.
     */
    private int playerSpeed;
    /**
     * Player bombs.
     */
    private int playerBombs;
    /**
     * Player lives.
     */
    private int playerHealth;
    /**
     * Size of bomb flame.
     */
    private int playerFire;
    /**
     * Collision rectangle.
     */
    private Rectangle collisionTile;
    /**
     * Bombs timer.
     */
    private ArrayList<Timer> timer;

    /**
     * The size of movement update on key stroke.
     */
    private static final double INCREMENT = 0.5;
    /**
     * Player image width.
     */
    public static final int PLAYER_WIDTH = 32;
    /**
     * Player image height.
     */
    public static final int PLAYER_HEIGHT = 32;
    /**
     * Maximum speed.
     */
    private static final int MAXIMUM_SPEED = 6;
    /**
     * Maximum number of bombs.
     */
    private static final int MAXIMUM_BOMBS = 6;
    /**
     * Maximum flame.
     */
    private static final int MAXIMUM_FIRE = 6;
    /**
     * Default speed.
     */
    private static final int DEFAULT_SPEED = 3;
    /**
     * Default number of bombs.
     */
    private static final int DEFAULT_BOMBS = 1;
    /**
     * Default life.
     */
    private static final int DEFAULT_LIFE = 1;
    /**
     * Default size of flame.
     */
    private static final int DEFAULT_FIRE = 1;
    /**
     * Player icon is centered in image.
     */
    public static final int CENTER_PLAYER_X = 3;
    /**
     * Player icon is centered in image.
     */
    public static final double CENTER_PLAYER_Y = 1;
    /**
     * Seconds until bomb explode.
     */
    private static final int BOMB_TIME = 2;

    /**
     * Player constructor.
     * @param game game
     */
    public Player(final Game game) {
        this.game = game;
        this.playerSpeed = DEFAULT_SPEED;
        this.playerBombs = DEFAULT_BOMBS;
        this.playerFire = DEFAULT_FIRE;
        this.playerHealth = DEFAULT_LIFE;
        this.timer = new ArrayList<>();
    }

    /**
     * Player position check.
     */
    public final void update() {
        if (isMovingDown() && isMovingUp()) {
            yMove = 0;
            setMovingDown(false);
            setMovingUp(false);
        }
        if (isMovingLeft() && isMovingRight()) {
            xMove = 0;
            setMovingLeft(false);
            setMovingRight(false);
        }
        if (game.getMap() != null && game.isRunning()) {
            twoArrowsUpdate();
        }
    }

    /**
     * Player position update. Collision control.
     */
    public final void twoArrowsUpdate() {
        Tile[][] map = game.getMap();
        /**
         * movement update
         */
        if (x + PLAYER_WIDTH + xMove >= map[0].length * Game.TILE_WIDTH) {
            x = map[0].length * Game.TILE_WIDTH - PLAYER_WIDTH;
        } else {
            x += xMove;
        }
        if (x + xMove < 0) {
            x = 0;
        }


        if (y + PLAYER_HEIGHT + yMove >= map.length * Game.TILE_HEIGHT) {
            y = map.length * Game.TILE_HEIGHT - PLAYER_HEIGHT;
        } else {
            y += yMove;
        }
        if (y + yMove < 0) {
            y = 0;
        }

        /**
         * Special items collision
         */
        SpecialTile specialTile = game.getCollision().specialCollision(x, y);
        if (specialTile != null) {
            int specialType = specialTile.getSpecial();
            if (specialType == SpecialTile.EXIT) {
                game.nextLevel();
            } else if (specialType == SpecialTile.SPEED) {
                if (playerSpeed + 1 <= MAXIMUM_SPEED) {
                    playerSpeed++;
                }
                setTile(specialTile.getX(), specialTile.getY(), 0, true);
            } else if (specialType == SpecialTile.BOMB) {
                if (playerBombs + 1 <= MAXIMUM_BOMBS) {
                    playerBombs++;
                }
                setTile(specialTile.getX(), specialTile.getY(), 0, true);
            } else if (specialType == SpecialTile.FIRE) {
                if (playerFire + 1 <= MAXIMUM_FIRE) {
                    playerFire++;
                }
                setTile(specialTile.getX(), specialTile.getY(), 0, true);
            }
        }

        /**
         * Bomb walkable setup
         */
        if (
            !game.getCollision().bombCollision(this)
            && numberOfWalkableBombs >= 1
        ) {
            if (collisionTile != null) {
                for (Bomb b : bombs) {
                    if (b.isWalkable()) {
                        b.setWalkable(false);
                        numberOfWalkableBombs--;
                    }
                }
            }
        }

        /**
         * Collision detection & fix
         */
        if (game.getCollision().collision(x, y)) {
            y -= yMove;
            if (game.getCollision().collision(x, y)) {
                x -= xMove;
                y += yMove;
                if (game.getCollision().collision(x, y)) {
                    y -= yMove;
                }
            }
        }
    }

    /**
     * Set player x position.
     * @param x position
     */
    public final void setX(final double x) {
        this.x = x;
    }

    /**
     * Set player y position.
     * @param y position
     */
    public final void setY(final double y) {
        this.y = y;
    }

    /**
     * Collision tile setter.
     * @param collisionTile tile
     */
    public final void setCollisionTile(final Rectangle collisionTile) {
        this.collisionTile = collisionTile;
    }

    /**
     * Move player up.
     */
    public final void moveUp() {
        yMove = -INCREMENT;
    }

    /**
     * Move player down.
     */
    public final void moveDown() {
        yMove = INCREMENT;
    }

    /**
     * Move player left.
     */
    public final void moveLeft() {
        xMove = -INCREMENT;
    }

    /**
     * Move player right.
     */
    public final void moveRight() {
        xMove = INCREMENT;
    }

    /**
     * Stop player moving up.
     */
    public final void stopUp() {
        setMovingUp(false);
        stopY();
    }

    /**
     * Stop player moving down.
     */
    public final void stopDown() {
        setMovingDown(false);
        stopY();
    }

    /**
     * Check if player is not moving in other vertical direction.
     */
    public final void stopY() {
        if (!isMovingUp() && !isMovingDown()) {
            yMove = 0;
        }
        if (!isMovingUp() && isMovingDown()) {
            moveDown();
        }
        if (isMovingUp() && !isMovingDown()) {
            moveUp();
        }
    }

    /**
     * Stop player moving left.
     */
    public final void stopLeft() {
        setMovingLeft(false);
        stopX();
    }

    /**
     * Stop player moving right.
     */
    public final void stopRight() {
        setMovingRight(false);
        stopX();
    }

    /**
     * Check if player is not moving in other horizontal direction.
     */
    public final void stopX() {
        if (!isMovingLeft() && !isMovingRight()) {
            xMove = 0;
        }
        if (!isMovingLeft() && isMovingRight()) {
            moveRight();
        }
        if (isMovingLeft() && !isMovingRight()) {
            moveLeft();
        }
    }

    /**
     * Check if player can set-up a bomb.
     * @return result
     */
    public final boolean canSetBomb() {
        return (bombs.size() < playerBombs);
    }

    /**
     * Bomb set-up.
     */
    public final void setBomb() {
        Tile[][] map = game.getMap();
        if (canSetBomb()) {
            int tileY = (int) Math.round(y / Game.TILE_WIDTH);
            int tileX = (int) Math.round(x / Game.TILE_HEIGHT);

            if (map[tileY][tileX].getType() != Tile.TYPE_BOMB) {
                setTile(tileX, tileY, Tile.TYPE_BOMB, true);
                bombs.add((Bomb) map[tileY][tileX]);
                numberOfWalkableBombs++;
            }
        }
    }

    /**
     * Bomb explosion.
     * @param x x
     * @param y y
     * @param bombFire fire
     */
    public final void boom(final int x, final int y, final int bombFire) {
        setTile(x, y, 0, true);
        checkFireCollison(x, y, bombFire, "fire");

        boolean[] collisions = game.getCollision().multipleCollision();
        int timerIndex = timer.size();
        Timer t = new Timer(Animation.A_BOMB, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                timer.get(timerIndex).stop();
                checkFireCollison(x, y, bombFire, "resolve");
                for (int i = 0; i < collisions.length; i++) {
                    if (collisions[i]) {
                        game.getPlayer()[i].decreasePlayerHealth(1);
                    }
                }
                bombOtherBombs();
            }
        });
        timer.add(t);
        t.start();

        int index = -1;
        for (int i = 0; i < bombs.size(); i++) {
            if (bombs.get(i).getX() == x && bombs.get(i).getY() == y) {
                index = i;
            }
        }
        if (index >= 0) {
            bombs.remove(index);
        }
    }

    /**
     * In case fire from one bomb reached other bomb(s)
     * we need to explode them either.
     */
    public final void bombOtherBombs() {
        if (gonnaBoom.size() >= 1) {
            for (int i = 0; i < 1; i++) {
                Bomb b = gonnaBoom.get(i);
                boom(b.getX(), b.getY(), b.getBombFire());
                b.setBombed(true);
            }
            gonnaBoom.remove(0);
        }
    }

    /**
     * Check collision of bomb flames.
     * 4 loops for 4 different flames
     * (flame begins in the middle and goes up, down, left, right).
     * If flame reaches a wall it will not continue.
     * If flame destroys a block there might spawn a power-up tile or exit.
     * @param x horizontal position
     * @param y vertical position
     * @param bombFire size of flame
     * @param action start or end of explosion
     */
    public final void checkFireCollison(
        final int x,
        final int y,
        final int bombFire,
        final String action
    ) {
        Tile[][] map = game.getMap();

        int yFrom = 0, xFrom = 0;
        if (y - bombFire >= 0) {
            yFrom = y - bombFire;
        }
        if (x - bombFire >= 0) {
            xFrom = x - bombFire;
        }

        int yTo, xTo;
        if (y + bombFire <= map.length - 1) {
            yTo = y + bombFire;
        } else {
            yTo = map.length - 1;
        }
        if (x + bombFire <= map[0].length - 1) {
            xTo = x + bombFire;
        } else {
            xTo = map[0].length - 1;
        }

        if (action.equals("fire")) {
            blocks.clear();
        }

        // do not let the bomb bomb through the wall or more than 1 block
        boolean stop = false;
        for (int i = y; i >= yFrom; i--) {
            if (action.equals("fire")) {
                if (map[i][x].getType() == Tile.TYPE_BOMB) {
                    Bomb bomb = (Bomb) map[i][x];
                    gonnaBoom.add(bomb);
                }
                if (
                    map[i][x].getType() != Tile.TYPE_WALL
                    && map[i][x].getType() != SpecialTile.TYPE_EXIT
                ) {
                    if (!stop) {
                        if (map[i][x].getType() == Tile.TYPE_BLOCK) {
                            blocks.add((Block) map[i][x]);
                            stop = true;
                        }
                        if (i == yFrom) {
                            setTile(x, i, Fire.TYPE_FIRE_1, false);
                        } else {
                            setTile(x, i, Fire.TYPE_FIRE_3, false);
                        }

                        if (stop) {
                            setTile(x, i, Fire.TYPE_FIRE_1, false);
                        }
                    }
                } else {
                    stop = true;
                }
            } else if (action.equals("resolve")) {
                if (!stop) {
                    if (
                        map[i][x].getType() != Tile.TYPE_WALL
                        && map[i][x].getType() != Tile.TYPE_BLOCK
                        && map[i][x].getType() != SpecialTile.TYPE_EXIT
                    ) {
                        setTile(x, i, Tile.TYPE_GRASS, true);
                    } else {
                        stop = true;
                    }
                }
            }
        }
        stop = false;

        for (int i = y; i <= yTo; i++) {
            if (action.equals("fire")) {
                if (map[i][x].getType() == Tile.TYPE_BOMB) {
                    Bomb bomb = (Bomb) map[i][x];
                    gonnaBoom.add(bomb);
                }
                if (
                    map[i][x].getType() != Tile.TYPE_WALL
                    && map[i][x].getType() != SpecialTile.TYPE_EXIT
                ) {
                    if (!stop) {
                        if (map[i][x].getType() == Tile.TYPE_BLOCK) {
                            blocks.add((Block) map[i][x]);
                            stop = true;
                        }
                        if (i == y)  {
                            setTile(x, i, Fire.TYPE_FIRE_4, false);
                        } else if (i == yTo)  {
                            setTile(x, i, Fire.TYPE_FIRE_2, false);
                        } else {
                            setTile(x, i, Fire.TYPE_FIRE_3, false);
                        }

                        if (stop) {
                            setTile(x, i, Fire.TYPE_FIRE_2, false);
                        }
                    }
                } else {
                    stop = true;
                }
            } else if (action.equals("resolve")) {
                if (!stop) {
                    if (
                        map[i][x].getType() != Tile.TYPE_WALL
                        && map[i][x].getType() != Tile.TYPE_BLOCK
                        && map[i][x].getType() != SpecialTile.TYPE_EXIT
                    ) {
                        setTile(x, i, Tile.TYPE_GRASS, true);
                    } else {
                        stop = true;
                    }
                }
            }

        }
        stop = false;

        for (int j = x; j >= xFrom; j--) {
            if (action.equals("fire")) {
                if (map[y][j].getType() == Tile.TYPE_BOMB) {
                    Bomb bomb = (Bomb) map[y][j];
                    gonnaBoom.add(bomb);
                }
                if (
                    map[y][j].getType() != Tile.TYPE_WALL
                    && map[y][j].getType() != SpecialTile.TYPE_EXIT
                ) {
                    if (!stop) {
                        if (map[y][j].getType() == Tile.TYPE_BLOCK) {
                            blocks.add((Block) map[y][j]);
                            stop = true;
                        }
                        if (j == xFrom) {
                            setTile(j, y, Fire.TYPE_FIRE_5, false);
                        } else {
                            setTile(j, y, Fire.TYPE_FIRE_7, false);
                        }

                        if (stop) {
                            setTile(j, y, Fire.TYPE_FIRE_5, false);
                        }
                    }
                } else {
                    stop = true;
                }
            } else if (action.equals("resolve")) {
                if (!stop) {
                    if (
                        map[y][j].getType() != Tile.TYPE_WALL
                        && map[y][j].getType() != Tile.TYPE_BLOCK
                        && map[y][j].getType() != SpecialTile.TYPE_EXIT
                    ) {
                        setTile(j, y, Tile.TYPE_GRASS, true);
                    } else {
                        stop = true;
                    }
                }
            }
        }
        stop = false;

        for (int j = x; j <= xTo; j++) {
            if (action.equals("fire")) {
                if (map[y][j].getType() == Tile.TYPE_BOMB) {
                    Bomb bomb = (Bomb) map[y][j];
                    gonnaBoom.add(bomb);
                }
                if (
                    map[y][j].getType() != Tile.TYPE_WALL
                    && map[y][j].getType() != SpecialTile.TYPE_EXIT
                ) {
                    if (!stop) {
                        if (map[y][j].getType() == Tile.TYPE_BLOCK) {
                            blocks.add((Block) map[y][j]);
                            stop = true;
                        }
                        if (j == x) {
                            setTile(j, y, Fire.TYPE_FIRE_4, false);
                        } else if (j == xTo) {
                            setTile(j, y, Fire.TYPE_FIRE_6, false);
                        } else {
                            setTile(j, y, Fire.TYPE_FIRE_7, false);
                        }

                        if (stop) {
                            setTile(j, y, Fire.TYPE_FIRE_6, false);
                        }
                    }
                } else {
                    stop = true;
                }
            } else if (action.equals("resolve")) {
                if (!stop) {
                    if (
                        map[y][j].getType() != Tile.TYPE_WALL
                        && map[y][j].getType() != Tile.TYPE_BLOCK
                        && map[y][j].getType() != SpecialTile.TYPE_EXIT
                    ) {
                        setTile(j, y, Tile.TYPE_GRASS, true);
                    } else {
                        stop = true;
                    }
                }
            }
        }

        if (action.equals("resolve")) {
            for (int b = 0; b < blocks.size(); b++) {
                if (blocks.get(b).hasExit()) {
                    setTile(
                        blocks.get(b).getX(), blocks.get(b).getY(),
                        SpecialTile.TYPE_EXIT, true, SpecialTile.EXIT
                    );
                } else {
                    Random r = new Random();
                    final int rand = 3;
                    int special = r.nextInt(2);
                    if (special == 1) {
                        int item = r.nextInt(rand) + (rand - 1);
                        if (item == SpecialTile.SPEED) {
                            setTile(
                                blocks.get(b).getX(), blocks.get(b).getY(),
                                SpecialTile.TYPE_SPEED, true, SpecialTile.SPEED
                            );
                        } else if (item == SpecialTile.BOMB) {
                            setTile(
                                blocks.get(b).getX(), blocks.get(b).getY(),
                                SpecialTile.TYPE_BOMB, true, SpecialTile.BOMB
                            );
                        } else if (item == SpecialTile.FIRE) {
                            setTile(
                                blocks.get(b).getX(), blocks.get(b).getY(),
                                SpecialTile.TYPE_FIRE, true, SpecialTile.FIRE
                            );
                        }
                    } else {
                        setTile(
                            blocks.get(b).getX(), blocks.get(b).getY(),
                            Tile.TYPE_GRASS, true
                        );
                    }
                }
            }
        }
    }

    /**
     * Map tile setter.
     * @param x horizontal position
     * @param y vertical position
     * @param type tile type
     * @param walkable is it walkable
     */
    public final void setTile(
        final int x, final int y, final int type, final boolean walkable
    ) {
        if (isAlive) {
            if (type >= Fire.TYPE_FIRE_1 && type <= Fire.TYPE_FIRE_7) {
                game.setTile(x, y, new Fire(x, y, type, walkable, game));
            } else if (type == Tile.TYPE_BOMB) {
                game.setTile(x, y, new Bomb(
                    x, y, type, walkable, BOMB_TIME, playerFire, this
                ));
            } else {
                game.setTile(x, y, new Tile(x, y, type, walkable));
            }
        }
    }

    /**
     * Special map tile setter.
     * @param x horizontal position
     * @param y vertical position
     * @param type tile type
     * @param walkable is it walkable
     * @param special type of special tile
     */
    public final void setTile(
        final int x, final int y, final int type,
        final boolean walkable, final int special
    ) {
        if (isAlive) {
            if (type >= SpecialTile.TYPE_EXIT) {
                game.setTile(
                    x, y, new SpecialTile(x, y, type, walkable, special)
                );
            }
        }
    }

    /**
     * Player x getter.
     * @return x
     */
    public final double getX() {
        return x;
    }

    /**
     * Player y getter.
     * @return y
     */
    public final double getY() {
        return y;
    }

    /**
     * Player statement getter.
     * @return isAlive
     */
    public final boolean isAlive() {
        return isAlive;
    }

    /**
     * Is player moving left.
     * @return movingLeft
     */
    public final boolean isMovingLeft() {
        return movingLeft;
    }

    /**
     * Set player moving left.
     * @param movingLeft moving left
     */
    public final void setMovingLeft(final boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    /**
     * Is player moving right.
     * @return movingRight
     */
    public final boolean isMovingRight() {
        return movingRight;
    }

    /**
     * Set player moving right.
     * @param movingRight moving right
     */
    public final void setMovingRight(final boolean movingRight) {
        this.movingRight = movingRight;
    }

    /**
     * Is player moving up.
     * @return movingUp
     */
    public final boolean isMovingUp() {
        return movingUp;
    }

    /**
     * Set player moving up.
     * @param movingUp moving up
     */
    public final void setMovingUp(final boolean movingUp) {
        this.movingUp = movingUp;
    }

    /**
     * Is player moving down.
     * @return movingDown
     */
    public final boolean isMovingDown() {
        return movingDown;
    }

    /**
     * Set player moving down.
     * @param movingDown moving down
     */
    public final void setMovingDown(final boolean movingDown) {
        this.movingDown = movingDown;
    }

    /**
     * Player (icon) width getter.
     * @return width
     */
    public final int getPlayerWidth() {
        return PLAYER_WIDTH;
    }

    /**
     * Player (icon) height getter.
     * @return height
     */
    public final int getPlayerHeight() {
        return PLAYER_HEIGHT;
    }

    /**
     * Player speed getter.
     * @return speed
     */
    public final int getSpeed() {
        return playerSpeed;
    }

    /**
     * Get size of bomb flame.
     * @return bomb fire
     */
    public final int getBombFire() {
        return playerFire;
    }

    /**
     * Decrease player lives.
     * (player has only 1 life but it is designed for more)
     * @param number number of lives
     */
    public final void decreasePlayerHealth(final int number) {
        this.playerHealth -= number;
        if (this.playerHealth <= 0) {
            isAlive = false;
        }
    }

    /**
     * Get player speed.
     * @return speed
     */
    public final int getPlayerSpeed() {
        return playerSpeed;
    }

    /**
     * Get number of bombs that can player set-up.
     * @return number of bombs
     */
    public final int getPlayerBombs() {
        return playerBombs;
    }
}
