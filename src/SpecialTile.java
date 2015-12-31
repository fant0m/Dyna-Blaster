
public class SpecialTile extends Tile {
	
	private int special;

	/**
	 * �peci�lny typ pol��ka
	 * @param special 1 - v�chod, 2 - zv��enie r�chlosti, 3 - zv��enie po�tu b�mb, 4 - zv��ie dosahu plame�a
	 */
	public SpecialTile(int x, int y, int type, boolean walkable, int special) {
		super(x, y, type, walkable);
		this.special = special;
	}
	
	public int getSpecial() {
		return special;
	}

}
