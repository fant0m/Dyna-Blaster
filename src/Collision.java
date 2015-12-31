import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.Timer;


public class Collision {
	
	private Game game;
	private Timer timer;

	public Collision(Game game) {
		this.game = game;
	}
	
	/**
	 * Kontrola kolízie nepriate¾a
	 * @param x horizontálna pozícia
	 * @param y	vertikálna pozícia
	 * @param direction	smer, ktorım sa nepriate¾ chce pohnú
	 * @param speed rıchlos pohybu nepriate¾a
	 * @param enemy inštancia nepriate¾a
	 * @return vráti true pokia¾ sa nepriate¾ nemôe pohnú danım smerom
	 */
	public boolean checkEnemyCollision(double x, double y, int direction, double speed, Enemy enemy) {
		Rectangle2D enemyRect = null;
		ArrayList<Rectangle> rect = new ArrayList<Rectangle>();
	    Tile[][] map = game.getMap();
	    
		if (direction == 1) {
			enemyRect = new Rectangle2D.Double(x, y-speed, 32, 32);
			if (y-speed < 0) return true;
		} else if (direction == 2) {
			enemyRect = new Rectangle2D.Double(x, y+speed, 32, 32);
			if (y+speed+32 > map.length * 32) return true;
		} else if (direction == 3) {
			enemyRect = new Rectangle2D.Double(x-speed, y, 32, 32);
			if (x-speed < 0) return true;
		} else if (direction == 4) {
			enemyRect = new Rectangle2D.Double(x+speed, y, 32, 32);
			if (x+speed+32 > map[0].length * 32) return true;
		}

	    for(int i = 0; i < map[0].length; i++) {
	        for(int j = 0; j < map.length; j++) {
	            if(map[j][i].isWalkable() == false || map[j][i].getType() == 3) {
	                Rectangle tileRect = new Rectangle(i*32, j*32, 32, 32);
	                rect.add(tileRect);
	            }
	        }
	    }  
	    for(Rectangle collision : rect) {
			if(collision.intersects(enemyRect)) {
				int tile = map[collision.y / 32][collision.x / 32].getType();
				/**
				 * Ak je nepriate¾ v kolízii s plameòom, treba ho zabi + zvıši hráèovi skóre
				 */
				if (tile >= 20 && tile <= 26) {
					timer = new Timer(400, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							enemy.setDying(false);
							enemy.setAlive(false);
							timer.stop();
						}
					});
					timer.start();
					enemy.setDying(true);
					enemy.getDyingEnemy().setMoving(true);
					
					if (enemy.hasReceivedScore() == false) {
						game.getPlayerInfo().increaseScore(10);
						enemy.setReceivedScore(true);
					}
					
				}
	            return true;
	        }
	    }
	    
	    return false;
	}

	/**
	 * Kontrola kolízie medzi nepriate¾om a hráèom. V prípade zistenia kolízie treba hráèovi zníi ivot(zabi ho).
	 * @param x horizontána pozícia nepriate¾a
	 * @param y vertikálna pozícia nepriate¾a
	 */
	public void checkEnemyWithPlayerCollision(double x, double y) {
		Rectangle2D enemyRect = new Rectangle2D.Double(x, y, 32, 32);
		Player player = game.getPlayer();
		Rectangle2D playerRect = new Rectangle2D.Double(player.getX(), player.getY(), 32, 32);
		
		if(playerRect.intersects(enemyRect)) {
			player.decreasePlayerHealth(1);
        }
	}
}
