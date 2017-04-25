package sk.uniza.fri.fant0m.bomberman;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * User interface panel.
 * @author fant0m
 */
public class UI extends JPanel {
    /**
     * Main app buttons.
     */
    private JButton singleplayerGameButton, multiplayerGameButton,
                    loadGameButton, statsButton, exitGameButton;
    /**
     * Horizontal gap in layout.
     */
    private static final int H_GAP = 20;
    /**
     * Vertical gap in layout.
     */
    private static final int V_GAP = 200;

    /**
     * App menu (first view after start).
     * @param ac listener
     */
    public UI(final ActionListener ac) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, H_GAP, V_GAP));

        singleplayerGameButton = new JButton("Singleplayer");
        singleplayerGameButton.addActionListener(ac);

        multiplayerGameButton = new JButton("Multiplayer");
        multiplayerGameButton.addActionListener(ac);

        loadGameButton = new JButton("Načítať hru");
        loadGameButton.addActionListener(ac);

        statsButton = new JButton("Najlepší hráči");
        statsButton.addActionListener(ac);

        exitGameButton = new JButton("Ukončiť hru");
        exitGameButton.addActionListener(ac);

        this.add(singleplayerGameButton);
        this.add(multiplayerGameButton);
        this.add(loadGameButton);
        this.add(statsButton);
        this.add(exitGameButton);
    }
}
