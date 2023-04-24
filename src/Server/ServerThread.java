package Server;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread{
    private User user;
    private Socket socket;
    private BufferedReader bufferedReader;
    private String ID;
    private String name;
    private String msg;
    private String ClientList = "";
    private JTextArea screen;
    private JTextField join;

    @Override
    public void run(){
        setClient();
        makeBufferedReader();
        joinServer();
        waitExitMsg();

    }
    // User 세팅
    public void setUser(User user){
        this.user=user;
    }

    // Reader 초기화
    public void makeBufferedReader(){
        try{
            bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // 종료 대기
    private void waitExitMsg(){
        while(true){
            try{
                msg = bufferedReader.readLine();
                if (msg.contains("EXIT")) {
                    ServerController.List.remove(user);
                    msg = "EXIT:" + ID;
                    allUserSendMsg();
                    screen.append(ID + " out the room\n");
                    JoinFieldUpdate();
                    ServerThread.interrupted();
                    break;
                }
            }catch (IOException e){
                msg = ID + " out the room.";
                allUserSendMsg();
                ServerController.List.remove(user);
                JoinFieldUpdate();
                break;
            }
        }
    }

    private void allUserSendMsg() {
        for (int i = 0; i < ServerController.List.size(); i++) {
            ServerController.List.get(i).sendMessage(msg);
        }
    }

    // 서버에 접속
    private void joinServer() {
        try {
            ID = bufferedReader.readLine();
            name = bufferedReader.readLine();
            user.setId(ID);
            user.setName(name);
            screen.append(ID + " join the room\n");
            JoinFieldUpdate();
        } catch (IOException e) {

        }
        for (int i = 0; i < ServerController.List.size(); i++) {
            ServerController.List.get(i).sendMessage("JOIN:" + ID);
        }
    }

    private void JoinFieldUpdate() {
        String str = new String();
        str = "Connected User  : ";
        for (int i = 0; i < ServerController.List.size(); i++) {
            str += ServerController.List.get(i).getId() + " ";
        }
        join.setText(str);

        ClientList = "";
        for (int i = 0; i < ServerController.List.size(); i++) {
            ClientList += ServerController.List.get(i).getId()+",";
        }
        for (int i = 0; i < ServerController.List.size(); i++) {
            ServerController.List.get(i).sendMessage("LIST:" + ClientList);
        }

    }

    private void setClient() { this.socket = user.getSocket(); }

    public void setScreen(JTextArea screen) {
        this.screen = screen;
    }

    public void setJoinField(JTextField join) {
        this.join = join;
    }
}
