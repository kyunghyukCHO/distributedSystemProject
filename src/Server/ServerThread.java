package Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class ServerThread extends Thread{
    private User user;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedOutputStream bos;
    private DataInputStream dis;
    private DataOutputStream dos;
    private BufferedInputStream bis;
    private String fileName;
    private ArrayList<String> receivedFileNames;

    @Override
    public void run(){
        setClient();
        makeReceiveInfoReader();
        receiveInfo();
        System.out.println("클라이언트 " + user.getId() + " 연결 완료!");
        showConnectingUser();
        sendMessageToAllUsers("JOIN:"+user.getId());
        waitMsg();
    }

    public void sendFiles() {
        sendMessageToUser("RESPONSEFILE");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String directoryPath = "/Users/chokyunghyuk/testtest4/src/Server/Files/ServerFiles";

        // 디렉토리 객체 생성
        File directory = new File(directoryPath);

        // 디렉토리 내의 파일 목록 가져오기
        File[] files = directory.listFiles();

        // TODO !!
        // 서버 전송 코드
        try {
            ArrayList<File> fileList = new ArrayList<File>();
            ArrayList<String> fileNameList = new ArrayList<String>();

            for (File f : files) {
                fileList.add(new File("/Users/chokyunghyuk/testtest4/src/Server/Files/ServerFiles/" + f.getName()));
                fileNameList.add(f.getName());
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteFiles(String fileName) {
        sendMessageToUser("DELETE:"+fileName);
    }

    public void updateFile(User user2, String updatedFileName) {
        sendMessageToUser("SHARINGFILE");

        String directoryPath = "/Users/chokyunghyuk/testtest4/src/Server/Files/SharedFiles/"+user2.getId();

        // 디렉토리 객체 생성
        File file = new File(directoryPath+"/"+updatedFileName);

        // TODO !!
        // 서버 전송 코드
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            int readSize = 0;
            long transferSize = file.length();
            int bufSize = 1024;
            int totalReadCount = (int) transferSize / bufSize;
            int lastReadCount = (int) transferSize % bufSize;

            dos.writeUTF(updatedFileName);
            dos.writeLong(transferSize);

            bis = new BufferedInputStream(new FileInputStream(file));
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
    }

    public void setClient() {
        this.socket = user.getSocket();
    }

    public void makeReceiveInfoReader() {
        try{
            bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void receiveInfo() {
        try {
            String id = bufferedReader.readLine();
            user.setId(id);
        } catch (IOException e) {

        }
    }

    public void showConnectingUser() {
        System.out.println("=== 현재 접속 인원 ===");
        for (int i = 0; i < ServerController.userList.size()-1; i++) {
            System.out.print(ServerController.userList.get(i).getId() + " , ");
        }
        System.out.print(ServerController.userList.get(ServerController.userList.size()-1).getId()+"\n");
        System.out.println("===================");
    }

    public void sendMessageToAllUsers(String message) {
        for (int i = 0; i < ServerController.userList.size(); i++) {
            if ( ServerController.userList.get(i) != this.user ) {
                ServerController.userList.get(i).sendMessage(message);
            }
        }
    }

    public void sendMessageToUser(String message) {
        user.sendMessage(message);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void waitMsg() {
        while(true) {
            try {
                String statement = bufferedReader.readLine();
                if (statement.equals("EXIT")) {
                    ServerController.userList.remove(user);
                    sendMessageToAllUsers("EXIT:" + user.getId());
                    System.out.println(user.getId() + " 님이 나갔습니다.");
                    showConnectingUser();
                    ServerController.serverThreads.remove(this);
                }
                else if (statement.equals("USER")) {
                    String msg = "USER: == 현재 서버에 접속중인 유저 ==";
                    sendMessageToUser(msg);
                    msg = "USER:";
                    for (int i = 0; i < ServerController.userList.size(); i++) {
                        msg += " ";
                        msg += ServerController.userList.get(i).getId();

                    }
                    sendMessageToUser(msg);

                }
                else if (statement.equals("FILE")) {
                    int clientLogicalClient = Integer.parseInt(bufferedReader.readLine());
                    if (clientLogicalClient >= ServerController.getMainLogicalClock()) {
                        try {
                            dis = new DataInputStream(socket.getInputStream());
                            dos = new DataOutputStream(socket.getOutputStream());
                            try {
                                int fileTransferCount = dis.readInt();
                                receivedFileNames = new ArrayList<String>();
                                for (int i = 0; i < fileTransferCount; i++) {
                                    fileName = dis.readUTF();
                                    receivedFileNames.add(fileName);
                                    File copyFile = new File("/Users/chokyunghyuk/testtest4/src/Server/Files/ServerFiles/" + fileName);
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
                                System.out.println(user.getId() + " 가 " + receivedFileNameString + " 를 업로드 하였습니다.");
                                sendMessageToAllUsers("FILEUPLOAD:" + user.getId() + " 가 " + receivedFileNameString + " 를 업로드 하였습니다.");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tickTack();
                    } else {
                        sendMessageToUser("LogicalClockError:"+String.valueOf(ServerController.getMainLogicalClock()+","+statement));
                    }

                    // ##############################################################################
                    for (int k = 0; k < ServerController.serverThreads.size(); k++) {
                        ServerController.serverThreads.get(k).sendFiles();
                    }


                }
                else if (statement.equals("FILELIST")) {
                    String directoryPath = "/Users/chokyunghyuk/testtest4/src/Server/Files/ServerFiles";

                    // 디렉토리 객체 생성
                    File directory = new File(directoryPath);

                    if (directory.exists() && directory.isDirectory()) {
                        File[] files = directory.listFiles();

                        if (files != null && files.length == 0) {
                            sendMessageToUser("FILELIST:NULL");
                        } else {
                            String fileList = "FILELIST:";

                            // 파일 목록 출력
                            for (File file : files) {
                                if (file.isFile()) {
                                    fileList += (" " + file.getName());
                                }
                            }
                            sendMessageToUser("FILELIST: == 현재 서버에 저장된 파일 목록 ==");
                            sendMessageToUser(fileList);
                        }
                    } else {
                        System.out.println("해당 디렉토리가 존재하지 않거나 디렉토리가 아닙니다.");
                    }

                }
                else if (statement.equals("UPDATESHARINGFILE")) {

                    String directoryPath = "/Users/chokyunghyuk/testtest4/src/Server/Files/SharedFiles/"+user.getId();
                    File directory = new File(directoryPath);
                    if (directory.exists() && directory.isDirectory()) {
                        File[] files = directory.listFiles();

                        if (files != null && files.length == 0) {
                            sendMessageToUser("FILELIST:NULL");
                        } else {
                            String fileList = "FILELIST:";

                            // 파일 목록 출력
                            for (File file : files) {
                                if (file.isFile()) {
                                    fileList += (" " + file.getName());
                                }
                            }
                            sendMessageToUser("FILELIST: == 현재 공유 가능한 파일 목록 ==");
                            sendMessageToUser(fileList);
                        }
                    } else {
                        System.out.println("해당 디렉토리가 존재하지 않거나 디렉토리가 아닙니다.");
                    }

                    try {
                        dis = new DataInputStream(socket.getInputStream());
                        String updateFileName = dis.readUTF();
                        String userId = dis.readUTF();

                        String updateFilePath = "/Users/chokyunghyuk/testtest4/src/Server/Files/SharedFiles/" + userId+"/"+updateFileName;
                        int contentTransferSize = dis.readInt();
                        for (int j = 0; j < contentTransferSize; j++) {
                            String content = dis.readUTF();
                            // 수정된 내용 파일에 저장
                            try (BufferedWriter writer1 = new BufferedWriter(new FileWriter(updateFilePath, true))) {
                                writer1.write(content);
                                writer1.newLine(); // 새로운 줄로 이동
                            }
                        }
                        String sharedUser = user.getSharing().get(updateFileName);
                        for (int k = 0; k < ServerController.serverThreads.size(); k++) {
                            if (ServerController.serverThreads.get(k).user.getId().equals(sharedUser)) {
                                ServerController.serverThreads.get(k).updateFile(user, updateFileName);
                            }
                        }
//                        String updateFilePath2 = "/Users/chokyunghyuk/testtest4/src/Server/Files/SharedFiles/" + userId+"/"+updateFileName;
//                        Path sourcePath = Path.of("/Users/chokyunghyuk/testtest4/src/Server/Files/SharedFiles/" + userId+"/"+updateFileName);
//                        Path targetPath = Path.of(updateFilePath2);
//                        try {
//                            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
//                            System.out.println("파일이 성공적으로 복사되었습니다.");
//                        } catch (IOException e) {
//                            System.out.println("파일 복사 중 오류가 발생하였습니다: " + e.getMessage());
//                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                }
                else if (statement.equals("REQUESTFILE")) {
                    sendMessageToUser("RESPONSEFILE");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    String directoryPath = "/Users/chokyunghyuk/testtest4/src/Server/Files/ServerFiles";

                    // 디렉토리 객체 생성
                    File directory = new File(directoryPath);

                    // 디렉토리 내의 파일 목록 가져오기
                    File[] files = directory.listFiles();

                    // TODO !!
                    // 서버 전송 코드
                    try {
                        ArrayList<File> fileList = new ArrayList<File>();
                        ArrayList<String> fileNameList = new ArrayList<String>();

                        for (File f : files) {
                            fileList.add(new File("/Users/chokyunghyuk/testtest4/src/Server/Files/ServerFiles/" + f.getName()));
                            fileNameList.add(f.getName());
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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (statement.equals("DELETEFILE")){
                    int clientLogicalClient = Integer.parseInt(bufferedReader.readLine());
                    if (clientLogicalClient >= ServerController.getMainLogicalClock()) {
                        try {
                            dis = new DataInputStream(socket.getInputStream());
                            String fileName = dis.readUTF();

                            String filePath = "/Users/chokyunghyuk/testtest4/src/Server/Files/ServerFiles/"+fileName; // 삭제하려는 파일의 경로를 지정하세요.

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

                            // ###########################################################################################
                            for (int k = 0; k < ServerController.serverThreads.size(); k++) {
                                ServerController.serverThreads.get(k).deleteFiles(fileName);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        tickTack();

                    } else {
                        sendMessageToUser("LogicalClockError:"+String.valueOf(ServerController.getMainLogicalClock()+","+statement));
                    }


                }
                else if(statement.equals("UPDATEFILE")) {
                    int clientLogicalClient = Integer.parseInt(bufferedReader.readLine());
                    if (clientLogicalClient >= ServerController.getMainLogicalClock()) {
                        try {
                            dis = new DataInputStream(socket.getInputStream());
                            String updateFileName = dis.readUTF();
                            String updateFilePath = "/Users/chokyunghyuk/testtest4/src/Server/Files/ServerFiles/" + updateFileName;
                            int contentTransferSize = dis.readInt();
                            for (int j = 0; j < contentTransferSize; j++) {
                                String content = dis.readUTF();
                                // 수정된 내용 파일에 저장
                                try (BufferedWriter writer = new BufferedWriter(new FileWriter(updateFilePath, true))) {
                                    writer.write(content);
                                    writer.newLine(); // 새로운 줄로 이동
                                }
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tickTack();
                    } else {
                        sendMessageToUser("LogicalClockError:"+String.valueOf(ServerController.getMainLogicalClock()+","+statement));
                    }
                }
                else if (statement.equals("SHARING")) {

//
//                    if (user.getSharing2().containsKey(userId)) {
//                        ArrayList<String> values = user.getSharing2().get(userId);
//                        if (!values.contains(sharedFileName)) {
//                            values.add(sharedFileName);
//                        }
//                    } else {
//                        ArrayList<String> newValues = new ArrayList<>();
//                        newValues.add(sharedFileName);
//                        user.getSharing2().put(userId, newValues);
//                    }

//
//                    if (requestedUser.getSharing().containsKey(sharedFileName)) {
//                        ArrayList<String> values = requestedUser.getSharing().get(sharedFileName);
//                        if (!values.contains(user.getId())) {
//                            values.add(user.getId());
//                        }
//                    } else {
//                        ArrayList<String> newValues = new ArrayList<>();
//                        newValues.add(user.getId());
//                        requestedUser.getSharing().put(sharedFileName, newValues);
//                    }
//
//                    if (requestedUser.getSharing2().containsKey(sharedFileName)) {
//                        ArrayList<String> values = requestedUser.getSharing2().get(user.getId());
//                        if (!values.contains(sharedFileName)) {
//                            values.add(sharedFileName);
//                        }
//                    } else {
//                        ArrayList<String> newValues = new ArrayList<>();
//                        newValues.add(sharedFileName);
//                        requestedUser.getSharing2().put(user.getId(), newValues);
//                    }


                    try {
                        dis = new DataInputStream(socket.getInputStream());
                        dos = new DataOutputStream(socket.getOutputStream());
                        try {
                            String tmp = dis.readUTF();
                            String[] pars = tmp.split(":");
                            String requested = pars[0];
                            String fileName = pars[1];

                            String directoryPath = "/Users/chokyunghyuk/testtest4/src/Server/Files/SharedFiles/"+user.getId();
                            Path directory = Paths.get(directoryPath);
                            Files.createDirectories(directory);

                            if (user.getSharing().containsKey(fileName)) {
                                String value = user.getSharing().get(fileName);
                                if (!value.equals(requested)) {
                                    value = requested;
                                }
                            } else {
                                String newValue = "";
                                newValue = requested;
                                user.getSharing().put(fileName, newValue);
                            }


                            User requestedUser = ServerController.findUserById(requested);
                            File copyFile1 = new File(directory + "/" + fileName);
                            long fileTransferSize = dis.readLong();
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(copyFile1, false));

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

                            requestedUser.sendMessage("SHARING:"+user.getId()+" 님이 "+fileName+ "을 공유하셨습니다.");


                            if (bos != null) {
                                bos.close();
                            }

                            for (int k = 0; k < ServerController.serverThreads.size(); k++) {
                                if (ServerController.serverThreads.get(k).user.getId().equals(requested)) {
                                    ServerController.serverThreads.get(k).updateFile(user, fileName);
                                }
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

    public void tickTack() {
        int ControllerLogicalClock = ServerController.getMainLogicalClock();
        ControllerLogicalClock++;
        ServerController.setMainLogicalClock(ControllerLogicalClock);
        System.out.println("서버의 Logical Clock : "+ControllerLogicalClock);
    }

}
