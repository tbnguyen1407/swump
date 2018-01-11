package com.cs2105.swump.gui.multi;

import com.cs2105.swump.gui.misc.ColorComboBox;
import com.cs2105.swump.gui.misc.ColorComboBoxEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputPlayer extends JPanel implements KeyListener, ActionListener
{
    String name;
    Color color;
    Color colors[] = {new Color(215, 25, 28), new Color(253, 174, 97), new Color(44, 123, 182), new Color(171, 217, 233)};

    final JTextField txtName = new JTextField(10);
    final JComboBox comboBox = new JComboBox(colors);

    InputPlayer(int n)
    {
        setLayout(new FlowLayout());
        add(new JLabel("Player" + n + " name: "));

        txtName.addKeyListener(this);
        add(txtName);

        comboBox.setEditable(true);
        comboBox.setRenderer(new ColorComboBox.ColorCellRenderer());
        comboBox.setSelectedIndex(n - 1);
        color = (Color) comboBox.getSelectedItem();
        ComboBoxEditor editor = new ColorComboBoxEditor(color);
        comboBox.setEditor(editor);

        this.add(comboBox);
        comboBox.addActionListener(this);
    }

    @Override
    public void keyPressed(KeyEvent arg0)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyReleased(KeyEvent arg0)
    {
        // TODO Auto-generated method stub
        name = txtName.getText();
    }

    @Override
    public void keyTyped(KeyEvent arg0)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        // TODO Auto-generated method stub
        color = (Color) comboBox.getSelectedItem();
    }
}
