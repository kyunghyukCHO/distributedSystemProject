package Client;

import javax.swing.*;
import java.io.File;
import java.net.Socket;
import java.util.ArrayList;

import Client.UI.*;

public class Controller {

    // 초기 설정 값
    private Socket socketToServer;
    private String ip = null;
    private String id = "test";
    private String name = "test";
    private String fileName = null;
    private ArrayList<String> fileNames = new ArrayList<String>();
    private int port = 0;

    private Send SendThread; // Send 스레드
    private Receive ReceiveThread; // Receice 스레드
    private FileSender fileSender;
    private JTextArea screen; // 메인 UI 의 Screen
    private JTextArea connectingScreen; // 메인 UI 의 현재 접속 인원 Screen

    public void start() {
        if (ip != null && port != 0) {
            connectServer(); // 서버와 연결
        }
    }

    // 초기화
    public void setIP(String ip) {
        this.ip = ip;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) { this.name = name; }
    public void setPort(int port) {
        this.port = port;
    }

    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
        makeFileThread();
    }

    // 서버와 연결
    private void connectServer() {
        try {
            socketToServer = new Socket(ip, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 스레드 생성
    private void makeThread() {
        SendThread = new Send();
        SendThread.setSocket(socketToServer);
        SendThread.setId(id);
        SendThread.setUserName(name);
        SendThread.start();

        ReceiveThread = new Receive();
        ReceiveThread.setSocket(socketToServer);
        ReceiveThread.setConnectingScreen(connectingScreen);
        ReceiveThread.setScreen(screen);
        ReceiveThread.start();

    }

    public void makeFileThread() {
        fileSender = new FileSender();
        fileSender.setSocket(socketToServer);
        fileSender.setFileNames(fileNames);
        fileSender.start();
    }


    public void setConnectingScreen(JTextArea connectingScreen) {
        this.connectingScreen = connectingScreen;

    }

    public void setScreen(JTextArea screen) {
        this.screen = screen;
        makeThread();
    }


}