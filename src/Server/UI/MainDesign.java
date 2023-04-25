package Server.UI;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;


public class MainDesign {

    private JFrame frame;
    private endBtn endBtn;
    private JoinUserField joinfield;
    private JScrollPane scroll;
    private JScrollPane scroll2;
    private ChatScreen screen;
    private FileScreen fileScreen;
    private FileNameLabel fileNameLabel;
    private ClientNameLabel clientNameLabel;

    public void makeFrame() {
        drawFrame();
        drawStartButton();
        drawJoinField();
        drawScreen();
        drawFileScreen();
        drawLabel();
        frame.repaint();
    }

    private void drawFrame() {
        frame = new JFrame();
        frame.setSize(500, 750);
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
        scroll.setBounds(10,450,480,250);
        screen.setCaretPosition(screen.getDocument().getLength());
        frame.add(scroll);
    }

    private void drawFileScreen() {
        fileScreen = new FileScreen();
        scroll2 = new JScrollPane(fileScreen, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll2.setBounds(10,110,480,240);
        fileScreen.setCaretPosition(fileScreen.getDocument().getLength());
        frame.add(scroll2);
    }

    private void drawLabel() {
        fileNameLabel = new FileNameLabel();
        frame.add(fileNameLabel);

        clientNameLabel = new ClientNameLabel();
        frame.add(clientNameLabel);
    }

    public ChatScreen getScreen() {
        return this.screen;
    }

    public FileScreen getFileScreen() { return this.fileScreen; }

    public JoinUserField getJoinField() {
        return this.joinfield;
    }

    public endBtn getButton() {
        return this.endBtn;
    }
}
