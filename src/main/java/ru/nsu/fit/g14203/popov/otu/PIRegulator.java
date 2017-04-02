package ru.nsu.fit.g14203.popov.otu;

import ru.nsu.fit.g14203.popov.util.ImageDecoder;
import ru.nsu.fit.g14203.popov.util.MyRobot;

import java.awt.*;
import java.awt.image.BufferedImage;

class PIRegulator {

    private static class Define {
        double dValue;
        String sValue;
        Point location;

        Define(double value, Point location) {
            dValue = value;
            this.location = location;

            sValue = String.format("%.3f", value);
        }

        Define(String value, Point location) {
            sValue = value;
            this.location = location;
        }

        void setValue(double value) {
            ROBOT.mouseClick(location);

            for (int i = 0; i < sValue.length(); i++)
                ROBOT.backspace();

            dValue = value;
            sValue = String.format("%.3f", value);
            ROBOT.typeString(sValue);
        }

        void setValue(String value) {
            ROBOT.mouseClick(location);

            for (int i = 0; i < sValue.length(); i++)
                ROBOT.backspace();

            sValue = value;
            ROBOT.typeString(value);
        }
    }

    private final static MyRobot ROBOT;
    static {
        MyRobot tmp = null;
        try {
            tmp = new MyRobot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        ROBOT = tmp;
    }

    private final static Rectangle SCREEN = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

    private final static double Kk = 2.36;
    private final static double Tk = 12.92;

    private final static double delta = 0.001;

//    ------   defines   ------

    private final static Define K0    = new Define(Kk * 0.45, new Point(500, 222));
    private final static Define T0    = new Define(Tk / 1.2, new Point(500, 239));

    private final static Define I0    = new Define(0, new Point(500, 273));
    private final static Define Ik0   = new Define(0, new Point(500, 290));
    private final static Define It0   = new Define(0, new Point(500, 307));

    private final static Define step  = new Define(0, new Point(500, 341));

    private final static Define K     = new Define("K0", new Point(500, 427));
    private final static Define T     = new Define("T0", new Point(500, 444));

//    ------   points   ------

    private final static Point RUN      = new Point(296, 120);
    private final static Point SHOW     = new Point(952, 121);
    private final static Point TEXT     = new Point(130, 595);
    private final static Point CHART    = new Point(64, 594);

    static void start() {
        for (int i = 0; i < 100; i++) {
            K.setValue("K0");
            T.setValue("T0");

//            ------   I0   ------
            I0.setValue(getValue());

//            ------   Ik0   ------
            String saveK0 = K0.sValue;
            K0.setValue(saveK0 + " + delta");

            Ik0.setValue(getValue());
            K0.setValue(saveK0);

//            ------   It0   ------
            String saveT0 = T0.sValue;
            T0.setValue(saveT0 + " + delta");

            It0.setValue(getValue());
            T0.setValue(saveT0);

//            ------   stepping   ------
            K.setValue("K1");
            T.setValue("T1");

            makeStepping();

            K0.setValue((Ik0.dValue - I0.dValue) / delta * step.dValue + K0.dValue);
            T0.setValue((It0.dValue - T0.dValue) / delta * step.dValue + T0.dValue);
        }
    }

    private static double getValue() {
        ROBOT.mouseClick(CHART);
        ROBOT.mouseClick(RUN);
        ROBOT.mouseClick(SHOW);

        BufferedImage screenShot = ROBOT.createScreenCapture(SCREEN);

        ROBOT.mouseClick(TEXT);

        String data = ImageDecoder.decode(screenShot);
        data = data.replaceAll("1\\.000K.", "");

        return Double.parseDouble(data);
    }

    private static void makeStepping() {
        double ds = 0.1;
        double __step = 0;
        double err;
        do {
            step.setValue(__step - ds);
            double minusI = getValue();

            step.setValue(__step);
            double zeroI = getValue();

            step.setValue(__step + ds);
            double plusI = getValue();

            if (zeroI < minusI && zeroI < plusI)
                ds /= 10;
            else if (minusI < plusI)
                __step = __step - ds;
            else
                __step = __step + ds;

            double errMinus = Math.abs(zeroI - minusI);
            double errPlus = Math.abs(zeroI - plusI);

            err = Math.max(errMinus, errPlus);
        } while (err > 0.01);
    }
}
