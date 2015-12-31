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


@SuppressWarnings("serial")
public class Game extends JPanel implements KeyListener {

	private Player player;
	private PlayerInfo playerInfo;
	private String name;
	private BufferedImage sprites, grass, wall, block, doors, iSpeed, iBomb, iFire, top,
						  wallUpLeft, wallUp, wallUpRight, wallRight, wallRightWindowStart, wallRightWindowEnd,
						  wallDownLeft, wallDown, wallDownRight, wallLeft, wallLeftWindowStart, wallLeftWindowEnd, left, leftDown, right, rightDown;
	private Animation downAnim, upAnim, leftAnim, rightAnim, character, bomb, enemy;
	private Map mapGenerator;
	private ArrayList<Enemy> enemies;
	private Tile[][] map = null;
	private Collision collision;
	
	private static final int TILE_WIDTH = 32;
	private static final int TILE_HEIGHT = 32;
	
	private static final int SPRITE = 24;
	
	private int level = 1;
	private int levels = 3;
	
	private static final int MOVE_SCREEN_X = 48;
	private static final int MOVE_SCREEN_Y = 80;
	
	private Views views;
	private boolean running = true;

	/**
	 * Inicializ·cia obr·zkov, jpanelu, anim·cii.
	 * @param views
	 */
	public Game(Views views) {
		this.views = views;
		enemies = new ArrayList<Enemy>();
		collision = new Collision(this);
		playerInfo = new PlayerInfo(0, "");
		setFocusable(true);
		addKeyListener(this);
		try {
			sprites = ImageIO.read(getClass().getClassLoader().getResource("data/sprites.png"));
		    grass = ImageIO.read(getClass().getClassLoader().getResource("data/grass2.png"));
		    wall = ImageIO.read(getClass().getClassLoader().getResource("data/wall.png"));
		    block = ImageIO.read(getClass().getClassLoader().getResource("data/block.png"));
		    doors = ImageIO.read(getClass().getClassLoader().getResource("data/doors.png"));
		    iSpeed = ImageIO.read(getClass().getClassLoader().getResource("data/iSpeed.png"));
		    iBomb = ImageIO.read(getClass().getClassLoader().getResource("data/iBomb.png"));
		    iFire = ImageIO.read(getClass().getClassLoader().getResource("data/iFire.png"));
		    top = ImageIO.read(getClass().getClassLoader().getResource("data/top.png"));
		    
		    wallUpLeft = sprites.getSubimage(431, 110, 16, 16);
		    wallUp = sprites.getSubimage(447, 110, 16, 16);
		    wallUpRight = sprites.getSubimage(479, 110, 16, 16);
		    wallRight = sprites.getSubimage(495, 110, 16, 16);
		    wallRightWindowStart = sprites.getSubimage(511, 110, 16, 16);
		    wallRightWindowEnd = sprites.getSubimage(527, 110, 16, 16);
		    wallDownLeft = sprites.getSubimage(543, 110, 16, 16);
		    wallDown = sprites.getSubimage(559, 110, 16, 16);
		    wallDownRight = sprites.getSubimage(591, 110, 16, 16);
		    wallLeft = sprites.getSubimage(623, 110, 16, 16);
		    wallLeftWindowStart = sprites.getSubimage(639, 110, 16, 16);
		    wallLeftWindowEnd = sprites.getSubimage(655, 110, 16, 16);
		    left = sprites.getSubimage(463, 126, 8, 16);
		    leftDown = sprites.getSubimage(479, 126, 8, 16);
		    right = sprites.getSubimage(671, 110, 8, 16);
		    rightDown = sprites.getSubimage(367, 126, 8, 16);

		    enemy = new Animation();
		    enemy.addFrame(sprites.getSubimage(394, 233, 16, 18), 200);
		    enemy.addFrame(sprites.getSubimage(410, 233, 16, 18), 200);
		    enemy.addFrame(sprites.getSubimage(426, 233, 16, 18), 200);
		    enemy.setMoving(true);
		    
		    bomb = new Animation();
		    bomb.addFrame(sprites.getSubimage(470, 0, 16, 16), 200);
		    bomb.addFrame(sprites.getSubimage(486, 0, 16, 16), 200);
		    bomb.addFrame(sprites.getSubimage(502, 0, 16, 16), 200);
		    bomb.setMoving(true);
		    
		    downAnim = new Animation();
		    downAnim.addFrame(getImageFromSprite(0, 0), 200);
		    downAnim.addFrame(getImageFromSprite(1, 0), 200);
		    downAnim.addFrame(getImageFromSprite(2, 0), 200);
		    
		    upAnim = new Animation();
		    upAnim.addFrame(getImageFromSprite(9, 0), 200);
		    upAnim.addFrame(getImageFromSprite(10, 0), 200);
		    upAnim.addFrame(getImageFromSprite(10, 0), 200);
		    
		    leftAnim = new Animation();
		    leftAnim.addFrame(getImageFromSprite(6, 0), 200);
		    leftAnim.addFrame(getImageFromSprite(7, 0), 200);
		    leftAnim.addFrame(getImageFromSprite(8, 0), 200);
		    
		    rightAnim = new Animation();
		    rightAnim.addFrame(getImageFromSprite(3, 0), 200);
		    rightAnim.addFrame(getImageFromSprite(4, 0), 200);
		    rightAnim.addFrame(getImageFromSprite(5, 0), 200);
		    
			character = downAnim;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Vr·ti obr·zok z veækÈho obr·zku podæa öpecifickej pozÌcie
	 * @param x horizont·lna pozÌcia
	 * @param y	vertik·lna pozÌcia
	 * @return
	 */
	public BufferedImage getImageFromSprite(int x, int y) {
		return sprites.getSubimage(x * SPRITE, y * SPRITE, SPRITE, SPRITE);
	}
	
	/**
	 * Vr·ti obr·zok z veækÈho obr·zku podæa öpecifickej pozÌcie a rozmerov
	 * @param x horizont·lna pozÌcia
	 * @param y vertik·lna pozÌcia
	 * @param width öÌrka obr·zka
	 * @param height v˝öka obr·zka
	 * @return
	 */
	public BufferedImage getImageFromSprite(int x, int y, int width, int height) {
		return sprites.getSubimage(x, y , width, height);
	}
	
	/**
	 * Spustenie hry. Z·kladnÈ inform·cie ako ovl·daù hru a moûnosù zadaù meno hr·Ëa.
	 */
	public void init() {
		running = true;
		this.revalidate();
		this.repaint();
		name = JOptionPane.showInputDialog(this, "Vitajte v hre bomberman!\nPostavu ovl·date pomocou öÌpok(hore, dole, vpravo, væavo).\nBombu prid·te pomocou medzernÌku.\nCieæom hry je nahraù Ëo najvyööie skÛre. SkÛre zÌskavate za prejdenie levela a zabÌjanie nepriateæov.\nDo Ôalöieho levela sa dostane pokiaæ n·jdete v˝chod. \n\nZadajte Vaöe meno", "Welcome", JOptionPane.PLAIN_MESSAGE);
		if (name != null) {
			start(1);
		} else {
			views.setView("Intro");
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Key listener na medzernÌk a öÌpky(hore, dole, vpravo, væavo)
	 * V prÌpade stlaËenia öÌpky zmena anim·cie postavy a nastavenia, ûe sa hr·Ë h˝be dan˝m smerom.
	 * V prÌpade stlaËenia medzernÌka volanie metÛdy setBomb na inötancii hr·Ëa(poloûenie bomby).
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (KeyEvent.getKeyText(e.getKeyCode())) {
			case "Up": 
				player.moveUp();
				player.setMovingUp(true);
				upAnim.setMoving(true);
				character = upAnim;
				break;
			case "Down": 
				player.moveDown();
				player.setMovingDown(true);
				downAnim.setMoving(true);
				character = downAnim;
				break;
			case "Left": 
				player.moveLeft();
				player.setMovingLeft(true);
				leftAnim.setMoving(true);
				character = leftAnim;
				break;
			case "Right": 
				player.moveRight();
				player.setMovingRight(true);
				rightAnim.setMoving(true);
				character = rightAnim;
				break;
			case "Space":
				player.setBomb();
				break;
			default: break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (KeyEvent.getKeyText(e.getKeyCode())) {
			case "Up": 
				player.stopUp();
				upAnim.setMoving(false);
				break;
			case "Down": 
				player.stopDown();
				downAnim.setMoving(false);
				break;
			case "Left": 
				player.stopLeft();
				leftAnim.setMoving(false);
				break;
			case "Right": 
				player.stopRight();
				rightAnim.setMoving(false);
				break;
			default: break;
		}
	}
	
	public void setMap(Tile[][] map) {
		this.map = map;
	}
	
	public Tile[][] getMap() {
		return map;
	}

	public void setEnemies(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}
	
	/**
	 * NaËÌtanie mapy a nepriateæov
	 * @param level level, ktor˝ chceme naËÌtaù
	 */
	public void loadMap(int level) {
		map = mapGenerator.loadMap(level);
		enemies = mapGenerator.getEnemies();
		mapGenerator.setReady(true);
	}
	
	/**
	 * Spustenie levelu
	 * @param level
	 */
	public void start(int level) {
		enemies.clear();
		if (name.equals("")) name = "BezMena";
		playerInfo.setName(name);
		if (level == 1) playerInfo.setScore(0);
		mapGenerator = new Map(collision);
		loadMap(level);
		player = new Player(this);
		player.setMap(map);
		mapGenerator.countdown();
	}
	
	/**
	 * Hern˝ cyklus. 
	 * V prÌpade, ûe je hr·Ë naûive a m· dostatok Ëasu tak sa hra aktualizuje kaûd˝ch 17ms.
	 * V prÌpade, ûe je hr·Ë m‡tvy alebo mu vypröal Ëas vypÌsanie Ëi chce hraù znova.
	 */
	public void loop() {
		if (player != null) {
			while (player.isAlive() && mapGenerator.getSeconds() >= 0 && running) {
				try {
					this.update();
					this.repaint();
					
					Thread.sleep(17);
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
		if (!player.isAlive() && running) {
			this.repaint();
			Object[] options = {"¡no", "Nie"};
			int n = JOptionPane.showOptionDialog(this, "Boli ste zabit˝. Chcete hraù znova?", "Game Over", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (n == JOptionPane.YES_OPTION) {
				playerInfo.decreaseScore(50);
				start(level);
				loop();
			} else {
				playerInfo.writeScore();
				running = false;
				level = 1;
				views.setView("Intro");
			}
		}
		
		/**
		 * Out of time
		 */
		if (mapGenerator.getSeconds() < 0 && running) {
			playerInfo.writeScore();
			Object[] options = {"¡no", "Nie"};
			int n = JOptionPane.showOptionDialog(this, "Bohuûiaæ, vypröal V·m Ëas. Chcete hraù znova?", "Game Over", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (n == JOptionPane.YES_OPTION) {
				playerInfo.decreaseScore(50);
				start(level);
				loop();
			} else {
				running = false;
				level = 1;
				views.setView("Intro");
			}
		}
		
	}
	
	public PlayerInfo getPlayerInfo() {
		return playerInfo;
	}

	/**
	 * DialÛgy Ëi chce hr·Ë pokraËovaù v Ôalöom leveli alebo hraù od zaËiatku ak preöiel cel˙ hru.
	 */
	public void nextLevel() {
		if (level+1 <= levels) {
			Object[] options = {"¡no", "Nie"};
			int m = JOptionPane.showOptionDialog(this, "Gratulujeme! ⁄speöne ste preöli level " + level + "! Chcete pokraËovaù v Ôalöom leveli?", "Congratulations", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			playerInfo.increaseScore(mapGenerator.getScore());
			level++;
			if (m == JOptionPane.YES_OPTION) {
				start(level);
				loop();
			} else {
				level = 1;
				playerInfo.writeScore();
				running = false;
				views.setView("Intro");
			}
		} else {
			Object[] options = {"¡no", "Nie"};
			playerInfo.increaseScore(mapGenerator.getScore());
			int m = JOptionPane.showOptionDialog(this, "Gratulujeme! ⁄speöne ste preöli cel˙ hru! Vaöe fin·lne skÛre je " + playerInfo.getScore() +  "! Chcete hraù znova?", "Congratulations", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (m == JOptionPane.YES_OPTION) {
				level = 1;
				start(level);
				loop();
			} else {
				level = 1;
				playerInfo.writeScore();
				running = false;
				views.setView("Intro");
			}
		}
	}
	
	/**
	 * Update pozÌcie hr·Ëa, nepriateæov, anim·cii.
	 */
	public void update() {
		
		int speed = player.getSpeed();
		for (int i = 1; i <= speed; i++) {
			player.update();
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
		
		character.update(17);
		for (Enemy e : enemies) {
			if (e.isDying()) {
				e.getDyingEnemy().update(17);
			}
		}
		enemy.update(17);
		bomb.update(17);

	}
	
	public Player getPlayer() {
		return player;
	}

	/**
	 * Vykresæovanie celej hry
	 */
	public void paintComponent(Graphics g) {
		if (mapGenerator != null) {
			if (!player.isAlive()) {
				super.paintComponent(g);
				return;
			}
			if (mapGenerator.isReady()) {
				super.paintComponent(g);

				for (int i = 0; i < map.length; i++) {
					for (int j = 0; j < map[0].length; j++) {
						Tile current = map[i][j];
						int type = current.getType();
						if (type >= 20 && type <= 26) ((Fire) current).update(17);

						if (type == 0) {
							g.drawImage(grass, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
						} else if (type == 1) {
							g.drawImage(wall, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
						} else if(type == 2) {
							g.drawImage(block, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
						} else if (type == 3) {
							g.drawImage(grass, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
							g.drawImage(bomb.getCurrentImage(), MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, this);
						} else if (type == 20) {
							g.drawImage(grass, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
							g.drawImage(((Fire) current).getFire().getCurrentImage(), MOVE_SCREEN_X +current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, this);
						} else if (type == 21) {
							g.drawImage(grass, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
							g.drawImage(((Fire) current).getFire().getCurrentImage(), MOVE_SCREEN_X +current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, this);
						} else if (type == 22) {
							g.drawImage(grass, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
							g.drawImage(((Fire) current).getFire().getCurrentImage(), MOVE_SCREEN_X +current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, this);
						} else if (type == 23) {
							g.drawImage(grass, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
							g.drawImage(((Fire) current).getFire().getCurrentImage(), MOVE_SCREEN_X +current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, this);
						} else if (type == 24) {
							g.drawImage(grass, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
							g.drawImage(((Fire) current).getFire().getCurrentImage(), MOVE_SCREEN_X +current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, this);
						} else if (type == 25) {
							g.drawImage(grass, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
							g.drawImage(((Fire) current).getFire().getCurrentImage(), MOVE_SCREEN_X +current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, this);
						} else if (type == 26) {
							g.drawImage(grass, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
							g.drawImage(((Fire) current).getFire().getCurrentImage(), MOVE_SCREEN_X +current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, this);
						} else if (type == 30) {
							g.drawImage(doors, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, this);
						} else if (type == 31) {
							g.drawImage(iSpeed, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
						} else if (type == 32) {
							g.drawImage(iBomb, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
						} else if (type == 33) {
							g.drawImage(iFire, MOVE_SCREEN_X + current.getX() * TILE_WIDTH, MOVE_SCREEN_Y + current.getY() * TILE_HEIGHT, this);
						}
						
					}
				}
				
				/**
				 * Draw enemies
				 */
				for (Enemy e : enemies) {
					if (e.isAlive()) {
						g.drawImage(enemy.getCurrentImage(), MOVE_SCREEN_X + (int)e.getX(), MOVE_SCREEN_Y + (int)e.getY(), TILE_WIDTH, TILE_HEIGHT, this);
					}
					if (e.isDying()) {
						g.drawImage(e.getDyingEnemy().getCurrentImage(), MOVE_SCREEN_X + (int)e.getX(), MOVE_SCREEN_Y + (int)e.getY(), TILE_WIDTH, TILE_HEIGHT, this);
					}
					
				}
				
				g.drawImage(wallUpLeft, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y - 32, 32, 32, this);
				g.drawImage(wallDownLeft, MOVE_SCREEN_X - 32, MOVE_SCREEN_Y + 352, 32, 32, this);
				g.drawImage(left, MOVE_SCREEN_X - 48, MOVE_SCREEN_Y - 32, 16, 32, this);
				g.drawImage(leftDown, MOVE_SCREEN_X - 48, MOVE_SCREEN_Y + 352, 16, 32, this);
				//if (level == 1) {
					for (int i = 1; i <= 17; i++) {
						g.drawImage(wallUp, MOVE_SCREEN_X - 32 + i * 32, MOVE_SCREEN_Y - 32, 32, 32, this);
						g.drawImage(wallDown, MOVE_SCREEN_X - 32 + i * 32, MOVE_SCREEN_Y + 352, 32, 32, this);
					}
					g.drawImage(wallUpRight, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y - 32, 32, 32, this);
					g.drawImage(wallDownRight, MOVE_SCREEN_X - 32 + 18 * 32, MOVE_SCREEN_Y + 352, 32, 32, this);
					g.drawImage(right, MOVE_SCREEN_X - 32 + 19 * 32, MOVE_SCREEN_Y - 32, 16, 32, this);
					g.drawImage(rightDown, MOVE_SCREEN_X - 32 + 19 * 32, MOVE_SCREEN_Y + 352, 16, 32, this);
				//}
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
				
				
				g.drawImage(wallLeft, MOVE_SCREEN_X - 32 , MOVE_SCREEN_Y, 32, 32, this);
				g.drawImage(wallLeftWindowStart, MOVE_SCREEN_X - 32 , MOVE_SCREEN_Y + 32, 32, 32, this);
				g.drawImage(wallLeftWindowEnd, MOVE_SCREEN_X - 32 , MOVE_SCREEN_Y + 64, 32, 32, this);
				g.drawImage(wallLeft, MOVE_SCREEN_X - 32 , MOVE_SCREEN_Y + 96, 32, 32, this);
				g.drawImage(wallLeft, MOVE_SCREEN_X - 32 , MOVE_SCREEN_Y + 128, 32, 32, this);
				g.drawImage(wallLeft, MOVE_SCREEN_X - 32 , MOVE_SCREEN_Y + 160, 32, 32, this);
				g.drawImage(wallLeft, MOVE_SCREEN_X - 32 , MOVE_SCREEN_Y + 192, 32, 32, this);
				g.drawImage(wallLeftWindowStart, MOVE_SCREEN_X - 32 , MOVE_SCREEN_Y + 224, 32, 32, this);
				g.drawImage(wallLeftWindowEnd, MOVE_SCREEN_X - 32 , MOVE_SCREEN_Y + 256, 32, 32, this);
				g.drawImage(wallLeft, MOVE_SCREEN_X - 32 , MOVE_SCREEN_Y + 288, 32, 32, this);
				g.drawImage(wallLeft, MOVE_SCREEN_X - 32 , MOVE_SCREEN_Y + 320, 32, 32, this);
				
				for (int i = 0; i < 11; i++) {
					g.drawImage(left, MOVE_SCREEN_X - 48, MOVE_SCREEN_Y + i * 32, 16, 32, this);
				}
				
				/**
				 * Draw player
				 */
				g.drawImage(character.getCurrentImage(), MOVE_SCREEN_X +(int)player.getX(), MOVE_SCREEN_Y + (int)player.getY(), player.getPlayerWidth(), player.getPlayerHeight(), this);
			
			
				/**
				 * Draw top panel
				 */
				g.setFont(new Font("Courier new", Font.BOLD, 17));
				g.setColor(Color.white);
				
				g.drawImage(top, 0, 0, 642, 48, this);
				
				g.drawString("" + playerInfo.getScore(), 40, 29);
				g.drawString(mapGenerator.getTime(), 163, 29);

				g.drawString(String.valueOf(player.getPlayerBombs()), 253, 29);
				g.drawString(String.valueOf(player.getBombFire()), 307, 29);
				g.drawString(String.valueOf(player.getPlayerSpeed()-2), 361, 29);
				
				
				g.drawString("" + level, 415, 29);
				g.drawString(playerInfo.getName(), 470, 29);
			}
		}
	}
	

}
