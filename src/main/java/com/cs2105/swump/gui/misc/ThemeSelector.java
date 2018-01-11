package com.cs2105.swump.gui.misc;

import com.cs2105.swump.gui.SudokuMainUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThemeSelector extends JDialog
{
    private static final long serialVersionUID = -7490402782065032152L;

    public ThemeSelector()
    {
        super(SudokuMainUI.main, "ThemeSelector", true);
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(200, 50));
        this.getContentPane().add(mainPanel);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(SudokuMainUI.main);

        this.setTitle("ThemeSelector selector");
        String[] color = {"Greys", "Blue Green", "Blues", "Oranges", "Purples", "Red Purple", "Reds"};

        mainPanel.setLayout(new BorderLayout());
        final JComboBox cboTheme = new JComboBox(color);
        cboTheme.setSelectedItem(null);
        cboTheme.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int choice = cboTheme.getSelectedIndex();
                switch (choice)
                {
                    case 0:
                        changeTheme(new Color(240, 240, 240), new Color(189, 189, 189));
                        break;
                    case 1:
                        changeTheme(new Color(229, 245, 249), new Color(153, 216, 201));
                        break;
                    case 2:
                        changeTheme(new Color(222, 235, 247), new Color(158, 202, 225));
                        break;
                    case 3:
                        changeTheme(new Color(254, 230, 206), new Color(253, 174, 107));
                        break;
                    case 4:
                        changeTheme(new Color(239, 237, 245), new Color(188, 189, 220));
                        break;
                    case 5:
                        changeTheme(new Color(253, 224, 221), new Color(250, 159, 181));
                        break;
                    case 6:
                        changeTheme(new Color(254, 224, 210), new Color(252, 146, 114));
                        break;
                }
            }
        });

        JLabel msg = new JLabel("Please select a theme :");
        msg.setFont(FontGenerator.generateStdFont(Font.PLAIN, 12));
        mainPanel.add(msg, BorderLayout.NORTH);
        mainPanel.add(cboTheme, BorderLayout.SOUTH);
    }

    private void changeTheme(Color x, Color y)
    {
        SudokuMainUI.main.sudokuBoard.changeTheme(x, y);
    }
}
