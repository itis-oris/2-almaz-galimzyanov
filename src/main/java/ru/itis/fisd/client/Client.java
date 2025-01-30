package ru.itis.fisd.client;

import javafx.application.Platform;
import javafx.scene.paint.Paint;
import lombok.Getter;
import lombok.Setter;
import ru.itis.fisd.app.GameState;
import ru.itis.fisd.client.gui.controller.GameFXController;
import ru.itis.fisd.client.gui.controller.SceneController;
import ru.itis.fisd.entity.Card;
import ru.itis.fisd.entity.CardColor;
import ru.itis.fisd.entity.Deck;
import ru.itis.fisd.entity.Player;
import ru.itis.fisd.protocol.Converter;
import ru.itis.fisd.protocol.Protocol;
import ru.itis.fisd.protocol.ProtocolType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

@Getter
@Setter
public class Client {

    private Socket socket;
    private Deck deck;
    private Player player;
    private int order;

    public void connectToServer(String host, int port) throws IOException {
        try {
            System.out.println("Connecting...");
            socket = new Socket(host, port);
            player = new Player();
            System.out.println("Client connected to server: " + host + ":" + port);

            new Thread(this::handleServerMessages).start();

        } catch (IOException e) {
            System.out.println("Failed to connect to server: " + e.getMessage());
            throw e;
        }
    }

    private void handleServerMessages() {
        try (InputStream input = socket.getInputStream()) {
            byte[] buffer = new byte[1024];
            while (!socket.isClosed()) {
                int res = input.read(buffer);
                if (res != -1) {
                    Protocol protocol = Converter.decode(buffer);
                    String message = protocol.body();
                    System.out.println("Received from server: " + message);

                    if (message.startsWith("updateState")) {
                        String[] parts = message.split(":")[1].split(",");
                        for (String part : parts) {
                            String[] keyValue = part.split("=");
                            switch (keyValue[0]) {
                                case "order" -> GameState.order = Integer.parseInt(keyValue[1]);
                                case "isStart" -> GameState.isStart = Boolean.parseBoolean(keyValue[1]);
                            }
                        }
                        System.out.println("Updated GameState: order=" + GameState.order + ", isStart=" + GameState.isStart);
                    } else if (message.startsWith("card")) {
                        System.out.println(message);
                        System.out.println(Arrays.toString(message.split(":")));
                        String[] parts = message.split(":")[1].split("&");
                        System.out.println(Arrays.toString(parts));
                        String value = parts[0];
                        String color = parts[1];
                        CardColor cardColor = switch (color) {
                            case "0xff0000ff" -> CardColor.RED;
                            case "0x0000ffff" -> CardColor.BLUE;
                            case "0xffff00ff" -> CardColor.YELLOW;
                            case "0x008000ff" -> CardColor.GREEN;
                            default -> throw new IllegalStateException("Unexpected value: " + color);
                        };
                        GameState.currentCard = new Card(Integer.parseInt(value), CardColor.valueOf(String.valueOf(cardColor)));
                        GameFXController.getInstance().setDeckCard(value, Paint.valueOf(color));
                    } else if (protocol.type().equals(ProtocolType.UI)) {
                        switch (message) {
                            case "start" -> {
                                GameFXController.getInstance().setPlayer(player);
                                SceneController.activate("game");
                            }
                            case "wait" -> SceneController.activate("waiting_room");
                        }
                    } else if (protocol.type().equals(ProtocolType.GET)) {
                        String[] parts = message.split("&");
                        for (String part : parts) {
                            String[] values = part.split(":");
                            Card card = new Card(Integer.parseInt(values[0]), CardColor.valueOf(values[1]));
                            player.getPlayerCards().add(card);
                            System.out.println(player.getPlayerCards());
                        }
                        Platform.runLater(() -> GameFXController.getInstance().updatePlayerCards(player.getPlayerCards()));
                    } else if (protocol.type().equals(ProtocolType.DELETE)) {
                        String[] parts = message.split(":");
                        if (Integer.parseInt(parts[0]) == order) {
                            CardColor cardColor = switch (parts[2]) {
                                case "RED" -> CardColor.RED;
                                case "BLUE" -> CardColor.BLUE;
                                case "YELLOW" -> CardColor.YELLOW;
                                case "GREEN" -> CardColor.GREEN;
                                default -> throw new IllegalStateException("Unexpected value: " + parts[2]);
                            };
                            Card card = new Card(Integer.parseInt(parts[1]), cardColor);
                            System.out.println("BEF " + player.getPlayerCards());
                            System.out.println("CARDDDDD " + card);
                            player.getPlayerCards().remove(card);
                            GameFXController.getInstance().removeCard(card);
                            System.out.println("AFT" + player.getPlayerCards());
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public void sendMessage(Protocol message) {
        try {
            OutputStream output = socket.getOutputStream();
            byte[] encodedMessage = Converter.encode(message);
            output.write(encodedMessage);
            output.flush();
            System.out.println("Message sent to server: " + message);
        } catch (IOException e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }
}
