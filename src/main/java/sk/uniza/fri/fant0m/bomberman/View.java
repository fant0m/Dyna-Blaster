package sk.uniza.fri.fant0m.bomberman;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Main view runnable class.
 * @author fant0m
 */
public class View extends JFrame implements ActionListener {
    /**
     * Main application frame.
     */
    private JFrame frame;
    /**
     * Layout type.
     */
    private CardLayout cl;
    /**
     * Cards panel.
     */
    private JPanel cards;
    /**
     * 2 types of games.
     */
    private Game singleplayer, multiplayer;
    /**
     * Game loader.
     */
    private Progress progress;
    /**
     * Statistics.
     */
    private Stats stats;
    /**
     * Game thread.
     */
    private Thread thread;
    /**
     * Width of main frame.
     */
    private static final int FRAME_WIDTH = 640;
    /**
     * Height of main frame.
     */
    private static final int FRAME_HEIGHT = 464;

    /**
     * Create window, layout and add views (jpanels) into the layout.
     */
    public View() {
        super("Bomberman v1.0");

        singleplayer = new Game(this, Game.SINGLEPLAYER);
        multiplayer = new Game(this, Game.MULTIPLAYER);
        progress = new Progress(this);
        stats = new Stats(this);
        frame = new JFrame("Bomberman v1.0");

        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        cl = new CardLayout();
        cards = new JPanel(cl);

        cards.add(new UI(this), "Intro");
        cards.add(singleplayer, "Singleplayer");
        cards.add(multiplayer, "Multiplayer");
        cards.add(progress, "Progress");
        cards.add(stats, "Stats");

        this.add(cards);
        this.revalidate();
    }

    /**
     * Main application method.
     * @param args app arguments
     */
    public static void main(final String[] args) {
        new View();
    }

    /**
     * Frame getter.
     * @return frame
     */
    public final JFrame getFrame() {
        return frame;
    }

    /**
     * Change the view of window.
     * @param name view name
     * @param params optional parameters for saved game
     */
    public final void setView(final String name, final Object... params) {
        if (name.equals("Singleplayer")) {
            cl.show(cards, "Singleplayer");

            singleplayer.init(params);

            super.repaint();
            this.revalidate();
            this.repaint();

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    singleplayer.loop();
                }
            });
            thread.start();
        } else if (name.equals("Multiplayer")) {
            cl.show(cards, "Multiplayer");

            multiplayer.init();

            super.repaint();
            this.revalidate();
            this.repaint();

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    multiplayer.loop();
                }
            });
            thread.start();
        } else if (name.equals("Načítať hru")) {
            progress.reload();
            cl.show(cards, "Progress");
        } else if (name.equals("Najlepší hráči")) {
            stats.update();
            cl.show(cards, "Stats");
        } else if (name.equals("Ukončiť hru")) {
            System.exit(0);
        } else if (name.equals("Späť") || name.equals("Intro")) {
            cl.show(cards, "Intro");
        } else if (name.equals("Načítať")) {
            Object[] item = progress.getSelectedItem();
            if (item != null) {
                setView("Singleplayer", item);
            } else {
                JOptionPane.showMessageDialog(
                    this, "Pre načítanie uloženej hry kliknite na daný riadok."
                );
            }
        }
    }

    /**
     * Dynamic view change.
     * @param e action
     */
    @Override
    public final void actionPerformed(final ActionEvent e) {
        setView(e.getActionCommand());
    }
}
