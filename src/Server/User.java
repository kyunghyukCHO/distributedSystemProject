package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class User {
    private Socket socket;
    private String id;
    private String name;
    private PrintWriter send;

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void makeWriter(){
        try{
            send = new PrintWriter(socket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg){
        send.println(msg);
        send.flush();
    }

    public Socket getSocket() {
        return socket;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
