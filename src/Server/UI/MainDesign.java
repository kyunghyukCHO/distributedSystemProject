package Server.UI;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class MainDesign {

    private JFrame frame;
    private endBtn endBtn;
    private JoinUserField joinfield;
    private JScrollPane scroll;
    private ChatScreen screen;

    public void makeFrame() {
        drawFrame();
        drawStartButton();
        drawJoinField();
        drawScreen();
        frame.repaint();
    }

    private void drawFrame() {
        frame = new JFrame();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    private void drawStartButton() {
        endBtn = new endBtn();
        frame.add(endBtn);
    }

    private void drawJoinField() {
        joinfield = new JoinUserField();
        frame.add(joinfield);
    }
    private void drawScreen() {
        screen = new ChatScreen();
        scroll = new JScrollPane(screen, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(10,110,480,250);
        screen.setCaretPosition(screen.getDocument().getLength());
        frame.add(scroll);
    }

    public ChatScreen getScreen() {
        return this.screen;
    }

    public JoinUserField getJoinField() {
        return this.joinfield;
    }

    public endBtn getButton() {
        return this.endBtn;
    }
}
