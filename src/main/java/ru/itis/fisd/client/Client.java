package ru.itis.fisd.client;

import javafx.scene.paint.Paint;
import lombok.Getter;
import lombok.Setter;
import ru.itis.fisd.app.GameState;
import ru.itis.fisd.client.gui.controller.GameFXController;
import ru.itis.fisd.client.gui.controller.SceneController;
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
    private int order;
    private Player player;

    public void connectToServer(String host, int port) throws IOException {
        try {
            socket = new Socket(host, port);
            System.out.println("Client connected to server: " + host + ":" + port);

            new Thread(() -> {
                try (OutputStream outputStream = socket.getOutputStream()) {
                    while (!socket.isClosed()) {
                        Protocol protocol = new Protocol(ProtocolType.PING, "ping");
                        byte[] message = Converter.encode(protocol);
                        outputStream.write(message);
                        outputStream.flush();
                        Thread.sleep(5000); // Отправляем "пинг" каждые 5 секунд
                    }
                } catch (IOException | InterruptedException e) {
                    System.out.println("Ping error: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }).start();


            new Thread(this::handleMessages).start();

        } catch (IOException e) {
            System.out.println("Failed to connect to server: " + e.getMessage());
            throw e;
        }
    }

    private void handleMessages() {
        try (InputStream input = socket.getInputStream()) {
            byte[] buffer = new byte[1024];
            while (!socket.isClosed()) {
                int res = input.read(buffer);
                if (res != -1) {
                    Protocol protocol = Converter.decode(buffer);
                    String message = protocol.body();
                    System.out.println("Received from server: " + message);

                    if (message.startsWith("updateState")) {
                        // Разбираем новое состояние, например: "updateState:order=2,isStart=true"
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
                        GameFXController.getInstance().setDeckCard(value, Paint.valueOf(color));
                    }
                    else {
                        switch (message) {
                            case "start" -> SceneController.activate("game");
                            case "wait" -> {
                                System.out.println("Waiting for message...");
                                SceneController.activate("waiting_room");
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    // Новый метод для отправки сообщений на сервер
    public void sendMessage(Protocol message) {
        try {
            OutputStream output = socket.getOutputStream();
            // Кодируем сообщение в Protocol, если нужно
            byte[] encodedMessage = Converter.encode(message);

            output.write(encodedMessage);  // Отправляем данные
            output.flush();  // Обязательно сбрасываем поток для отправки данных
            System.out.println("Message sent to server: " + message);
        } catch (IOException e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }
}
