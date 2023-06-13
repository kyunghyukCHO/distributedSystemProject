package Client2;

import Client.UserController;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MainEventThread extends Thread {
    private Scanner scanner = new Scanner(System.in);

    private Socket socket;
    private boolean fileFlag;

    public void setFileFlag(boolean fileFlag) {
        this.fileFlag = fileFlag;
    }

    private int LogicalClock;

    private PrintWriter printWriter;
    private BufferedInputStream bis;
    private DataInputStream dis;
    private DataOutputStream dos;

    public static ArrayList<String> fileNameList;

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setLogicalClock(int logicalClock) {
        LogicalClock = logicalClock;
        System.out.println("현재 나의 Logical Clock : "+LogicalClock);
    }

    @Override
    public void run() {
        makePrintWriter();

        while (true) {
            System.out.println("1) 현재 접속자  2) 파일 전송  3) 공유 파일 조회  4) 파일 조회  5) 파일 공유  6) 종료");
            String input = scanner.nextLine();

            switch (Integer.parseInt(input)) {
                case 1:
                    requestConnectingUsers();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 2:
                    printWriter.println("FILE");
                    printWriter.flush();

                    printWriter.println(String.valueOf(LogicalClock));
                    printWriter.flush();

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    String directoryPath = "/Users/chokyunghyuk/testtest4/src/Client2/LocalFiles";

                    // 디렉토리 객체 생성
                    File directory = new File(directoryPath);

                    // 디렉토리 내의 파일 목록 가져오기
                    File[] files = directory.listFiles();

                    // 파일 목록 출력
                    for (File file : files) {
                        if (file.isFile()) {
                            System.out.print("  " + file.getName());
                        }
                    }
                    System.out.println();

                    boolean flag = false;
                    fileNameList = new ArrayList<String>();
                    while (!flag) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.print("파일의 이름을 입력하세요 (send 입력시 전송) : ");
                        String fileName = scanner.nextLine();
                        if (fileName.equals("send")) {
                            flag = true;
                        } else {
                            fileNameList.add(fileName);
                        }
                    }

                    // TODO !!
                    // 서버 전송 코드
                    try {
                        ArrayList<File> fileList = new ArrayList<File>();

                        for (String fileName : fileNameList) {
                            fileList.add(new File("/Users/chokyunghyuk/testtest4/src/Client2/LocalFiles/" + fileName));
                        }

                        dos = new DataOutputStream(socket.getOutputStream());
                        dis = new DataInputStream(socket.getInputStream());
                        dos.writeInt(fileNameList.size());
                        int readSize = 0;
                        for (int i = 0; i < fileNameList.size(); i++) {
                            long transferSize = fileList.get(i).length();
                            int bufSize = 1024;
                            int totalReadCount = (int) transferSize / bufSize;
                            int lastReadCount = (int) transferSize % bufSize;

                            dos.writeUTF(fileNameList.get(i));
                            dos.writeLong(transferSize);

                            bis = new BufferedInputStream(new FileInputStream(fileList.get(i)));
                            byte[] buffer = new byte[bufSize];

                            for (int k = 0; k < totalReadCount; k++) {
                                if ((readSize = bis.read(buffer, 0, bufSize)) != -1) {
                                    dos.write(buffer, 0, readSize);
                                }
                            }
                            if (lastReadCount > 0) {
                                if ((readSize = bis.read(buffer, 0, lastReadCount)) != -1) {
                                    dos.write(buffer, 0, readSize);
                                }
                            }
                        }
                        System.out.println("파일 전송 완료!!");
                        LogicalClock++;
                        System.out.println("나의 Logical Clock : "+LogicalClock);

                        break;

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (bis != null) {
                                bis.close();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 3:
                    String sharedDirectoryPath = "/Users/chokyunghyuk/testtest4/src/Client2/SharedFiles";

                    // 디렉토리 객체 생성
                    File sharedDirectory = new File(sharedDirectoryPath);

                    // 디렉토리 내의 파일 목록 가져오기
                    File[] sharedFiles = sharedDirectory.listFiles();

                    // 파일 목록 출력
                    for (File file : sharedFiles) {
                        if (file.isFile()) {
                            System.out.print("  " + file.getName());
                        }
                    }
                    System.out.println();
                    break;
                case 4:
                    printWriter.println("FILELIST");
                    printWriter.flush();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (fileFlag == true) {
                        System.out.println("1) 서버 파일 수정   2) 서버 파일 삭제");
                        switch(Integer.parseInt(scanner.nextLine())) {
                            case 1:
                                printWriter.println("UPDATEFILE");
                                printWriter.flush();

                                printWriter.println(String.valueOf(LogicalClock));
                                printWriter.flush();

                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                                System.out.print("내용을 추가할 파일의 이름을 입력하세요: ");
                                String updateFileName = scanner.nextLine();
                                String updateFilePath = "/Users/chokyunghyuk/testtest4/src/Client2/ServerFiles/" + updateFileName;

                                System.out.println("파일에 추가할 내용을 입력 후 enter 를 누르면 추가됩니다. (END 입력시 종료)");

                                try {
                                    dos = new DataOutputStream(socket.getOutputStream());
                                    dos.writeUTF(updateFileName);

                                    ArrayList<String> updateContents = new ArrayList<String>();

                                    while(true) {
                                        String content = scanner.nextLine();
                                        if (content.equals("END")) {
                                            break;
                                        } else {
                                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(updateFilePath, true))) {
                                                writer.write(content);
                                                writer.newLine();
                                                System.out.println("파일에 내용이 추가되었습니다.");
                                                updateContents.add(content);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    dos.writeInt(updateContents.size());
                                    for (int j = 0; j < updateContents.size(); j++) {
                                        dos.writeUTF(updateContents.get(j));
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                LogicalClock++;
                                System.out.println("나의 Logical Clock : "+LogicalClock);
                                break;


                            case 2:
                                printWriter.println("DELETEFILE");
                                printWriter.flush();

                                printWriter.println(String.valueOf(LogicalClock));
                                printWriter.flush();

                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                System.out.print("삭제할 파일 명을 입력하세요 : ");
                                String deleteFileName = scanner.nextLine();

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                                try {
                                    dos = new DataOutputStream(socket.getOutputStream());
                                    dos.writeUTF(deleteFileName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                String filePath = "/Users/chokyunghyuk/testtest4/src/Client2/ServerFiles/"+deleteFileName; // 삭제하려는 파일의 경로를 지정하세요.

                                File file = new File(filePath);

                                if (file.exists() && file.isFile()) {
                                    if (file.delete()) {
                                        System.out.println("파일 삭제: " + file.getName());
                                    } else {
                                        System.out.println("파일 삭제 실패: " + file.getName());
                                    }
                                } else {
                                    System.out.println("해당 파일이 존재하지 않거나 파일이 아닙니다.");
                                }
                                LogicalClock++;
                                System.out.println("나의 Logical Clock : "+LogicalClock);

                        }
                    } else {
                        System.out.println("파일 조회를 종료합니다.");
                        break;
                    }

                    break;

                case 5:
                    System.out.println("1) 공유 파일 업로드   2) 공유 파일 업데이트 ");
                    System.out.print("입력하세요 : ");
                    int num = Integer.parseInt(scanner.nextLine());
                    switch (num) {
                        case 1:
                            printWriter.println("USER");
                            printWriter.flush();

                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }

                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }

                            System.out.print("공유하고자 하는 유저의 아이디를 입력하세요 : ");
                            String userId = scanner.nextLine();
                            System.out.println();

                            String directoryPath2 = "/Users/chokyunghyuk/testtest4/src/Client2/SharedFiles";

                            // 디렉토리 객체 생성
                            File directory2 = new File(directoryPath2);

                            // 디렉토리 내의 파일 목록 가져오기
                            File[] files2 = directory2.listFiles();

                            // 파일 목록 출력
                            for (File file : files2) {
                                if (file.isFile()) {
                                    System.out.print("  " + file.getName());
                                }
                            }
                            System.out.println();

                            System.out.print("공유하고자 하는 파일의 이름을 입력하세요 : ");
                            String fileName = scanner.nextLine();
                            System.out.println("공유 파일을 업로드 합니다.");

                            printWriter.println("SHARING");
                            printWriter.flush();


                            File sharedFile = new File("/Users/chokyunghyuk/testtest4/src/Client2/SharedFiles/"+fileName);
                            try {
                                dos = new DataOutputStream(socket.getOutputStream());
                                dis = new DataInputStream(socket.getInputStream());
                                int readSize = 0;
                                long transferSize = sharedFile.length();
                                int bufSize = 1024;
                                int totalReadCount = (int) transferSize / bufSize;
                                int lastReadCount = (int) transferSize % bufSize;

                                dos.writeUTF(userId+":"+fileName);
                                dos.writeLong(transferSize);

                                bis = new BufferedInputStream(new FileInputStream(sharedFile));
                                byte[] buffer = new byte[bufSize];

                                for (int k = 0; k < totalReadCount; k++) {
                                    if ((readSize = bis.read(buffer, 0, bufSize)) != -1) {
                                        dos.write(buffer, 0, readSize);
                                    }
                                }
                                if (lastReadCount > 0) {
                                    if ((readSize = bis.read(buffer, 0, lastReadCount)) != -1) {
                                        dos.write(buffer, 0, readSize);
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            printWriter.println("UPDATESHARINGFILE");
                            printWriter.flush();

                            try {
                                Thread.sleep(800);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }

                            System.out.print("수정하고자 하는 파일의 이름을 입력하세요 : ");
                            String sharedFileName = scanner.nextLine();

                            String sharedFilePath = "/Users/chokyunghyuk/testtest4/src/Client2/SharedFiles/" + sharedFileName;

                            System.out.println("파일에 추가할 내용을 입력 후 enter 를 누르면 추가됩니다. (END 입력시 종료)");

                            try {
                                dos = new DataOutputStream(socket.getOutputStream());
                                dos.writeUTF(sharedFileName);
                                dos.writeUTF(UserController.getUserId());

                                ArrayList<String> updateContents = new ArrayList<String>();

                                while(true) {
                                    String content = scanner.nextLine();
                                    if (content.equals("END")) {
                                        break;
                                    } else {
                                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sharedFilePath, true))) {
                                            writer.write(content);
                                            writer.newLine();
                                            System.out.println("파일에 내용이 추가되었습니다.");
                                            updateContents.add(content);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                dos.writeInt(updateContents.size());
                                for (int j = 0; j < updateContents.size(); j++) {
                                    dos.writeUTF(updateContents.get(j));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                    }
                    break;

                case 6:
                    makeSendExitThread();
                    System.exit(0);

            }
        }
    }


    private void makePrintWriter() {
        try{
            printWriter = new PrintWriter(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private void requestConnectingUsers() {
        printWriter.println("USER");
        printWriter.flush();
    }

    private void makeSendExitThread() {
        printWriter.println("EXIT");
        printWriter.flush();
        System.out.println("사용자 종료 요청");
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}