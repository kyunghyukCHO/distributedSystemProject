package Client;

import java.io.File;
import java.net.Socket;

public class UserController {
    private Socket socket;
    private static String userId;
    private String userPw;
    private String ip = null;
    private int port = 0;

    private static SendInfoThread sendInfoThread;
    private static PersistentThread persistentThread;

    public static SendInfoThread getSendInfoThread() {
        return sendInfoThread;
    }

    public void start() {
        if (ip != null && port != 0) {
            connectServer();
            initializeDirectory();
            makeSendInfoThread();
            makePersistentThreads();
        }
    }

    private void connectServer() {
        try {
            socket = new Socket(ip, port);
            System.out.println("서버 연결 완료");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDirectory() {
        String directoryPath = "/Users/chokyunghyuk/testtest4/src/Client/ServerFiles"; // 삭제하려는 디렉토리 경로를 지정하세요.

        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        } else {
            System.out.println("해당 디렉토리가 존재하지 않거나 디렉토리가 아닙니다.");
        }
    }

    private void makeSendInfoThread() {
        sendInfoThread = new SendInfoThread();
        sendInfoThread.setSocket(socket);
        sendInfoThread.setUserId(userId);
        sendInfoThread.start();
    }

    private void makePersistentThreads() {
        persistentThread = new PersistentThread();
        persistentThread.setSocket(socket);
        persistentThread.setIp(ip);
        persistentThread.setPort(port);
        persistentThread.start();

    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public static String getUserId() {
        return userId;
    }

    public void setPort(int port) {
        this.port = port;
    }


}
