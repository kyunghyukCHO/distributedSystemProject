package Client2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SendInfoThread extends Thread{
    private Socket socket;
    private String userId;
    public static PrintWriter writer;

    public void run() {
        super.run();
        connectServer();
        sendUserId();
        sendFileRequest();
    }

    public void connectServer() {
        try{
            writer = new PrintWriter(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendUserId() {
        writer.println(userId);
        writer.flush();
    }

    public void sendFileRequest() {
        writer.println("REQUESTFILE");
        writer.flush();
    }


    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
