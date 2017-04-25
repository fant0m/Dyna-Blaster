package sk.uniza.fri.fant0m.bomberman;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.Timer;

/**
 * Collision class.
 * @author fant0m
 */
public class Collision {
    /**
     * Game instance.
     */
    private Game game;
    /**
     * Timers for dying enemies.
     */
    private ArrayList<Timer> timer;

    /**
     * Collision constructor.
     * @param game game
     */
    public Collision(final Game game) {
        this.game = game;
        this.timer = new ArrayList<>();
    }

    /**
     * Collision control of enemy.
     * @param x horizontal position
     * @param y vertical position
     * @param direction direction where the enemy is moving
     * @param speed enemy speed
     * @param enemy enemy instance
     * @return true if enemy cant move in that direction
     */
    public final boolean checkEnemyCollision(
        final double x,
        final double y,
        final int direction,
        final double speed,
        final Enemy enemy
    ) {
        Rectangle2D enemyRect = null;
        ArrayList<Rectangle> rect = new ArrayList<>();
        Tile[][] map = game.getMap();

        if (direction == Enemy.DIR_UP) {
            enemyRect = new Rectangle2D.Double(
                x, y - speed, Game.TILE_WIDTH, Game.TILE_HEIGHT
            );

            if (y - speed < 0) {
                return true;
            }
        } else if (direction == Enemy.DIR_DOWN) {
            enemyRect = new Rectangle2D.Double(
                x, y + speed, Game.TILE_WIDTH, Game.TILE_HEIGHT
            );

            if (y + speed + Game.TILE_HEIGHT > map.length * Game.TILE_HEIGHT) {
                return true;
            }
        } else if (direction == Enemy.DIR_LEFT) {
            enemyRect = new Rectangle2D.Double(
                x - speed, y, Game.TILE_WIDTH, Game.TILE_HEIGHT
            );

            if (x - speed < 0) {
                return true;
            }
        } else if (direction == Enemy.DIR_RIGHT) {
            enemyRect = new Rectangle2D.Double(
                x + speed, y, Game.TILE_WIDTH, Game.TILE_HEIGHT
            );

            if (x + speed + Game.TILE_WIDTH > map[0].length * Game.TILE_WIDTH) {
                return true;
            }
        }

        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (
                    !map[j][i].isWalkable()
                    ||
                    map[j][i].getType() == Tile.TYPE_BOMB
                ) {
                    Rectangle tileRect = new Rectangle(
                        i * Game.TILE_WIDTH, j * Game.TILE_HEIGHT,
                        Game.TILE_WIDTH, Game.TILE_WIDTH
                    );
                    rect.add(tileRect);
                }
            }
        }

        for (Rectangle collision : rect) {
            if (collision.intersects(enemyRect)) {
                Tile tile = map[collision.y / Game.TILE_HEIGHT]
                              [collision.x / Game.TILE_WIDTH];

                /**
                 * If enemy is in collision with flame
                 * we need to kill it + increase player score
                 */
                if (tile.getType() >= Fire.TYPE_FIRE_1
                    && tile.getType() <= Fire.TYPE_FIRE_7) {
                    // todo test if its enough to compare getCurrentFrame with 0
                    if (
                        !enemy.isDying()
                        && ((Fire) tile).getFire().getCurrentFrame() == 0
                    ) {
                        int timerIndex = timer.size();
                        Timer t = new Timer(
                            Animation.A_DYING_ENEMY, new ActionListener() {
                                @Override
                                public void actionPerformed(
                                    final ActionEvent e
                                ) {
                                    enemy.setDying(false);
                                    enemy.setAlive(false);
                                    timer.get(timerIndex).stop();
                                }
                            }
                        );
                        timer.add(t);
                        t.start();
                        enemy.setDying(true);
                        enemy.getDyingEnemy().setMoving(true);

                        if (!enemy.hasReceivedScore()) {
                            if (game.getType() == Game.SINGLEPLAYER) {
                                game.getPlayerInfo()[0].increaseScore(
                                    PlayerInfo.SCORE_ENEMY
                                );
                            }
                            enemy.setReceivedScore(true);
                        }
                    }
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Collision control between enemy and player.
     * @param x horizontal position of enemy
     * @param y vertical position of enemy
     */
    public final void checkEnemyWithPlayerCollision(
        final double x, final double y
    ) {
        Rectangle2D enemyRect = new Rectangle2D.Double(
            (int) x, (int) y, Game.TILE_WIDTH, Game.TILE_HEIGHT
        );

        for (Player player : game.getPlayer()) {
            Rectangle2D playerRect = new Rectangle2D.Double(
                (int) player.getX() + Player.CENTER_PLAYER_X,
                (int) player.getY() + Player.CENTER_PLAYER_Y,
                Game.TILE_WIDTH - (Player.CENTER_PLAYER_X * 2),
                Game.TILE_HEIGHT - (Player.CENTER_PLAYER_Y * 2)
            );

            if (playerRect.intersects(enemyRect)) {
                player.decreasePlayerHealth(1);
            }
        }
    }

    /**
     * Collision control between player and power-up.
     * @param x player x
     * @param y player y
     * @return collision tile
     */
    public final SpecialTile specialCollision(final double x, final double y) {
        Tile[][] map = game.getMap();
        ArrayList<Rectangle> rect = new ArrayList<>();
        Rectangle2D playerRect = new Rectangle2D.Double(
            x + Player.CENTER_PLAYER_X,
            y + Player.CENTER_PLAYER_Y,
            Player.PLAYER_WIDTH - (Player.CENTER_PLAYER_X * 2),
            Player.PLAYER_HEIGHT - (Player.CENTER_PLAYER_Y * 2)
        );

        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (
                    map[j][i].isWalkable()
                    && map[j][i].getType() >= SpecialTile.TYPE_EXIT
                ) {
                    Rectangle tileRect = new Rectangle(
                        i * Game.TILE_WIDTH, j * Game.TILE_HEIGHT,
                        Game.TILE_WIDTH, Game.TILE_HEIGHT
                    );
                    rect.add(tileRect);
                }
            }
        }

        for (Rectangle collision : rect) {
            if (collision.intersects(playerRect)) {
                return (SpecialTile) map[(collision.y / Game.TILE_HEIGHT)]
                                        [(collision.x / Game.TILE_WIDTH)];
            }
        }

        return null;
    }

    /**
     * Collision control between player and map tile.
     * @param x player x
     * @param y player y
     * @return collision result
     */
    public final boolean collision(final double x, final double y) {
        Tile[][] map = game.getMap();
        ArrayList<Rectangle> rect = new ArrayList<>();
        Rectangle2D playerRect = new Rectangle2D.Double(
            x + Player.CENTER_PLAYER_X,
            y + Player.CENTER_PLAYER_Y,
            Player.PLAYER_WIDTH - (Player.CENTER_PLAYER_X * 2),
            Player.PLAYER_HEIGHT - (Player.CENTER_PLAYER_Y * 2)
        );

        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (!map[j][i].isWalkable()) {
                    Rectangle tileRect = new Rectangle(
                        i * Game.TILE_WIDTH, j * Game.TILE_HEIGHT,
                        Game.TILE_WIDTH, Game.TILE_HEIGHT
                    );
                    rect.add(tileRect);
                }
            }
        }

        for (Rectangle collision : rect) {
            if (collision.intersects(playerRect)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Collision control between players and map tile.
     * @return collision results
     */
    public final boolean[] multipleCollision() {
        ArrayList<Rectangle> rect = new ArrayList<>();

        Tile[][] map = game.getMap();
        Player[] p = game.getPlayer();
        Rectangle2D.Double[] players = new Rectangle2D.Double[p.length];

        boolean[] ids = new boolean[p.length];

        for (int i = 0; i < p.length; i++) {
            players[i] = new Rectangle2D.Double(
                p[i].getX() + Player.CENTER_PLAYER_X,
                p[i].getY() + Player.CENTER_PLAYER_Y,
                Player.PLAYER_WIDTH - (Player.CENTER_PLAYER_X * 2),
                Player.PLAYER_HEIGHT - (Player.CENTER_PLAYER_Y * 2)
            );
        }

        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (!map[j][i].isWalkable()) {
                    Rectangle tileRect = new Rectangle(
                        i * Game.TILE_WIDTH, j * Game.TILE_HEIGHT,
                        Game.TILE_WIDTH, Game.TILE_HEIGHT
                    );
                    rect.add(tileRect);
                }
            }
        }

        for (Rectangle collision : rect) {
            for (int i = 0; i < p.length; i++) {
                if (collision.intersects(players[i])) {
                    ids[i] = true;
                }
            }
        }

        return ids;
    }

    /**
     * Collision control between player and bomb.
     * @param player player
     * @return collision result
     */
    public final boolean bombCollision(final Player player) {
        Tile[][] map = game.getMap();
        ArrayList<Rectangle> rect = new ArrayList<>();
        Rectangle2D playerRect = new Rectangle2D.Double(
            player.getX() + Player.CENTER_PLAYER_X,
            player.getY() + Player.CENTER_PLAYER_Y,
            Player.PLAYER_WIDTH - (Player.CENTER_PLAYER_X * 2),
            Player.PLAYER_HEIGHT - (Player.CENTER_PLAYER_Y * 2)
        );

        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (
                    map[j][i].isWalkable()
                    && map[j][i].getType() == Tile.TYPE_BOMB
                ) {
                    Rectangle tileRect = new Rectangle(
                        i * Game.TILE_WIDTH, j * Game.TILE_HEIGHT,
                        Game.TILE_WIDTH, Game.TILE_HEIGHT
                    );
                    rect.add(tileRect);
                }
            }
        }

        for (Rectangle collision : rect) {
            if (collision.intersects(playerRect)) {
                player.setCollisionTile(collision);
                return true;
            }
        }

        return false;
    }
}
