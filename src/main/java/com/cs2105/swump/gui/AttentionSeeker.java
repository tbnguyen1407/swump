package com.cs2105.swump.gui;

import com.cs2105.swump.gui.misc.FontGenerator;

import javax.swing.*;
import java.awt.*;

public class AttentionSeeker extends JPanel
{
    private String text;
    private int size;
    private int style;
    private int alpha;

    public AttentionSeeker(String text, int alpha, int style, int size)
    {
        super();
        setVisible(false);
        setSize(400, 400);
        setText(text, alpha, style, size);
        setOpaque(false);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(FontGenerator.generateItalicFont(style, size));

        int fontWidth = g.getFontMetrics().stringWidth(text);
        int fontHeight = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent() + g.getFontMetrics().getLeading();

        g.setColor(new Color(255, 0, 0, alpha));
        g.drawString(text, (this.getWidth() - fontWidth) / 2, (this.getHeight() - fontHeight) / 2 + fontHeight - g.getFontMetrics().getLeading());
    }

    public void setText(String text, int alpha, int style, int size)
    {
        this.text = text;
        this.size = size;
        this.style = style;
        this.alpha = alpha;
    }

    public String getText()
    {
        return text;
    }
}
