package ru.itis.fisd.listener;

import javafx.application.Platform;
import javafx.scene.control.Button;
import ru.itis.fisd.app.Game;
import ru.itis.fisd.app.GameState;
import ru.itis.fisd.client.Client;
import ru.itis.fisd.client.gui.controller.GameFXController;
import ru.itis.fisd.entity.Player;
import ru.itis.fisd.protocol.Protocol;
import ru.itis.fisd.protocol.ProtocolType;

public class GameActionListener {

    private final GameFXController gameFXController;

    public GameActionListener(GameFXController gameFXController) {
        this.gameFXController = gameFXController;
    }


    public void handleButton(Button button, Player player) {
        Client client = Game.client;
        if (client.getPlayer() == null) {
            client.setPlayer(player);
        }
        System.out.println(client + " " + client.getSocket());
        System.out.println(button.getText() + " " + button.getBackground().getFills().getFirst().getFill());
        System.out.println("ORDER: " + client.getOrder());
        System.out.println("STATUS " + GameState.isStart + " " + GameState.order);

        if (!GameState.isStart) {
            if (client.getOrder() == GameState.order) {
                gameFXController.setDeckCard(button.getText(),
                        button.getBackground().getFills().getFirst().getFill());

                // Отправляем серверу не только действие, но и запрос на обновление состояния игры
                client.sendMessage(new Protocol(ProtocolType.GAME, "move:" + client.getOrder() + ":" + button.getText()));
                client.sendMessage(new Protocol(ProtocolType.GAME, "card:" + button.getText() + "&" + button.getBackground().getFills().getFirst().getFill()));
            }
        } else {
            if (client.getOrder() == GameState.order) {
                gameFXController.setDeckCard(button.getText(),
                        button.getBackground().getFills().getFirst().getFill());

                // Отправляем серверу не только действие, но и запрос на обновление состояния игры
                client.sendMessage(new Protocol(ProtocolType.GAME, "move:" + client.getOrder() + ":" + button.getText()));
                client.sendMessage(new Protocol(ProtocolType.GAME, "card:" + button.getText() + "&" + button.getBackground().getFills().getFirst().getFill()));
            }
        }
    }

}
