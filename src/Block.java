/**
 * Trieda rozširujúca triedu Tile(políèko). Toto políèko môe by znièené bombou a po vıbuchu sa tu môe nachádza vıchod.
 * 
 * @author Dev
 *
 */
public class Block extends Tile {
	
	private boolean hasExit = false;

	public Block(int x, int y, int type, boolean walkable) {
		super(x, y, type, walkable);
	}
	
	public Block(int x, int y, int type, boolean walkable, boolean hasExit) {
		super(x, y, type, walkable);
		this.hasExit = hasExit;
	}
	
	public void setHasExit(boolean hasExit) {
		this.hasExit = hasExit;
	}

	public boolean hasExit() {
		return hasExit;
	}

}
