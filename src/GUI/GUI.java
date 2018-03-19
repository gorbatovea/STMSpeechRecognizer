package GUI;

import AudioHandler.IAudioHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private JButton startButton = new JButton("START");
    private JButton stopButton = new JButton("STOP");
    private JTextArea text = new JTextArea("Press START");
    private IAudioHandler audioHandler;
    public boolean started = false;

    public GUI(IAudioHandler audioHandler) {
        super("STM Speech Recognition");
        this.audioHandler = audioHandler;
        this.setBounds(100, 100, 500, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 2, 2, 2));
        text.setEditable(false);
        container.add(text);
        container.add(startButton);
        container.add(stopButton);
        startButton.addActionListener(new StartButtonListener());
        stopButton.addActionListener(new StopButtonListener());
    }

    class StartButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            started = true;
            Thread listenThread = new Thread(){
                @Override
                public void run(){
                    audioHandler.listen();
                }
            };
            Thread executionHandler = new Thread(){
                @Override
                public void run(){
                    while(!audioHandler.isFetched()){}
                    stopExec();
                }
            };
            listenThread.start();
            executionHandler.start();
            text.setText("Working...");

        }
    }

    class StopButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            stopExec();
        }
    }

    private void stopExec(){
        if (!started) return;
        else started = false;
        audioHandler.stop();
        while (!audioHandler.isFetched()) {};
        text.setText(audioHandler.getResult());
    }
}
