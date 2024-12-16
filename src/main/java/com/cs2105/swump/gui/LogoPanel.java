package com.cs2105.swump.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class LogoPanel extends JPanel {
    // region fields

    private final String logoPath = "img/logo.png";
    private final int width = 385;
    private Image logoImage;

    // endregion

    // region constructors

    public LogoPanel() {
        super();
        try {
            this.logoImage = ImageIO.read(new File(logoPath));
        } catch (IOException e) {
            e.printStackTrace();
            this.logoImage = null;
        }
        setSize(width, width);
        setOpaque(true);
    }

    // endregion

    // region public methods

    public void setVisible(boolean arg0) {
        SudokuMainUI.main.sudokuBoard.setVisible(false);
        SudokuMainUI.main.getProgressBar().setVisible(false);
        SudokuMainUI.main.getSidePanel().setVisible(false);
        super.setVisible(arg0);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        setLocation((SudokuMainUI.main.getWidth() - width) / 2, (SudokuMainUI.main.getHeight() - width - 100) / 2 + 70);
        if (logoImage != null) {
            g.drawImage(logoImage, 0, 0, getParent());
        }
    }

    // endregion
}
