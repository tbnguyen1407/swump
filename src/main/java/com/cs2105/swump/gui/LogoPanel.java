package com.cs2105.swump.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LogoPanel extends JPanel
{
    private final String LOGO_PATH = "img/logo.png";
    private final int WIDTH = 385;
    private Image logoImage;

    public LogoPanel()
    {
        super();
        try
        {
            this.logoImage = ImageIO.read(new File(LOGO_PATH));
        }
        catch (IOException e)
        {
            this.logoImage = null;
        }
        setSize(WIDTH, WIDTH);
        setOpaque(true);
    }

    public void setVisible(boolean arg0)
    {
        SudokuMainUI.main.sudokuBoard.setVisible(false);
        SudokuMainUI.main.getProgressBar().setVisible(false);
        SudokuMainUI.main.getSidePanel().setVisible(false);
        super.setVisible(arg0);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        setLocation((SudokuMainUI.main.getWidth() - WIDTH) / 2, (SudokuMainUI.main.getHeight() - WIDTH - 100) / 2 + 70);
        if (logoImage != null)
            g.drawImage(logoImage, 0, 0, getParent());
    }
}
