package com.cs2105.swump.gui.misc;

import com.cs2105.swump.core.SudokuLogic;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel
{
    private static final long serialVersionUID = 1735162861773916659L;
    private JLabel timer;

    public SidePanel()
    {
        this.setLayout(new BorderLayout());
        timer = new JLabel("00:00")
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        timer.setHorizontalAlignment(AbstractButton.CENTER);
        timer.setFont(FontGenerator.generateItalicFont(Font.PLAIN, 50));

        this.add(timer, BorderLayout.NORTH);
        this.setPreferredSize(new Dimension(200, 500));
    }

    public void setPanel(JPanel infoPanel)
    {
        this.removeAll();
        this.add(timer, BorderLayout.NORTH);
        this.add(infoPanel, BorderLayout.CENTER);
        revalidate();
    }

    public void setTime(long elapsedTime)
    {
        String format = String.format("%%0%dd", 2);
        elapsedTime = elapsedTime / 1000;
        String seconds = String.format(format, elapsedTime % 60);
        String minutes = String.format(format, (elapsedTime % 3600) / 60);
        //String hours = String.format(format, elapsedTime / 3600);

        String time;
        if (SudokuLogic.getInstance().getMode() == 0)
            time = minutes + ":" + seconds;
        else
            time = elapsedTime + " s";

        timer.setText(time);
    }
}
