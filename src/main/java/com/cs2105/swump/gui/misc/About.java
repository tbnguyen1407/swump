package com.cs2105.swump.gui.misc;

import com.cs2105.swump.gui.SudokuMainUI;

import javax.swing.*;
import java.awt.*;

public class About extends JDialog
{
    private static final long serialVersionUID = 1L;

    private String strAbout = "<html>"
            + "<h1>&nbsp;Swump!</h1><p>"
            + "&nbsp;&nbsp;Version 1.0<br>"
            + "&nbsp;&nbsp;All rights reserved."
            + "<p><h3>&nbsp;&nbsp;Developed by:</h3>"
            + "<ul>"
            + "<li>Wei-Lun Lau</li>"
            + "<li>Aik-Wei Sng (Chris)</li>"
            + "<li>Binh-Nguyen Tran (Ben)</li>"
            + "<li>Kang-Wei Wong</li>"
            + "</ul>"
            + "<p><p></html>";

    public About()
    {
        super(SudokuMainUI.main, "About", true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(300, 250));

        JLabel about = new JLabel();
        about.setText(strAbout);
        about.setFont(FontGenerator.generateStdFont(Font.PLAIN, 14));
        about.setVerticalTextPosition(JLabel.NORTH);

        mainPanel.add(new JLabel(new ImageIcon("img/pen.png")), BorderLayout.WEST);
        mainPanel.add(about, BorderLayout.CENTER);

        this.getContentPane().add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(SudokuMainUI.main);
        this.setResizable(false);
        this.setVisible(true);
    }
}
