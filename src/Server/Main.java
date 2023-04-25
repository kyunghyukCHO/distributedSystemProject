package Server;

import Server.UI.MainDesign;

public class Main {
    public static void main(String[] args) {
        ServerController server = new ServerController();
        MainDesign design = new MainDesign();
        design.makeFrame();
        server.setEndButton(design.getButton());
        server.setJoinField(design.getJoinField());
        server.setScreen(design.getScreen());
        server.setFileScreen(design.getFileScreen());
        server.setPort(8888);
        server.start();
    }
}