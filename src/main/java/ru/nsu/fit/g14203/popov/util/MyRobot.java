package ru.nsu.fit.g14203.popov.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MyRobot extends Robot {

    public MyRobot() throws AWTException {
    }

//    ------   typing   ------

    private void keyType(int keycode) {
        keyPress(keycode);
        keyRelease(keycode);
    }

    public void typeChar(char ch) {
        boolean upper = Character.isUpperCase(ch);
        if (upper)
            keyPress(KeyEvent.VK_SHIFT);

        int keycode = KeyStroke.getKeyStroke(ch).getKeyCode();
        keyType(keycode);

        if (upper)
            keyRelease(KeyEvent.VK_SHIFT);
    }

    public void typeString(String s) {
        for (char ch : s.toCharArray())
            typeChar(ch);
    }

    public void backspace() {
        typeChar('\b');
    }

//    ------   mouse   ------

    public void mouseClick(Point p) {
        mouseMove(p.x, p.y);
        mousePress(MouseEvent.BUTTON1);
        mouseRelease(MouseEvent.BUTTON1);
    }
}
