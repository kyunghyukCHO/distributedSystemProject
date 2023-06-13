package Server;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {
    private int port = 0;
    private ServerSocket serverSocket;
    private Socket userSocket;
    private User user;
    public static ArrayList<User> userList;
    private static int mainLogicalClock = 0; // 메인 서버의 논리적 시계 값
    public static ArrayList<ServerThread> serverThreads = new ArrayList<ServerThread>();

    public void start() {
        if (port != 0) {
            userList = new ArrayList<User>();
            makeServerSocket();
            acceptClient();
        }
    }

    public static User findUserById(String id) {
        for (User user : userList) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null; // id와 일치하는 User를 찾지 못한 경우 null 반환
    }

    private void makeServerSocket() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptClient() {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try {
            System.out.println("========================================");
            System.out.println("|    Server started on port " + port+"       |");
            System.out.println("========================================");
            while (true) {
                userSocket = serverSocket.accept();
                ServerThread serverThread = new ServerThread();
                user = new User();
                user.setSocket(userSocket);
                user.makeWriter();
                serverThread.setUser(user);
                userList.add(user);
                serverThreads.add(serverThread);
                pool.submit(serverThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setPort(int port) {
        this.port = port;
    }

    public static synchronized void setMainLogicalClock(int mainLogicalClock) {
        ServerController.mainLogicalClock = mainLogicalClock;
    }

    public static synchronized int getMainLogicalClock() {
        return mainLogicalClock;
    }
}
