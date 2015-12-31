
public class SpecialTile extends Tile {
	
	private int special;

	/**
	 * Špeciálny typ políèka
	 * @param special 1 - východ, 2 - zvýšenie rýchlosti, 3 - zvýšenie poètu bômb, 4 - zväèšie dosahu plameòa
	 */
	public SpecialTile(int x, int y, int type, boolean walkable, int special) {
		super(x, y, type, walkable);
		this.special = special;
	}
	
	public int getSpecial() {
		return special;
	}

}
