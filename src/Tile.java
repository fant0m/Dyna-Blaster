

public class Tile {
	
	private int x;
	private int y;
	private int type;
	private boolean walkable;

	/**
	 * Kon�truktor pre pol��ko mapy
	 * @param x horizont�lna poz�cia
	 * @param y vertik�lna poz�cia
	 * @param type typ pol��ka(0-tr�va, 1-stena, 2-blok, ktor� mo�no zni�i�, 3-bomba, 20-26-plame�, 30-v�chod, 31-33-power-ups)
	 * @param walkable d� sa cez pol��ko prech�dza�?
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
