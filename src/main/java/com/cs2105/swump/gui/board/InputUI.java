package com.cs2105.swump.gui.board;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.cs2105.swump.core.SudokuLogic;
import com.cs2105.swump.gui.SudokuMainUI;
import com.cs2105.swump.gui.misc.FontGenerator;

public class InputUI extends JPanel {
    // region fields

    private static final long serialVersionUID = -7301750359459732387L;
    private static InputUI inputUI = null;

    // endregion

    // region constructors

    public InputUI() {
        this.setLayout(new GridLayout(4, 3));
        this.setVisible(false);
        this.setSize(120, 160);
        this.setOpaque(false);

        // Input numbers from 1..9
        for (int i = 1; i <= 9; i++)
            this.add(new InputButton(i + ""));

        // For Solver add a 0 to clear the cell
        if (SudokuLogic.getInstance().getDifficulty() == 3 && !SudokuMainUI.main.getIsRegionInput())
            this.add(new InputButton(0 + ""));

        inputUI = this;
    }

    // endregion

    // region public methods

    public void refresh() {
        // Set layout and size
        if (SudokuLogic.getInstance().getDifficulty() == 3 && !SudokuMainUI.main.getIsRegionInput()) {
            this.setLayout(new GridLayout(4, 3));
            this.setSize(120, 160);
        } else {
            this.setLayout(new GridLayout(3, 3));
            this.setSize(120, 120);
        }

        // Input numbers from 1..9
        this.removeAll();
        for (int i = 1; i <= 9; i++)
            this.add(new InputButton(i + ""));

        // For Solver add a 0 to clear the cell
        if (SudokuLogic.getInstance().getDifficulty() == 3 && SudokuMainUI.main.getIsValueInput())
            this.add(new InputButton(0 + ""));
    }

    // endregion

    // region private methods

    protected void paintComponent(Graphics g) {
        // Draw border
        int w = getWidth() - 2;
        int h = getHeight() - 2;
        int arc = 20;

        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(189, 189, 189, 200));
        g.fillRoundRect(0, 0, w, h, arc, arc);

        ((Graphics2D) g).setStroke(new BasicStroke(2f));
        g.setColor(Color.WHITE);
        g.drawRoundRect(0, 0, w, h, arc, arc);

        // Draw vertical lines
        g.drawLine(w / 3, 0, w / 3, h); // left vertical
        g.drawLine((w / 3) * 2, 0, w / 3 * 2, h); // right vertical

        // Draw horizontal lines
        if (SudokuLogic.getInstance().getDifficulty() == 3 && !SudokuMainUI.main.getIsRegionInput()) {
            g.drawLine(0, h / 4, w, h / 4); // top horizontal
            g.drawLine(0, h / 2, w, h / 2); // middle horizontal
            g.drawLine(0, h / 4 * 3, w, h / 4 * 3); // bottom horizontal
        } else {
            g.drawLine(0, h / 3, w, h / 3); // top horizontal
            g.drawLine(0, h / 3 * 2, w, h / 3 * 2); // bottom horizontal
        }
    }

    // endregion

    private class InputButton extends JLabel {
        // region fields

        private static final long serialVersionUID = -4816053135542630420L;

        // endregion

        // region constructors

        InputButton(String text) {
            super(text);

            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);
            setFont(FontGenerator.generateItalicFont(Font.PLAIN, 14));
            setOpaque(false);

            // If mouse is outside inputValue panel -> set visible to false
            this.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent evt) {
                    numberSelected(null);
                }

                public void mouseExited(MouseEvent evt) {
                    Point mousePos = SudokuMainUI.main.getContentPane().getMousePosition(true);
                    try {
                        if ((mousePos.getX() < inputUI.getX()) || (mousePos.getY() < inputUI.getY())
                                || (mousePos.getX() > inputUI.getX() + inputUI.getWidth())
                                || (mousePos.getY() > inputUI.getY() + inputUI.getHeight())) {
                            inputUI.setVisible(false);
                            SudokuMainUI.main.setActiveCell(null);
                            SudokuMainUI.main.repaint();
                        }
                    } catch (NullPointerException npe) {
                        inputUI.setVisible(false);
                        SudokuMainUI.main.setActiveCell(null);
                        SudokuMainUI.main.repaint();
                    }
                }

            });
        }

        // endregion

        // region private methods

        private void numberSelected(ActionEvent evt) {
            inputUI.setVisible(false);
            if (SudokuMainUI.main.getIsPencilMarkInput())
                SudokuMainUI.main.getActiveCell().inputPencilMark(Integer.parseInt(this.getText()));
            else if (SudokuMainUI.main.getIsRegionInput())
                SudokuMainUI.main.getActiveCell().inputRegionId(Integer.parseInt(this.getText()));
            else // normal inputValue
                SudokuMainUI.main.getActiveCell().inputValue(Integer.parseInt(this.getText()));
            SudokuMainUI.main.setActiveCell(null);
            SudokuMainUI.main.repaint();
        }

        // endregion
    }
}
