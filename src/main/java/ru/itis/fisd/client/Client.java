package ru.itis.fisd.client;

import javafx.application.Application;
import lombok.Getter;
import lombok.Setter;
import ru.itis.fisd.client.gui.MainFX;

import java.io.IOException;
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

            // Отправка тестового сообщения на сервер
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("Hello from client!".getBytes());
            outputStream.flush();

        } catch (IOException e) {
            System.out.println("Failed to connect to server: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) {
        Application.launch(MainFX.class, args);
    }
}
