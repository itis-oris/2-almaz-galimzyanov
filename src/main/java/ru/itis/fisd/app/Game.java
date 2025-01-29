package ru.itis.fisd.app;

import javafx.application.Application;
import lombok.Getter;
import ru.itis.fisd.client.Client;
import ru.itis.fisd.client.gui.MainFX;
import ru.itis.fisd.server.Server;

import java.io.IOException;

@Getter
public class Game {

    private static int SERVER_PORT = 50000;
    private static MainFX mainFX;
    public static Client client;

    public Game(int port) throws IOException {

        boolean isSuccess = false;
        SERVER_PORT = port > 0 ? port : SERVER_PORT;

        try {
            clientConnection(2);
            isSuccess = true;
        } catch (Exception e) {
            serverStart();
        } finally {
            if (!isSuccess) {
                clientConnection(1);
            }
        }
    }

    private static void serverStart() {
        new Thread(() -> Server.main(new String[]{String.valueOf(SERVER_PORT)})).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException er) {
            Thread.currentThread().interrupt();
        }
    }

    private static void clientConnection(int order) throws IOException {
        client = new Client();
        client.connectToServer("localhost", SERVER_PORT);
        client.setOrder(order);
        mainFX.setClientSocket(client.getSocket());
        mainFX.todo();
    }

    public static void main(String[] args) {
        mainFX = new MainFX();
        Application.launch(MainFX.class, args);
    }
}
