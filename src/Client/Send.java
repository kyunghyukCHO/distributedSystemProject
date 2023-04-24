package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Send extends Thread{
    private Socket Server;
    private String id;
    private String userName;
    static public PrintWriter writer;


    public void run() {
        super.run();
        makeSender();
        sendID();
        sendName();

    }

    public void setId(String id) {
        this.id = id;
    }
    public void setSocket(Socket Server) { this.Server = Server; }
    public void setUserName(String userName) { this.userName = userName; }

    // writer 생성
    private void makeSender(){
        try{
            writer = new PrintWriter(Server.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void sendID(){
        writer.println(id);
        writer.flush();
    }

    private void sendName(){
        writer.println(userName);
        writer.flush();
    }

}
