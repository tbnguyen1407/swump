package com.cs2105.swump.gui.misc;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class FontGenerator {
    // region fields

    private static Font italicFont;
    private static Font font;

    // endregion

    // region constructors

    private FontGenerator() {
    }

    // endregion

    // region public methods

    public static Font generateItalicFont(int style, int size) {
        if (italicFont != null)
            return italicFont.deriveFont(style, size);

        try {
            italicFont = Font.createFont(Font.TRUETYPE_FONT,
                    new BufferedInputStream(new FileInputStream("font/segoepr.ttf")));
        } catch (Exception e) {
            italicFont = new Font("SanSerif", style, size);
        }
        return italicFont.deriveFont(style, size);
    }

    public static Font generateStdFont(int style, int size) {
        if (font != null)
            return font.deriveFont(style, size);

        try {
            font = Font.createFont(Font.TRUETYPE_FONT,
                    new BufferedInputStream(new FileInputStream("font/segoeui.ttf")));
        } catch (Exception e) {
            font = new Font("SanSerif", style, size);
        }
        return font.deriveFont(style, size);
    }

    // endregion
}
