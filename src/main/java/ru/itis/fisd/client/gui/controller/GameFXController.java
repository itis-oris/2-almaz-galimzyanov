package ru.itis.fisd.client.gui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import ru.itis.fisd.app.Game;
import ru.itis.fisd.entity.Card;
import ru.itis.fisd.entity.CardColor;
import ru.itis.fisd.entity.Deck;
import ru.itis.fisd.entity.Player;
import ru.itis.fisd.listener.ButtonActionListener;
import ru.itis.fisd.listener.GameActionListener;

import java.util.Random;

public class GameFXController {

    @Getter
    private static GameFXController instance;

    public GameFXController() {
        instance = this;
    }

    @FXML
    private HBox buttonRow; // Контейнер для строки с кнопками

    @FXML
    private Button coloda;

    @FXML
    private Label current;

    private static GameActionListener buttonActionListener;


    @FXML
    public void initialize() {
        buttonActionListener = new GameActionListener(this); // Передаём текущий контроллер

        int totalButtons = 7;
        Player player = new Player();

        coloda.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        current.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        current.setFont(Font.font("Arial", FontWeight.BOLD, 128));
        current.setStyle("-fx-font-size: 400%;");
        current.setTextFill(Color.WHITE);
        current.setPrefWidth(80);
        current.setPrefHeight(150);
        for (int i = 0; i < totalButtons; i++) {
            Card card = getRandomCard();
            player.getPlayerCards().add(card);
            Button button = getButton(card, player);
            buttonRow.getChildren().add(button);
        }

    }


    private static Button getButton(Card card, Player player) {
        Button button = new Button(String.valueOf(card.value()));

        button.setFont(Font.font("Arial", FontWeight.BOLD, 128));
        button.setTextFill(Color.WHITE);

        button.setStyle("-fx-font-size: 500%;");

        button.setPrefWidth(80);
        button.setPrefHeight(150);
        BackgroundFill background_fill = new BackgroundFill(card.color().getColor(), CornerRadii.EMPTY, Insets.EMPTY);

        button.setBackground(new Background(background_fill));

        button.setOnAction(_ -> {
            System.out.println("Нажата кнопка: " + button.getText());
            buttonActionListener.handleButton(button, player);
        });
        return button;
    }

    private Card getRandomCard() {
        return Deck.cards.remove();
    }

    public void setDeckCard(String value, Paint color) {

        if (current != null) {
            Platform.runLater(() -> {
                current.setText(value);
                current.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
            });
        }
    }
}
