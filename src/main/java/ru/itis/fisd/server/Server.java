package ru.itis.fisd.server;

import javafx.scene.paint.Color;
import lombok.Getter;
import ru.itis.fisd.app.GameLogic;
import ru.itis.fisd.app.GameState;
import ru.itis.fisd.entity.Card;
import ru.itis.fisd.entity.CardColor;
import ru.itis.fisd.entity.Deck;
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

@Getter
public class Server {

    public static int SERVER_PORT = 50000;
    private static boolean isRunning = false;
    private static final List<Socket> clients = new ArrayList<>();
    private static final Deck deck = new Deck();
    private static final GameLogic gameLogic = new GameLogic();

    public static void main(String[] args) {
        gameLogic.setDeck(deck);
        if (isRunning) {
            System.out.println("Server is already running");
            return;
        }

        if (args.length > 0) {
            SERVER_PORT = Integer.parseInt(args[0]) > 0 ? Integer.parseInt(args[0]) : SERVER_PORT;
        }

        System.out.println("Starting server...");

        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            isRunning = true;
            System.out.println("Server started on port " + SERVER_PORT);

            while (isRunning) {
                System.out.println("Waiting for client connection...");
                Socket clientSocket = server.accept();

                synchronized (clients) {
                    clients.add(clientSocket);
                }

                System.out.println("Client connected: " + clientSocket);
                printClientList();

                new Thread(() -> handleClientMessages(clientSocket, server)).start();
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            isRunning = false;
            System.out.println("Server is shutting down...");
        }
    }

    private static void handleClientMessages(Socket clientSocket, ServerSocket serverSocket) {
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
                        handleCardMove(message);
                    } else if (protocol.type().equals(ProtocolType.CLOSE)) {
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
                            System.out.println("Client disconnected: " + clientSocket);
                            printClientList();
                        }
                    } else if (protocol.type().equals(ProtocolType.GET)) {
                        for (Socket client : clients) {
                            System.out.println("DECK SIZE: " + deck.getCards().size());
                            Card card = deck.getCards().remove();
                            String a = card.value() + ":" + card.color();
                            if (client.toString().equals(message)) {
                                OutputStream writer = client.getOutputStream();
                                Protocol msg = new Protocol(ProtocolType.GET, a);
                                writer.write(Converter.encode(msg));
                                writer.flush();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Client connection error: " + e.getMessage());

            try {
                clientSocket.close();
            } catch (IOException ee) {
                System.out.println("Error closing client socket: " + ee.getMessage());
            }
        }
    }

    private static void handleCardMove(String message) {
        String[] parts = message.split(":");
        String value = parts[1].split("&")[0];
        String color = parts[1].split("&")[1];
        CardColor cardColor = switch (color) {
            case "0xff0000ff" -> CardColor.RED;
            case "0x0000ffff" -> CardColor.BLUE;
            case "0xffff00ff" -> CardColor.YELLOW;
            case "0x008000ff" -> CardColor.GREEN;
            default -> throw new IllegalStateException("Unexpected value: " + color);
        };
        Card playerCard = new Card(Integer.parseInt(value), CardColor.valueOf(String.valueOf(cardColor)));
        boolean result = gameLogic.handleMove(playerCard, GameState.currentCard);
        if (result) {
            System.out.println("DECK BEFORE: " + deck.getCards().size());
            deck.getCards().add(playerCard);
            System.out.println("DECK AFTER: " + deck.getCards().size());
            broadcastCardState(message);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            deleteCard((GameState.order == 1) ? 2 : 1, playerCard);
        } else {
            GameState.order = (GameState.order == 1) ? 2 : 1;
            GameState.isStart = !GameState.isStart;
            broadcastGameState();
            System.out.println("WRONG MOVE!!!");
        }
    }

    private static void deleteCard(int order, Card playerCard) {
        String msg = order + ":" + playerCard.value() + ":" + playerCard.color();
        System.out.println("DELETING CARD");
        synchronized (clients) {
            for (Socket client : clients) {
                try {
                    if (!client.isClosed()) {
                        OutputStream writer = client.getOutputStream();
                        Protocol message = new Protocol(ProtocolType.DELETE, msg);
                        writer.write(Converter.encode(message));
                        writer.flush();
                    }
                } catch (IOException e) {
                    System.out.println("Error sending game state to client: " + e.getMessage());
                }
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

    private static void printClientList() {
        synchronized (clients) {
            System.out.println("Currently connected clients (" + clients.size() + "):");
            for (Socket client : clients) {
                System.out.println("- " + client);
            }
            try {
                if (clients.size() == 1) {
                    for (Socket client : clients) {
                        if (!client.isClosed()) {
                            OutputStream writer = client.getOutputStream();
                            Protocol message = new Protocol(ProtocolType.UI, "wait");
                            writer.write(Converter.encode(message));
                        }
                    }
                } else if (clients.size() == 2) {
                    for (Socket client : clients) {
                        if (!client.isClosed()) {
                            OutputStream writer = client.getOutputStream();
                            Protocol message = new Protocol(ProtocolType.UI, "start");
                            writer.write(Converter.encode(message));

                            Thread.sleep(100);
                            StringBuilder cardInfo = new StringBuilder();
                            for (int i = 0; i < 7; i++) {
                                Card card = deck.getCards().remove();
                                if (cardInfo.isEmpty()){
                                    cardInfo.append(card.value()).append(":").append(card.color());
                                } else {
                                    cardInfo.append("&").append(card.value()).append(":").append(card.color());
                                }
                            }
                            Protocol cardMessage = new Protocol(ProtocolType.GET, cardInfo.toString());
                            writer.write(Converter.encode(cardMessage));
                            writer.flush();
                            Thread.sleep(5);
                        }


                    }
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
