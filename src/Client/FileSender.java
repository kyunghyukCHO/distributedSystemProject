package Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class FileSender extends Thread {
    private Socket Server;
    private ArrayList<String> fileNames;
    public static final int DEFAULT_BUFFER_SIZE = 10000;

    public void setFileNames(ArrayList<String> fileNames) { this.fileNames = fileNames; }
    public void setSocket(Socket Server) { this.Server = Server; }

    public void run() {
        super.run();
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        try {
            ArrayList<File> files = new ArrayList<File>();
            for (String fileName : fileNames) {
                fileName = "/Users/chokyunghyuk/Desktop/testtest3/src/Client/File/" + fileName;
                files.add(new File(fileName));
            }
            dos = new DataOutputStream(Server.getOutputStream());
            dis = new DataInputStream(Server.getInputStream());

            dos.writeInt(fileNames.size());

            int readSize = 0;

            for (int i = 0; i < fileNames.size(); i++) {
                System.out.println("start file trans"+ i);
                long transferSize = files.get(i).length();
                int bufSize = 1024;
                int totalReadCount = (int) transferSize / bufSize;
                int lastReadSize = (int) transferSize % bufSize;
                dos.writeUTF(fileNames.get(i));
                dos.writeLong(transferSize);
                bis = new BufferedInputStream(new FileInputStream(files.get(i)));
                byte[] buffer = new byte[bufSize];

                for (int k = 0; k < totalReadCount; k++) {
                    if ((readSize = bis.read(buffer,0, bufSize)) != -1) {
                        dos.write(buffer,0,readSize);
                    }
                }

                if (lastReadSize > 0) {
                    System.out.println("LastReadSize : "+lastReadSize);
                    if ((readSize = bis.read(buffer, 0, lastReadSize)) != -1) {
                        System.out.println("readSize : "+readSize);
                        dos.write(buffer, 0, readSize);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (dos != null) dos.close();
                if (dis != null) dis.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }


    }

}



