package com.cs2105.swump.gui.misc;

import javax.swing.*;
import java.awt.*;

public class ColorComboBox extends JComboBox
{
    public static class ColorCellRenderer implements ListCellRenderer
    {
        protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        // width doesn't matter as combobox will size
        private final static Dimension preferredSize = new Dimension(0, 20);

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Color)
            {
                renderer.setBackground((Color) value);
                renderer.setForeground((Color) value);
            }
            renderer.setPreferredSize(preferredSize);
            return renderer;
        }
    }
}
