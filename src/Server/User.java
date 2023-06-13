package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private Socket socket;
    private String id;
    private PrintWriter printWriter;

    public HashMap<String, String> sharing = new HashMap<>();
    public HashMap<String, String> getSharing() {
        return sharing;
    }

    public void makeWriter(){
        try{
            printWriter = new PrintWriter(socket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        printWriter.println(message);
        printWriter.flush();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getId() {
        return id;
    }
}
