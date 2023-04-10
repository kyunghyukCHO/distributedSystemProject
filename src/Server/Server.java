package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private List<ClientHandler> clients = new ArrayList<>();

    public void start(int port) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("=============================");
            System.out.println("Server started on port " + port);
            System.out.println("=============================");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client is trying connection from " + socket.getInetAddress().getHostAddress()+"....");
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                pool.submit(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Client disconnected: " + clientHandler.getClientName());
        broadcast("Client " + clientHandler.getClientName() + " disconnected");
        broadcastClientList();
    }

    public void broadcastClientList() {
        String clientList = "Clients online: ";
        for (ClientHandler client : clients) {
            clientList += client.getClientName() + ", ";
        }
        broadcast(clientList.substring(0, clientList.length() - 2));
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(1234);
    }





    private class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader reader;
        private BufferedWriter writer;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public String getClientName() {
            return clientName;
        }

        public void sendMessage(String message) {
            try {
                writer.write(message + "\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                clientName = reader.readLine();
                System.out.println("Client name : " + clientName + "    //    PORT NUMBER : "+socket.getPort()+"    //    IP ADDRESS : "+socket.getInetAddress());
                System.out.println("CLIENT '"+clientName+"' CONNECTION THREAD COMPLETED");

                broadcast("Client " + clientName + " connected");
                broadcastClientList();

                String input;
                while ((input = reader.readLine()) != null) {
                    System.out.println("Received message from " + clientName + ": " + input);
                    broadcast(clientName + ": " + input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    reader.close();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                removeClient(this);
            }
        }
    }
}
