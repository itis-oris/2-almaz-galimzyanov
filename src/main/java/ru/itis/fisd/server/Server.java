package ru.itis.fisd.server;

import lombok.Getter;
import ru.itis.fisd.app.GameState;
import ru.itis.fisd.protocol.Converter;
import ru.itis.fisd.protocol.Protocol;
import ru.itis.fisd.protocol.ProtocolType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class Server {

    public static final int SERVER_PORT = 50000;
    private static boolean isRunning = false;
    private static final List<Socket> clients = new ArrayList<>();
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        if (isRunning) {
            System.out.println("Server is already running");
            return;
        }

        System.out.println("Starting server...");
        isRunning = true;

        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started on port " + SERVER_PORT);

            while (isRunning) {
                System.out.println("Waiting for client connection...");
                Socket clientSocket = server.accept();

                synchronized (clients) {
                    clients.add(clientSocket);
                }

                System.out.println("Client connected: " + clientSocket);
                printClientList(server);

                new Thread(() -> handleClient(clientSocket, server)).start();
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            isRunning = false;
            System.out.println("Server is shutting down...");
        }
    }

    private static void handleClient(Socket clientSocket, ServerSocket serverSocket) {
        boolean res = true;
        try (InputStream input = clientSocket.getInputStream()) {
            byte[] buffer = new byte[1024];
            while (true) {
                if (input.read(buffer) != -1) {
                    Protocol protocol = Converter.decode(buffer);
                    String message = protocol.body();
                    System.out.println("Received from client: " + message);

                    System.out.println(message);
                    if (message.startsWith("move:")) {
                        handleMove(message);
                        broadcastGameState();
                    } else if (message.startsWith("card:")) {
                        broadcastCardState(message);
                    }
                }

            }
        } catch (IOException e) {
            res = false;
            System.out.println("Client connection error: " + e.getMessage());

            synchronized (clients) {
                clients.remove(clientSocket);
                if (clients.isEmpty()) {
                    System.out.println("No clients connected");
                    isRunning = false;
                    try {
                        serverSocket.close();
                    } catch (IOException er) {
                        throw new RuntimeException(er);
                    }
                }
            }

            System.out.println("Client disconnected: " + clientSocket);
            printClientList(null);

            try {
                clientSocket.close();
            } catch (IOException ee) {
                System.out.println("Error closing client socket: " + ee.getMessage());
            }
        }
    }




    private static void handleMove(String message) {
        String[] parts = message.split(":");
        int playerOrder = Integer.parseInt(parts[1]);
        String move = parts[2];

        System.out.println("Player " + playerOrder + " made move: " + move);

        GameState.order = (playerOrder == 1) ? 2 : 1;
        GameState.isStart = !GameState.isStart;
    }

    private static void broadcastGameState() {
        String updateMessage = "updateState:order=" + GameState.order + ",isStart=" + GameState.isStart;
        synchronized (clients) {
            for (Socket client : clients) {
                try {
                    if (!client.isClosed()) {
                        OutputStream writer = client.getOutputStream();
                        Protocol message = new Protocol(ProtocolType.INFO, updateMessage);
                        writer.write(Converter.encode(message));
                        writer.flush();
                    }
                } catch (IOException e) {
                    System.out.println("Error sending game state to client: " + e.getMessage());
                }
            }
        }
    }

    private static void broadcastCardState(String msg) {
        synchronized (clients) {
            for (Socket client : clients) {
                try {
                    if (!client.isClosed()) {
                        OutputStream writer = client.getOutputStream();
                        Protocol message = new Protocol(ProtocolType.GAME, msg);
                        writer.write(Converter.encode(message));
                        writer.flush();
                    }
                } catch (IOException e) {
                    System.out.println("Error sending game card to client: " + e.getMessage());
                }
            }
        }
    }



    private static void printClientList(ServerSocket server) {
        synchronized (clients) {
            System.out.println("Currently connected clients (" + clients.size() + "):");
            for (Socket client : clients) {
                System.out.println("- " + client);
            }

            try {
                // Проверяем, что сокет открыт
                if (clients.size() == 1) {
                    for (Socket client : clients) {
                        if (!client.isClosed()) {  // Проверка на закрытость сокета
                            OutputStream writer = client.getOutputStream();
                            Protocol message = new Protocol(ProtocolType.INFO, "wait");
                            writer.write(Converter.encode(message));
                        }
                    }
                } else if (clients.size() == 2) {
                    for (Socket client : clients) {
                        if (!client.isClosed()) {  // Проверка на закрытость сокета
                            OutputStream writer = client.getOutputStream();
                            Protocol message = new Protocol(ProtocolType.INFO, "start");
                            writer.write(Converter.encode(message));
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
