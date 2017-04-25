package sk.uniza.fri.fant0m.bomberman;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Class for saving current game progress.
 * @author fant0m
 */
public class Progress extends JPanel {
    /**
     * Table.
     */
    private JTable table;

    /**
     * Layout and table initialization.
     * @param ac listener
     */
    public Progress(final ActionListener ac) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Hráč");
        model.addColumn("Level");
        model.addColumn("Skóre");
        table = new JTable(model);
        table.setFillsViewportHeight(true);

        this.add(table.getTableHeader());
        this.add(table);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));

        JButton loadButton = new JButton("Načítať");
        loadButton.addActionListener(ac);

        JButton backButton = new JButton("Späť");
        backButton.addActionListener(ac);

        final int margin = 10;
        buttonPane.setBorder(BorderFactory.createEmptyBorder(
            margin, margin, margin, margin
        ));
        buttonPane.add(loadButton);
        buttonPane.add(Box.createRigidArea(new Dimension(margin, 0)));
        buttonPane.add(backButton);

        this.add(buttonPane);
    }

    /**
     * Reload saved games.
     */
    public final void reload() {
        BufferedReader bfr = null;
        try {
            bfr = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream("save.txt"),
                    StandardCharsets.UTF_8
                )
            );

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            String row;
            while ((row = bfr.readLine()) != null) {
                Object[] data = row.split("-");
                model.addRow(data);
            }

            bfr.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns selected row from the table.
     * @return row values
     */
    public final Object[] getSelectedItem() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Object[] result = {
                table.getValueAt(row, 0),
                table.getValueAt(row, 1),
                table.getValueAt(row, 2)
            };
            return result;
        }

        return null;
    }

    /**
     * Save game progress.
     * @param name player name
     * @param level game level
     * @param score player score
     */
    public static final void saveProgress(
        final String name,
        final int level,
        final int score
    ) {
        int s = score - PlayerInfo.SCORE_REPEAT_LEVEL;
        if (s < 0) {
            return;
        }

        try {
            OutputStreamWriter output = new OutputStreamWriter(
                new FileOutputStream("save.txt", true),
                StandardCharsets.UTF_8
            );

            output.append(name + "-" + level + "-" + s);
            output.append('\n');
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
