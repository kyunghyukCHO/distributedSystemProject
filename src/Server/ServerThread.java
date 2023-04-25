package Server;

import Client.Send;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread{
    private User user;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedOutputStream bos = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private String ID;
    private String name;
    private String msg;
    private String ClientList = "";
    private JTextArea screen;
    private JTextField join;
    private JTextArea fileScreen;
    private ArrayList<String> fileNames = new ArrayList<String>();

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

    private void setClient() { this.socket = user.getSocket(); }

    // Reader 초기화
    public void makeBufferedReader(){
        try{
            bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }


//    // 대기
//    private void waitExitMsg(){
//        while(true){
//            try{
//                msg = bufferedReader.readLine();
//                if (msg.contains("EXIT")) {
//                    ServerController.List.remove(user);
//                    msg = "EXIT:" + ID;
//                    allUserSendMsg();
//                    screen.append(ID + " out the room\n");
//                    JoinFieldUpdate();
//                    ServerThread.interrupted();
//                    break;
//                }
//                else if (msg.contains("FILE")) {
//                    try {
//                        dis = new DataInputStream(socket.getInputStream());
//                        dos = new DataOutputStream(socket.getOutputStream());
//                        int fileTransferCount = 0;
//                        long fileTransferSize = 0;
//                        String fileName = null;
//                        File copyFile = null;
//                        try {
//                            fileTransferCount = dis.readInt();
//                            for (int i = 0; i < fileTransferCount; i++) {
//                                String tmp = dis.readUTF();
//                                fileNames.add(tmp);
//                                fileName = "/Users/chokyunghyuk/Desktop/testtest2/src/Server/File/" + tmp;
//                                copyFile = new File(fileName);
//                                fileTransferSize = dis.readLong();
//                                bos = new BufferedOutputStream(new FileOutputStream(copyFile, false));
//                                int bufSize = 1024;
//                                long count = 0;
//                                int totalReadCount = (int) fileTransferSize / bufSize;
//                                int lastReadSize = (int) fileTransferSize % bufSize;
//                                byte[] buffer = new byte[bufSize];
//                                int readBufLength = 0;
//                                for (int k = 0; k < totalReadCount; k++) {
//                                    if ((readBufLength = dis.read(buffer, 0, bufSize)) != -1) {
//                                        bos.write(buffer, 0, readBufLength);
//                                        count += readBufLength;
//                                    }
//                                }
//                                if (lastReadSize > 0) {
//                                    if ((readBufLength = dis.read(buffer, 0, lastReadSize)) != -1) {
//                                        bos.write(buffer, 0, readBufLength);
//                                        count += readBufLength;
//                                    }
//                                }
//                                if (bos != null) {
//                                    bos.close();
//                                }
//                                FileFieldClear();
//                                FileFieldUpdate();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    finally {
//                        if (this.dis != null) {
//                            try {
//                                this.dis.close();
//                                if (this.dos != null) this.dos.close();
//                                if (this.bos != null) this.bos.close();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//    }


    // 대기
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
                else if (msg.contains("FILE")) {
                    try {
                        dis = new DataInputStream(socket.getInputStream());
                        dos = new DataOutputStream(socket.getOutputStream());
                        int fileTransferCount = 0;
                        long fileTransferSize = 0;
                        String fileName = null;
                        File copyFile = null;
                        try {
                            fileTransferCount = dis.readInt();
                            for (int i = 0; i < fileTransferCount; i++) {
                                String tmp = dis.readUTF();
                                fileNames.add(tmp);
                                fileName = "/Users/chokyunghyuk/Desktop/testtest3/src/Server/File/" + tmp;
                                copyFile = new File(fileName);
                                fileTransferSize = dis.readLong();
                                bos = new BufferedOutputStream(new FileOutputStream(copyFile, false));
                                int bufSize = 1024;
                                long count = 0;
                                int totalReadCount = (int) fileTransferSize / bufSize;
                                int lastReadSize = (int) fileTransferSize % bufSize;
                                byte[] buffer = new byte[bufSize];
                                int readBufLength = 0;
                                for (int k = 0; k < totalReadCount; k++) {
                                    if ((readBufLength = dis.read(buffer, 0, bufSize)) != -1) {
                                        bos.write(buffer, 0, readBufLength);
                                        count += readBufLength;
                                    }
                                }
                                if (lastReadSize > 0) {
                                    if ((readBufLength = dis.read(buffer, 0, lastReadSize)) != -1) {
                                        bos.write(buffer, 0, readBufLength);
                                        count += readBufLength;
                                    }
                                }

                                msg = "UPLOAD:" + tmp;
                                allUserSendMsg();
                                FileFieldClear();
                                FileFieldUpdate();

                                if (bos != null) {
                                    bos.close();
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (this.dis != null) {
                            try {
                                this.dis.close();
                                if (this.dos != null) this.dos.close();
                                if (this.bos != null) this.bos.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }catch (IOException e){
                ServerController.List.remove(user);
                msg = "EXIT:" + ID;
                allUserSendMsg();
                screen.append(ID + " out the room\n");
                JoinFieldUpdate();
                ServerThread.interrupted();
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

    private void FileFieldClear() {
        fileScreen.setText("");
    }
    private void FileFieldUpdate() {
        String str = new String();
        str = " ==== Uploaded Files List ==== \n\n";
        for (int i = 0; i < fileNames.size(); i++) {
            str += " "+(i+1)+"  "+fileNames.get(i)+ " \n ";
        }
        fileScreen.append(str);
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


    public void setScreen(JTextArea screen) {
        this.screen = screen;
    }

    public void setFileScreen(JTextArea fileScreen) {
        this.fileScreen = fileScreen;
    }

    public void setJoinField(JTextField join) {
        this.join = join;
    }
}
