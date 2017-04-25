package sk.uniza.fri.fant0m.bomberman;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 * Map class.
 * @author fant0m
 */
public class Map {
    /**
     * Is map loaded and ready.
     */
    private boolean ready;
    /**
     * Map time.
     */
    private int time;
    /**
     * Map score.
     */
    private int score;

    /**
     * Game images.
     */
    private BufferedImage sprites;
    /**
     * Enemies collection.
     */
    private ArrayList<Enemy> enemies;
    /**
     * Collision instance.
     */
    private Collision collision;
    /**
     * File reading.
     */
    private BufferedReader in;

    /**
     * Map constructor.
     * @param collision collision
     */
    public Map(final Collision collision) {
        this.ready = false;
        this.enemies = new ArrayList<>();
        this.collision = collision;
    }

    /**
     * Level loading.
     * @param level level
     * @return array of tiles
     */
    public final Tile[][] loadMap(final int level) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            sprites = ImageIO.read(loader.getResource("img/sprites.png"));
        } catch (IOException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }
        in = new BufferedReader(new InputStreamReader(
            loader.getResourceAsStream("maps/" + level + ".txt"),
            StandardCharsets.UTF_8
        ));

        String row = null;
        try {
            row = in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }

        Tile[][] output = null;
        if (row != null) {
            String[] data = row.split("x");
            output = new Tile[Integer.parseInt(data[1])]
                             [Integer.parseInt(data[0])];

            final int scoreIndex = 3;
            time = Integer.parseInt(data[2]);
            score = Integer.parseInt(data[scoreIndex]);
        }

        try {
            row = in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }

        final int startX = 442;
        final int startY = 233;
        final int width = 16;
        final int height = 18;

        if (row != null) {
            String[] mapEnemies = row.split("x");
            for (String s : mapEnemies) {
                String[] enemyData = s.split("-");
                Animation dyingEnemy = new Animation();
                dyingEnemy.addFrame(
                    sprites.getSubimage(startX, startY, width, height),
                    Animation.A_DYING_ENEMY / 2
                );
                dyingEnemy.addFrame(
                    sprites.getSubimage(startX + width, startY, width, height),
                    Animation.A_DYING_ENEMY / 2
                );
                Enemy e = new Enemy(
                    Integer.parseInt(enemyData[1]) * Game.TILE_WIDTH,
                    Integer.parseInt(enemyData[0]) * Game.TILE_WIDTH,
                    collision,
                    dyingEnemy
                );
                enemies.add(e);
            }
        }

        int yIndex = -1;
        int xIndex = -1;
        ArrayList<Block> blocks = new ArrayList<Block>();

        try {
            while ((row = in.readLine()) != null) {
                yIndex++;
                xIndex = -1;
                for (int i = 0; i < row.length(); i++) {
                    xIndex++;
                    char c = row.charAt(i);
                    if (c == '0') {
                        output[yIndex][xIndex] = new Tile(
                            xIndex, yIndex, Tile.TYPE_GRASS, true
                        );
                    } else if (c == '1') {
                        output[yIndex][xIndex] = new Tile(
                            xIndex, yIndex, Tile.TYPE_WALL, false
                        );
                    } else if (c == '2') {
                        output[yIndex][xIndex] = new Block(
                            xIndex, yIndex, Tile.TYPE_BLOCK, false
                        );
                        blocks.add((Block) output[yIndex][xIndex]);
                    }
                }
            }
        } catch (IOException e) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, e);
        }

        // do not create exit in multiplayer game
        if (level < Game.LEVEL_MULTIPLAYER) {
            Random random = new Random();
            int exit = random.nextInt(blocks.size());
            blocks.get(exit).setHasExit(true);
        }

        try {
            in.close();
        } catch (IOException e) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, e);
        }

        return output;
    }

    /**
     * Enemies getter.
     * @return list of enemies
     */
    public final ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Level countdown until player has to find exit.
     */
    public final void countdown() {
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                time--;
                if (time < 0) {
                    timer.cancel();
                    timer.purge();
                    return;
                }
            }
        };

        final long t = 1000L;
        timer.schedule(task, 0L, t);
    }

    /**
     * Remaining time of level.
     * @return seconds
     */
    public final int getSeconds() {
        return time;
    }

    /**
     * Remaining formatted time of level.
     * @return formatted string
     */
    public final String getTime() {
        final int s = 60;
        final int checkSeconds = 10;

        int minutes = time / s;
        int seconds = time - minutes * s;
        String sec;
        if (seconds < checkSeconds) {
            sec = "0" + Integer.toString(seconds);
        } else {
            sec = Integer.toString(seconds);
        }
        return "0" + Integer.toString(minutes) + ":" + sec;
    }

    /**
     * Is map loaded.
     * @return ready
     */
    public final boolean isReady() {
        return ready;
    }

    /**
     * Set it it is map loaded.
     * @param ready ready
     */
    public final void setReady(final boolean ready) {
        this.ready = ready;
    }

    /**
     * Score that player gains going through current level.
     * @return score
     */
    public final int getScore() {
        return score;
    }
}
