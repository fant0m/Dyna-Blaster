package sk.uniza.fri.fant0m.bomberman;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Statistics class.
 * @author fant0m
 */
public class Stats extends JPanel {
    /**
     * Stats fields.
     */
    private ArrayList<String> data;
    /**
     * Horizontal gap in layout.
     */
    private static final int H_GAP = 20;
    /**
     * Vertical gap in layout.
     */
    private static final int V_GAP = 200;

    /**
     * Arraylist and layout initialization.
     * @param ac listener
     */
    public Stats(final ActionListener ac) {
        data = new ArrayList<String>();
        this.setLayout(new FlowLayout(FlowLayout.CENTER, H_GAP, V_GAP));

        JButton backButton = new JButton("Späť");
        backButton.addActionListener(ac);
        this.add(backButton);
    }

    /**
     * Load scores into arraylist.
     */
    public final void update() {
        BufferedReader bfr = null;
        try {
            bfr = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream("stats.txt"),
                    StandardCharsets.UTF_8
                )
            );

            String row;
            data.clear();

            while ((row = bfr.readLine()) != null) {
                data.add(row);
            }

            Collections.sort(data, statsComp);

            bfr.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save player score.
     * @param name player name
     * @param score player score
     */
    public static final void writeScore(final String name, final int score) {
        if (score > 0) {
            try {
                OutputStreamWriter output = new OutputStreamWriter(
                    new FileOutputStream("stats.txt", true),
                    StandardCharsets.UTF_8
                );

                output.append(name + "-" + score);
                output.append('\n');
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Save player score.
     * @param player player info
     */
    public static final void writeScore(final PlayerInfo player) {
        writeScore(player.getName(), player.getScore());
    }

    /**
     * Sort loaded scores.
     */
    private static Comparator<String> statsComp = new Comparator<String>() {
        public int compare(final String s1, final String s2) {
            int num1 = Integer.parseInt(s1.split("-")[1]);
            int num2 = Integer.parseInt(s2.split("-")[1]);

            return num2 - num1;
        }
    };

    /**
     * Draw top 3 players and their scores.
     * @param g graphics
     */
    public final void paint(final Graphics g) {
        super.paint(g);

        final int fontSize = 18;
        final int titleX = 275;
        final int titleY = 60;
        final int nameX = 250;
        final int scoreX = 370;
        final int scoreY = 100;
        final int scoreRow = 20;

        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        g.drawString("Top 3 hráči", titleX, titleY);
        for (int i = 0; i <= 2; i++) {
            if (data.size() >= i + 1) {
                String[] inf = data.get(i).split("-");
                g.drawString(inf[0], nameX, scoreY + (i * scoreRow));
                g.drawString(inf[1], scoreX, scoreY + (i * scoreRow));
            }
        }
    }
}
