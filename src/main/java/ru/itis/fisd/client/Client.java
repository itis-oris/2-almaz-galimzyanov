package ru.itis.fisd.client;

import javafx.application.Application;
import lombok.Getter;
import lombok.Setter;
import ru.itis.fisd.client.gui.MainFX;
import ru.itis.fisd.client.gui.controller.SceneController;
import ru.itis.fisd.protocol.Converter;
import ru.itis.fisd.protocol.Protocol;
import ru.itis.fisd.protocol.ProtocolType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@Getter
@Setter
public class Client {

    private Socket socket;

    public void connectToServer(String host, int port) throws IOException {
        try {
            socket = new Socket(host, port);
            System.out.println("Client connected to server: " + host + ":" + port);

            // Создаем поток для отправки пинг-сообщений
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
            while (input.read(buffer) != -1) {
                Protocol protocol = Converter.decode(buffer);
                String message = protocol.body();
                System.out.println("Received from server: " + message);


                switch (message) {
                    case "start" -> SceneController.activate("game");
                    case "wait" -> {
                        System.out.println("Waiting for message...sdsfsdfsdfsdfds");
                        SceneController.activate("waiting_room");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Application.launch(MainFX.class, args);
    }
}
