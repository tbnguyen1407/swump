package com.cs2105.swump;

import com.cs2105.swump.core.generator.PuzzleStorageThread;
import com.cs2105.swump.core.storage.Storage;
import com.cs2105.swump.core.storage.SqlStorage;
import com.cs2105.swump.gui.SudokuMainUI;

import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
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
