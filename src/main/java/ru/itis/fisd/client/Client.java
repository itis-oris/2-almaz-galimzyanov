package ru.itis.fisd.client;

import javafx.application.Application;
import ru.itis.fisd.client.gui.MainFX;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {

//        MainFX.setClientSocket(clientSocket);
        //            Socket clientSocket = new Socket("localhost", 50000);
        System.out.println("From client: connected");


        Application.launch(MainFX.class, args);
    }
}
