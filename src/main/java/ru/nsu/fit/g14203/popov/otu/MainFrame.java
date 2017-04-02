package ru.nsu.fit.g14203.popov.otu;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private Thread thread;

    public MainFrame() {
        super("OTU");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setSize(150, 100);
        setResizable(false);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(2, 1));

        JButton start = new JButton("Start");
        start.addActionListener(e -> {
            thread = new Thread(PIRegulator::start);
            thread.start();
        });
        add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(e -> {
            if (thread != null && thread.isAlive())
                thread.stop();
        });
        add(stop);

        setVisible(true);
    }
}
