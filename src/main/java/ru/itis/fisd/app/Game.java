package ru.itis.fisd.app;

import ru.itis.fisd.client.Client;
import ru.itis.fisd.server.Server;

import java.io.IOException;

public class Game {

    private static final int SERVER_PORT = 50000;
    private boolean isSuccess = false;

    public Game() throws IOException {

        try {

            Client client = new Client();
            client.connectToServer("localhost", SERVER_PORT);
            isSuccess = true;
        } catch (Exception e) {
            new Thread(() -> Server.main(new String[]{String.valueOf(SERVER_PORT)})).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException er) {
                Thread.currentThread().interrupt();
            }
        } finally {
            if (!isSuccess) {
                Client client = new Client();
                client.connectToServer("localhost", SERVER_PORT);
            }
        }


    }
}
