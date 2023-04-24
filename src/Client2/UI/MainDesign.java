package Client2.UI;

import Client2.Send;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainDesign {

    private JFrame frame;
    private JTextArea screen;
    private JTextArea connectingScreen;
    private Logoutbtn logoutbtn;

    public void makeFrame() {
        drawFrame();
        drawChat();
        drawButtons();
        frame.repaint();
    }

    private void drawFrame() {
        frame = new JFrame();
        frame.setSize(1000, 850);

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void drawButtons() {
        makeLogoutButton();
    }

    private void makeLogoutButton(){
        logoutbtn = new Logoutbtn();
        frame.add(logoutbtn);
        LogoutButtonEvent();
    }

    public void LogoutButtonEvent(){
        logoutbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Send.writer.println("EXIT");
                Send.writer.flush();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(0);
            }
        });
    }

    private void drawChat() {
        drawScreen();
        drawConnectingScreen();
    }

    private void drawScreen() {
        screen = new JTextArea();
        JScrollPane scroll = new JScrollPane(screen);
        scroll.setBounds(710, 10, 270, 500);
        Border screenborder = BorderFactory.createLineBorder(Color.BLACK, 3);
        screen.setEnabled(false);
        screen.setDisabledTextColor(Color.BLACK);
        screen.setFont(screen.getFont().deriveFont(10));
        screen.setBorder(screenborder);
        screen.setCaretPosition(screen.getDocument().getLength());
        frame.add(scroll);
    }

    private void drawConnectingScreen() {
        connectingScreen = new JTextArea();
        JScrollPane scroll2 = new JScrollPane(connectingScreen);
        scroll2.setBounds(90, 10, 500, 300);
        Border screenborder2 = BorderFactory.createLineBorder(Color.BLACK, 3);
        connectingScreen.setEnabled(false);
        connectingScreen.setDisabledTextColor(Color.BLACK);
        connectingScreen.setFont(connectingScreen.getFont().deriveFont(10));
        connectingScreen.setBorder(screenborder2);
        connectingScreen.setCaretPosition(connectingScreen.getDocument().getLength());
        frame.add(scroll2);
    }


    public JTextArea getScreen() {
        return this.screen;
    }

    public JTextArea getConnectingScreen() {
        return this.connectingScreen;
    }
}