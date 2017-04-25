package sk.uniza.fri.fant0m.bomberman;

/**
 * Informations about player.
 * @author fant0m
 */
public class PlayerInfo {
    /**
     * Player score.
     */
    private int score;
    /**
     * Player name.
     */
    private String name;
    /**
     * Points for killing an enemy.
     */
    public static final int SCORE_ENEMY = 10;
    /**
     * Decrease score for repeating same level.
     */
    public static final int SCORE_REPEAT_LEVEL = 50;

    /**
     * Constructor.
     * @param score player score
     * @param name player name
     */
    public PlayerInfo(final int score, final String name) {
        this.score = score;
        this.name = name;
    }

    /**
     * Score getter.
     * @return score
     */
    public final int getScore() {
        return score;
    }

    /**
     * Score setter.
     * @param score score
     */
    public final void setScore(final int score) {
        this.score = score;
    }

    /**
     * Increase score.
     * @param s amount
     */
    public final void increaseScore(final int s) {
        this.score += s;
    }

    /**
     * Decrease score.
     * @param s amount
     */
    public final void decreaseScore(final int s) {
        this.score -= s;

        if (this.score < 0) {
            this.score = 0;
        }
    }

    /**
     * Name getter.
     * @return name
     */
    public final String getName() {
        return name;
    }

    /**
     * Name setter.
     * @param name name
     */
    public final void setName(final String name) {
        this.name = name;
    }
}
