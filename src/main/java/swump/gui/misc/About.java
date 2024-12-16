package swump.gui.misc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import swump.gui.SudokuMainUI;

public class About extends JDialog {
    // region fields

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

    // endregion

    // region constructors

    public About() {
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

    // endregion
}
