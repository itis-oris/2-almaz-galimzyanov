package ru.itis.fisd.app;

import javafx.application.Application;
import lombok.Getter;
import ru.itis.fisd.client.Client;
import ru.itis.fisd.client.gui.MainFX;
import ru.itis.fisd.server.Server;

import java.io.IOException;

@Getter
public class Game {

    private static final int SERVER_PORT = 50000;
    private static MainFX mainFX;
    public static Client client;

    public Game() throws IOException {

        boolean isSuccess = false;
        try {

            client = new Client();
            client.connectToServer("localhost", SERVER_PORT);
            client.setOrder(2);

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
                client = new Client();
                client.connectToServer("localhost", SERVER_PORT);
                client.setOrder(1);

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
