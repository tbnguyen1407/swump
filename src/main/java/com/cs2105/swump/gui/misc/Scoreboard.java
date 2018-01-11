package com.cs2105.swump.gui.misc;

import com.cs2105.swump.core.SudokuLogic;
import com.cs2105.swump.gui.SudokuMainUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class Scoreboard extends JDialog
{
    private static final long serialVersionUID = 7697941659710544986L;

    public Scoreboard()
    {
        super(SudokuMainUI.main, "ThemeSelector", true);
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(400, 250));
        this.getContentPane().add(mainPanel);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(SudokuMainUI.main);
        this.setTitle("Top scores");

        String[][][] scores = SudokuLogic.getInstance().retrieveScoreboard();
        String[] columnName = {"Player", "Date", "Score"};

        //Create the components.
        JPanel easy = createSimpleDialogBox(scores, columnName, 0);
        JPanel normal = createSimpleDialogBox(scores, columnName, 1);
        JPanel hard = createSimpleDialogBox(scores, columnName, 2);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Easy", null, easy, ""); //tooltip text
        tabbedPane.addTab("Normal", null, normal, ""); //tooltip text
        tabbedPane.addTab("Hard", null, hard, ""); //tooltip text

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Creates the panel shown by the first tab.
     */
    private JPanel createSimpleDialogBox(String[][][] scores, String[] columnName, int diff)
    {
        JPanel panel = new JPanel();
        JLabel text = new JLabel();
        panel.setLayout(new BorderLayout());
        JTable table = new JTable(scores[diff], columnName);

        table.setPreferredSize(new Dimension(350, 160));
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setShowGrid(false);
        table.setEnabled(false);
        TableCellRenderer renderer = new EvenOddRenderer();
        table.setDefaultRenderer(Object.class, renderer);

        if (diff == 0)
            text.setText("Highscore for easy puzzle");
        else if (diff == 1)
            text.setText("Highscore for normal puzzle");
        else if (diff == 2)
            text.setText("Highscore for difficult puzzle");

        text.setFont(FontGenerator.generateStdFont(Font.BOLD, 16));
        panel.add(text, BorderLayout.NORTH);
        panel.add(table.getTableHeader(), BorderLayout.CENTER);
        panel.add(table, BorderLayout.SOUTH);

        return panel;
    }
}

class EvenOddRenderer implements TableCellRenderer
{
    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        ((JLabel) renderer).setOpaque(true);
        Color foreground, background;
        if (isSelected)
        {
            foreground = Color.yellow;
            background = Color.green;
        }
        else
        {
            if (row % 2 == 0)
            {
                foreground = Color.black;
                background = new Color(209, 228, 227);
            }
            else
            {
                foreground = Color.black;
                background = Color.white;
            }
        }
        renderer.setForeground(foreground);
        renderer.setBackground(background);
        return renderer;
    }
}
