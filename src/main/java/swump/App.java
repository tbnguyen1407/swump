package swump;

import javax.swing.UIManager;

import swump.core.generator.PuzzleStorageThread;
import swump.core.storage.SqlStorage;
import swump.core.storage.Storage;
import swump.gui.SudokuMainUI;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Storage storage = SqlStorage.getInstance();
                new Thread(new PuzzleStorageThread(storage, 0)).start();
                new Thread(new PuzzleStorageThread(storage, 1)).start();
                new Thread(new PuzzleStorageThread(storage, 2)).start();

                SudokuMainUI main = new SudokuMainUI();
                main.getLogoPanel().setVisible(true);
                main.setVisible(true);
            }
        });
    }
}
