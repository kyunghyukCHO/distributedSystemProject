package Client2;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receive extends Thread {

    private Socket Server;
    private BufferedReader msgbuff;
    private String msg;
    private JTextArea screen;
    private JTextArea connectingScreen;

    public void run() {
        super.run();
        makeMsgBuff();
        getMsg();
    }

    public void setSocket(Socket Server) {
        this.Server = Server;
    }

    private void makeMsgBuff() {
        try {
            msgbuff = new BufferedReader(new InputStreamReader(Server.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setScreen(JTextArea screen) {
        this.screen = screen;
    }

    public void setConnectingScreen(JTextArea connectingScreen) {
        this.connectingScreen = connectingScreen;
    }

    private void getMsg() {
        while (true) {
            try {
                msg = msgbuff.readLine();
                if (msg.contains(":")) {
                    String[] pars = msg.split(":");
                    if (pars[0].equals("JOIN")) {
                        screen.append(pars[1] + " join the room.\n");
                        screen.setCaretPosition(screen.getDocument().getLength());
                    }
                    else if (pars[0].equals("EXIT")) {
                        screen.append(pars[1] + " out the room.\n");
                        screen.setCaretPosition(screen.getDocument().getLength());
                    } else if (pars[0].equals("LIST")) {
                        connectingScreen.getDocument().remove(0, connectingScreen.getDocument().getLength());
                        String[] names = pars[1].split(",");
                        connectingScreen.append("==== clients currently connected to the server ===="+"\n");
                        for (String name : names) {
                            connectingScreen.append(name + " is connecting ! "+"\n");
                        }
                    }
                }
            } catch (IOException e) {

            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }



}