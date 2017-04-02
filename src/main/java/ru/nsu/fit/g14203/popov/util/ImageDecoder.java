package ru.nsu.fit.g14203.popov.util;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Function;

public class ImageDecoder {

    private static final Tesseract TESSERACT = new Tesseract();
    static {
        TESSERACT.setTessVariable("tessedit_char_whitelist", "0123456789.,K");
    }

    private static BufferedImage prepare(BufferedImage image) {
        int __x = 0;
        int __y = 0;
        int width = 0;
        int height = 0;

        Function<Integer, Boolean> isYellow = RGB -> {
            Color c = new Color(RGB);
            return Math.abs(c.getRed() - Color.YELLOW.getRed()) < 0x18
                    && Math.abs(c.getGreen() - Color.YELLOW.getGreen()) < 0x18
                    && Math.abs(c.getBlue() - Color.YELLOW.getBlue()) < 0x18;
        };

        Function<Integer, Boolean> isBlack = RGB -> {
            Color c = new Color(RGB);
            return Math.abs(c.getRed() - Color.BLACK.getRed()) < 0x18
                    && Math.abs(c.getGreen() - Color.BLACK.getGreen()) < 0x18
                    && Math.abs(c.getBlue() - Color.BLACK.getBlue()) < 0x18;
        };

loop:   for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (isYellow.apply(image.getRGB(x, y))) {
                    __x = x;
                    __y = y;

                    while (!isBlack.apply(image.getRGB(__x + ++width, __y)));
                    while (!isBlack.apply(image.getRGB(__x, __y + ++height)));
                    break loop;
                }
            }
        }

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int dx = 0; dx < width; dx++) {
            for (int dy = 0; dy < height; dy++)
                result.setRGB(dx, dy, image.getRGB(__x + dx, __y + dy));
        }

        return result;
    }

    public static String decode(BufferedImage image) {
        try {
            return TESSERACT.doOCR(prepare(image));
        } catch (TesseractException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }
}
