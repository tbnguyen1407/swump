package swump.gui.single;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import swump.core.SudokuLogic;
import swump.core.generator.PuzzleGenerator;
import swump.gui.SudokuMainUI;
import swump.gui.misc.FontGenerator;

public class SinglePlayerPanel extends JPanel {
    // region fields

    private static final long serialVersionUID = 1735162861773916659L;
    private JLabel difficulty;
    JLabel left;

    // endregion

    // region constructors

    public SinglePlayerPanel() {
        this.setLayout(new BorderLayout());

        // Stat panel
        JPanel stats = new JPanel();
        stats.setLayout(new GridLayout(8, 1, 0, 0));
        difficulty = new JLabel("Difficulty: ");
        difficulty.setFont(FontGenerator.generateItalicFont(Font.PLAIN, 20));

        stats.add(difficulty);

        JPanel paneControls = new JPanel();
        paneControls.setLayout(new BoxLayout(paneControls, BoxLayout.Y_AXIS));

        // Marker type selection
        final JPanel paneMarkerType = new JPanel();
        paneMarkerType.setLayout(new BoxLayout(paneMarkerType, BoxLayout.X_AXIS));
        paneMarkerType.setVisible(false);

        JLabel lbMarkerTypeHeader = new JLabel("Marker type:");

        String[] markerTypes = { "Distinct", "Same", "Even", "Odd" };
        final JComboBox<String> cbxMarkerType = new JComboBox<>(markerTypes);
        cbxMarkerType.setSelectedIndex(0);

        paneMarkerType.add(lbMarkerTypeHeader);
        paneMarkerType.add(Box.createHorizontalGlue());
        paneMarkerType.add(cbxMarkerType);

        paneControls.add(paneMarkerType);
        paneControls.add(Box.createRigidArea(new Dimension(0, 10)));

        // icon panel
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.LINE_AXIS));

        // btnHint
        final JButton btnHint = new JButton("Show hint", getIcon("img/iconHint.png"));
        btnHint.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnHint.setHorizontalTextPosition(AbstractButton.CENTER);
        btnHint.setIconTextGap(-4);
        btnHint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    int elapsedTime = (int) (SudokuLogic.getInstance().getElapsedTime() / 1000);
                    int hintLeft = SudokuLogic.getInstance().getCurrentPlayer().getNumHints();
                    if (hintLeft > 0) {
                        btnHint.setEnabled(true);
                        SudokuLogic.getInstance().showHint();
                        SudokuMainUI.main.updateProgressBar();
                        SudokuMainUI.main.repaint();
                        if (hintLeft - 1 == 0)
                            btnHint.setEnabled(false);
                        if (SudokuLogic.getInstance().getState() == 1)
                            SudokuMainUI.main.showWin(elapsedTime);
                    } else
                        btnHint.setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // btnSolve
        final JButton btnSolve = new JButton("Solve", getIcon("img/iconSolve.png"));
        btnSolve.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnSolve.setHorizontalTextPosition(AbstractButton.CENTER);
        btnSolve.setIconTextGap(-4);
        btnSolve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                int[][] puzzleToSolve = SudokuLogic.getInstance().getPuzzle().getUserAnswers();

                // Collect constraints
                int[][] puzzleCustomRegion = SudokuLogic.getInstance().getPuzzle().getRegionGrid();
                boolean[][] puzzleMarkedGrid = SudokuLogic.getInstance().getPuzzle().getMarkedGrid();

                int[][] resultPuzzle = PuzzleGenerator.getInstance().solvePuzzle(puzzleToSolve, puzzleCustomRegion,
                        cbxMarkerType.getSelectedItem().toString(), puzzleMarkedGrid);
                SudokuLogic.getInstance().getPuzzle().setGrid(resultPuzzle);
                SudokuMainUI.main.repaint();
            }
        });

        // btnToggle
        final JButton btnToggle = new JButton("Pen", getIcon("img/iconPen.png"));
        btnToggle.setVerticalTextPosition(AbstractButton.BOTTOM);
        btnToggle.setHorizontalTextPosition(AbstractButton.CENTER);
        btnToggle.setIconTextGap(-4);
        btnToggle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (btnToggle.getText().equalsIgnoreCase("Pen")) {
                    paneMarkerType.setVisible(false);

                    switch (SudokuLogic.getInstance().getDifficulty()) {
                        case 3: // Solver
                            btnToggle.setText("Region");
                            SudokuMainUI.main.setIsRegionInput();
                            btnToggle.setIcon(getIcon("img/iconRegion.png"));

                            // Refresh UI
                            SudokuMainUI.main.setActiveCell(null);
                            SudokuMainUI.main.repaint();
                            break;
                        default: // SinglePlayer
                            btnToggle.setText("Pencil");
                            SudokuMainUI.main.setIsPencilMarkInput();
                            btnToggle.setIcon(getIcon("img/iconPencil.png"));
                            break;
                    }
                } else if (btnToggle.getText().equalsIgnoreCase("Region")) {
                    btnToggle.setText("Marker");
                    SudokuMainUI.main.setIsMarkInput();
                    btnToggle.setIcon(getIcon("img/iconMark.png"));

                    paneMarkerType.setVisible(true);
                } else if (btnToggle.getText().equalsIgnoreCase("Pencil")
                        || btnToggle.getText().equalsIgnoreCase("Marker")) {
                    btnToggle.setText("Pen");
                    SudokuMainUI.main.setIsValueInput();
                    btnToggle.setIcon(getIcon("img/iconPen.png"));

                    paneMarkerType.setVisible(false);
                }
            }
        });

        // Populate icon panels
        iconPanel.add(btnToggle);
        iconPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        if (SudokuLogic.getInstance().getDifficulty() == 3)
            iconPanel.add(btnSolve);
        else
            iconPanel.add(btnHint);

        paneControls.add(iconPanel);

        // Populate main ui
        if (!(SudokuLogic.getInstance().getDifficulty() == 3))
            this.add(stats, BorderLayout.NORTH);
        this.add(paneControls, BorderLayout.SOUTH);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // endregion

    // region public methods

    public void setDifficulty(String diff) {
        difficulty.setText("Difficulty: " + diff);
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
