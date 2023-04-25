package Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {
    private int port = 0;
    private ServerSocket serverSocket;
    private Socket socket;
    private User user;
    public static ArrayList<User> List;
    private JTextArea screen;
    private JTextField join;
    private JTextArea fileScreen;
    private JButton endBtn;

    public void start() {
        if (port != 0) {
            List = new ArrayList<User>();
            makeServerSocket();
            makeClientSocket();
            endEvent();
            acceptClient();
        } else {
            System.out.println("set server port");
        }

    }

    public void setPort(int port) {
        this.port = port;
    }

    private void makeServerSocket() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeClientSocket() {
        socket = new Socket();
    }

    private void acceptClient() {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try {
            System.out.println("========================================");
            System.out.println("|    Server started on port " + port+"       |");
            System.out.println("========================================");
            while (true) {
                socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread();
                user = new User();
                user.setSocket(socket);
                user.makeWriter();
                serverThread.setUser(user);
                serverThread.setJoinField(join);
                serverThread.setScreen(screen);
                serverThread.setFileScreen(fileScreen);
                List.add(user);
                pool.submit(serverThread);
                JoinFieldUpdate();
            }
        } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private void endEvent() {
        endBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JoinFieldUpdate();
            }
        });
    }

    public void setScreen(JTextArea screen) {
        this.screen = screen;
    }

    public void setFileScreen(JTextArea fileScreen) { this.fileScreen = fileScreen; }

    public void setJoinField(JTextField join) {
        this.join = join;
    }

    public void setEndButton(JButton endBtn) {
        this.endBtn = endBtn;
    }

    private void JoinFieldUpdate() {
        String str = new String();
        str = "Connected User  : ";
        for (int i = 0; i < ServerController.List.size(); i++) {
            str += ServerController.List.get(i).getId() + " ";
        }
        join.setText(str);
    }


}
