/**
 * Trieda roz�iruj�ca triedu Tile(pol��ko). Toto pol��ko m��e by� zni�en� bombou a po v�buchu sa tu m��e nach�dza� v�chod.
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
