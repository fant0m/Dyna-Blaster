import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JPanel;


@SuppressWarnings("serial")
/**
 * Trieda pre vypÌsanie ötatistÌk
 * @author Dev
 *
 */
public class Stats extends JPanel {
	
	private ArrayList<String> data;

	/**
	 * Inicializ·cia arraylistu so vöetk˝mi skÛre.
	 * Nastavenie layoutu a pridanie tlaËidla pre n·vrat do menu.
	 * @param ac
	 */
	public Stats(ActionListener ac) {
		data = new ArrayList<String>();
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 200));
		
		JButton backButton = new JButton("Sp‰ù");
		backButton.addActionListener(ac);
		this.add(backButton);
	}
	
	/**
	 * NaËÌtanie vöetk˝ch v˝sledkov do arraylistu
	 */
	public void update() {
		BufferedReader bfr = null;
		try {
			bfr = new BufferedReader(new FileReader("stats.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
        String row;
        data.clear();
        try {
			while((row = bfr.readLine()) != null) {
			    data.add(row);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        Collections.sort(data, StatsComparator);
        try {
			bfr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Zoradenie v˝sledkov podæa bodov.
	 */
	public static Comparator<String> StatsComparator = new Comparator<String>() {
	        
	    public int compare(String s1, String s2) {
	        int cislo1 = Integer.parseInt(s1.split("-")[1]);
	        int cislo2 = Integer.parseInt(s2.split("-")[1]);

	        return cislo2 - cislo1;
	    }
	        
    };
	
    /**
     * Vykreslenie top 3 hr·Ëov a ich v˝sledkov.
     */
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.drawString("Top 3 hr·Ëi", 275, 60);
		for (int i = 0; i <= 2; i++) {
			if (data.size() >= i + 1) {
				String[] inf = data.get(i).split("-");
				g.drawString(inf[0], 250, 100 + (i * 20));
				g.drawString(inf[1], 370, 100 + (i * 20));
			}
		}
		
	}

}
