package swump.gui.multi;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import swump.gui.misc.ColorComboBox;
import swump.gui.misc.ColorComboBoxEditor;

public class InputPlayer extends JPanel implements KeyListener, ActionListener {
    // region fields

    private String name;
    private Color color;
    private Color[] colors = {
            new Color(215, 25, 28),
            new Color(253, 174, 97),
            new Color(44, 123, 182),
            new Color(171, 217, 233)
    };

    private final JTextField txtName = new JTextField(10);
    private final JComboBox comboBox = new JComboBox(colors);

    // endregion

    // region constructors

    public InputPlayer(int n) {
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

    // endregion

    // region accessors

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    // endregion

    // region public methods

    @Override
    public void keyPressed(KeyEvent arg0) {

    }

    @Override
    public void keyReleased(KeyEvent arg0) {

        name = txtName.getText();
    }

    @Override
    public void keyTyped(KeyEvent arg0) {

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        color = (Color) comboBox.getSelectedItem();
    }

    // endregion
}
