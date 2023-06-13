package Server;

public class Main {
    public static void main(String[] args) {
        ServerController serverController = new ServerController();
        serverController.setPort(8888);
        serverController.start();
    }
}