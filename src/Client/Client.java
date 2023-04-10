package Client;

import java.io.*;
import java.net.Socket;

public class Client {
    private String name;
    private BufferedReader reader;
    private BufferedWriter writer;

    public void start(String hostname, int port) {
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to server : " + socket.getInetAddress());

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            System.out.print("Enter your name : ");
            name = new BufferedReader(new InputStreamReader(System.in)).readLine();
            writer.write(name + "\n");
            writer.flush();

            new Thread(new ServerListener()).start();
            new Thread(new InputListener()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                String input;
                while ((input = reader.readLine()) != null) {
                    System.out.println(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class InputListener implements Runnable {
        @Override
        public void run() {
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                String input;
                while ((input = inputReader.readLine()) != null) {
                    writer.write(input + "\n");
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start("localhost", 1234);
    }
}
