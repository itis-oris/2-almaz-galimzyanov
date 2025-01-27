package ru.itis.fisd.server;

import lombok.Getter;
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
        threadPool.execute(() -> {
            try (InputStream input = clientSocket.getInputStream()) {
                byte[] buffer = new byte[1024];
                while (input.read(buffer) != -1) {
                    Protocol protocol = Converter.decode(buffer);
                    String message = protocol.body();
                    System.out.println("Received from client: " + message);
                }
            } catch (IOException e) {
                System.out.println("Client connection error: " + e.getMessage());
            } finally {
                synchronized (clients) {
                    clients.remove(clientSocket);
                    if (clients.isEmpty()) {
                        System.out.println("No clients connected");
                        isRunning = false;
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
                System.out.println("Client disconnected: " + clientSocket);
                printClientList(null);

                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("Error closing client socket: " + e.getMessage());
                }
            }
        });
    }


    private static void printClientList(ServerSocket server) {
        synchronized (clients) {
            System.out.println("Currently connected clients (" + clients.size() + "):");
            for (Socket client : clients) {
                System.out.println("- " + client);
            }

            try {
                if (clients.size() == 1) {
                    for (Socket client : clients) {
                        OutputStream writer = client.getOutputStream();
                        Protocol message = new Protocol(ProtocolType.INFO, "wait");
                        writer.write(Converter.encode(message));
                    }
                } else if (clients.size() == 2) {
                    for (Socket client : clients) {
                        OutputStream writer = client.getOutputStream();
                        Protocol message = new Protocol(ProtocolType.INFO, "start");
                        writer.write(Converter.encode(message));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
