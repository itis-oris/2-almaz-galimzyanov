package ru.itis.fisd.app;

import javafx.application.Application;
import ru.itis.fisd.client.Client;
import ru.itis.fisd.client.gui.MainFX;
import ru.itis.fisd.server.Server;

import java.io.IOException;

public class Game {

    private static final int SERVER_PORT = 50000;
    private static MainFX mainFX;

    public Game() throws IOException {

        boolean isSuccess = false;
        try {

            Client client = new Client();
            client.connectToServer("localhost", SERVER_PORT);

            mainFX.setClientSocket(client.getSocket());
            mainFX.todo();
            System.out.println("SAODKASODKDO" + mainFX.getClientSocket());
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

                mainFX.setClientSocket(client.getSocket());
                mainFX.todo();

                System.out.println("SAODKASODKDO" + mainFX.getClientSocket());
            }

        }


    }

    public static void main(String[] args) {
        mainFX = new MainFX();
        Application.launch(MainFX.class, args);
    }
}
