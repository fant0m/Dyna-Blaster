import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class UI extends JPanel {
	
	private JButton newGameButton, statsButton, exitGameButton;

	/**
	 * Menu hry(�vodn� obrazovka po spusten� hry).
	 * @param ac
	 */
	public UI(ActionListener ac) {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 200));
		
		newGameButton = new JButton("Nov� hra");
		newGameButton.addActionListener(ac);
		
		statsButton = new JButton("Najlep�� hr��i");
		statsButton.addActionListener(ac);

		exitGameButton = new JButton("Ukon�i� hru");
		exitGameButton.addActionListener(ac);
		
		this.add(newGameButton);
		this.add(statsButton);
		this.add(exitGameButton);
	}

}
