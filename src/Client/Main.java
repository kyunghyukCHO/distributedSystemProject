package Client;

import Client.UI.MainDesign;
import Client.UI.initDesign;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String id=null,ip=null,name=null;

        ArrayList<String> fileNames = new ArrayList<String>();
        boolean flag = false;

        Controller Socket = new Controller(); // Controller 생성
        MainDesign design = new MainDesign(); // 메인 UI 생성

        // 초기 접속 UI 생성
        initDesign init = new initDesign();
        init.makeFrame();
        do {
            id = init.getID();
            ip = init.getIP();
            name = init.getName();
            System.out.println("");
        }while(id==null || ip==null || name==null);

        // Controller 설정 IP, PORT, ID, NAME
        Socket.setIP(ip);
        Socket.setPort(8888);
        Socket.setId(id);
        Socket.setName(name);
        design.makeFrame(); // 메인 UI 의 makeFrame

        Socket.start(); // 연결 Start

        Socket.setConnectingScreen(design.getConnectingScreen());
        Socket.setScreen(design.getScreen()); // Controller 의 setScreen ( main design 의 getScreen )

        while (true) {
            flag = design.isFlag();
            if (!flag) {
                System.out.println(" ");
                continue;
            } else {
                Send.writer.println("FILE");
                Send.writer.flush();
                Socket.setFileNames(design.getFileNames());
                break;
            }
        }
    }
}