package com.cs2105.swump.gui.misc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ColorComboBox extends JComboBox {
    public static class ColorCellRenderer implements ListCellRenderer {
        protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        // width doesn't matter as combobox will size
        private final static Dimension preferredSize = new Dimension(0, 20);

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);
            if (value instanceof Color) {
                renderer.setBackground((Color) value);
                renderer.setForeground((Color) value);
            }
            renderer.setPreferredSize(preferredSize);
            return renderer;
        }
    }
}
