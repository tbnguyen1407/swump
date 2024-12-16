package com.cs2105.swump.gui.single;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.cs2105.swump.gui.SudokuMainUI;

public class SingleInit extends JDialog {
    // region fields

    private static final long serialVersionUID = -7490402782065032152L;

    // endregion

    // region constructors

    public SingleInit() {
        super(SudokuMainUI.main, "Singleplayer", true);

        // btnBlank
        JButton btnBlank = new JButton("Blank", getIcon("img/iconDifBlank.png"));
        btnBlank.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnBlank.setHorizontalTextPosition(AbstractButton.CENTER);
        btnBlank.setIconTextGap(-4);
        btnBlank.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                SudokuMainUI.main.startNewSinglePlayerGame(3);
            }
        });

        // btnEasy
        JButton btnEasy = new JButton("Easy", getIcon("img/iconDifEasy.png"));
        btnEasy.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnEasy.setHorizontalTextPosition(AbstractButton.CENTER);
        btnEasy.setIconTextGap(-4);
        btnEasy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                SudokuMainUI.main.startNewSinglePlayerGame(0);
            }
        });

        // btnNormal
        JButton btnNormal = new JButton("Normal", getIcon("img/iconDifNormal.png"));
        btnNormal.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnNormal.setHorizontalTextPosition(AbstractButton.CENTER);
        btnNormal.setIconTextGap(-4);
        btnNormal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                SudokuMainUI.main.startNewSinglePlayerGame(1);
            }
        });

        // btnHard
        JButton btnHard = new JButton("Hard", getIcon("img/iconDifHard.png"));
        btnHard.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnHard.setHorizontalTextPosition(AbstractButton.CENTER);
        btnHard.setIconTextGap(-4);
        btnHard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                SudokuMainUI.main.startNewSinglePlayerGame(2);
            }
        });

        // buttonPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnBlank, BorderLayout.WEST);
        buttonPanel.add(btnEasy, BorderLayout.CENTER);
        buttonPanel.add(btnNormal, BorderLayout.CENTER);
        buttonPanel.add(btnHard, BorderLayout.EAST);

        // mainPanel
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(350, 120));
        mainPanel.setLayout(new FlowLayout());

        mainPanel.add(new JLabel("Please select difficulty level"), BorderLayout.NORTH);
        mainPanel.add(buttonPanel);

        this.getContentPane().add(mainPanel);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(SudokuMainUI.main);
    }

    // endregion

    // region private methods

    private ImageIcon getIcon(String imgPath) {
        ImageIcon icon = new ImageIcon(imgPath);
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);

    }

    // endregion
}
