package Client.UI;

import Client.Controller;
import Client.FileSender;
import Client.Send;
import Server.ServerController;
import Server.ServerThread;
import Server.UI.JoinUserField;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class MainDesign {

    private JFrame frame;
    private JTextArea screen;
    private JTextArea connectingScreen;
    private Logoutbtn logoutbtn;
    private Sendbtn sendbtn;
    private FileLabel fileLabel;
    private fileNameField fileNameField;
    private JTextField input;
    private String fileName = "";
    private boolean flag = false;
    private String fileNameView = "Selected Files : ";

    private ArrayList<String> fileNames = new ArrayList<String>();

    public void makeFrame() {
        drawFrame();
        drawChat();
        drawButtons();
        drawFileLabel();
        drawFileField();
        frame.repaint();
    }

    private void drawFileField() {
        fileNameField = new fileNameField();
        frame.add(fileNameField);
    }

    private void drawFileLabel() {
        fileLabel = new FileLabel();
        frame.add(fileLabel);
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
        makeSendButton();
    }

    private void makeSendButton() {
        sendbtn = new Sendbtn();
        frame.add(sendbtn);
        SendButtonEvent();
    }

    private void SendButtonEvent() {
        sendbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag = true;
                fileNameView = "Selected Files : ";
                fileNameField.setText(fileNameView);

            }
        });
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
        drawInputText();
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

    private void drawInputText() {
        input = new JTextField();
        input.setBounds(90, 500, 270, 40);
        Border inputborder = BorderFactory.createLineBorder(Color.BLACK, 3);
        input.setBorder(inputborder);
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    fileName = input.getText();
                    fileNames.add(fileName);
                    input.setText("");
                    fileNameView +=  fileName + " / ";
                    fileNameField.setText(fileNameView);
                }
            }

        });
        frame.add(input);
    }


    public JTextArea getScreen() {
        return this.screen;
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }

    public boolean isFlag() {
        return flag;
    }

    public JTextArea getConnectingScreen() {
        return this.connectingScreen;
    }

    public fileNameField getFileNameField() { return fileNameField; }
}