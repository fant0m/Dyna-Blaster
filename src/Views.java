import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Views extends JFrame implements ActionListener {
	
	private JFrame frame;
	private CardLayout cl;
	private JPanel cards;
	private Game game;
	private Stats stats;
	private Thread thread;
	
	/**
	 * Vytvorenie okna, layoutu.
	 * Pridanie viewov(jpanelov) do layoutu.
	 */
	public Views() {
		super("Bomberman v1.0");
		
		game = new Game(this);
		stats = new Stats(this);
		
		frame = new JFrame("Bomberman v1.0");
		
		this.setSize(647, 494);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		cl = new CardLayout();
        cards = new JPanel(cl);

        
        cards.add(new UI(this), "Intro");
        cards.add(game, "Game");
        cards.add(stats, "Stats");
        
        this.add(cards);
        this.revalidate();
	}

    public static void main(String[] args) {
        new Views();
    }
    
    public JFrame getFrame() {
    	return frame;
    }
    
    /**
     * ZmeÚ view(obsah obrazovky)
     * @param name
     */
    public void setView(String name) {

    	if (name.equals("Nov· hra")) {
			cl.show(cards, "Game");

			game.init();
			
			super.repaint();
			this.revalidate();
			this.repaint();
			
			thread = new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	game.loop();
			    }
			});
			thread.start();
			
			
		} else if (name.equals("NajlepöÌ hr·Ëi")) {
			stats.update();
			cl.show(cards, "Stats");
		} else if (name.equals("UkonËiù hru")) {
			System.exit(0);
		} else if (name.equals("Sp‰ù") || name.equals("Intro")) {
			cl.show(cards, "Intro");
		}
    }
    
	public void actionPerformed(ActionEvent e) {
		setView(e.getActionCommand());
	}
    
}