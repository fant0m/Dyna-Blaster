

public class Tile {
	
	private int x;
	private int y;
	private int type;
	private boolean walkable;

	/**
	 * Konštruktor pre políèko mapy
	 * @param x horizontálna pozícia
	 * @param y vertikálna pozícia
	 * @param type typ políèka(0-tráva, 1-stena, 2-blok, ktorı mono znièi, 3-bomba, 20-26-plameò, 30-vıchod, 31-33-power-ups)
	 * @param walkable dá sa cez políèko prechádza?
	 */
	public Tile(int x, int y, int type, boolean walkable) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.walkable = walkable;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getType() {
		return type;
	}

	public boolean isWalkable() {
		return walkable;
	}

	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}
	
}
