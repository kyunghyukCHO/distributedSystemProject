package Client2;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PersistentThread extends Thread {
    private BufferedReader bufferedReader;
    private String message;
    private Socket socket;

    private BufferedOutputStream bos;
    private DataInputStream dis;
    private DataOutputStream dos;
    private BufferedInputStream bis;
    private ArrayList<String> receivedFileNames;
    private String fileName;


    private int port;
    private String ip;

    private int clientLogicalClock;

    private MainEventThread mainEventThread;

    public void run() {
        super.run();

        mainEventThread = new MainEventThread();
        mainEventThread.setSocket(socket);
        mainEventThread.setLogicalClock(clientLogicalClock);
        mainEventThread.start();

        makeBufferedReader();
        receiveMessage();
    }


    public void makeBufferedReader() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() {
        while (true) {
            try {
                message = bufferedReader.readLine();
                if (message.contains(":")) {
                    String[] pars = message.split(":");
                    if (pars[0].equals("JOIN")) {
                        System.out.println(pars[1] + " 가 들어왔습니다.");
                        System.out.println("1) 현재 접속자  2) 파일 전송  3) 공유 파일 조회  4) 파일 조회  5) 파일 공유  6) 종료");
                    }
                    else if (pars[0].equals("EXIT")) {
                        System.out.println(pars[1] + " 가 나갔습니다.");
                        System.out.println("1) 현재 접속자  2) 파일 전송  3) 공유 파일 조회  4) 파일 조회  5) 파일 공유  6) 종료");
                    }
                    else if (pars[0].equals("USER")) {
                        System.out.println(pars[1]);
                    }
                    else if (pars[0].equals("FILELIST")) {
                        if (pars[1].equals("NULL")) {
                            System.out.println("서버에 저장된 파일이 존재하지 않습니다.");
                            mainEventThread.setFileFlag(false);
                        }else {
                            System.out.println(pars[1]);
                            mainEventThread.setFileFlag(true);
                        }
                    }
                    else if (pars[0].equals("FILEUPLOAD")) {
                        System.out.println(pars[1]);
                        System.out.println("1) 현재 접속자  2) 파일 전송  3) 공유 파일 조회  4) 파일 조회  5) 파일 공유  6) 종료");
                    }
                    else if (pars[0].equals("CLOCK")) {
                        System.out.println("Logical Clock 오류입니다. 현재 서버의 CLOCK 은 "+pars[1]+"입니다.");
                        System.out.println("CLOCK 수정 후 재시도합니다.");
                        mainEventThread.setLogicalClock(Integer.parseInt(pars[1]));
                    }
                    else if (pars[0].equals("SHARING")) {
                        System.out.println(pars[1]);
                        System.out.println("1) 현재 접속자  2) 파일 전송  3) 공유 파일 조회  4) 파일 조회  5) 파일 공유  6) 종료");
                    }
                    else if (pars[0].equals("LogicalClockError")) {
                        String[] pars2 = pars[1].split(",");
                        SendInfoThread sendInfoThread = UserController.getSendInfoThread();
                        sendInfoThread.writer.println("REQUESET");
                        sendInfoThread.writer.flush();
                        System.out.println("Logical Clock 오류입니다. 현재 서버의 CLOCK 은 "+pars2[0]+"입니다.");
                        System.out.println("CLOCK 수정 후 재시도합니다.");
                        mainEventThread.setLogicalClock(Integer.parseInt(pars2[0]));
                        sendInfoThread.writer.println(pars2[1]);
                        sendInfoThread.writer.flush();
                        sendInfoThread.writer.println(pars2[0]);
                        sendInfoThread.writer.flush();
                    } else if (pars[0].equals("DELETE")) {
                        String deleteFileName = pars[1];
                        String filePath = "/Users/chokyunghyuk/testtest4/src/Client2/ServerFiles/" + deleteFileName; // 삭제하려는 파일의 경로를 지정하세요.
                        File file = new File(filePath);

                        if (file.exists() && file.isFile()) {
                            if (file.delete()) {
                                System.out.println("파일 삭제: " + file.getName());
                            } else {
                                System.out.println("파일 삭제 실패: " + file.getName());
                            }
                        }
                    } else if (pars[0].equals("UPDATE")) {
                        String updatedFileName = pars[1];

                        try {
                            dis = new DataInputStream(socket.getInputStream());
                            dos = new DataOutputStream(socket.getOutputStream());
                            try {
                                fileName = dis.readUTF();
                                receivedFileNames.add(fileName);
                                File copyFile = new File("/Users/chokyunghyuk/testtest4/src/Client2/ServerFiles/" + fileName);
                                long fileTransferSize = dis.readLong();
                                bos = new BufferedOutputStream(new FileOutputStream(copyFile, false));
                                int bufSize = 1024;
                                int totalReadCount = (int) fileTransferSize / bufSize;
                                int lastReadSize = (int) fileTransferSize % bufSize;
                                byte[] buffer = new byte[bufSize];

                                int readBufLength = 0;
                                for (int k = 0; k < totalReadCount; k++) {
                                    if ((readBufLength = dis.read(buffer, 0, bufSize)) != -1) {
                                        bos.write(buffer, 0, readBufLength);
                                    }
                                }
                                if (lastReadSize > 0) {
                                    if ((readBufLength = dis.read(buffer, 0, lastReadSize)) != -1) {
                                        bos.write(buffer, 0, readBufLength);
                                    }
                                }
                                if (bos != null) {
                                    bos.close();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                else if (message.equals("RESPONSEFILE")) {
                    try {
                        dis = new DataInputStream(socket.getInputStream());
                        dos = new DataOutputStream(socket.getOutputStream());
                        try {

                            int fileTransferCount = dis.readInt();
                            receivedFileNames = new ArrayList<String>();
                            for (int i = 0; i < fileTransferCount; i++) {
                                fileName = dis.readUTF();
                                receivedFileNames.add(fileName);
                                File copyFile = new File("/Users/chokyunghyuk/testtest4/src/Client2/ServerFiles/" + fileName);
                                long fileTransferSize = dis.readLong();
                                bos = new BufferedOutputStream(new FileOutputStream(copyFile, false));
                                int bufSize = 1024;
                                int totalReadCount = (int) fileTransferSize / bufSize;
                                int lastReadSize = (int) fileTransferSize % bufSize;
                                byte[] buffer = new byte[bufSize];

                                int readBufLength = 0;
                                for (int k = 0; k < totalReadCount; k++) {
                                    if ((readBufLength = dis.read(buffer, 0, bufSize)) != -1) {
                                        bos.write(buffer, 0, readBufLength);
                                    }
                                }
                                if (lastReadSize > 0) {
                                    if ((readBufLength = dis.read(buffer, 0, lastReadSize)) != -1) {
                                        bos.write(buffer, 0, readBufLength);
                                    }
                                }
                                if (bos != null) {
                                    bos.close();
                                }
                            }
                            String receivedFileNameString = "";
                            for (String str : receivedFileNames) {
                                receivedFileNameString += (" " + str);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (message.equals("SHARINGFILE")) {
                    try {
                        dis = new DataInputStream(socket.getInputStream());
                        dos = new DataOutputStream(socket.getOutputStream());
                        try {
                            fileName = dis.readUTF();
                            receivedFileNames.add(fileName);
                            File copyFile = new File("/Users/chokyunghyuk/testtest4/src/Client2/SharedFiles/" + fileName);
                            long fileTransferSize = dis.readLong();
                            bos = new BufferedOutputStream(new FileOutputStream(copyFile, false));
                            int bufSize = 1024;
                            int totalReadCount = (int) fileTransferSize / bufSize;
                            int lastReadSize = (int) fileTransferSize % bufSize;
                            byte[] buffer = new byte[bufSize];

                            int readBufLength = 0;
                            for (int k = 0; k < totalReadCount; k++) {
                                if ((readBufLength = dis.read(buffer, 0, bufSize)) != -1) {
                                    bos.write(buffer, 0, readBufLength);
                                }
                            }
                            if (lastReadSize > 0) {
                                if ((readBufLength = dis.read(buffer, 0, lastReadSize)) != -1) {
                                    bos.write(buffer, 0, readBufLength);
                                }
                            }
                            if (bos != null) {
                                bos.close();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }


    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}




