package ru.itis.fisd.client.gui.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ru.itis.fisd.entity.Card;
import ru.itis.fisd.entity.CardColor;
import ru.itis.fisd.entity.Deck;

import java.util.Random;

public class GameFXController {

    @FXML
    private HBox buttonRow; // Контейнер для строки с кнопками

    @FXML
    private Button coloda;

    @FXML
    private Label current;


    @FXML
    public void initialize() {

        int totalButtons = 7; // Пример: 50 кнопок

        // Set green background for the button
        coloda.setBackground(new Background(new BackgroundFill(
                Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        // Set light blue background for the label
        current.setBackground(new Background(new BackgroundFill(
                Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        for (int i = 0; i < totalButtons; i++) {
            // Создаем кнопку
            Random rand = new Random();
            CardColor color = CardColor.values()[rand.nextInt(CardColor.values().length)];
            Card card = getRandomCard();
            Button button = getButton(card);

            // Добавляем кнопку в строку
            buttonRow.getChildren().add(button);
        }
    }

    private static Button getButton(Card card) {
        Button button = new Button(String.valueOf(card.value()));

        button.setFont(Font.font("Arial", FontWeight.BOLD, 128));
        button.setTextFill(Color.WHITE);

        button.setStyle("-fx-font-size: 500%;");

        button.setPrefWidth(80);
        button.setPrefHeight(500);
        BackgroundFill background_fill = new BackgroundFill(card.color().getColor(), CornerRadii.EMPTY, Insets.EMPTY);

        button.setBackground(new Background(background_fill));

        button.setOnAction(_ -> System.out.println("Нажата кнопка: " + button.getText()));
        return button;
    }

    private Card getRandomCard() {
        return Deck.cards.remove();
    }
}
