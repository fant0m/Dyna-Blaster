import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;


public class Bomb extends Tile implements ActionListener {
	
	private int bombFire;
	private boolean bombed;

	private Player player;
	private Timer timer;

	/**
	 * Inicializácia bomby, odpoèet èasu pre vıchuch bomby
	 * @param x horizontálna pozícia
	 * @param y vertikálna pozícia
	 * @param type typ políèka(3 - bomba)
	 * @param walkable dá sa cez bombu prechádza? (keï hráè poloí bombu tak cez òu musí najskôr prejs take najprv sa musí da cez òu da prechádza, potom u nie)
	 * @param bombTime èas vıbuchu bomby
	 * @param bombFire akı ve¾kı je plameò
	 * @param player inštancia hráèa, ktorı poloil bombu
	 */
	public Bomb(int x, int y, int type, boolean walkable, int bombTime, int bombFire, Player player) {
		super(x, y, type, walkable);
		this.bombFire = bombFire;
		this.player = player;
		this.bombed = false;
		
		timer = new Timer(bombTime * 1000, this);
		timer.start();
	}

	/**
	 * Vıbuch bomby
	 */
	public void actionPerformed(ActionEvent e) {
		timer.stop();
		if (!bombed) {
			player.boom(super.getX(), super.getY(), bombFire);
			setBombed(true);
		}
	}
	
	public int getBombFire() {
		return bombFire;
	}

	public boolean isBombed() {
		return bombed;
	}

	public void setBombed(boolean bombed) {
		this.bombed = bombed;
	}
	
}
