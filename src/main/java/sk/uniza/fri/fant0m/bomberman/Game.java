package sk.uniza.fri.fant0m.bomberman;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Main game class.
 * @author fant0m
 */
public class Game extends JPanel implements KeyListener {
    /**
     * Array of players.
     */
    private Player[] player;
    /**
     * Array of player informations.
     */
    private PlayerInfo[] playerInfo;
    /**
     * Array of player names.
     */
    private String[] name;
    /**
     * Game images.
     */
    private BufferedImage sprites, grass, wall, block, doors, iSpeed, iBomb,
            iFire, top, wallUpLeft, wallUp, wallUpRight, wallRight,
            wallRightWindowStart, wallRightWindowEnd, wallDownLeft,
            wallDown, wallDownRight, wallLeft, wallLeftWindowStart,
            wallLeftWindowEnd, left, leftDown, right, rightDown;
    /**
     * Game array animations.
     */
    private Animation[] downAnim, upAnim, leftAnim, rightAnim, character;
    /**
     * Game single animations.
     */
    private Animation bomb, enemy;
    /**
     * Game map generator.
     */
    private Map mapGenerator;
    /**
     * ArrayList of enemies.
     */
    private ArrayList<Enemy> enemies;
    /**
     * Game map tiles.
     */
    private Tile[][] map = null;
    /**
     * Collision instance.
     */
    private Collision collision;
    /**
     * Current level.
     */
    private int level = 1;
    /**
     * View controller.
     */
    private View view;
    /**
     * Is game running.
     */
    private boolean running = true;
    /**
     * Type of the game.
     */
    private int type;

    /**
     * Tile width.
     */
    public static final int TILE_WIDTH = 32;
    /**
     * Tile height.
     */
    public static final int TILE_HEIGHT = 32;
    /**
     * Sprite dimension.
     */
    private static final int SPRITE = 24;
    /**
     * Number of levels.
     */
    private static final int LEVELS = 3;
    /**
     * Increment for multiplayer levels.
     */
    public static final int LEVEL_MULTIPLAYER = 100;
    /**
     * Width of game panel.
     */
    private static final int MOVE_SCREEN_X = 48;
    /**
     * Height of game panel.
     */
    private static final int MOVE_SCREEN_Y = 80;
    /**
     * Single player game.
     */
    public static final int SINGLEPLAYER = 1;
    /**
     * Multi player game.
     */
    public static final int MULTIPLAYER = 2;
    /**
     * Update interval.
     */
    public static final int UPDATE_INTERVAL = 17;


    /**
     * Init game panel, images and animations.
     * @param view view
     * @param type type of the game
     */
    public Game(final View view, final int type) {
        this.view = view;
        this.type = type;
        this.enemies = new ArrayList<>();
        this.collision = new Collision(this);

        setFocusable(true);
        addKeyListener(this);

        /**
         * Just 2 players in multiplayer but there's possibility for more
         * in future because players are saved in array.
         */
        int players = 1;
        if (type == MULTIPLAYER) {
            players = 2;
        }

        this.player = new Player[players];
        this.playerInfo = new PlayerInfo[players];
        this.name = new String[players];
        this.downAnim = new Animation[players];
        this.upAnim = new Animation[players];
        this.leftAnim = new Animation[players];
        this.rightAnim = new Animation[players];
        this.character = new Animation[players];

        final int animation = 200;
        final int sprite = 16;
        final int y = 110;

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            sprites = ImageIO.read(loader.getResource("img/sprites.png"));
            grass = ImageIO.read(loader.getResource("img/grass2.png"));
            wall = ImageIO.read(loader.getResource("img/wall.png"));
            block = ImageIO.read(loader.getResource("img/block.png"));
            doors = ImageIO.read(loader.getResource("img/doors.png"));
            iSpeed = ImageIO.read(loader.getResource("img/iSpeed.png"));
            iBomb = ImageIO.read(loader.getResource("img/iBomb.png"));
            iFire = ImageIO.read(loader.getResource("img/iFire.png"));
            top = ImageIO.read(loader.getResource("img/top.png"));

            // I refuse to update everything to constants ;-).
            wallUpLeft = sprites.getSubimage(431, y, sprite, sprite);
            wallUp = sprites.getSubimage(447, y, sprite, sprite);
            wallUpRight = sprites.getSubimage(479, y, sprite, sprite);
            wallRight = sprites.getSubimage(495, y, sprite, sprite);
            wallRightWindowStart = sprites.getSubimage(511, y, sprite, sprite);
            wallRightWindowEnd = sprites.getSubimage(527, y, sprite, sprite);
            wallDownLeft = sprites.getSubimage(543, y, sprite, sprite);
            wallDown = sprites.getSubimage(559, y, sprite, sprite);
            wallDownRight = sprites.getSubimage(591, y, sprite, sprite);
            wallLeft = sprites.getSubimage(623, y, sprite, sprite);
            wallLeftWindowStart = sprites.getSubimage(639, y, sprite, sprite);
            wallLeftWindowEnd = sprites.getSubimage(655, y, sprite, sprite);
            left = sprites.getSubimage(463, y + sprite, sprite / 2, sprite);
            leftDown = sprites.getSubimage(
                479, y + sprite, sprite / 2, sprite
            );
            right = sprites.getSubimage(
                671, y, sprite / 2, sprite
            );
            rightDown = sprites.getSubimage(
                367, y + sprite, sprite / 2, sprite
            );

            enemy = new Animation();
            enemy.addFrame(sprites.getSubimage(
                394, 233, sprite, 18), animation
            );
            enemy.addFrame(sprites.getSubimage(
                410, 233, sprite, 18), animation
            );
            enemy.addFrame(sprites.getSubimage(
                426, 233, sprite, 18), animation
            );
            enemy.setMoving(true);

            bomb = new Animation();
            bomb.addFrame(sprites.getSubimage(
                470, 0, sprite, sprite), animation
            );
            bomb.addFrame(sprites.getSubimage(
                486, 0, sprite, sprite), animation
            );
            bomb.addFrame(sprites.getSubimage(
                502, 0, sprite, sprite), animation
            );
            bomb.setMoving(true);

            for (int i = 0; i < players; i++) {
                downAnim[i] = new Animation();
                downAnim[i].addFrame(getImageFromSprite(0, 0), animation);
                downAnim[i].addFrame(getImageFromSprite(1, 0), animation);
                downAnim[i].addFrame(getImageFromSprite(2, 0), animation);

                upAnim[i] = new Animation();
                upAnim[i].addFrame(getImageFromSprite(9, 0), animation);
                upAnim[i].addFrame(getImageFromSprite(10, 0), animation);
                upAnim[i].addFrame(getImageFromSprite(10, 0), animation);

                leftAnim[i] = new Animation();
                leftAnim[i].addFrame(getImageFromSprite(6, 0), animation);
                leftAnim[i].addFrame(getImageFromSprite(7, 0), animation);
                leftAnim[i].addFrame(getImageFromSprite(8, 0), animation);

                rightAnim[i] = new Animation();
                rightAnim[i].addFrame(getImageFromSprite(3, 0), animation);
                rightAnim[i].addFrame(getImageFromSprite(4, 0), animation);
                rightAnim[i].addFrame(getImageFromSprite(5, 0), animation);

                playerInfo[i] = new PlayerInfo(0, "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get sub image from main game sprite image by default dimensions.
     * @param x horizontal position
     * @param y vertical position
     * @return image
     */
    public final BufferedImage getImageFromSprite(final int x, final int y) {
        return sprites.getSubimage(x * SPRITE, y * SPRITE, SPRITE, SPRITE);
    }

    /**
     * Get sub image from main game sprite image by dimensions.
     * @param x horizontal position
     * @param y vertical position
     * @param width image width
     * @param height image height
     * @return image
     */
    public final BufferedImage getImageFromSprite(
        final int x, final int y, final int width, final int height
    ) {
        return sprites.getSubimage(x, y, width, height);
    }

    /**
     * First game screen. Basic info about game and nick setup.
     * @param params optional parameters for saved game
     */
    public final void init(final Object... params) {
        this.view.revalidate();
        this.view.repaint();

        if (this.type == SINGLEPLAYER) {
            if (params.length != 0) {
                name[0] = (String) params[0];
            } else {
                name[0] = JOptionPane.showInputDialog(
                    this,
                    "Vitajte v hre bomberman!\nPostavu ovládate pomocou šípok"
                    + "(hore, dole, vpravo, vľavo).\nBombu pridáte pomocou "
                    + "medzerníku. \nCieľom hry je nahrať čo najvyššie skóre."
                    + "Skóre získavate za prejdenie levela a zabíjanie "
                    + "nepriateľov.\nDo ďalšieho levela sa dostanete pokiaľ"
                    + "nájdete východ a vojdete do neho. \n\n"
                    + "Zadajte Vaše meno", "Vitajte",
                    JOptionPane.PLAIN_MESSAGE
                );
            }

            if (name[0] != null) {
                if (params.length != 0) {
                    level = Integer.parseInt((String) params[1]);
                }

                running = true;
                start(level, params);
            } else {
                running = false;
                this.view.setView("Intro");
            }

        } else {
            JTextField player1 = new JTextField();
            JTextField player2 = new JTextField();
            Object[] message = {
                "Vitajte v hre bomberman!\nPrvú postavu ovládate pomocou "
                + "šípok(hore, dole, vpravo, vľavo), druhú pomocou kláves "
                + "W, A, S, D.\nBombu pridáte pomocou medzerníka a klávesu "
                + "B.\nCieľom hry je vydržať nažive dlhšie ako protivník."
                + "\n\nZadajte Vaše mená",
                "Hráč 1:", player1,
                "Hráč 2:", player2
            };

            int option = JOptionPane.showConfirmDialog(
                this, message, "Welcome", JOptionPane.OK_CANCEL_OPTION
            );
            name[0] = player1.getText();
            name[1] = player2.getText();

            if (option == JOptionPane.OK_OPTION) {
                running = true;
                startMultiplayer(level + LEVEL_MULTIPLAYER);
            } else {
                running = false;
                this.view.setView("Intro");
            }
        }
    }

    @Override
    public final void keyTyped(final KeyEvent e) {
    }

    /**
     * Key listener for movement update and bomb setup.
     */
    @Override
    public final void keyPressed(final KeyEvent e) {
        switch (KeyEvent.getKeyText(e.getKeyCode())) {
            case "Up":
                player[0].moveUp();
                player[0].setMovingUp(true);
                upAnim[0].setMoving(true);
                character[0] = upAnim[0];
                break;
            case "Down":
                player[0].moveDown();
                player[0].setMovingDown(true);
                downAnim[0].setMoving(true);
                character[0] = downAnim[0];
                break;
            case "Left":
                player[0].moveLeft();
                player[0].setMovingLeft(true);
                leftAnim[0].setMoving(true);
                character[0] = leftAnim[0];
                break;
            case "Right":
                player[0].moveRight();
                player[0].setMovingRight(true);
                rightAnim[0].setMoving(true);
                character[0] = rightAnim[0];
                break;
            case "Space":
                player[0].setBomb();
                break;
            case "W":
                if (type == MULTIPLAYER) {
                    player[1].moveUp();
                    player[1].setMovingUp(true);
                    upAnim[1].setMoving(true);
                    character[1] = upAnim[1];
                }
                break;
            case "S":
                if (type == MULTIPLAYER) {
                    player[1].moveDown();
                    player[1].setMovingDown(true);
                    downAnim[1].setMoving(true);
                    character[1] = downAnim[1];
                }
                break;
            case "A":
                if (type == MULTIPLAYER) {
                    player[1].moveLeft();
                    player[1].setMovingLeft(true);
                    leftAnim[1].setMoving(true);
                    character[1] = leftAnim[1];
                }
                break;
            case "D":
                if (type == MULTIPLAYER) {
                    player[1].moveRight();
                    player[1].setMovingRight(true);
                    rightAnim[1].setMoving(true);
                    character[1] = rightAnim[1];
                }
                break;
            case "B":
                if (type == MULTIPLAYER) {
                    player[1].setBomb();
                }
                break;
            default: break;
        }
    }

    @Override
    public final void keyReleased(final KeyEvent e) {
        switch (KeyEvent.getKeyText(e.getKeyCode())) {
            case "Up":
                player[0].stopUp();
                upAnim[0].setMoving(false);
                break;
            case "Down":
                player[0].stopDown();
                downAnim[0].setMoving(false);
                break;
            case "Left":
                player[0].stopLeft();
                leftAnim[0].setMoving(false);
                break;
            case "Right":
                player[0].stopRight();
                rightAnim[0].setMoving(false);
                break;
            case "W":
                if (type == MULTIPLAYER) {
                    player[1].stopUp();
                    upAnim[1].setMoving(false);
                }
                break;
            case "S":
                if (type == MULTIPLAYER) {
                    player[1].stopDown();
                    downAnim[1].setMoving(false);
                }
                break;
            case "A":
                if (type == MULTIPLAYER) {
                    player[1].stopLeft();
                    leftAnim[1].setMoving(false);
                }
                break;
            case "D":
                if (type == MULTIPLAYER) {
                    player[1].stopRight();
                    rightAnim[1].setMoving(false);
                }
                break;
            default: break;
        }
    }

    /**
     * Map tile setter.
     * @param x x
     * @param y y
     * @param tile tile
     */
    public final void setTile(final int x, final int y, final Object tile) {
        this.map[y][x] = (Tile) tile;
    }

    /**
     * Map getter.
     * @return map
     */
    public final Tile[][] getMap() {
        return map.clone();
    }

    /**
     * Set array of enemies.
     * @param enemies enemies
     */
    public final void setEnemies(final ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    /**
     * Load map and enemies.
     * @param level number of level we want to load
     */
    public final void loadMap(final int level) {
        map = mapGenerator.loadMap(level);
        enemies = mapGenerator.getEnemies();
        mapGenerator.setReady(true);
    }

    /**
     * Start of singleplayer level.
     * @param level level
     * @param params optional parameters for saved game
     */
    public final void start(final int level, final Object... params) {
        enemies.clear();
        mapGenerator = new Map(collision);
        loadMap(level);

        for (int i = 0; i < this.player.length; i++) {
            player[i] = new Player(this);

            if (level == 1) {
                playerInfo[i].setScore(0);
            }

            if (name[i].equals("")) {
                name[i] = "BezMena";
            }
            playerInfo[i].setName(name[i]);

            if (params.length != 0) {
                playerInfo[i].setScore(Integer.parseInt((String) params[2]));
                playerInfo[i].setName((String) params[0]);
            }

            // reset animations
            character[i] = downAnim[i];
            upAnim[i].setMoving(false);
            downAnim[i].setMoving(false);
            leftAnim[i].setMoving(false);
            rightAnim[i].setMoving(false);
        }

        mapGenerator.countdown();
    }

    /**
     * Start of multiplayer level.
     * @param level level
     */
    public final void startMultiplayer(final int level) {
        enemies.clear();
        mapGenerator = new Map(collision);
        loadMap(level);

        for (int i = 0; i < this.player.length; i++) {
            player[i] = new Player(this);

            if (level == 1) {
                playerInfo[i].setScore(0);
            }

            if (name[i].equals("")) {
                name[i] = "BezMena";
            }
            playerInfo[i].setName(name[i]);

            // reset animations
            character[i] = downAnim[i];
            upAnim[i].setMoving(false);
            downAnim[i].setMoving(false);
            leftAnim[i].setMoving(false);
            rightAnim[i].setMoving(false);

            if (i == 1) {
                player[i].setY(map.length * TILE_HEIGHT);
                player[i].setX(map[0].length * TILE_WIDTH);
            }
        }

        mapGenerator.countdown();
        running = true;
    }

    /**
     * Game loop.
     * If player is alive and has time to finish level, game updates every 17ms.
     * Otherwise it shows a dialog with further options.
     */
    public final void loop() {
        if (player[0] != null) {
            while (
                playersAlive() && mapGenerator.getSeconds() >= 0 && running
            ) {
                try {
                    this.update();
                    this.repaint();

                    Thread.sleep(UPDATE_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return;
        }

        /**
         * You are dead
         */
        if (!this.playersAlive() && running) {
            this.repaint();

            if (type == SINGLEPLAYER) {
                Object[] options = {"Áno", "Nie"};
                int n = JOptionPane.showOptionDialog(
                    this, "Boli ste zabitý. Chcete hrať znova?",
                    "Game Over", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]
                );
                if (n == JOptionPane.YES_OPTION) {
                    playerInfo[0].decreaseScore(PlayerInfo.SCORE_REPEAT_LEVEL);
                    start(level);
                    loop();
                } else {
                    Progress.saveProgress(
                        playerInfo[0].getName(),
                        level,
                        playerInfo[0].getScore()
                    );
                    Stats.writeScore(playerInfo[0]);
                    running = false;
                    level = 1;
                    view.setView("Intro");
                }
            } else {
                int winner = -1;
                for (int i = 0; i < player.length; i++) {
                    if (player[i].isAlive()) {
                        winner = i;
                    }
                }

                Object[] options = {"Áno", "Nie"};
                int n;
                if (winner != -1) {
                    n = JOptionPane.showOptionDialog(
                        this, "Hru vyhral hráč " + playerInfo[winner].getName()
                                + ". Chcete hrať znova?",
                        "Game Over", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]
                    );
                } else {
                    n = JOptionPane.showOptionDialog(
                        this, "Hru skončila remízou. Chcete hrať znova?",
                        "Game Over", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]
                    );
                }

                if (n == JOptionPane.YES_OPTION) {
                    startMultiplayer(level + LEVEL_MULTIPLAYER);
                    loop();
                } else {
                    running = false;
                    view.setView("Intro");
                }
            }
        }

        /**
         * Out of time
         */
        if (mapGenerator.getSeconds() < 0 && running) {
            if (type == SINGLEPLAYER) {
                Stats.writeScore(playerInfo[0]);

                Object[] options = {"Áno", "Nie"};
                int n = JOptionPane.showOptionDialog(
                    this, "Bohužiaľ, vypršal Vám čas. Chcete hrať znova?",
                    "Game Over", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]
                );
                if (n == JOptionPane.YES_OPTION) {
                    playerInfo[0].decreaseScore(PlayerInfo.SCORE_REPEAT_LEVEL);
                    start(level);
                    loop();
                } else {
                    Progress.saveProgress(
                        playerInfo[0].getName(),
                        level,
                        playerInfo[0].getScore()
                    );
                    Stats.writeScore(playerInfo[0]);
                    running = false;
                    level = 1;
                    view.setView("Intro");
                }
            } else {
                Object[] options = {"Áno", "Nie"};
                int n = JOptionPane.showOptionDialog(
                    this, "Bohužiaľ, vypršal Vám čas. Hra skončila remízou."
                            + "Chcete hrať znova?",
                    "Game Over", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]
                );
                if (n == JOptionPane.YES_OPTION) {
                    startMultiplayer(level + LEVEL_MULTIPLAYER);
                    loop();
                } else {
                    running = false;
                    view.setView("Intro");
                }
            }
        }
    }

    /**
     * Get player informations.
     * @return player informations
     */
    public final PlayerInfo[] getPlayerInfo() {
        return playerInfo.clone();
    }

    /**
     * Dialog for continuing on next level or playing from beginning.
     */
    public final void nextLevel() {
        if (level + 1 <= LEVELS) {
            Object[] options = {"Áno", "Nie"};
            int m = JOptionPane.showOptionDialog(
                this,
                "Gratulujeme! Úspešne ste prešli level " + level + "! "
                + "Chcete pokračovať v ďalšom leveli?", "Congratulations",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
            );
            playerInfo[0].increaseScore(mapGenerator.getScore());
            level++;
            if (m == JOptionPane.YES_OPTION) {
                start(level);
                loop();
            } else {
                level = 1;
                Stats.writeScore(playerInfo[0]);
                running = false;
                view.setView("Intro");
            }
        } else {
            Object[] options = {"Áno", "Nie"};
            playerInfo[0].increaseScore(mapGenerator.getScore());
            int m = JOptionPane.showOptionDialog(
                this,
                "Gratulujeme! Úspešne ste prešli celú hru! "
                + "Vaše finálne skóre je " + playerInfo[0].getScore() +  "!"
                + " Chcete hrať znova?", "Congratulations",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
            );
            if (m == JOptionPane.YES_OPTION) {
                level = 1;
                start(level);
                loop();
            } else {
                level = 1;
                Stats.writeScore(playerInfo[0]);
                running = false;
                view.setView("Intro");
            }
        }
    }

    /**
     * Player, enemy position and animations update.
     */
    public final void update() {
        int players = this.player.length;
        for (int i = 0; i < players; i++) {
            int speed = player[i].getSpeed();
            for (int j = 1; j <= speed; j++) {
                player[i].update();
            }
        }

        int removeIndex = -1;
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (e.isAlive()) {
                e.update();
            } else {
                removeIndex = i;
            }
        }
        if (removeIndex >= 0) {
            enemies.remove(removeIndex);
        }

        for (int i = 0; i < players; i++) {
            character[i].update(UPDATE_INTERVAL);
        }

        for (Enemy e : enemies) {
            if (e.isDying()) {
                e.getDyingEnemy().update(UPDATE_INTERVAL);
            }
        }
        enemy.update(UPDATE_INTERVAL);
        bomb.update(UPDATE_INTERVAL);
    }

    /**
     * Get players.
     * @return array of players
     */
    public final Player[] getPlayer() {
        return player.clone();
    }

    /**
     * Game painting.
     * @param g graphics
     */
    public final void paintComponent(final Graphics g) {
        if (mapGenerator != null && running) {
            if (!this.playersAlive()) {
                super.paintComponent(g);
                return;
            }
            if (mapGenerator.isReady()) {
                super.paintComponent(g);

                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map[0].length; j++) {
                        Tile current = map[i][j];
                        int type = current.getType();
                        if (
                            type >= Fire.TYPE_FIRE_1
                            && type <= Fire.TYPE_FIRE_7
                        ) {
                            ((Fire) current).update(UPDATE_INTERVAL);
                        }

                        switch (type) {
                            case Tile.TYPE_GRASS:
                                g.drawImage(
                                    grass,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                break;
                            case Tile.TYPE_WALL:
                                g.drawImage(
                                    wall,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                break;
                            case Tile.TYPE_BLOCK:
                                g.drawImage(
                                    block,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                break;
                            case Tile.TYPE_BOMB:
                                g.drawImage(
                                    grass,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                g.drawImage(
                                    bomb.getCurrentImage(),
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    TILE_WIDTH,
                                    TILE_HEIGHT,
                                    this
                                );
                                break;
                            case Fire.TYPE_FIRE_1:
                                g.drawImage(
                                    grass,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                g.drawImage(
                                    ((Fire) current).getFire().getCurrentImage(),
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    TILE_WIDTH,
                                    TILE_HEIGHT,
                                    this
                                );
                                break;
                            case Fire.TYPE_FIRE_2:
                                g.drawImage(
                                    grass,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                g.drawImage(
                                    ((Fire) current).getFire().getCurrentImage(),
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    TILE_WIDTH,
                                    TILE_HEIGHT,
                                    this
                                );
                                break;
                            case Fire.TYPE_FIRE_3:
                                g.drawImage(
                                    grass,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                g.drawImage(
                                    ((Fire) current).getFire().getCurrentImage(),
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    TILE_WIDTH,
                                    TILE_HEIGHT,
                                    this
                                );
                                break;
                            case Fire.TYPE_FIRE_4:
                                g.drawImage(
                                    grass,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                g.drawImage(
                                    ((Fire) current).getFire().getCurrentImage(),
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    TILE_WIDTH,
                                    TILE_HEIGHT,
                                    this
                                );
                                break;
                            case Fire.TYPE_FIRE_5:
                                g.drawImage(
                                    grass,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                g.drawImage(
                                    ((Fire) current).getFire().getCurrentImage(),
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    TILE_WIDTH,
                                    TILE_HEIGHT,
                                    this
                                );
                                break;
                            case Fire.TYPE_FIRE_6:
                                g.drawImage(
                                    grass,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                g.drawImage(
                                    ((Fire) current).getFire().getCurrentImage(),
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    TILE_WIDTH,
                                    TILE_HEIGHT,
                                    this
                                );
                                break;
                            case Fire.TYPE_FIRE_7:
                                g.drawImage(
                                    grass,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                g.drawImage(
                                    ((Fire) current).getFire().getCurrentImage(),
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    TILE_WIDTH,
                                    TILE_HEIGHT,
                                    this
                                );
                                break;
                            case SpecialTile.TYPE_EXIT:
                                g.drawImage(
                                    doors,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    TILE_WIDTH,
                                    TILE_HEIGHT,
                                    this
                                );
                                break;
                            case SpecialTile.TYPE_SPEED:
                                g.drawImage(
                                    iSpeed,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                break;
                            case SpecialTile.TYPE_BOMB:
                                g.drawImage(
                                    iBomb,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                break;
                            case SpecialTile.TYPE_FIRE:
                                g.drawImage(
                                    iFire,
                                    MOVE_SCREEN_X + current.getX() * TILE_WIDTH,
                                    MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT,
                                    this
                                );
                                break;
                            default:
                                break;
                        }
                    }
                }

                /**
                 * Draw enemies
                 */
                for (Enemy e : enemies) {
                    if (e.isAlive()) {
                        g.drawImage(
                            enemy.getCurrentImage(),
                            MOVE_SCREEN_X + (int) e.getX(),
                            MOVE_SCREEN_Y + (int) e.getY(),
                            TILE_WIDTH,
                            TILE_HEIGHT,
                            this
                        );
                    }
                    if (e.isDying()) {
                        g.drawImage(
                            e.getDyingEnemy().getCurrentImage(),
                            MOVE_SCREEN_X + (int) e.getX(),
                            MOVE_SCREEN_Y + (int) e.getY(),
                            TILE_WIDTH,
                            TILE_HEIGHT,
                            this
                        );
                    }
                }

                g.drawImage(wallUpLeft, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y - 32, 32, 32, this);
                g.drawImage(wallDownLeft, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y + 352, 32, 32, this);
                g.drawImage(left, MOVE_SCREEN_X - 48, MOVE_SCREEN_Y - 32, 16, 32, this);
                g.drawImage(leftDown, MOVE_SCREEN_X - 48, MOVE_SCREEN_Y + 352, 16, 32, this);

                for (int i = 1; i <= 17; i++) {
                    g.drawImage(wallUp, MOVE_SCREEN_X - 32 + i * 32, MOVE_SCREEN_Y - 32, 32, 32, this);
                    g.drawImage(wallDown, MOVE_SCREEN_X - 32 + i * 32, MOVE_SCREEN_Y + 352, 32, 32, this);
                }
                g.drawImage(wallUpRight, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y - 32, 32, 32, this);
                g.drawImage(wallDownRight, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 352, 32, 32, this);
                g.drawImage(right, MOVE_SCREEN_X - 32 + 19 * 32, MOVE_SCREEN_Y - 32, 16, 32, this);
                g.drawImage(rightDown, MOVE_SCREEN_X - 32 + 19 * 32, MOVE_SCREEN_Y + 352, 16, 32, this);

                g.drawImage(wallRight, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y, 32, 32, this);
                g.drawImage(wallRight, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 32, 32, 32, this);
                g.drawImage(wallRightWindowStart, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 32, 32, 32, this);
                g.drawImage(wallRightWindowEnd, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 64, 32, 32, this);
                g.drawImage(wallRight, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 96, 32, 32, this);
                g.drawImage(wallRight, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 128, 32, 32, this);
                g.drawImage(wallRight, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 160, 32, 32, this);
                g.drawImage(wallRight, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 192, 32, 32, this);
                g.drawImage(wallRightWindowStart, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 224, 32, 32, this);
                g.drawImage(wallRightWindowEnd, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 256, 32, 32, this);
                g.drawImage(wallRight, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 288, 32, 32, this);
                g.drawImage(wallRight, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 320, 32, 32, this);

                for (int i = 0; i < 11; i++) {
                    g.drawImage(right,  MOVE_SCREEN_X + 18 * 32, MOVE_SCREEN_Y + i * 32, 16, 32, this);
                }

                g.drawImage(wallLeft, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y, 32, 32, this);
                g.drawImage(wallLeftWindowStart, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y + 32, 32, 32, this);
                g.drawImage(wallLeftWindowEnd, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y + 64, 32, 32, this);
                g.drawImage(wallLeft, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y + 96, 32, 32, this);
                g.drawImage(wallLeft, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y + 128, 32, 32, this);
                g.drawImage(wallLeft, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y + 160, 32, 32, this);
                g.drawImage(wallLeft, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y + 192, 32, 32, this);
                g.drawImage(wallLeftWindowStart, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y + 224, 32, 32, this);
                g.drawImage(wallLeftWindowEnd, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y + 256, 32, 32, this);
                g.drawImage(wallLeft, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y + 288, 32, 32, this);
                g.drawImage(wallLeft, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y + 320, 32, 32, this);

                for (int i = 0; i < 11; i++) {
                    g.drawImage(left, MOVE_SCREEN_X - 48, MOVE_SCREEN_Y + i * 32, 16, 32, this);
                }

                /**
                 * Draw players
                 */
                for (int i = 0; i < this.player.length; i++) {
                    g.drawImage(
                        character[i].getCurrentImage(),
                        MOVE_SCREEN_X + (int) player[i].getX(),
                        MOVE_SCREEN_Y + (int) player[i].getY(),
                        player[i].getPlayerWidth(),
                        player[i].getPlayerHeight(),
                        this
                    );
                }

                /**
                 * Draw top panel
                 */
                g.setFont(new Font("Courier new", Font.BOLD, 17));
                g.setColor(Color.white);

                g.drawImage(top, 0, 0, 642, 48, this);

                // @todo celý top panel
                g.drawString("" + playerInfo[0].getScore(), 40, 29);
                g.drawString(mapGenerator.getTime(), 163, 29);

                g.drawString(String.valueOf(player[0].getPlayerBombs()), 253, 29);
                g.drawString(String.valueOf(player[0].getBombFire()), 307, 29);
                g.drawString(String.valueOf(player[0].getPlayerSpeed() - 2), 361, 29);


                g.drawString("" + level, 415, 29);
                g.drawString(playerInfo[0].getName(), 470, 29);
            }
        }
    }

    /**
     * Set game type.
     * @param type type
     */
    private void setType(final int type) {
        this.type = type;
    }

    /**
     * Get game type.
     * @return type
     */
    public final int getType() {
        return type;
    }

    /**
     * Is game running.
     * @return running
     */
    public final boolean isRunning() {
        return running;
    }

    /**
     * Is player(s) alive.
     * @return alive
     */
    private boolean playersAlive() {
        for (int i = 0; i < this.player.length; i++) {
            if (!this.player[i].isAlive()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Collision getter.
     * @return collision
     */
    public final Collision getCollision() {
        return collision;
    }
}
