package ru.itis.fisd.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static final int SERVER_PORT = 50000;
    private static boolean isRunning = true;
    private static final List<Socket> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Starting server");

        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started");

            while (isRunning) {
                System.out.println("Waiting for client connection...");
                Socket clientSocket = server.accept();

                synchronized (clients) {
                    clients.add(clientSocket);
                }

                System.out.println("Client connected: " + clientSocket);
                printClientList();

                new Thread(() -> handleClient(clientSocket)).start();
            }

            System.out.println("Server is shutting down...");
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (InputStream input = clientSocket.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                String message = new String(buffer, 0, bytesRead);
                System.out.println("Received from client: " + message);

            }
        } catch (IOException e) {
            System.out.println("Client connection error: " + e.getMessage());
        } finally {
            synchronized (clients) {
                clients.remove(clientSocket);
            }
            System.out.println("Client disconnected: " + clientSocket);
            printClientList();

            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    private static void printClientList() {
        synchronized (clients) {
            System.out.println("Currently connected clients (" + clients.size() + "):");
            for (Socket client : clients) {
                System.out.println("- " + client);
            }
        }
    }
}
