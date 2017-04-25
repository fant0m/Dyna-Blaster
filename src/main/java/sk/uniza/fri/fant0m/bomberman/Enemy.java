package sk.uniza.fri.fant0m.bomberman;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Enemy class.
 * @author fant0m
 */
public class Enemy {
    /**
     * X position.
     */
    private double x;
    /**
     * Y position.
     */
    private double y;
    /**
     * Direction where is the enemy going.
     * 1 = up, 2 = down, 3 = left, 4 = right
     */
    private int direction;
    /**
     * Is enemy alive.
     */
    private boolean alive;
    /**
     * Has player received a score for killing enemy.
     */
    private boolean receivedScore;
    /**
     * Is enemy dying.
     */
    private boolean dying;

    /**
     * Collision instance.
     */
    private Collision collision;
    /**
     * Animation of dying enemy.
     */
    private Animation dyingEnemy;

    /**
     * Enemy speed.
     */
    private static final double SPEED = 1.0;
    /**
     * Direction up.
     */
    public static final int DIR_UP = 1;
    /**
     * Direction down.
     */
    public static final int DIR_DOWN = 2;
    /**
     * Direction left.
     */
    public static final int DIR_LEFT = 3;
    /**
     * Direction right.
     */
    public static final int DIR_RIGHT = 4;
    /**
     * Number of directions.
     */
    public static final int DIRECTIONS = 4;

    /**
     * Enemy constructor.
     * @param x x
     * @param y y
     * @param collision collision
     * @param dyingEnemy enemy
     */
    public Enemy(
        final int x,
        final int y,
        final Collision collision,
        final Animation dyingEnemy
    ) {
        this.x = x;
        this.y = y;
        this.collision = collision;
        this.dyingEnemy = dyingEnemy;

        direction = -1;
        alive = true;
        receivedScore = false;
        dying = false;
    }

    /**
     * Enemy position change. Plan and update enemy random direction.
     */
    public final void update() {
        boolean cantContinueCurrentDirection = false;
        int opposite = -1;
        if (direction != -1) {
            if (direction == DIR_UP) {
                opposite = DIR_DOWN;
            } else if (direction == DIR_DOWN) {
                opposite = DIR_UP;
            } else if (direction == DIR_LEFT) {
                opposite = DIR_RIGHT;
            } else if (direction == DIR_RIGHT) {
                opposite = DIR_LEFT;
            }
            cantContinueCurrentDirection = collision.checkEnemyCollision(
                x, y, direction, SPEED, this
            );
        }

        boolean foundDirection = false;
        ArrayList<Integer> directions = new ArrayList<Integer>(DIRECTIONS);
        for (int i = 1; i <= DIRECTIONS; i++) {
            if (cantContinueCurrentDirection || i != opposite) {
                directions.add(i);
            }
        }
        Collections.shuffle(directions);

        for (int i : directions) {
            if (!foundDirection) {
                if (!collision.checkEnemyCollision(x, y, i, SPEED, this)) {
                    foundDirection = true;
                    direction = i;
                }
            }
        }

        if (foundDirection) {
            if (direction == DIR_UP) {
                y -= SPEED;
            } else if (direction == DIR_DOWN) {
                y += SPEED;
            } else if (direction == DIR_LEFT) {
                x -= SPEED;
            } else if (direction == DIR_RIGHT) {
                x += SPEED;
            }
        }

        collision.checkEnemyWithPlayerCollision(x, y);
    }

    /**
     * Get dying enemy animation.
     * @return dying enemy
     */
    public final Animation getDyingEnemy() {
        return dyingEnemy;
    }

    /**
     * Check if it is dying.
     * @return dying
     */
    public final boolean isDying() {
        return dying;
    }

    /**
     * Set if it is dying.
     * @param dying dying
     */
    public final void setDying(final boolean dying) {
        this.dying = dying;
    }

    /**
     * Get x position.
     * @return x
     */
    public final double getX() {
        return x;
    }

    /**
     * Get y position.
     * @return y
     */
    public final double getY() {
        return y;
    }

    /**
     * Check if it is alive.
     * @return alive
     */
    public final boolean isAlive() {
        return alive;
    }

    /**
     * Set if it is alive.
     * @param alive alive
     */
    public final void setAlive(final boolean alive) {
        this.alive = alive;
    }

    /**
     * Check if it has received score.
     * @return received score
     */
    public final boolean hasReceivedScore() {
        return receivedScore;
    }

    /**
     * Set if it has received score.
     * @param receivedScore score
     */
    public final void setReceivedScore(final boolean receivedScore) {
        this.receivedScore = receivedScore;
    }
}
