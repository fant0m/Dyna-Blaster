import java.util.ArrayList;
import java.util.Collections;


public class Enemy {
	
	private double x;
	private double y;
	private int direction; // 1=up, 2=down, 3=left, 4=right
	private boolean alive;
	private boolean receivedScore;
	private boolean dying;
	
	private Collision collision;
	private Animation dyingEnemy;

	private static final double SPEED = 1.0;

	public Enemy(int x, int y, Collision collision, Animation dyingEnemy) {
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
	 * Zmena poz�cie nepriate�a, najprv sa n�hodne zoradia 4 mo�n� smery(hore,dole,vpravo,v�avo).
	 * N�sledne sa ka�d� smer testuje �i tam m��e nepriate� �s�.
	 * Ak nie tak sa sk�si �al�� smer a v pr�pade, �e sa n�jde mo�n� smer tak sa uskuto�n� update poz�cie.
	 */
	public void update() {
		boolean cantContinueCurrentDirection = false;
		int opposite = -1;
		if (direction != -1) {
			if (direction == 1) {
				opposite = 2;
			} else if (direction == 2) {
				opposite = 1;
			} else if (direction == 3) {
				opposite = 4;
			} else if (direction == 4) {
				opposite = 3;
			}
			cantContinueCurrentDirection = collision.checkEnemyCollision(x, y, direction, SPEED, this);
		}

		boolean foundDirection = false;
		ArrayList<Integer> directions = new ArrayList<Integer>(4); 
		for (int i = 1; i <= 4; i++) {
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
			if (direction == 1) {
				y -= SPEED;
			} else if (direction == 2) {
				y += SPEED;
			} else if (direction == 3) {
				x -= SPEED;
			} else if (direction == 4) {
				x += SPEED;
			}
		}
		
		collision.checkEnemyWithPlayerCollision(x, y);
			
	}
	
	public Animation getDyingEnemy() {
		return dyingEnemy;
	}
	
	public boolean isDying() {
		return dying;
	}

	public void setDying(boolean dying) {
		this.dying = dying;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean hasReceivedScore() {
		return receivedScore;
	}

	public void setReceivedScore(boolean receivedScore) {
		this.receivedScore = receivedScore;
	}
	
	
	
}
