import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;


public class Bomb extends Tile implements ActionListener {
	
	private int bombFire;
	private boolean bombed;

	private Player player;
	private Timer timer;

	/**
	 * Inicializ�cia bomby, odpo�et �asu pre v�chuch bomby
	 * @param x horizont�lna poz�cia
	 * @param y vertik�lna poz�cia
	 * @param type typ pol��ka(3 - bomba)
	 * @param walkable d� sa cez bombu prech�dza�? (ke� hr�� polo�� bombu tak cez �u mus� najsk�r prejs� tak�e najprv sa mus� da� cez �u da� prech�dza�, potom u� nie)
	 * @param bombTime �as v�buchu bomby
	 * @param bombFire ak� ve�k� je plame�
	 * @param player in�tancia hr��a, ktor� polo�il bombu
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
	 * V�buch bomby
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
