package Client2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserController userController = new UserController();

        System.out.print("아이디를 입력하세요 : ");
        String userId = scanner.nextLine();
        System.out.print("IP를 입력하세요 : ");
        String serverIp = scanner.nextLine();

        userController.setUserId(userId);
        userController.setIp(serverIp);
        userController.setPort(8888);
        userController.start();

    }
}