import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;


public class Player {
	
	private double x = 0;
	private double y = 0;
	private double xMove = 0;
	private double yMove = 0;
	
	private boolean isAlive = true;
	private boolean movingLeft = false;
	private boolean movingRight = false;
	private boolean movingUp = false;
	private boolean movingDown = false;
	
	private int playerSpeed = 3;
	private int playerBombs = 1;
	private int playerHealth = 1;
	
	private int bombFire = 1;
	private int bombTime = 2;
	
	private ArrayList<Bomb> bombs = new ArrayList<Bomb>();
	private ArrayList<Bomb> gonnaBoom = new ArrayList<Bomb>();
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private int numberOfWalkableBombs = 0;
	
	private Game game;
	private Tile[][] map = null;

	private static final double INCREMENT = 0.5;
	private static final int PLAYER_WIDTH = 32;
	private static final int PLAYER_HEIGHT = 32;
	private static final int SPRITE = 32;
	private static final int MAXIMUM_SPEED = 6;
	private static final int MAXIMUM_BOMBS = 6;
	private static final int MAXIMUM_FIRE = 6;
	
	private Rectangle2D playerRect;
	private Rectangle collisionTile;
	private Timer timer;

	public Player(Game game) {
		this.game = game;
	}
	
	public void setMap(Tile[][] map) {
		this.map = map;
	}

	/**
	 * Zmena poz�cie hr��a, v pr�pade �e sa hr�� sna�� �s� aj hore aj dole(vpravo, v�avo) tak mu to zak�eme.
	 */
	public void update() {
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
		if (map != null) twoArrowsUpdate();
		
	}
	

	/**
	 * Zmena poz�cie hr��a, kontrola r�znych kol�zii(nem��e v�js� z mapy, nem��e �s� cez stenu..).
	 */
	public void twoArrowsUpdate() {
		/**
		 * movement update
		 */
		if (x + PLAYER_WIDTH + xMove >= map[0].length * SPRITE) {
			x = map[0].length * SPRITE - PLAYER_WIDTH;
		} else {
			x += xMove;
		}
		if (x + xMove < 0) {
			x = 0;
		}
		
		
		if (y + PLAYER_HEIGHT + yMove >= map.length * SPRITE) {
			y = map.length * SPRITE - PLAYER_HEIGHT;
		} else {
			y += yMove;
		}
		if (y + yMove < 0) {
			y = 0;
		}
		
		/**
		 * Special items collision
		 */
		SpecialTile specialTile = specialCollision();
		if (specialTile != null) {
			int specialType = specialTile.getSpecial();
			if (specialType == 1) {
				game.nextLevel();
			} else if (specialType == 2) {
				if (playerSpeed + 1 <= MAXIMUM_SPEED) playerSpeed++;
				setTile(specialTile.getX(), specialTile.getY(), 0, true);
			} else if (specialType == 3) {
				if (playerBombs + 1 <= MAXIMUM_BOMBS) playerBombs++;
				setTile(specialTile.getX(), specialTile.getY(), 0, true);
			} else if (specialType == 4) {
				if (bombFire + 1 <= MAXIMUM_FIRE) bombFire++;
				setTile(specialTile.getX(), specialTile.getY(), 0, true);
			}
		}
		
		/**
		 * Bomb walkable setup
		 */
		if (!bombCollision() && numberOfWalkableBombs >= 1) {
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
		if (collision()) {
			y -= yMove;
			if (collision()) {
				x -= xMove;
				y+= yMove;
				if (collision()) {
					y -= yMove;
				}
			}
		}
	}
	
	/**
	 * Kontrola kol�zie medzi hr��om a prvkom mapy(napr. stena).
	 * @return true, v pr�pade kol�zie
	 */
	public boolean collision() {
		ArrayList<Rectangle> rect = new ArrayList<Rectangle>();
	    playerRect = new Rectangle2D.Double(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);

	    for(int i = 0; i < map[0].length; i++) {
	        for(int j = 0; j < map.length; j++) {
	            if(map[j][i].isWalkable() == false) {
	                Rectangle tileRect = new Rectangle(i*32, j*32, 32, 32);
	                rect.add(tileRect);
	            }
	        }
	    }  
	    for(Rectangle collision : rect) {
	        if(collision.intersects(playerRect)) {
	            return true;
	        }
	    }
	    
	    return false;
	}
	
	/**
	 * Kontrola kol�zie medzi hr��om a bombou.
	 * @return true v pr�pade kol�zie
	 */
	public boolean bombCollision() {
		ArrayList<Rectangle> rect = new ArrayList<Rectangle>();
	    playerRect = new Rectangle2D.Double(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
	    
	    for(int i = 0; i < map[0].length; i++) {
	        for(int j = 0; j < map.length; j++) {
	            if(map[j][i].isWalkable() == true && map[j][i].getType() == 3) {
	                Rectangle tileRect = new Rectangle(i*32, j*32, 32, 32);
	                rect.add(tileRect);
	            }
	        }
	    }  
	    for(Rectangle collision : rect) {
	        if(collision.intersects(playerRect)) {
	        	collisionTile = collision;
	            return true;
	        }
	    }
	    
	    return false;
	}
	
	/**
	 * Kontrola kol�zie medzi hr��om a power up-om(zv��enie po�tu b�mb, zv��enie dosahu bomby, r�chlosti, v�chod).
	 * @return pol��ko kde vznikla kol�zia
	 */
	public SpecialTile specialCollision() {
		ArrayList<Rectangle> rect = new ArrayList<Rectangle>();
	    playerRect = new Rectangle2D.Double(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
	    
	    for(int i = 0; i < map[0].length; i++) {
	        for(int j = 0; j < map.length; j++) {
	            if(map[j][i].isWalkable() == true && map[j][i].getType() >= 30) {
	                Rectangle tileRect = new Rectangle(i*32, j*32, 32, 32);
	                rect.add(tileRect);
	            }
	        }
	    }  
	    for(Rectangle collision : rect) {
	        if(collision.intersects(playerRect)) {
	            return (SpecialTile) map[(collision.y / SPRITE)][(collision.x / SPRITE)];
	        }
	    }
	    
	    return null;
	}
	
	/**
	 * Vr�ti pol��ko kde vznikla kol�zia.
	 * @return Tile
	 */
	public Tile getCollisionTile() {
		int mapX = collisionTile.x / SPRITE;
		int mapY = collisionTile.y / SPRITE;
		return map[mapY][mapX];
	}
	
	public void moveUp() {
		yMove = -INCREMENT;
	}
	
	public void moveDown() {
		yMove = INCREMENT;
	}
	
	public void moveLeft() {
		xMove = -INCREMENT;
	}
	
	public void moveRight() {
		xMove = INCREMENT;
	}
	
	/**
	 * Zastav� pohyb hr��a smerom hore
	 */
	public void stopUp() {
		setMovingUp(false);
		stopY();
	}
	
	/**
	 * Zastav� pohyb hr��a smerom dole
	 */
	public void stopDown() {
		setMovingDown(false);
		stopY();
	}
	
	/**
	 * Kontrola �i sa hr�� neh�be e�te v inom vertik�lnom smere
	 */
	public void stopY() {
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
	 * Zastav� pohyb hr��a smerom v�avo
	 */
	public void stopLeft() {
		setMovingLeft(false);
		stopX();
	}
	
	/**
	 * Zastav� pohyb hr��a smerom vpravo
	 */
	public void stopRight() {
		setMovingRight(false);
		stopX();
	}
	
	/**
	 * Kontrola �i sa hr�� neh�be e�te v inom horizont�lnom smere
	 */
	public void stopX() {
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
	 * Overenie �i hr�� m��e polo�i� bombu.
	 * @return true, ak m��e polo�i� bombu
	 */
	public boolean canSetBomb() {
		return (bombs.size() < playerBombs);
	}
	
	/**
	 * Polo�enie bomby
	 */
	public void setBomb() {
		if (canSetBomb()) {
			int tileY = (int)Math.round((y / SPRITE));
			int tileX = (int)Math.round((x  / SPRITE));
			
			if (map[tileY][tileX].getType() != 3) {
				setTile(tileX, tileY, 3, true);
				bombs.add((Bomb) map[tileY][tileX]);
				numberOfWalkableBombs++;
			}
		}
	}
	
	/**
	 * V�buch bomby, timer kv�li anim�cii v�buchu, kontrola kol�zii pri v�buchu
	 * @param x
	 * @param y
	 * @param bombFire
	 */
	public void boom(int x, int y, int bombFire) {
		setTile(x, y, 0, true);
		checkFireCollison(x, y, bombFire, "fire");
		
		boolean collision = collision();
		
		timer = new Timer(319, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				checkFireCollison(x, y, bombFire, "resolve");
				if (collision) {
					decreasePlayerHealth(1);
				}
				bombOtherBombs();
			}
		});
		timer.start();

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
	 * V pr�pade, �e plame� jednej bomby zasiahol in� bombu, tak aj t�to bomba(y) mus�(ia) hne� vybuchn��
	 */
	public void bombOtherBombs() {
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
	 * Kontrola kol�zie plame�ov.
	 * 4 cykly z toho d�vodu �e plame� za��na v strede a odtia� ide smerom hore, dole, vpravo, v�avo.
	 * Pokia� naraz� na stenu tak plame� nem��e pokra�ova� �alej.
	 * V pr�pade, �e plame� zni�il nejak� blok tak sa tam n�hodne m��e objavi� �peci�lna polo�ka(zv��enie plame�a, r�chlosti, po�et b�mb) popr. v�chod.
	 * @param x horizont�lna poz�cia
	 * @param y vertik�lna poz�cia
	 * @param bombFire ve�kos� plame�a
	 * @param action v�buch alebo koniec v�buchu
	 */
	public void checkFireCollison(int x, int y, int bombFire, String action) {
		int yFrom = (y - bombFire >= 0) ? (y - bombFire) : 0;
		int yTo = (y + bombFire <= map.length - 1) ? (y + bombFire) : map.length - 1;
		int xFrom = (x - bombFire >= 0) ? (x - bombFire) : 0;
		int xTo = (x + bombFire <= map[0].length - 1) ? (x + bombFire) : map[0].length - 1;
		
		if (action == "fire") blocks.clear();
		
		boolean stop = false; // do not let the bomb bomb threw the wall or more than 1 block
		for (int i = y; i >= yFrom; i--) {
			if (action == "fire") {
				if (map[i][x].getType() == 3) {
					
					Bomb bomb = (Bomb) map[i][x];
					gonnaBoom.add(bomb);
				}
				if (map[i][x].getType() != 1 && map[i][x].getType() != 30) {
					if (!stop) {
						if (map[i][x].getType() == 2) {
							blocks.add((Block) map[i][x]);
							stop = true;
						}
						if (i == yFrom) {
							setTile(x, i, 20, false);
						} else {
							setTile(x, i, 22, false);
						}
					}
				} else {
					stop = true;
				}
			} else if (action == "resolve") {
				if (!stop) {
					if (map[i][x].getType() != 1 && map[i][x].getType() != 2 && map[i][x].getType() != 30) {
						setTile(x, i, 0, true);
					} else {
						stop = true;
					}
				}
				
			}
			
		}
		stop = false;
		for (int i = y; i <= yTo; i++) {
			if (action == "fire") {
				if (map[i][x].getType() == 3) {
					Bomb bomb = (Bomb) map[i][x];
					gonnaBoom.add(bomb);
				}
				if (map[i][x].getType() != 1 && map[i][x].getType() != 30) {
					if (!stop) {
						if (map[i][x].getType() == 2) {
							blocks.add((Block) map[i][x]);
							stop = true;
						}
						if (i == y)  {
							setTile(x, i, 23, false);
						} else if (i == yTo)  {
							setTile(x, i, 21, false);
						} else {
							setTile(x, i, 22, false);
						}
					}
				} else {
					stop = true;
				}
			} else if (action == "resolve") {
				if (!stop) {
					if (map[i][x].getType() != 1 && map[i][x].getType() != 2 && map[i][x].getType() != 30) {
						setTile(x, i, 0, true);
					} else {
						stop = true;
					}
				}
			}
			
		}
		stop = false;
		for (int j = x; j >= xFrom; j--) {
			if (action == "fire") {
				if (map[y][j].getType() == 3) {
					Bomb bomb = (Bomb) map[y][j];
					gonnaBoom.add(bomb);
				}
				if (map[y][j].getType() != 1 && map[y][j].getType() != 30) {
					if (!stop) {
						if (map[y][j].getType() == 2) {
							blocks.add((Block) map[y][j]);
							stop = true;
						}
						if (j == xFrom) {
							setTile(j, y, 24, false);
						} else {
							setTile(j, y, 26, false);
						}
					}
					
				} else {
					stop = true;
				}
			} else if (action == "resolve") {
				if (!stop) {
					if (map[y][j].getType() != 1 && map[y][j].getType() != 2 && map[y][j].getType() != 30) {
						setTile(j, y, 0, true);
					} else {
						stop = true;
					}
				}
			}
		}
		stop = false;
		for (int j = x; j <= xTo; j++) {
			if (action == "fire") {
				if (map[y][j].getType() == 3) {
					Bomb bomb = (Bomb) map[y][j];
					gonnaBoom.add(bomb);

				}
				if (map[y][j].getType() != 1 && map[y][j].getType() != 30) {
					if (!stop) {
						if (map[y][j].getType() == 2) {
							blocks.add((Block) map[y][j]);
							stop = true;
						}
						if (j == x) {
							setTile(j, y, 23, false);
						} else if (j == xTo) {
							setTile(j, y, 25, false);
						} else {
							setTile(j, y, 26, false);
						}
					}
				} else {
					stop = true;
				}
			} else if (action == "resolve") {
				if (!stop) {
					if (map[y][j].getType() != 1 && map[y][j].getType() != 2 && map[y][j].getType() != 30) {
						setTile(j, y, 0, true);
					} else {
						stop = true;
					}
				}
			}
		}
		
		if (action == "resolve") {
			for (int b = 0; b < blocks.size(); b++) {
				if (blocks.get(b).hasExit() == true) {
					setTile(blocks.get(b).getX(), blocks.get(b).getY(), 30, true, 1);
				} else {
					Random r = new Random();
					int special = r.nextInt(2);
					if (special == 1) {
						int item = r.nextInt(3) + 2;
						if (item == 2) {
							setTile(blocks.get(b).getX(), blocks.get(b).getY(), 31, true, 2);
						} else if (item == 3) {
							setTile(blocks.get(b).getX(), blocks.get(b).getY(), 32, true, 3);
						} else if (item == 4) {
							setTile(blocks.get(b).getX(), blocks.get(b).getY(), 33, true, 4);
						}
					} else {
						setTile(blocks.get(b).getX(), blocks.get(b).getY(), 0, true);
					}
					
				}
			}
		}
		
	}
	
	
	/**
	 * Nastavenia pol��ka mapy
	 * @param x horizont�lna poz�cia
	 * @param y vertik�lna poz�cia
	 * @param type typ pol��ka
	 * @param walkable d� sa cez pol��ko prech�dza�?
	 */
	public void setTile(int x, int y, int type, boolean walkable) {
		if (isAlive) {
			if (type >= 20 && type <= 26) {
				map[y][x] = new Fire(x, y, type, walkable, game);
			} else if (type == 3) {
				map[y][x] = new Bomb(x, y, type, walkable, bombTime, bombFire, this);
			} else {
				map[y][x] = new Tile(x, y, type, walkable);
			}
			game.setMap(map);
		}
	}
	
	/**
	 * Nastavenia pol��ka mapy pre �peci�lne polo�ky(power-upy + v�chod)
	 * @param x horizont�lna poz�cia
	 * @param y vertik�lna poz�cia
	 * @param type typ pol��ka
	 * @param walkable d� sa cez pol��ko prech�dza�?
	 * @param special typ �peci�lneho pol��ka
	 */
	public void setTile(int x, int y, int type, boolean walkable, int special) {
		if (isAlive) {
			if (type >= 30) {
				map[y][x] = new SpecialTile(x, y, type, walkable, special);
			} 
			game.setMap(map);
		}
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public boolean isMovingLeft() {
		return movingLeft;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public boolean isMovingUp() {
		return movingUp;
	}

	public void setMovingUp(boolean movingUp) {
		this.movingUp = movingUp;
	}

	public boolean isMovingDown() {
		return movingDown;
	}

	public void setMovingDown(boolean movingDown) {
		this.movingDown = movingDown;
	}
	
	public int getPlayerWidth() {
		return PLAYER_WIDTH;
	}

	public int getPlayerHeight() {
		return PLAYER_HEIGHT;
	}
	
	public int getSpeed() {
		return playerSpeed;
	}
	
	public int getBombFire() {
		return bombFire;
	}
	
	public int getBombTime() {
		return bombTime;
	}

	public void setBombTime(int bombTime) {
		this.bombTime = bombTime;
	}

	/**
	 * Zn�enie �ivotov hr��a = zabitie(je to navrhnut� tak, �eby hr�� mohol ma� teoreticky aj viac ako 1 �ivot)
	 * @param number po�et �ivotov
	 */
	public void decreasePlayerHealth(int number) {
		this.playerHealth -= number;
		if (this.playerHealth <= 0) {
			isAlive = false;
		}
	}
	
	public int getPlayerSpeed() {
		return playerSpeed;
	}

	public int getPlayerBombs() {
		return playerBombs;
	}
	
}
