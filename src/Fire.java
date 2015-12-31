
public class Fire extends Tile {
	
	private Animation fire;

	/**
	 * Trieda rozširujúca triedu Tile(Políèko).
	 * Pod¾a typu políèka sa vytvorí príslušnú animácia plameòa.
	 * @param x
	 * @param y
	 * @param type
	 * @param walkable
	 * @param g
	 */
	public Fire(int x, int y, int type, boolean walkable, Game g) {
		super(x, y, type, walkable);

		if (type == 20) {
			fire = new Animation();
			fire.addFrame(g.getImageFromSprite(326, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(342, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(358, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(374, 16, 16, 16), 80);
			fire.setMoving(true);
		} else if (type == 21) {
			fire = new Animation();
			fire.addFrame(g.getImageFromSprite(454, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(470, 17, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(486, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(502, 16, 16, 16), 80);
		    fire.setMoving(true);
		} else if (type == 22) {
			fire = new Animation();
			fire.addFrame(g.getImageFromSprite(582, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(598, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(614, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(630, 16, 16, 16), 80);
		    fire.setMoving(true);
		} else if (type == 23) {
			fire = new Animation();
			fire.addFrame(g.getImageFromSprite(390, 32, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(406, 32, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(422, 32, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(438, 32, 16, 16), 80);
			fire.setMoving(true);
		} else if (type == 24) {
			fire = new Animation();
			fire.addFrame(g.getImageFromSprite(518, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(534, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(550, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(566, 16, 16, 16), 80);
			fire.setMoving(true);
		} else if (type == 25) {
			fire = new Animation();
			fire.addFrame(g.getImageFromSprite(390, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(406, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(422, 16, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(438, 16, 16, 16), 80);
		    fire.setMoving(true);
		} else if (type == 26) {
			fire = new Animation();
			fire.addFrame(g.getImageFromSprite(326, 32, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(342, 32, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(358, 32, 16, 16), 80);
			fire.addFrame(g.getImageFromSprite(374, 32, 16, 16), 80);
			fire.setMoving(true);
		}
		
	    
	}

	public void update(int time) {
		fire.update(time);
	}
	
	public Animation getFire() {
		return fire;
	}
	

}
